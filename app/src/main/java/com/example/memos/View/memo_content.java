package com.example.memos.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memos.LineEditText;
import com.example.memos.Models.Note;
import com.example.memos.R;
import com.example.memos.ViewModel.MemoViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class memo_content extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, View.OnTouchListener, View.OnClickListener {
    private static final String TAG = "memo_content";

    //Vars
    private Note mNote;
    private GestureDetector mDetector;
    private MemoViewModel mViewModel;

    /*
    1 - enable edit mode
    2 - disable edit mode
     */
    private int CURRENT_STATE = 0;
    private boolean mIsEdited;
    private String mTimestamp;

    //UI
    TextView mTitle;
    ImageView mBackArrow, mCheckMark;
    EditText mEditTitle;
    LineEditText mContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_content);
        mViewModel = new MemoViewModel(getApplication());

        mTitle = findViewById(R.id.memo_title);
        mContent = findViewById(R.id.memo_content);
        mBackArrow = findViewById(R.id.back_arrow_memo);
        mCheckMark = findViewById(R.id.save_memo);
        mEditTitle = findViewById(R.id.edit_title_memo);

        mDetector = new GestureDetector(this, this);
        mDetector.setOnDoubleTapListener(this);

        findActualDate();
        getMemo();
    }

    private void findActualDate() {
        Date date = Calendar.getInstance().getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE\nd MMMM");
        mTimestamp = simpleDateFormat.format(date);
    }

    private void getMemo() {
        /*
        check if we are creating new memo, or editing existing
        if it's new memo, enter into create mode. if it's not (already existing) enter edit mode
        */

        //Memo exists

        if (getIntent().hasExtra("memo_content")) {
            mNote = getIntent().getParcelableExtra("memo_content");
            CURRENT_STATE = 2;
            setMemo();
        } else {
            CURRENT_STATE = 1;
        }
        prepareAppropriateMode();
    }

    //set view with data of the existing memo
    private void setMemo() {
        mTitle.setText(mNote.getTitle());
        mContent.setText(mNote.getContent());
    }

    private void prepareAppropriateMode() {

        switch (CURRENT_STATE) {
            case 1:
            case 3:
                enableEditMode();
                break;
            case 2:
                disableEditMode();
                break;
        }
        setListeners();
    }

    private void enableEditMode() {
        mBackArrow.setVisibility(View.VISIBLE);
        mTitle.setVisibility(View.GONE);

        mCheckMark.setVisibility(View.GONE);
        mEditTitle.setVisibility(View.VISIBLE);

        //if memo is new, create new Note object and set hints in views
        if (mNote == null) {
            mNote = new Note();
            mEditTitle.setHint("Title");
            mContent.setHint("Content");
        } else {
            mEditTitle.setText(mNote.getTitle());
            mEditTitle.requestFocus();
            mContent.setText(mNote.getContent());
        }
        mContent.setFocusableInTouchMode(true);
        mContent.setEnabled(true);
        mContent.setClickable(true);
    }

    private void disableEditMode() {
        mCheckMark.setVisibility(View.VISIBLE);
        mTitle.setVisibility(View.VISIBLE);

        mBackArrow.setVisibility(View.GONE);
        mEditTitle.setVisibility(View.GONE);

        mTitle.setText(mNote.getTitle());

        mContent.setFocusable(false);
    }

    //prepare appropriate view to handle data manipulations (such as edit/update)
    private void setListeners() {
        mTitle.setOnTouchListener(this);

        if (mBackArrow.getVisibility() == View.VISIBLE) {
            mBackArrow.setOnClickListener(this);
        }
        if (mCheckMark.getVisibility() == View.VISIBLE) {
            mCheckMark.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_memo:
            case R.id.back_arrow_memo:
                checkDataCorrectness();
                break;
        }
    }

    private void checkDataCorrectness() {
        switch (CURRENT_STATE) {
            case 1: // memo was in edit mode
            case 3: // memo was in update mode

                //make sure that memo has title and description/content
                if (mEditTitle.getText().toString().isEmpty() || mContent.getText().toString().isEmpty()) {
                    Toast.makeText(memo_content.this, "Title and content cannot be empty!", Toast.LENGTH_SHORT).show();
                    break;
                }
                mNote.setTitle(mEditTitle.getText().toString());
                mNote.setContent(mContent.getText().toString());
                mNote.setTimestamp(mTimestamp);
                dealWithMemo();
                break;

            case 2: //already existing memo. Check if user make any change, if he does, check if title and description is not empty
                if (mTitle.getText().toString().isEmpty() || mContent.getText().toString().isEmpty()) {
                    Toast.makeText(memo_content.this, "Title and content cannot be empty!", Toast.LENGTH_SHORT).show();
                    break;
                } else if (mTitle.getText().toString().equals(mNote.getTitle()) || mContent.getText().toString().equals(mNote.getContent())) {
                    Toast.makeText(memo_content.this, "Memo wasn't edited", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(memo_content.this, MainActivity.class));
                    break;
                }
                dealWithMemo();
        }
    }

    //check if memo was new, or user wanted to update already existing
    private void dealWithMemo() {
        if (mIsEdited) {
            mViewModel.updateMemo(mNote);
            startActivity(new Intent(memo_content.this, MainActivity.class));
        } else {
            mViewModel.insertMemo(mNote);
            startActivity(new Intent(memo_content.this, MainActivity.class));
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    //begin to update mode
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        CURRENT_STATE = 3;
        mIsEdited = true;
        prepareAppropriateMode();
        return true;
    }

    // don't need this stuff for now. It's just needed to keep everything works.
    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }
}