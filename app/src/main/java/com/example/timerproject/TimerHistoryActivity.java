package com.example.timerproject;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class TimerHistoryActivity extends AppCompatActivity {

    // Default constructor (implicitly provided by the system)
    public TimerHistoryActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_history);
    }
}