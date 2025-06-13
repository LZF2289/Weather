package com.example.weather;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

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

    private WeatherController controller;   // Controller的引用

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

        // 初始化Controller
        controller = new WeatherController(this);

        // 设置按钮点击监听器
        buttonQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = editTextCityName.getText().toString();
                Log.d(TAG, "查询按钮点击: " + cityName);
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

    // Controller调用此方法来更新View（UI界面）
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
            textViewTemperature.setText("温度: " + data.getTemperature());
            textViewCondition.setText("状况: " + data.getCondition());
            textViewWindSpeed.setText("风速: " + data.getWindSpeed());
            textViewHumidity.setText("湿度: " + data.getHumidity());

            textViewError.setVisibility(View.GONE); // 隐藏错误信息TextView
        }
    }
}