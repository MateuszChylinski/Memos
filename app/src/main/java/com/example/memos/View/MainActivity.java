package com.example.memos.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.memos.Models.Note;
import com.example.memos.R;
import com.example.memos.Adapter.MemoAdapter;
import com.example.memos.ViewModel.MemoViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MemoAdapter.OnNoteListener {

    private static final String TAG = "MainActivity";

    //Vars
    private ArrayList<Note> mNotes = new ArrayList<>();
    private MemoViewModel mViewModel;
    private MemoAdapter mMemoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewModel = new MemoViewModel(getApplication());

        Toolbar toolbar = (Toolbar) findViewById(R.id.memoToolbar);
        setSupportActionBar(toolbar);
        setTitle("My memos");

        initRecyclerView();
    }

    //This method will be called after getting result from memo_content such as new memo, or edited existing memo.
    @Override
    protected void onResume() {
        super.onResume();
        getMemos();
    }

    private void getMemos() {
        mViewModel.getMemos().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                if (mNotes.size() > 0) {
                    mNotes.clear();
                }
                if (notes != null) {
                    mNotes.addAll(notes);
                    mMemoAdapter.watchMemoChanges((ArrayList<Note>) notes);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.icon_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addNewNote:
                startActivity(new Intent(this, memo_content.class));
                break;

            case R.id.deleteAllNotes:
                mViewModel.deleteAllMemos();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {
        //UI
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mMemoAdapter = new MemoAdapter(mNotes, this);
        new ItemTouchHelper(itemTouch).attachToRecyclerView(mRecyclerView);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mMemoAdapter);
    }

    //sending data about specific memo to the memo_content (which is class responsible for view/edit memo)
    @Override
    public void onMemoClick(int position) {
        Intent intent = new Intent(this, memo_content.class);
        intent.putExtra("memo_content", mNotes.get(position));
        startActivity(intent);
    }

    private ItemTouchHelper.SimpleCallback itemTouch = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        //delete swiped memo
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            mViewModel.deleteSingleMemo(mNotes.get(viewHolder.getAdapterPosition()));
        }
    };
}
