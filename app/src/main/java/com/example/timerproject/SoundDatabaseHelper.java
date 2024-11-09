package com.example.timerproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SoundDatabaseHelper extends SQLiteOpenHelper {

    public SoundDatabaseHelper(Context context) {
        super(context, "appDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE sound_settings (id INTEGER PRIMARY KEY AUTOINCREMENT, sound TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS sound_settings");
        onCreate(db);
    }
}