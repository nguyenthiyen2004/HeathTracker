// HeartRateData.java
package com.example.healthapp.models;

public class HeartRateData {
    private int id;
    private int heartRate;
    private long timestamp;

    public HeartRateData() {
        this.timestamp = System.currentTimeMillis();
    }

    public HeartRateData(int heartRate) {
        this.heartRate = heartRate;
        this.timestamp = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
