package com.dachen.medicine.app;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class Channel {

	private static Context mContext;
	private static Channel channel;

	private Channel() {
	}

	public static Channel getChannel() {
		if (channel == null) {
			mContext = MedicineApplication.getApplication();
			channel = new Channel();
		}
		return channel;
	}

	// 获取渠道号
	public String getChannelId() {
		String CHANNELID = "";
		try {
			ApplicationInfo ai = mContext.getPackageManager()
					.getApplicationInfo(mContext.getPackageName(),
							PackageManager.GET_META_DATA);
			Object value = ai.metaData.get("channelname"); // manifest里面的name值
			if (value != null) {
				CHANNELID = value.toString();
			}
		} catch (Exception e) {
			//
		}
		return CHANNELID;
	}

	// 获取appid
	public String getAppId() {
		String APPID = "";
		try {
			ApplicationInfo ai = mContext.getPackageManager()
					.getApplicationInfo(mContext.getPackageName(),
							PackageManager.GET_META_DATA);
			Object value = ai.metaData.get("appid"); // manifest里面的name值
			if (value != null) {
				APPID = value.toString();
				if (APPID.length() == 5) {
					APPID = "0" + APPID;
				}
			}
		} catch (Exception e) {
			//
		}
		return APPID;
	}
}
