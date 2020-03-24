package com.example.memos.AsyncTasks;

import android.os.AsyncTask;

import com.example.memos.Dao.MemoInterface;
import com.example.memos.Models.Note;

public class InsertMemoAsyncTask extends AsyncTask<Note, Void, Void> {

    private MemoInterface memoInterface;

    public InsertMemoAsyncTask(MemoInterface memoInterface){
        this.memoInterface = memoInterface;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        memoInterface.insertMemo(notes);
        return null;
    }
}
