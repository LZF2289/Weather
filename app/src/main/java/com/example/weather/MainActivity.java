package com.example.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.weather.database.WeatherHistoryManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private EditText editTextCityName;   // 城市名称输入框
    private MaterialButton buttonQuery;           // 查询按钮
    private TextView textViewCityName;            // 显示城市名称的TextView
    private TextView textViewTemperature;         // 显示温度的TextView
    private TextView textViewCondition;           // 显示天气状况的TextView
    private TextView textViewWindSpeed;           // 显示风速的TextView
    private TextView textViewHumidity;            // 显示湿度的TextView
    private TextView textViewError;               // 显示错误信息的TextView
    private ProgressBar progressBarLoading;       // 加载指示器
    private CardView weatherCard;                 // 今日天气卡片
    private ImageView imageViewWeatherIcon;       // 今日天气图标

    // 预报相关控件
    private TextView textViewForecastTitle;       // 预报标题
    private WeatherController controller;         // Controller的引用
    private LinearLayout containerForecast;       // 预报容器
    private WeatherHistoryManager historyManager;
    private MaterialButton buttonHistory;
    private FloatingActionButton fabRefresh;      // 刷新按钮
    private TextView textViewTitle;               // 标题文本
    private static final int REQUEST_HISTORY = 100;

    // 动画
    private Animation fadeIn;
    private Animation fadeOut;
    private Animation slideUp;
    private Animation slideInRight;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 初始化主题，必须在setContentView之前调用
        ThemeManager.initTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity onCreate");

        // 初始化动画
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);

        // 初始化UI控件
        editTextCityName = findViewById(R.id.editTextCityName);
        buttonQuery = findViewById(R.id.buttonQuery);
        progressBarLoading = findViewById(R.id.progressBarLoading);
        textViewError = findViewById(R.id.textViewError);

        // 初始化天气卡片
        weatherCard = findViewById(R.id.weatherCard);
        textViewCityName = weatherCard.findViewById(R.id.textViewCityName);
        textViewTemperature = weatherCard.findViewById(R.id.textViewTemperature);
        textViewCondition = weatherCard.findViewById(R.id.textViewCondition);
        textViewWindSpeed = weatherCard.findViewById(R.id.textViewWindSpeed);
        textViewHumidity = weatherCard.findViewById(R.id.textViewHumidity);
        imageViewWeatherIcon = weatherCard.findViewById(R.id.imageViewWeatherIcon);

        // 初始化预报UI控件
        textViewForecastTitle = findViewById(R.id.textViewForecastTitle);
        containerForecast = findViewById(R.id.containerForecast);
        textViewTitle = findViewById(R.id.textViewTitle);

        // 初始化Controller
        controller = new WeatherController(this);

        // 初始化历史记录和设置
        buttonHistory = findViewById(R.id.buttonHistory);
        MaterialButton buttonSettings = findViewById(R.id.buttonSettings);
        fabRefresh = findViewById(R.id.fabRefresh);

        // 设置历史记录按钮监听器
        buttonHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivityForResult(intent, REQUEST_HISTORY);
        });

        buttonSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        fabRefresh.setOnClickListener(v -> {
            String cityName = editTextCityName.getText().toString();
            if (!cityName.isEmpty()) {
                refreshWeatherData(cityName);
            }
        });

        historyManager = new WeatherHistoryManager(this);
        historyManager.open();

        // 设置查询按钮监听器
        buttonQuery.setOnClickListener(v -> {
            String cityName = editTextCityName.getText().toString();
            Log.d(TAG, "查询按钮点击: " + cityName);

            // 隐藏预报标题和清空容器
            textViewForecastTitle.setVisibility(View.GONE);
            containerForecast.removeAllViews();

            // 开始查询天气
            refreshWeatherData(cityName);
        });

        // 动画显示标题
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            textViewTitle.setAlpha(1);
            textViewTitle.startAnimation(fadeIn);
        }, 200);

        loadDefaultCity();
    }

    // 刷新天气数据
    private void refreshWeatherData(String cityName) {
        // 当按钮被点击时，隐藏卡片
        if (weatherCard.getVisibility() == View.VISIBLE) {
            weatherCard.startAnimation(fadeOut);
            weatherCard.setVisibility(View.GONE);
        }

        // 当按钮被点击时，调用Controller的方法获取天气数据
        controller.fetchWeatherData(cityName);
    }

    private void loadDefaultCity() {
        // 仅当输入框为空时才加载默认城市
        if (editTextCityName.getText().toString().trim().isEmpty()) {
            SharedPreferences settings = getSharedPreferences(SettingsActivity.PREFS_NAME, Context.MODE_PRIVATE);
            String defaultCity = settings.getString(SettingsActivity.KEY_DEFAULT_CITY, "");

            if (!defaultCity.isEmpty()) {
                editTextCityName.setText(defaultCity);
                controller.fetchWeatherData(defaultCity);
            }
        }
    }

    // 显示或隐藏加载中状态
    public void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBarLoading.setVisibility(View.VISIBLE);
            buttonQuery.setEnabled(false); // 禁用按钮防止重复点击
            fabRefresh.hide();
        } else {
            progressBarLoading.setVisibility(View.GONE);
            buttonQuery.setEnabled(true); // 启用按钮
            fabRefresh.show();
        }
    }

    // Controller调用此方法来更新今日天气View
    public void updateWeatherInfo(WeatherData data) {
        if (data.isError()) {
            // 如果是错误数据，显示错误信息
            Log.e(TAG, "显示错误信息: " + data.getErrorMessage());

            // 隐藏天气卡片
            if (weatherCard.getVisibility() == View.VISIBLE) {
                weatherCard.startAnimation(fadeOut);
                weatherCard.setVisibility(View.GONE);
            }

            // 显示错误信息
            textViewError.setText(data.getErrorMessage());
            textViewError.setVisibility(View.VISIBLE);
            textViewError.startAnimation(fadeIn);

        } else {
            // 如果数据正常，显示天气信息
            Log.d(TAG, "显示天气信息: " + data.getCityName());

            // 隐藏可能显示的错误信息
            textViewError.setVisibility(View.GONE);

            // 设置城市名和天气状况
            textViewCityName.setText(data.getCityName());
            textViewCondition.setText(data.getCondition());

            // 如果有低温数据，则显示温度范围
            if (data.getLowTemp() != null && !data.getLowTemp().isEmpty()) {
                textViewTemperature.setText(data.getLowTemp() + " ~ " + data.getTemperature());
            } else {
                textViewTemperature.setText(data.getTemperature());
            }

            textViewWindSpeed.setText("风速: " + data.getWindSpeed());
            textViewHumidity.setText("湿度: " + data.getHumidity());

            // 设置天气图标
            imageViewWeatherIcon.setImageDrawable(WeatherIconManager.getWeatherIcon(this, data.getCode()));

            // 显示天气卡片并添加动画
            weatherCard.setVisibility(View.VISIBLE);
            weatherCard.startAnimation(slideUp);

            // 保存查询历史
            if (historyManager != null) {
                historyManager.createHistory(
                        data.getCityName(),
                        data.getTemperature(),
                        data.getCondition()
                );
            }
        }
    }

    // 更新多天天气预报信息
    public void updateForecastInfo(ForecastData forecastData) {
        updateForecastInfo(forecastData, false); // 默认使用摄氏度
    }

    // 更新多天天气预报信息
    public void updateForecastInfo(ForecastData forecastData, boolean useFahrenheit) {
        if (forecastData.isError() || forecastData.getDailyForecasts().isEmpty()) {
            // 如果获取预报失败或数据为空，不显示预报
            textViewForecastTitle.setVisibility(View.GONE);
            containerForecast.removeAllViews();
            return;
        }

        // 显示预报标题并添加动画
        textViewForecastTitle.setVisibility(View.VISIBLE);
        textViewForecastTitle.startAnimation(fadeIn);

        // 清空当前预报容器
        containerForecast.removeAllViews();

        // 获取天气预报数据
        List<ForecastData.DayForecast> forecasts = forecastData.getDailyForecasts();

        // 从第二天开始显示，因为第一天在今日天气中已经显示了
        int startIndex = 1;

        // 遍历预报数据，为每天创建一个预报卡片
        Handler handler = new Handler(Looper.getMainLooper());
        for (int i = startIndex; i < forecasts.size(); i++) {
            final int delay = (i - startIndex) * 150; // 错开每个item的动画时间
            final ForecastData.DayForecast dayForecast = forecasts.get(i);

            handler.postDelayed(() -> {
                View forecastView = getLayoutInflater().inflate(R.layout.forecast_item, null);

                TextView textViewDate = forecastView.findViewById(R.id.textViewForecastDate);
                TextView textViewCondition = forecastView.findViewById(R.id.textViewForecastCondition);
                TextView textViewTemp = forecastView.findViewById(R.id.textViewForecastTemp);
                TextView textViewWind = forecastView.findViewById(R.id.textViewForecastWind);
                ImageView imageViewIcon = forecastView.findViewById(R.id.imageViewWeatherIcon);

                // 设置数据
                textViewDate.setText(dayForecast.getDate());
                textViewCondition.setText(dayForecast.getCondition());

                // 设置温度
                String highTemp = dayForecast.getHighTemp();
                String lowTemp = dayForecast.getLowTemp();

                if (useFahrenheit) {
                    // 使用controller提供的转换方法
                    highTemp = controller.convertTemperatureIfNeeded(highTemp);
                    lowTemp = controller.convertTemperatureIfNeeded(lowTemp);
                }

                String tempText = lowTemp + " ~ " + highTemp;
                textViewTemp.setText(tempText);

                textViewWind.setText(dayForecast.getWindInfo());

                // 设置图标
                imageViewIcon.setImageDrawable(WeatherIconManager.getWeatherIcon(MainActivity.this, dayForecast.getCode()));

                // 添加到容器并设置动画
                containerForecast.addView(forecastView);
                forecastView.startAnimation(slideInRight);
            }, delay);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (historyManager != null) {
            historyManager.open();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (historyManager != null) {
            historyManager.close();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_HISTORY && resultCode == RESULT_OK && data != null) {
            String cityName = data.getStringExtra("cityName");
            if (cityName != null && !cityName.isEmpty()) {
                // 设置城市名称
                editTextCityName.setText(cityName);
                // 自动查询该城市的天气
                controller.fetchWeatherData(cityName);
            }
        }
    }
}