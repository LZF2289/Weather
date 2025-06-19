package com.example.weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private EditText editTextDefaultCity;
    private Button buttonSaveSettings;

    // 定义SharedPreferences的名称和键名
    public static final String PREFS_NAME = "WeatherAppPrefs";
    public static final String KEY_DEFAULT_CITY = "default_city";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // 启用返回按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("设置");
        }

        // 初始化UI组件
        editTextDefaultCity = findViewById(R.id.editTextDefaultCity);
        buttonSaveSettings = findViewById(R.id.buttonSaveSettings);

        // 加载当前设置
        loadSettings();

        // 设置保存按钮的点击监听器
        buttonSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
                Toast.makeText(SettingsActivity.this, "设置已保存", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    // 加载当前设置
    private void loadSettings() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // 加载默认城市
        String defaultCity = settings.getString(KEY_DEFAULT_CITY, "");
        editTextDefaultCity.setText(defaultCity);
    }

    // 保存设置
    private void saveSettings() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // 保存默认城市
        String defaultCity = editTextDefaultCity.getText().toString().trim();
        editor.putString(KEY_DEFAULT_CITY, defaultCity);

        // 应用更改
        editor.apply();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}