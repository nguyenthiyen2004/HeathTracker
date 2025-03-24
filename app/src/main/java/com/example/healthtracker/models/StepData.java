// StepData.java
package com.example.healthapp.models;

public class StepData {
    private int id;
    private int day;
    private int month;
    private int year;
    private int steps;
    private long timestamp;

    public StepData() {
        this.timestamp = System.currentTimeMillis();
    }

    public StepData(int day, int month, int year, int steps) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.steps = steps;
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

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}