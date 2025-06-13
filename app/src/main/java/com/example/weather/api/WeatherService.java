package com.example.weather.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    // 原来的实时天气API
    @GET("v3/weather/now.json")
    Call<WeatherResponse> getWeatherByCity(
            @Query("key") String key,
            @Query("location") String location,
            @Query("language") String language,
            @Query("unit") String unit
    );

    // 新增的天气预报API
    @GET("v3/weather/daily.json")
    Call<DailyWeatherResponse> getDailyWeatherByCity(
            @Query("key") String key,
            @Query("location") String location,
            @Query("language") String language,
            @Query("unit") String unit,
            @Query("start") int start,
            @Query("days") int days
    );
}