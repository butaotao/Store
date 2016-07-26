package com.dachen.dgroupdoctorcompany.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.ChoiceDepList;
import com.dachen.dgroupdoctorcompany.utils.JsonUtils.SingRecordTrans;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Burt on 2016/6/23.
 */
public class RecordActivity extends BaseActivity   implements View.OnClickListener,HttpManager.OnHttpListener{
    public static String orgId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        findViewById(R.id.rl_visitrecord).setOnClickListener(this);
        findViewById(R.id.rl_singrecord).setOnClickListener(this);
        setTitle("签到统计类型");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()){
            case R.id.rl_visitrecord:
                intent = new Intent(this,VisitRecordActivity.class);
                intent.putExtra("type","visit");
                startActivity(intent);
                break;
            case R.id.rl_singrecord:
                intent = new Intent(this,SignRecordActivity.class);
                intent.putExtra("type","sign");
                startActivity(intent);
                break;
        }
    }
    private void getDep() {
        showLoadingDialog();
        HashMap<String ,String > maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(this).getSesstion());
        maps.put("userId", SharedPreferenceUtil.getString(this, "id", ""));
        maps.put("drugCompanyId", SharedPreferenceUtil.getString(this, "enterpriseId", ""));
        new HttpManager().post(this, Constants.MANINDEP, ChoiceDepList.class,
                maps, this,
                false, 1);
    }

    @Override
    public void onSuccess(Result response) {
        if (response.resultCode==1) {
            if (response instanceof ChoiceDepList) {

                if (response instanceof ChoiceDepList) {
                    ChoiceDepList list = (ChoiceDepList) response;
                    if (null != list.data) {
                        SingRecordTrans.processData(this, list.data);
                        orgId = list.data.orgId;
                    }

                }
            }
        }
    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {

    }
}
