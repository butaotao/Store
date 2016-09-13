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
import android.widget.EditText;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.AddSignInActivity;
import com.dachen.dgroupdoctorcompany.activity.MenuWithFABActivity;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.entity.SignTodayInList;
import com.dachen.dgroupdoctorcompany.utils.UserInfo;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;
import com.dachen.medicine.view.ScrollTabView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Burt on 2016/6/23.
 */
public class DialogEditorText extends Dialog implements View.OnClickListener,ScrollTabView.OnInitView {
    MenuWithFABActivity mActivity;
    SignTodayInList.Data.DataList data;
    EditText et_edit;
    public DialogEditorText(MenuWithFABActivity activity,SignTodayInList.Data.DataList data) {
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
        et_edit = (EditText) findViewById(R.id.et_edit);
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
                upDate(data.signedId,et_edit.getText().toString());
                break;
        }
    }
    public void upDate(String id,String des){
        mActivity.showLoadingDialog();
        HashMap<String,String> maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(mActivity).getSesstion());
        maps.put("id",id);
        maps.put("remark",des);
         new HttpManager().post(mActivity, "sign/signed/updateSigned", Result.class,
                 maps,
                 new HttpManager.OnHttpListener<Result>() {
                     @Override
                     public void onSuccess(Result response) {
                         mActivity.closeLoadingDialog();
                     }

                     @Override
                     public void onSuccess(ArrayList<Result> response) {

                     }

                     @Override
                     public void onFailure(Exception e, String errorMsg, int s) {
                         mActivity.closeLoadingDialog();
                     }
                 }, false, 1);
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
