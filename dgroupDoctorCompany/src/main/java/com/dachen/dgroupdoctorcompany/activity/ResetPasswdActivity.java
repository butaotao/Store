package com.dachen.dgroupdoctorcompany.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.base.UserLoginc;
import com.dachen.dgroupdoctorcompany.entity.LoginRegisterResult;
import com.dachen.dgroupdoctorcompany.utils.UserUtils;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.SystemUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 重置密码界面
 *
 *
 *
 */
public class ResetPasswdActivity extends BaseActivity implements
		OnClickListener, OnHttpListener {

	private static final String TAG = ResetPasswdActivity.class.getSimpleName();

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
		enableBack();
		phone = getIntent().getStringExtra(PreResetPasswdActivity.PHONE);
		ranCode = getIntent().getStringExtra(PreResetPasswdActivity.RANCODE);
		smsid = getIntent().getStringExtra(PreResetPasswdActivity.SMSID);

		initViews();
	}

	void initViews() {
		setTitle("设置密码");
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
		final String userType = Constants.USER_TYPE;
		String confirmPassword = mConfirmPasswordEdit.getText().toString()
				.trim();

	/*	if (TextViewUtils.isEmpty(password) || password.length() < 6) {
			mPasswordEdit.requestFocus();
			mPasswordEdit.setError(StringUtils.editTextHtmlErrorTip(this,
					R.string.password_empty_error));
			return;
		}
		if (TextViewUtils.isEmpty(confirmPassword) || confirmPassword.length() < 6
				|| confirmPassword.length() > 18) {
			mConfirmPasswordEdit.requestFocus();
			mConfirmPasswordEdit.setError(StringUtils.editTextHtmlErrorTip(
					this, R.string.confirm_password_empty_error));
			return;
		}*/


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
			return;
		}

		// TODO 注释
		reset(password, userType);

	}

	private void reset(String password, String userType) {
		// TODO Auto-generated method stub
		final String requestTag = "Reset";
		showLoadingDialog();
		new HttpManager().post(this, Constants.RESET_PASSWD,
				LoginRegisterResult.class, Params.getResetPasswordParams(phone,
						userType, smsid, ranCode, mPasswordEdit.getText()
								.toString().trim()), this, false, 1);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(Result response) {
		closeLoadingDialog();
		// TODO Auto-generated method stub
		if (null != response && 1 == response.getResultCode()) {
			/*Intent intent = new Intent(ResetPasswdActivity.this,
					LoginActivity.class);

			startActivity(intent);*/
			if (response instanceof  LoginRegisterResult){
				ToastUtils.showToast(ResetPasswdActivity.this,"密码重置成功");
				LoginRegisterResult logins = (LoginRegisterResult) response;
				UserLoginc.setUserInfo(logins, ResetPasswdActivity.this);

				boolean success = false;
				if (response.getResultCode() == Result.CODE_SUCCESS) {
					if (null != logins.data.getUser().getUserId() && null != logins.data.getUser().getTelephone()
							&& null != logins.data.getUser().getName())
						success = true;// 设置登陆用户信息
				}

				if (success) {// 登陆成功
					UserUtils.logingetUserType(ResetPasswdActivity.this);
					//							UserLoginc.setUserInfo(logins, ResetPasswdActivity.this);
				} else {// 登录失败
					String message = TextUtils.isEmpty(response.getResultMsg()) ? getString(R.string.login_failed) : response.getResultMsg();
					ToastUtils.showToast(ResetPasswdActivity.this, message);
				}



//				autoLogin();
			}


		} else {
			ToastUtils.showToast(ResetPasswdActivity.this,response.getResultMsg());
		}
	}
	/**
	 * 注册后自动登录
	 */
	private void autoLogin() {
		Intent intent = new Intent();
		final String userId = SharedPreferenceUtil.getString(ResetPasswdActivity.this, "id", "");
		final String access_token = SharedPreferenceUtil.getString(ResetPasswdActivity.this,"session", "");
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
				LoginRegisterResult.class, params, new OnHttpListener<Result>() {
					@Override
					public void onSuccess(Result result) {
						if (result == null) {
							closeLoadingDialog();
							return;
						}
						LoginRegisterResult logins = (LoginRegisterResult) result;
						UserLoginc.setUserInfo(logins, ResetPasswdActivity.this);
						boolean success = false;
						if (result.getResultCode() == Result.CODE_SUCCESS) {
							if (null != logins.data.getUser().getUserId() && null != logins.data.getUser().getTelephone()
									&& null != logins.data.getUser().getName())
								success = true;// 设置登陆用户信息
						}


						if (success) {// 登陆成功
							UserUtils.logingetUserType(ResetPasswdActivity.this);
//							UserLoginc.setUserInfo(logins, ResetPasswdActivity.this);
						} else {// 登录失败
							String message = TextUtils.isEmpty(result.getResultMsg()) ? getString(R.string.login_failed) : result.getResultMsg();
							ToastUtils.showToast(ResetPasswdActivity.this, message);
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
	public void onSuccess(ArrayList response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailure(Exception e, String errorMsg,int s) {
		closeLoadingDialog();
		// TODO Auto-generated method stub
		ToastUtils.showToast(ResetPasswdActivity.this,errorMsg);
	}

}
