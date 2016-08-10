package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;

/**
 * Created by Burt on 2016/8/10.
 */
public class GuiderDialogActivity extends BaseActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {//dialog_medieinfo
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_guider);

       /* DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);*/
        findViewById(R.id.btn_gosetting).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
       /* Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.Umengstyle);

        lp.width = (int) (metric.widthPixels)-200;; // 宽度
        lp.height = (int) (metric.heightPixels);; // 高度*//*
       *//* lp.alpha = 0.7f; // 透明度*//*
        window.setAttributes(lp);*/
    }





    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_gosetting) {
            finish();
             Intent intent = new Intent(this,GuiderDialogsetworkTime.class);
            startActivity(intent);
        }else if(v.getId() == R.id.btn_cancel){
//            CloseDialog();
            closeDialog();
        }
    }

    public void showDialog(){


    }
    public void showDialog(String id){



        showDialog();;

    }
    public void closeDialog(){

    }
}
