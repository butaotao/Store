package com.dachen.dgroupdoctorcompany.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Burt on 2016/8/29.
 */
public class RotateTextView extends TextView {

        public RotateTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
            // TODO Auto-generated constructor stub
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.rotate(90);
            canvas.translate(0, -getWidth());
            super.onDraw(canvas);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if(getMeasuredWidth() >= getMeasuredHeight()){
                setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
            }else if(getMeasuredWidth() < getMeasuredHeight()){
                setMeasuredDimension(getMeasuredHeight(), getMeasuredHeight());
            }

        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(h, w, oldh, oldw);
        }
    }