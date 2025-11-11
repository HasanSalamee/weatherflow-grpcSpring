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

        // إضافة خطاف لإغلاق نظيف
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    public void start() {
        System.out.println("🚀 بدء عميل مراقبة الطقس...");
        System.out.println("📍 الاتصال بـ: " + host + ":" + port);

        // بدء خدمة الويب
        dashboardService.start();

        // الاشتراك في الإحصائيات
        subscribeToStats();

        // الاشتراك في التنبيهات
        subscribeToAlerts();

        // الحفاظ على البرنامج نشط
        keepAlive();
    }

    private void subscribeToStats() {
        System.out.println("📊 جاري الاشتراك في إحصائيات الطقس...");

        ReportRequest request = ReportRequest.newBuilder()
                .setRegion("Middle-East")
                .build();

        asyncStub.subscribeToStats(request, new StreamObserver<WeatherStats>() {
            @Override
            public void onNext(WeatherStats stats) {
                System.out.println("📈 بيانات إحصائيات جديدة:");
                System.out.printf("   📍 متوسط الحرارة: %.2f°C\n", stats.getAvgTemp());
                System.out.printf("   🔥 أقصى حرارة: %.2f°C\n", stats.getMaxTemp());
                System.out.printf("   ⚠️  إجمالي التنبيهات: %d\n", stats.getTotalAlerts());
                System.out.println("   " + "=".repeat(40));

                // حفظ البيانات للتخزين
                dataStorage.addStats(stats);

                // تحديث لوحة التحكم
                dashboardService.updateStats(stats);
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("❌ خطأ في اشتراك الإحصائيات: " + t.getMessage());
                System.out.println("🔄 إعادة المحاولة خلال 10 ثواني...");

                // إعادة المحاولة بعد فترة
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
                System.out.println("🔚 انتهى تدفق الإحصائيات من الخادم");
                if (isRunning.get()) {
                    System.out.println("🔄 إعادة الاتصال...");
                    subscribeToStats();
                }
            }
        });
    }

    private void subscribeToAlerts() {
        System.out.println("🚨 جاري الاشتراك في تنبيهات الطقس...");

        asyncStub.subscribeToAlerts(Empty.newBuilder().build(), new StreamObserver<AlertMessage>() {
            @Override
            public void onNext(AlertMessage alert) {
                System.out.println("⚠️  تنبيه طقس جديد:");
                System.out.printf("   🏙️  المدينة: %s\n", alert.getCity());
                System.out.printf("   🌡️  درجة الحرارة: %.2f°C\n", alert.getTemperature());
                System.out.printf("   📝 الرسالة: %s\n", alert.getMessage());
                System.out.println("   " + "🚨".repeat(10));

                // حفظ التنبيه
                dataStorage.addAlert(alert);

                // تحديث لوحة التحكم
                dashboardService.updateAlert(alert);

                // إشعار فوري
                sendNotification(alert);
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("❌ خطأ في اشتراك التنبيهات: " + t.getMessage());
                System.out.println("🔄 إعادة المحاولة خلال 10 ثواني...");

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
                System.out.println("🔚 انتهى تدفق التنبيهات من الخادم");
                if (isRunning.get()) {
                    System.out.println("🔄 إعادة الاتصال...");
                    subscribeToAlerts();
                }
            }
        });
    }

    private void sendNotification(AlertMessage alert) {
        // يمكن إضافة إشعارات نظام هنا
        System.out.println("🔔 إشعار: تنبيه طقس في " + alert.getCity() + " - " + alert.getMessage());
    }

    private void keepAlive() {
        try {
            while (isRunning.get()) {
                Thread.sleep(1000);

                // طباعة نبضة حياة كل 30 ثانية
                if (System.currentTimeMillis() % 30000 < 1000) {
                    System.out.println("💓 العميل يعمل - في انتظار البيانات...");
                    System.out.printf("   📊 الإحصائيات المستلمة: %d\n", dataStorage.getStatsCount());
                    System.out.printf("   🚨 التنبيهات المستلمة: %d\n", dataStorage.getAlertsCount());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("⏹️  تم إيقاف العميل");
        }
    }

    public void shutdown() {
        System.out.println("🛑 إيقاف عميل الطقس...");
        isRunning.set(false);

        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
            dashboardService.stop();
            System.out.println("✅ تم الإيقاف بنجاح");
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