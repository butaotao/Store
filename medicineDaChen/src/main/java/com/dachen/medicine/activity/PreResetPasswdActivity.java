package com.dachen.medicine.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.dachen.medicine.R;
import com.dachen.medicine.app.Constants;
import com.dachen.medicine.app.MedicineApplication;
import com.dachen.medicine.common.utils.StringUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.ResetPassword;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;

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
	@Bind(R.id.reset_top_txt)
	TextView mResetTopTxt;
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
	@Bind(R.id.next_step_btn)
	Button mNextStepBtn;
	@Nullable
	@Bind(R.id.back_step_btn)
	Button btn_back;

	protected ProgressDialog mProgressDialog;
	private String smsid = "";
	String authCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pre_reset_password);
		ButterKnife.bind(this);
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
	@OnClick(R.id.send_again_btn)
	void onSendAgainBtnClicked() {
		phoneNumber = mPhoneNumEdit.getText().toString().trim();
		if (TextUtils.isEmpty(phoneNumber)) {
			ToastUtils.showToast(getResources().getString(
					R.string.toast_verify_phone_null));
			return;
		}
		if (/*!StringUtils.isMobileNumber(phoneNumber)*/phoneNumber.length() != 11) {
			ToastUtils.showToast(R.string.toast_verify_phone_length);
			mPhoneNumEdit.requestFocus();
		/*	mPhoneNumEdit.setError(StringUtils.editTextHtmlErrorTip(
					PreResetPasswdActivity.this,
					R.string.phone_number_format_error));*/
			return;
		}
		sendAgain();
	}

	@Nullable
	@OnClick(R.id.next_step_btn)
	void onSureBtnClicked() {
		// http请求
		nextStep();
	}

	private void sendAgain() {

		phoneNumber = mPhoneNumEdit.getText().toString().trim();
		final String userType = Constants.USERTYPE;
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(phoneNumber)) {
			ToastUtils.showToast(getResources().getString(
					R.string.toast_verify_phone_null));
			return;
		}
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(PHONE, phoneNumber);
		params.put("userType", userType);
		HashMap<String, String> interfaces = (HashMap<String, String>) Params
				.getInterface(Constants.PRE_RESET_PASSWD);
		//
		new HttpManager().post(MedicineApplication.app, interfaces,
				ResetPassword.class, params, this, 1);
	}

	private int reckonTime = 120;
	private Handler mReckonHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0x1) {
				mSendAgainBtn.setText(reckonTime + "s");
				mSendAgainBtn.setTextColor(getResources().getColor(
						R.color.common_text_grey));
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


		authCode = mAuthCodeEdit.getText().toString().trim();
		if (TextUtils.isEmpty(authCode)) {
			mAuthCodeEdit.requestFocus();
			ToastUtils.showToast(R.string.auth_code_input);
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
		final String userType = Constants.USERTYPE;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("phone", phoneNumber);
		params.put("userType", userType);
		params.put("ranCode", randcode);
		params.put("smsid", smsid);
		HashMap<String, String> interfaces = new HashMap<String, String>();
		interfaces.put("interface1", Constants.VERIFYRESETPASSWORD);
		new HttpManager().post(interfaces, Result.class, params, this, 1);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(Result result) {
		// TODO Auto-generated method stub
		System.err.println("response==" + result);
		if (result != null) {// 发送成功
			if (result instanceof ResetPassword) {
				if (1 == result.getResultCode()) {

					ResetPassword reset = (ResetPassword) result;
					smsid = reset.getData().smsid;

					mSendAgainBtn.setEnabled(false);
					mReckonHandler.sendEmptyMessage(0x1);
					ToastUtils.showToast("验证码已发送，请等待");

				} else {
					ToastUtils.showToast(result.getResultMsg());
				}
			} else {
				if (1 == result.getResultCode()) {
					Intent intent = new Intent(PreResetPasswdActivity.this,
							ResetPasswdActivity.class);
					intent.putExtra(SMSID, smsid);
					intent.putExtra(PHONE, phoneNumber);
					intent.putExtra(RANCODE, authCode);
					startActivity(intent);
					ToastUtils.showToast(R.string.auth_code_right);
				} else {
					ToastUtils.showToast(R.string.auth_code_error);
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
		ToastUtils.showToast(R.string.net_exception);
	}

}
