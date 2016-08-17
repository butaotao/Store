package com.dachen.medicine.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.DecimalFormat;
import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 获取系统信息工具类 Created by yuankangle 陆山 on 2015/6/3.
 */
public class SystemUtils {
	public static SystemUtils sSystemUtils;

	public static SystemUtils getInstance() {
		if (sSystemUtils == null) {
			sSystemUtils = new SystemUtils();
		}
		return sSystemUtils;
	}

	/**
	 * 获取APP版本号
	 * 
	 * @param ctx
	 * @return String
	 */
	public static String getSysteVersion(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				return versionName;
			}
		} catch (PackageManager.NameNotFoundException e) {
			return "";
		}
		return "";
	}

	/**
	 * 获取系统版本号
	 * 
	 * @return
	 */
	public String getOSVersion() {
		return Build.VERSION.RELEASE;
	}

	/**
	 * 获取手机型号
	 * 
	 * @return
	 */
	public String getPhoneDevice() {
		return Build.MODEL;
	}

	/**
	 * 获取手机品牌
	 * 
	 * @return
	 */
	public String getPhoneVendor() {
		return Build.BRAND;
	}

	/**
	 * 获取root状态
	 * 
	 * @return 0未知状态 1已经root 2未root
	 */
	public int getRootStatu() {
		int root = 0;
		try {
			if ((!new File("/system/bin/su").exists())
					&& (!new File("/system/xbin/su").exists())) {
				root = 2;
			} else {
				root = 1;
			}
		} catch (Exception e) {
		}
		return root;
	}

	/**
	 * 获取系统字体大小
	 * 
	 * @return
	 */
	public int getFontSize() {
		Paint sPaint = new Paint();
		return (int) sPaint.getTextSize();
	}

	/**
	 * 获取需求的分辨率规则
	 * 
	 * @param ctx
	 * @return
	 */
	public String getResolution(Context ctx) {
		DecimalFormat df = new DecimalFormat("0");
		DisplayMetrics display = ctx.getResources().getDisplayMetrics();
		float screenWidth = (float) (display.widthPixels * 1.0 / 16);
		float screenHeight = (float) (display.heightPixels * 1.0 / 16);
		String widht = df.format(screenWidth);
		String heigth = df.format(screenHeight);
		return widht + "_" + heigth + "_" + getFontSize();
	}

	/**
	 * 判断是否有sim卡
	 * 
	 * @liyanli
	 * @param context
	 * @return
	 */
	public boolean isCanSim(Context context) {
		TelephonyManager mgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return TelephonyManager.SIM_STATE_ABSENT == mgr.getSimState();
	}

	/**
	 * 获取AndroidManidest.xml里面的meta-data
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public String getManifestMetaData(Context context, String key) {
		try {
			ApplicationInfo appInfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			return appInfo.metaData.get(key) + "";
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 保存对象到SharedPreferences中
	 * 
	 * @param key
	 * @param object
	 *            序列化
	 */
	public void setObject(SharedPreferences sp, String key, Object object) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(baos);
			out.writeObject(object);
			String objectVal = new String(Base64.encode(baos.toByteArray(),
					Base64.DEFAULT));
			SharedPreferences.Editor editor = sp.edit();
			editor.putString(key, objectVal);
			editor.commit();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取对象
	 * 
	 * @param key
	 * @param <T>
	 * @return
	 */
	public <T> T getObject(SharedPreferences sp, String key) {
		if (sp.contains(key)) {
			String objectVal = sp.getString(key, null);
			byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
			ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(bais);
				T t = (T) ois.readObject();
				return t;
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					if (bais != null) {
						bais.close();
					}
					if (ois != null) {
						ois.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static int[] getScreenDisplay(Context context) throws Exception {
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(mDisplayMetrics);
		int width = mDisplayMetrics.widthPixels;
		int height = mDisplayMetrics.heightPixels;
		int result[] = { width, height };
		return result;
	}

	/**
	 * 获取手机系统信息
	 * 
	 * @param context
	 * @return
	 */
	public static String[] getPhoneInfo(Context context) {
		String[] info = new String[5];
		TelephonyManager mTm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = mTm.getDeviceId();// 手机IMEI号,DeviceId
		String imsi = mTm.getSubscriberId();// 手机IMSI号
		String mtype = android.os.Build.MODEL;// 手机型号
		String mtyb = android.os.Build.BRAND;// 手机品牌
		String numer = mTm.getLine1Number();// 手机号码，有的可得，有的不可得
		info[0] = imei;
		info[1] = imsi;
		info[2] = mtype;
		info[3] = mtyb;
		info[4] = numer;
		return info;
	}

	/* 获取手机唯一序列号 */
	public static String getDeviceId(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = tm.getDeviceId();// 手机设备ID，这个ID会被用为用户访问统计
		if (deviceId == null) {
			deviceId = UUID.randomUUID().toString().replaceAll("-", "");
		}
		return deviceId;
	}

}
