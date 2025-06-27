package com.example.weather.api;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.weather.ForecastData;
import com.example.weather.WeatherData;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherAPI {
    private static final String TAG = "WeatherAPI";
    private static final String BASE_URL = "https://api.seniverse.com/";
    private static final String API_KEY = "Sd0eZ-fESIrjGeEj7"; // 私钥

    private final WeatherService weatherService;
    private final Handler mainHandler;

    public interface WeatherCallback {
        void onSuccess(WeatherData weatherData);
        void onFailure(String errorMessage);
    }

    public interface ForecastCallback {
        void onSuccess(ForecastData forecastData);
        void onFailure(String errorMessage);
    }

    public WeatherAPI() {
        // 配置OkHttp客户端
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        // 创建Retrofit实例
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 创建API服务
        weatherService = retrofit.create(WeatherService.class);
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public void getWeatherByCityName(String cityName, final WeatherCallback callback) {
        Log.d(TAG, "获取天气数据: " + cityName);

        Call<DailyWeatherResponse> call = weatherService.getDailyWeatherByCity(
                API_KEY,      // 私钥
                cityName,     // 城市名称
                "zh-Hans",    // 简体中文
                "c",          // 摄氏度
                0,            // 从今天开始
                1             // 只获取一天的数据
        );

        // 执行异步请求
        call.enqueue(new Callback<DailyWeatherResponse>() {
            @Override
            public void onResponse(Call<DailyWeatherResponse> call, Response<DailyWeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        DailyWeatherResponse weatherResponse = response.body();
                        if (weatherResponse.getResults() == null || weatherResponse.getResults().isEmpty()) {
                            mainHandler.post(() -> callback.onFailure("未找到天气数据"));
                            return;
                        }

                        // 提取天气数据
                        DailyWeatherResponse.Result result = weatherResponse.getResults().get(0);
                        DailyWeatherResponse.Location location = result.getLocation();

                        // 获取今天的天气
                        if (result.getDaily() == null || result.getDaily().isEmpty()) {
                            mainHandler.post(() -> callback.onFailure("未找到今日天气数据"));
                            return;
                        }

                        DailyWeatherResponse.Daily today = result.getDaily().get(0);

                        // 获取风向和风力
                        String windInfo = "";
                        if (today.getWindDirection() != null && !today.getWindDirection().isEmpty()) {
                            windInfo = today.getWindDirection();
                        }
                        if (today.getWindScale() != null && !today.getWindScale().isEmpty()) {
                            windInfo += " " + today.getWindScale() + "级";
                        } else if (today.getWindSpeed() != null && !today.getWindSpeed().isEmpty()) {
                            windInfo += " " + today.getWindSpeed() + "km/h";
                        }

                        if (windInfo.isEmpty()) {
                            windInfo = "暂无风向数据";
                        }

                        // 获取湿度
                        String humidity = today.getHumidity() != null && !today.getHumidity().isEmpty()
                                ? today.getHumidity() + "%" : "暂无湿度数据";

                        // 创建天气数据对象
                        final WeatherData weatherData = new WeatherData(
                                location.getName(),
                                today.getHigh() + "°C",
                                today.getLow() + "°C",
                                today.getTextDay(),
                                windInfo,
                                humidity,
                                today.getCodeDay()
                        );

                        Log.d(TAG, "获取天气数据成功: " + location.getName() + ", " + today.getTextDay());
                        // 在主线程更新UI
                        mainHandler.post(() -> callback.onSuccess(weatherData));

                    } catch (Exception e) {
                        String errorMsg = "解析天气数据失败: " + e.getMessage();
                        Log.e(TAG, errorMsg, e);
                        mainHandler.post(() -> callback.onFailure(errorMsg));
                    }
                } else {
                    // 请求失败处理
                    String errorMsg = "API请求失败: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            errorMsg += " - " + response.errorBody().string();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "请求失败");
                    }

                    Log.e(TAG, errorMsg);
                    final String finalErrorMsg = errorMsg;
                    mainHandler.post(() -> callback.onFailure(finalErrorMsg));
                }
            }

            @Override
            public void onFailure(Call<DailyWeatherResponse> call, Throwable t) {
                String errorMsg = "网络请求失败: " + t.getMessage();
                Log.e(TAG, errorMsg, t);
                mainHandler.post(() -> callback.onFailure(errorMsg));
            }
        });
    }

    // 获取多天天气预报
    public void getWeatherForecast(String cityName, final ForecastCallback callback) {
        Log.d(TAG, "获取天气预报: " + cityName);

        Call<DailyWeatherResponse> call = weatherService.getDailyWeatherByCity(
                API_KEY,      // 私钥
                cityName,     // 城市名称
                "zh-Hans",    // 简体中文
                "c",          // 摄氏度
                0,            // 从今天开始
                3             // 获取3天的数据
        );

        // 执行异步请求
        call.enqueue(new Callback<DailyWeatherResponse>() {
            @Override
            public void onResponse(Call<DailyWeatherResponse> call, Response<DailyWeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        DailyWeatherResponse weatherResponse = response.body();
                        if (weatherResponse.getResults() == null || weatherResponse.getResults().isEmpty()) {
                            mainHandler.post(() -> callback.onFailure("未找到天气预报数据"));
                            return;
                        }

                        // 提取天气数据
                        DailyWeatherResponse.Result result = weatherResponse.getResults().get(0);
                        DailyWeatherResponse.Location location = result.getLocation();

                        // 获取多天天气
                        if (result.getDaily() == null || result.getDaily().isEmpty()) {
                            mainHandler.post(() -> callback.onFailure("未找到天气预报数据"));
                            return;
                        }

                        // 创建预报数据对象
                        final ForecastData forecastData = getForecastData(location, result);

                        Log.d(TAG, "获取天气预报成功: " + location.getName() + ", 共 " + result.getDaily().size() + " 天");
                        // 在主线程更新UI
                        mainHandler.post(() -> callback.onSuccess(forecastData));

                    } catch (Exception e) {
                        String errorMsg = "解析天气预报数据失败: " + e.getMessage();
                        Log.e(TAG, errorMsg, e);
                        mainHandler.post(() -> callback.onFailure(errorMsg));
                    }
                } else {
                    // 请求失败处理
                    String errorMsg = "API请求失败: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            errorMsg += " - " + response.errorBody().string();
                        }
                    } catch (Exception e) {
                        // 忽略错误体解析异常
                    }

                    Log.e(TAG, errorMsg);
                    final String finalErrorMsg = errorMsg;
                    mainHandler.post(() -> callback.onFailure(finalErrorMsg));
                }
            }

            @NonNull
            private ForecastData getForecastData(DailyWeatherResponse.Location location, DailyWeatherResponse.Result result) {
                final ForecastData forecastData = new ForecastData();
                forecastData.setCityName(location.getName());

                // 添加每天的预报
                for (DailyWeatherResponse.Daily day : result.getDaily()) {
                    ForecastData.DayForecast dayForecast = new ForecastData.DayForecast(
                            day.getDate(),
                            day.getHigh() + "°C",
                            day.getLow() + "°C",
                            day.getTextDay(),
                            day.getWindDirection(),
                            day.getWindScale(),
                            day.getHumidity() + "%",
                            day.getCodeDay()
                    );
                    forecastData.addDayForecast(dayForecast);
                }
                return forecastData;
            }

            @Override
            public void onFailure(Call<DailyWeatherResponse> call, Throwable t) {
                String errorMsg = "网络请求失败: " + t.getMessage();
                Log.e(TAG, errorMsg, t);
                mainHandler.post(() -> callback.onFailure(errorMsg));
            }
        });
    }
}