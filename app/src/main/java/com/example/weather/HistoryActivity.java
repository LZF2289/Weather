// com/example/weather/HistoryActivity.java
package com.example.weather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.adapter.HistoryAdapter;
import com.example.weather.database.WeatherHistory;
import com.example.weather.database.WeatherHistoryManager;

import java.util.List;

/**
 * 历史记录Activity
 * 显示用户的历史查询记录
 */
public class HistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerViewHistory;
    private WeatherHistoryManager historyManager;
    private List<WeatherHistory> historyList;
    private HistoryAdapter adapter;
    private TextView textViewNoHistory;
    private Button buttonClearHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.initTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // 初始化数据库管理器
        historyManager = new WeatherHistoryManager(this);
        historyManager.open();

        // 初始化UI组件
        recyclerViewHistory = findViewById(R.id.recyclerViewHistory);
        textViewNoHistory = findViewById(R.id.textViewNoHistory);
        buttonClearHistory = findViewById(R.id.buttonClearHistory);

        // 设置RecyclerView
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));

        // 获取历史数据并显示
        loadHistoryData();

        // 设置清空历史按钮点击事件
        buttonClearHistory.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("清空历史记录")
                    .setMessage("确定要清空所有历史记录吗？")
                    .setPositiveButton("确定", (dialog, which) -> {
                        historyManager.clearAllHistory();
                        loadHistoryData();
                        Toast.makeText(HistoryActivity.this, "历史记录已清空", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("取消", null)
                    .show();
        });
    }

    /**
     * 加载历史数据
     */
    private void loadHistoryData() {
        historyList = historyManager.getAllHistories();

        if (historyList.isEmpty()) {
            // 如果没有历史记录，显示提示
            textViewNoHistory.setVisibility(View.VISIBLE);
            recyclerViewHistory.setVisibility(View.GONE);
        } else {
            // 显示历史记录
            textViewNoHistory.setVisibility(View.GONE);
            recyclerViewHistory.setVisibility(View.VISIBLE);

            // 如果适配器不存在则创建
            if (adapter == null) {
                adapter = new HistoryAdapter(this, historyList);
                adapter.setOnItemClickListener(history -> {
                    // 点击历史记录项后，返回主页并自动查询该城市
                    Intent intent = new Intent();
                    intent.putExtra("cityName", history.getCityName());
                    setResult(RESULT_OK, intent);
                    finish();
                });
                recyclerViewHistory.setAdapter(adapter);
            } else {
                // 更新适配器数据
                adapter.updateData(historyList);
            }
        }
    }

    @Override
    protected void onResume() {
        historyManager.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        historyManager.close();
        super.onPause();
    }
}