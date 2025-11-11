package com.example.javaServer.Client;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String host = "192.168.96.202";
        int port = 50053;

        // قراءة الإعدادات من الأرجومنتات
        if (args.length >= 1) host = args[0];
        if (args.length >= 2) port = Integer.parseInt(args[1]);

        System.out.println("=".repeat(50));
        System.out.println("    🌤️  عميل نظام مراقبة الطقس الموزع");
        System.out.println("=".repeat(50));

        WeatherClient client = new WeatherClient(host, port);

        try {
            client.start();

            // الانتظار لإدخال المستخدم للإيقاف
            System.out.println("\n⏸️  اضغط Enter لإيقاف العميل...");
            new Scanner(System.in).nextLine();

        } catch (Exception e) {
            System.err.println("❌ خطأ غير متوقع: " + e.getMessage());
            e.printStackTrace();
        } finally {
            client.shutdown();
        }
    }
}