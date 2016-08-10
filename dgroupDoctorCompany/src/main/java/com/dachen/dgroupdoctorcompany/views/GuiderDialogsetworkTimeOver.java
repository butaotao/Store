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

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.medicine.view.ScrollTabView;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/6/23.
 */
public class GuiderDialogsetworkTimeOver extends Dialog implements View.OnClickListener,ScrollTabView.OnInitView {
    Activity mActivity;
    TextView tv_onwork;
    int isworkTime;
    public GuiderDialogsetworkTimeOver(Activity activity) {
        super(activity, R.style.dialog_with_alpha);
        mActivity = activity;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {//dialog_medieinfo
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_worktimesetover);
        isworkTime = 0;
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        findViewById(R.id.btn_gosetting).setOnClickListener(this);
        tv_onwork = (TextView) findViewById(R.id.tv_onwork);
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.Umengstyle);
        setCancelable(true);
        this.setCanceledOnTouchOutside(false);

        lp.width = (int) (metric.widthPixels)-200;; // 宽度
        lp.height = (int) (metric.heightPixels);; // 高度/*
       /* lp.alpha = 0.7f; // 透明度*/
        window.setAttributes(lp);
    }





    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_gosetting) {

                closeDialog();


        }
    }

    public void showDialog(){
        if(!isShowing() ){

            show();

        }

    }
    public void showDialog(String id){



       showDialog();;

    }
    public void closeDialog(){
        if(isShowing())
            dismiss();
    }



    @Override
    public void onInit(ArrayList<View> arg0) {
        // TODO Auto-generated method stub

    }
}
