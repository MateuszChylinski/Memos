package com.example.memos.Dao;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.memos.Models.Note;

@Database(entities = {Note.class}, version = 2)
public abstract class MemoDatabase extends RoomDatabase {

    private static final String DB_NAME = "memo.db";
    private static MemoDatabase database;

    public static synchronized MemoDatabase getDatabase(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(
                    context.getApplicationContext(),
                    MemoDatabase.class,
                    DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract MemoInterface memoInterface();
}
