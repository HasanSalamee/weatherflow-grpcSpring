package main

import (
    "context"
    "flag"
    "fmt"
    "log"
    "math/rand"
    "time"

    "github.com/hasan/weather"
    "google.golang.org/grpc"
    "google.golang.org/grpc/metadata"
    "google.golang.org/grpc/credentials/insecure"
)

func main() {
    addr := flag.String("addr", "localhost:50052", "analytics server address")
    token := flag.String("token", "test-token", "authorization token")
    rate := flag.Float64("rate", 2.0, "messages per second")
    count := flag.Int("count", 10, "number of readings to send (0 = infinite)")
    sensorID := flag.String("id", "sensor-1", "sensor id")
    flag.Parse()

    conn, err := grpc.Dial(*addr, grpc.WithTransportCredentials(insecure.NewCredentials()))
    if err != nil {
        log.Fatalf("failed to dial: %v", err)
    }
    defer conn.Close()

    client := weather.NewSensorServiceClient(conn)

    ctx := metadata.AppendToOutgoingContext(context.Background(), "authorization", fmt.Sprintf("Bearer %s", *token))
    stream, err := client.StreamSensorData(ctx)
    if err != nil {
        log.Fatalf("failed to open stream: %v", err)
    }

    ticker := time.NewTicker(time.Duration(1e3/ *rate) * time.Millisecond)
    defer ticker.Stop()

    sent := 0
    for {
        if *count > 0 && sent >= *count {
            break
        }
        // create a reading
        r := &weather.SensorReading{
            SensorId: *sensorID,
            Temperature: float32(20 + rand.Float32()*10),
            Humidity: float32(30 + rand.Float32()*20),
            Pressure: float32(990 + rand.Float32()*20),
            Ts: time.Now().UnixMilli(),
        }
        if err := stream.Send(r); err != nil {
            log.Fatalf("failed to send: %v", err)
        }
        log.Printf("sent reading: t=%.2f h=%.2f p=%.2f", r.Temperature, r.Humidity, r.Pressure)
        sent++
        if *count == 0 {
            <-ticker.C
            continue
        }
        <-ticker.C
    }

    ack, err := stream.CloseAndRecv()
    if err != nil {
        log.Fatalf("stream close error: %v", err)
    }
    log.Printf("server ack: ok=%v msg=%s", ack.Ok, ack.Msg)
}
