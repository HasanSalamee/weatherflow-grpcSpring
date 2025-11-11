package com.example.javaServer;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import com.weather.proto.SensorReading;
import com.weather.proto.SensorAck;
import com.weather.proto.SensorServiceGrpc;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class SensorServer {
    private final int port;
    private final Server server;

    public SensorServer(int port) {
        this.port = port;
        this.server = ServerBuilder.forPort(port)
                .addService(new SensorServiceImpl())
                .build();
    }

    public void start() throws IOException {
        server.start();
        System.out.println("🔧 Sensor Server started on port " + port);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("🛑 Shutting down Sensor Server...");
                try {
                    SensorServer.this.stop();
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

    static class SensorServiceImpl extends SensorServiceGrpc.SensorServiceImplBase {
        private final AtomicInteger receivedReadings = new AtomicInteger(0);

        @Override
        public StreamObserver<SensorReading> streamSensorData(StreamObserver<SensorAck> responseObserver) {
            System.out.println("📡 New sensor connected to stream data");

            return new StreamObserver<SensorReading>() {
                @Override
                public void onNext(SensorReading reading) {
                    int count = receivedReadings.incrementAndGet();
                    System.out.println("📨 Received sensor data #" + count +
                            " | ID: " + reading.getSensorId() +
                            " | Temp: " + String.format("%.1f", reading.getTemperature()) + "°C" +
                            " | Humidity: " + String.format("%.1f", reading.getHumidity()) + "%" +
                            " | Pressure: " + String.format("%.1f", reading.getPressure()) + "hPa");
                }

                @Override
                public void onError(Throwable t) {
                    System.err.println("❌ Sensor stream error: " + t.getMessage());
                }

                @Override
                public void onCompleted() {
                    int total = receivedReadings.get();
                    SensorAck ack = SensorAck.newBuilder()
                            .setOk(true)
                            .setMsg("✅ Successfully processed " + total + " sensor readings")
                            .build();
                    responseObserver.onNext(ack);
                    responseObserver.onCompleted();
                    System.out.println("✅ Sensor stream completed. Total readings processed: " + total);
                }
            };
        }
    }

    public static void main(String[] args) throws Exception {
        SensorServer server = new SensorServer(50052);
        server.start();
        System.out.println("✅ Sensor Server is running... Press Ctrl+C to stop.");
        server.blockUntilShutdown();
    }
}