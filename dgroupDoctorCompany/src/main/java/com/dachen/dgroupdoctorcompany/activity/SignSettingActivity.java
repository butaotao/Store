package com.dachen.dgroupdoctorcompany.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;

/**
 * Created by weiwei on 2016/7/13.
 */
public class SignSettingActivity extends BaseActivity{
    private RelativeLayout signRemind;
    private RelativeLayout vAddSign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_setting);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        signRemind = (RelativeLayout) findViewById(R.id.signRemind);
        vAddSign = (RelativeLayout) findViewById(R.id.vAddSign);
        signRemind.setOnClickListener(this);
        vAddSign.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.signRemind:
                break;
            case R.id.vAddSign:
                break;
        }

    }
}
