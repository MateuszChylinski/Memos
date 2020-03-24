package com.example.memos.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.memos.Models.Note;

import java.util.List;

@Dao
public interface MemoInterface {

    @Insert
    void insertMemo(Note... note);
    @Update
    void updateMemo(Note... note);
    @Delete
    void deleteMemo(Note... note);

    @Query("DELETE FROM note" )
    void deleteAllMemos();
    @Query("SELECT * FROM note")
    LiveData<List<Note>> getAllMemos();
}
