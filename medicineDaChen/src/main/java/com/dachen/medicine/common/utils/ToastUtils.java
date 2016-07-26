package com.dachen.medicine.common.utils;

import android.text.TextUtils;
import android.widget.Toast;

import com.dachen.medicine.app.MedicineApplication;
import com.dachen.medicine.entity.Result;

public class ToastUtils {
	public static void showToast(String text) {
		Toast.makeText(MedicineApplication.getApplication(), text,
				Toast.LENGTH_LONG).show();
	}
	public static void showToast(Result result) {
		if (null!=result&& !TextUtils.isEmpty(result.resultMsg)){
			Toast.makeText(MedicineApplication.getApplication(), result.resultMsg,
					Toast.LENGTH_LONG).show();
		}

	}

	public static void showToast(int resourceId) {
		Toast.makeText(MedicineApplication.getApplication(),
				MedicineApplication.getApplication().getText(resourceId),
				Toast.LENGTH_LONG).show();
	}
}
