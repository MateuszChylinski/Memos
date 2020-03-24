package com.example.memos.Dao;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.memos.AsyncTasks.DeleteAsyncTask;
import com.example.memos.AsyncTasks.DeleteSingleMemoAsyncTask;
import com.example.memos.AsyncTasks.InsertMemoAsyncTask;
import com.example.memos.AsyncTasks.UpdateMemoAsyncTask;
import com.example.memos.Models.Note;

import java.util.List;

public class MemoRepository  {

    private MemoDatabase mDatabase;

    public MemoRepository (Context context){
        this.mDatabase = MemoDatabase.getDatabase(context);
    }

    public void insertNewMemo(Note note){
        new InsertMemoAsyncTask(mDatabase.memoInterface()).execute(note);
    }

    public LiveData<List<Note>> getAllMemos(){
       return mDatabase.memoInterface().getAllMemos();
    }

    public void deleteAllMemos(){
        new DeleteAsyncTask(mDatabase.memoInterface()).execute();
    }

    public void updateMemo(Note note){
        new UpdateMemoAsyncTask(mDatabase.memoInterface()).execute(note);
    }

    public void deleteMemo(Note note){
        new DeleteSingleMemoAsyncTask(mDatabase.memoInterface()).execute(note);
    }
}



