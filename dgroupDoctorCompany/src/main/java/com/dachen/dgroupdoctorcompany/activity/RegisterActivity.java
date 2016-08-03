package com.dachen.dgroupdoctorcompany.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.base.UserLoginc;
import com.dachen.dgroupdoctorcompany.entity.Void;
import com.dachen.dgroupdoctorcompany.utils.UserUtils;
import com.dachen.dgroupdoctorcompany.views.CustomDialog;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.SystemUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.LoginRegisterResult;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册验证手机号页面
 * 
 * @author ZENG
 * 
 */
public class RegisterActivity extends BaseActivity implements OnClickListener,HttpManager.OnHttpListener{
	public static final String EXTRA_AUTH_CODE = "auth_code";
	public static final String EXTRA_PHONE_NUMBER = "phone_number";
	public static final String EXTRA_PASSWORD = "password";
	public static final String EXTRA_USERID = "user_id";
	public static final int SMSCODE = 0; //短信验证
	public static final int VOICECODE = 1; //语音验证
	protected ProgressDialog mProgressDialog;
	protected Intent intent;
	private final String userType = Constants.USER_TYPE;
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
	@Bind(R.id.confirm_password_edit)
	EditText mConfirmPasswordEdit;
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

	private void initViews() {
		mSendAgainBtn.setText(R.string.get_auth_code);
		setTitle("注册");
		mNextStepBtn.setBackgroundResource(R.drawable.btn_blue_all_9ddcff);
		mNextStepBtn.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!TextUtils.isEmpty(s)){
					if (!TextUtils.isEmpty(mAuthCodeEdit.getText())){
						mNextStepBtn.setBackgroundResource(R.drawable.btn_blue_all_3cbaff);
						return;
					}
				}
				mNextStepBtn.setBackgroundResource(R.drawable.btn_blue_all_9ddcff);
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	@Nullable
	@OnClick(R.id.send_again_btn)
	void onSendAgainBtnClicked() {
		sendAgain(SMSCODE);
	}
	
	@Nullable
	@OnClick(R.id.next_step_btn)
	void onNextStepBtnClicked() {
		nextStep2();
	}
	
	@Nullable
	@OnClick(R.id.rl_back)
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
		interfaces.put("interface1", Constants.VERIFY_TELEPHONE);

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

