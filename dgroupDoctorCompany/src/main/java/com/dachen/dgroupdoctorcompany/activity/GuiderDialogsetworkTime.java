package com.dachen.dgroupdoctorcompany.activity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.views.GuiderDialogsetofferworkTime;
import com.dachen.dgroupdoctorcompany.views.TimePickerCustomer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Burt on 2016/8/10.
 */
public class GuiderDialogsetworkTime extends BaseActivity implements View.OnClickListener{
    TimePickerCustomer timePicker;
    @Override
                                       protected void onCreate(Bundle savedInstanceState) {//dialog_medieinfo
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_worktime);
   /* DisplayMetrics metric = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(metric);*/
    findViewById(R.id.btn_gosetting).setOnClickListener(this);
   /* Window window = getWindow();
    WindowManager.LayoutParams lp = window.getAttributes();
    window.setGravity(Gravity.BOTTOM);
    window.setWindowAnimations(R.style.Umengstyle);

    lp.width = (int) (metric.widthPixels)-100;; // 宽度
    lp.height = (int) (metric.heightPixels);; // 高度*//*
       *//* lp.alpha = 0.7f; // 透明度*//*
    window.setAttributes(lp);*/
        timePicker = (TimePickerCustomer) findViewById(R.id.minute_pv);

            long curTime = System.currentTimeMillis();
            Calendar c = Calendar.getInstance();
            c.setTime(new Date(curTime));
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            timePicker.hours.setItems(timePicker.hourList,hour+"");
            timePicker.mins.setItems(timePicker.minuteList, minute + "");



        timePicker.setOnChangeListener(new TimePickerCustomer.OnChangeListener() {
            @Override
            public void onChange(int hours, int munites) {
              /*  hour = hours;
                minute = munites;*/
            }
        });
}





    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_gosetting) {
            GuiderDialogsetofferworkTime dialogsetworkTimeOver = new GuiderDialogsetofferworkTime(this);
            dialogsetworkTimeOver.showDialog();


        }
    }


}
