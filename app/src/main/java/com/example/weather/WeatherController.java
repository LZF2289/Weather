package com.example.weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.example.weather.api.WeatherAPI;

public class WeatherController {
    private static final String TAG = "WeatherController";

    private MainActivity view; // 更新UI
    private WeatherAPI weatherAPI; // API引用

    public WeatherController(MainActivity view) {
        this.view = view;
        this.weatherAPI = new WeatherAPI();
    }

    public void fetchWeatherData(String cityName) {
        if (cityName == null || cityName.trim().isEmpty()) {
            WeatherData errorData = new WeatherData("请输入城市名称");
            view.updateWeatherInfo(errorData);
            return;
        }

        Log.d(TAG, "获取天气数据: " + cityName);
        view.showLoading(true);

        // 获取今日天气数据
        weatherAPI.getWeatherByCityName(cityName, new WeatherAPI.WeatherCallback() {
            @Override
            public void onSuccess(WeatherData data) {
                Log.d(TAG, "获取今日天气数据成功");

                // 根据设置转换温度单位
                convertTemperatureIfNeeded(data);
                // 更新今日天气UI
                view.updateWeatherInfo(data);
                // 获取天气预报
                fetchForecastData(cityName);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e(TAG, "获取今日天气数据失败: " + errorMessage);
                view.showLoading(false);
                WeatherData errorData = new WeatherData(errorMessage);
                view.updateWeatherInfo(errorData);
            }
        });
    }

    // 获取多天天气预报
    private void fetchForecastData(String cityName) {
        weatherAPI.getWeatherForecast(cityName, new WeatherAPI.ForecastCallback() {
            @Override
            public void onSuccess(ForecastData forecastData) {
                Log.d(TAG, "获取多天天气预报成功");

                // 根据设置转换预报中的温度单位
                convertForecastTemperaturesIfNeeded(forecastData);
                view.showLoading(false);
                view.updateForecastInfo(forecastData);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e(TAG, "获取多天天气预报失败: " + errorMessage);
                view.showLoading(false);
            }
        });
    }
    
    public String convertTemperatureIfNeeded(String temperature) {
        if (shouldUseFahrenheit() && temperature != null && !temperature.isEmpty()) {
            return celsiusToFahrenheit(temperature);
        }
        return temperature;
    }
    
    // 检查是否需要转换温度单位并进行转换
    private void convertTemperatureIfNeeded(WeatherData data) {
        if (data == null || data.isError()) {
            return;
        }

        if (shouldUseFahrenheit()) {
            // 转换温度
            if (data.getTemperature() != null && !data.getTemperature().isEmpty()) {
                String fahrenheitTemp = celsiusToFahrenheit(data.getTemperature());
                data.setTemperature(fahrenheitTemp);
            }

            // 转换低温
            if (data.getLowTemp() != null && !data.getLowTemp().isEmpty()) {
                String fahrenheitLowTemp = celsiusToFahrenheit(data.getLowTemp());
                data.setLowTemp(fahrenheitLowTemp);
            }
        }
    }

    // 转换预报温度
    private void convertForecastTemperaturesIfNeeded(ForecastData forecastData) {
        if (forecastData == null || forecastData.isError() || forecastData.getDailyForecasts() == null) {
            return;
        }

        if (shouldUseFahrenheit()) {
            for (ForecastData.DayForecast day : forecastData.getDailyForecasts()) {
                // 转换高温
                if (day.getHighTemp() != null) {
                    String fahrenheitHighTemp = celsiusToFahrenheit(day.getHighTemp());
                    day.setHighTemp(fahrenheitHighTemp);
                }

                // 转换低温
                if (day.getLowTemp() != null) {
                    String fahrenheitLowTemp = celsiusToFahrenheit(day.getLowTemp());
                    day.setLowTemp(fahrenheitLowTemp);
                }
            }
        }
    }

    // 检查是否使用华氏度
    private boolean shouldUseFahrenheit() {
        SharedPreferences prefs = view.getSharedPreferences("WeatherAppPrefs", Context.MODE_PRIVATE);
        return prefs.getBoolean("use_fahrenheit", false);
    }

    // 摄氏度转华氏度
    private String celsiusToFahrenheit(String celsiusTemp) {
        try {
            // 移除温度符号
            String temp = celsiusTemp;
            if (temp.contains("°C")) {
                temp = temp.replace("°C", "");
            }
            temp = temp.trim();

            // 转换温度
            float celsius = Float.parseFloat(temp);
            float fahrenheit = celsius * 9/5 + 32;

            return String.format("%.1f°F", fahrenheit);
        } catch (NumberFormatException e) {
            Log.e(TAG, "温度转换错误: " + celsiusTemp, e);
            return celsiusTemp;  // 如果转换失败，返回原始温度
        }
    }
}