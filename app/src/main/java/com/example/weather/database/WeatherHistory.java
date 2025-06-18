// com/example/weather/database/WeatherHistory.java
package com.example.weather.database;

/**
 * 天气历史记录数据类
 * 用于存储用户查询过的城市及其天气信息
 */
public class WeatherHistory {
    private long id;
    private String cityName;      // 城市名称
    private String temperature;   // 温度
    private String condition;     // 天气状况(如晴、雨等)
    private String queryTime;     // 查询时间

    // 默认构造函数
    public WeatherHistory() {
    }

    // 带参数的构造函数，不含ID(由数据库自动生成)
    public WeatherHistory(String cityName, String temperature, String condition, String queryTime) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.condition = condition;
        this.queryTime = queryTime;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(String queryTime) {
        this.queryTime = queryTime;
    }

    @Override
    public String toString() {
        return "WeatherHistory{" +
                "id=" + id +
                ", cityName='" + cityName + '\'' +
                ", temperature='" + temperature + '\'' +
                ", condition='" + condition + '\'' +
                ", queryTime='" + queryTime + '\'' +
                '}';
    }
}