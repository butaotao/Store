package com.dachen.medicine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dachen.medicine.R;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.SpUtil;
import com.dachen.medicine.view.UISwitchButton;

/**
 * @author znouy 账户安全界面
 */
public class AccountSafeActivity extends BaseActivity implements
		OnClickListener {

	private TextView mTv_title;
	private RelativeLayout mRl_back;
	private RelativeLayout mRl_updatepwd;
	private RelativeLayout mRl_gesturetoggle_accountsafe;
	private RelativeLayout mR1_updategesture_accountsafe;
	private Intent intent;
	private UISwitchButton mIv_getsture_toggle;
	private boolean mIs_gesture_toggle_on;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_accountsafe);

		mIs_gesture_toggle_on = SharedPreferenceUtil.getBoolean(
				"IS_GESTURE_TOGGLE_ON", false);

		initView();

		initEvent();

	}

	private void initEvent() {
		mIv_getsture_toggle
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean isChecked) {
						// 获取密码
						String passwordStr = SpUtil
								.getGesturePassword(AccountSafeActivity.this);// 获取密码
						// 开关是开且没设置密码，进入设置密码锁界面，同时显示修改密码锁view
						if (isChecked && passwordStr == "") {
							Toast.makeText(AccountSafeActivity.this,
									"开关打开,进入设置密码界面", 0).show();
							SharedPreferenceUtil.putBoolean(
									"IS_GESTURE_TOGGLE_ON", true);
							// 进入设置密码锁界面，同时显示修改密码锁view
							mR1_updategesture_accountsafe
									.setVisibility(View.VISIBLE);

							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {

									intent = new Intent(
											AccountSafeActivity.this,
											SetLockActivity.class);
									startActivity(intent);
								}
							}, 5);

							// 开关是关，显示关闭手势密码锁
						} else if (!isChecked && passwordStr != "") {
							Toast.makeText(AccountSafeActivity.this,
									"开关关闭，关闭手势密码", 0).show();

							intent = new Intent(AccountSafeActivity.this,
									CloseLockActivity.class);
							startActivity(intent);

							// 获取密码
							String passwordStr1 = SpUtil
									.getGesturePassword(AccountSafeActivity.this);// 获取密码
							if (passwordStr1 == "") {

								mR1_updategesture_accountsafe
										.setVisibility(View.GONE);
								SharedPreferenceUtil.putBoolean(
										"IS_GESTURE_TOGGLE_ON", false);
							}
						}
					}

				});

		if (mIs_gesture_toggle_on) {
			mIv_getsture_toggle.setChecked(true);

		} else {
			mIv_getsture_toggle.setChecked(false);

		}
	}

	private void initView() {
		// title_bar
		mTv_title = (TextView) findViewById(R.id.tv_title);
		mRl_back = (RelativeLayout) findViewById(R.id.rl_back);
		mRl_back.setOnClickListener(this);
		mTv_title.setText("账户安全");
		// 修改密码
		mRl_updatepwd = (RelativeLayout) findViewById(R.id.rl_updatepwd_accountsafe);
		mRl_updatepwd.setOnClickListener(this);
		// 手势密码锁
		mRl_gesturetoggle_accountsafe = (RelativeLayout) findViewById(R.id.rl_gesturetoggle_accountsafe);
		mIv_getsture_toggle = (UISwitchButton) findViewById(R.id.iv_getsture_toggle);

		// 修改手势密码
		mR1_updategesture_accountsafe = (RelativeLayout) findViewById(R.id.rl_updategesture_accountsafe);
		mR1_updategesture_accountsafe.setVisibility(View.GONE);
		mR1_updategesture_accountsafe.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// 判断是否有密码
		// 没有密码，隐藏修改手势密码的view
		// 否则显示
		isShowUpdateGestureView();
		super.onResume();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.rl_back:// 返回

			isShowUpdateGestureView();
			finish();

			break;
		case R.id.rl_updatepwd_accountsafe:// 修改密码
			// 进入修改密码界面
			intent = new Intent(this, ModifyPasswordActivity.class);
			startActivity(intent);
			break;

		case R.id.rl_updategesture_accountsafe:// 修改密码
			// 前提是手势密锁开关是开的，点击进入修改密码锁界面
			intent = new Intent(this, UpdataLockActivity.class);
			startActivity(intent);
			break;

		}
	}

	/**
	 * 是否显示修改密码的view
	 */
	private void isShowUpdateGestureView() {
		// 获取密码
		String passwordStr = SpUtil
				.getGesturePassword(AccountSafeActivity.this);// 获取密码
		if (passwordStr == "") {// 没有密码，隐藏修改手势密码的view,开关是关
			mR1_updategesture_accountsafe.setVisibility(View.GONE);
			mIv_getsture_toggle.setChecked(false);
		} else {
			mR1_updategesture_accountsafe.setVisibility(View.VISIBLE);
			// 开关为开
			mIv_getsture_toggle.setChecked(true);
		}

	}

}