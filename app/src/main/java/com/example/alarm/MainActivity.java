package com.example.alarm;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Context context;
    AlarmManager alarmManager;
    TimePicker timeDialog;
    DatePicker datePicker;
    private static final int DATE_DIALOG = 1;
    private static final int TIME_DIALOG = 2;
    int hour;
    int minute;
    int second;
    AlarmMan alarmMan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
//        DatePickerDialog datePickerDialog = new DatePickerDialog(this);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        timeDialog = (TimePicker) findViewById(R.id.timePicker);
//        datePicker = (DatePicker) findViewById(R.id.datePicker);




    }

    @Override
    protected Dialog onCreateDialog(int id) {

        int currentDialog = id;
        if (currentDialog == TIME_DIALOG) {
            TimePickerDialog tpd = new TimePickerDialog(this,timeListner,hour,minute,true);
            return tpd;
        }
        return super.onCreateDialog(id);
    }

    TimePickerDialog.OnTimeSetListener timeListner = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {

            hour = i;
            minute = i1;
            Toast.makeText(MainActivity.this, "Hour = "+hour+"\n Minute = "+minute,Toast.LENGTH_LONG).show();
            test();
        }
    };



    private void test(){
        alarmMan = new AlarmMan(hour,minute,MainActivity.this);
        alarmMan.setAlarm();
    }

//    @Nullable
//    @Override
//    protected Dialog onCreateDialog(int id, Bundle args) {
//
//
//        boolean test = true;
////        DatePickerDialog dpd = new DatePickerDialog(this, R.layout.datepicker_theme, myCallBack, a, b, c, test);
//        TimePickerDialog timePickerDialog = new TimePickerDialog(this,myCallBack,a,b,test);
//        return super.onCreateDialog(id, args);
//    }
//
//        TimePickerDialog.OnTimeSetListener myCallBack = new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int i, int i1) {
//
//            }
//        }
//    }

    private void getDataFromPickers(){
        int hour = timeDialog.getCurrentHour();
        int minute = timeDialog.getCurrentMinute();
        Log.i("getDataFromPicker", "Hour = "+hour+"\n Minute = "+minute);
    }

    public void onStartButton(View view) {
            showDialog(TIME_DIALOG);
//        Intent intent = new Intent(this, TestAlarm.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,intent,0);
//        alarmManager.set(AlarmManager.RTC_WAKEUP,5000,pendingIntent);
//        getDataFromPickers();
//        onCreateDialog()
    }
}
