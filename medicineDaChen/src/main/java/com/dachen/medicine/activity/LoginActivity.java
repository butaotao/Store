package com.dachen.medicine.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;
import com.baoyz.actionsheet.ActionSheet.ActionSheetListener;
import com.dachen.incomelibrary.utils.UserInfo;
import com.dachen.medicine.R;
import com.dachen.medicine.app.Constants;
import com.dachen.medicine.app.MedicineApplication;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.common.utils.UserUtils.LoginUtils;
import com.dachen.medicine.config.ContextConfig;
import com.dachen.medicine.db.MedicineProvider;
import com.dachen.medicine.db.MedicineURIField;
import com.dachen.medicine.db.table.TableUser;
import com.dachen.medicine.entity.LoginRegisterResult;
import com.dachen.medicine.entity.LoginVerify;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.entity.User;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;

import java.util.ArrayList;
import java.util.HashMap;

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
	@Bind(R.id.tv_login_title)
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
	//DialogBank dialog;
	protected ProgressDialog mProgressDialog;
	private int clickTitle = 0;
	String phoneNum;
	String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ButterKnife.bind(this);
		initView();
	}

	private void initView() {
		  String tel = 
					SharedPreferenceUtil.getString("telephone", "");
		   if(!TextUtils.isEmpty(tel)) {
			   mPhoneNumberEdit.setText(tel);
			   mPasswordEdit.setFocusable(true);
			   mPasswordEdit.setFocusableInTouchMode(true);
			   mPasswordEdit.requestFocus();
		   }
			  
		  
	}

	/*
	 * @Nullable
	 * 
	 * @OnClick(R.id.register_account_btn) void onRegisterAccountBtnClicked() {
	 * //startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
	 * }
	 */

	@Nullable
	@OnClick(R.id.forget_password_btn)
	void onForgetPasswordBtnClicked() {
		Intent intent = new Intent(this, PreResetPasswdActivity.class);
		startActivity(intent);
	}

	@Nullable
	@OnClick(R.id.login_btn)
	void onLoginBtnClicked() {
		login();
	}

	@Nullable
	@OnClick(R.id.tv_login_title)
	void onLoginTitleClicked() {
		if(clickTitle>3)
		 {
		clickTitle = 0;
		ActionSheet.createBuilder(this, getSupportFragmentManager())
				.setCancelButtonTitle("取消")
				.setOtherButtonTitles("内网", "外网(阿里云环境)", "生产环境","生产测试环境", "屈军利")
				.setCancelableOnTouchOutside(true).setListener(this).show();
		 }
		else {
		 clickTitle++;
		 }
	}

	private void login() {

		if (!verifyPhoneIfNeed()) {
			return;
		}
		;
		showLoadingDialog();
		loginRequest(phoneNum, password);

	}

	private void loginRequest(String phoneNum, String password) {
		new HttpManager().post(Constants.LOGIN + "", LoginRegisterResult.class,
				Params.getLoginParams(phoneNum, password, Constants.USERTYPE), this,
				false, 1);
	}

	public final boolean verifyPhoneIfNeed() {
		boolean verify = true;
		phoneNum = mPhoneNumberEdit.getText().toString().trim();
		password = mPasswordEdit.getText().toString().trim();
		if (TextUtils.isEmpty(phoneNum)) {
			ToastUtils.showToast(R.string.toast_verify_phone_null);
			verify = false;
			return verify;
		}

		if (phoneNum.length() != 11) {
			ToastUtils.showToast(R.string.toast_verify_phone_length);
			verify = false;
			return verify;
		}

		if (TextUtils.isEmpty(password)) {
			ToastUtils.showToast(R.string.toast_verify_password_null);
			verify = false;
			return verify;
		}
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
			ToastUtils.showToast("已切换到内网" + ContextConfig.API_INNER_URL);
			/*ContextConfig.getInstance().setEnvironmentType(
					EnvironmentType.INNER);*/

			com.dachen.incomelibrary.utils.Constants.changeIp(ContextConfig.API_INNER_URL);
			UserInfo.getInstance(this).setKeyNet(ContextConfig.API_INNER_URL);
			SharedPreferenceUtil.putString("netdes", ContextConfig.API_INNER_URL);
		} else if (index == 1) {
			ToastUtils.showToast("已切换到阿里云环境" + ContextConfig.APP_PUBLISH_API_URL);
			/*ContextConfig.getInstance().setEnvironmentType(
					EnvironmentType.PUBLISH);*/
			com.dachen.incomelibrary.utils.Constants.changeIp(ContextConfig.API_OTER_URL);
			UserInfo.getInstance(this).setKeyNet(ContextConfig.API_OTER_URL);
			SharedPreferenceUtil.putString("netdes", ContextConfig.API_OTER_URL);
		} else if (index == 2) {
			ToastUtils.showToast("已切换到生产环境" + ContextConfig.APP_TEST_API_URL);
			/*ContextConfig.getI			nstance()
					.setEnvironmentType(EnvironmentType.TEST);*/
			com.dachen.incomelibrary.utils.Constants.changeIp(ContextConfig.KANG_ZHE);
			UserInfo.getInstance(this).setKeyNet(ContextConfig.KANG_ZHE);
			SharedPreferenceUtil.putString("netdes", ContextConfig.KANG_ZHE);

		}else if (index == 3) {
			ToastUtils.showToast("已切换到生产测试环境" + ContextConfig.KANG_ZHE_TEST);
			/*ContextConfig.getI			nstance()
					.setEnvironmentType(EnvironmentType.TEST);*/
			com.dachen.incomelibrary.utils.Constants.changeIp(ContextConfig.KANG_ZHE_TEST);
			UserInfo.getInstance(this).setKeyNet(ContextConfig.KANG_ZHE_TEST);
			SharedPreferenceUtil.putString("netdes", ContextConfig.KANG_ZHE_TEST);

		} else {
		/*	ContextConfig.getInstance()
			.setEnvironmentType(EnvironmentType.API_QUJUNLI_URL); */
		}
	}

	@Override
	public void onSuccess(Result entity) {
		// TODO Auto-generated method stub
		closeLoadingDialog();
		  if (entity instanceof LoginRegisterResult) {
			if (entity.getResultCode() != 1) {
				ToastUtils.showToast(entity.getResultMsg());
//				if (entity.getResultCode() == 1040103){//到重置密码页面
//					Intent intent = new Intent(LoginActivity.this, PreResetPasswdActivity.class);
//					startActivity(intent);
//				}
				return;
			}

			LoginRegisterResult logins = (LoginRegisterResult) entity;
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



			 // startMainActivity();
		//	loginVerifyTwice();
			LoginUtils.loginVerifyTwice(this,true);
		}/*else if(entity instanceof  LoginVerify){
			LoginVerify verify = (LoginVerify) entity;
			  SharedPreferenceUtil.putString("companyId","");
			if(verify.data==null||verify.data.companys==null){
				ToastUtils.showToast("该员工无此权限");
			}else {
				boolean isManager = false;
				if (null!=verify.data&&verify.data.companys!=null&&verify.data.companys.size()>0){
					for (int i=0;i<verify.data.companys.size();i++){
						if (!TextUtils.isEmpty(verify.data.companys.get(i).duty)){
							if (verify.data.companys.get(i).duty.equals("1")){
								SharedPreferenceUtil.putString("shop_manager","店长");

								isManager = true;
							}
						}
						SharedPreferenceUtil.putString("companyId",verify.data.companys.get(i).companyId);
						break;
					}
				}
				if (!isManager){
					SharedPreferenceUtil.putString("shop_manager","");
				}
				ToastUtils.showToast(getResources().getString(
						R.string.toast_login_success));
				startMainActivity();
			}
		}*/
	}
public void startMainActivity(){
	Intent intent = new Intent(LoginActivity.this, MainActivity.class);
	intent.putExtra("login", "login");
	startActivity(intent);
	finish();
}
	@Override
	public void onFailure(Exception e, String errorMsg,int s) {
		// TODO Auto-generated method stub
		ToastUtils.showToast(getString(R.string.connect_error)); 
		closeLoadingDialog(); 
	} 
	 

	@Override
	public void onSuccess(ArrayList response) {
		// TODO Auto-generated method stub

	}
	//http://192.168.3.7:9002/web/api/login?u=12010101010|9&p=123456
	private void loginVerifyTwice(){
		HashMap<String,String> maps = new HashMap<>();
		maps.put("access_token",SharedPreferenceUtil.getString("session",""));
				maps.put("userId",SharedPreferenceUtil.getString("id",""));
		new HttpManager().get(this, "drugCompanyEmployee/getLoginInfo"+ "", LoginVerify.class,maps
				, this,
				false, 3);
	}


	
}
