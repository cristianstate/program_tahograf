package com.fitness.myapplication.data.java;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "trucker_logs_java")
public class TruckerLog {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String date;
    private String startTime;
    private String endTime;
    private String drivingTime;
    private String restDuration;
    private String kilometers;

    public TruckerLog(String date, String startTime, String endTime, String drivingTime, String restDuration, String kilometers) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.drivingTime = drivingTime;
        this.restDuration = restDuration;
        this.kilometers = kilometers;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getDrivingTime() { return drivingTime; }
    public void setDrivingTime(String drivingTime) { this.drivingTime = drivingTime; }

    public String getRestDuration() { return restDuration; }
    public void setRestDuration(String restDuration) { this.restDuration = restDuration; }

    public String getKilometers() { return kilometers; }
    public void setKilometers(String kilometers) { this.kilometers = kilometers; }
}
