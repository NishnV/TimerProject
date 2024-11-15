package com.example.timerproject; // Use your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView timerDisplay;
    private EditText inputHours, inputMinutes, inputSeconds;
    private Button startButton, pauseButton, resetButton, soundSettingsButton, viewHistoryButton;
    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private long timeInMillis;
    private long timeLeftInMillis; // To store remaining time when paused
    private MediaPlayer mediaPlayer;
    private DatabaseHelper databaseHelper;

    // Sound options and their resource IDs
    private String[] soundOptions = {"Default", "Beep", "Alert", "Chime"};
    private int[] soundResIds = {R.raw.notification, R.raw.notification_2, R.raw.notification_3, R.raw.notification_4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        timerDisplay = findViewById(R.id.timerDisplay);
        inputHours = findViewById(R.id.inputHours);
        inputMinutes = findViewById(R.id.inputMinutes);
        inputSeconds = findViewById(R.id.inputSeconds);
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        resetButton = findViewById(R.id.resetButton);
        soundSettingsButton = findViewById(R.id.soundSettingsButton);
        viewHistoryButton = findViewById(R.id.viewHistoryButton);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Initialize MediaPlayer with a default sound file
        mediaPlayer = MediaPlayer.create(this, R.raw.notification);

        // Start Button Listener
        startButton.setOnClickListener(v -> startTimer());

        // Pause Button Listener
        pauseButton.setOnClickListener(v -> pauseTimer());

        // Reset Button Listener
        resetButton.setOnClickListener(v -> resetTimer());

        // Sound Settings Button Listener
        soundSettingsButton.setOnClickListener(v -> {
            // Launch Sound Settings Activity
            Intent intent = new Intent(MainActivity.this, SoundSettingsActivity.class);
            startActivity(intent);
        });

        // View History Button Listener
        viewHistoryButton.setOnClickListener(v -> {
            // Launch TimerHistoryActivity to view timer history
            Intent intent = new Intent(MainActivity.this, TimerHistoryActivity.class);
            startActivity(intent);
        });
    }

    private void startTimer() {
        if (isRunning) return; // If timer is already running, do nothing

        int hours = parseInput(inputHours.getText().toString());
        int minutes = parseInput(inputMinutes.getText().toString());
        int seconds = parseInput(inputSeconds.getText().toString());

        if (timeLeftInMillis == 0) {
            timeInMillis = (hours * 3600 + minutes * 60 + seconds) * 1000;
            if (timeInMillis == 0) {
                Toast.makeText(this, "Please enter a valid time", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            timeInMillis = timeLeftInMillis; // Continue from where we left off
        }

        countDownTimer = new CountDownTimer(timeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeInMillis = millisUntilFinished;
                timeLeftInMillis = millisUntilFinished; // Store remaining time
                updateTimerDisplay();
            }

            @Override
            public void onFinish() {
                isRunning = false;
                startButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Time's Up!", Toast.LENGTH_SHORT).show();

                // Play the selected notification sound
                playNotificationSound();

                // Save the timer to history
                saveTimerToHistory();
            }
        }.start();

        isRunning = true;
        startButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);
    }

    private void pauseTimer() {
        if (isRunning) {
            countDownTimer.cancel();
            isRunning = false;
            startButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.GONE);
        }
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.prepareAsync(); // Prepare it for next play
        }
        isRunning = false;
        timeInMillis = 0;
        timeLeftInMillis = 0; // Reset the remaining time
        updateTimerDisplay();
        startButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
    }

    private void updateTimerDisplay() {
        int hours = (int) (timeInMillis / 1000) / 3600;
        int minutes = (int) ((timeInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeInMillis / 1000) % 60;
        String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerDisplay.setText(timeFormatted);
    }

    private int parseInput(String input) {
        if (input.isEmpty()) return 0;
        return Integer.parseInt(input);
    }

    // Method to play the selected notification sound
    private void playNotificationSound() {
        String selectedSound = databaseHelper.getSelectedSound();
        int soundResId = R.raw.notification; // Default sound

        // Find the correct sound based on the selection
        for (int i = 0; i < soundOptions.length; i++) {
            if (soundOptions[i].equals(selectedSound)) {
                soundResId = soundResIds[i];
                break;
            }
        }

        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        // Play the selected sound
        mediaPlayer = MediaPlayer.create(this, soundResId);
        mediaPlayer.start();
    }

    // Save the timer to the history in the database
    private void saveTimerToHistory() {
        long endTimeMillis = System.currentTimeMillis();

        // Save the timer duration (formatted as HH:MM:SS) and end time (timestamp in milliseconds)
        String duration = String.format("%02d:%02d:%02d",
                (int)(timeInMillis / 3600000),
                (int)((timeInMillis % 3600000) / 60000),
                (int)((timeInMillis % 60000) / 1000));

        // Insert the timer record into the database with additional info (e.g., timer type or any other info)
        databaseHelper.addTimerHistory(duration, String.valueOf(endTimeMillis), "Sample Additional Info");

        Toast.makeText(MainActivity.this, "Timer saved to history", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}