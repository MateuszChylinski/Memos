package com.example.memos.AsyncTasks;

import android.os.AsyncTask;

import com.example.memos.Dao.MemoInterface;
import com.example.memos.Models.Note;

public class DeleteSingleMemoAsyncTask extends AsyncTask<Note, Void, Void> {

    private MemoInterface mInterface;

    public DeleteSingleMemoAsyncTask(MemoInterface mInterface) {
        this.mInterface = mInterface;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mInterface.deleteMemo(notes);
        return null;
    }
}
