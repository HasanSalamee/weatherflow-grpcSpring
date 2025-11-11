package com.example.javaServer.Client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import com.weather.proto.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class WeatherClient {
    private final ManagedChannel channel;
    private final AnalyticsServiceGrpc.AnalyticsServiceStub asyncStub;
    private final String host;
    private final int port;
    private final AtomicBoolean isRunning;
    private final DataStorage dataStorage;
    private final DashboardService dashboardService;

    public WeatherClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.isRunning = new AtomicBoolean(true);
        this.dataStorage = new DataStorage();
        this.dashboardService = new DashboardService(dataStorage);

        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .keepAliveTime(30, TimeUnit.SECONDS)
                .keepAliveWithoutCalls(true)
                .build();

        this.asyncStub = AnalyticsServiceGrpc.newStub(channel);

        // Add shutdown hook for clean closure
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    public void start() {
        System.out.println("🚀 Starting weather monitoring client...");
        System.out.println("📍 Connecting to: " + host + ":" + port);

        // Start web service
        dashboardService.start();

        // Subscribe to statistics
        subscribeToStats();

        // Subscribe to alerts
        subscribeToAlerts();

        // Keep program active
        keepAlive();
    }

    private void subscribeToStats() {
        System.out.println("📊 Subscribing to weather statistics...");

        ReportRequest request = ReportRequest.newBuilder()
                .setRegion("Middle-East")
                .build();

        asyncStub.subscribeToStats(request, new StreamObserver<WeatherStats>() {
            @Override
            public void onNext(WeatherStats stats) {
                System.out.println("📈 New statistics data:");
                System.out.printf("   📍 Average Temperature: %.2f°C\n", stats.getAvgTemp());
                System.out.printf("   🔥 Max Temperature: %.2f°C\n", stats.getMaxTemp());
                System.out.printf("   ⚠️  Total Alerts: %d\n", stats.getTotalAlerts());
                System.out.println("   " + "=".repeat(40));

                // Save data for storage
                dataStorage.addStats(stats);

                // Update dashboard
                dashboardService.updateStats(stats);
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("❌ Error in statistics subscription: " + t.getMessage());
                System.out.println("🔄 Retrying in 10 seconds...");

                // Retry after delay
                if (isRunning.get()) {
                    try {
                        Thread.sleep(10000);
                        subscribeToStats();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            @Override
            public void onCompleted() {
                System.out.println("🔚 Statistics stream ended from server");
                if (isRunning.get()) {
                    System.out.println("🔄 Reconnecting...");
                    subscribeToStats();
                }
            }
        });
    }

    private void subscribeToAlerts() {
        System.out.println("🚨 Subscribing to weather alerts...");

        asyncStub.subscribeToAlerts(Empty.newBuilder().build(), new StreamObserver<AlertMessage>() {
            @Override
            public void onNext(AlertMessage alert) {
                System.out.println("⚠️  New weather alert:");
                System.out.printf("   🏙️  City: %s\n", alert.getCity());
                System.out.printf("   🌡️  Temperature: %.2f°C\n", alert.getTemperature());
                System.out.printf("   📝 Message: %s\n", alert.getMessage());
                System.out.println("   " + "🚨".repeat(10));

                // Save alert
                dataStorage.addAlert(alert);

                // Update dashboard
                dashboardService.updateAlert(alert);

                // Instant notification
                sendNotification(alert);
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("❌ Error in alerts subscription: " + t.getMessage());
                System.out.println("🔄 Retrying in 10 seconds...");

                if (isRunning.get()) {
                    try {
                        Thread.sleep(10000);
                        subscribeToAlerts();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            @Override
            public void onCompleted() {
                System.out.println("🔚 Alerts stream ended from server");
                if (isRunning.get()) {
                    System.out.println("🔄 Reconnecting...");
                    subscribeToAlerts();
                }
            }
        });
    }

    private void sendNotification(AlertMessage alert) {
        // Can add system notifications here
        System.out.println("🔔 Notification: Weather alert in " + alert.getCity() + " - " + alert.getMessage());
    }

    private void keepAlive() {
        try {
            while (isRunning.get()) {
                Thread.sleep(1000);

                // Print heartbeat every 30 seconds
                if (System.currentTimeMillis() % 30000 < 1000) {
                    System.out.println("💓 Client running - waiting for data...");
                    System.out.printf("   📊 Statistics received: %d\n", dataStorage.getStatsCount());
                    System.out.printf("   🚨 Alerts received: %d\n", dataStorage.getAlertsCount());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("⏹️ Client stopped");
        }
    }

    public void shutdown() {
        System.out.println("🛑 Stopping weather client...");
        isRunning.set(false);

        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
            dashboardService.stop();
            System.out.println("✅ Stopped successfully");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            channel.shutdownNow();
        }
    }

    public DataStorage getDataStorage() {
        return dataStorage;
    }

    public DashboardService getDashboardService() {
        return dashboardService;
    }
}