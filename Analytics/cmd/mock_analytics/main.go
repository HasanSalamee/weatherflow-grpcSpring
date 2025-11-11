package main

import (
	"flag"
	"fmt"
	"io"
	"log"
	"net"
	"sync"

	"github.com/hasan/weather"
	"google.golang.org/grpc"
	"google.golang.org/grpc/metadata"
)

type server struct {
	weather.UnimplementedSensorServiceServer
	weather.UnimplementedAnalyticsServiceServer
	mu sync.Mutex

	totalReadings int64
	sumTemp       float64
	maxTemp       float64
	alertCount    int32

	statsSubscribers []chan *weather.WeatherStats
	alertSubscribers []chan *weather.AlertMessage
}

func (s *server) StreamSensorData(stream weather.SensorService_StreamSensorDataServer) error {
	md, _ := metadata.FromIncomingContext(stream.Context())
	if toks := md.Get("authorization"); len(toks) > 0 {
		log.Printf("received authorization metadata: %s", toks[0])
	}

	var batchCount int64
	var batchSumTemp float64
	var batchMaxTemp float64

	for {
		r, err := stream.Recv()
		if err == io.EOF {
			var ack weather.SensorAck
			if batchCount == 0 {
				ack = weather.SensorAck{Ok: false, Msg: "no readings received"}
			} else {
				avgT := batchSumTemp / float64(batchCount)
				ack = weather.SensorAck{Ok: true, Msg: fmt.Sprintf("received %d readings; avgT=%.2f", batchCount, avgT)}

				s.mu.Lock()
				s.totalReadings += batchCount
				s.sumTemp += batchSumTemp

				if batchMaxTemp > s.maxTemp {
					s.maxTemp = batchMaxTemp
				}

				globalAvgTemp := s.sumTemp / float64(s.totalReadings)

				stats := &weather.WeatherStats{
					AvgTemp:     float32(globalAvgTemp),
					MaxTemp:     float32(s.maxTemp),
					TotalAlerts: s.alertCount,
				}

				for _, ch := range s.statsSubscribers {
					select {
					case ch <- stats:
					default:
						// skip if channel is full
					}
				}
				s.mu.Unlock()

				if batchMaxTemp > 30 {
					s.mu.Lock()
					s.alertCount++
					alert := &weather.AlertMessage{
						City:        "Sensor Network",
						Temperature: float32(batchMaxTemp),
						Message:     fmt.Sprintf("High temperature warning: %.2f°C", batchMaxTemp),
					}

					for _, ch := range s.alertSubscribers {
						select {
						case ch <- alert:
						default:
							// skip if channel is full
						}
					}
					s.mu.Unlock()
				}
			}
			return stream.SendAndClose(&ack)
		}
		if err != nil {
			log.Printf("recv error: %v", err)
			return err
		}

		batchCount++
		temp := float64(r.Temperature)
		batchSumTemp += temp

		if temp > batchMaxTemp {
			batchMaxTemp = temp
		}

		log.Printf("recv reading from %s: t=%.2f h=%.2f p=%.2f ts=%d",
			r.SensorId, r.Temperature, r.Humidity, r.Pressure, r.Ts)
	}
}

func (s *server) SubscribeToStats(req *weather.ReportRequest, stream weather.AnalyticsService_SubscribeToStatsServer) error {
	statsChan := make(chan *weather.WeatherStats, 10)

	s.mu.Lock()
	s.statsSubscribers = append(s.statsSubscribers, statsChan)
	s.mu.Unlock()

	defer func() {
		s.mu.Lock()
		for i, ch := range s.statsSubscribers {
			if ch == statsChan {
				s.statsSubscribers = append(s.statsSubscribers[:i], s.statsSubscribers[i+1:]...)
				break
			}
		}
		s.mu.Unlock()
		close(statsChan)
	}()

	s.mu.Lock()
	if s.totalReadings > 0 {
		currentStats := &weather.WeatherStats{
			AvgTemp:     float32(s.sumTemp / float64(s.totalReadings)),
			MaxTemp:     float32(s.maxTemp),
			TotalAlerts: s.alertCount,
		}
		if err := stream.Send(currentStats); err != nil {
			s.mu.Unlock()
			return err
		}
	}
	s.mu.Unlock()

	for {
		select {
		case stats := <-statsChan:
			if err := stream.Send(stats); err != nil {
				return err
			}
		case <-stream.Context().Done():
			return stream.Context().Err()
		}
	}
}

func (s *server) SubscribeToAlerts(req *weather.Empty, stream weather.AnalyticsService_SubscribeToAlertsServer) error {
	alertChan := make(chan *weather.AlertMessage, 10)

	s.mu.Lock()
	s.alertSubscribers = append(s.alertSubscribers, alertChan)
	s.mu.Unlock()

	defer func() {
		s.mu.Lock()
		for i, ch := range s.alertSubscribers {
			if ch == alertChan {
				s.alertSubscribers = append(s.alertSubscribers[:i], s.alertSubscribers[i+1:]...)
				break
			}
		}
		s.mu.Unlock()
		close(alertChan)
	}()

	for {
		select {
		case alert := <-alertChan:
			if err := stream.Send(alert); err != nil {
				return err
			}
		case <-stream.Context().Done():
			return stream.Context().Err()
		}
	}
}

func main() {
	addr := flag.String("addr", ":50052", "listen address")
	flag.Parse()

	lis, err := net.Listen("tcp", *addr)
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}

	grpcServer := grpc.NewServer()
	s := &server{}

	weather.RegisterSensorServiceServer(grpcServer, s)
	weather.RegisterAnalyticsServiceServer(grpcServer, s)

	log.Printf("Server listening on %s (Supports both SensorService and AnalyticsService)", *addr)
	if err := grpcServer.Serve(lis); err != nil {
		log.Fatalf("server exited: %v", err)
	}
}

//To run this scrpit use the following command
//go run main.go -addr ":50053"
