package com.dachen.dgroupdoctorcompany.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.fragment.AddressList;
import com.dachen.dgroupdoctorcompany.utils.GetAllDoctor;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Burt on 2016/5/16.
 */
public class AddFriendByPhone extends BaseActivity implements HttpManager.OnHttpListener{
    @Bind(R.id.phone_numer_edit)
    EditText phone_numer_edit;
    @Bind(R.id.name_edit)
    EditText name_edit;
    @Bind(R.id.btn_sure)
    Button btn_sure;
    @Bind(R.id.rl_back)
    RelativeLayout rl_back;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                ToastUtil.showToast(AddFriendByPhone.this, "添加成功");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend_byphone);
        ButterKnife.bind(this);
        setTitle("手机号码添加");
     //   enableBack();
        phone_numer_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())&&!TextUtils.isEmpty(name_edit.getText().toString().trim())){
                    btn_sure.setBackgroundResource(R.drawable.btn_blue_all_3cbaff);
                }else {
                    btn_sure.setBackgroundResource(R.drawable.btn_blue_all_9ddcff);
                }
            }
        });
        name_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())&&!TextUtils.isEmpty(phone_numer_edit.getText().toString().trim())){
                    btn_sure.setBackgroundResource(R.drawable.btn_blue_all_3cbaff);
                }else {
                    btn_sure.setBackgroundResource(R.drawable.btn_blue_all_9ddcff);
                }
            }
        });
    }
    @Nullable
    @OnClick(R.id.btn_sure)
    void btnSure(){
        String phone =  phone_numer_edit.getText().toString().trim();
        String name =  name_edit.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showToast(this, "您未输入被邀请职员的手机号");
            return;
        }
        if(phone.startsWith("+86")){
            phone=phone.replace("+86","");
        }
        if (phone.length()!=11) {
            ToastUtils.showToast(this, "手机号格式不正确");
            return;
        }
        if (TextUtils.isEmpty(name)){
            ToastUtils.showToast(this, "您未输入被邀请职员的姓名");
            return;
        }
        submintToServer(phone,name);
    }
    @Nullable
    @OnClick(R.id.rl_back)
    void back(){
        this.finish();
    }
    public void submintToServer(String phoneNumber,String name){
        HashMap<String ,String > maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(this).getSesstion());
        maps.put("drugCompanyId", SharedPreferenceUtil.getString(this, "enterpriseId", ""));
        maps.put("id",""+ AddressList.deptId);
        maps.put("telephone",phoneNumber);
        //1表示移动端添加
        maps.put("addSource","1");
        String userName = "";
        if (!TextUtils.isEmpty(name)){
            userName = name;
        }
        showLoadingDialog();
        maps.put("name",userName);
        new HttpManager().post(this, "org/drugCompany/dept/addEnterUser", Result.class,
                maps, new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result response) {
                        if (response.resultCode==1&&response.resultMsg.equals("success")){
                            GetAllDoctor.getInstance().getPeople(AddFriendByPhone.this,handler);

                        }else {
                            closeLoadingDialog();
                            if (!TextUtils.isEmpty(response.resultMsg)){

                                ToastUtil.showToast(AddFriendByPhone.this,response.resultMsg);
                            }else {
                                ToastUtil.showToast(AddFriendByPhone.this,"添加失败");
                            }
                        }
                    }

                    @Override
                    public void onSuccess(ArrayList<Result> response) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {

                    }
                },
                false, 1);
    }

    @Override
    public void onSuccess(Result response) {

    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {

    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(this,FriendsContactsActivity.class);
//        startActivity(intent);
//        finish();
//    }
}
