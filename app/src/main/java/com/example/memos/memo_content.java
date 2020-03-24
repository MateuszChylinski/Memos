package com.example.memos;

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

import com.example.memos.Dao.MemoRepository;
import com.example.memos.Models.Note;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class memo_content extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, View.OnTouchListener, View.OnClickListener {
    private static final String TAG = "memo_content";

    //Vars
    private Note mNote;
    private GestureDetector mDetector;
    private MemoRepository mRepository;

    /*
    1 - enable edit mode
    2 - disable edit mode
     */
    private int CURRENT_STATE = 0;
    private boolean isEdited;
    private String timestamp;

    //UI
    TextView title;
    ImageView backArrow, checkMark;
    EditText editTitle;
    LineEditText content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_content);
        mRepository = new MemoRepository(this);

        title = findViewById(R.id.memo_title);
        content = findViewById(R.id.memo_content);
        backArrow = findViewById(R.id.back_arrow_memo);
        checkMark = findViewById(R.id.save_memo);
        editTitle = findViewById(R.id.edit_title_memo);

        mDetector = new GestureDetector(this, this);
        mDetector.setOnDoubleTapListener(this);

        findActualDate();
        getMemo();
    }

    private void findActualDate() {
        Date date = Calendar.getInstance().getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE\nd MMMM");
        timestamp = simpleDateFormat.format(date);
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
        title.setText(mNote.getTitle());
        content.setText(mNote.getContent());
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
        backArrow.setVisibility(View.VISIBLE);
        title.setVisibility(View.GONE);

        checkMark.setVisibility(View.GONE);
        editTitle.setVisibility(View.VISIBLE);

        //if memo is new, create new Note object and set hints in views
        if (mNote == null) {
            mNote = new Note();
            editTitle.setHint("Title");
            content.setHint("Content");
        } else {
            editTitle.setText(mNote.getTitle());
            editTitle.requestFocus();
            content.setText(mNote.getContent());
        }
    }

    private void disableEditMode() {
        checkMark.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);

        backArrow.setVisibility(View.GONE);
        editTitle.setVisibility(View.GONE);

        title.setText(mNote.getTitle());
    }

    //prepare appropriate view to handle data manipulations (such as edit/update)
    private void setListeners() {
        title.setOnTouchListener(this);

        if (backArrow.getVisibility() == View.VISIBLE) {
            backArrow.setOnClickListener(this);
        }
        if (checkMark.getVisibility() == View.VISIBLE) {
            checkMark.setOnClickListener(this);
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
                if (editTitle.getText().toString().isEmpty() || content.getText().toString().isEmpty()) {
                    Toast.makeText(memo_content.this, "Title and content cannot be empty!", Toast.LENGTH_SHORT).show();
                    break;
                }
                mNote.setTitle(editTitle.getText().toString());
                mNote.setContent(content.getText().toString());
                mNote.setTimestamp(timestamp);
                dealWithMemo();
                break;


            case 2: //already existing memo. Check if user make any change, if he does, check if title and description is not empty
                if (title.getText().toString().isEmpty() || content.getText().toString().isEmpty()) {
                    Toast.makeText(memo_content.this, "Title and content cannot be empty!", Toast.LENGTH_SHORT).show();
                    break;
                }
                else if (title.getText().toString().equals(mNote.getTitle()) || content.getText().toString().equals(mNote.getContent())){
                    Toast.makeText(memo_content.this,"Memo wasn't edited",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(memo_content.this, MainActivity.class));
                    break;
                }
                dealWithMemo();
        }
    }

    //check if memo was new, or user wanted to update already existing
    private void dealWithMemo(){
        if (isEdited){
                mRepository.updateMemo(mNote);
                startActivity(new Intent(memo_content.this, MainActivity.class));
        }else{
            mRepository.insertNewMemo(mNote);
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
        isEdited = true;
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
    public void onShowPress(MotionEvent e) {}
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {return false;}
    @Override
    public void onLongPress(MotionEvent e) {}
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {return false;}
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }
}