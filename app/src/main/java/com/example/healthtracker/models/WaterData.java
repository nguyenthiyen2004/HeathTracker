// WaterData.java
package com.example.healthapp.models;

public class WaterData {
    private int id;
    private int day;
    private int month;
    private int year;
    private int amount; // in ml
    private long timestamp;

    public WaterData() {
        this.timestamp = System.currentTimeMillis();
    }

    public WaterData(int day, int month, int year, int amount) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.amount = amount;
        this.timestamp = System.currentTimeMillis();
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}