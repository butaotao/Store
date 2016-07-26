package com.dachen.medicine.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dachen.medicine.R;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.SpUtil;
import com.dachen.medicine.view.LockPatternView;

/**
 * @author znouy
 * @desc 关闭密码锁界面
 * @data 2015-12-14
 */
public class CloseLockActivity extends Activity implements OnClickListener {
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
							.getGesturePassword(CloseLockActivity.this);

					@Override
					public void getStringPassword(String password) {
						// 获取本次绘制的密码
						mPassword = password;
					}

					@Override
					public boolean isPassword() {
						// 判断存在密码和本次绘制的密码是否一致
						if (mPassword.equals(password)) {
							Toast.makeText(CloseLockActivity.this, "密码正确",
									0).show();
							// 清除密码，将sp中保存的密码置为空
							SpUtil.setGesturePassword(CloseLockActivity.this,
									"");
							finish();

						} else {
							tv_updatelock.setText("原手势密码错误!");
							tv_updatelock.setTextColor(Color.RED);

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

		mTv_title.setText("关闭手势密码");

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.rl_back:
			Toast.makeText(this, "将开关职位开", 0).show();
			//表示没有关闭密码，
			SharedPreferenceUtil.putBoolean(
					"IS_GESTURE_TOGGLE_ON", true);
			finish();

			break;

		}
	}
}
