package main

import (
    "context"
    "fmt"
    "io"
    "log"
    "sync"
    "time"

    "github.com/hasan/weather"
    "google.golang.org/grpc"
    "google.golang.org/grpc/credentials/insecure"
)

func main() {
    ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
    defer cancel()

    conn, err := grpc.DialContext(ctx, "localhost:50051", grpc.WithTransportCredentials(insecure.NewCredentials()), grpc.WithBlock())
    if err != nil {
        log.Fatalf("failed to dial server: %v", err)
    }
    defer conn.Close()

    client := weather.NewAnalyticsServiceClient(conn)

    var wg sync.WaitGroup
    wg.Add(2)

    // Subscribe to stats
    go func() {
        defer wg.Done()
        stream, err := client.SubscribeToStats(context.Background(), &weather.ReportRequest{Region: "demo-region"})
        if err != nil {
            log.Printf("SubscribeToStats error: %v", err)
            return
        }
        for {
            msg, err := stream.Recv()
            if err == io.EOF {
                return
            }
            if err != nil {
                log.Printf("stats recv error: %v", err)
                return
            }
            fmt.Printf("STATS: avg=%.1f max=%.1f alerts=%d\n", msg.AvgTemp, msg.MaxTemp, msg.TotalAlerts)
        }
    }()

    // Subscribe to alerts
    go func() {
        defer wg.Done()
        stream, err := client.SubscribeToAlerts(context.Background(), &weather.Empty{})
        if err != nil {
            log.Printf("SubscribeToAlerts error: %v", err)
            return
        }
        for {
            msg, err := stream.Recv()
            if err == io.EOF {
                return
            }
            if err != nil {
                log.Printf("alerts recv error: %v", err)
                return
            }
            fmt.Printf("ALERT: city=%s temp=%.1f msg=%s\n", msg.City, msg.Temperature, msg.Message)
        }
    }()

    wg.Wait()
}
