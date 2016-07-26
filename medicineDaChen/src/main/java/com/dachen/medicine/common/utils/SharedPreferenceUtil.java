package com.dachen.medicine.common.utils;

import android.content.SharedPreferences.Editor;

import com.dachen.medicine.app.MedicineApplication;

/**
 * @描述： SharedPreferences工具类
 * @作者： @butaotao
 * @创建时间： @2015-6-23
 */
public class SharedPreferenceUtil {
	public static boolean putString(String key, String value) {
		if (!"".equals(key) && null != key) {
			Editor editor = MedicineApplication.getSharePreferences().edit();
			editor.putString(key, value);
			return editor.commit();
		}

		return false;
	}

	public static String getString(String key, String defaultValue) {
		String value = MedicineApplication.getSharePreferences().getString(key,
				defaultValue);
		return value;
	}

	public static boolean putBoolean(String key, Boolean value) {
		Editor editor = MedicineApplication.getSharePreferences().edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}

	public static Boolean getBoolean(String key, Boolean defaultValue) {
		Boolean value = MedicineApplication.getSharePreferences().getBoolean(
				key, defaultValue);
		return value;
	}

	public static Boolean putLong(String key, Long value) {
		Editor editor = MedicineApplication.getSharePreferences().edit();
		editor.putLong(key, value);
		return editor.commit();
	}

	public static long getLong(String key, int defaultValue) {
		long value = MedicineApplication.getSharePreferences().getLong(key,
				defaultValue);
		return value;
	}

	public static boolean putInt(String key, int value) {
		Editor editor = MedicineApplication.getSharePreferences().edit();
		editor.putInt(key, value);
		return editor.commit();
	}

	public static int getInt(String key, int defaultValue) {
		int value = MedicineApplication.getSharePreferences().getInt(key,
				defaultValue);
		return value;
	}

	public static boolean clearAll() {
		return MedicineApplication.getSharePreferences().edit().clear()
				.commit();
	}
}
