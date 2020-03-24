package com.example.memos;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;


public class LineEditText extends AppCompatEditText  {

    Rect mRect;
    Paint mPaint;

    public LineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(0xFFFFD966);


    }

    @Override
    protected void onDraw(Canvas canvas) {

        int screenHeight = ((View)this.getParent()).getHeight();
        int lineHeight = getLineHeight();
        int numberOfLines = screenHeight / lineHeight;

        Rect rect = new Rect();
        Paint paint = new Paint();

        int baseline = getLineBounds(0, rect);

        for (int i = 0; i < numberOfLines; i++) {
            canvas.drawLine(rect.left, baseline+1, rect.right, baseline+1, paint);
            baseline += lineHeight;
        }
        super.onDraw(canvas);
    }
}
