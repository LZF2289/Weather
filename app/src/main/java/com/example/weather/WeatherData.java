package com.example.weather;

public class WeatherData {
    private String cityName;    // 城市名称
    private String temperature; // 温度
    private String condition;   // 天气状况
    private String windSpeed;   // 风速
    private String humidity;    // 湿度
    private String errorMessage; // 用于显示错误信息

    public WeatherData() {
        // 默认构造函数
    }

    // 成功获取数据时的构造函数
    public WeatherData(String cityName, String temperature, String condition, String windSpeed, String humidity) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.condition = condition;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.errorMessage = null; // 没有错误
    }

    // 发生错误时的构造函数
    public WeatherData(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    // Getters 和 Setters 方法
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    // 判断是否是错误数据
    public boolean isError() {
        return errorMessage != null;
    }
}