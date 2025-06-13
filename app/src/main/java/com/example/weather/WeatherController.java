package com.example.weather;

public class WeatherController {

    private MainActivity view; // 持有View的引用，用于更新UI

    public WeatherController(MainActivity view) {
        this.view = view;
    }

    public void fetchWeatherData(String cityName) {
        // 在真实的App中，这里会进行API网络请求。
        // 目前，我们使用一些虚拟数据或模拟一个错误。

        if (cityName == null || cityName.trim().isEmpty()) {
            // 如果城市名为空，则创建一个错误数据对象
            WeatherData errorData = new WeatherData("请输入城市名称。");
            view.updateWeatherInfo(errorData); // 通知View更新UI
            return;
        }

        // 模拟一个已知城市的成功API调用
        if ("London".equalsIgnoreCase(cityName)) {
            WeatherData data = new WeatherData(
                    "London",
                    "15°C",
                    "多云",
                    "10 km/h",
                    "70%"
            );
            view.updateWeatherInfo(data); // 通知View更新UI
        } else if ("ErrorCity".equalsIgnoreCase(cityName)) { // 模拟一个错误情况
            WeatherData errorData = new WeatherData("城市未找到或网络错误。");
            view.updateWeatherInfo(errorData); // 通知View更新UI
        }
        else { // 模拟其他城市的成功API调用
            WeatherData data = new WeatherData(
                    cityName,
                    "20°C",
                    "晴朗",
                    "5 km/h",
                    "60%"
            );
            view.updateWeatherInfo(data); // 通知View更新UI
        }
    }
}