package com.example.weather;

public class WeatherData {
    private String location;
    private String temperature;
    private String condition;
    private String wind;
    private String humidity;
    private String lowTemp; // 新增最低温度
    private boolean error;
    private String errorMessage;
    public WeatherData() {
        // 默认构造函数
    }

    // 成功获取数据时的构造函数
    public WeatherData(String location, String highTemp, String lowTemp, String condition, String wind, String humidity) {
        this.location = location;
        this.temperature = highTemp;
        this.lowTemp = lowTemp;
        this.condition = condition;
        this.wind = wind;
        this.humidity = humidity;
        this.error = false;
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