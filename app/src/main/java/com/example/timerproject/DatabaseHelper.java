package com.example.timerproject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "timerApp.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "sound_settings";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SELECTED_SOUND = "selected_sound";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_SELECTED_SOUND + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to save selected sound to the database
    public void saveSelectedSound(String sound) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SELECTED_SOUND, sound);

        // If no sound exists, insert a new row; otherwise, update the existing row
        if (getSelectedSound() == null) {
            db.insert(TABLE_NAME, null, values);
        } else {
            db.update(TABLE_NAME, values, null, null);
        }
        db.close();
    }

    // Method to retrieve the saved selected sound from the database
    public String getSelectedSound() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_SELECTED_SOUND}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String sound = cursor.getString(cursor.getColumnIndex(COLUMN_SELECTED_SOUND));
            cursor.close();
            return sound;
        }
        return null; // No sound saved
    }
}