package com.example.alarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by клаудио on 08.04.2017.
 */

public class DataBase extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "status_database";
    private static final String TABLE_NAME = "week_status";
    private static final String KEY_ID = "id";
    private static final String WEEK_DAY = "day";
    private static final String STATUS = "current_status";
    //Second table
    private static final String TABLE_HM_NAME = "hour_minute";
    private static final String KEY_HM_ID = "id";
    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";

    //Third table. Making it for save different state
    private static final String TABLE_OPTION = "option_intent";
    private static final String KEY_OPTION_ID = "id";
    private static final String OPTION = "option";


    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+WEEK_DAY +" TEXT,"
                +STATUS+" TEXT"+")";
        String CREATE_2_TABLE = "CREATE TABLE " + TABLE_HM_NAME + "("+KEY_HM_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+HOUR+" TEXT,"
                +MINUTE+" TEXT"+")";
        String CREATE_3_TABLE = "CREATE TABLE " + TABLE_OPTION + "("+ KEY_OPTION_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+ OPTION
                +" TEXT"+")";

        String CREATE_LINE1 = "INSERT INTO "+ TABLE_HM_NAME + "("+HOUR+", "+MINUTE+") VALUES "+"("+1+", "+1+");";
        String CREATE_LINE1_TABLE_OPTION = "INSERT INTO "+TABLE_OPTION + "("+OPTION+") VALUES "+"("+2+");";

        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_2_TABLE);
        sqLiteDatabase.execSQL(CREATE_LINE1);
        sqLiteDatabase.execSQL(CREATE_3_TABLE);
        sqLiteDatabase.execSQL(CREATE_LINE1_TABLE_OPTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST "+TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST "+TABLE_HM_NAME);
        onCreate(sqLiteDatabase);
    }

    public void creatLineInTable(WeekDayStatus weekDayStatus){
//
            SQLiteDatabase sqLiteDatabase = super.getWritableDatabase();
            String currentStatus = String.valueOf(weekDayStatus.isStatus());
            ContentValues contentValues = new ContentValues();
            contentValues.put(WEEK_DAY, weekDayStatus.getDay());
            contentValues.put(STATUS, currentStatus);
            sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
            boolean openDB = sqLiteDatabase.isOpen();
            boolean workDB = sqLiteDatabase.isDatabaseIntegrityOk();

            Log.i("InsertDB", "openDB = " + openDB + "\nworkDB = " + workDB);
            sqLiteDatabase.close();
        }

    public void updateStatus(WeekDayStatus weekDayStatus){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String currentStatus = String.valueOf(weekDayStatus.isStatus());
        String id = String.valueOf(weekDayStatus.getId());
        ContentValues values = new ContentValues();
        values.put(STATUS, currentStatus);
        String where = "update "+TABLE_NAME+" set "+STATUS+" = '"+currentStatus + "' where "+KEY_ID+" = "+id+";";
        Log.i("BD_UpdateStatus", "Day id = "+weekDayStatus.getId()+"\nstatus = "+weekDayStatus.isStatus());
//        sqLiteDatabase.update(TABLE_NAME,values,where, null);
        sqLiteDatabase.execSQL(where);
//        sqLiteDatabase.close();
    }

    public boolean readLineInTable(int i){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        boolean status = false;
        String statusInBase = "smt";
        String selectQuery = "SELECT * FROM "+TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.move(i))
            statusInBase = cursor.getString(2);
        if (statusInBase.equals("true"))
            status = true;
        cursor.close();
        Log.i("readLine...", "Status = "+status);
        return status;
    }

    //Second table manipulation

    // сохраняю часы и минуты в БД. Далее можно сделать отдлеьную базу для этих параметров.
    public void timeLines(int hour, int minute){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HOUR,hour);
        values.put(MINUTE,minute);
        String where = " id = 1;";
        sqLiteDatabase.update(TABLE_HM_NAME, values, where,null);
        Log.i("timeLines", "method finished...");
    }

    public int[] readTime(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        int hour = 0;
        int minute = 0;
        int[] time = new int[2];
        String selectQuery = "SELECT * FROM "+TABLE_HM_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            hour = Integer.parseInt(cursor.getString(1));
            minute = Integer.parseInt(cursor.getString(2));
            Log.i("readTime", "hour = "+hour+"\nminute = "+minute);
        }
            time[0] = hour;
            time[1] = minute;
        Log.i("readTime", "method finished...");
        return time;
    }

    // Third table manipulation
    // если опций (option) будет много можно на вход передавать номер ID и делать строки через StringBuilder
    public void setOption (String option){
        Log.i("setOption", "boolean option = "+option);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OPTION,option);
        String where = " id = 1";
        sqLiteDatabase.update(TABLE_OPTION, values, where,null);
        Log.i("setOption", "method finished...");
    }
    public void insertOption (String option){
        Log.i("insertOption", "boolean option = "+option);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OPTION,option);
        sqLiteDatabase.insert(TABLE_OPTION,null,values);
        Log.i("insertOption", "method finished...");
    }
    public String readOption(int id){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String option = "";
        String selectQuery = "SELECT * FROM "+TABLE_OPTION;
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            option = cursor.getString(id);
            Log.i("readOption", "option = "+option);
        }
        Log.i("readOption", "method finished...");
        return option;
    }

}
