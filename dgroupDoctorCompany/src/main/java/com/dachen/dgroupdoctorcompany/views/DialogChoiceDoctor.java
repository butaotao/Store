package com.dachen.dgroupdoctorcompany.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.ProvinceActivity;
import com.dachen.dgroupdoctorcompany.activity.SearchActivity;

/**
 * Created by Burt on 2016/2/27.
 */
public class DialogChoiceDoctor implements View.OnClickListener{
    Activity context;
    Dialog dialog;
    public DialogChoiceDoctor(Activity con){
        this.context = con;
        dialog = new Dialog(con, R.style.addresspickerstyle);
        View mView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_choice_doctor, null);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(mView);
        dialog.getWindow().setGravity(Gravity.CENTER);
        Window window = con.getWindow();

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        float mWidth = con.getResources().getDisplayMetrics().widthPixels;
        float height = con.getResources().getDisplayMetrics().heightPixels;
        lp.width = (int) (mWidth );
        lp.height=(int)height;
      //  window.setAttributes(lp);
        mView.findViewById(R.id.iv_keywordfinddoctor).setOnClickListener(this);
        mView.findViewById(R.id.iv_provincefinddoctor).setOnClickListener(this);
        mView.findViewById(R.id.iv_cancel).setOnClickListener(this);
    }
    public void hide() {
        if (null!=dialog){
            dialog.hide();
        }
    }

    public void dismiss() {
        if (null!=dialog){
            dialog.dismiss();}
    }
    public boolean isShow(){
        if (null!=dialog){
        return dialog.isShowing();
        }
        return false;
    }
    public void showDialog(){
        if (null!=dialog&&!dialog.isShowing()){
            dialog.show();
        }
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.iv_keywordfinddoctor:
                dismiss();
                intent = new Intent(context,SearchActivity.class);
                context.startActivity(intent);
                break;
            case R.id.iv_provincefinddoctor:
                dismiss();
                intent = new Intent(context,ProvinceActivity.class);
                context.startActivity(intent);
                break;
            case R.id.iv_cancel:
                dismiss();
                break;
        }

    }
}
