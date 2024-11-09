package com.example.timerproject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "timer_history.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_TIMER_HISTORY = "timer_history";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DURATION = "duration";
    private static final String COLUMN_END_TIME = "end_time";
    private static final String COLUMN_SELECTED_SOUND = "selected_sound";  // New column for sound

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table with an additional column for selected sound
        String createTableQuery = "CREATE TABLE " + TABLE_TIMER_HISTORY + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DURATION + " TEXT, " +
                COLUMN_END_TIME + " TEXT, " +
                COLUMN_SELECTED_SOUND + " TEXT);";  // Add column for selected sound
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the table if it exists and create a new one
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMER_HISTORY);
        onCreate(db);
    }

    // Method to insert a new timer history with sound info
    public void addTimerHistory(String duration, String endTime, String selectedSound) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DURATION, duration);
        values.put(COLUMN_END_TIME, endTime);
        values.put(COLUMN_SELECTED_SOUND, selectedSound);  // Store the selected sound
        db.insert(TABLE_TIMER_HISTORY, null, values);
        db.close();
    }

    // Method to get all timer history records
    public List<TimerHistory> getAllTimerHistory() {
        List<TimerHistory> historyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TIMER_HISTORY, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(COLUMN_DURATION));
                @SuppressLint("Range") String endTime = cursor.getString(cursor.getColumnIndex(COLUMN_END_TIME));
                @SuppressLint("Range") String selectedSound = cursor.getString(cursor.getColumnIndex(COLUMN_SELECTED_SOUND)); // Retrieve selected sound
                historyList.add(new TimerHistory(duration, endTime, selectedSound));  // Pass selectedSound to TimerHistory
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return historyList;
    }

    // Method to get the selected sound (assuming one per app)
    @SuppressLint("Range")
    public String getSelectedSound() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_SELECTED_SOUND + " FROM " + TABLE_TIMER_HISTORY + " ORDER BY " + COLUMN_ID + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);
        String selectedSound = "Default";  // Default sound
        if (cursor.moveToFirst()) {
            selectedSound = cursor.getString(cursor.getColumnIndex(COLUMN_SELECTED_SOUND));
        }
        cursor.close();
        db.close();
        return selectedSound;
    }

    // Method to save the selected sound (you can update this to use any other logic)
    public void saveSelectedSound(String sound) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SELECTED_SOUND, sound);  // Save the selected sound

        // Update the most recent record with the selected sound
        db.update(TABLE_TIMER_HISTORY, values, null, null);
        db.close();
    }
}