package com.example.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.TextView;

import java.util.Calendar;


public class AlarmService extends Service {

    TextView textView;
    private int hour;
    private int minute;
    private DataBase db;

    public AlarmService() {
        db = new DataBase(getBaseContext(),DataBase.DATABASE_NAME,null,DataBase.DATABASE_VERSION);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //получаем из интента значения времени: часы и минуты
        hour=intent.getIntExtra("Hour",0);
        minute=intent.getIntExtra("Minute",0);

                    Intent alarmShow = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);
                    AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmShow, 0);
                    Calendar mCalendar = Calendar.getInstance();
                    mCalendar.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
                    mCalendar.set(Calendar.HOUR_OF_DAY,hour);
                    mCalendar.set(Calendar.MINUTE,minute);
                    aManager.set(AlarmManager.RTC_WAKEUP,mCalendar.getTimeInMillis(),sender);

//        stopSelf();

        return Service.START_STICKY;
    }


}
