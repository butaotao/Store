package com.dachen.dgroupdoctorcompany.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.TelePhoneVerifyData;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.ResetPassword;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;
import com.dachen.medicine.view.ClearEditText;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 请求重置密码界面
 *
 *
 *
 */
public class PreResetPasswdActivity extends BaseActivity implements
		OnClickListener, OnHttpListener {

	private static final String TAG = PreResetPasswdActivity.class
			.getSimpleName();
	public static final String SMSID = "smsid";
	public static final String PHONE = "phone";
	public static final String RANCODE = "ranCode";
	String phoneNumber;

	@Nullable
	@Bind(R.id.phone_numer_edit)
	ClearEditText mPhoneNumEdit;
	@Nullable
	@Bind(R.id.auth_code_edit)
	EditText mAuthCodeEdit;
	@Nullable
	@Bind(R.id.send_again_btn)
	Button mSendAgainBtn;
	@Nullable
	@Bind(R.id.next_step_btn)
	Button mNextStepBtn;

	@Nullable
	@Bind(R.id.get_call_code)
	TextView get_call_code;
	protected ProgressDialog mProgressDialog;
	private String smsid = "";
	String authCode;
	String phonenum;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pre_reset_password);
		ButterKnife.bind(this);
		phonenum = getIntent().getStringExtra("phonenum");
		enableBack();
		initViews();
	}

	void initViews() {
		setTitle("找回密码");
		if (!TextUtils.isEmpty(phonenum)){
			mPhoneNumEdit.setText(phonenum);
			mPhoneNumEdit.setFocusable(false);
			mPhoneNumEdit.clearFocus();
			mPhoneNumEdit.setInputType(InputType.TYPE_NULL);
			mPhoneNumEdit.setTextColor(getResources().getColor(R.color.gray_bbbbbb));
			mPhoneNumEdit.setClearIconVisible(false);
			mAuthCodeEdit.setFocusable(true);

			mAuthCodeEdit.requestFocus();
			setTitle("设置密码");
		}



		findViewById(R.id.rl_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	@Nullable
	@OnClick(R.id.get_call_code)
	void onGetCallCodeClicked() {
		String phoneNumber = mPhoneNumEdit.getText().toString().trim();
		if (TextUtils.isEmpty(phoneNumber)) {
			ToastUtils.showToast(PreResetPasswdActivity.this,"请输入正确的手机号");
			return;
		}
		getVoiceCode(phoneNumber);
	}
	/**
	 * 获取4位语音验证码
	 *
	 * @return
	 */
	private void getVoiceCode(final String phoneNumber) {
		showLoadingDialog();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("phone", phoneNumber);
		params.put("userType", Constants.USER_TYPE);


		new HttpManager().post(this, Constants.GETVOICECODE, TelePhoneVerifyData.class,
				params, this,
				false, 1);
	}


	@Nullable
	@OnClick(R.id.send_again_btn)
	void onSendAgainBtnClicked() {
		phoneNumber = mPhoneNumEdit.getText().toString().trim();
		if (TextUtils.isEmpty(phoneNumber)) {
			ToastUtils.showToast(PreResetPasswdActivity.this, getResources().getString(
					R.string.toast_verify_phone_null));
			return;
		}
		if (phoneNumber.length()<11) {
			ToastUtils.showToast(PreResetPasswdActivity.this, getResources().getString(
					R.string.toast_verify_phone_length));
			return;
		}
		/*if (!StringUtils.isMobileNumber(phoneNumber)) {
			mPhoneNumEdit.requestFocus();
			mPhoneNumEdit.setError(StringUtils.editTextHtmlErrorTip(
					PreResetPasswdActivity.this,
					R.string.phone_number_format_error));
			return;
		}*/
		sendAgain();
	}

	@Nullable
	@OnClick(R.id.next_step_btn)
	void onSureBtnClicked() {
		// http请求
		nextStep();
	}

	private void sendAgain() {
		showLoadingDialog();
		phoneNumber = mPhoneNumEdit.getText().toString().trim();
		final String userType = Constants.USER_TYPE;
		final String requestTag = "preReset";
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(phoneNumber)) {
			ToastUtils.showToast(PreResetPasswdActivity.this,getResources().getString(
					R.string.toast_verify_phone_null));
			return;
		}
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(PHONE, phoneNumber);
		params.put("userType", userType);
		HashMap<String, String> interfaces = (HashMap<String, String>) Params
				.getInterface(Constants.PRE_RESET_PASSWD);
		//
		new HttpManager().post(this, interfaces,
				ResetPassword.class, params, this, 1);
	}

	private int reckonTime = 120;
	private Handler mReckonHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0x1) {
				mSendAgainBtn.setText(reckonTime + "s");
				mSendAgainBtn.setTextColor(getResources().getColor(R.color.common_text_grey));
				mSendAgainBtn.setEnabled(false);
				reckonTime--;
				if (reckonTime < 0) {
					mReckonHandler.sendEmptyMessage(0x2);
				} else {
					mReckonHandler.sendEmptyMessageDelayed(0x1, 1000);
				}
			} else if (msg.what == 0x2) {// 120秒结束
				mSendAgainBtn.setText(R.string.again);
				mSendAgainBtn.setTextColor(getResources().getColor(R.color.color_3cbaff));
				mSendAgainBtn.setEnabled(true);
				get_call_code.setTextColor(getResources().getColor(R.color.color_3cbaff));
				get_call_code.setClickable(true);
				reckonTime = 120;
			}
		}
	};

	private void nextStep() {
		final String phoneNumber = mPhoneNumEdit.getText().toString().trim();
		if (TextUtils.isEmpty(phoneNumber)) {
			ToastUtils.showToast(PreResetPasswdActivity.this, R.string.toast_verify_phone_null);

			return;
		}

		/*if (!StringUtils.isMobileNumber(phoneNumber)) {
			mPhoneNumEdit.requestFocus();
			mPhoneNumEdit.setError(StringUtils.editTextHtmlErrorTip(this,
					R.string.phone_number_format_error));
			return;
		}*/

		authCode = mAuthCodeEdit.getText().toString().trim();
		if (TextUtils.isEmpty(authCode)) {
			mAuthCodeEdit.requestFocus();
			ToastUtils.showToast(PreResetPasswdActivity.this,R.string.auth_code_input);
			// mAuthCodeEdit.setError(StringUtils.editTextHtmlErrorTip(this,
			// R.string.auth_code_input));
			return;
		}

		verifyCode(phoneNumber, authCode);
	}

	/**
	 * 是否请求了验证码
	 *
	 * @return
	 */
	private void verifyCode(final String phoneNumber, final String randcode) {
		showLoadingDialog();
		final String templateId = "25118";
		final String userType = Constants.USER_TYPE;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("phone", phoneNumber);
		params.put("userType", userType);
		params.put("ranCode", randcode);
		params.put("smsid", smsid);
		params.put("templateId", templateId);
		new HttpManager().post(this,Constants.VERIFYRESETPASSWORD, Result.class, params, this,false, 1);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(Result result) {
		// TODO Auto-generated method stub
		System.err.println("response==" + result);
		closeLoadingDialog();
		if (result != null) {// 发送成功
			if (result instanceof ResetPassword) {
				if (1 == result.getResultCode()) {

					ResetPassword reset = (ResetPassword) result;
					smsid = reset.getData().smsid;

					mSendAgainBtn.setEnabled(false);
					mReckonHandler.sendEmptyMessage(0x1);
					ToastUtils.showToast(PreResetPasswdActivity.this,"验证码已发送，请等待");
					get_call_code.setTextColor(getResources().getColor(R.color.gray_time_text));
					get_call_code.setClickable(false);

				} else {
					ToastUtils.showToast(PreResetPasswdActivity.this,result.getResultMsg());
				}
			}else if(result instanceof TelePhoneVerifyData){
				TelePhoneVerifyData results = (TelePhoneVerifyData)(result);
				if (results != null && results.getResultCode() == 1) {// 发送成功

					if (results !=null && null!=results.data) {

						smsid = results.data.smsid;

						ToastUtils.showToast(PreResetPasswdActivity.this, "语音验证码已发送，请注意接听电话");
						//语音验证码不可点击，直到120秒后
						mSendAgainBtn.setEnabled(false);
						mReckonHandler.sendEmptyMessage(0x1);
						get_call_code.setTextColor(getResources().getColor(R.color.gray_time_text));
						get_call_code.setClickable(false);
					}
				} else {
					mReckonHandler.removeCallbacksAndMessages(null);
					mReckonHandler.sendEmptyMessage(0x2);
					ToastUtils.showToast(PreResetPasswdActivity.this, result.getResultMsg());
				}
			} else {
				if (1 == result.getResultCode()) {
					phoneNumber = mPhoneNumEdit.getText().toString().trim();
					Intent intent = new Intent(PreResetPasswdActivity.this,
							ResetPasswdActivity.class);
					intent.putExtra(SMSID, smsid);
					intent.putExtra(PHONE, phoneNumber);
					intent.putExtra(RANCODE, authCode);
					startActivity(intent);
					ToastUtils.showToast(PreResetPasswdActivity.this,R.string.auth_code_right);
				} else {
					ToastUtils.showToast(PreResetPasswdActivity.this,R.string.auth_code_error);
				}
			}
		}
	}

	@Override
	public void onSuccess(ArrayList response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailure(Exception e, String errorMsg,int s) {
		// TODO Auto-generated method stub
		ToastUtils.showToast(PreResetPasswdActivity.this,R.string.net_exception);
		closeLoadingDialog();
	}

}