							if (TELEPHONE_AUTH) {
								if (smsOrVoice == SMSCODE)
									sendAuthcode(phoneNumber);
								else if (smsOrVoice == VOICECODE) {
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
						} else if(result.getResultCode() == 100){//手机号未从后台导入
							clicked = false;
							String msg = result.getResultMsg();
							if(TextUtils.isEmpty(msg)){
								msg = "注册错误";
							}
							CustomDialog.Builder builder = new CustomDialog.Builder(RegisterActivity.this,new CustomDialog.CustomClickEvent(){


								@Override
								public void onClick(CustomDialog customDialog) {
									customDialog.dismiss();
								}

								@Override
								public void onDismiss(CustomDialog customDialog) {
									customDialog.dismiss();
								}
							}).setTitle("提示").setMessage(msg).setPositive("知道了");
							builder.create().show();
						}else {// 错误
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
				}, false, 3);
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
				mSendAgainBtn.setTextColor(Color.parseColor("#30b2cc"));
				mSendAgainBtn.setEnabled(true);
				reckonTime = 120;
			}
		}
	};

	private void nextStep() {
		final String phoneNumber = mPhoneNumEdit.getText().toString().trim();
		final String password = mPasswordEdit.getText().toString().trim();
		String confirmPassword = mConfirmPasswordEdit.getText().toString().trim();
		if (TextUtils.isEmpty(phoneNumber)) {
			ToastUtils.showToast(this, "请输入正确的手机号");
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
//			mPasswordEdit.setError(StringUtils.editTextHtmlErrorTip(this, R.string.password_empty_error));
			return;
		}
		if (TextUtils.isEmpty(confirmPassword) || confirmPassword.length() < 6 || confirmPassword.length() > 18) {
			mConfirmPasswordEdit.requestFocus();
			ToastUtils.showToast(this, getResources().getString(R.string.confirm_password_empty_error));
//			mConfirmPasswordEdit.setError(StringUtils.editTextHtmlErrorTip(this, R.string.confirm_password_empty_error));
			return;
		}
		if (!confirmPassword.equals(password)) {
			mConfirmPasswordEdit.requestFocus();
			ToastUtils.showToast(this, getResources().getString(R.string.password_confirm_password_not_match));
//			mConfirmPasswordEdit.setError(StringUtils.editTextHtmlErrorTip(this, R.string.password_confirm_password_not_match));
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
				}, 3);
	}

	private void register(final String phoneNumber,final String password,final String userType) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("telephone",phoneNumber);
		params.put("password", password);
		params.put("userType", userType);
		params.put("name", phoneNumber);
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
							LoginRegisterResult logins = (LoginRegisterResult) result;

							UserLoginc.setUserInfo(logins, RegisterActivity.this);

							if (null != logins.data.getUser().getUserId() && null != logins.data.getUser().getTelephone()
									&& null != logins.data.getUser().getName()) {
								closeLoadingDialog();
								autoLogin();
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
				}, 3);
	}

	/**
	 * 注册后自动登录
	 */
	private void autoLogin() {
		intent = new Intent();
		final String userId = SharedPreferenceUtil.getString(RegisterActivity.this,"id", "");
		final String access_token = SharedPreferenceUtil.getString(RegisterActivity.this,"session", "");
		if (TextUtils.isEmpty(userId)) {
			return;
		}
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("serial", SystemUtils.getDeviceId(this));
		params.put("access_token", access_token);
		HashMap<String, String> interfaces = new HashMap<String, String>();

		interfaces.put("interface1", Constants.USER_LORGIN_AUTO);
		new HttpManager().post(this, interfaces,
				LoginRegisterResult.class, params, new HttpManager.OnHttpListener<Result>() {
					@Override
					public void onSuccess(Result result) {
						if (result == null) {
							closeLoadingDialog();
							return;
						}
						LoginRegisterResult logins = (LoginRegisterResult) result;
						boolean success = false;
						if (result.getResultCode() == Result.CODE_SUCCESS) {
							if (null != logins.data.getUser().getUserId() && null != logins.data.getUser().getTelephone()
									&& null != logins.data.getUser().getName())
								success = true;// 设置登陆用户信息
						}
						UserLoginc.setUserInfo(logins, RegisterActivity.this);

						if (success) {// 登陆成功
							showLoadingDialog();
							UserUtils.logingetUserType(RegisterActivity.this);
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
				}, 3);

	}
	@Override
	public void onBackPressed() {
		doBack();
	}

	private void doBack() {
		Intent intent = new Intent(this,LoginActivity.class);
		startActivity(intent);
		finish();
	}

	private void nextStep2() {
		final String phoneNumber = mPhoneNumEdit.getText().toString().trim();
		if (TextUtils.isEmpty(phoneNumber)) {
			ToastUtils.showToast(this, "请输入手机号");
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
			ToastUtils.showToast(RegisterActivity.this, getResources().getString(R.string.auth_code_input));
//          mAuthCodeEdit.setError(StringUtils.editTextHtmlErrorTip(this, R.string.auth_code_input));
			return;
		}

		if(TELEPHONE_AUTH)
		{
			verifyCode2(phoneNumber,authCode);
//			register(phoneNumber,password,userType);
		}
		else {
//			verifyCode(phoneNumber,authCode,password,userType);
			register2(phoneNumber);
		}

	}

	/**
	 * 是否请求了验证码
	 *
	 * @return
	 */
	private void verifyCode2(final String phoneNumber,final String randcode) {


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
							register2(phoneNumber);
							ToastUtils.showToast(RegisterActivity.this, getResources().getString(R.string.auth_code_right));
						} else {
							ToastUtils.showToast(RegisterActivity.this, "请输入正确的验证码");
						}
					}

					@Override
					public void onSuccess(ArrayList<Result> response) {

					}

					@Override
					public void onFailure(Exception e, String errorMsg, int s) {
						ToastUtils.showToast(RegisterActivity.this, getResources().getString(R.string.auth_code_error));
					}
				}, 3);
	}
	
	private void register2(final String phoneNumber) {
		Intent intent = new Intent(RegisterActivity.this, RegisterStep2Activity.class);
		intent.putExtra("phoneNumber", phoneNumber);
		startActivity(intent);
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccess(Result entity) {
		closeLoadingDialog();
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(ArrayList response) {

	}

	@Override
	public void onFailure(Exception e, String errorMsg, int s) {

	}

}
