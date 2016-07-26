package com.dachen.medicine.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.dachen.medicine.R;
import com.dachen.medicine.app.Constants;
import com.dachen.medicine.app.MedicineApplication;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.StringUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.common.utils.UserUtils.LoginUtils;
import com.dachen.medicine.db.MedicineProvider;
import com.dachen.medicine.db.MedicineURIField;
import com.dachen.medicine.db.table.TableUser;
import com.dachen.medicine.entity.LoginRegisterResult;
import com.dachen.medicine.entity.ResetPassword;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.entity.User;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 重置密码界面
 */
public class ResetPasswdActivity extends BaseActivity implements
        OnClickListener, OnHttpListener {

    private static final String TAG = ResetPasswdActivity.class.getSimpleName();

    @Nullable
    @Bind(R.id.reset_top_txt)
    TextView mResetTopTxt;

    @Nullable
    @Bind(R.id.password_edit)
    EditText mPasswordEdit;
    @Nullable
    @Bind(R.id.confirm_password_edit)
    EditText mConfirmPasswordEdit;

    protected ProgressDialog mProgressDialog;
    private String phone;
    private String ranCode;
    private String smsid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_passwd);
        ButterKnife.bind(this);

        phone = getIntent().getStringExtra(PreResetPasswdActivity.PHONE);
        ranCode = getIntent().getStringExtra(PreResetPasswdActivity.RANCODE);
        smsid = getIntent().getStringExtra(PreResetPasswdActivity.SMSID);

        initView();
    }

    private void initView() {
        mResetTopTxt.setText("找回密码");
    }

    @Nullable
    @OnClick(R.id.back_step_btn)
    void onBackBtnClicked() {
        finish();
    }

    @Nullable
    @OnClick(R.id.sure_btn)
    void onSureBtnClicked() {
        // http请求
        sureResetPasswd();
    }

    private void sureResetPasswd() {
        // TODO Auto-generated method stub

        final String password = mPasswordEdit.getText().toString().trim();
        final String userType = Constants.USERTYPE;
        String confirmPassword = mConfirmPasswordEdit.getText().toString()
                .trim();

        if (TextUtils.isEmpty(password) || password.length() < 6 || password.length() > 18) {
            mPasswordEdit.requestFocus();
            mPasswordEdit.setError(StringUtils.editTextHtmlErrorTip(this,
                    R.string.password_empty_error));
            return;
        }
        //		if (TextUtils.isEmpty(confirmPassword) || confirmPassword.length() < 6
        //				|| confirmPassword.length() > 18) {
        //			mConfirmPasswordEdit.requestFocus();
        //			mConfirmPasswordEdit.setError(StringUtils.editTextHtmlErrorTip(
        //					this, R.string.confirm_password_empty_error));
        //			return;
        //		}
        if (!confirmPassword.equals(password)) {
            mConfirmPasswordEdit.requestFocus();
            mConfirmPasswordEdit.setError(StringUtils.editTextHtmlErrorTip(
                    this, R.string.password_confirm_password_not_match));
            return;
        }

        // TODO 注释
        showLoadingDialog();
        reset(password, userType);

    }

    private void reset(String password, String userType) {
        // TODO Auto-generated method stub
        final String requestTag = "Reset";

        new HttpManager().post(MedicineApplication.app, Constants.RESET_PASSWD,
                ResetPassword.class, Params.getResetPasswordParams(phone,
                        userType, smsid, ranCode, mPasswordEdit.getText()
                                .toString().trim()), this, false, 1);

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSuccess(Result response) {
        // TODO Auto-generated method stub
        if (null != response && 1 == response.getResultCode()) {
            if (response instanceof ResetPassword) {
                //				Intent intent = new Intent(ResetPasswdActivity.this,
                //						LoginActivity.class);
                //				startActivity(intent);
                //				finish();
                loginRequest(phone, mPasswordEdit.getText().toString().trim());
                ToastUtils.showToast("密码重置成功");
            }

            if (response instanceof LoginRegisterResult) {
                if (response.getResultCode() != 1) {
                    ToastUtils.showToast(response.getResultMsg());
                    return;
                }

                LoginRegisterResult logins = (LoginRegisterResult) response;
                if (null==logins.data||null==logins.data.getUser()){
                    ToastUtils.showToast(getString(R.string.data_user_err));
                    return;
                }
                SharedPreferenceUtil.putString("session",
                        logins.data.getAccess_token());
                if(logins.data.getUser()!=null){
                    SharedPreferenceUtil.putString("id", logins.data.getUser().getUserId());
                    SharedPreferenceUtil.putString("telephone", logins.data.getUser().getTelephone());
                    SharedPreferenceUtil.putString("username", logins.data.getUser().getName());
                    SharedPreferenceUtil.putString("usertype",Constants.USERTYPE);
                    long expires_in = logins.data.getExpires_in() * 1000L + System.currentTimeMillis();
                    SharedPreferenceUtil.putLong( "expires_in", expires_in);

                    String tel =
                            SharedPreferenceUtil.getString("telephone", "");
                    String url =  logins.data.getUser().headPicFileName;
                    SharedPreferenceUtil.putString(logins.data.getUser().getUserId() + "head_url", url);

                    User user = logins.data.getUser();
                    user.setToken(logins.data.access_token);
                    user.headUrl = url;
                    MedicineProvider.changeDatabase(logins.data.getUser().getUserId());
                    MedicineApplication
                            .getApplication()
                            .getContentResolver()
                            .insert(MedicineURIField.DOWNLOAD_TASK_URI,
                                    TableUser.buildContentValues(user));
                }
                closeLoadingDialog();
                LoginUtils.loginVerifyTwice(this,true);
            }


        } else {
            closeLoadingDialog();
            ToastUtils.showToast(response.getResultMsg());
        }
    }

    private void loginRequest(String phoneNum, String password) {
        new HttpManager().post(Constants.LOGIN + "", LoginRegisterResult.class,
                Params.getLoginParams(phoneNum, password, Constants.USERTYPE), this,
                false, 1);
    }

    @Override
    public void onSuccess(ArrayList response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        // TODO Auto-generated method stub
        ToastUtils.showToast(errorMsg);
    }

}
