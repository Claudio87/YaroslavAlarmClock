package com.example.alarm;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Context context;
    AlarmManager alarmManager;
    TimePicker timeDialog;
    DatePicker datePicker;
    TextView textView;
    ToggleButton startStopButton;
    private static final int DATE_DIALOG = 1;
    private static final int TIME_DIALOG = 2;
    int hour;
    int minute;
    Runnable runnable;
    AlarmMan alarmMan;
    CheckBox monday;
    CheckBox tuesday;
    CheckBox wednesday;
    CheckBox thursday;
    CheckBox friday;
    CheckBox saturday;
    CheckBox sunday;
    static WeeksDataBase weeksDataBase;
    Bundle test;
    DataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        test = savedInstanceState;
        Log.i("onCreate", "savedInstanceState = " +test);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        textView = (TextView) findViewById(R.id.textView);
        monday = (CheckBox) findViewById(R.id.mondayBox);
        tuesday = (CheckBox) findViewById(R.id.tuesdayBox);
        wednesday = (CheckBox) findViewById(R.id.wednesdayBox);
        thursday = (CheckBox) findViewById(R.id.thursdayBox);
        friday = (CheckBox) findViewById(R.id.fridayBox);
        saturday = (CheckBox) findViewById(R.id.saturdayBox);
        sunday = (CheckBox) findViewById(R.id.sundayBox);
        startStopButton = (ToggleButton) findViewById(R.id.toggleButton);
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        File customFile = new File(getFilesDir(), "Option.txt");
        db = new DataBase(this, DataBase.DATABASE_NAME,null,DataBase.DATABASE_VERSION);

//        boolean stat1 = monday.isChecked();
        if(db.readLineInTable(8) == true) {
            Log.i("MainA_onCreate", "readStatus...chosen");
            readStatus();
        }
        else {
            Log.i("MainA_onCreate", "initWeek...chosen");
            initWeekDayStatus();
        }
//        WeekDayStatus wdsMonday = new WeekDayStatus(stat1, 1, "Monday");
//        db.creatLineInTable(wdsMonday);
//        boolean mondayS = db.readLineInTable(1);
//        monday.setChecked(mondayS);
//        if(savedInstanceState != null)
//            db.updateStatus(wdsMonday);
        long time = currentDate.getTime();
//        Calendar mCalendar = Calendar.getInstance();

        textView.setText(sdf.format(currentDate));

//        if(savedInstanceState != null){
//            Log.i("saveInstanceState", "check ");


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean stat1 = monday.isChecked();
                WeekDayStatus wdsMonday = new WeekDayStatus(stat1, 1, "Monday");
                MainActivity.weeksDataBase.creatLineInTable(wdsMonday);
                boolean mondayS = weeksDataBase.readLineInTable(1);
                monday.setActivated(mondayS);

            }
        }
        );
        thread.setName("ThreadDB");
//        testDB();

//        }
    }

    public void testDB(){
        boolean stat1 = monday.isChecked();
        WeekDayStatus wdsMonday = new WeekDayStatus(stat1, 1, "Monday");
//        weeksDataBase.getWritableDatabase();
//        weeksDataBase.close();
        weeksDataBase.creatLineInTable(wdsMonday);
        boolean mondayS = weeksDataBase.readLineInTable(1);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("onResume", "savedInstanceState = " +test);
        weeksDataBase.CONTEXT = this.getApplicationContext();
        weeksDataBase = weeksDataBase.getDataBaseInstance();
//        boolean mondayS = weeksDataBase.readLineInTable(1);
//        monday.setActivated(mondayS);
    }

    private void alarmServiceTest(){
        Calendar mCalendar = Calendar.getInstance();
        Intent serviceIntent = new Intent(this, AlarmService.class);
        serviceIntent.putExtra("Calendar", mCalendar);

        startService(serviceIntent);
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
            writeCheckButton();

            runnable = new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(MainActivity.this, "Hour = "+hour+"\n Minute = "+minute,Toast.LENGTH_LONG).show();
//                    test();
                }
            };
            //запускаем новый поток, где создается календарь и alarmManager получает необходимые данные
            Thread serviceThread = new Thread(runnable);
            serviceThread.start();
        }
    };


    // передаю время в AlarmMan, где отправляю данные в AlarmService
    private void test(){
        alarmMan = new AlarmMan(hour,minute,MainActivity.this);
        alarmMan.setAlarm();
    }

//    private void getDataFromPickers(){
//        int hour = timeDialog.getCurrentHour();
//        int minute = timeDialog.getCurrentMinute();
//        Log.i("getDataFromPicker", "Hour = "+hour+"\n Minute = "+minute);
//    }

