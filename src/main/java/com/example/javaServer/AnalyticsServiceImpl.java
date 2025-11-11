package com.example.javaServer;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import com.weather.proto.Empty;
import com.weather.proto.ReportRequest;
import com.weather.proto.AlertMessage;
import com.weather.proto.WeatherStats;
import com.weather.proto.AnalyticsServiceGrpc;

import java.io.IOException;
import java.util.Random;

class AnalyticsServer {
    private final int port;
    private final Server server;

    public AnalyticsServer(int port) {
        this.port = port;
        this.server = ServerBuilder.forPort(port)
                .addService(new AnalyticsServiceImpl())
                .build();
    }

    public void start() throws IOException {
        server.start();
        System.out.println("Analytics Server started on port " + port);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Shutting down Analytics Server...");
                try {
                    AnalyticsServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, java.util.concurrent.TimeUnit.SECONDS);
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    static class AnalyticsServiceImpl extends AnalyticsServiceGrpc.AnalyticsServiceImplBase {
        private final Random rand = new Random();

        @Override
        public void subscribeToAlerts(Empty request, StreamObserver<AlertMessage> responseObserver) {
            System.out.println("New client subscribed to alerts");

            Thread thread = new Thread(() -> {
                String[] cities = {"Riyadh", "Jeddah", "Dammam", "Mecca", "Medina"};

                try {
                    for (int i = 0; i < 10; i++) {
                        float temp = 35 + rand.nextFloat() * 8;
                        String city = cities[rand.nextInt(cities.length)];

                        if (temp > 38) {
                            AlertMessage alert = AlertMessage.newBuilder()
                                    .setCity(city)
                                    .setTemperature(temp)
                                    .setMessage("ALERT! High temperature: " + String.format("%.1f", temp) + "°C")
                                    .build();

                            responseObserver.onNext(alert);
                            System.out.println("ALERT SENT: " + String.format("%.1f", temp) + "°C in " + city);
                        }
                        Thread.sleep(2000);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                responseObserver.onCompleted();
            });
            thread.start();
        }

        @Override
        public void subscribeToStats(ReportRequest request, StreamObserver<WeatherStats> responseObserver) {
            System.out.println("Client requested stats for region: " + request.getRegion());

            Thread thread = new Thread(() -> {
                float sum = 0;
                int count = 0;
                float maxTemp = 0;

                try {
                    for (int i = 0; i < 10; i++) {
                        float temp = 30 + rand.nextFloat() * 15;
                        sum += temp;
                        count++;
                        if (temp > maxTemp) maxTemp = temp;

                        WeatherStats stats = WeatherStats.newBuilder()
                                .setAvgTemp(sum / count)
                                .setMaxTemp(maxTemp)
                                .setTotalAlerts(rand.nextInt(20))
                                .build();

                        responseObserver.onNext(stats);
                        System.out.println("Stats sent - Avg: " + String.format("%.1f", sum/count) + "°C");

                        Thread.sleep(3000);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                responseObserver.onCompleted();
            });
            thread.start();
        }
    }

    public static void main(String[] args) throws Exception {
        AnalyticsServer server = new AnalyticsServer(50051);
        server.start();
        System.out.println("Analytics Server is running... Press Ctrl+C to stop.");
        server.blockUntilShutdown();
    }
}