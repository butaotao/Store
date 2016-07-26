package com.dachen.medicine.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.R;
import com.dachen.medicine.app.MedicineApplication;
import com.dachen.medicine.app.MyConstants;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.MyLog;
import com.dachen.medicine.common.utils.SpUtil;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.receiver.HomeWatchReceiver;
import com.dachen.medicine.view.DialogBank;

public class BaseActivity extends ConfigAcitivity implements OnClickListener,
		OnHttpListener {

	/* ##################手势锁增加开始###########################*** */
	HomeWatchReceiver receiver;
	SharedPreferences sp;
	/** 运行的程序是当前的app */
	protected boolean isCurrentApp = true;
	private ActivityManager activityManager;
	/* ##################手势锁增加结束###########################*** */

	protected View mLoadingView;
	private AnimationDrawable mAnimationDrawable;
	DialogBank dialog;
	TextView tv_title;
	RelativeLayout iv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MedicineApplication.app.getActivityManager().pushActivity(this);

		/* ##################手势锁增加开始###########################*** */
		sp = getSharedPreferences(MyConstants.SPFILENAME, 0);

		sp.edit().putBoolean("ISRUNBACK", false).commit();

		activityManager = (ActivityManager) this
				.getSystemService(Context.ACTIVITY_SERVICE);

		// 注册广播接收者
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		receiver = new HomeWatchReceiver();
		registerReceiver(receiver, intentFilter);
		/* ##################手势锁增加结束###########################*** */

	}

	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
		setUpViews();
	}

	@Override
	public void setContentView(View view) {
		// TODO Auto-generated method stub
		super.setContentView(view);
		setUpViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
		isGotoUnlock();
	}

	/* ##################手势锁增加开始###########################*** */
	/**
	 * 是否进入锁屏界面
	 */
	private void isGotoUnlock() {
		// isRunMyApp();
		// MyLog.log("开关是否打开isToggleOn");
		// if(isToggleOn){ TODO 设置密码的开关是打开的
		String passwordStr = SpUtil.getGesturePassword(this);// 获取密码

		// 1、获取是否按了home键的标记
		boolean isPressHomeKey = sp.getBoolean(MyConstants.SHORT_HOME_KEY_FLAG,
				false);
		// 将保存按了home键的信息置为false，防止密码输入正确activity在此调用onResume时又跳转到设置密码界面
		sp.edit().putBoolean(MyConstants.SHORT_HOME_KEY_FLAG, false).commit();
		MyLog.log("onResume ：isPressHomeKey=" + isPressHomeKey);
		LogUtils.burtLog("====onResume===========");
		// 2、获取是否按了锁屏键
		boolean isPressLockKey = sp.getBoolean(
				MyConstants.LOCK_SCREEN_KEY_FLAG, false);
		// 将保存按锁屏键的信息置为false，防止密码输入正确activity在此调用onResume时又跳转到设置密码界面
		sp.edit().putBoolean(MyConstants.LOCK_SCREEN_KEY_FLAG, false).commit();
		MyLog.log("onResume ：isPressLockKey=" + isPressLockKey);

		// 3、获取sp中保存的是否是当前程序运行的标记,默认是
		isCurrentApp = sp.getBoolean(MyConstants.ISCURRENTAPPLICATION, false);
		if (!isCurrentApp) {// 只要进入方法就将
			// isCurrentApp置为true，防止本应用程序界面之间的切换导致isCurrentApp为false，加if是防止多次提交，提高效率
			sp.edit().putBoolean(MyConstants.ISCURRENTAPPLICATION, true)
					.commit();
		}
		System.out.println("onResume ：isCurrentApp=" + isCurrentApp);

		boolean isRunBackground = sp.getBoolean("ISRUNBACK", false);
		MyLog.log("isRunBackground=" + isRunBackground);
		// 如果按了home键或按了锁屏键或不是本应用程序，且设置过密码，跳转到设置密码界面
		if ((isPressHomeKey && passwordStr != "")
				|| (isPressLockKey && passwordStr != "")
				|| (!isCurrentApp && passwordStr != "")
				|| (passwordStr != "" && isRunBackground)) {
			toUnlockActivity();
			sp.edit().putBoolean("ISRUNBACK", false).commit();
		}
	}

	/**
	 * 跳转到解锁界面
	 */
	private void toUnlockActivity() {
		Intent intent = new Intent(this, UnlockActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		isRunBackground();
		super.onStop();

	}

	/**
	 * 当前程序是否运行在后台
	 */
	private void isRunBackground() {
		String packageName = getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return;

		for (RunningAppProcessInfo appProcess : appProcesses) {

			// MyLog.log("appProcess.processName=========================="+appProcess.processName);
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {// 后台
				sp.edit().putBoolean("ISRUNBACK", true).commit();
			}
		}

	}

	@Override
	protected void onDestroy() {
		// 解除广播接受者
		unregisterReceiver(receiver);
		System.out.println(this.getClass().getSimpleName() + ":onDestroy");
		super.onDestroy();
	}

	/* ##################手势锁增加结束###########################*** */

	public void showLoadingDialog() {
		if (mLoadingView == null) {
			mLoadingView = LayoutInflater.from(this).inflate(
					R.layout.loading_view, null);
			((ViewGroup) this.getWindow().getDecorView()).addView(mLoadingView);
		}
		final ImageView iv = (ImageView) mLoadingView
				.findViewById(R.id.iv_progress_bar);
		iv.setImageResource(R.drawable.xlv_loadmore_animation);
		mAnimationDrawable = (AnimationDrawable) iv.getDrawable();
		mAnimationDrawable.setOneShot(false);
		if (mAnimationDrawable != null) {
			mAnimationDrawable.start();
		}
		mLoadingView.setVisibility(View.VISIBLE);
	}

	public void closeLoadingDialog() {
		if (mLoadingView != null) {
			mLoadingView.setVisibility(View.GONE);
		}
		if (mAnimationDrawable != null) {
			mAnimationDrawable.stop();
		}
	}

	public void showNotNetDialog(DialogBank.OnClickListener banckName) {
		dialog = new DialogBank(this, banckName);
		dialog.show();
	}

	protected void setUpViews() {
		tv_title = (TextView) this.findViewById(R.id.tv_title);
		iv_back = (RelativeLayout) this.findViewById(R.id.rl_back);
	}

	public final void setTitle(int resid) {
		try {
			tv_title = (TextView) this.findViewById(R.id.tv_title);
			tv_title.setText(resid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		try {
			tv_title = (TextView) this.findViewById(R.id.tv_title);
			tv_title.setText(title);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		MedicineApplication.app.getActivityManager().popActivity(this);
		// this.mApplication.getActivityManager().finishActivity(this.getClass());
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.iv_back) {
			this.onBackPressed();
		}
	}

	/**
	 * 打开返回按钮
	 */
	public final void enableBack() {
		iv_back.setVisibility(View.VISIBLE);
		iv_back.setOnClickListener(this);
	}

	@Override
	public void onSuccess(Result response)   {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(ArrayList response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailure(Exception e, String errorMsg, int s) {
		// TODO Auto-generated method stub

	}
}
