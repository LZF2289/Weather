package com.example.weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {
    private EditText editTextDefaultCity;
    private RadioGroup radioGroupTempUnit;
    private RadioButton radioButtonCelsius;
    private RadioButton radioButtonFahrenheit;
    private RadioGroup radioGroupTheme;
    private RadioButton radioButtonLightTheme;
    private RadioButton radioButtonDarkTheme;
    private RadioButton radioButtonSystemTheme;
    private Button buttonSaveSettings;

    // 定义SharedPreferences的名称和键名
    public static final String PREFS_NAME = "WeatherAppPrefs";
    public static final String KEY_DEFAULT_CITY = "default_city";
    public static final String KEY_USE_FAHRENHEIT = "use_fahrenheit";
    public static final String KEY_THEME_MODE = "theme_mode";

    // 主题模式常量
    public static final int THEME_LIGHT = 1;
    public static final int THEME_DARK = 2;
    public static final int THEME_SYSTEM = 0;

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
        radioGroupTempUnit = findViewById(R.id.radioGroupTempUnit);
        radioButtonCelsius = findViewById(R.id.radioButtonCelsius);
        radioButtonFahrenheit = findViewById(R.id.radioButtonFahrenheit);
        radioGroupTheme = findViewById(R.id.radioGroupTheme);
        radioButtonLightTheme = findViewById(R.id.radioButtonLightTheme);
        radioButtonDarkTheme = findViewById(R.id.radioButtonDarkTheme);
        radioButtonSystemTheme = findViewById(R.id.radioButtonSystemTheme);
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

        // 加载温度单位设置
        boolean useFahrenheit = settings.getBoolean(KEY_USE_FAHRENHEIT, false);
        if (useFahrenheit) {
            radioButtonFahrenheit.setChecked(true);
        } else {
            radioButtonCelsius.setChecked(true);
        }

        // 加载主题设置
        int themeMode = settings.getInt(KEY_THEME_MODE, THEME_SYSTEM);
        switch (themeMode) {
            case THEME_LIGHT:
                radioButtonLightTheme.setChecked(true);
                break;
            case THEME_DARK:
                radioButtonDarkTheme.setChecked(true);
                break;
            default:
                radioButtonSystemTheme.setChecked(true);
                break;
        }
    }

    // 保存设置
    private void saveSettings() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // 保存默认城市
        String defaultCity = editTextDefaultCity.getText().toString().trim();
        editor.putString(KEY_DEFAULT_CITY, defaultCity);

        // 保存温度单位设置
        boolean useFahrenheit = radioButtonFahrenheit.isChecked();
        editor.putBoolean(KEY_USE_FAHRENHEIT, useFahrenheit);

        // 保存主题设置
        int themeMode;
        if (radioButtonLightTheme.isChecked()) {
            themeMode = THEME_LIGHT;
        } else if (radioButtonDarkTheme.isChecked()) {
            themeMode = THEME_DARK;
        } else {
            themeMode = THEME_SYSTEM;
        }

        // 获取之前的主题设置
        int oldThemeMode = settings.getInt(KEY_THEME_MODE, THEME_SYSTEM);

        // 保存新的主题设置
        editor.putInt(KEY_THEME_MODE, themeMode);

        // 应用更改
        editor.apply();

        // 如果主题有变化，立即应用新主题
        if (oldThemeMode != themeMode) {
            applyTheme(themeMode);
        }
    }

    // 应用主题
    private void applyTheme(int themeMode) {
        switch (themeMode) {
            case THEME_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case THEME_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case THEME_SYSTEM:
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
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