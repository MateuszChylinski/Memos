package com.example.memos.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.memos.AsyncTasks.DeleteAllMemosAsyncTask;
import com.example.memos.AsyncTasks.DeleteSingleMemoAsyncTask;
import com.example.memos.AsyncTasks.InsertMemoAsyncTask;
import com.example.memos.AsyncTasks.UpdateMemoAsyncTask;
import com.example.memos.Dao.MemoDatabase;
import com.example.memos.Dao.MemoInterface;
import com.example.memos.Models.Note;

import java.util.List;

public class MemoRepository {

    private MemoInterface memoInterface;
    private LiveData<List<Note>> memoLiveData;

    public MemoRepository(Application application) {
        MemoDatabase database = MemoDatabase.getDatabase(application);
        memoInterface = database.memoInterface();
        memoLiveData = memoInterface.getAllMemos();
    }

    public void insertNewMemo(Note... notes) {
        new InsertMemoAsyncTask(memoInterface).execute(notes);
    }

    public void updateMemo(Note... notes) {
        new UpdateMemoAsyncTask(memoInterface).execute(notes);
    }

    public void deleteSingleMemo(Note... notes) {
        new DeleteSingleMemoAsyncTask(memoInterface).execute(notes);
    }

    public void deleteAllMemos() {
        new DeleteAllMemosAsyncTask(memoInterface).execute();
    }

    public LiveData<List<Note>> getAllMemos() {
        return memoLiveData;
    }


}
