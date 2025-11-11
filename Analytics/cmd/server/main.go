package main

import (
    "log"
    "net"
    "time"

    "github.com/hasan/weather"
    "google.golang.org/grpc"
)

// تعريف struct الخادم الذي يطبّق واجهة AnalyticsService التي وُلدت من البروتو
type analyticsServer struct {
    // تضمّن struct الافتراضي الذي يوفّر السلوك الافتراضي (منع تحذيرات عدم التغطية)
    weather.UnimplementedAnalyticsServiceServer
}

// SubscribeToStats: دالة تُنفّذ RPC من نوع server-streaming
// المدخل: req من نوع ReportRequest
// المخرج: stream تُستخدم لإرسال عدة رسائل WeatherStats بالترتيب
func (s *analyticsServer) SubscribeToStats(req *weather.ReportRequest, stream weather.AnalyticsService_SubscribeToStatsServer) error {
    // هذا المثال يرسل 10 رسائل بشكل دوري (لأغراض العرض فقط)
    for i := 0; i < 10; i++ {
        ws := &weather.WeatherStats{
            // قيم تجريبية تتغير مع كل رسالة
            AvgTemp:    float32(20 + i),
            MaxTemp:    float32(25 + i),
            TotalAlerts: int32(i % 5),
        }
        // إرسال الرسالة عبر الستريم إلى العميل
        if err := stream.Send(ws); err != nil {
            // في حالة خطأ عند الإرسال نعيد الخطأ ليتعامل gRPC معه
            return err
        }
        // ننتظر نصف ثانية قبل إرسال الرسالة التالية (محاكاة زمنية)
        time.Sleep(500 * time.Millisecond)
    }
    // نهاية الستريم بالنجاح
    return nil
}

// SubscribeToAlerts: RPC آخر من نوع server-streaming لإرسال تنبيهات
func (s *analyticsServer) SubscribeToAlerts(_ *weather.Empty, stream weather.AnalyticsService_SubscribeToAlertsServer) error {
    // مجموعة تنبيهات ثابتة للمثال
    alerts := []*weather.AlertMessage{
        {City: "Riyadh", Temperature: 42.3, Message: "Heat warning"},
        {City: "Cairo", Temperature: 39.1, Message: "High temperature"},
        {City: "Tunis", Temperature: 35.0, Message: "Heat advisory"},
    }
    for _, a := range alerts {
        // إرسال كل تنبيه
        if err := stream.Send(a); err != nil {
            return err
        }
        // محاكاة تأخير بين التنبيهات
        time.Sleep(700 * time.Millisecond)
    }
    return nil
}

// main: تهيئة السيرفر وبدء الاستماع على منفذ TCP
func main() {
    // فتح listener على المنفذ 50051 (يستقبل اتصال gRPC)
    lis, err := net.Listen("tcp", ":50051")
    if err != nil {
        log.Fatalf("failed to listen: %v", err)
    }

    // إنشاء خادم gRPC جديد
    grpcServer := grpc.NewServer()

    // تسجيل الخادم الذي يطبق واجهة AnalyticsService
    weather.RegisterAnalyticsServiceServer(grpcServer, &analyticsServer{})

    // طباعة رسالة إلى اللوغ تفيد أن السيرفر شغال
    log.Println("gRPC server listening on :50051")

    // بدء حلقة الخدمة التي تستقبل الاتصالات وتخدم الطلبات
    if err := grpcServer.Serve(lis); err != nil {
        log.Fatalf("server exited: %v", err)
    }
}
