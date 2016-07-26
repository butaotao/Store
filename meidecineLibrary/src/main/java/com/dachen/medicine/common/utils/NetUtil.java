package com.dachen.medicine.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {
	public static boolean checkNetworkEnable(Context context) {
		ConnectivityManager cwjManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = cwjManager.getActiveNetworkInfo();
		if (network == null)
			return false;
		return network.isAvailable();
	}
}
