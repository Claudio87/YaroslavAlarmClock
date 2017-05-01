package com.example.alarm;

/**
 * Created by клаудио on 02.04.2017.
 */

public class WeekDayStatus {

    int id;
    String day;
    boolean status;
//   Delete if works
//    public WeekDayStatus(boolean []statusArray) {
//    }

    public WeekDayStatus(boolean stat, int id, String day) {
        status = stat;
        this.id = id;
        this.day = day;
    }
    //   Delete if works
//    public WeekDayStatus(int id, boolean status){
//        this.id = id;
//        this.status = status;
//    }
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
