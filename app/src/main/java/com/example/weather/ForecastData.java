package com.example.weather;

import java.util.ArrayList;
import java.util.List;

public class ForecastData {
    private String cityName;
    private List<DayForecast> dailyForecasts;
    private boolean error;
    private String errorMessage;

    public ForecastData() {
        dailyForecasts = new ArrayList<>();
    }

    public ForecastData(String errorMessage) {
        this.error = true;
        this.errorMessage = errorMessage;
    }

    public void addDayForecast(DayForecast forecast) {
        if (dailyForecasts == null) {
            dailyForecasts = new ArrayList<>();
        }
        dailyForecasts.add(forecast);
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<DayForecast> getDailyForecasts() {
        return dailyForecasts;
    }

    public boolean isError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    // 内部类：单日预报
    public static class DayForecast {
        private String date;
        private String highTemp;
        private String lowTemp;
        private String condition;
        private String windDirection;
        private String windScale;
        private String humidity;

        public DayForecast(String date, String highTemp, String lowTemp, String condition,
                           String windDirection, String windScale, String humidity) {
            this.date = date;
            this.highTemp = highTemp;
            this.lowTemp = lowTemp;
            this.condition = condition;
            this.windDirection = windDirection;
            this.windScale = windScale;
            this.humidity = humidity;
        }

        public String getDate() {
            return date;
        }

        public String getHighTemp() {
            return highTemp;
        }

        public String getLowTemp() {
            return lowTemp;
        }

        public String getCondition() {
            return condition;
        }

        public String getWindDirection() {
            return windDirection;
        }

        public String getWindScale() {
            return windScale;
        }

        public String getHumidity() {
            return humidity;
        }

        public String getWindInfo() {
            if (windDirection != null && !windDirection.isEmpty()) {
                if (windScale != null && !windScale.isEmpty()) {
                    return windDirection + " " + windScale + "级";
                }
                return windDirection;
            }
            return "暂无风向数据";
        }
    }
}