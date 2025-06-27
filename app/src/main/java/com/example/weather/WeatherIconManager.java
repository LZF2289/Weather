package com.example.weather;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.appcompat.content.res.AppCompatResources;

import java.io.InputStream;

public class WeatherIconManager {
    private static final String TAG = "WeatherIconManager";

    //获取对应天气代码的图标

    public static Drawable getWeatherIcon(Context context, String weatherCode) {
        Log.d(TAG, "加载天气图标: " + weatherCode);
        
        if (weatherCode == null || weatherCode.isEmpty()) {
            Log.d(TAG, "天气代码为空，使用默认图标");
            return AppCompatResources.getDrawable(context, android.R.drawable.ic_menu_compass);
        }
        
        // 转换天气代码为整数
        int code;
        try {
            code = Integer.parseInt(weatherCode);
        } catch (NumberFormatException e) {
            Log.e(TAG, "无法解析天气代码: " + weatherCode, e);
            return AppCompatResources.getDrawable(context, android.R.drawable.ic_menu_compass);
        }

        // 根据当前主题选择图标颜色
        boolean useDarkTheme = (context.getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;

        String iconFolder = useDarkTheme ? "white" : "black";
        String iconSize = "@2x";
        String iconPath = "Pic/" + iconFolder + "/" + code + iconSize + ".png";

        Log.d(TAG, "图标路径: " + iconPath);

        try {
            // 从assets加载图标
            InputStream is = context.getAssets().open(iconPath);
            Drawable drawable = Drawable.createFromStream(is, null);
            is.close();

            if (drawable != null) {
                Log.d(TAG, "图标加载成功");
                return drawable;
            } else {
                Log.e(TAG, "加载图标返回null: " + iconPath);
            }
        } catch (Exception e) {
            Log.e(TAG, "加载天气图标失败: " + iconPath, e);
        }

        Log.d(TAG, "使用默认图标");
        return AppCompatResources.getDrawable(context, android.R.drawable.ic_menu_compass);
    }
}