package com.example.weather;

import android.content.Intent;
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
    private LinearLayout layoutForecastDay1; // 第一天预报布局
    private LinearLayout layoutForecastDay2; // 第二天预报布局
    private TextView textViewForecastDay1Date;  // 第一天日期
    private TextView textViewForecastDay1Weather; // 第一天天气
    private TextView textViewForecastDay1Temp;  // 第一天温度
    private TextView textViewForecastDay1Wind;  // 第一天风速
    private TextView textViewForecastDay2Date;  // 第二天日期
    private TextView textViewForecastDay2Weather; // 第二天天气
    private TextView textViewForecastDay2Temp;  // 第二天温度
    private TextView textViewForecastDay2Wind;  // 第二天风速
    private WeatherController controller;   // Controller的引用
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
        layoutForecastDay1 = findViewById(R.id.layoutForecastDay1);
        layoutForecastDay2 = findViewById(R.id.layoutForecastDay2);
        textViewForecastDay1Date = findViewById(R.id.textViewForecastDay1Date);
        textViewForecastDay1Weather = findViewById(R.id.textViewForecastDay1Weather);
        textViewForecastDay1Temp = findViewById(R.id.textViewForecastDay1Temp);
        textViewForecastDay1Wind = findViewById(R.id.textViewForecastDay1Wind);
        textViewForecastDay2Date = findViewById(R.id.textViewForecastDay2Date);
        textViewForecastDay2Weather = findViewById(R.id.textViewForecastDay2Weather);
        textViewForecastDay2Temp = findViewById(R.id.textViewForecastDay2Temp);
        textViewForecastDay2Wind = findViewById(R.id.textViewForecastDay2Wind);

        // 初始化Controller
        controller = new WeatherController(this);

        //初始化历史记录
        buttonHistory = findViewById(R.id.buttonHistory);
        //设置历史记录按钮监听器
        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivityForResult(intent, REQUEST_HISTORY);
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

                // 隐藏预报视图
                textViewForecastTitle.setVisibility(View.GONE);
                layoutForecastDay1.setVisibility(View.GONE);
                layoutForecastDay2.setVisibility(View.GONE);

                // 当按钮被点击时，调用Controller的方法获取天气数据
                controller.fetchWeatherData(cityName);
            }
        });
        APITest.testAPI();
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

    // 更新多天天气预报信息
    public void updateForecastInfo(ForecastData forecastData) {
        if (forecastData.isError() || forecastData.getDailyForecasts().size() < 2) {
            // 如果获取预报失败或数据不足，不显示预报卡片
            return;
        }

        // 显示预报标题
        textViewForecastTitle.setVisibility(View.VISIBLE);

        // 显示未来两天的预报（跳过今天的预报，因为今天的已经在今日天气中显示了）
        if (forecastData.getDailyForecasts().size() > 1) {
            ForecastData.DayForecast day1 = forecastData.getDailyForecasts().get(1); // 明天
            textViewForecastDay1Date.setText(day1.getDate());
            textViewForecastDay1Weather.setText("状况: " + day1.getCondition());
            textViewForecastDay1Temp.setText("温度: " + day1.getLowTemp() + " ~ " + day1.getHighTemp());
            textViewForecastDay1Wind.setText("风速: " + day1.getWindInfo());
            layoutForecastDay1.setVisibility(View.VISIBLE);
        }

        if (forecastData.getDailyForecasts().size() > 2) {
            // 如果有第3天数据
            ForecastData.DayForecast day2 = forecastData.getDailyForecasts().get(2); // 后天
            textViewForecastDay2Date.setText(day2.getDate());
            textViewForecastDay2Weather.setText("状况: " + day2.getCondition());
            textViewForecastDay2Temp.setText("温度: " + day2.getLowTemp() + " ~ " + day2.getHighTemp());
            textViewForecastDay2Wind.setText("风速: " + day2.getWindInfo());
            layoutForecastDay2.setVisibility(View.VISIBLE);
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