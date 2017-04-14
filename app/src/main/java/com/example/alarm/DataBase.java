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

    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+WEEK_DAY +" TEXT,"
                +STATUS+" TEXT"+")";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST "+TABLE_NAME);
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
}
