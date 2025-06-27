// com/example/weather/adapter/HistoryAdapter.java
package com.example.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.database.WeatherHistory;

import java.util.List;

//历史记录适配器，用于绑定历史记录数据到RecyclerView

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<WeatherHistory> historyList;
    private Context context;
    private OnItemClickListener listener;

    // 定义点击事件接口
    public interface OnItemClickListener {
        void onItemClick(WeatherHistory history);
    }

    public HistoryAdapter(Context context, List<WeatherHistory> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    // 设置点击事件监听器
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // 更新数据
    public void updateData(List<WeatherHistory> newHistoryList) {
        this.historyList = newHistoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 绑定数据
        WeatherHistory history = historyList.get(position);
        holder.textViewHistoryCityName.setText(history.getCityName());
        holder.textViewHistoryTemp.setText("温度: " + history.getTemperature());
        holder.textViewHistoryCondition.setText("状况: " + history.getCondition());
        holder.textViewHistoryTime.setText(history.getQueryTime());

        // 设置点击事件
        if (listener != null) {
            holder.itemView.setOnClickListener(v -> listener.onItemClick(history));
        }
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }


//      ViewHolder类，用于保存视图引用

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewHistoryCityName;
        public TextView textViewHistoryTemp;
        public TextView textViewHistoryCondition;
        public TextView textViewHistoryTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHistoryCityName = itemView.findViewById(R.id.textViewHistoryCityName);
            textViewHistoryTemp = itemView.findViewById(R.id.textViewHistoryTemp);
            textViewHistoryCondition = itemView.findViewById(R.id.textViewHistoryCondition);
            textViewHistoryTime = itemView.findViewById(R.id.textViewHistoryTime);
        }
    }
}