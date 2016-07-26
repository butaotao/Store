package com.dachen.medicine.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dachen.medicine.R;
import com.dachen.medicine.common.utils.SpUtil;
import com.dachen.medicine.view.LockPatternView;

/**
 * @author znouy
 * @desc 修改密码界面
 * @data 2015-12-14
 */
public class UpdataLockActivity extends Activity implements OnClickListener {
	private LockPatternView mLockPatternView;
	private TextView mTv_title;
	private RelativeLayout mRl_back;
	/**
	 * 本次绘制的密码
	 */
	protected String mPassword;
	private TextView tv_updatelock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_undatalockactivity);

		initView();
	
		mLockPatternView
				.setOnPatterChangeLisrener(new LockPatternView.OnPatterChangeLisrener() {
					// 获取sp中存储的密码
					String password = SpUtil
							.getGesturePassword(UpdataLockActivity.this);

					@Override
					public void getStringPassword(String password) {
						// 获取本次绘制的密码
						mPassword = password;
					}

					@Override
					public boolean isPassword() {
						// 判断存在密码和本次绘制的密码是否一致
						if (mPassword.equals(password)) {
							Toast.makeText(UpdataLockActivity.this, "密码正确",
									Toast.LENGTH_SHORT).show();
							// 跳转到设置密码界面
							Intent intent = new Intent(UpdataLockActivity.this,
									SetLockActivity.class);
							startActivity(intent);
							finish();

						} else {
							tv_updatelock.setText("原手势密码错误!");
							tv_updatelock.setTextColor(Color.RED);
							Toast.makeText(UpdataLockActivity.this, "密码不正确",
									Toast.LENGTH_SHORT).show();
						}
						return false;
					}
				});

	}

	private void initView() {
		mTv_title = (TextView) findViewById(R.id.tv_title);
		mLockPatternView = (LockPatternView) findViewById(R.id.updatalock_lockview);

		tv_updatelock = (TextView) findViewById(R.id.tv_updatelock);

		mRl_back = (RelativeLayout) findViewById(R.id.rl_back);
		mRl_back.setOnClickListener(this);

		mTv_title.setText("修改手势密码");

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.rl_back:
			Toast.makeText(this, "shohah", 1).show();
			finish();

			break;

		}
	}
}
