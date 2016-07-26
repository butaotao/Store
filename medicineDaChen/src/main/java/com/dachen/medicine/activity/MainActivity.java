package com.dachen.medicine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.medicine.R;
import com.dachen.medicine.app.Constants;
import com.dachen.medicine.app.MyConstants;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.SpUtil;
import com.dachen.medicine.common.utils.VersionUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.entity.ResultData;
import com.dachen.medicine.entity.VersionInfo;
import com.dachen.medicine.fragment.FragmentUtils;
import com.dachen.medicine.fragment.HomeFragment;
import com.dachen.medicine.fragment.QRCodeFragment;
import com.dachen.medicine.fragment.SettingFragment;
import com.dachen.medicine.logic.ScaningData;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;
import com.dachen.medicine.service.VersionUpdateService;
import com.dachen.medicine.view.MessageDialog;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @des 打开程序的主界面进来的时候需要判断程序是否设置过密码
 * 
 */
public class MainActivity extends BaseActivity implements OnHttpListener,
		OnClickListener {

	private int fragment_index = 0;
	protected List<Fragment> fragments;
	int tab = -1;
	ImageView mainActivity_settingsfragment_btn;
	ImageView mainActivity_home_btn;
	TextView mainActivity_home_txt;
	TextView mainActivity_settingsfragment_txt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		if (fragments == null) {
			fragments = new ArrayList<Fragment>();
		}
		if (savedInstanceState == null) {
			FragmentUtils.changeFragment(getSupportFragmentManager(),
					R.id.fragment_container, fragments, fragment_index);
		}
		init();
		UpdateConfig.setDebug(false); // 正式版设置为false，调试
		UmengUpdateAgent.setUpdateCheckConfig(false); // 正式版设置为false，如果正确完成不会出现任何提示，否则会以如下的toast提示您。
		UmengUpdateAgent.setUpdateOnlyWifi(false); // 正式版设置为false，需要在任意网络环境下都进行更新自动提醒
		UmengUpdateAgent.setUpdateAutoPopup(true);
		UmengUpdateAgent.update(this);
		mainActivity_settingsfragment_btn = (ImageView) findViewById(R.id.mainActivity_settingsfragment_btn);
		mainActivity_home_btn = (ImageView) findViewById(R.id.mainActivity_home_btn);
		// mainActivity_settingsfragment_btn.setBackgroundResource(R.drawable.home_gray);
		// mainActivity_home_btn.setBackgroundResource(R.drawable.settingsfragment_btn_pressed);
		mainActivity_home_txt = (TextView) findViewById(R.id.mainActivity_home_txt);
		mainActivity_settingsfragment_txt = (TextView) findViewById(R.id.mainActivity_settingsfragment_txt);
		mainActivity_home_txt.setTextColor(getResources().getColor(
				R.color.color_30B7A5));
		mainActivity_settingsfragment_txt.setTextColor(getResources().getColor(
				R.color.gray));

		mainActivity_home_btn
				.setBackgroundResource(R.drawable.home_gray_pressed);
		mainActivity_settingsfragment_btn
				.setBackgroundResource(R.drawable.settingsfragment_btn);
		String login = getIntent().getStringExtra("login");
		if (!TextUtils.isEmpty(login)) {
			regeisterXiaoMi();
		}

		getVersion();
	}

	@Override
	public void onFailure(Exception e, String errorMsg, int s) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub color_30B7A5

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ScaningData.sCanFlag = -1;
		ScaningData.setlistdrug(null);
	}

	private void init() {
		HomeFragment homeFragment = new HomeFragment();
		QRCodeFragment qrfragment = new QRCodeFragment();
		SettingFragment settingsfragment = new SettingFragment();
		fragments.add(homeFragment);
		fragments.add(qrfragment);
		fragments.add(settingsfragment);

		FragmentUtils.changeFragment(getSupportFragmentManager(),
				R.id.fragment_container, fragments, fragment_index);
		
		

		/* ##################手势锁增加开始###########################*** */
		// activity一创建需将程序置为当前程序
		sp.edit().putBoolean(MyConstants.ISCURRENTAPPLICATION, true).commit();

		String passwordStr = SpUtil.getGesturePassword(MainActivity.this);// 获取密码

		// 判断是否进行过密码设置
		if (passwordStr == "") {// 未设置过密码

			// intent = new Intent(SplashActivity.this, SetLockActivity.class);
		} else {// 设置过密码
			Intent intent = new Intent(MainActivity.this,
					UnlockActivity.class);
			startActivity(intent);
		}
		/* ##################手势锁增加结束###########################*** */

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	@OnClick(R.id.mainActivity_settingsfragment_layout)
	public void clicks() {
		fragment_index = 2;
		FragmentUtils.changeFragment(getSupportFragmentManager(),
				R.id.fragment_container, fragments, fragment_index);
		mainActivity_home_btn.setBackgroundResource(R.drawable.home_gray);
		mainActivity_settingsfragment_btn
				.setBackgroundResource(R.drawable.settingsfragment_btn_pressed);
		mainActivity_home_txt.setTextColor(getResources()
				.getColor(R.color.gray));
		mainActivity_settingsfragment_txt.setTextColor(getResources().getColor(
				R.color.color_30B7A5));
	}

	@OnClick(R.id.mainActivity_home_layout)
	public void clickHome() {
		fragment_index = 0;
		FragmentUtils.changeFragment(getSupportFragmentManager(),
				R.id.fragment_container, fragments, fragment_index);
		mainActivity_home_btn
				.setBackgroundResource(R.drawable.home_gray_pressed);
		mainActivity_settingsfragment_btn
				.setBackgroundResource(R.drawable.settingsfragment_btn);
		// ToastUtils.showToast("===clickHome====");
		mainActivity_home_txt.setTextColor(getResources().getColor(
				R.color.color_30B7A5));
		mainActivity_settingsfragment_txt.setTextColor(getResources().getColor(
				R.color.gray));
	}

	@OnClick(R.id.mainActivity_qrfragment_btn)
	public void clickQW() {

		Intent intent = new Intent(this, MipcaActivityCaptures.class);
		intent.putExtra("code", "erweima");
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	public void onSuccess(ArrayList response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(Result response) {
		// TODO Auto-generated method stub
		if (response instanceof ResultData) {
			ResultData data = (ResultData) response;
			if (1==data.resultCode) {
				
			}
		}
	}
	public void regeisterXiaoMi(){
		HashMap<String, String> infaces = new HashMap<String, String>();
		infaces.put("interface1", Constants.XIAOMI);
		new HttpManager().post(infaces, ResultData.class,
				Params.getReginsterXiaoMiReceiverRe(this,SharedPreferenceUtil.getString("id", ""),
						SharedPreferenceUtil.getString("mRegId", ""),
						SharedPreferenceUtil.getString("session", ""),Constants.USERTYPE), this,
				 1,80);
	}

	/**
	 * 获取版本号
	 */
	private void getVersion() {
		new HttpManager().post(this, Constants.GET_VERSION, VersionInfo.class,
				Params.getVersionParams(this), new OnHttpListener<Result>() {
					@Override
					public void onSuccess(Result response) {
						if (response.getResultCode() == 1) {
							if (response instanceof VersionInfo) {
								final VersionInfo versionInfo = (VersionInfo) response;
								if (versionInfo.data == null) {
									return;
								}
								if (VersionUtils.hasNewVersion(MainActivity.this, versionInfo.data.version)) {
									new Handler().postDelayed(new Runnable() {
										@Override
										public void run() {
											final MessageDialog messageDialog = new MessageDialog(MainActivity.this, "取消", "马上更新", versionInfo.data.info);
											messageDialog.setBtn1ClickListener(new View.OnClickListener() {
												@Override
												public void onClick(View v) {
													messageDialog.dismiss();
												}
											});
											messageDialog.setBtn2ClickListener(new View.OnClickListener() {
												@Override
												public void onClick(View v) {
													messageDialog.dismiss();
													Intent intent = new Intent(MainActivity.this, VersionUpdateService.class);
													intent.putExtra("desc", getString(R.string.app_name));
													intent.putExtra("fileName", "medicine_release_v" + versionInfo.data.version + ".apk");
													intent.putExtra("url", versionInfo.data.downloadUrl);
													startService(intent);
												}
											});
											messageDialog.show();
										}
									}, 1000);
								}
							}
						}


					}

					@Override
					public void onSuccess(ArrayList<Result> response) {

					}

					@Override
					public void onFailure(Exception e, String errorMsg, int s) {

					}
				},
				false, 1);
	}
}
