package com.example.timerproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class TimerHistoryAdapter extends BaseAdapter {

    private Context context;
    private List<TimerHistory> timerHistoryList;

    public TimerHistoryAdapter(Context context, List<TimerHistory> timerHistoryList) {
        this.context = context;
        this.timerHistoryList = timerHistoryList;
    }

    @Override
    public int getCount() {
        return timerHistoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return timerHistoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the layout for each item
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_timer_history, parent, false);
        }

        // Get the current item (timer record)
        TimerHistory currentHistory = timerHistoryList.get(position);

        // Set the data to the views
        TextView durationTextView = convertView.findViewById(R.id.timerHistoryDuration);
        TextView endTimeTextView = convertView.findViewById(R.id.timerHistoryEndTime);
        TextView soundTextView = convertView.findViewById(R.id.timerHistorySound);

        durationTextView.setText(currentHistory.getDuration());
        endTimeTextView.setText(currentHistory.getEndTime());
        soundTextView.setText(currentHistory.getSelectedSound());

        return convertView;
    }
}