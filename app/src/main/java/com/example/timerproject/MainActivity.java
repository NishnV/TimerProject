package com.example.timerproject; // Use your actual package name
import android.view.View;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView timerDisplay;
    private EditText inputHours, inputMinutes, inputSeconds;
    private Button startButton, pauseButton, resetButton;
    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private boolean isPaused = false; // New flag to track pause state
    private long timeInMillis;
    private long timeLeftInMillis; // Variable to store the remaining time

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

        // Start Button Listener
        startButton.setOnClickListener(v -> startTimer());

        // Pause Button Listener (toggle between pause and resume)
        pauseButton.setOnClickListener(v -> {
            if (isPaused) {
                resumeTimer();
            } else {
                pauseTimer();
            }
        });

        // Reset Button Listener
        resetButton.setOnClickListener(v -> resetTimer());
    }

    private void startTimer() {
        if (isRunning) return; // If timer is already running, do nothing

        int hours = parseInput(inputHours.getText().toString());
        int minutes = parseInput(inputMinutes.getText().toString());
        int seconds = parseInput(inputSeconds.getText().toString());

        timeInMillis = (hours * 3600 + minutes * 60 + seconds) * 1000;
        timeLeftInMillis = timeInMillis; // Initialize remaining time

        if (timeInMillis == 0) {
            Toast.makeText(this, "Please enter a valid time", Toast.LENGTH_SHORT).show();
            return;
        }

        // Start the countdown timer
        startCountDown();

        isRunning = true;
        startButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);
    }

    // Method to start the countdown timer
    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerDisplay();
            }

            @Override
            public void onFinish() {
                isRunning = false;
                isPaused = false;
                startButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Time's Up!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    private void pauseTimer() {
        if (isRunning) {
            countDownTimer.cancel();
            isRunning = false;
            isPaused = true;
            pauseButton.setText("Resume");
        }
    }

    private void resumeTimer() {
        if (!isRunning && isPaused) {
            startCountDown(); // Resume the countdown
            isRunning = true;
            isPaused = false;
            pauseButton.setText("Pause");
        }
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isRunning = false;
        isPaused = false;
        timeLeftInMillis = 0;
        timeInMillis = 0;
        updateTimerDisplay();
        startButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
        pauseButton.setText("Pause"); // Reset pause button text
    }

    private void updateTimerDisplay() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerDisplay.setText(timeFormatted);
    }

    private int parseInput(String input) {
        if (input.isEmpty()) return 0;
        return Integer.parseInt(input);
    }
}