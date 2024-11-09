package com.example.timerproject;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class SoundSettingsActivity extends AppCompatActivity {
    private ListView soundListView;
    private Button saveSoundButton;
    private DatabaseHelper databaseHelper;
    private MediaPlayer mediaPlayer;

    private String[] soundOptions = {"Default", "Beep", "Alert", "Chime"};
    private int[] soundResIds = {R.raw.notification, R.raw.notification_2, R.raw.notification_3, R.raw.notification_4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_settings);

        soundListView = findViewById(R.id.soundListView);
        saveSoundButton = findViewById(R.id.saveSoundButton);

        databaseHelper = new DatabaseHelper(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, soundOptions);
        soundListView.setAdapter(adapter);

        String savedSound = databaseHelper.getSelectedSound();
        if (savedSound != null) {
            int position = getPositionForSound(savedSound);
            soundListView.setItemChecked(position, true);
        }

        soundListView.setOnItemClickListener((parent, view, position, id) -> {
            // Stop any currently playing sound
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }

            mediaPlayer = MediaPlayer.create(SoundSettingsActivity.this, soundResIds[position]);
            mediaPlayer.start();
        });

        saveSoundButton.setOnClickListener(v -> {
            int selectedPosition = soundListView.getCheckedItemPosition();
            if (selectedPosition != -1) {
                String selectedSound = soundOptions[selectedPosition];
                databaseHelper.saveSelectedSound(selectedSound);
                Toast.makeText(SoundSettingsActivity.this, "Sound saved: " + selectedSound, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SoundSettingsActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(SoundSettingsActivity.this, "Please select a sound", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getPositionForSound(String sound) {
        for (int i = 0; i < soundOptions.length; i++) {
            if (soundOptions[i].equals(sound)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}