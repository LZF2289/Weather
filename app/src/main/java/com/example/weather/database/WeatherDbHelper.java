// com/example/weather/database/WeatherDbHelper.java
package com.example.weather.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 数据库辅助类
 * 负责创建和升级数据库
 */
public class WeatherDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "WeatherDbHelper";

    // 数据库名称和版本
    private static final String DATABASE_NAME = "weather.db";
    private static final int DATABASE_VERSION = 1;

    // 表名和列名
    public static final String TABLE_HISTORY = "history";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_TEMP = "temperature";
    public static final String COLUMN_CONDITION = "condition";
    public static final String COLUMN_QUERY_TIME = "query_time";

    // 创建表的SQL语句
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_HISTORY + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CITY + " TEXT NOT NULL, " +
                    COLUMN_TEMP + " TEXT NOT NULL, " +
                    COLUMN_CONDITION + " TEXT NOT NULL, " +
                    COLUMN_QUERY_TIME + " TEXT NOT NULL);";

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "数据库辅助类已创建");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表
        db.execSQL(TABLE_CREATE);
        Log.d(TAG, "数据库表已创建: " + TABLE_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 升级数据库时，删除旧表并创建新表
        Log.w(TAG, "升级数据库从版本 " + oldVersion + " 到 " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }
}