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
    Bundle test;
    DataBase db;
//    DataBaseHM dbhm;
//     weekDayStatus links
    private WeekDayStatus wdsMonday;
    private WeekDayStatus wdsTuesday;
    private WeekDayStatus wdsWednesday;
    private WeekDayStatus wdsThursday;
    private WeekDayStatus wdsFriday;
    private WeekDayStatus wdsSaturday;
    private WeekDayStatus wdsSunday;

    MainActivity mainActivity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        test = savedInstanceState;
        Log.i("onCreate", "savedInstanceState = " +test);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        db = new DataBase(this, DataBase.DATABASE_NAME,null,DataBase.DATABASE_VERSION);
//        dbhm = new DataBaseHM(this,DataBase.DATABASE_NAME, null,DataBaseHM.DATABASE_HM_VERSION);
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

        if(db.readLineInTable(8) == true) {
            Log.i("MainA_onCreate", "readStatus...chosen");
            readStatus();
        }
        else {
            Log.i("MainA_onCreate", "initWeek...chosen");
            initWeekDayStatus();
        }
        long time = currentDate.getTime();
        textView.setText(sdf.format(currentDate));

//        if(savedInstanceState != null){
//            Log.i("saveInstanceState", "check ");


//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                boolean stat1 = monday.isChecked();
//                WeekDayStatus wdsMonday = new WeekDayStatus(stat1, 1, "Monday");
//                MainActivity.weeksDataBase.creatLineInTable(wdsMonday);
//                boolean mondayS = weeksDataBase.readLineInTable(1);
//                monday.setActivated(mondayS);
//
//            }
//        }
//        );
//        thread.setName("ThreadDB");
//        testDB();

//        }
//        db.close();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("onResume", "savedInstanceState = " +test);
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
            if(db != null)
                db.timeLines(hour,minute);
            else{
                db = new DataBase(MainActivity.this, DataBase.DATABASE_NAME,null,DataBase.DATABASE_VERSION);
                db.timeLines(hour,minute);
            }
            Log.i("TimePickerDia", "часы и минуты переданы в БД для обработки....");
            test();
        }
    };

    // передаю время в AlarmMan, где отправляю данные в AlarmService
    private void test(){
        alarmMan = new AlarmMan(hour,minute,MainActivity.this);
        alarmMan.setAlarm();
    }

//    public void writeCheckButton(){
//        try {
//            OutputStream outputStream = openFileOutput("Option.txt",Context.MODE_PRIVATE);
//            OutputStreamWriter osw = new OutputStreamWriter(outputStream);
//            BufferedWriter bufferdWriter = new BufferedWriter(osw);
//            String mondayCheck = String.valueOf("Monday is checked - "+monday);
//            try {
//                bufferdWriter.write(mondayCheck);
//                bufferdWriter.flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    public void onStartStopButton(View view) {
        if (startStopButton.isChecked()){
                Log.i("MainA_onStartButton", "updateWeek on");
                newUpdateWeekStatus();
//            Intent serviceIntent = new Intent(MainActivity.this, AlarmService.class);
//            startService(serviceIntent);
            showDialog(TIME_DIALOG);
            }
        else
            Toast.makeText(getApplicationContext(), "Будильник\nвыключен",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private void updateTest(WeekDayStatus weekDayStatus, CheckBox day, int dayNumber){
        if(weekDayStatus != null){
            boolean dayChecked = day.isChecked();
            weekDayStatus.setStatus(dayChecked);
            db.updateStatus(weekDayStatus);
            Log.i("updateTest", "Link is already exists");
        }
        else{
            boolean dayChecked = day.isChecked();
            weekDayStatus = new WeekDayStatus(dayChecked,dayNumber,null);
            db.updateStatus(weekDayStatus);
            Log.i("updateTest", "Link was created");
        }
    }
    private void newUpdateWeekStatus(){
        mainActivity.updateTest(wdsMonday,monday,2);
        mainActivity.updateTest(wdsTuesday,tuesday,3);
        mainActivity.updateTest(wdsWednesday,wednesday,4);
        mainActivity.updateTest(wdsThursday,thursday,5);
        mainActivity.updateTest(wdsFriday,friday,6);
        mainActivity.updateTest(wdsSaturday,saturday,7);
        mainActivity.updateTest(wdsSunday,sunday,1);
    }

    private void initWeekDayStatus(){
        boolean stat7 = sunday.isChecked();
        wdsSunday = new WeekDayStatus(stat7, 1, "Sunday");
        db.creatLineInTable(wdsSunday);

        boolean stat1 = monday.isChecked();
        wdsMonday = new WeekDayStatus(stat1, 2, "Monday");
        db.creatLineInTable(wdsMonday);

        boolean stat2 = tuesday.isChecked();
        wdsTuesday = new WeekDayStatus(stat2, 3, "Tuesday");
        db.creatLineInTable(wdsTuesday);

        boolean stat3 = wednesday.isChecked();
        wdsWednesday = new WeekDayStatus(stat3, 4, "Wednesday");
        db.creatLineInTable(wdsWednesday);

        boolean stat4 = thursday.isChecked();
        wdsThursday = new WeekDayStatus(stat4, 5, "Thursday");
        db.creatLineInTable(wdsThursday);

        boolean stat5 = friday.isChecked();
        wdsFriday = new WeekDayStatus(stat5, 6, "Friday");
        db.creatLineInTable(wdsFriday);

        boolean stat6 = saturday.isChecked();
        wdsSaturday = new WeekDayStatus(stat6, 7, "Saturday");
        db.creatLineInTable(wdsSaturday);
        //init superDay
        boolean superDay = true;
        WeekDayStatus wdsSuper = new WeekDayStatus(superDay, 8, "Super");
        db.creatLineInTable(wdsSuper);
    }

    private void readStatus(){
        monday.setChecked(db.readLineInTable(2));
        tuesday.setChecked(db.readLineInTable(3));
        wednesday.setChecked(db.readLineInTable(4));
        thursday.setChecked(db.readLineInTable(5));
        friday.setChecked(db.readLineInTable(6));
        saturday.setChecked(db.readLineInTable(7));
        sunday.setChecked(db.readLineInTable(1));
    }
}
