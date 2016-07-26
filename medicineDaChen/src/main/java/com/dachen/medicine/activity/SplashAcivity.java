package com.dachen.medicine.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.dachen.medicine.R;
import com.dachen.medicine.app.Constants;
import com.dachen.medicine.app.MedicineApplication;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.common.utils.UserUtils.LoginUtils;
import com.dachen.medicine.db.MedicineProvider;
import com.dachen.medicine.db.MedicineURIField;
import com.dachen.medicine.db.table.TableUser;
import com.dachen.medicine.entity.LoginRegisterResult;
import com.dachen.medicine.entity.LoginVerify;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.entity.User;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;

import java.util.ArrayList;
import java.util.HashMap;

public class SplashAcivity extends BaseActivity implements HttpManager.OnHttpListener {
	public final static String SHAREDPREFERENCES_NAME = "first_pref";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.activity_splash, null);
		setContentView(view);
		startActivitys();
		final Uri uridata = this.getIntent().getData();
//		showLoadingDialog();
		//渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
		aa.setDuration(1000);
		view.startAnimation(aa);
		aa.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (null==uridata){
					autoLogin();
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});

	}
	public void startActivitys(){
		Uri uridata = this.getIntent().getData();
		if (null!=uridata) {
		
		final String mydata = uridata.getQueryParameter("data");
				String session = SharedPreferenceUtil.getString("session", null);
				if (uridata!=null) {
					if (!TextUtils.isEmpty(session)) {
						Intent intent = new Intent(SplashAcivity.this, MainActivity.class);
						startActivity(intent);
					} else if (TextUtils.isEmpty(session))  {
						Intent intent = new Intent(SplashAcivity.this, LoginActivity.class);
						startActivity(intent);
					}
					finish();
				}  
				
		}
	}
	public void autoLogin(){
		SharedPreferences preferences = getSharedPreferences(
				SHAREDPREFERENCES_NAME, MODE_PRIVATE);
		// 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
		boolean isFirstIn = preferences.getBoolean("isFirstIn", true);
		if(isFirstIn){
			Intent intent = new Intent(SplashAcivity.this,GuideActivity.class);
			startActivity(intent);
			finish();
		}else{
			long expiresInTime = SharedPreferenceUtil.getLong("expires_in", 0);
			long currentTime = System.currentTimeMillis();
			String session = SharedPreferenceUtil.getString("session","");

			if(expiresInTime!=0){
				if(expiresInTime-currentTime>0){
				//startMainActivity();
					loginRequest();
				}else {
					startLoginActivity();
				}
				return;
			}
			if(!TextUtils.isEmpty(session)){
				loginRequest();

			}else {
				startLoginActivity();
			}
		}

	}

	@Override
	public void onSuccess(Result entity) {
		// TODO Auto-generated method stub
		closeLoadingDialog();
		if (entity instanceof LoginRegisterResult) {
			if (entity.resultCode != 1) {
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
				finish();
				return;
			}
			LoginRegisterResult logins = (LoginRegisterResult) entity;
			String url = /*getAvatarUrl(logins.data.getUser().getUserId(),*/logins.data.getUser().headPicFileName;//);
			SharedPreferenceUtil.putString(logins.data.getUser().getUserId() + "head_url", url);
			SharedPreferenceUtil.putString("telephone", logins.data.getUser().getTelephone());
			SharedPreferenceUtil.putString("usertype",Constants.USERTYPE);
			SharedPreferenceUtil.putString("username", logins.data.getUser().getName());

			LoginUtils.loginVerifyTwice(this,false);
		}
	}

	@Override
	public void onFailure(Exception e, String errorMsg,int s) {
		// TODO Auto-generated method stub
		ToastUtils.showToast(getString(R.string.connect_error));


		SharedPreferenceUtil.putString("session", "");
		 startLoginActivity();
		closeLoadingDialog();
	}
	@Override
	public void onSuccess(ArrayList response) {
		// TODO Auto-generated method stub
	}

	private void loginRequest(/*String phoneNum, String password*/) {
		boolean id = TextUtils.isEmpty(SharedPreferenceUtil.getString("id",""));
		boolean session = TextUtils.isEmpty(SharedPreferenceUtil.getString( "session", ""));//
		if (!id&& !session){
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("userId", SharedPreferenceUtil.getString( "id", ""));
			params.put("serial", SharedPreferenceUtil.getString( "mRegId", ""));
			params.put("access_token", SharedPreferenceUtil.getString( "session", ""));
			final String userType = Constants.USERTYPE;
			new HttpManager().post(this, Constants.USER_LORGIN_AUTO + "", LoginRegisterResult.class,
					params, this,
					false, 1);
		}else {
			startLoginActivity();
		}
	}
	public void startLoginActivity(){
		Intent intent = new Intent(SplashAcivity.this, LoginActivity.class);
		intent.putExtra("login", "login");
		startActivity(intent);
		finish();
	}
}
