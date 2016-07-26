package com.dachen.medicine.common.utils;

import android.content.Context;
import android.content.SharedPreferences.Editor;

/**
 * @描述： SharedPreferences工具类
 * @作者： @butaotao
 * @创建时间： @2015-6-23
 */
public class SharedPreferenceUtil {
	public static boolean putString(Context context,String key, String value) {
		if (!"".equals(key) && null != key) {
			Editor editor = MedicineApplication.getSharePreferences(context).edit();
			editor.putString(key, value);
			return editor.commit();
		}

		return false;
	}

	public static String getString(Context context,String key, String defaultValue) {
		String value = MedicineApplication.getSharePreferences(context).getString(key,
				defaultValue);
		return value;
	}

	public static boolean putBoolean(Context context,String key, Boolean value) {
		Editor editor = MedicineApplication.getSharePreferences(context).edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}

	public static Boolean getBoolean(Context context,String key, Boolean defaultValue) {
		Boolean value = MedicineApplication.getSharePreferences(context).getBoolean(
				key, defaultValue);
		return value;
	}

	public static Boolean putLong(Context context,String key, Long value) {
		Editor editor = MedicineApplication.getSharePreferences(context).edit();
		editor.putLong(key, value);
		return editor.commit();
	}

	public static long getLong(Context context,String key, int defaultValue) {
		long value = MedicineApplication.getSharePreferences(context).getLong(key,
				defaultValue);
		return value;
	}

	public static boolean putInt(Context context,String key, int value) {
		Editor editor = MedicineApplication.getSharePreferences(context).edit();
		editor.putInt(key, value);
		return editor.commit();
	}

	public static int getInt(Context context,String key, int defaultValue) {
		int value = MedicineApplication.getSharePreferences(context).getInt(key,
				defaultValue);
		return value;
	}

	public static boolean clearAll(Context context) {
		return MedicineApplication.getSharePreferences(context).edit().clear()
				.commit();
	}
}
