package com.example.weather;

public class WeatherData {
    private String cityName; // 更改名称与getter/setter相匹配
    private String temperature;
    private String condition;
    private String windSpeed; // 更改名称与getter/setter相匹配
    private String humidity;
    private String lowTemp; // 新增最低温度
    private boolean error;
    private String errorMessage;
    private String code;

    public WeatherData() {
        // 默认构造函数
    }

    // 成功获取数据时的构造函数
    public WeatherData(String cityName, String highTemp, String lowTemp, String condition, String windSpeed, String humidity, String code) {
        this.cityName = cityName;
        this.temperature = highTemp;
        this.lowTemp = lowTemp;
        this.condition = condition;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.error = false;
        this.code = code;
    }

    // 发生错误时的构造函数
    public WeatherData(String errorMessage) {
        this.errorMessage = errorMessage;
        this.error = true; // 确保error标志被正确设置
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

    public String getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(String lowTemp) {
        this.lowTemp = lowTemp;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // 判断是否是错误数据
    public boolean isError() {
        return errorMessage != null;
    }
}