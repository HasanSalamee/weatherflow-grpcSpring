package com.example.javaServer;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import com.weather.proto.AnalyticsServiceGrpc;
import com.weather.proto.ReportRequest;
import com.weather.proto.WeatherStats;
import com.weather.proto.AlertMessage;
import com.weather.proto.Empty;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class WeatherClient {
    public static void main(String[] args) throws InterruptedException {
        // ✅ العنوان والمنفذ الصحيحين
        String host = "192.168.96.202";
        int port = 50052;
        String region = "Gulf";

        if (args.length >= 1) host = args[0];
        if (args.length >= 2) port = Integer.parseInt(args[1]);
        if (args.length >= 3) region = args[2];

        System.out.println("🔄 Attempting to connect to: " + host + ":" + port);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        AnalyticsServiceGrpc.AnalyticsServiceBlockingStub stub = AnalyticsServiceGrpc.newBlockingStub(channel);

        try {
            // Subscribe to stats (server-streaming)
            System.out.println("📊 Requesting stats for region: " + region);
            try {
                Iterator<WeatherStats> statsIt = stub.subscribeToStats(ReportRequest.newBuilder().setRegion(region).build());
                int statsCount = 0;
                while (statsIt.hasNext()) {
                    WeatherStats s = statsIt.next();
                    statsCount++;
                    System.out.printf("📈 Stats [%d]: avg_temp=%.2f, max_temp=%.2f, total_alerts=%d\n",
                            statsCount, s.getAvgTemp(), s.getMaxTemp(), s.getTotalAlerts());
                }
                if (statsCount == 0) {
                    System.out.println("ℹ️ No stats received - server might not have data yet");
                }
                System.out.println("-- stats stream finished --\n");
            } catch (StatusRuntimeException e) {
                System.err.println("❌ Stats RPC error: " + e.getStatus() + " - " + e.getMessage());
            }

            // Subscribe to alerts (server-streaming)
            System.out.println("🚨 Subscribing to alerts");
            try {
                Iterator<AlertMessage> alertsIt = stub.subscribeToAlerts(Empty.newBuilder().build());
                int alertCount = 0;
                while (alertsIt.hasNext()) {
                    AlertMessage a = alertsIt.next();
                    alertCount++;
                    System.out.printf("⚠️ Alert [%d]: city=%s, temp=%.2f, msg=%s\n",
                            alertCount, a.getCity(), a.getTemperature(), a.getMessage());
                }
                if (alertCount == 0) {
                    System.out.println("ℹ️ No alerts received");
                }
            } catch (StatusRuntimeException e) {
                System.err.println("❌ Alerts RPC error: " + e.getStatus() + " - " + e.getMessage());
            }

        } finally {
            // Clean shutdown
            System.out.println("🔚 Shutting down...");
            channel.shutdown();
            if (!channel.awaitTermination(5, TimeUnit.SECONDS)) {
                channel.shutdownNow();
            }
        }
    }
}