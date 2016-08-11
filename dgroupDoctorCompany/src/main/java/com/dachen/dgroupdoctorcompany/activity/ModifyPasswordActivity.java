package com.dachen.dgroupdoctorcompany.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;

import com.dachen.common.utils.Md5Util;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.Void;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改密码
 *
 * @author lmc
 *
 */
public class ModifyPasswordActivity extends BaseActivity {

    private static final String TAG = ModifyPasswordActivity.class.getSimpleName();

    @Nullable
    @Bind(R.id.modifyPwdActivity_oldPwd)
    EditText mOldPwdView;

    @Nullable
    @Bind(R.id.modifyPwdActivity_newPwd)
    EditText mNewPwdView;

    @Nullable
    @Bind(R.id.modifyPwdActivity_confirmPwd)
    EditText mConfirmPwdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        setTitle("修改密码");
        ButterKnife.bind(this);
    }


    @Nullable
    @OnClick(R.id.modifyPwdActivity_submit)
    void onSubmitBtnClicked() {

        final String oldPwd = mOldPwdView.getText().toString();
        final String newPwd = mNewPwdView.getText().toString();
        String confirmPwd = mConfirmPwdView.getText().toString();

        if (TextUtils.isEmpty(oldPwd)) {
            mOldPwdView.requestFocus();
            ToastUtils.showToast(this, "请输入旧密码");
            return;
        }
        if (TextUtils.isEmpty(newPwd)) {
            mNewPwdView.requestFocus();
            ToastUtils.showToast(this, "请输入新密码");
            return;
        }
        if (newPwd.length() < 6) {
            mNewPwdView.requestFocus();
            ToastUtils.showToast(this, "请输入6-18位新密码");
            return;
        }
        if (TextUtils.isEmpty(confirmPwd)) {
            mConfirmPwdView.requestFocus();
            ToastUtils.showToast(this, "请输入确认密码");
            return;
        }
        if (!newPwd.equals(confirmPwd)) {
            mConfirmPwdView.requestFocus();
            ToastUtils.showToast(getApplicationContext(), "两次密码输入不一致");
            return;
        }

        showLoadingDialog();

        Map<String, String> params = new HashMap<String, String>();
        String userId = SharedPreferenceUtil.getString(this,"id","");
        String session = SharedPreferenceUtil.getString(this,"session","");
        params.put("userId", userId);
        params.put("oldPassword", oldPwd);
        params.put("newPassword", newPwd);
        params.put("access_token", session);
        new HttpManager().post(this, Constants.DRUG+"drugCompanyEmployee/updatePassword",
                Void.class, params, new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        closeLoadingDialog();
                        if (result != null) {
                            Void v = (Void) result;
                            if (v.resultCode == 1) {
                                ToastUtils.showToast(ModifyPasswordActivity.this,"修改密码成功");

                                finish();
                            } else if (v.resultCode == 0||v.resultCode ==100) {
                                ToastUtils.showToast(ModifyPasswordActivity.this,"旧密码输入错误");
                            } else {
                                ToastUtils.showToast(ModifyPasswordActivity.this,v.resultMsg);
                            }
                        }
                    }
                    @Override
                    public void onSuccess(ArrayList<Result> response) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {
                        closeLoadingDialog();
                    }
                },false, 1);



    }
}
