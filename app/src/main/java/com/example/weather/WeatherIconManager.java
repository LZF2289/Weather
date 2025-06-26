package com.example.weather;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.appcompat.content.res.AppCompatResources;

import java.io.InputStream;

public class WeatherIconManager {
    private static final String TAG = "WeatherIconManager";

    /**
     * 获取对应天气代码的图标
     * @param context 上下文
     * @param weatherCode 天气代码
     * @return 天气图标Drawable，如果无法加载则返回null
     */
    public static Drawable getWeatherIcon(Context context, String weatherCode) {
        if (weatherCode == null || weatherCode.isEmpty()) {
            return AppCompatResources.getDrawable(context, android.R.drawable.ic_menu_compass);
        }

        // 尝试将天气代码转换为整数
        int code;
        try {
            code = Integer.parseInt(weatherCode);
        } catch (NumberFormatException e) {
            Log.e(TAG, "无法解析天气代码: " + weatherCode, e);
            return AppCompatResources.getDrawable(context, android.R.drawable.ic_menu_compass);
        }

        // 确定使用白色或黑色图标
        boolean useDarkTheme = (context.getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;

        String iconFolder = useDarkTheme ? "white" : "black";
        String iconSize = "@2x"; // 使用高分辨率图标

        try {
            // 从assets加载
            String iconPath = "Pic/" + iconFolder + "/" + code + iconSize + ".png";

            InputStream is = context.getAssets().open(iconPath);
            Drawable drawable = Drawable.createFromStream(is, null);
            is.close();

            if (drawable != null) {
                return drawable;
            }
        } catch (Exception e) {
            Log.e(TAG, "无法加载天气图标: " + code, e);
        }

        // 如果无法加载，返回一个默认图标
        return AppCompatResources.getDrawable(context, android.R.drawable.ic_menu_compass);
    }

    /**
     * 根据天气状况文本获取最匹配的天气代码
     * @param condition 天气状况文本
     * @return 天气代码
     */
    public static String getWeatherCodeFromCondition(String condition) {
        if (condition == null) {
            return "99"; // 未知
        }

        condition = condition.toLowerCase();

        if (condition.contains("晴") && !condition.contains("阴") && !condition.contains("云")) {
            return "0"; // 晴
        } else if (condition.contains("多云") || condition.contains("云")) {
            if (condition.contains("晴间")) {
                return "5"; // 晴间多云
            } else if (condition.contains("大部")) {
                return "7"; // 大部多云
            } else {
                return "4"; // 多云
            }
        } else if (condition.contains("阴")) {
            return "9"; // 阴
        } else if (condition.contains("雷")) {
            if (condition.contains("冰雹")) {
                return "12"; // 雷阵雨伴有冰雹
            } else {
                return "11"; // 雷阵雨
            }
        } else if (condition.contains("雨")) {
            if (condition.contains("阵雨")) {
                return "10"; // 阵雨
            } else if (condition.contains("小雨")) {
                return "13"; // 小雨
            } else if (condition.contains("中雨")) {
                return "14"; // 中雨
            } else if (condition.contains("大雨")) {
                return "15"; // 大雨
            } else if (condition.contains("暴雨")) {
                if (condition.contains("大暴雨")) {
                    if (condition.contains("特大暴雨")) {
                        return "18"; // 特大暴雨
                    } else {
                        return "17"; // 大暴雨
                    }
                } else {
                    return "16"; // 暴雨
                }
            } else if (condition.contains("冻雨")) {
                return "19"; // 冻雨
            } else if (condition.contains("雨夹雪")) {
                return "20"; // 雨夹雪
            } else {
                return "13"; // 默认小雨
            }
        } else if (condition.contains("雪")) {
            if (condition.contains("阵雪")) {
                return "21"; // 阵雪
            } else if (condition.contains("小雪")) {
                return "22"; // 小雪
            } else if (condition.contains("中雪")) {
                return "23"; // 中雪
            } else if (condition.contains("大雪")) {
                return "24"; // 大雪
            } else if (condition.contains("暴雪")) {
                return "25"; // 暴雪
            } else {
                return "22"; // 默认小雪
            }
        } else if (condition.contains("雾")) {
            return "30"; // 雾
        } else if (condition.contains("霾")) {
            return "31"; // 霾
        } else if (condition.contains("沙尘暴")) {
            if (condition.contains("强")) {
                return "29"; // 强沙尘暴
            } else {
                return "28"; // 沙尘暴
            }
        } else if (condition.contains("扬沙")) {
            return "27"; // 扬沙
        } else if (condition.contains("浮尘")) {
            return "26"; // 浮尘
        } else if (condition.contains("风")) {
            if (condition.contains("大风")) {
                return "33"; // 大风
            } else if (condition.contains("飓风")) {
                return "34"; // 飓风
            } else if (condition.contains("龙卷风")) {
                return "36"; // 龙卷风
            } else if (condition.contains("热带风暴")) {
                return "35"; // 热带风暴
            } else {
                return "32"; // 风
            }
        } else if (condition.contains("热")) {
            return "38"; // 热
        } else if (condition.contains("冷")) {
            return "37"; // 冷
        }

        return "99"; // 未知
    }
}