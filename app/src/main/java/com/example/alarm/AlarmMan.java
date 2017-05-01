package com.example.alarm;


import android.content.Context;
import android.content.Intent;

/**
 * Created by клаудио on 17.03.2017.
 */

public class AlarmMan {

    private int hour;
    private int minute;
    Context context;


    public AlarmMan(int hour, int minute, Context context) {
        this.hour = hour;
        this.minute = minute;
        this.context = context;
    }

    public void setAlarm() {

        Intent serviceIntent = new Intent(context, AlarmService.class);
        //добавляем поля int hour, minute в интент для AlarmService (service)
        serviceIntent.putExtra("Hour", hour);
        serviceIntent.putExtra("Minute", minute);
        // запускаем сервис
        context.startService(serviceIntent);

//        Toast.makeText(context,"Alarm is on",Toast.LENGTH_LONG).show();

    }
}