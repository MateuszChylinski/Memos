package com.example.memos.AsyncTasks;

import android.os.AsyncTask;

import com.example.memos.Dao.MemoDatabase;
import com.example.memos.Dao.MemoInterface;
import com.example.memos.Models.Note;

public class UpdateMemoAsyncTask extends AsyncTask<Note, Void, Void> {

   private MemoInterface mInterface;

    public UpdateMemoAsyncTask (MemoInterface memoInterface){
        this.mInterface = memoInterface;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mInterface.updateMemo(notes);
        return null;
    }


}
