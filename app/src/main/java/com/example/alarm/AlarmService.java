package com.example.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;


public class AlarmService extends Service {

    private int hour;
    private int minute;
    private AlarmManager aManager;
    private DataBase db;

    private PendingIntent sender;
    private Calendar secondCalendar;
    private AlarmService alarmService;

    @Override
    public void onCreate() {
        super.onCreate();
        aManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        db = new DataBase(getBaseContext(), DataBase.DATABASE_NAME, null, DataBase.DATABASE_VERSION);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int intentFlag = intent.getFlags();
        switch(intentFlag){
            case 1:
                servicepurposeAction();
                Log.d("onStartCom", "servicepurposeAction choosen");
                break;
            case 0:
                cancelAlarm();
                Log.d("onStartCom", "cancelAlarm chossen");
                break;
            case 2:
                autoDaySetUp();
                Log.d("onStartCom", "autoDaySetUp chossen");
                break;
        }
        stopSelf();
        return Service.START_NOT_STICKY;
    }

    private void servicepurposeAction(){
        Log.i("servicepurposeAction", "db = " + db);
        // метод readTime возвращает массив с 2 элементами ЧАС и МИНУТЫ из БД
        int[] time = db.readTime();
        hour = time[0];
        minute = time[1];
        Log.i("servicepurposeAction", "Hour = " + hour + "\nMinute = " + minute);
        //метод dayToNext - возвращает число дней, которое нужно прибавить к текущему дню для установки следующего будильника
        int nextDay = newDayCheck();
        // возвращенное число дней,  которое будет переданно в экземпляр Calendar через метод add()
        int plusDay = dayToNext(nextDay);
        Log.i("!!!!!", "plusDay=" + plusDay);
        Intent alarmShow = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);
        sender = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmShow, 0);
//        Date curDate = Calendar.getInstance().getTime();// проверить работу без этой и следующей строчки, думаю они не нужны
        Calendar mCalendar = Calendar.getInstance();
//        mCalendar.setTime(curDate);
        mCalendar.add(Calendar.DAY_OF_YEAR, plusDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        mCalendar.set(Calendar.MINUTE, minute);
        int testDay = mCalendar.get(Calendar.DAY_OF_WEEK);
        String day = String.valueOf(testDay);
        db.insertOption(day);
        Log.i("CalendarTimeMillis", "timeInMillis = " + mCalendar.getTimeInMillis());
        aManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), sender);
    }

    private void cancelAlarm(){
        Intent alarmIntent = new Intent(getApplicationContext(),AlarmBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(),0,alarmIntent,0);
        aManager.cancel(sender);
    }

    private int dayToNext(int day) {
        int dayToNext = 0;
        int cDay = MainActivity.CURRENT_DAY;
        Log.i("dayToNext", "current day = " + cDay + "\nnext day = " + day);
         if (day == cDay) {
             if (isCurDayFits(cDay, hour, minute)) {
                 dayToNext = 0;
                 Log.i("dayToNext", "current day == next day. Day to next = 0");
             }
             else{
                 dayToNext = 7;
         Log.i("dayToNext", "current day == next day. Day to next = 7");}
        } else if (day > cDay) {
            dayToNext = Math.abs(cDay - day);
         Log.i("dayToNext", "current day < next day" + "\ndayToNext = " + dayToNext);
        } else if (day < cDay) {
            dayToNext = day + (7 - cDay);
         Log.i("dayToNext", "current day > next day" + "\ndayToNext = " + dayToNext);
        }
        Log.i("dayToNext", "END METHOD....");
        return dayToNext;
    }

    private int newDayCheck() {
        Log.i("newDayCheck", "method start...");
        secondCalendar = Calendar.getInstance();//второй календарь, нужно разобраться!!!!!!!!
        int dayN = 0;
        int cDay = MainActivity.CURRENT_DAY;
        if (isCurDayFits(cDay, hour, minute))
            dayN = cDay;
        else
            dayN = secondCalendar.get(Calendar.DAY_OF_WEEK) + 1;
        Log.i("newDayCheck", "searching for checked day start");
        boolean dayChe = false;
        Log.i("newDayCheck", "before while dayN = " + dayN);
        while (dayChe != true) {
            Log.i("newDayCheck", "in while dayN = " + dayN);
            if (dayN <= 7) {
                dayChe = db.readLineInTable(dayN);
                Log.i("newDayCheck", "dayChe = " + dayChe);
                if (dayChe == true) {
                    Toast.makeText(getBaseContext(), "Будильник установлен на " + dayN + " день", Toast.LENGTH_SHORT).show();
                    return dayN;
                }
            } else {
                dayN = 0;
                dayN++;
                dayChe = db.readLineInTable(dayN);
                Log.i("NewDayCheck", "dayN > 7.. else chosen dayN = " + dayN);
                if (dayChe == true) {
                    Toast.makeText(getBaseContext(), "Будильник установлен на " + dayN + " день", Toast.LENGTH_SHORT).show();
                    return dayN;
                }
            }
            ++dayN;
            Log.i("newDayCheck", "in while END dayN = " + dayN);
        }
        Toast.makeText(getBaseContext(), "Будильник установлен на " + dayN + " день", Toast.LENGTH_SHORT).show();
        return dayN;
    }

    private boolean isCurDayFits(int cDay, int hour, int minute) {
        boolean check = true;
        if (db.readLineInTable(cDay)) {
            Calendar aCalendar = Calendar.getInstance();
            Calendar bCalendar = Calendar.getInstance();
            bCalendar.set(Calendar.HOUR_OF_DAY, hour);
            bCalendar.set(Calendar.MINUTE, minute);
            if (aCalendar.getTimeInMillis() >= bCalendar.getTimeInMillis()) {
                check = false;
                Log.i("isCurDayFits", "aCalendar.getTimeInMillis() = " + aCalendar.getTimeInMillis() + "\nbCalendar.getTimeInMillis() = "
                        + bCalendar.getTimeInMillis());
                return check;
            }

        }
        return check;
    }
// если пользователь нажал триггре, не выбрав день
    private void autoDaySetUp(){
        Calendar cCalendar = Calendar.getInstance();
        cCalendar.add(Calendar.DAY_OF_YEAR,MainActivity.CURRENT_DAY+1);
        cCalendar.add(Calendar.HOUR, hour);
        cCalendar.add(Calendar.MINUTE, minute);
        Intent alarmIntent = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);
        sender = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, 0);
        aManager.set(AlarmManager.RTC_WAKEUP,cCalendar.getTimeInMillis(),sender);
    }
}
