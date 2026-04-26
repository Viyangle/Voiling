package com.voiling.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UserDictEntry.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDictDao userDictDao();

    public static AppDatabase build(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "voiling.db").build();
    }
}
