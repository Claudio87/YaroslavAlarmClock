package com.example.alarm;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
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

    //Second Table for haours and minutes
//    public static final int DATABASE_HM_VERSION = 1;
    private static final String TABLE_HM_NAME = "hour_minute";
    private static final String KEY_HM_ID = "id";
    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";

    //Third table for pending intent
    private static final String TABLE_PI_NAME = "pending_intent";
    private static final String KEY_PI_ID = "id";
    private static final String P_INTENT = "pIntent";


    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+WEEK_DAY +" TEXT,"
                +STATUS+" TEXT"+")";
        String CREATE_2_TABLE = "CREATE TABLE " + TABLE_HM_NAME + "("+KEY_HM_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+HOUR+" TEXT,"
                +MINUTE+" TEXT"+")";
        String CREATE_3_TABLE = "CREATE TABLE " + TABLE_PI_NAME + "("+KEY_PI_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+P_INTENT
                +" TEXT"+")";

        String CREATE_LINE1 = "INSERT INTO "+ TABLE_HM_NAME + "("+HOUR+", "+MINUTE+") VALUES "+"("+1+", "+1+");";
        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_2_TABLE);
        sqLiteDatabase.execSQL(CREATE_LINE1);
//        sqLiteDatabase.execSQL(CREATE_3_TABLE);
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

    // сохраняет часы и минуты в БД. Далее можно сделать отдлеьную базу для этих параметров.
    public void saveHoMiDate(int hour, int minute){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WEEK_DAY,String.valueOf(hour));
        values.put(STATUS,String.valueOf(minute));
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
//
//    public void insertPenIntent(PendingIntent pendingIntent){
//        Log.i("insertPenIntent", "Method starts...");
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("pIntent", String.valueOf(pendingIntent));
//        sqLiteDatabase.insert(TABLE_PI_NAME, null, values);
//        sqLiteDatabase.close();
//        Log.i("insertPenIntent", "Method finished. pIntent = "+String.valueOf(pendingIntent));
//    }

////    public void readPenIntent(){
//        String pIntent = "";
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//        String query = "SELECT * FROM "+TABLE_PI_NAME;
//        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
//        if(cursor.moveToFirst())
//            pIntent = cursor.getString(1);
//            Uri mUri = Uri.parse(pIntent);
//        PendingIntent pendingIntent = (PendingIntent)pIntent.getBytes();

//        PendingIntent pendingIntent = pIntent;
//        PendingIntent pendingIntent = PendingIntent.

//    }

}
