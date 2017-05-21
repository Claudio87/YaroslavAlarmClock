package com.example.alarm;

/**
 * Created by клаудио on 02.04.2017.
 */

public class WeekDayStatus {

    int id;
    String day;
    boolean status;

    public WeekDayStatus(boolean stat, int id, String day) {
        status = stat;
        this.id = id;
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public String getDay() {
        return day;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
