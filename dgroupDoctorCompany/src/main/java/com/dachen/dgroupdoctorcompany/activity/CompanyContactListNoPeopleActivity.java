package com.dachen.dgroupdoctorcompany.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.medicine.common.utils.MActivityManager;

/**
 * Created by Burt on 2016/4/29.
 */
public class CompanyContactListNoPeopleActivity extends BaseActivity implements View.OnClickListener{
    TextView tv_des;
    private TextView tv_nocontent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companynopeoplecontactlist);
        setTitle(getIntent().getStringExtra("dept"));
        tv_nocontent = (TextView) findViewById(R.id.tv_notcontent);
        tv_des = (TextView) findViewById(R.id.tv_des);
        tv_des.setOnClickListener(this);
        tv_des.setText("关闭");
        tv_nocontent.setText("该部门没有成员");
        tv_des.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_des:
                MActivityManager.getInstance().popActivity(this);
                MActivityManager.getInstance().finishActivityE(CompanyContactListActivity.class);
                break;
        }
    }
}
