package com.example.weather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.example.weather.database.WeatherHistoryManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private EditText editTextCityName;      // 城市名称输入框
    private Button buttonQuery;             // 查询按钮
    private TextView textViewCityName;      // 显示城市名称的TextView
    private TextView textViewTemperature;   // 显示温度的TextView
    private TextView textViewCondition;     // 显示天气状况的TextView
    private TextView textViewWindSpeed;     // 显示风速的TextView
    private TextView textViewHumidity;      // 显示湿度的TextView
    private TextView textViewError;         // 显示错误信息的TextView
    private ProgressBar progressBarLoading; // 加载指示器

    // 预报相关控件
    private TextView textViewForecastTitle; // 预报标题
    private WeatherController controller;   // Controller的引用
    private LinearLayout containerForecast; // 预报容器
    private WeatherHistoryManager historyManager;
    private Button buttonHistory;
    private static final int REQUEST_HISTORY = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity onCreate");

        // 初始化UI控件
        editTextCityName = findViewById(R.id.editTextCityName);
        buttonQuery = findViewById(R.id.buttonQuery);
        textViewCityName = findViewById(R.id.textViewCityName);
        textViewTemperature = findViewById(R.id.textViewTemperature);
        textViewCondition = findViewById(R.id.textViewCondition);
        textViewWindSpeed = findViewById(R.id.textViewWindSpeed);
        textViewHumidity = findViewById(R.id.textViewHumidity);
        textViewError = findViewById(R.id.textViewError);
        progressBarLoading = findViewById(R.id.progressBarLoading);

        // 初始化预报UI控件
        textViewForecastTitle = findViewById(R.id.textViewForecastTitle);
        containerForecast = findViewById(R.id.containerForecast);

        // 初始化Controller
        controller = new WeatherController(this);

        //初始化历史记录和设置
        buttonHistory = findViewById(R.id.buttonHistory);
        Button buttonSettings = findViewById(R.id.buttonSettings);
        //设置历史记录按钮监听器
        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivityForResult(intent, REQUEST_HISTORY);
            }
        });
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        historyManager = new WeatherHistoryManager(this);
        historyManager.open();

        // 设置查询按钮监听器
        buttonQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = editTextCityName.getText().toString();
                Log.d(TAG, "查询按钮点击: " + cityName);

                // 隐藏预报标题和清空容器
                textViewForecastTitle.setVisibility(View.GONE);
                containerForecast.removeAllViews();

                // 当按钮被点击时，调用Controller的方法获取天气数据
                controller.fetchWeatherData(cityName);
            }
        });
        APITest.testAPI();
        loadDefaultCity();
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
        } else {
            progressBarLoading.setVisibility(View.GONE);
            buttonQuery.setEnabled(true); // 启用按钮
        }
    }

    // Controller调用此方法来更新今日天气View
    public void updateWeatherInfo(WeatherData data) {
        if (data.isError()) {
            // 如果是错误数据，显示错误信息
            Log.e(TAG, "显示错误信息: " + data.getErrorMessage());
            textViewCityName.setText("城市: ");
            textViewTemperature.setText("温度: ");
            textViewCondition.setText("状况: ");
            textViewWindSpeed.setText("风速: ");
            textViewHumidity.setText("湿度: ");

            textViewError.setText(data.getErrorMessage());
            textViewError.setVisibility(View.VISIBLE); // 让错误信息可见
        } else {
            // 如果数据正常，显示天气信息
            Log.d(TAG, "显示天气信息: " + data.getCityName());
            textViewCityName.setText("城市: " + data.getCityName());

            // 如果有低温数据，则显示温度范围
            if (data.getLowTemp() != null && !data.getLowTemp().isEmpty()) {
                textViewTemperature.setText("温度: " + data.getLowTemp() + " ~ " + data.getTemperature());
            } else {
                textViewTemperature.setText("温度: " + data.getTemperature());
            }

            textViewCondition.setText("状况: " + data.getCondition());
            textViewWindSpeed.setText("风速: " + data.getWindSpeed());
            textViewHumidity.setText("湿度: " + data.getHumidity());

            if (historyManager != null) {
                historyManager.createHistory(
                        data.getCityName(),
                        data.getTemperature(),
                        data.getCondition()
                );
            }

            textViewError.setVisibility(View.GONE); // 隐藏错误信息TextView
        }
    }
    // 将dp转换为像素
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
    private View createForecastDayView(ForecastData.DayForecast day) {
        // 创建卡片容器
        LinearLayout dayCardLayout = new LinearLayout(this);
        dayCardLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        dayCardLayout.setOrientation(LinearLayout.VERTICAL);
        dayCardLayout.setBackgroundColor(getResources().getColor(android.R.color.darker_gray)); // 使用与原卡片相同的背景色
        dayCardLayout.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) dayCardLayout.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, dpToPx(8));
        dayCardLayout.setLayoutParams(layoutParams);

        // 添加日期
        TextView textViewDate = new TextView(this);
        textViewDate.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        textViewDate.setText(day.getDate());
        textViewDate.setTextSize(16);
        textViewDate.setTypeface(null, android.graphics.Typeface.BOLD);
        textViewDate.setPadding(0, 0, 0, dpToPx(4));
        dayCardLayout.addView(textViewDate);

        // 添加天气状况
        TextView textViewWeather = new TextView(this);
        textViewWeather.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        textViewWeather.setText("状况: " + day.getCondition());
        textViewWeather.setTextSize(16);
        textViewWeather.setPadding(0, 0, 0, dpToPx(4));
        dayCardLayout.addView(textViewWeather);

        // 添加温度
        TextView textViewTemp = new TextView(this);
        textViewTemp.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        textViewTemp.setText("温度: " + day.getLowTemp() + " ~ " + day.getHighTemp());
        textViewTemp.setTextSize(16);
        textViewTemp.setPadding(0, 0, 0, dpToPx(4));
        dayCardLayout.addView(textViewTemp);

        // 添加风速
        TextView textViewWind = new TextView(this);
        textViewWind.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        textViewWind.setText("风速: " + day.getWindInfo());
        textViewWind.setTextSize(16);
        dayCardLayout.addView(textViewWind);

        return dayCardLayout;
    }
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

        // 显示预报标题
        textViewForecastTitle.setVisibility(View.VISIBLE);

        // 清空当前预报容器
        containerForecast.removeAllViews();

        // 获取天气预报数据
        List<ForecastData.DayForecast> forecasts = forecastData.getDailyForecasts();

        // 从第二天开始显示，因为第一天在今日天气中已经显示了
        int startIndex = 1;

        // 遍历预报数据，为每天创建一个预报卡片
        for (int i = startIndex; i < forecasts.size(); i++) {
            ForecastData.DayForecast dayForecast = forecasts.get(i);
            View forecastDayView = createForecastDayView(dayForecast);
            containerForecast.addView(forecastDayView);
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