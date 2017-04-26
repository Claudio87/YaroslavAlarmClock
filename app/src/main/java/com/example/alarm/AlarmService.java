package com.example.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;


public class AlarmService extends Service {

    TextView textView;
    private int hour;
    private int minute;
    private DataBase db;
    private PendingIntent sender;
//    private DataBaseHM dbhm;
    private Calendar mCalendar;
    AlarmManager aManager;
    private AlarmService alarmService;
    public AlarmService() {

    }

    protected AlarmService(Parcel in) {
        hour = in.readInt();
        minute = in.readInt();
        sender = in.readParcelable(PendingIntent.class.getClassLoader());
        alarmService = in.readParcelable(AlarmService.class.getClassLoader());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        alarmService = this;
        db = new DataBase(getBaseContext(),DataBase.DATABASE_NAME,null,DataBase.DATABASE_VERSION);
        Calendar mCalendar = Calendar.getInstance();
        Log.i("onStartCom..", "db = "+ db);
        //получаем из интента значения времени: часы и минуты
//        hour=intent.getIntExtra("Hour",0);
//        minute=intent.getIntExtra("Minute",0);
        int [] time = db.readTime();
        hour = time[0];
        minute = time[1];
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        mCalendar.set(Calendar.MINUTE, minute);

        int flag = intent.getIntExtra("Flag",0);
        Log.i("onStartCom..", "Hour = "+hour+"\nMinute = "+minute);

//        if(flag == AlarmClass.NEW_ALARM_TASK) {
//            Log.i("onStartCom..", "Flag NEW_ALARM_TASK");
//            newDayCheck();
//        }
//        else {
            int nextDay = newDayCheck();
            Intent alarmShow = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);
            aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            sender = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmShow, 0);
            mCalendar.set(Calendar.DAY_OF_WEEK, nextDay);
            aManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), sender);
//            db.insertPenIntent(sender);
//            serializAlSer();
//        }

//        stopSelf();

        return Service.START_STICKY;
    }

    private int newDayCheck(){
        Log.i("newDayCheck", "method start...");
//        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
        mCalendar = Calendar.getInstance();
        int dayN = 0;
        if(Calendar.getInstance().getTimeInMillis() >= mCalendar.getTimeInMillis()) {
            dayN = mCalendar.get(Calendar.DAY_OF_WEEK)+1;
            Log.i("newDayCheck","first if  dayN = "+dayN);
        }
        else
            dayN = mCalendar.get(Calendar.DAY_OF_WEEK);
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

    public void cancelAlarm(){
        Log.i("CancelAlarm", "aManager = "+aManager+"\nsender = "+sender);
        aManager.cancel(sender);
    }

    public PendingIntent getSender(){
        return sender;
    }

//    public void serializAlSer(){
//        Log.i("serializAlSer", "Serialize start...");
//        try {
//            File mFile = new File(getFilesDir(),"Sender.dat");
//            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(mFile));
//            oos.writeObject(alarmService);
////            FileOutputStream fos = new FileOutputStream("Sender.dat");
//            Log.i("serializAlSer", "pIntent = "+sender);
//
//            oos.flush();
//            oos.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Log.i("serializAlSer", "Serialize finished...");
//    }
    public void testCancelAlarm(){
        Intent alarmShow = new Intent(getBaseContext(), AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmShow, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

}
