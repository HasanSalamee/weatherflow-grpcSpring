package com.example.javaServer.Client;

import com.weather.proto.WeatherStats;
import com.weather.proto.AlertMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DataStorage {
    private final List<WeatherStats> weatherStats;
    private final List<AlertMessage> alertMessages;
    private final int maxStats = 1000;
    private final int maxAlerts = 500;

    public DataStorage() {
        this.weatherStats = new CopyOnWriteArrayList<>();
        this.alertMessages = new CopyOnWriteArrayList<>();
    }

    public synchronized void addStats(WeatherStats stats) {
        weatherStats.add(stats);

        // الحفاظ على الحد الأقصى
        if (weatherStats.size() > maxStats) {
            weatherStats.remove(0);
        }
    }

    public synchronized void addAlert(AlertMessage alert) {
        alertMessages.add(alert);

        // الحفاظ على الحد الأقصى
        if (alertMessages.size() > maxAlerts) {
            alertMessages.remove(0);
        }
    }

    public List<WeatherStats> getRecentStats(int count) {
        if (weatherStats.isEmpty()) {
            return Collections.emptyList();
        }

        int startIndex = Math.max(0, weatherStats.size() - count);
        return new ArrayList<>(weatherStats.subList(startIndex, weatherStats.size()));
    }

    public List<AlertMessage> getRecentAlerts(int count) {
        if (alertMessages.isEmpty()) {
            return Collections.emptyList();
        }

        int startIndex = Math.max(0, alertMessages.size() - count);
        return new ArrayList<>(alertMessages.subList(startIndex, alertMessages.size()));
    }

    public WeatherStats getLatestStats() {
        if (weatherStats.isEmpty()) {
            return null;
        }
        return weatherStats.get(weatherStats.size() - 1);
    }

    public int getStatsCount() {
        return weatherStats.size();
    }

    public int getAlertsCount() {
        return alertMessages.size();
    }

    public void clearAll() {
        weatherStats.clear();
        alertMessages.clear();
    }

    public List<WeatherStats> getAllStats() {
        return new ArrayList<>(weatherStats);
    }

    public List<AlertMessage> getAllAlerts() {
        return new ArrayList<>(alertMessages);
    }
}