package com.example.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
//import android.icu.util.Calendar;
import java.util.Calendar;

/**
 * Created by клаудио on 17.03.2017.
 */

public class AlarmMan {

    private int hour;
    private int minute;
    Context context;
    AlarmClass alarmClass;

    public AlarmMan(int hour, int minute, Context context) {
        this.hour = hour;
        this.minute = minute;
        this.context = context;
    }

    public void setAlarm() {
        Intent AlarmReceiver = new Intent(context, AlarmBroadcastReceiver.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, AlarmReceiver, 0);

//        Calendar alarmCalendar = Calendar.getInstance();
        Calendar alarmClaendar = Calendar.getInstance();
        alarmClaendar.set(Calendar.HOUR_OF_DAY,hour);
        alarmClaendar.set(Calendar.MINUTE,minute);
//        alarmClaendar.set(Calendar.DAY_OF_MONTH, Calendar.SUNDAY);
        alarmManager.set(AlarmManager.RTC_WAKEUP,alarmClaendar.getTimeInMillis(),sender);
        Toast.makeText(context,"Alarm is on",Toast.LENGTH_LONG).show();

    }
}