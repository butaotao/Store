package com.dachen.dgroupdoctorcompany.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.Void;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 手机号添加
 *
 */
public class AddTelActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = AddTelActivity.class.getSimpleName();


    private EditText mPhoneNumEdit, mAuthCodeEdit;
    private Button commit_btn,mSendAgainBtn;
    private TextView get_call_code;
    public static final int SMSCODE = 0; //短信验证
    public static final int VOICECODE = 1; //语音验证
    private boolean clicked = false;//判断是否点击了发送短信
    private static boolean TELEPHONE_AUTH = true;//是否要验证真实手机号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tel);
        setTitle("修改手机号");
        init();
    }

    private void init() {
        mPhoneNumEdit = getViewById(R.id.phone_numer_edit);
        mAuthCodeEdit = getViewById(R.id.auth_code_edit);
        mSendAgainBtn = getViewById(R.id.send_again_btn);
        commit_btn = getViewById(R.id.commit_btn);
        get_call_code = getViewById(R.id.get_call_code);
        mSendAgainBtn.setOnClickListener(this);
        get_call_code.setOnClickListener(this);
        commit_btn.setBackgroundResource(R.drawable.btn_blue_all_9ddcff);
        mAuthCodeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)){
                    if (!TextUtils.isEmpty(mPhoneNumEdit.getText())){
                        commit_btn.setBackgroundResource(R.drawable.btn_blue_all_3cbaff);
                        return;
                    }
                }
                commit_btn.setBackgroundResource(R.drawable.btn_blue_all_9ddcff);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPhoneNumEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)){
                    if (!TextUtils.isEmpty(mAuthCodeEdit.getText())){
                        commit_btn.setBackgroundResource(R.drawable.btn_blue_all_3cbaff);
                        return;
                    }
                }
                commit_btn.setBackgroundResource(R.drawable.btn_blue_all_9ddcff);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
        interfaces.put("interface1", Constants.VERIFY_TELEPHONE);

        new HttpManager().post(this, interfaces,
                Void.class, params, new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result == null) {
                            clicked = false;
                            ToastUtils.showToast(AddTelActivity.this, getResources().getString(R.string.data_exception));
                            return;
                        }
                        if (result.getResultCode() == 1) {// 手机号没有被注册,那么就发送验证码

                            mSendAgainBtn.setEnabled(false);
                            mReckonHandler.sendEmptyMessage(0x1);

                            if (TELEPHONE_AUTH) {
                                if (smsOrVoice == SMSCODE)
                                    sendAuthcode(phoneNumber);
                                else if (smsOrVoice == VOICECODE) {
                                    getVoiceCode(phoneNumber);
                                }
                            }

                        } else if (result.getResultCode() == 0) {// 手机号已经被注册
                            clicked = false;
                            ToastUtils.showToast(AddTelActivity.this, result.getResultMsg());
                        } else if(result.getResultCode() == 100){//手机号未从后台导入
//                            clicked = false;
//                            String msg = result.getResultMsg();
//                            if(TextUtils.isEmpty(msg)){
//                                msg = "注册错误";
//                            }
//                            CustomDialog.Builder builder = new CustomDialog.Builder(AddTelActivity.this,new CustomDialog.CustomClickEvent(){
//
//
//                                @Override
//                                public void onClick(CustomDialog customDialog) {
//                                    customDialog.dismiss();
//                                }
//
//                                @Override
//                                public void onDismiss(CustomDialog customDialog) {
//                                    customDialog.dismiss();
//                                }
//                            }).setTitle("提示").setMessage(msg).setPositive("知道了");
//                            builder.create().show();
                        }else {// 错误
                            clicked = false;
                            ToastUtils.showToast(AddTelActivity.this, result.getResultMsg());
                        }
                    }

                    @Override
                    public void onSuccess(ArrayList<Result> result) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {
                        clicked = false;
                        ToastUtils.showToast(AddTelActivity.this, errorMsg);
                    }
                }, 3);
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
                Void.class, params, new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result != null && result.getResultCode() == 1) {// 发送成功
                            ToastUtils.showToast(AddTelActivity.this, "验证码已发送，请注意查收");
                            //语音验证码不可点击，直到60秒后
                            get_call_code.setTextColor(getResources().getColor(R.color.gray_time_text));
                            get_call_code.setClickable(false);
                        } else {
                            mReckonHandler.removeCallbacksAndMessages(null);
                            mReckonHandler.sendEmptyMessage(0x2);
                            ToastUtils.showToast(AddTelActivity.this, result.getResultMsg());
                        }
                        clicked = false;
                    }

                    @Override
                    public void onSuccess(ArrayList<Result> result) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {
                        ToastUtils.showToast(AddTelActivity.this, getResources().getString(R.string.get_auth_code_failed));
                        mReckonHandler.removeCallbacksAndMessages(null);
                        mReckonHandler.sendEmptyMessage(0x2);
                        clicked = false;
                    }
                }, 3);
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
                            ToastUtils.showToast(AddTelActivity.this, "语音验证码已发送，请注意接听电话");
                            //语音验证码不可点击，直到120秒后
                            get_call_code.setTextColor(getResources().getColor(R.color.gray_time_text));
                            get_call_code.setClickable(false);
                        } else {
                            mReckonHandler.removeCallbacksAndMessages(null);
                            mReckonHandler.sendEmptyMessage(0x2);
                            ToastUtils.showToast(AddTelActivity.this, result.getResultMsg());
                        }
                        clicked = false;
                    }

                    @Override
                    public void onSuccess(ArrayList<Result> response) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {
                        ToastUtils.showToast(AddTelActivity.this, "获取语音验证码失败");
                        mReckonHandler.removeCallbacksAndMessages(null);
                        mReckonHandler.sendEmptyMessage(0x2);
                        clicked = false;
                    }
                }, false, 3);
    }

    private int reckonTime = 60;
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
            } else if (msg.what == 0x2) {// 60秒结束
                mSendAgainBtn.setText(R.string.again);
                mSendAgainBtn.setTextColor(Color.parseColor("#30b2cc"));
                mSendAgainBtn.setEnabled(true);
                reckonTime = 60;
            }
        }
    };

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.send_again_btn:
                sendAgain(SMSCODE);
                break;
            case R.id.get_call_code:
                sendAgain(VOICECODE);
                break;
            case R.id.commit_btn:
                //提交，成功后保存手机号，跳到登录页
//                SharedPreferenceUtil.putString(this,"telephone", mPhoneNumEdit.getText().toString().trim());
//                Intent intent = new Intent(this, LoginActivity.class);
//                startActivity(intent);
                break;
        }
    }




}
