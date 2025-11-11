package com.example.javaServer.Client;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String host = "192.168.96.202";
        int port = 50053;

        // Read settings from command line arguments
        if (args.length >= 1) host = args[0];
        if (args.length >= 2) port = Integer.parseInt(args[1]);

        System.out.println("=".repeat(50));
        System.out.println("    🌤️ Distributed Weather Monitoring System Client");
        System.out.println("=".repeat(50));
        System.out.println("📍 Connecting to: " + host + ":" + port);

        WeatherClient client = new WeatherClient(host, port);

        try {
            client.start();

            // Wait for user input to stop
            System.out.println("\n⏸️ Press Enter to stop the client...");
            new Scanner(System.in).nextLine();

        } catch (Exception e) {
            System.err.println("❌ Unexpected error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            client.shutdown();
        }
    }
}