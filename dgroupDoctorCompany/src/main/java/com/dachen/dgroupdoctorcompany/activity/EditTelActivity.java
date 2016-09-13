package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dachen.common.utils.Md5Util;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.net.LoginLogic;
import com.dachen.dgroupdoctorcompany.views.CustomDialog;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;

/**
 * 修改手机号
 *
 */
public class EditTelActivity extends BaseActivity implements OnClickListener {

    private static final String TAG = EditTelActivity.class.getSimpleName();


    private TextView link_service,phone_num;
    private Button next_btn;
    private String number = "400-618-8886";
    EditText password;
    String tel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tel);
        setTitle("修改手机号码");
        init();
    }

    private void init() {
        link_service = getViewById(R.id.link_service);
        phone_num = getViewById(R.id.phone_num);
        next_btn = getViewById(R.id.next_btn);
        password = getViewById(R.id.password);

        tel = SharedPreferenceUtil.getString(this, "telephone", "");
        SharedPreferenceUtil.getString(this,"telephone", "");
        if (!TextUtils.isEmpty(tel)) {
            String telele = tel.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            phone_num.setText(telele);
        }

        link_service.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    next_btn.setBackgroundResource(R.drawable.btn_blue_all_3cbaff);
                        return;
                }
                next_btn.setBackgroundResource(R.drawable.btn_blue_all_9ddcff);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.next_btn:
                if (TextUtils.isEmpty(password.getText().toString().trim())) {
                    ToastUtils.showToast(EditTelActivity.this, "密码不能为空");
                } else {
                    showLoadingDialog();
                    LoginLogic.loginRequest(tel,password.getText().toString().trim(),this,LoginLogic.EDITPHONE);
                }

                break;
            case R.id.link_service:

                CustomDialog.Builder builder = new CustomDialog.Builder(EditTelActivity.this,new CustomDialog.CustomClickEvent(){


                    @Override
                    public void onClick(CustomDialog customDialog) {
                        customDialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
                        startActivity(intent);
                    }

                    @Override
                    public void onDismiss(CustomDialog customDialog) {
                        customDialog.dismiss();
                    }
                }).setTitle("提示").setMessage("是否拨打客服电话" + number).setPositive("是").setNegative("否");
                builder.create().show();

                break;
        }
    }

}
