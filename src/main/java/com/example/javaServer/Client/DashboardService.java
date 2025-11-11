package com.example.javaServer.Client;

import com.weather.proto.WeatherStats;
import com.weather.proto.AlertMessage;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DashboardService {
    private final DataStorage dataStorage;
    private HttpServer server;
    private final int port;
    private volatile WeatherStats latestStats;
    private volatile AlertMessage latestAlert;

    public DashboardService(DataStorage dataStorage) {
        this(dataStorage, 8080);
    }

    public DashboardService(DataStorage dataStorage, int port) {
        this.dataStorage = dataStorage;
        this.port = port;
    }

    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);

            // Routes
            server.createContext("/", new DashboardHandler());
            server.createContext("/api/stats", new StatsHandler());
            server.createContext("/api/alerts", new AlertsHandler());
            server.createContext("/api/latest", new LatestHandler());
            server.createContext("/api/health", new HealthHandler());

            server.setExecutor(null);
            server.start();

            System.out.println("🌐 Dashboard running at: http://localhost:" + port);
            System.out.println("   📊 /api/stats - Statistics");
            System.out.println("   🚨 /api/alerts - Alerts");
            System.out.println("   🔄 /api/latest - Latest Data");

        } catch (IOException e) {
            System.err.println("❌ Failed to start web server: " + e.getMessage());
        }
    }

    public void updateStats(WeatherStats stats) {
        this.latestStats = stats;
    }

    public void updateAlert(AlertMessage alert) {
        this.latestAlert = alert;
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("⏹️ Web server stopped");
        }
    }

    // HTTP Handlers
    class DashboardHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = createDashboardHTML();
            sendResponse(exchange, response, "text/html; charset=utf-8");
        }
    }

    class StatsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            List<WeatherStats> stats = dataStorage.getRecentStats(50);
            String response = createStatsJSON(stats);
            sendResponse(exchange, response, "application/json");
        }
    }

    class AlertsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            List<AlertMessage> alerts = dataStorage.getRecentAlerts(20);
            String response = createAlertsJSON(alerts);
            sendResponse(exchange, response, "application/json");
        }
    }

    class LatestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = createLatestJSON();
            sendResponse(exchange, response, "application/json");
        }
    }

    class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "{\"status\": \"healthy\", \"timestamp\": \"" +
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\"}";
            sendResponse(exchange, response, "application/json");
        }
    }

    private void sendResponse(HttpExchange exchange, String response, String contentType) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }

    private String createDashboardHTML() {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Weather Monitoring Dashboard</title>
                <style>
                    * { margin: 0; padding: 0; box-sizing: border-box; }
                    body { 
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        color: #333; min-height: 100vh; padding: 20px;
                    }
                    .container { max-width: 1200px; margin: 0 auto; }
                    header { 
                        background: rgba(255,255,255,0.95); padding: 20px; 
                        border-radius: 15px; margin-bottom: 20px; text-align: center;
                    }
                    .dashboard { display: grid; gap: 20px; }
                    .card { 
                        background: rgba(255,255,255,0.95); padding: 20px; 
                        border-radius: 15px; box-shadow: 0 8px 32px rgba(0,0,0,0.1);
                    }
                    .stats-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 15px; }
                    .stat-item { text-align: center; padding: 15px; background: #f8f9fa; border-radius: 10px; }
                    .alert-item { background: #fff3cd; border: 1px solid #ffeaa7; padding: 15px; margin: 10px 0; border-radius: 8px; }
                    .loading { text-align: center; padding: 40px; color: #666; }
                </style>
            </head>
            <body>
                <div class="container">
                    <header>
                        <h1>🌤️ Distributed Weather Monitoring Dashboard</h1>
                        <p>Live data from multi-language gRPC system</p>
                    </header>
                    
                    <div class="dashboard">
                        <div class="card">
                            <h2>📊 Live Statistics</h2>
                            <div id="stats-container" class="stats-grid">
                                <div class="loading">Loading data...</div>
                            </div>
                        </div>
                        
                        <div class="card">
                            <h2>🚨 Weather Alerts</h2>
                            <div id="alerts-container">
                                <div class="loading">Loading alerts...</div>
                            </div>
                        </div>
                    </div>
                </div>

                <script>
                    async function loadStats() {
                        try {
                            const response = await fetch('/api/stats');
                            const stats = await response.json();
                            displayStats(stats);
                        } catch (error) {
                            console.error('Error loading stats:', error);
                        }
                    }

                    async function loadAlerts() {
                        try {
                            const response = await fetch('/api/alerts');
                            const alerts = await response.json();
                            displayAlerts(alerts);
                        } catch (error) {
                            console.error('Error loading alerts:', error);
                        }
                    }

                    function displayStats(stats) {
                        const container = document.getElementById('stats-container');
                        if (stats.length === 0) {
                            container.innerHTML = '<div class="loading">No data available</div>';
                            return;
                        }

                        const latest = stats[stats.length - 1];
                        container.innerHTML = `
                            <div class="stat-item">
                                <div>🌡️ Average Temperature</div>
                                <div style="font-size: 24px; font-weight: bold;">${latest.avgTemp.toFixed(1)}°C</div>
                            </div>
                            <div class="stat-item">
                                <div>🔥 Max Temperature</div>
                                <div style="font-size: 24px; font-weight: bold;">${latest.maxTemp.toFixed(1)}°C</div>
                            </div>
                            <div class="stat-item">
                                <div>⚠️ Total Alerts</div>
                                <div style="font-size: 24px; font-weight: bold;">${latest.totalAlerts}</div>
                            </div>
                            <div class="stat-item">
                                <div>📈 Readings Count</div>
                                <div style="font-size: 24px; font-weight: bold;">${stats.length}</div>
                            </div>
                        `;
                    }

                    function displayAlerts(alerts) {
                        const container = document.getElementById('alerts-container');
                        if (alerts.length === 0) {
                            container.innerHTML = '<div class="alert-item">✅ No current alerts</div>';
                            return;
                        }

                        container.innerHTML = alerts.map(alert => `
                            <div class="alert-item">
                                <strong>🏙️ ${alert.city}</strong><br>
                                🌡️ ${alert.temperature.toFixed(1)}°C<br>
                                📝 ${alert.message}<br>
                                <small>🕒 ${new Date(alert.timestamp).toLocaleString()}</small>
                            </div>
                        `).join('');
                    }

                    // Update data every 5 seconds
                    setInterval(() => {
                        loadStats();
                        loadAlerts();
                    }, 5000);

                    // Initial load
                    loadStats();
                    loadAlerts();
                </script>
            </body>
            </html>
            """;
    }

    private String createStatsJSON(List<WeatherStats> stats) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < stats.size(); i++) {
            WeatherStats stat = stats.get(i);
            json.append(String.format(
                    "{\"avgTemp\": %.2f, \"maxTemp\": %.2f, \"totalAlerts\": %d, \"timestamp\": %d}",
                    stat.getAvgTemp(), stat.getMaxTemp(), stat.getTotalAlerts(), System.currentTimeMillis() - (stats.size() - i) * 1000
            ));
            if (i < stats.size() - 1) json.append(",");
        }
        json.append("]");
        return json.toString();
    }

    private String createAlertsJSON(List<AlertMessage> alerts) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < alerts.size(); i++) {
            AlertMessage alert = alerts.get(i);
            json.append(String.format(
                    "{\"city\": \"%s\", \"temperature\": %.2f, \"message\": \"%s\", \"timestamp\": %d}",
                    alert.getCity(), alert.getTemperature(), alert.getMessage(), System.currentTimeMillis() - i * 60000
            ));
            if (i < alerts.size() - 1) json.append(",");
        }
        json.append("]");
        return json.toString();
    }

    private String createLatestJSON() {
        if (latestStats == null) {
            return "{\"status\": \"no data\"}";
        }

        return String.format(
                "{\"avgTemp\": %.2f, \"maxTemp\": %.2f, \"totalAlerts\": %d, \"lastAlert\": \"%s\"}",
                latestStats.getAvgTemp(), latestStats.getMaxTemp(), latestStats.getTotalAlerts(),
                latestAlert != null ? latestAlert.getCity() + " - " + latestAlert.getMessage() : "None"
        );
    }
}