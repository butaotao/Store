package com.dachen.dgroupdoctorcompany.adapter;

/**
 * Created by Burt on 2016/2/20.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.MainActivity;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.*;
import com.dachen.dgroupdoctorcompany.entity.Void;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.SystemUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;

/**
 * 注册验证手机号页面
 *
 * @author ZENG
 *
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener,HttpManager.OnHttpListener {
    public static final String EXTRA_AUTH_CODE = "auth_code";
    public static final String EXTRA_PHONE_NUMBER = "phone_number";
    public static final String EXTRA_PASSWORD = "password";
    public static final String EXTRA_DOCTOR_NUM = "doctor_num";
    public static final int SMSCODE = 0; //短信验证
    public static final int VOICECODE = 1; //语音验证
    protected ProgressDialog mProgressDialog;
    protected Intent intent;
    private boolean clicked = false;//判断是否点击了发送短信
    private static boolean TELEPHONE_AUTH = true;//是否要验证真实手机号

    @Nullable
    @Bind(R.id.phone_numer_edit)
    EditText mPhoneNumEdit;
    @Nullable
    @Bind(R.id.auth_code_edit)
    EditText mAuthCodeEdit;
    @Nullable
    @Bind(R.id.send_again_btn)
    Button mSendAgainBtn;
    @Nullable
    @Bind(R.id.password_edit)
    EditText mPasswordEdit;
    @Nullable
    @Bind(R.id.next_step_btn)
    Button mNextStepBtn;
    @Nullable
    @Bind(R.id.get_call_code)
    TextView get_call_code;
    @Nullable
    @Bind(R.id.back_step_btn)
    Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initViews();
    }

     void initViews() {
        mSendAgainBtn.setText(R.string.getcode);
    }

    @Nullable
    @OnClick(R.id.send_again_btn)
    void onSendAgainBtnClicked() {
        sendAgain(SMSCODE);
    }

    @Nullable
    @OnClick(R.id.next_step_btn)
    void onNextStepBtnClicked() {
        nextStep();
    }

    @Nullable
    @OnClick(R.id.back_step_btn)
    void onBackStepBtnClicked() {
        doBack();
    }

    @Nullable
    @OnClick(R.id.get_call_code)
    void onGetCallCodeClicked() {
        sendAgain(VOICECODE);
    }

    private void sendAgain(int smsOrVoice)
    {
        String phoneNumber = mPhoneNumEdit.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastUtils.showToast(this, "请输入正确的手机号");
            return;
        }
        //验证号码真实，去掉
//		if (!StringUtils.isMobileNumber(phoneNumber)) {
//			mPhoneNumEdit.requestFocus();
//			mPhoneNumEdit.setError(StringUtils.editTextHtmlErrorTip(RegisterActivity.this, R.string.phone_number_format_error));
//			return;
//		}
        if(!clicked){
            clicked = true;
            verifyTelephone(phoneNumber,smsOrVoice);
        }
    }

    /* 验证该号码有没有注册 */
    private void verifyTelephone(final String phoneNumber,final int smsOrVoice) {
        final String userType = Constants.USER_TYPE;

        Map<String, String> params = new HashMap<String, String>();
        params.put("telephone", phoneNumber);
        params.put("userType", userType);
        final String requestTag = "verifyTelephone";
        HashMap<String, String> interfaces = new HashMap<String, String>();
        interfaces.put("interface1",Constants.VERIFY_TELEPHONE);

        new HttpManager().post(this, interfaces,
                Void.class, params, new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result == null) {
                            clicked = false;
                            ToastUtils.showToast(RegisterActivity.this, getResources().getString(R.string.data_exception));
                            return;
                        }
                        if (result.getResultCode() == 1) {// 手机号没有被注册,那么就发送验证码

                            mSendAgainBtn.setEnabled(false);
                            mReckonHandler.sendEmptyMessage(0x1);

                            if(TELEPHONE_AUTH)
                            {
                                if(smsOrVoice == SMSCODE)
                                    sendAuthcode(phoneNumber);
                                else if(smsOrVoice == VOICECODE){
                                    getVoiceCode(phoneNumber);
                                }
                            }

                        } else if (result.getResultCode() == 0) {// 手机号已经被注册
                            clicked = false;
                            if (!TextUtils.isEmpty(result.getResultMsg())) {
                                ToastUtils.showToast(RegisterActivity.this, result.getResultMsg());
                            } else {
                                ToastUtils.showToast(RegisterActivity.this, getResources().getString(R.string.telphone_already_rigister));
                            }
                        } else {// 错误
                            clicked = false;
                            if (!TextUtils.isEmpty(result.getResultMsg())) {
                                ToastUtils.showToast(RegisterActivity.this, result.getResultMsg());
                            } else {
                                ToastUtils.showToast(RegisterActivity.this, getResources().getString(R.string.data_exception));
                            }
                        }
                    }

                    @Override
                    public void onSuccess(ArrayList<Result> result) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {
                        clicked = false;
                    }
                }, 1);

    }

    /**
     * 请求验证码
     *
     * @return
     */
    private void sendAuthcode(final String phoneNumber) {
        final String templateId = "25118";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("telephone", phoneNumber);
        params.put("templateId", templateId);// 短信模板。
        HashMap<String, String> interfaces = new HashMap<String, String>();
        interfaces.put("interface1", Constants.SEND_AUTH_CODE);
        new HttpManager().post(this, interfaces,
                com.dachen.dgroupdoctorcompany.entity.Void.class, params, new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result != null && result.getResultCode() == 1) {// 发送成功
                            ToastUtils.showToast(RegisterActivity.this, "验证码已发送，请注意查收");
                            //语音验证码不可点击，直到120秒后
                            get_call_code.setTextColor(getResources().getColor(R.color.gray_time_text));
                            get_call_code.setClickable(false);
                        } else {
                            mReckonHandler.removeCallbacksAndMessages(null);
                            mReckonHandler.sendEmptyMessage(0x2);
                            ToastUtils.showToast(RegisterActivity.this, result.getResultMsg());
                        }
                        clicked = false;
                    }

                    @Override
                    public void onSuccess(ArrayList<Result> result) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {
                        ToastUtils.showToast(RegisterActivity.this, getResources().getString(R.string.get_auth_code_failed));
                        mReckonHandler.removeCallbacksAndMessages(null);
                        mReckonHandler.sendEmptyMessage(0x2);
                        clicked = false;
                    }
                }, 1);
    }
    /**
     * 获取4位语音验证码
     *
     * @return
     */
    private void getVoiceCode(final String phoneNumber) {
        final String templateId = "25118";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("telephone", phoneNumber);
        params.put("templateId", templateId);// 短信模板。
        /*HashMap<String, String> interfaces = new HashMap<String, String>();
        interfaces.put("interface1","user/preResetPasswordVoiceCode");*/
        new HttpManager().post(this, "/sms/randcode/getVoiceCode",
                Void.class, params, new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result != null && result.getResultCode() == 1) {// 发送成功
                            ToastUtils.showToast(RegisterActivity.this, "语音验证码已发送，请注意接听电话");
                            //语音验证码不可点击，直到120秒后
                            get_call_code.setTextColor(getResources().getColor(R.color.gray_time_text));
                            get_call_code.setClickable(false);
                        } else {
                            mReckonHandler.removeCallbacksAndMessages(null);
                            mReckonHandler.sendEmptyMessage(0x2);
                            ToastUtils.showToast(RegisterActivity.this, result.getResultMsg());
                        }
                        clicked = false;
                    }

                    @Override
                    public void onSuccess(ArrayList<Result> response) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {
                        ToastUtils.showToast(RegisterActivity.this, "获取语音验证码失败");
                        mReckonHandler.removeCallbacksAndMessages(null);
                        mReckonHandler.sendEmptyMessage(0x2);
                        clicked = false;
                    }
                },false, 1);
    }

    private int reckonTime = 120;
    private Handler mReckonHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0x1) {
                mSendAgainBtn.setText(reckonTime + "s");
                mSendAgainBtn.setTextColor(getResources().getColor(R.color.common_text_grey));
                reckonTime--;
                if (reckonTime < 0) {
                    mReckonHandler.sendEmptyMessage(0x2);
                } else {
                    mReckonHandler.sendEmptyMessageDelayed(0x1, 1000);
                }
            } else if (msg.what == 0x2) {// 120秒结束
                mSendAgainBtn.setText(R.string.again);
                mSendAgainBtn.setEnabled(true);
                get_call_code.setTextColor(getResources().getColor(R.color.blue_496fb7));
                get_call_code.setClickable(true);
                reckonTime = 120;
            }
        }
    };

    private void nextStep() {
        final String phoneNumber = mPhoneNumEdit.getText().toString().trim();
        final String password = mPasswordEdit.getText().toString().trim();
        final String userType = Constants.USER_TYPE;
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastUtils.showToast(this, getResources().getString(R.string.phone_number_input));
            return;
        }

        if(TELEPHONE_AUTH)
        {
            //验证号码真实，去掉
//			if (!StringUtils.isMobileNumber(phoneNumber)) {
//				mPhoneNumEdit.requestFocus();
//				mPhoneNumEdit.setError(StringUtils.editTextHtmlErrorTip(this, R.string.phone_number_format_error));
//				return;
//			}
        }

        String authCode = mAuthCodeEdit.getText().toString().trim();
        if (TextUtils.isEmpty(authCode)) {
            mAuthCodeEdit.requestFocus();
            ToastUtils.showToast(this, getResources().getString(R.string.auth_code_input));
//			 mAuthCodeEdit.setError(StringUtils.editTextHtmlErrorTip(this, R.string.auth_code_input));
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            mPasswordEdit.requestFocus();
            ToastUtils.showToast(this, getResources().getString(R.string.password_empty_error));
            return;
        }

        if(TELEPHONE_AUTH)
        {
            verifyCode(phoneNumber,authCode,password,userType);
//			register(phoneNumber,password,userType);
        }
        else {
//			verifyCode(phoneNumber,authCode,password,userType);
            register(phoneNumber,password,userType);
        }
    }

    /**
     * 是否请求了验证码
     *
     * @return
     */
    private void verifyCode(final String phoneNumber,final String randcode,final String password,final String userType) {
        final String templateId = "25118";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("telephone", phoneNumber);
        params.put("randcode", randcode);
        params.put("templateId", templateId);// 短信模板。
        HashMap<String, String> interfaces = new HashMap<String, String>();
        interfaces.put("interface1", Constants.VERIFY_CODE);

        new HttpManager().post(this, interfaces,
                Void.class, params, new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result != null && result.getResultCode() == 1) {// 发送成功
                            register(phoneNumber, password, userType);
                        } else {
                            ToastUtils.showToast(RegisterActivity.this, getResources().getString(R.string.auth_code_error));
                        }
                    }

                    @Override
                    public void onSuccess(ArrayList<Result> response) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {
                        ToastUtils.showToast(RegisterActivity.this, getResources().getString(R.string.auth_code_error));
                    }
                }, 1);
    }

    private void register(final String phoneNumber,final String password,final String userType) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("telephone",phoneNumber);
        params.put("password", password);
        params.put("userType", userType);
        params.put("name",phoneNumber);
        HashMap<String, String> interfaces = new HashMap<String, String>();
        interfaces.put("interface1", Constants.USER_REGISTER);
        new HttpManager().post(this, interfaces,
                LoginRegisterResult.class, params, new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result == null) {
                            ToastUtils.showToast(RegisterActivity.this, getResources().getString(R.string.register_error));
                            return;
                        }
                        if (result.getResultCode() == Result.CODE_SUCCESS) {// 注册成功
                           // boolean success = LoginHelper.setLoginUser(RegisterActivity.this, phoneNumber, password, result);
                            LoginRegisterResult logins = (LoginRegisterResult) result;
                            SharedPreferenceUtil.putString(RegisterActivity.this,"session",
                                    logins.data.getAccess_token());
                            SharedPreferenceUtil.putString(RegisterActivity.this,"id", logins.data.getUser().getUserId());
                            SharedPreferenceUtil.putString(RegisterActivity.this,"telephone", logins.data.getUser().getTelephone());
                            SharedPreferenceUtil.putString(RegisterActivity.this,"username", logins.data.getUser().getName());
                            SharedPreferenceUtil.putString(RegisterActivity.this,"usertype",Constants.USER_TYPE);
                            long expires_in = logins.data.getExpires_in() * 1000L + System.currentTimeMillis();
                            SharedPreferenceUtil.putLong(RegisterActivity.this,"expires_in",expires_in);

                            if (null!=logins.data.getUser().getUserId()&&null!=logins.data.getUser().getTelephone()
                                    &&null!=logins.data.getUser().getName()) {
                                closeLoadingDialog();
                                autoLogin(RegisterActivity.this);
                            } else {// 失败
                                closeLoadingDialog();
                                if (TextUtils.isEmpty(result.getResultMsg())) {
                                    ToastUtils.showToast(RegisterActivity.this, getResources().getString(R.string.register_error));
                                } else {
                                    ToastUtils.showToast(RegisterActivity.this, result.getResultMsg());
                                }
                            }
                        } else {// 失败
                            closeLoadingDialog();
                            if (TextUtils.isEmpty(result.getResultMsg())) {
                                ToastUtils.showToast(RegisterActivity.this, getResources().getString(R.string.register_error));
                            } else {
                                ToastUtils.showToast(RegisterActivity.this, result.getResultMsg());
                            }
                        }
                    }

                    @Override
                    public void onSuccess(ArrayList<Result> response) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {
                        ToastUtils.showToast(RegisterActivity.this, getResources().getString(R.string.auth_code_error));
                    }
                }, 1);

    }

    private void autoLogin(Context context) {
        intent = new Intent();
        final String userId = SharedPreferenceUtil.getString(context,"id", "");
        final String access_token = SharedPreferenceUtil.getString(context,"session","");
        if (TextUtils.isEmpty(userId)) {
            return;
        }



        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userId", userId);
        params.put("serial", SystemUtils.getDeviceId(this));
        params.put("access_token", access_token);
        HashMap<String, String> interfaces = new HashMap<String, String>();

        interfaces.put("interface1",Constants.USER_LORGIN_AUTO);
        new HttpManager().post(this, interfaces,
                LoginRegisterResult.class, params, new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result == null) {
                            closeLoadingDialog();
                            return;
                        }  LoginRegisterResult logins = (LoginRegisterResult) result;
                        boolean success = false;
                        if (result.getResultCode() == Result.CODE_SUCCESS) {
                            if (null!=logins.data.getUser().getUserId()&&null!=logins.data.getUser().getTelephone()
                                    &&null!=logins.data.getUser().getName())
                                success = true;// 设置登陆用户信息
                        }


                        if (success) {// 登陆成功
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);


                            startActivity(intent);
                            finish();
                        } else {// 登录失败
                            String message = TextUtils.isEmpty(result.getResultMsg()) ? getString(R.string.login_failed) : result.getResultMsg();
                            ToastUtils.showToast(RegisterActivity.this, message);
                        }
                        closeLoadingDialog();
                    }

                    @Override
                    public void onSuccess(ArrayList<Result> response) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {
                        closeLoadingDialog();
                    }
                }, 1);

    }

    @Override
    public void onBackPressed() {
        doBack();
    }

    private void doBack() {
        finish();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSuccess(Result result) {
         if(result instanceof TelePhoneVerifyData){
            TelePhoneVerifyData results = (TelePhoneVerifyData)(result);




             if (result != null && result.getResultCode() == 1) {// 发送成功
                 ToastUtils.showToast(RegisterActivity.this, "语音验证码已发送，请注意接听电话");
                 //语音验证码不可点击，直到120秒后
                 get_call_code.setTextColor(getResources().getColor(R.color.gray_time_text));
                 get_call_code.setClickable(false);
             } else {
                 mReckonHandler.removeCallbacksAndMessages(null);
                 mReckonHandler.sendEmptyMessage(0x2);
                 ToastUtils.showToast(RegisterActivity.this, result.getResultMsg());
             }
             clicked = false;
        }
    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {

    }
}
