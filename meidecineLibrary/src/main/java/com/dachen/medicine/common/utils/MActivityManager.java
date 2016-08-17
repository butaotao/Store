package com.dachen.medicine.common.utils;

import android.app.Activity;
import android.util.Log;

import com.dachen.medicine.config.AppConfig;

import java.util.Stack;


public class MActivityManager {

	public static final String TAG = "ActivityManager";

	private static Stack<Activity> sActivityStack;
	private static MActivityManager instance;

	private MActivityManager() {
		sActivityStack = new Stack<Activity>();
	}

	public static MActivityManager getInstance() {
		if (instance == null) {
			instance = new MActivityManager();
		}
		return instance;
	}

	public final void addActivity(Activity activity) {
//		if (sActivityStack == null) {
//			sActivityStack = new Stack<Activity>();
//		}
		sActivityStack.add(activity);
	}

	public void popActivity(Activity activity) {
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
	public final void removeActivitys(Activity activity) {
		if (activity != null) {
			if (sActivityStack.contains(activity)){
				sActivityStack.remove(activity);
				activity = null;
			}

		}
	}
	public final void finishAppointActivity(Class<?> cls) {
		for (Activity activity : sActivityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
				break;
			}
		}
	}
	public void popTo( Activity activity) {//SearchDoctorResultActivity SearchDoctorDeptResultActivity
		Log.v(TAG, "pop activity=" + activity.getClass().getSimpleName());
		if (activity != null&&containActivity()) {
			// 在从自定义集合中取出当前Activity时，也进行了Activity的关闭操�?

			for (int i=sActivityStack.size()-1;i>0;i--){
				if (null!=sActivityStack.get(i)){
					if (!sActivityStack.get(i).getClass().getName().contains("DoctorFriendActivity")
							 ){
						sActivityStack.get(i).finish();
					}else {
						break;
					}
				}
			}
		}
	}
	public boolean containActivity(){
		for (int i=0;i<sActivityStack.size();i++){
			if (null!=sActivityStack.get(i)){
				if (sActivityStack.get(i).getClass().getName().contains("DoctorFriendActivity")
						){
					return true;
				}
			}
		}
		return  false;
	}

	public void popTos( Activity activity) {//SearchDoctorResultActivity SearchDoctorDeptResultActivity
		Log.v(TAG, "pop activity=" + activity.getClass().getSimpleName());
		if (activity != null&&containActivitys()) {
			// 在从自定义集合中取出当前Activity时，也进行了Activity的关闭操�?

			for (int i=sActivityStack.size()-1;i>0;i--){
				if (null!=sActivityStack.get(i)){
					if (!sActivityStack.get(i).getClass().getName().contains("EidtColleagueActivity")
							){
						sActivityStack.get(i).finish();
					}else {
						break;
					}
				}
			}
		}
	}
	public boolean containActivitys(){
		for (int i=0;i<sActivityStack.size();i++){
			if (null!=sActivityStack.get(i)){
				if (sActivityStack.get(i).getClass().getName().contains("EidtColleagueActivity")
						){
					return true;
				}
			}
		}
		return  false;
	}


	public final void finishAllActivity() {
		for (int i = 0, size = sActivityStack.size(); i < size; i++) {
			if (null != sActivityStack.get(i)) {
				sActivityStack.get(i).finish();
			}
		}
		sActivityStack.clear();
	}//com.dachen.
	public final void finishAllActivityMedie() {
		for (int i = 0, size = sActivityStack.size(); i < size; i++) {
			if (null != sActivityStack.get(i)) {
				if (sActivityStack.get(i).getClass().getName()
						.contains(AppConfig.PACKAGENAME_DOCTOR_LIBRARY)) {
					sActivityStack.get(i).finish();
				}
			}
		}
		sActivityStack.clear();
	}
	public final void finishAllActivityMedieTo() {
		for (int i = 0, size = sActivityStack.size(); i < size; i++) {
			if (null != sActivityStack.get(i)) {
				if (sActivityStack.get(i).getClass().getName()
						.contains(AppConfig.PACKAGENAME_DOCTOR_LIBRARY)) {
					sActivityStack.get(i).finish();
				}
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
		for (int i = 0, size = sActivityStack.size()-1; i < size; i++) {
			if (null != sActivityStack.get(i)) {
				String name = sActivityStack.get(i).getClass().getName();
				String className = clazz.getName();
				if (name.equals(className)) {
					sActivityStack.get(i).finish();
				}
			}
		}
	}
	public final void finishActivityE(Class clazz) {
		for (int i = 0, size = sActivityStack.size(); i < size; i++) {
			if (null != sActivityStack.get(i)) {
				String name = sActivityStack.get(i).getClass().getName();
				String className = clazz.getName();
				if (name.equals(className)) {
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
