package com.example.alarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by клаудио on 13.04.2017.
 */

public class DataBaseHM extends SQLiteOpenHelper {

    public static final int DATABASE_HM_VERSION = 1;
    public static final String DATABASE_HM_NAME = "database_2";
    private static final String TABLE_HM_NAME = "hour_minute";
    private static final String KEY_HM_ID = "id";
    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";


    public DataBaseHM(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_2_TABLE = "CREATE TABLE "+TABLE_HM_NAME+"("+KEY_HM_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+HOUR+" TEXT,"+MINUTE
                +" TEXT"+")";
        sqLiteDatabase.execSQL(CREATE_2_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP IF EXIST "+DATABASE_HM_NAME);
        onCreate(sqLiteDatabase);
    }
}
