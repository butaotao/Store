package com.dachen.dgroupdoctorcompany.activity;

import android.app.Activity;
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

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.CheckPhoneOnSys;
import com.dachen.dgroupdoctorcompany.entity.Void;
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
	EditText phone_numer_edit;
	EditText auth_code_edit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		ButterKnife.bind(this);
		initViews();
	}

	private void initViews() {
		mSendAgainBtn.setText(R.string.get_auth_code);
		setTitle("修改手机号码");
		phone_numer_edit = (EditText) findViewById(R.id.phone_numer_edit);
		auth_code_edit = (EditText) findViewById(R.id.auth_code_edit);
		mNextStepBtn.setBackgroundResource(R.drawable.btn_blue_all_9ddcff);
		phone_numer_edit.setOnClickListener(this);
		if (!TextUtils.isEmpty(auth_code_edit.getText())) {
			if (!TextUtils.isEmpty(mAuthCodeEdit.getText())) {
				mNextStepBtn.setBackgroundResource(R.drawable.btn_blue_all_3cbaff);
				return;
			}
		}
		phone_numer_edit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (TextUtils.isEmpty(s)){
					mSendAgainBtn.setTextColor(getResources().getColor(R.color.gray_time_text));
				}else {
					mSendAgainBtn.setTextColor(getResources().getColor(R.color.blue_496fb7));
				}
				if (!TextUtils.isEmpty(s)) {
					if (!TextUtils.isEmpty(mAuthCodeEdit.getText())) {
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
		auth_code_edit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (TextUtils.isEmpty(phone_numer_edit.getText())){
					mSendAgainBtn.setTextColor(getResources().getColor(R.color.gray_time_text));
				}else {
					mSendAgainBtn.setTextColor(getResources().getColor(R.color.blue_496fb7));
				}
				if (!TextUtils.isEmpty(s)) {
					if (!TextUtils.isEmpty(phone_numer_edit.getText())) {
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
			mPhoneNumEdit.requestFocus();
			ToastUtils.showToast(this, "请输入正确的手机号码");
			return;
		}
		if (phoneNumber.trim().length()!=11) {
			mPhoneNumEdit.requestFocus();
			ToastUtils.showToast(this, "请输入正确的手机号码");
			return;
		}
		String num = SharedPreferenceUtil.getString(this,"telephone","");
		if (phone_numer_edit.getText().toString().trim().equals(num)){
			mPhoneNumEdit.requestFocus();
			ToastUtils.showToast(this, "该手机号码与当前登录的手机号码一致,请更换新号码重试");
			return;
		}
		if(!clicked){
			clicked = true;
			verifyTelephone(phoneNumber,smsOrVoice);
		}
	}

	/* 验证该号码有没有注册 */
	private void verifyTelephone(final String phoneNumber,final int smsOrVoice) {
		showLoadingDialog();
		HashMap<String,String> maps = new HashMap<>();
		maps.put("access_token", UserInfo.getInstance(this).getSesstion());
		maps.put("newPhone", phoneNumber);
		new HttpManager().post(this, Constants.DRUG +"drugCompanyEmployee/checkNewPhoneIfOnSystem", CheckPhoneOnSys.class,
				maps, new HttpManager.OnHttpListener<Result>() {
					@Override
					public void onSuccess(Result result) {
						if (result.resultCode ==1){
							CheckPhoneOnSys sys = (CheckPhoneOnSys)result;
							if (sys.data==1){
								closeLoadingDialog();
								mSendAgainBtn.setText(R.string.getcode);
								mSendAgainBtn.setEnabled(true);
								mSendAgainBtn.setTextColor(getResources().getColor(R.color.blue_496fb7));
								get_call_code.setTextColor(getResources().getColor(R.color.blue_496fb7));
								get_call_code.setClickable(true);
								clicked = false;
								ToastUtils.showToast(RegisterActivity.this, "该手机号已被绑定或使用，请更换新号码重试");
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
							closeLoadingDialog();
							clicked = false;
							if (!TextUtils.isEmpty(result.getResultMsg())) {
								ToastUtils.showToast(RegisterActivity.this, result.getResultMsg());
							}
						}  else {// 错误
							closeLoadingDialog();
							clicked = false;
							if (!TextUtils.isEmpty(result.getResultMsg())) {
								ToastUtils.showToast(RegisterActivity.this, result.getResultMsg());
							} else {
								ToastUtils.showToast(RegisterActivity.this, getResources().getString(R.string.data_exception));
							}
						}
					}
					}

					@Override
					public void onSuccess(ArrayList<Result> result) {

					}

					@Override
					public void onFailure(Exception e, String errorMsg, int s) {
						closeLoadingDialog();
						clicked = false;
					}
				},
				false, 1);
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
						closeLoadingDialog();
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
						closeLoadingDialog();
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
		new HttpManager().post(this, "/sms/randcode/getVoiceCode",
				Void.class, params, new HttpManager.OnHttpListener<Result>() {
					@Override
					public void onSuccess(Result result) {
						closeLoadingDialog();
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
				get_call_code.setTextColor(getResources().getColor(R.color.blue_496fb7));
				get_call_code.setClickable(true);

				reckonTime = 120;
			}
		}
	};



	@Override
	public void onBackPressed() {
		doBack();
	}

	private void doBack() {

		finish();
	}

	private void nextStep2() {

		final String phoneNumber = mPhoneNumEdit.getText().toString().trim();
		String authCode = mAuthCodeEdit.getText().toString().trim();
		if (TextUtils.isEmpty(phoneNumber)) {
			mPhoneNumEdit.requestFocus();
			return;
		}
		if (TextUtils.isEmpty(authCode)) {
			mAuthCodeEdit.requestFocus();
			return;
		}

		if (phoneNumber.trim().length()!=11) {
			mPhoneNumEdit.requestFocus();
			ToastUtils.showToast(this, "请输入正确的手机号码");
			return;
		}
		String num = SharedPreferenceUtil.getString(this,"telephone","");
		if (phone_numer_edit.getText().toString().trim().equals(num)){
			mPhoneNumEdit.requestFocus();
			ToastUtils.showToast(this, "该手机号码与当前登录的手机号码一致,请更换新号码重试");
			return;
		}


		if(TELEPHONE_AUTH)
		{
		}



		changePhoneNum(phoneNumber, this, authCode);
	}
	public  void changePhoneNum(String phoneNum, final Activity context,String authCode ) {
		//drugCompanyEmployee/modifyUserPhone
		showLoadingDialog();
		HashMap<String,String> maps = new HashMap<>();
		maps.put("access_token", UserInfo.getInstance(context).getSesstion());
		maps.put("newPhone", phoneNum);
		maps.put("drugCompanyId", SharedPreferenceUtil.getString(context, "enterpriseId", ""));
		maps.put("authCode", authCode);
		final String templateId = "25118";
		maps.put("templateId", templateId);
		new HttpManager().post(context, Constants.DRUG + "drugCompanyEmployee/modifyUserPhone", Result.class,
				maps, new HttpManager.OnHttpListener<Result>() {
					@Override
					public void onSuccess(Result response) {
						closeLoadingDialog();
						if (response.resultCode == 1) {
							ToastUtil.showToast(context, "修改手机号成功，请使用新手机号登录");
							if (context instanceof BaseActivity) {
								BaseActivity baseActivity = (BaseActivity) context;
								baseActivity.closeLoadingDialog();
							}
							Intent intent = new Intent(context, LoginActivity.class);
							context.startActivity(intent);
							context.finish();
						} else {
							ToastUtils.showToast(context, response.resultMsg);
						}
					}

					@Override
					public void onSuccess(ArrayList<Result> response) {

					}

					@Override
					public void onFailure(Exception e, String errorMsg, int s) {
						closeLoadingDialog();
					}
				},
				false, 1);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
			case R.id.next_step_btn:
				sendAgain(SMSCODE);
				break;

		}
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
