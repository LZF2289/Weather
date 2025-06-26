package com.example.weather;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 2000; // 延迟时间，单位毫秒 (例如2000毫秒 = 2秒)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.initTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // 设置布局文件为 activity_splash.xml

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // 创建一个意图 (Intent) 以启动 MainActivity
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                // 关闭 SplashActivity，这样用户按返回键时不会回到启动界面
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}