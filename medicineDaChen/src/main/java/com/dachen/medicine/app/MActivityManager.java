package com.dachen.medicine.app;

import java.util.Stack;

import android.app.Activity;
import android.util.Log;

import com.dachen.medicine.common.utils.LogUtils;

/**
 * 
 * @描述�? @activity管理工具�?
 * @作�?�? @蒋诗�?
 * @创建时间�? @2014-12-11
 */

public class MActivityManager {

	public static final String TAG = "ActivityManager";

	private static Stack<Activity> sActivityStack;
	private static MActivityManager instance;

	private MActivityManager() {

	}

	public static MActivityManager getInstance() {
		if (instance == null) {
			instance = new MActivityManager();
		}
		return instance;
	}

	public final void addActivity(Activity activity) {
		if (sActivityStack == null) {
			sActivityStack = new Stack<Activity>();
		}
		sActivityStack.add(activity);
	}

	public void popActivity(Activity activity) {
		Log.v(TAG, "pop activity=" + activity.getClass().getSimpleName());
		if (activity != null) {
			// 在从自定义集合中取出当前Activity时，也进行了Activity的关闭操�?
			activity.finish();
			sActivityStack.remove(activity);
			activity = null;
		}
	}

	// 将当前Activity推入栈中
	public void pushActivity(Activity activity) {
		if (sActivityStack == null) {
			sActivityStack = new Stack<Activity>();
		}
		Log.v(TAG, "push activity=" + activity.getClass().getSimpleName());
		sActivityStack.add(activity);
	}

	// 获得当前栈顶Activity
	public Activity currentActivity() {
		Activity activity = null;
		if (!sActivityStack.empty())
			activity = sActivityStack.lastElement();
		return activity;
	}

	// �?��栈中�?��Activity
	public void popAllActivityExceptOne(Class cls) {
		while (true) {
			Activity activity = currentActivity();
			if (activity == null) {
				break;
			}
			if (activity.getClass().equals(cls)) {
				break;
			}
			popActivity(activity);
		}
	}

	public final Activity topRunningActivity() {
		return sActivityStack.lastElement();
	}

	public final void finishActivity() {
		if (sActivityStack == null) {
			sActivityStack = new Stack<Activity>();
		}
		Activity activity = sActivityStack.lastElement();
		finishActivity(activity);
	}

	public final void finishActivity(Activity activity) {
		if (activity != null) {
			sActivityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	public final void removeActivity(Activity activity) {
		if (activity != null) {
			sActivityStack.remove(activity);
			activity = null;
		}
	}

	public final void finishAppointActivity(Class<?> cls) {
		for (Activity activity : sActivityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	public final void finishAllActivity() {
		for (int i = 0, size = sActivityStack.size(); i < size; i++) {
			if (null != sActivityStack.get(i)) {
				sActivityStack.get(i).finish();
			}
		}
		sActivityStack.clear();
	}

	// public final void finishAllActivityExceptOne(Class clasz) {
	// for (int i = 0, size = sActivityStack.size() - 1; i < size; i++) {
	// if (null != sActivityStack.get(i)) {
	// if(sActivityStack.get(i).getClass().equals(clasz)){
	// sActivityStack.get(i).finish();
	// }
	// }
	// }
	// }

	public final void finishAllActivityExceptMainActivity() {
		for (int i = 0, size = sActivityStack.size() - 1; i < size; i++) {
			if (null != sActivityStack.get(i)) {
				if (!sActivityStack.get(i).getClass().getName()
						.contains(".MainActivity")) {
					sActivityStack.get(i).finish();
					LogUtils.burtLog("======="+sActivityStack.get(i).getClass().getName());
				}
			}
		}
	}

	public final void finishAllActivityExceptOne(Class clazz) {
		for (int i = 0, size = sActivityStack.size() - 1; i < size; i++) {
			if (null != sActivityStack.get(i)) {
				if (!sActivityStack.get(i).getClass().getName()
						.equals(clazz.getName())) {
					sActivityStack.get(i).finish();
				}
			}
		}
	}

	public final void finishActivity(Class clazz) {
		for (int i = 0, size = sActivityStack.size() - 1; i < size; i++) {
			if (null != sActivityStack.get(i)) {
				if (sActivityStack.get(i).getClass().getName()
						.equals(clazz.getName())) {
					sActivityStack.get(i).finish();
				}
			}
		}
	}

	public void clear() {
		if (instance != null) {
			instance = null;
		}
	}

}
