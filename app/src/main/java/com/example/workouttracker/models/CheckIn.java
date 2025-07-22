package com.example.workouttracker.models;

public class CheckIn {
    private int id;
    private String timestamp;

    public CheckIn(int id, String timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
