package com.dachen.dgroupdoctorcompany.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;
import com.baoyz.actionsheet.ActionSheet.ActionSheetListener;
import com.dachen.common.utils.Md5Util;
import com.dachen.common.utils.QiNiuUtils;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.CompanyApplication;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.base.UserLoginc;
import com.dachen.dgroupdoctorcompany.utils.UserUtils;
import com.dachen.imsdk.ImSdk;
import com.dachen.medicine.common.utils.MActivityManager;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.ContextConfig;
import com.dachen.medicine.config.ContextConfig.EnvironmentType;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.LoginRegisterResult;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.NetConfig;
import com.dachen.medicine.net.Params;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登陆界面
 *
 * @author ZENG
 *
 */
public class LoginActivity extends BaseActivity implements OnClickListener,
        ActionSheetListener, OnHttpListener {

    @Nullable
    @Bind(R.id.tv_title)
    TextView tv_login_title;

    @Nullable
    @Bind(R.id.phone_numer_edit)
    EditText mPhoneNumberEdit;
    @Nullable
    @Bind(R.id.password_edit)
    EditText mPasswordEdit;
    @Nullable
    @Bind(R.id.avatar_img)
    ImageView avatar_img;
    @Nullable
    @Bind(R.id.login_btn)
    Button login_btn;
    //DialogBank dialog;
    protected ProgressDialog mProgressDialog;
    private int clickTitle = 0;
    String phoneNum;
    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setBackInVisiable();
        ButterKnife.bind(this);

        initViews();
        setTitle("登录");
        mPhoneNumberEdit.addTextChangedListener(watcherPhoneNum);
        mPasswordEdit.addTextChangedListener(watcherPassWord);
        login_btn.setBackgroundResource(R.drawable.btn_blue_all_9ddcff);
        if (!TextUtils.isEmpty(mPhoneNumberEdit.getText())){
            login_btn.setBackgroundResource(R.drawable.btn_blue_all_3cbaff);
        }
    }
    TextWatcher watcherPassWord = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
          //  if (!TextUtils.isEmpty(s)){
                if (!TextUtils.isEmpty(mPhoneNumberEdit.getText())){
                    login_btn.setBackgroundResource(R.drawable.btn_blue_all_3cbaff);
                    return;
                }
           // }
            login_btn.setBackgroundResource(R.drawable.btn_blue_all_9ddcff);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher watcherPhoneNum = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
         /*   if (!TextUtils.isEmpty(s)){
                if (!TextUtils.isEmpty(mPasswordEdit.getText())){
                    login_btn.setBackgroundResource(R.drawable.btn_blue_all_3cbaff);
                    return;
                }
            }*/
           // login_btn.setBackgroundResource(R.drawable.btn_blue_all_9ddcff);
            if (!TextUtils.isEmpty(s)){
                    login_btn.setBackgroundResource(R.drawable.btn_blue_all_3cbaff);
                    return;
            }
            login_btn.setBackgroundResource(R.drawable.btn_blue_all_3cbaff);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    void initViews() {
        findViewById(R.id.rl_back).setVisibility(View.GONE);
        String tel =
                SharedPreferenceUtil.getString(this,"telephone", "");
        if(!TextUtils.isEmpty(tel)) {
            mPhoneNumberEdit.setText(tel);
            mPasswordEdit.setFocusable(true);
            mPasswordEdit.setFocusableInTouchMode(true);
            mPasswordEdit.requestFocus();
        }
    }

    @Nullable
    @OnClick(R.id.forget_password_btn)
    void onForgetPasswordBtnClicked() {
        Intent intent = new Intent(this, PreResetPasswdActivity.class);
        startActivity(intent);
//        ToastUtils.showToast(this, "忘记密码");
    }


    @Nullable
    @OnClick(R.id.login_btn)
    void onLoginBtnClicked() {
        login();
    }

    @Nullable
    @OnClick(R.id.register_btn)
    void onRegister(){
       /* Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);*/
        Intent intent = new Intent(this,HowRegisterActivity.class);
        startActivity(intent);
    }

    @Nullable
    @OnClick(R.id.rl_titlebar)
    void onLoginTitleClicked() {
         if(clickTitle>3)
        {
        clickTitle = 0;
        ActionSheet.createBuilder(this,  getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("内网", "外网(阿里云环境)", "生产环境","生产测试环境", "屈军利")
                .setCancelableOnTouchOutside(true).setListener(this).show();
         }
         else {
          clickTitle++;
         }
    }
    //
    private void login() {
        if (!verifyPhoneIfNeed()) {
            return;
        }
        showLoadingDialog();
        loginRequest(phoneNum, password);
    }

    private void loginRequest(String phoneNum, String password) {
        final String userType = Constants.USER_TYPE;
        final String requestTag = "login";
        new HttpManager().post(this, Constants.LOGIN + "", LoginRegisterResult.class,
                Params.getLoginParams(phoneNum, password, userType, this), loginListener,
                false, 3);

    }
    public final boolean verifyPhoneIfNeed() {
        boolean verify = true;
        phoneNum = mPhoneNumberEdit.getText().toString().trim();
        password = mPasswordEdit.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum)) {
            ToastUtils.showToast(this,R.string.toast_verify_phone_null);
            verify = false;
            return verify;
        }

        if (phoneNum.length() != 11) {
            ToastUtils.showToast(this,R.string.toast_verify_phone_length);
            verify = false;
            return verify;
        }

        if (TextUtils.isEmpty(password)) {
         /*   ToastUtils.showToast(this,R.string.toast_verify_password_null);
            verify = false;
            return verify;*/
            password = "";
        }
       SharedPreferenceUtil.putString(this,"pss", "");
       SharedPreferenceUtil.putString(this, "ph", "");
        return verify;
    }

    /**
     * 登录获取好友列表
     */
    private static final String ACTION = "login_getFriend_result";



    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
        // TODO Auto-generated method stub

    }

    /**
     * 获得上次的ip
     *
     * @param index
     *            内网：0；外网：1
     */
    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        if (index == 0) {
            ToastUtils.showToast(this,"已切换到内网" + NetConfig.API_INNER_URL);
            ContextConfig.getInstance().setEnvironmentType(
                    EnvironmentType.INNER);
            SharedPreferenceUtil.putString(this, "netdes",  NetConfig.API_INNER_URL);
            QiNiuUtils.changeEnv(QiNiuUtils.DOMAIN_3_7);
            UserInfo.getInstance(this).setIp("2");
        } else if (index == 1) {
            ToastUtils.showToast(this,"已切换到外网" + NetConfig.API_OTER_URL);
            ContextConfig.getInstance().setEnvironmentType(
                    EnvironmentType.PUBLISH);
            SharedPreferenceUtil.putString(this, "netdes", NetConfig.API_OTER_URL);
            QiNiuUtils.changeEnv(QiNiuUtils.DOMAIN_ALI_YUN);
            UserInfo.getInstance(this).setIp("0");
        } else if (index == 2) {
            ToastUtils.showToast(this,"已切换到生产环境" + NetConfig.KANG_ZHE);
            ContextConfig.getInstance()
                    .setEnvironmentType(EnvironmentType.TEST);
            SharedPreferenceUtil.putString(this, "netdes", NetConfig.KANG_ZHE);
            UserInfo.getInstance(this).setIp("1");
            QiNiuUtils.changeEnv(QiNiuUtils.DOMAIN_KANGZHE);
        } else if(index ==3){//生产测试环境
            ToastUtils.showToast(this,"已切换到生产测试环境" + NetConfig.KANG_ZHE_TEST);
            ContextConfig.getInstance()
                    .setEnvironmentType(EnvironmentType.PUBLISHTEST);
            SharedPreferenceUtil.putString(this, "netdes", NetConfig.KANG_ZHE_TEST);
            UserInfo.getInstance(this).setIp("3");
            QiNiuUtils.changeEnv(QiNiuUtils.DOMAIN_KANGZHE_TEST);
        }else {
            ContextConfig.getInstance()
                    .setEnvironmentType(EnvironmentType.API_QUJUNLI_URL);
        }
        ImSdk.getInstance().changeIp(ContextConfig.IP);

    }
    OnHttpListener loginListener=new OnHttpListener() {
        @Override
        public void onSuccess(Result entity) {
            if (entity instanceof  LoginRegisterResult){
                if (entity.getResultCode() != 1) {
                    closeLoadingDialog();
                    if (entity.getResultCode() ==entity.CODE_ACCOUNT_REGETER){
                        Intent intent = new Intent(LoginActivity.this,PreResetPasswdActivity.class);
                        intent.putExtra("phonenum",mPhoneNumberEdit.getText().toString());
                        startActivity(intent);
                    }else {
                        ToastUtils.showToast(mThis,entity.getResultMsg());
                    }

                    return;
                }
                CompanyApplication.setInitContactList(2);
                LoginRegisterResult logins = (LoginRegisterResult) entity;
                UserLoginc.setUserInfo(logins, LoginActivity.this);
                UserUtils.logingetUserType(LoginActivity.this);
                SharedPreferenceUtil.putString(LoginActivity.this,"password", Md5Util.toMD5(password));
            }
        }
        @Override
        public void onSuccess(ArrayList response) {

        }

        @Override
        public void onFailure(Exception e, String errorMsg, int s) {
            ToastUtils.showToast(mThis,getString(R.string.connect_error));
            closeLoadingDialog();
            ImSdk.getInstance().logout();
        }
    };

    @Override
    public void onSuccess(Result entity) {
    }

    @Override
    public void onFailure(Exception e, String errorMsg,int s) {
        // TODO Auto-generated method stub
        ToastUtils.showToast(this, getString(R.string.connect_error));
        closeLoadingDialog();
    }


    @Override
    public void onSuccess(ArrayList response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MActivityManager.getInstance().finishAllActivity();
    }
}
