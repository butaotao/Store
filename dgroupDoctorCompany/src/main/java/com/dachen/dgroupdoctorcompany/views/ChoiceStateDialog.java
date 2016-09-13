package com.dachen.dgroupdoctorcompany.views;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.BaseRecordActivity;
import com.dachen.medicine.view.ScrollTabView;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/6/23.
 */
public class ChoiceStateDialog extends Dialog implements View.OnClickListener,ScrollTabView.OnInitView {
    Activity mActivity;
    TextView textView;
    ImageView imageView;
    TextView tv_first;
    TextView tv_second;
    TextView tv_three;
    RelativeLayout rl_wait;
    int state;
    int choice;
    RelativeLayout rl_cancel;
    BaseRecordActivity.GetRecord record;
    BaseRecordActivity.RefreshData refreshData;
    public ChoiceStateDialog(Activity activity,TextView textView,ImageView imageView,int state,
                             BaseRecordActivity.GetRecord record,BaseRecordActivity.RefreshData refreshData) {
        super(activity, R.style.dialog_with_alpha);
        this.mActivity = activity;
        this.textView = textView;
        this.imageView = imageView;
        this.state =state;
        this.record =record;
        this.refreshData = refreshData;
        if (state==2||state==4){
            textView.setText("全部拜访");
        } else {
            textView.setText("全部状态");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {//dialog_medieinfo
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_chicedepstate);
        findViewById(R.id.rl_all).setOnClickListener(this);
        findViewById(R.id.rl_normal).setOnClickListener(this);
        findViewById(R.id.rl_err).setOnClickListener(this);
        findViewById(R.id.rl_cancel).setOnClickListener(this);
        rl_wait = (RelativeLayout) findViewById(R.id.rl_wait);
        rl_wait.setOnClickListener(this);
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        tv_first = (TextView) findViewById(R.id.tv_first);
        tv_second = (TextView) findViewById(R.id.tv_second);
        tv_three = (TextView) findViewById(R.id.tv_three);
        if (state==2||state==4){
            tv_first.setText("全部拜访");
            tv_second.setText("独立拜访");
            tv_three.setText("协同拜访");
            textView.setText("全部拜访");
        }else {
            textView.setText("全部状态");
            rl_wait.setVisibility(View.GONE);
        }
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.Umengstyle);
        setCancelable(true);
        this.setCanceledOnTouchOutside(true);

        lp.width = (int) (metric.widthPixels);; // 宽度
        lp.height = (int) (metric.heightPixels);; // 高度/*
       /* lp.alpha = 0.7f; // 透明度*/
        window.setAttributes(lp);
    }





    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_all) {
            choice = 0;
            if (state==2||state==4){
                textView.setText("全部拜访");
            }else {
                textView.setText("全部状态");
            }

        }else if(v.getId() == R.id.rl_normal){
            choice = 1;
            if (state==2||state==4){
                textView.setText("独立拜访");
            }else{
                textView.setText("正常");
            }

        }else if(v.getId() == R.id.rl_err){
            choice = 2;
            if (state==2||state==4){
                textView.setText("协同拜访");
            }else{
                textView.setText("异常");
            }

        }else if (v.getId() == R.id.rl_cancel){
            imageView.setBackgroundResource(R.drawable.recordirro);
            textView.setTextColor(mActivity.getResources().getColor(R.color.color_333333));
            if(isShowing())
                dismiss();
            return;
        }else if (v.getId() == R.id.rl_wait){
            choice = 4;
            if (state==2||state==4){
                rl_wait.setVisibility(View.GONE);
            }else{
                rl_wait.setVisibility(View.VISIBLE);
                textView.setText("待定");

            }
        }
        CloseDialog();
    }

    public void showDialog(){
        imageView.setBackgroundResource(R.drawable.recordirro_select);
        textView.setTextColor(mActivity.getResources().getColor(R.color.color_3cbaff));
        if(!isShowing() ){

            show();

        }

    }

    public void CloseDialog(){
        imageView.setBackgroundResource(R.drawable.recordirro);
        textView.setTextColor(mActivity.getResources().getColor(R.color.color_333333));
        if(isShowing())
            dismiss();
        if (null!=record){
            record.initConfig();
            record.getRecord("");
        }
        if (state ==3||state==4){
            refreshData.refresh(choice);
        }

    }



    @Override
    public void onInit(ArrayList<View> arg0) {
        // TODO Auto-generated method stub

    }
}
