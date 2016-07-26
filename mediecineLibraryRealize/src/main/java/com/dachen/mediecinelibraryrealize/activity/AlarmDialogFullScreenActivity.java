package com.dachen.mediecinelibraryrealize.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.dachen.medicine.common.utils.Alarm;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.entity.AlarmBusiness;

/**
 * 锁屏状态下显示的对话框
 * 
 * @author gaozhuo
 * @date 2015年12月14日
 */
public class AlarmDialogFullScreenActivity extends BaseActivity implements OnClickListener{
 
	TextView mContent;

	private Alarm mAlarm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_dialog_fullscreen); 
		this.findViewById(R.id.left_btn).setOnClickListener(this);
		this.findViewById(R.id.right_btn).setOnClickListener(this);
		// 当处于锁屏状态时，允许此界面出现在锁屏界面之上，即不用解锁也能显示此界面
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

		// 设置点击对话框区域外不关闭对话框
		setFinishOnTouchOutside(false);

		initData(getIntent());

		updateView();

	}

	protected int getLayoutResId() {
		return R.layout.activity_alarm_dialog_fullscreen;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);

		initData(getIntent());

		updateView();
	}

	private void updateView() {
		if (mAlarm != null && mAlarm.drugRemind != null) {
			mContent.setText(mAlarm.drugRemind.patientName + "的用药：" + mAlarm.drugRemind.drugName);
		}
	}

	private void initData(Intent intent) {
		if (intent == null) {
			return;
		}
		mAlarm = (Alarm) intent.getSerializableExtra("alarm");
	}
 

	private void close() {
		finish();
		overridePendingTransition(0, 0);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.left_btn) {
			close();
		}else if (v.getId() == R.id.right_btn) {
			if (mAlarm != null) {
				// 重新设置该闹钟，使之在下一个时间点提醒，其目的是取消该闹钟延迟提醒
				AlarmBusiness.setAlarm(this, mAlarm);

				AlarmBusiness.cancelNotification(this, mAlarm);

				finish();
			}
		}
	}

}
