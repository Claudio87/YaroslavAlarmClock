package com.example.alarm;

/**
 * Created by клаудио on 02.04.2017.
 */

public class WeekDayStatus {

    int id;
    String day;
    boolean status;

    public WeekDayStatus(boolean []statusArray) {
    }

    public WeekDayStatus(boolean stat, int id, String day) {
        status = stat;
        this.id = id;
        this.day = day;
    }

    public WeekDayStatus(int id, boolean status){
        this.id = id;
        this.status = status;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isStatus() {
        return status;
    }
//    public String stringStatus(){
//        String test = "true";
//        return test;
//    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
