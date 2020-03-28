package com.example.memos.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.memos.Models.Note;
import com.example.memos.Repository.MemoRepository;

import java.util.List;

public class MemoViewModel extends AndroidViewModel {

    private MemoRepository repository;
    private LiveData<List<Note>> memos;

    public MemoViewModel(@NonNull Application application) {
        super(application);
        repository = new MemoRepository(application);
        memos = repository.getAllMemos();
    }

    public void insertMemo(Note model) {
        repository.insertNewMemo(model);
    }

    public void updateMemo(Note model) {
        repository.updateMemo(model);
    }

    public void deleteSingleMemo(Note model) {
        repository.deleteSingleMemo(model);
    }

    public void deleteAllMemos() {
        repository.deleteAllMemos();
    }

    public LiveData<List<Note>> getMemos() {
        return memos;
    }
}