//    public void onStartButton(View view) {
//            showDialog(TIME_DIALOG);
//    }

    public void writeCheckButton(){
        try {
            OutputStream outputStream = openFileOutput("Option.txt",Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(outputStream);
            BufferedWriter bufferdWriter = new BufferedWriter(osw);
            String mondayCheck = String.valueOf("Monday is checked - "+monday);
            try {
                bufferdWriter.write(mondayCheck);
                bufferdWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void onStartStopButton(View view) {


        if (startStopButton.isChecked()){

//            if(db.readLineInTable(8) == true) {
                Log.i("MainA_onStartButton", "updateWeek on");
                updateWeekDayStatus();
            }
//            else {
//                Log.i("MainA_onStartButton", "initWeek...chosen");
//                initWeekDayStatus();
//            }

//
////            boolean stat1 = monday.isChecked();
////            WeekDayStatus wdsMonday = new WeekDayStatus(stat1, 1, "Monday");
////            weeksDataBase.creatLineInTable(wdsMonday);
//            boolean stat2 = tuesday.isChecked();
//            WeekDayStatus wdsTuesday = new WeekDayStatus(stat2, 2, "Tuesday");
//            boolean stat3 = wednesday.isChecked();
//            WeekDayStatus wdsWednesday = new WeekDayStatus(stat3, 3, "Wednesday");
//            boolean stat4 = thursday.isChecked();
//            WeekDayStatus wdsThursday = new WeekDayStatus(stat4, 4, "Thursday");
//            boolean stat5 = friday.isChecked();
//            WeekDayStatus wdsfriday = new WeekDayStatus(stat5, 5, "Friday");
//            boolean stat6 = saturday.isChecked();
//            WeekDayStatus wdsSaturday = new WeekDayStatus(stat6, 6, "Saturday");
//            boolean stat7 = sunday.isChecked();
//            WeekDayStatus wdsSunday = new WeekDayStatus(stat7, 7, "Sunday");
////            boolean [] dayStatus = new boolean[7];
////            dayStatus[0] = monday.isChecked();
////            dayStatus[1] = tuesday.isChecked();
////            dayStatus[2] = wednesday.isChecked();
////            dayStatus[3] = thursday.isChecked();
////            dayStatus[4] = friday.isChecked();
////            dayStatus[5] = saturday.isChecked();
////            dayStatus[6] = sunday.isChecked();
////            WeekDayStatus wds = new WeekDayStatus(dayStatus);
//
//            showDialog(TIME_DIALOG);
//        }
        else
            Toast.makeText(getApplicationContext(), "Будильник\nвыключен",Toast.LENGTH_SHORT).show();
    }

    private void updateWeekDayStatus(){
        boolean stat1 = monday.isChecked();
        WeekDayStatus wdsMonday = new WeekDayStatus(1, stat1);
        db.updateStatus(wdsMonday);
        boolean stat2 = tuesday.isChecked();
        WeekDayStatus wdsTuesday = new WeekDayStatus(2, stat2);
        db.updateStatus(wdsTuesday);
        boolean stat3 = wednesday.isChecked();
        WeekDayStatus wdsWednesday = new WeekDayStatus(3, stat3);
        db.updateStatus(wdsWednesday);
        boolean stat4 = thursday.isChecked();
        WeekDayStatus wdsThursday = new WeekDayStatus(4,stat4);
        db.updateStatus(wdsThursday);
        boolean stat5 = friday.isChecked();
        WeekDayStatus wdsFriday = new WeekDayStatus(5, stat5);
        db.updateStatus(wdsFriday);
        boolean stat6 = saturday.isChecked();
        WeekDayStatus wdsSaturday = new WeekDayStatus(6, stat6);
        db.updateStatus(wdsSaturday);
        boolean stat7 = sunday.isChecked();
        WeekDayStatus wdsSunday = new WeekDayStatus(7, stat7);
        db.updateStatus(wdsSunday);
    }

    private void initWeekDayStatus(){
        boolean stat1 = monday.isChecked();
        WeekDayStatus wdsMonday = new WeekDayStatus(stat1, 1, "Monday");
        db.creatLineInTable(wdsMonday);
        boolean stat2 = tuesday.isChecked();
        WeekDayStatus wdsTuesday = new WeekDayStatus(stat2, 2, "Tuesday");
        db.creatLineInTable(wdsTuesday);
        boolean stat3 = wednesday.isChecked();
        WeekDayStatus wdsWednesday = new WeekDayStatus(stat3, 3, "Wednesday");
        db.creatLineInTable(wdsWednesday);
        boolean stat4 = thursday.isChecked();
        WeekDayStatus wdsThursday = new WeekDayStatus(stat4, 4, "Thursday");
        db.creatLineInTable(wdsThursday);
        boolean stat5 = friday.isChecked();
        WeekDayStatus wdsFriday = new WeekDayStatus(stat5, 5, "Friday");
        db.creatLineInTable(wdsFriday);
        boolean stat6 = saturday.isChecked();
        WeekDayStatus wdsSaturday = new WeekDayStatus(stat6, 6, "Saturday");
        db.creatLineInTable(wdsSaturday);
        boolean stat7 = sunday.isChecked();
        WeekDayStatus wdsSunday = new WeekDayStatus(stat7, 7, "Sunday");
        db.creatLineInTable(wdsSunday);
        //init superDay
        boolean superDay = true;
        WeekDayStatus wdsSuper = new WeekDayStatus(superDay, 8, "Super");
        db.creatLineInTable(wdsSuper);
    }

    private void readStatus(){
        monday.setChecked(db.readLineInTable(1));
        tuesday.setChecked(db.readLineInTable(2));
        wednesday.setChecked(db.readLineInTable(3));
        thursday.setChecked(db.readLineInTable(4));
        friday.setChecked(db.readLineInTable(5));
        saturday.setChecked(db.readLineInTable(6));
        sunday.setChecked(db.readLineInTable(7));
    }
}
