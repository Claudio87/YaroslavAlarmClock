package com.example.alarm;

/**
 * Created by клаудио on 18.05.2017.
 */

public enum WeekDays {
    SUNDAY("Воскресенье"),
    MONDAY("Понедельник"),
    TUESDAY("Вторник"),
    WEDNESDAY("Среду"),
    THURSDAY("Четверг"),
    FRIDAY("Пятницу"),
    SATURDAY("Субботу");

    private WeekDays day;
    private String description;

    private WeekDays(String понедельник) {
        description = понедельник;
    }

    public String getDescription(){
        return description;
    }
}
