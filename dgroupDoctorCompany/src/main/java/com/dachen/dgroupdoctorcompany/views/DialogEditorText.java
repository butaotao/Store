package com.dachen.dgroupdoctorcompany.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.AddSignInActivity;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.entity.SignTodayInList;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;
import com.dachen.medicine.view.ScrollTabView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Burt on 2016/6/23.
 */
public class DialogEditorText extends Dialog implements View.OnClickListener,ScrollTabView.OnInitView {
    Activity mActivity;
    SignTodayInList.Data.DataList data;
    public DialogEditorText(Activity activity,SignTodayInList.Data.DataList data) {
        super(activity, R.style.dialog_with_alpha);
        mActivity = activity;
        this.data = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {//dialog_medieinfo
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_editortext);

        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.sure).setOnClickListener(this);
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.Umengstyle);
        setCancelable(true);
        this.setCanceledOnTouchOutside(false);

        lp.width = (int) (metric.widthPixels)-100;; // 宽度
        lp.height = (int) (metric.heightPixels);; // 高度/*
       /* lp.alpha = 0.7f; // 透明度*/
        window.setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                closeDialog();
                break;
            case R.id.sure:
                closeDialog();

                break;
        }
    }
    public void upDate(){
       /* new HttpManager().post(this, Constants.CREATE_OR_UPDATA_SIGIN_IN, Result.class,
                Params.getWorkingParams(mActivity, deviceId, remark, mId, coordinate, address, signLable, orgId),
                this,false,4);*/
    }
    public void showDialog(){
            if(!isShowing() ){

                show();

            }

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
