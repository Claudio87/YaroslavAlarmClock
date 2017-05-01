package com.example.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;


public class AlarmService extends Service {

    private int hour;
    private int minute;
    private DataBase db;
    private PendingIntent sender;
    private Calendar secondCalendar;
    AlarmManager aManager;
    private AlarmService alarmService;
//    public AlarmService() {
//
//    }
//
//    protected AlarmService() {
//        hour = in.readInt();
//        minute = in.readInt();
//        sender = in.readParcelable(PendingIntent.class.getClassLoader());
//        alarmService = in.readParcelable(AlarmService.class.getClassLoader());
//    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        alarmService = this;
        //получаю экземпляр БД
        db = new DataBase(getBaseContext(),DataBase.DATABASE_NAME,null,DataBase.DATABASE_VERSION);
        Calendar mCalendar = Calendar.getInstance();
        Log.i("onStartCom..", "db = "+ db);
        // метод readTime возвращает массив с 2 элементами ЧАС и МИНУТЫ из БД
        int [] time = db.readTime();
        hour = time[0];
        minute = time[1];
        Log.i("onStartCom..", "Hour = "+hour+"\nMinute = "+minute);
        //метод dayToNext - возвращает число дней, которое нужно прибавить к текущему дню для установки следующего будильника
            int nextDay = newDayCheck();
        // возвращенное число дней,  которое будет переданно в экземпляр Calendar через метод add()
            int plusDay = dayToNext(nextDay);
        Log.i("!!!!!","plusDay="+plusDay);
            Intent alarmShow = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);
            aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            sender = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmShow, 0);
            Date curDate = Calendar.getInstance().getTime();
            mCalendar.setTime(curDate);
            mCalendar.add(Calendar.DAY_OF_YEAR,plusDay);
            mCalendar.set(Calendar.HOUR_OF_DAY, hour);
            mCalendar.set(Calendar.MINUTE, minute);
        Log.i("CalendarTimeMillis", "timeInMillis = "+mCalendar.getTimeInMillis());
            aManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), sender);
//        stopSelf();
        return Service.START_STICKY;
    }

    private int dayToNext(int day){
        int dayToNext = 0;
        int cDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        Log.i("dayToNext", "current day = "+cDay+"\nnext day = "+day);
        if(day == cDay) {
            dayToNext = 7;
            Log.i("dayToNext", "current day == next day. Day to next = 7");
        }
        else if(day > cDay) {
            dayToNext = Math.abs(cDay - day);
            Log.i("dayToNext", "current day < next day"+"\ndayToNext = "+dayToNext);
        }
        else if (day < cDay) {
            dayToNext = day + (7 - cDay);
            Log.i("dayToNext", "current day > next day"+"\ndayToNext = "+dayToNext);
        }
        Log.i("dayToNext", "END METHOD....");
        return dayToNext;
    }

    private int newDayCheck(){
        Log.i("newDayCheck", "method start...");
        secondCalendar = Calendar.getInstance();//второй календарь, нужно разобраться!!!!!!!!
        int dayN = 0;
//        if(Calendar.getInstance().getTimeInMillis() >= secondCalendar.getTimeInMillis()) {
            dayN = secondCalendar.get(Calendar.DAY_OF_WEEK)+1;
//            Log.i("newDayCheck","first if  dayN = "+dayN);
//        }
//        else
//            dayN = secondCalendar.get(Calendar.DAY_OF_WEEK);
        Log.i("newDayCheck","searching for checked day start");
        boolean dayChe = false;
        Log.i("newDayCheck","before while dayN = "+dayN);
        while(dayChe != true){
            Log.i("newDayCheck","in while dayN = "+dayN);

             if(dayN <=7){
                dayChe = db.readLineInTable(dayN);
        Log.i("newDayCheck", "dayChe = "+dayChe);
                if(dayChe == true){
                    Toast.makeText(getBaseContext(),"Будильник установлен на "+dayN+" день",Toast.LENGTH_SHORT).show();
                    return dayN;}
        }
            else {
                dayN = 0;
                dayN++;
                dayChe = db.readLineInTable(dayN);
        Log.i("NewDayCheck","dayN > 7.. else chosen dayN = "+dayN);
                if(dayChe == true){
                    Toast.makeText(getBaseContext(),"Будильник установлен на "+dayN+" день",Toast.LENGTH_SHORT).show();
                    return dayN;}
            }
            ++dayN;
            Log.i("newDayCheck","in while END dayN = "+dayN);
        }
        Toast.makeText(getBaseContext(),"Будильник установлен на "+dayN+" день",Toast.LENGTH_SHORT).show();
        return dayN;
    }
}
