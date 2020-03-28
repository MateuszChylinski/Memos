package com.example.memos.AsyncTasks;

import android.os.AsyncTask;

import com.example.memos.Dao.MemoInterface;
import com.example.memos.Models.Note;

public class DeleteAllMemosAsyncTask extends AsyncTask<Note, Void, Void> {

    private MemoInterface mInterface;

    public DeleteAllMemosAsyncTask(MemoInterface memoInterface) {
        this.mInterface = memoInterface;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mInterface.deleteAllMemos();
        return null;
    }
}
