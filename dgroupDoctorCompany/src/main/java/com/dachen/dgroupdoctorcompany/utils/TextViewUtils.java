package com.dachen.dgroupdoctorcompany.utils;

import android.annotation.TargetApi;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * @项目名 MedicineProject
 * @Author: zxy on 16/8/12下午1:54.
 * @描述 计算textView长宽
 */
public class TextViewUtils {

    public static int measureTextLength(TextView tv) {
        Paint paint = null;
        if (paint == null) {
            paint = new Paint();
        }
        paint.setTextSize(tv.getTextSize());
        return (int)(paint.measureText(tv.getText() + ""));
    }

    static boolean flag = false;
    public static boolean testWeith(final TextView textView) {
        final int[] textViewWidth = new int[1];
        final float textViewLength = (measureTextLength(textView) + 0.5f) + textView.getPaddingLeft() + textView
                .getPaddingRight();
        textView.measure(0,0);
        textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                textViewWidth[0] = textView.getWidth();
                textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        return flag;
    }

    public static int getViewMeasureWeith(View view){
        view.measure(0,0);
        return view.getMeasuredWidth();
    }
}
