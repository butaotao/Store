package com.dachen.dgroupdoctorcompany.views;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.BaseRecordActivity;
import com.dachen.dgroupdoctorcompany.adapter.ChoiceAdpHorizonAdapter;
import com.dachen.dgroupdoctorcompany.adapter.ChoiceAdpVertiAdapter;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.DepAdminsListDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.DepAdminsList;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.view.ScrollTabView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Burt on 2016/6/23.
 */
public class GuiderDialog extends Dialog implements View.OnClickListener,ScrollTabView.OnInitView {
    Activity mActivity;

    public GuiderDialog(Activity activity ) {
        super(activity, R.style.dialog_with_alpha);
        mActivity = activity;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {//dialog_medieinfo
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_guider);

        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        findViewById(R.id.btn_gosetting).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
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
        if (v.getId() == R.id.btn_gosetting) {
            GuiderDialogsetworkTime dialogsetworkTime = new GuiderDialogsetworkTime(mActivity);
            dialogsetworkTime.showDialog();
            closeDialog();
        }else if(v.getId() == R.id.btn_cancel){
//            CloseDialog();
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
