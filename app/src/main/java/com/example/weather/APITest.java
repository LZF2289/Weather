package com.example.weather;

import android.util.Log;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class APITest {
    private static final String TAG = "APITest";

    public static void testAPI() {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.seniverse.com/v3/weather/now.json?key=Sd0eZ-fESIrjGeEj7&location=beijing&language=zh-Hans&unit=c")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String result = response.body().string();
                Log.d(TAG, "API测试结果: " + result);
            } catch (IOException e) {
                Log.e(TAG, "API测试失败: " + e.getMessage(), e);
            }
        }).start();
    }
}