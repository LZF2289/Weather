package com.example.weather.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DailyWeatherResponse {
    @SerializedName("results")
    private List<Result> results;

    public List<Result> getResults() {
        return results;
    }

    public static class Result {
        @SerializedName("location")
        private Location location;

        @SerializedName("daily")
        private List<Daily> daily;

        @SerializedName("last_update")
        private String lastUpdate;

        public Location getLocation() {
            return location;
        }

        public List<Daily> getDaily() {
            return daily;
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

    public static class Daily {
        @SerializedName("date")
        private String date;

        @SerializedName("text_day")
        private String textDay;

        @SerializedName("code_day")
        private String codeDay;

        @SerializedName("text_night")
        private String textNight;

        @SerializedName("code_night")
        private String codeNight;

        @SerializedName("high")
        private String high;

        @SerializedName("low")
        private String low;

        @SerializedName("precip")
        private String precip;

        @SerializedName("wind_direction")
        private String windDirection;

        @SerializedName("wind_direction_degree")
        private String windDirectionDegree;

        @SerializedName("wind_speed")
        private String windSpeed;

        @SerializedName("wind_scale")
        private String windScale;

        @SerializedName("rainfall")
        private String rainfall;

        @SerializedName("humidity")
        private String humidity;

        public String getDate() {
            return date;
        }

        public String getTextDay() {
            return textDay;
        }

        public String getCodeDay() {
            return codeDay;
        }

        public String getHigh() {
            return high;
        }

        public String getLow() {
            return low;
        }

        public String getWindDirection() {
            return windDirection;
        }

        public String getWindSpeed() {
            return windSpeed;
        }

        public String getWindScale() {
            return windScale;
        }

        public String getHumidity() {
            return humidity;
        }
    }
}