package com.example.alarm;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_ALARM_EVENT = 1;
    public static final int CANCEL_ALARM_EVENT = 0;
    public static final int NEW_ALARM_EVENT_DAY_NOT_CHOOSEN = 2;
    public static final int CURRENT_DAY = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    Context context;
    private TextView textView;
    private TextView textViewNotify;
    private ToggleButton startStopButton;
    private static final int TIME_DIALOG = 2;
    private int hour;
    private int minute;
    private CheckBox monday;
    private CheckBox tuesday;
    private CheckBox wednesday;
    private CheckBox thursday;
    private CheckBox friday;
    private CheckBox saturday;
    private CheckBox sunday;
    private RelativeLayout relativeLayout;
    Bundle test;
    private DataBase db;
    boolean check;

    private WeekDayStatus wdsMonday;
    private WeekDayStatus wdsTuesday;
    private WeekDayStatus wdsWednesday;
    private WeekDayStatus wdsThursday;
    private WeekDayStatus wdsFriday;
    private WeekDayStatus wdsSaturday;
    private WeekDayStatus wdsSunday;

    private Date currentDate;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
    private TextView textViewMonday;
    private int alarmedDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onCreate", "static CURRENT_DAY = "+CURRENT_DAY);
        test = savedInstanceState;
        Log.i("onCreate", "savedInstanceState = " +test);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        db = new DataBase(getApplicationContext(), DataBase.DATABASE_NAME,null,DataBase.DATABASE_VERSION);
        textView   = (TextView) findViewById(R.id.textView);
        textViewNotify = (TextView) findViewById(R.id.textView2);
        textViewMonday = (TextView) findViewById(R.id.textViewMonday);
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int marginLeft = (width - 630)/2;
        Log.i("onCreate", "display width = "+width);
        monday     = (CheckBox) findViewById(R.id.checkBoxMonday);
        tuesday    = (CheckBox) findViewById(R.id.checkBoxTuesday);
        wednesday  = (CheckBox) findViewById(R.id.checkBoxWednesday);
        thursday   = (CheckBox) findViewById(R.id.checkBoxThursday);
        friday     = (CheckBox) findViewById(R.id.checkBoxFriday);
        saturday   = (CheckBox) findViewById(R.id.checkBoxSaturday);
        sunday     = (CheckBox) findViewById(R.id.checkBoxSunday);
        ((RelativeLayout.LayoutParams)textViewMonday.getLayoutParams()).setMargins(marginLeft,60,0,0);
        startStopButton = (ToggleButton) findViewById(R.id.toggleButton);
        File customFile = new File(getFilesDir(), "Option.txt");

        if(db.readLineInTable(8) == true) {
            Log.i("MainA_onCreate", "readStatus...chosen");
            readStatus();
            boolean b = Boolean.parseBoolean(db.readOption(1));
            Log.i("onCreate", "Table created, boolean b = "+b);
            startStopButton.setChecked(b);
        }
        else {
            Log.i("MainA_onCreate", "initWeek...chosen");
            initWeekDayStatus();
            db.setOption(String.valueOf(startStopButton.isChecked()));
            Log.i("onCreate", "Table not created, else version");
        }
        enableCheckBox();
        currentDate = new Date();
        textView.setText("Привет!"+"\nСегодня "+sdf.format(currentDate));
        alarmDay();


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
        Log.i("onResume", "REEESUME");
        super.onResume();
        currentDate = new Date();
        textView.setText("Привет!"+"\nСегодня "+sdf.format(currentDate));
        readStatus();
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
            final Intent serviceIntent = new Intent(getApplicationContext(), AlarmService.class);

            if(check == false) {
                Log.d("TimePicker listner","check == false");
                serviceIntent.addFlags(NEW_ALARM_EVENT_DAY_NOT_CHOOSEN);
            }
            else
                serviceIntent.addFlags(NEW_ALARM_EVENT);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    startService(serviceIntent);
                }
            });

            thread.start();
//            if(startStopButton.isChecked()) {//не работает
//                monday.setClickable(false);
//                Log.i("readStatus","startStopButton.isChecked = "+startStopButton.isChecked());
//            }
//            test();
        }
    };
//   if works delete
    // передаю время в AlarmMan, где отправляю данные в AlarmService
