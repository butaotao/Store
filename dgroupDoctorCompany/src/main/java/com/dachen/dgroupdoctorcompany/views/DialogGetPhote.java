package com.dachen.dgroupdoctorcompany.views;

/**
 * Created by Burt on 2016/2/19.
 */

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import com.dachen.dgroupdoctorcompany.R;


public class DialogGetPhote extends Dialog implements View.OnClickListener {
    private Activity mActivity;
    private OnClickListener listener;
    LinearLayout lv_photo_cancel;
    LinearLayout lv_photo_choicephotofromalbum;
    LinearLayout lv_photo_choicephotofromcamera;
    LinearLayout spaceforcancel;
    String methodOne;
    String methodTwo;
    TextView dialog_methodtwo;
    TextView dialog_showmethodone;
    TextView tv_classes;
    TextView tv_sellclass;
    TextView tv_specification;
    TextView tv_packaging;
    TextView tv_num;
    TextView tv_generalname;
    HashMap<String, String> maps ;
    private WebView wv;
    private String linkUri;
    public DialogGetPhote(Activity activity, OnClickListener l,String methodOne,String methodTwo) {
        super(activity, R.style.dialog_with_alpha);
        this.mActivity = activity;
        this.listener = l;
        this.methodOne = methodOne;
        this.methodTwo = methodTwo;

    }
    public DialogGetPhote(Activity activity, OnClickListener l) {
        super(activity, R.style.dialog_with_alpha);
        this.mActivity = activity;
        this.listener = l;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {//dialog_medieinfo
        super.onCreate(savedInstanceState);


        setContentView(R.layout.dialog_takefoto);
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = (int) (metric.widthPixels);
        Window window = getWindow();
        window.setAttributes(p);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.Umengstyle);
        setCancelable(true);
        this.setCanceledOnTouchOutside(true);
        this.findViewById(R.id.lv_tackfoto).setOnClickListener(this);
        this.findViewById(R.id.lv_getfotofromlibrary).setOnClickListener(this);
        this.findViewById(R.id.ll_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lv_tackfoto:
                listener.btnCameraClick(v);
                CloseDialog();
                break;
            case R.id.lv_getfotofromlibrary:
                listener.btnPhotoClick(v);
                CloseDialog();
                break;
            case R.id.ll_cancel:
                CloseDialog();
                break;
            default:
                break;
        }
    }

    public void ShowDialog(){
        if(!isShowing())
            show();
    }

    public void CloseDialog(){
        if(isShowing())
            dismiss();
    }

    public interface OnClickListener {
        public void btnCameraClick(View v);//

        public void btnPhotoClick(View v);//
    }


}
