// com/example/weather/database/WeatherHistoryManager.java
package com.example.weather.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 天气历史数据管理类
 * 负责与数据库进行交互，提供增删改查操作
 */
public class WeatherHistoryManager {
    private static final String TAG = "WeatherHistoryManager";

    private SQLiteDatabase database;
    private WeatherDbHelper dbHelper;
    private String[] allColumns = {
            WeatherDbHelper.COLUMN_ID,
            WeatherDbHelper.COLUMN_CITY,
            WeatherDbHelper.COLUMN_TEMP,
            WeatherDbHelper.COLUMN_CONDITION,
            WeatherDbHelper.COLUMN_QUERY_TIME
    };

    /**
     * 构造函数，初始化数据库辅助类
     */
    public WeatherHistoryManager(Context context) {
        dbHelper = new WeatherDbHelper(context);
        Log.d(TAG, "天气历史数据管理类已创建");
    }

    /**
     * 打开数据库连接
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.d(TAG, "数据库已打开");
    }

    /**
     * 关闭数据库连接
     */
    public void close() {
        dbHelper.close();
        Log.d(TAG, "数据库已关闭");
    }

    /**
     * 创建新的历史记录
     */
    public WeatherHistory createHistory(String city, String temp, String condition) {
        // 删除相同城市的旧记录
        database.delete(
                WeatherDbHelper.TABLE_HISTORY,
                WeatherDbHelper.COLUMN_CITY + " = ?",
                new String[] { city });

        // 创建新记录的值
        ContentValues values = new ContentValues();
        values.put(WeatherDbHelper.COLUMN_CITY, city);
        values.put(WeatherDbHelper.COLUMN_TEMP, temp);
        values.put(WeatherDbHelper.COLUMN_CONDITION, condition);

        // 获取当前时间作为查询时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        values.put(WeatherDbHelper.COLUMN_QUERY_TIME, currentTime);

        // 插入新记录
        long insertId = database.insert(WeatherDbHelper.TABLE_HISTORY, null, values);
        Log.d(TAG, "创建/更新历史记录, 城市: " + city + ", ID: " + insertId);

        // 获取插入的记录
        Cursor cursor = database.query(
                WeatherDbHelper.TABLE_HISTORY,
                allColumns,
                WeatherDbHelper.COLUMN_ID + " = " + insertId,
                null, null, null, null);
        cursor.moveToFirst();
        WeatherHistory newHistory = cursorToHistory(cursor);
        cursor.close();

        return newHistory;
    }

    /**
     * 删除历史记录
     */
    public void deleteHistory(WeatherHistory history) {
        long id = history.getId();
        database.delete(
                WeatherDbHelper.TABLE_HISTORY,
                WeatherDbHelper.COLUMN_ID + " = " + id,
                null);
        Log.d(TAG, "删除历史记录, ID: " + id);
    }

    /**
     * 清空所有历史记录
     */
    public void clearAllHistory() {
        database.delete(WeatherDbHelper.TABLE_HISTORY, null, null);
        Log.d(TAG, "清空所有历史记录");
    }

    /**
     * 获取所有历史记录
     */
    public List<WeatherHistory> getAllHistories() {
        List<WeatherHistory> histories = new ArrayList<>();

        // 查询所有记录，按查询时间降序排序
        Cursor cursor = database.query(
                WeatherDbHelper.TABLE_HISTORY,
                allColumns,
                null, null, null, null,
                WeatherDbHelper.COLUMN_QUERY_TIME + " DESC");

        Log.d(TAG, "查询到 " + cursor.getCount() + " 条历史记录");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            WeatherHistory history = cursorToHistory(cursor);
            histories.add(history);
            cursor.moveToNext();
        }
        cursor.close();

        return histories;
    }

    /**
     * 将Cursor转换为WeatherHistory对象
     */
    private WeatherHistory cursorToHistory(Cursor cursor) {
        WeatherHistory history = new WeatherHistory();
        history.setId(cursor.getLong(0));
        history.setCityName(cursor.getString(1));
        history.setTemperature(cursor.getString(2));
        history.setCondition(cursor.getString(3));
        history.setQueryTime(cursor.getString(4));
        return history;
    }
}