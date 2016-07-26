package com.dachen.medicine.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.dachen.medicine.R;
import com.dachen.medicine.app.MyConstants;
import com.dachen.medicine.common.utils.SpUtil;
import com.dachen.medicine.view.LockPatternView;

/**
 * @author znouy 密码解锁界面
 */
public class UnlockActivity extends Activity {
	private LockPatternView mLockPatternView;

	/**
	 * 本次绘制的密码
	 */
	protected String mPasswordStr;
	private SharedPreferences sp;

	private TextView mTv_check_unlock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_unlockactivity);

		sp = getSharedPreferences(MyConstants.SPFILENAME, 0);
		initView();

		mLockPatternView
				.setOnPatterChangeLisrener(new LockPatternView.OnPatterChangeLisrener() {
					// 获取sp中存储的密码
					String password = SpUtil
							.getGesturePassword(UnlockActivity.this);

					@Override
					public void getStringPassword(String password) {
						// 获取本次绘制的密码
						mPasswordStr = password;
					}

					@Override
					public boolean isPassword() {
						// 判断存在密码和本次绘制的密码是否一致
						if (mPasswordStr.equals(password)) {
							Toast.makeText(UnlockActivity.this, "密码正确",
									Toast.LENGTH_SHORT).show();

							UnlockActivity.this.finish();

						} else {
							mTv_check_unlock.setText("原手势密码错误");
							mTv_check_unlock.setTextColor(Color.RED);
						}
						return false;
					}
				});

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// 防止进入应用程序按home键回来时需进行两次解锁
		finish();// 关闭本页面
	}

	private void initView() {
		mTv_check_unlock = (TextView) findViewById(R.id.tv_check_unlock);
		mLockPatternView = (LockPatternView) findViewById(R.id.unlock_lockview);

	}

	@Override
	public void onBackPressed() {
		// 点击了返回键,跳转到桌面
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

		// 退回到桌面的时候将当前应用程序标记置为false
		sp.edit().putBoolean(MyConstants.ISCURRENTAPPLICATION, false).commit();
		super.onBackPressed();
	}

}