package com.dachen.dgroupdoctorcompany.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.base.UserLoginc;
import com.dachen.dgroupdoctorcompany.entity.Company;
import com.dachen.dgroupdoctorcompany.entity.LoginGetUserInfo;
import com.dachen.dgroupdoctorcompany.utils.UserUtils;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.SystemUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.LoginRegisterResult;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册输入密码页面
 * 
 * @author ZENG
 * 
 */
public class RegisterStep2Activity extends BaseActivity implements OnClickListener,HttpManager.OnHttpListener{
	public static final String EXTRA_AUTH_CODE = "auth_code";
	public static final String EXTRA_PHONE_NUMBER = "phone_number";
	public static final String EXTRA_PASSWORD = "password";
	public static final String EXTRA_USERID = "user_id";
	protected ProgressDialog mProgressDialog;
	protected Intent intent;
	private final String userType = Constants.USER_TYPE;
  
	@Nullable
	@Bind(R.id.name_edit)
	EditText name_edit;
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
	@Bind(R.id.back_step_btn) 
	Button btn_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_step2);
		ButterKnife.bind(this);
		initViews();
	}

	 void initViews() {
		 setTitle("注册");
		 mNextStepBtn.setBackgroundResource(R.drawable.btn_blue_all_9ddcff);
		 mNextStepBtn.addTextChangedListener(new TextWatcher() {
			 @Override
			 public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			 }

			 @Override
			 public void onTextChanged(CharSequence s, int start, int before, int count) {
				 if (!TextUtils.isEmpty(s)) {
					 if (!TextUtils.isEmpty(mConfirmPasswordEdit.getText())) {
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
	@OnClick(R.id.next_step_btn)
	void onNextStepBtnClicked() {
		nextStep();
	}
	
	@Nullable
	@OnClick(R.id.rl_back)
	void onBackStepBtnClicked() {
		doBack();
	}
	
	private void nextStep() {
		final String phoneNumber = getIntent().getStringExtra("phoneNumber");
		final String password = mPasswordEdit.getText().toString().trim();
		//final String name = name_edit.getText().toString().trim();
		String confirmPassword = mConfirmPasswordEdit.getText().toString().trim();
		if (TextUtils.isEmpty(phoneNumber)) {
			return;
		}
		
	/*	if (TextUtils.isEmpty(name)) {
			name_edit.requestFocus();
			name_edit.setError(StringUtils.editTextHtmlErrorTip(this, R.string.please_input_name));
			return;
		}*/

		if(TextUtils.isEmpty(password)){
			mPasswordEdit.requestFocus();
//			mPasswordEdit.setError(StringUtils.editTextHtmlErrorTip(this, R.string.register_empty_error));
			ToastUtil.showToast(this,R.string.register_empty_error);
			return;
		}
		if ( password.length() < 6||password.length()>18) {
			mPasswordEdit.requestFocus();
//			mPasswordEdit.setError(StringUtils.editTextHtmlErrorTip(this, R.string.register_empty_length_error));
			ToastUtil.showToast(this,R.string.register_empty_length_error);
			return;
		}
//		if (TextUtils.isEmpty(confirmPassword) || confirmPassword.length() < 6 || confirmPassword.length() > 18) {
//			mConfirmPasswordEdit.requestFocus();
//			mConfirmPasswordEdit.setError(StringUtils.editTextHtmlErrorTip(this, R.string.confirm_password_empty_error));
//			return;
//		}
		if (!confirmPassword.equals(password)) {
			mConfirmPasswordEdit.requestFocus();
//			mConfirmPasswordEdit.setError(StringUtils.editTextHtmlErrorTip(this, R.string.register_password_confirm_password_not_match));
			ToastUtil.showToast(this,R.string.register_password_confirm_password_not_match);
			return;
		}
		
		register(phoneNumber,password,userType);
	}

	private void register(final String phoneNumber,final String password,final String userType) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("telephone",phoneNumber);
		params.put("password", password);
		params.put("userType", userType);
//		params.put("name", phoneNumber);
		HashMap<String, String> interfaces = new HashMap<String, String>();
		interfaces.put("interface1", Constants.USER_REGISTER);
		new HttpManager().post(this, interfaces,
				LoginRegisterResult.class, params, new HttpManager.OnHttpListener<Result>() {
					@Override
					public void onSuccess(Result result) {
						if (result == null) {
							ToastUtils.showToast(RegisterStep2Activity.this, getResources().getString(R.string.register_error));
							return;
						}
						if (result.getResultCode() == Result.CODE_SUCCESS) {// 注册成功
							// boolean success = LoginHelper.setLoginUser(RegisterActivity.this, phoneNumber, password, result);
							LoginRegisterResult logins = (LoginRegisterResult) result;
				/*			SharedPreferenceUtil.putString(RegisterStep2Activity.this,"session",
									logins.data.getAccess_token());
							String url = *//*getAvatarUrl(logins.data.getUser().getUserId(),*//*logins.data.getUser().headPicFileName;//);
							SharedPreferenceUtil.putString(mThis,   "head_url", url);
							SharedPreferenceUtil.putString(mThis, logins.data.getUser().getUserId() + "head_url", url);
							SharedPreferenceUtil.putString(RegisterStep2Activity.this,"id", logins.data.getUser().getUserId());
							SharedPreferenceUtil.putString(RegisterStep2Activity.this,"telephone", logins.data.getUser().getTelephone());
							SharedPreferenceUtil.putString(RegisterStep2Activity.this,"username", logins.data.getUser().getName());
							SharedPreferenceUtil.putString(RegisterStep2Activity.this,"usertype", userType);
							SharedPreferenceUtil.getString(mThis, "nickname", logins.data.getUser().getNickname());
							long expires_in = logins.data.getExpires_in() * 1000L + System.currentTimeMillis();
							SharedPreferenceUtil.putLong(RegisterStep2Activity.this, "expires_in", expires_in);
							LoginRegisterResult.LoginData d = logins.data;
							User u = d.getUser();
							ImSdk.getInstance().initUser(d.getAccess_token(), u.userId, u.name, u.nickname, u.headPicFileName);*/


							UserLoginc.setUserInfo(logins, RegisterStep2Activity.this);


							if (null != logins.data.getUser().getUserId() && null != logins.data.getUser().getTelephone()
									) {
								closeLoadingDialog();
								autoLogin();
							} else {// 失败
								closeLoadingDialog();
								if (TextUtils.isEmpty(result.getResultMsg())) {
									ToastUtils.showToast(RegisterStep2Activity.this, getResources().getString(R.string.register_error));
								} else {
									ToastUtils.showToast(RegisterStep2Activity.this, result.getResultMsg());
								}
							}
						} else {// 失败
							closeLoadingDialog();
							if (TextUtils.isEmpty(result.getResultMsg())) {
								ToastUtils.showToast(RegisterStep2Activity.this, getResources().getString(R.string.register_error));
							} else {
								ToastUtils.showToast(RegisterStep2Activity.this, result.getResultMsg());
							}
						}
					}

					@Override
					public void onSuccess(ArrayList<Result> response) {

					}

					@Override
					public void onFailure(Exception e, String errorMsg, int s) {
						ToastUtils.showToast(RegisterStep2Activity.this, getResources().getString(R.string.auth_code_error));
					}
				}, 3);
	}
	
	/**
	 * 注册后自动登录
	 */
	private void autoLogin() {
		intent = new Intent();
		final String userId = SharedPreferenceUtil.getString(RegisterStep2Activity.this,"id", "");
		final String access_token = SharedPreferenceUtil.getString(RegisterStep2Activity.this,"session", "");
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
						UserLoginc.setUserInfo(logins, RegisterStep2Activity.this);
						boolean success = false;
						if (result.getResultCode() == Result.CODE_SUCCESS) {
							if (null != logins.data.getUser().getUserId() && null != logins.data.getUser().getTelephone()
									&& null != logins.data.getUser().getName())
								success = true;// 设置登陆用户信息
						}


						if (success) {// 登陆成功
							UserUtils.logingetUserType(RegisterStep2Activity.this);
						} else {// 登录失败
							String message = TextUtils.isEmpty(result.getResultMsg()) ? getString(R.string.login_failed) : result.getResultMsg();
							ToastUtils.showToast(RegisterStep2Activity.this, message);
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
		AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(R.string.prompt_title).setMessage(R.string.cancel_register_prompt)
				.setNegativeButton(getString(R.string.no), null).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
						Intent intent = new Intent(RegisterStep2Activity.this,RegisterActivity.class);
						startActivity(intent);
					}
				});
		builder.create().show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	private void logingetUserType( ) {

		String s = "org/drugCompanyEmployee/getLoginInfo";
		new HttpManager().post(this, s, LoginGetUserInfo.class,
				Params.getUserInfo(this), this,
				false, 1);
	}

	@Override
	public void onSuccess(Result entity) {
		if (entity.resultCode==1){
			if (entity instanceof LoginGetUserInfo){
				LoginGetUserInfo info = (LoginGetUserInfo) entity;

				UserLoginc.setUserInfos(info, RegisterStep2Activity.this);
				Intent intent = new Intent(mThis, MainActivity.class);
				intent.putExtra("login", "login");
				startActivity(intent);
				finish();
			}
		}else {
			ToastUtils.showToast(this,""+entity.resultMsg);
		}
	}

	@Override
	public void onSuccess(ArrayList response) {

	}

	@Override
	public void onFailure(Exception e, String errorMsg, int s) {

	}
}
