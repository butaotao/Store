package com.dachen.medicine.common.utils;

import android.content.Context;
import android.text.*;
import android.text.TextUtils;
import android.widget.Toast;

import com.dachen.medicine.entity.Result;

public class ToastUtils {
	public static void showToast(Context context,String text) {
		if (!android.text.TextUtils.isEmpty(text)){
			Toast.makeText(MedicineApplication.getApplication(context), text,
					Toast.LENGTH_LONG).show();
		}
	}
	public static void showToast(Context context,int resourceId) {
		Toast.makeText(MedicineApplication.getApplication(context),
				MedicineApplication.getApplication(context).getText(resourceId),
				Toast.LENGTH_LONG).show();
	}
	public static void showResultToast(Context context,Result result) {
		if (null!=result&&!TextUtils.isEmpty(result.resultMsg)){
			Toast.makeText(MedicineApplication.getApplication(context),
					result.resultMsg,
					Toast.LENGTH_LONG).show();
		}

	}
}
