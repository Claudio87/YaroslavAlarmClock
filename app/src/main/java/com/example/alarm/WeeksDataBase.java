package com.example.alarm;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by клаудио on 02.04.2017.
 */

public class WeeksDataBase extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "status_database";
    private static final String TABLE_NAME = "week_status";
    private static final String KEY_ID = "id";
    private static final String WEEK_DAY = "day";
    private static final String STATUS = "current_status";
//    private SQLiteDatabase db;
    public static Context CONTEXT;
    private WeeksDataBase weeksDataBase;
    private WeeksDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.CONTEXT = context;
    }

    public void setContext(Context context){
        CONTEXT = context;
    }

    //singleton DB link;
    private static WeeksDataBase singleTonLink = new WeeksDataBase(CONTEXT,TABLE_NAME, null, DATABASE_VERSION);

        public static WeeksDataBase getDataBaseInstance(){
            if (singleTonLink != null)
            Log.i("Singleton", "instance != null ");
            return singleTonLink;
        }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("OnCreateDB", "onCreate starts");
         String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+WEEK_DAY+"TEXT,"
                +STATUS+" TEXT);";
        sqLiteDatabase.execSQL(CREATE_TABLE);
//        sqLiteDatabase.endTransaction();
//        db = sqLiteDatabase;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXIST "+TABLE_NAME);
            onCreate(sqLiteDatabase);

    }

    //CRUD model CREATE AND UPDATE only
    public void creatLineInTable(WeekDayStatus weekDayStatus){
//            String dayStatus = "false";
//            if (dayChecked)
//                dayStatus = "true";
//        Log.i("createLineInTable", "String dayStatus = "+dayStatus);
//        String INSERT_LINE = "INSERT INTO "+TABLE_NAME+" ("+WEEK_DAY+", "+STATUS+") VALUES "+"("+weekDay+", "+dayChecked+");";
//        if (db.isOpen()) {
        Log.i("InsertDB", "before insert");
        if (singleTonLink != null) {
            SQLiteDatabase sqLiteDatabase = super.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(WEEK_DAY, weekDayStatus.getDay());
//            contentValues.put(STATUS, weekDayStatus.stringStatus());
            sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
            boolean openDB = sqLiteDatabase.isOpen();
            boolean workDB = sqLiteDatabase.isDatabaseIntegrityOk();

            Log.i("InsertDB", "openDB = " + openDB + "\nworkDB = " + workDB);
            sqLiteDatabase.close();
        }
//        }
//        else
//            Log.i("createLineInTable", "База не открыта");
    }

    public void updateLineInTable(WeekDayStatus weekDayStatus){
//        String dayStatus = "false";
//        if(dayChecked)
//            dayStatus = "true";
//        Log.i("updateLineInTable", "String dayStatus = "+dayStatus);
//        String UPDATE_LINE = "";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WEEK_DAY, weekDayStatus.getDay());
        contentValues.put(STATUS, weekDayStatus.isStatus());// проверить как отображается в базе
        sqLiteDatabase.update(TABLE_NAME, contentValues,String.valueOf(weekDayStatus.getId()),null);
    }

    public boolean readLineInTable(int i){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//        ContentValues contentValues = new ContentValues();
        boolean status = false;
        String statusInBase = "smt";
        String selectQuery = "SELECT * FROM "+TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToPosition(i)) {
            statusInBase = cursor.getString(2);
            Log.i("readLineInTable", "statusInBase = " + statusInBase);
            if (statusInBase.equals("1"))
                status = true;
            Log.i("BD_read", "Status = " + status);
            cursor.close();

        }
            return status;
    }
}