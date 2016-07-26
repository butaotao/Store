package com.dachen.medicine.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.dachen.medicine.app.MyConstants;
import com.dachen.medicine.common.utils.MyLog;

public class HomeWatchReceiver extends BroadcastReceiver {

	// SharedPreferences sp;
	//
	// // 广播的类型
	// private String receiveAction;
	//
	// /** 手机外置键的广播 */
	// final String action1 = Intent.ACTION_CLOSE_SYSTEM_DIALOGS;
	// /** 外拨电话的广播 */
	// final String action2 = Intent.ACTION_NEW_OUTGOING_CALL;
	//
	// // 外置键按键的类型
	// final String SYSTEM_DIALOG_REASON_KEY = "reason";
	// /** 短按Home键 */
	// final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
	// final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
	// /** 锁屏 */
	// final String SYSTEM_DIALOG_REASON_LOCK = "lock";
	// final String SYSTEM_DIALOG_REASON_ASSIST = "assist";
	//
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// receiveAction = intent.getAction();
	//
	// if (action1.equals(receiveAction)) {// 监听到按键的广播
	// sp = context.getSharedPreferences("homeinfo", 0);
	// String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
	//
	// if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {// 短按Home键
	// // 保存按了home键信息到sp中
	// sp.edit().putBoolean("short_home_key_flag", true).commit();
	// } else if (SYSTEM_DIALOG_REASON_RECENT_APPS.equals(reason)) {// 长按Home键或者
	// // activity切换键
	// } else if (SYSTEM_DIALOG_REASON_LOCK.equals(reason)) {// 锁屏
	//
	// sp.edit().putBoolean("lock", false).commit();
	// } else if (SYSTEM_DIALOG_REASON_ASSIST.equals(reason)) {// samsung长按Home键
	//
	// }
	//
	// }
	// if (action2.equals(receiveAction)) {// 外拨电话的广播
	// sp = context.getSharedPreferences("phonestate", 0);
	// }
	// }
	// }

	@Override
	public void onReceive(Context context, Intent intent) {
		/** 外置键按键的类型 */
		final String REASON = "reason";
		final String HOME_KEY = "homekey";
		final String LOCK = "lock";

		SharedPreferences sp = context.getSharedPreferences(
				MyConstants.SPFILENAME, 0);

		String action = intent.getAction();
		MyLog.log("onReceive: action: " + action);
		if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {

			String reason = intent.getStringExtra(REASON);
			MyLog.log("reason: " + reason);

			if (HOME_KEY.equals(reason)) {// 短按Home键

				sp.edit().putBoolean(MyConstants.SHORT_HOME_KEY_FLAG, true)
						.commit();
			} else if (LOCK.equals(reason)) {// 锁屏
				sp.edit().putBoolean(MyConstants.LOCK_SCREEN_KEY_FLAG, true)
				.commit();
			}
		}
	}
}
