package com.dachen.dgroupdoctorcompany.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.utils.GetAllDoctor;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;
import com.dachen.medicine.view.ClearEditText;

import java.util.ArrayList;

/**
 * Created by weiwei on 2016/5/19.
 */
public class UpdateUserInfoActivity extends BaseActivity implements HttpManager.OnHttpListener {
    public static final int MODE_UPDATE_NAME = 1;
    public static final int MODE_UPDATE_JOB_TITLE = 2;
    public static final int MODE_UPDATE_NAME_MANAGER = 3;
    public static final int MODE_UPDATE_JOB_TITLE_MANAGER = 4;
    private ClearEditText mEtUserName;
    private TextView mTvSave;
    private String mStrUserName="";
    private String mStrJobTitle="";
    private String mStrOrgId="";
    private int mMode;
    private String mStrGroupName="";
    private String id;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1100:
                    closeLoadingDialog();
                    ToastUtil.showToast(UpdateUserInfoActivity.this,"修改成功");
                    finish();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_username);
        initView();
        initDate();
    }

    @Override
    public void initView() {
        super.initView();
        mEtUserName = (ClearEditText) findViewById(R.id.etUserName);
        mTvSave = (TextView) findViewById(R.id.tvSave);
        mTvSave.setOnClickListener(this);
    }

    private void initDate(){
        mMode = this.getIntent().getIntExtra("mode",MODE_UPDATE_NAME);
        String name = this.getIntent().getStringExtra("name");
        if(MODE_UPDATE_NAME == mMode){
            setTitle("修改姓名");
             name = SharedPreferenceUtil.getString(UpdateUserInfoActivity.this,"username", "");
            id = SharedPreferenceUtil.getString(this, "id", "");
            mEtUserName.setHint("输入姓名");
            mEtUserName.setText(name);
        }else if(MODE_UPDATE_JOB_TITLE == mMode){
            setTitle("我的职位");
            mStrJobTitle = this.getIntent().getStringExtra("job_title");
            mStrOrgId = this.getIntent().getStringExtra("part");
            mEtUserName.setHint("输入职位名称");
            mEtUserName.setText(mStrJobTitle);
        }else if (MODE_UPDATE_NAME_MANAGER == mMode){
            setTitle("修改姓名");

            mEtUserName.setHint("输入姓名");
            mEtUserName.setText(name);
            id = getIntent().getStringExtra("id");
        }else if (MODE_UPDATE_JOB_TITLE_MANAGER == mMode){
            setTitle("修改职位");
            id = getIntent().getStringExtra("id");
            mStrJobTitle = this.getIntent().getStringExtra("job_title");
            mStrOrgId = this.getIntent().getStringExtra("part");
            mEtUserName.setHint("输入职位名称");
            mEtUserName.setText(mStrJobTitle);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tvSave:
                if(MODE_UPDATE_NAME == mMode){

                    updateName(id);
                }else if(MODE_UPDATE_JOB_TITLE == mMode){
                    updateJobTitle(id);
                }else if (MODE_UPDATE_NAME_MANAGER == mMode){
                    updateName(id);
                }else if(MODE_UPDATE_JOB_TITLE_MANAGER == mMode){
                    updateJobTitle(id);
                }

                break;
        }
    }

    private void updateName(String id){
        mStrUserName = mEtUserName.getText().toString().trim();
        if(TextUtils.isEmpty(mStrUserName)){
            ToastUtil.showToast(UpdateUserInfoActivity.this,"请输入姓名");
            return;
        }

        showLoadingDialog();
        new HttpManager().post(this, Constants.UPDATE_USER_NAME,Result.class, Params
                .updateUserName(UpdateUserInfoActivity.this,mStrUserName,id),this,false,1);
    }

    private void updateJobTitle(String id){
        mStrJobTitle = mEtUserName.getText().toString().trim();
        if(TextUtils.isEmpty(mStrJobTitle)){
            ToastUtil.showToast(UpdateUserInfoActivity.this,"请输入职位名称");
            return;
        }
        showLoadingDialog();
        new HttpManager().post(this, Constants.UPDATE_JOB_TITLE,Result.class, Params
                .updateJobTitle(UpdateUserInfoActivity.this,mStrOrgId,mStrJobTitle,id),this,false,1);
    }

    @Override
    public void onSuccess(Result response) {

        if(null != response){
            if(response.getResultCode() == 1){
                if(MODE_UPDATE_NAME == mMode){
                    SharedPreferenceUtil.putString(UpdateUserInfoActivity.this,"username",mStrUserName);
                }

                GetAllDoctor.getInstance().getPeople(this, handler);

            }else {
                closeLoadingDialog();
                ToastUtil.showToast(this,response.resultMsg);
            }
        }

    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        closeLoadingDialog();
    }
}