//    private void test(){
//        alarmMan = new AlarmMan(hour,minute,MainActivity.this);
//        alarmMan.setAlarm();
//    }

    public void onStartStopButton(View view) {
        if (startStopButton.isChecked()){
                Log.i("MainA_onStartButton", "updateWeek on");
                newUpdateWeekStatus();
            showDialog(TIME_DIALOG);
            db.setOption(String.valueOf(startStopButton.isChecked()));
            Log.i("onStartStopButton","startStopButton.isChecked() = "+startStopButton.isChecked());
            // исользую для check в readStatus
            readStatus();
            // если день/дни не были выбраны прежде чем нажать на триггер.
            if(check == false) {
                switch (CURRENT_DAY) {
                    case Calendar.MONDAY:
                        tuesday.setChecked(true);
                        break;
                    case Calendar.TUESDAY:
                        wednesday.setChecked(true);
                        break;
                    case Calendar.WEDNESDAY:
                        thursday.setChecked(true);
                        break;
                    case Calendar.THURSDAY:
                        friday.setChecked(true);
                        break;
                    case Calendar.FRIDAY:
                        saturday.setChecked(true);
                        break;
                    case Calendar.SATURDAY:
                        sunday.setChecked(true);
                        break;
                    case Calendar.SUNDAY:
                        monday.setChecked(true);
                        break;
                }
                newUpdateWeekStatus();

            }
            enableCheckBox();
        }
        else {
            Intent sIntent = new Intent(getApplicationContext(),AlarmService.class);
            sIntent.addFlags(CANCEL_ALARM_EVENT);
            startService(sIntent);
            enableCheckBox();
            Toast.makeText(getApplicationContext(), "Будильник\nвыключен", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        Log.d("onPause", "ONPAUSEEEE");
        super.onPause();
        //Сохраняю статус дней в БД
        newUpdateWeekStatus();
        //Сохраняю статус включателя в БД
        db.setOption(String.valueOf(startStopButton.isChecked()));
        //Закрываю БД
        db.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Сохраняю статус дней в БД
        newUpdateWeekStatus();
        //Сохраняю статус включателя в БД
        db.setOption(String.valueOf(startStopButton.isChecked()));
        //Закрываю БД
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
        // было mainActivity.updateTest...
        updateTest(wdsMonday,monday,2);
        updateTest(wdsTuesday,tuesday,3);
        updateTest(wdsWednesday,wednesday,4);
        updateTest(wdsThursday,thursday,5);
        updateTest(wdsFriday,friday,6);
        updateTest(wdsSaturday,saturday,7);
        updateTest(wdsSunday,sunday,1);
    }
    // use once, when application creates first time
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

        boolean[] status = new boolean[7];
        status[0] = monday.isChecked();
        status[1] = tuesday.isChecked();
        status[2] = wednesday.isChecked();
        status[3] = thursday.isChecked();
        status[4] = friday.isChecked();
        status[5] = saturday.isChecked();
        status[6] = sunday.isChecked();
        check = false;

        for(boolean b: status) {
            check = b;
            Log.d("readStatus", "forEach.."+b);
            if(check == true)
                return;
        }
    }

    private void enableCheckBox(){
        if(startStopButton.isChecked()){
            monday.setClickable(false);
            monday.setBackgroundColor(getResources().getColor(R.color.disabledCheckBox));
            tuesday.setClickable(false);
            tuesday.setBackgroundColor(getResources().getColor(R.color.disabledCheckBox));
            wednesday.setClickable(false);
            wednesday.setBackgroundColor(getResources().getColor(R.color.backgroundMain));
            thursday.setClickable(false);
            thursday.setBackgroundColor(getResources().getColor(R.color.disabledCheckBox));
            friday.setClickable(false);
            friday.setBackgroundColor(getResources().getColor(R.color.disabledCheckBox));
            saturday.setClickable(false);
            saturday.setBackgroundColor(getResources().getColor(R.color.disabledCheckBox));
            sunday.setClickable(false);
            sunday.setBackgroundColor(getResources().getColor(R.color.disabledCheckBox));
        }
        else
            disableCheckBox();
    }

    private void disableCheckBox(){
            monday.setClickable(true);
            monday.setBackgroundColor(getResources().getColor(R.color.backgroundMain));
            tuesday.setClickable(true);
            tuesday.setBackgroundColor(getResources().getColor(R.color.backgroundMain));
            wednesday.setClickable(true);
            wednesday.setBackgroundColor(getResources().getColor(R.color.backgroundMain));
            thursday.setClickable(true);
            thursday.setBackgroundColor(getResources().getColor(R.color.backgroundMain));
            friday.setClickable(true);
            friday.setBackgroundColor(getResources().getColor(R.color.backgroundMain));
            saturday.setClickable(true);
            saturday.setBackgroundColor(getResources().getColor(R.color.backgroundMain));
            sunday.setClickable(true);
            sunday.setBackgroundColor(getResources().getColor(R.color.backgroundMain));
    }

    private void alarmDay(){

        int alarmedDay = alarmDayCheck();

        WeekDays weekDays = WeekDays.FRIDAY;
        String day;

        switch(alarmedDay){
            case 1:
                weekDays = WeekDays.SUNDAY;
                break;
            case 2:
                weekDays = WeekDays.MONDAY;
                break;
            case 3:
                weekDays = WeekDays.TUESDAY;
                break;
            case 4:
                weekDays = WeekDays.WEDNESDAY;
                break;
            case 5:
                weekDays = WeekDays.THURSDAY;
                break;
            case 6:
                weekDays = WeekDays.FRIDAY;
                break;
            case 7:
                weekDays = WeekDays.SATURDAY;
                break;
        }
        day = weekDays.getDescription();
        if(startStopButton.isChecked())
            textViewNotify.setText("Будильник установлен на "+day);
    }

    private int alarmDayCheck(){
        try {
            alarmedDay = Integer.parseInt(db.readOption(2));
        }
        catch (Exception e){
            e.printStackTrace();
            Log.d("alarmDayCheck", "alarmedDay problem");
        }
        if(alarmedDay<7)
            alarmedDay++;
        else
            alarmedDay--;

        return alarmedDay;
    }

}
