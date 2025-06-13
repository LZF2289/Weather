package com.example.weather.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {
    @SerializedName("results")
    private List<Result> results;

    public List<Result> getResults() {
        return results;
    }

    public static class Result {
        @SerializedName("location")
        private Location location;

        @SerializedName("now")
        private Now now;

        @SerializedName("last_update")
        private String lastUpdate;

        public Location getLocation() {
            return location;
        }

        public Now getNow() {
            return now;
        }

        public String getLastUpdate() {
            return lastUpdate;
        }
    }

    public static class Location {
        @SerializedName("id")
        private String id;

        @SerializedName("name")
        private String name;

        @SerializedName("country")
        private String country;

        @SerializedName("path")
        private String path;

        @SerializedName("timezone")
        private String timezone;

        @SerializedName("timezone_offset")
        private String timezoneOffset;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCountry() {
            return country;
        }
    }

    public static class Now {
        @SerializedName("text")
        private String text;

        @SerializedName("code")
        private String code;

        @SerializedName("temperature")
        private String temperature;

        @SerializedName("feels_like")
        private String feelsLike;

        @SerializedName("pressure")
        private String pressure;

        @SerializedName("humidity")
        private String humidity;

        @SerializedName("visibility")
        private String visibility;

        @SerializedName("wind_direction")
        private String windDirection;

        @SerializedName("wind_direction_degree")
        private String windDirectionDegree;

        @SerializedName("wind_speed")
        private String windSpeed;

        @SerializedName("wind_scale")
        private String windScale;

        @SerializedName("clouds")
        private String clouds;

        @SerializedName("dew_point")
        private String dewPoint;

        public String getText() {
            return text;
        }

        public String getCode() {
            return code;
        }

        public String getTemperature() {
            return temperature;
        }

        public String getHumidity() {
            return humidity;
        }

        public String getWindDirection() {
            return windDirection;
        }

        public String getWindScale() {
            return windScale;
        }
    }
}