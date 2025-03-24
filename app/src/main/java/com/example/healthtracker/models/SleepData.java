// SleepData.java
package com.example.healthapp.models;

public class SleepData {
    private int id;
    private int day;
    private int month;
    private int year;
    private long startTime;
    private long endTime;
    private int durationMinutes;
    private String quality; // "LIGHT", "DEEP", "REM"

    public SleepData() {
    }

    public SleepData(int day, int month, int year, long startTime, long endTime, String quality) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinutes = (int) ((endTime - startTime) / (1000 * 60));
        this.quality = quality;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
        updateDuration();
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
        updateDuration();
    }

    private void updateDuration() {
        if (startTime > 0 && endTime > 0) {
            this.durationMinutes = (int) ((endTime - startTime) / (1000 * 60));
        }
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }
}