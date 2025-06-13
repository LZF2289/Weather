package com.example.weather;

import android.util.Log;
import com.example.weather.api.WeatherAPI;

public class WeatherController {
    private static final String TAG = "WeatherController";

    private MainActivity view; // 持有View的引用，用于更新UI
    private WeatherAPI weatherAPI; // API引用

    public WeatherController(MainActivity view) {
        this.view = view;
        this.weatherAPI = new WeatherAPI(); // 初始化API
    }

    public void fetchWeatherData(String cityName) {
        if (cityName == null || cityName.trim().isEmpty()) {
            // 如果城市名为空，则创建一个错误数据对象
            WeatherData errorData = new WeatherData("请输入城市名称");
            view.updateWeatherInfo(errorData); // 通知View更新UI
            return;
        }

        Log.d(TAG, "开始获取天气数据: " + cityName);
        view.showLoading(true); // 显示加载中状态

        // 调用API获取天气数据
        weatherAPI.getWeatherByCityName(cityName, new WeatherAPI.WeatherCallback() {
            @Override
            public void onSuccess(WeatherData data) {
                Log.d(TAG, "获取天气数据成功");
                view.showLoading(false); // 隐藏加载中状态
                view.updateWeatherInfo(data); // 通知View更新UI
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e(TAG, "获取天气数据失败: " + errorMessage);
                view.showLoading(false); // 隐藏加载中状态
                WeatherData errorData = new WeatherData(errorMessage);
                view.updateWeatherInfo(errorData); // 通知View更新UI
            }
        });
    }
}