package com.dachen.medicine.activity;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dachen.medicine.R;
import com.dachen.medicine.common.utils.MyLog;
import com.dachen.medicine.common.utils.SpUtil;
import com.dachen.medicine.view.LockPatternView;

/**
 * @author znouy 设置密码界面
 */
public class SetLockActivity extends Activity implements OnClickListener {

	private TextView setlock_tv;
	private LockPatternView setlock_lockview;
	private Button setlock_clearpassword;
	/** 提示图案被选中的点 */
	protected List<LockPatternView.GuideCell> mChosenGuidePattern = null;

	/**
	 * 是否是第一次输入密码
	 */
	protected boolean isFirst = true;
	protected String mPassword;
	private View[][] mPreviewViews = new View[3][3];
	private TextView mTv_title;
	private RelativeLayout mRl_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setlockactivity);

		initView();

		initPreviewViews();
		initEvent();

	}

	/**
	 * 2.初始化事件
	 */
	private void initEvent() {
		System.out.println("===SetLockActivity  initEvent");
		setlock_lockview
				.setOnPatterChangeLisrener(new LockPatternView.OnPatterChangeLisrener() {

					@Override
					public void getStringPassword(String password) {

						MyLog.log(" password" + password);
						if (isFirst) {// 是第一次输入密码
							mPassword = password;
							MyLog.log("mPassword" + mPassword);
							setlock_tv.setText("请确认手势密码");

							// TODO 显示提示图案上的点
							updatePreviewViews();

							isFirst = false;
							setlock_clearpassword.setVisibility(View.GONE);
						} else {// 不是第一次输入，确认密码
							MyLog.log(" 再次 password" + password);
							if (password.equals(mPassword)) {
								MyLog.log("mPassword" + mPassword);
								setPasswordToSp(password);
								
								SetLockActivity.this.finish();
							} else {

								// TODO 显示提示图案上的点
								// updatePreviewViews();
								Toast.makeText(SetLockActivity.this,
										"与上次绘制不一致，请重新设置", Toast.LENGTH_SHORT)
										.show();
								// mPassword = "";
								setlock_tv.setText("与上次绘制不一致，请重新绘制");
								setlock_tv.setTextColor(Color.RED);
								isFirst = false;
								setlock_clearpassword.setVisibility(View.GONE);
							}
						}

					}

					@Override
					public boolean isPassword() {

						return false;
					}
				});

	}

	/**
	 * @param password
	 *            将密码存储到sp的配置文件中
	 */
	protected void setPasswordToSp(String password) {
		SpUtil.setGesturePassword(SetLockActivity.this, password);

	}

	/**
	 * 2.初始化手势密码图案提示
	 */
	private void initPreviewViews() {
		mPreviewViews = new View[3][3];
		mPreviewViews[0][0] = findViewById(R.id.gesturepwd_setting_preview_0);
		mPreviewViews[0][1] = findViewById(R.id.gesturepwd_setting_preview_1);
		mPreviewViews[0][2] = findViewById(R.id.gesturepwd_setting_preview_2);
		mPreviewViews[1][0] = findViewById(R.id.gesturepwd_setting_preview_3);
		mPreviewViews[1][1] = findViewById(R.id.gesturepwd_setting_preview_4);
		mPreviewViews[1][2] = findViewById(R.id.gesturepwd_setting_preview_5);
		mPreviewViews[2][0] = findViewById(R.id.gesturepwd_setting_preview_6);
		mPreviewViews[2][1] = findViewById(R.id.gesturepwd_setting_preview_7);
		mPreviewViews[2][2] = findViewById(R.id.gesturepwd_setting_preview_8);
	}

	/**
	 * 更新提示密码图案状态
	 */
	private void updatePreviewViews() {
		MyLog.log("updatePreviewViews");

		if (mPassword != null) {
			mChosenGuidePattern = LockPatternView.stringToPattern(mPassword);
		}
		MyLog.log("result = " + mChosenGuidePattern.toString());
		if (mChosenGuidePattern == null)
			return;

		for (LockPatternView.GuideCell cell : mChosenGuidePattern) {
			MyLog.log("cell.getRow() = " + cell.getRow()
					+ ", cell.getColumn() = " + cell.getColumn());
			mPreviewViews[cell.getRow()][cell.getColumn()]
					.setBackgroundResource(R.drawable.gesture_create_grid_selected);

		}
	}

	/**
	 * 1.初始化View
	 */
	private void initView() {
		mTv_title = (TextView) findViewById(R.id.tv_title);
		setlock_tv = (TextView) findViewById(R.id.setlock_tv);
		setlock_lockview = (LockPatternView) findViewById(R.id.setlock_lockview);
		setlock_clearpassword = (Button) findViewById(R.id.setlock_clearpassword);

		mRl_back = (RelativeLayout) findViewById(R.id.rl_back);
		mRl_back.setOnClickListener(this);

		mTv_title.setText("设置手势密码");
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.rl_back:
			Toast.makeText(this, "jahhahhah", 1).show();
			finish();

			break;
		case R.id.setlock_lockview:
			break;
		case R.id.setlock_clearpassword:
			mPassword = "";
			isFirst = true;
			setlock_clearpassword.setVisibility(View.GONE);
			break;

		default:
			break;
		}

	}

}
