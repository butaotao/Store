package com.dachen.dgroupdoctorcompany.views;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.medicine.view.ScrollTabView;

import java.util.ArrayList;
import java.util.Calendar;

import cn.qqtheme.framework.picker.DatePicker;

/**
 * Created by Burt on 2016/6/23.
 */
public class ChoiceTimeDialog extends Dialog implements View.OnClickListener,ScrollTabView.OnInitView {
    Activity mActivity;
    TextView textView;
    public ChoiceTimeDialog(Activity activity) {
        super(activity, R.style.dialog_with_alpha);
        this.mActivity = activity;
        this.textView = textView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {//dialog_medieinfo
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choicetime);
        findViewById(R.id.rl_all).setOnClickListener(this);
        findViewById(R.id.rl_normal).setOnClickListener(this);
        findViewById(R.id.rl_err).setOnClickListener(this);
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);

        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.TOP);
        window.setWindowAnimations(R.style.Umengstyle);
        setCancelable(true);
        this.setCanceledOnTouchOutside(true);

        lp.width = (int) (metric.widthPixels);; // 宽度
        lp.height = (int) (metric.heightPixels-104);; // 高度/*
       /* lp.alpha = 0.7f; // 透明度*/
        window.setAttributes(lp);
        selectDate();
    }





    @Override
    public void onClick(View v) {

        CloseDialog();
    }

    public void ShowDialog(){
        if(!isShowing() ){

            show();

        }

    }

    public void CloseDialog(){
        if(isShowing())
            dismiss();
    }



    @Override
    public void onInit(ArrayList<View> arg0) {
        // TODO Auto-generated method stub

    }
    private void selectDate() {
        Calendar calendar = Calendar.getInstance();
        DatePicker picker = new DatePicker(mActivity);
        picker.setRange(2000, 2100);

        picker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
             /*   mMeetingDate.setText(year + "-" + month + "-" + day);
                mMeetingDate.setTextColor(getResources().getColor(R.color.gray_333333));*/
                ToastUtil.showToast(mActivity, year + "-" + month + "-" + day);
            }
        });
        picker.show();
    }
}
