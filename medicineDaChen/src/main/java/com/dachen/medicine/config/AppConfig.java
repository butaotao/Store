package com.dachen.medicine.config;

import java.io.File;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.dachen.medicine.app.Channel;
import com.dachen.medicine.app.MedicineApplication;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.SystemUtils;

/**
 * 
 * @描述： @使用sp保存数据
 * @作者： @蒋诗朋
 * @创建时间： @2014-11-27
 */

public class AppConfig {

	public static String getUrl(String interfaceName, int net) {
		String url = "";
		url = ContextConfig.getInstance().getApiUrl(net) + File.separator
				+ interfaceName; 
		return url;
	}
	public static String getuploadUrl(String interfaceName, int net){
		String url = "";
		url = ContextConfig.getInstance().getUploadUrl(net) + File.separator
				+ interfaceName; 
		return url;
	}
	public static String getuploadUrl(String interfaceName, int net,int port){
		String url = "";
		url = ContextConfig.getInstance().getUploadUrl(net,port) + File.separator
				+ interfaceName; 
		return url;
	}
	public static String getUrl(Map<String, String> interfaces, int net) {
		// SharedPreferenceUtil.getString("session", "");
		String url = "";
		if (net == 2) {
			url = ContextConfig.getInstance().getApiUrl(net) + File.separator
					+ interfaces.get("interface1") + File.separator
					+ SharedPreferenceUtil.getString("session", "")
					+ File.separator + interfaces.get("interface2");
		} else if (net == 1) {
			url = ContextConfig.getInstance().getApiUrl(net) + File.separator
					+ interfaces.get("interface1");
		} 
		return url;
	}
	public static String getUrl(Map<String, String> interfaces, int net,int port) {
		// SharedPreferenceUtil.getString("session", "");
		String url = "";
		if (net == 2) {
			url = ContextConfig.getInstance().getApiUrl(net) + File.separator
					+ interfaces.get("interface1") + File.separator
					+ SharedPreferenceUtil.getString("session", "")
					+ File.separator + interfaces.get("interface2");
		} else if (net == 1) {
			url = ContextConfig.getInstance().getApiUrl(net,port) + File.separator
					+ interfaces.get("interface1");
		} 
		return url;
	}
	/*
	 * public static String getUrl(String interfaceName,int net){ String url =
	 * ContextConfig.getInstance().getApiUrl(net)+ File.separator +
	 * interfaceName ; LogUtils.yuanLog(LogUtils.D,url); return url; }
	 */
	/** 用户数据 **/
	private static final String XML_USER_DATA = "USER_DATA";

	/** 用户手机号码 **/
	private static final String XML_USER_PHONE = "XML_USER_PHONE";

	/** 用户sessionid **/
	private static final String XML_USER_SESSION_ID = "USER_SESSION_ID";

	/** 用户jsession_id **/
	private static final String JSESSION_ID = "JSESSION_ID";

	/** 用户userid **/
	private static final String XML_USER_ID = "XML_USER_ID";

	/** 查询多个用户 **/
	private static final String XML_MULTIPLE_USER_QUERY = "MULTIPLE_USER_QUERY";

	/** 银行列表 **/
	private static final String XML_BANK_LIST = "XML_BANK_LIST";

	/** 轮播图片 **/
	private static final String XML_HOME_IMG_LIST = "XML_HOME_IMG_LIST";

	/** 是否第一次引导 **/
	private static final String XML_GUIDE_SHOW = "XML_GUIDE_SHOW";

	/** 是否第一次进入主界面 **/
	private static final String XML_IS_MAIN_SHOW = "XML_IS_MAIN_SHOW";

	/** 提现免费额度提现提示 **/
	private static final String XML_CASH_ALERT = "XML_CASH_ALERT";

	/**
	 * 保存用户手机号码
	 * 
	 * @param context
	 * @param phone
	 */
	public static void saveUserPhone(Context context, String phone) {
		if (!TextUtils.isEmpty(phone)) {
			SharedPreferences sp = context.getSharedPreferences(XML_USER_PHONE,
					Context.MODE_PRIVATE);
			Editor ed = sp.edit();
			ed.putString(XML_USER_PHONE, phone);
			ed.commit();
		}
	}

	/**
	 * 保存用户id
	 * 
	 * @param context
	 * @param userId
	 */
	public static void saveUserId(Context context, String userId) {
		SharedPreferences sp = context.getSharedPreferences(XML_USER_ID,
				Context.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putString(XML_USER_ID, userId);
		ed.commit();
	}

	/**
	 * Description:获取用户登录userId
	 * 
	 * @param context
	 */
	public static String getUserId(Context context) {
		SharedPreferences sp = context.getSharedPreferences(XML_USER_ID,
				Context.MODE_PRIVATE);
		return sp.getString(XML_USER_ID, "");
	}

	/**
	 * 保存第一次进入主界面信息
	 * 
	 * @param context
	 * @param isFirstMain
	 */
	public static void setIsFirstMain(Context context, boolean isFirstMain) {
		SharedPreferences sp = context.getSharedPreferences(XML_IS_MAIN_SHOW,
				Context.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putBoolean("first", isFirstMain);
		ed.commit();
	}

	/**
	 * Description:判断是否第一次打开主界面
	 * 
	 * @param context
	 */
	public static boolean isFirstMain(Context context) {
		SharedPreferences sp = context.getSharedPreferences(XML_IS_MAIN_SHOW,
				Context.MODE_PRIVATE);
		return sp.getBoolean("first", false);
	}

	/**
	 * 提现免费额度提现提示
	 * 
	 * @param context
	 * @param isChecked
	 */
	public static void setCashAlert(Context context, boolean isChecked) {
		SharedPreferences sp = context.getSharedPreferences(XML_CASH_ALERT,
				Context.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putBoolean(XML_CASH_ALERT, isChecked);
		ed.commit();
	}

	/**
	 * 提现免费额度提现提示
	 * 
	 * @param context
	 */
	public static boolean getCashAlertChecked(Context context) {
		SharedPreferences sp = context.getSharedPreferences(XML_CASH_ALERT,
				Context.MODE_PRIVATE);
		return sp.getBoolean(XML_CASH_ALERT, false);
	}

	/*	*//**
	 * 保存用户登录数据
	 * 
	 * @param context
	 * @param data
	 */
	/*
	 * public static void saveUserLoginData(Context context, LoginResult data) {
	 * if (data != null) { String json = new Gson().toJson(data);
	 * SharedPreferences sp = context.getSharedPreferences(XML_USER_DATA,
	 * Context.MODE_PRIVATE); Editor ed = sp.edit(); ed.putString(XML_USER_DATA,
	 * json); ed.commit(); } }
	 */

	/**
	 * Description:获取用户登录手机号码
	 * 
	 * @param context
	 */
	public static String getUserPhone(Context context) {
		SharedPreferences sp = context.getSharedPreferences(XML_USER_PHONE,
				Context.MODE_PRIVATE);
		String value = sp.getString(XML_USER_PHONE, "");
		if (TextUtils.isEmpty(value)) {
			return null;
		} else {
			return value;
		}
	}

	/*	*//**
	 * 
	 * Description:获取用户登录信息
	 * 
	 * @param context
	 */
	/*
	 * public static LoginResult getLoginResultData(Context context) {
	 * SharedPreferences sp = context.getSharedPreferences(XML_USER_DATA,
	 * Context.MODE_PRIVATE); String value = sp.getString(XML_USER_DATA, ""); if
	 * (TextUtils.isEmpty(value)) { return null; } else { LoginResult entity =
	 * new Gson().fromJson(value, LoginResult.class); return entity; } }
	 */

	/**
	 * 保存多个查询用户
	 * 
	 * @param context
	 * @param json
	 */
	public static void saveMultipleUser(Context context, String json) {
		if (!TextUtils.isEmpty(json)) {
			SharedPreferences sp = context.getSharedPreferences(
					XML_MULTIPLE_USER_QUERY, Context.MODE_PRIVATE);
			Editor ed = sp.edit();
			ed.putString(XML_MULTIPLE_USER_QUERY, json);
			ed.commit();
		}
	}

	/**
	 * 保存银行列表
	 * 
	 * @param context
	 * @param json
	 */
	public static void saveBankList(Context context, String json) {
		if (!TextUtils.isEmpty(json)) {
			SharedPreferences sp = context.getSharedPreferences(XML_BANK_LIST,
					Context.MODE_PRIVATE);
			Editor ed = sp.edit();
			ed.putString(XML_BANK_LIST, json);
			ed.commit();
		}
	}

	/**
	 * 获取用户银行列表
	 * 
	 * @param context
	 * @return
	 */
	public static String getBankList(Context context) {
		SharedPreferences sp = context.getSharedPreferences(XML_BANK_LIST,
				Context.MODE_PRIVATE);
		final String json = sp.getString(XML_BANK_LIST, "");
		if (TextUtils.isEmpty(json)) {
			return "";
		} else {
			return json;
		}
	}

	/**
	 * 获取多个登录查询用户
	 * 
	 * @param context
	 * @return
	 */
	public static String getMultipleUser(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				XML_MULTIPLE_USER_QUERY, Context.MODE_PRIVATE);
		final String json = sp.getString(XML_MULTIPLE_USER_QUERY, "");
		if (TextUtils.isEmpty(json)) {
			return "";
		} else {
			return json;
		}
	}

	/**
	 * 保存用户seesion
	 * 
	 * @param context
	 * @param sid
	 */
	public static void saveUserSession(Context context, String sid) {
		SharedPreferences sp = context.getSharedPreferences(
				XML_USER_SESSION_ID, Context.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putString(XML_USER_SESSION_ID, sid);
		ed.commit();
	}

	/**
	 * 保存用户seesion
	 * 
	 * @param context
	 * @param jsessionId
	 */
	public static void saveUserSessionNew(Context context, String jsessionId) {
		SharedPreferences sp = context.getSharedPreferences(JSESSION_ID,
				Context.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putString(JSESSION_ID, jsessionId);
		ed.commit();
	}

	/**
	 * 清空用户seesion
	 * 
	 * @param context
	 */
	public static void clearUserSession(Context context) {
		saveUserSession(context, "");
	}

	/**
	 * 清空用户登录数据
	 * 
	 * @param context
	 */

	public static void clearUserLoginData(Context context) {
		SharedPreferences sp = context.getSharedPreferences(XML_USER_DATA,
				Context.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putString(XML_USER_DATA, "");
		ed.commit();
	}

	/**
	 * 获取用户sessionid
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserSession(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				XML_USER_SESSION_ID, Context.MODE_PRIVATE);
		final String sid = sp.getString(XML_USER_SESSION_ID, "");
		if (TextUtils.isEmpty(sid)) {
			return "";
		} else {
			return sid;
		}
	}

	/**
	 * 获取用户sessionid
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserSessionNew(Context context) {
		SharedPreferences sp = context.getSharedPreferences(JSESSION_ID,
				Context.MODE_PRIVATE);
		final String jsessionId = sp.getString(JSESSION_ID, "");
		if (TextUtils.isEmpty(jsessionId)) {
			return "";
		} else {
			return jsessionId;
		}
	}

	/**
	 * 
	 * Description:引导页显示否
	 * 
	 * @return
	 */
	public static boolean isFirstTime(Context context) {
		SharedPreferences sp = context.getSharedPreferences(XML_GUIDE_SHOW,
				Context.MODE_PRIVATE);
		return sp.getBoolean("first", true);
	}

	public static void setIsFirstTime(Context context, boolean isFirst) {
		SharedPreferences sp = context.getSharedPreferences(XML_GUIDE_SHOW,
				Context.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putBoolean("first", isFirst);
		ed.commit();
	}

	public static void setShadeName(String shadeName) {
		if (!TextUtils.isEmpty(shadeName))
			SharedPreferenceUtil.putBoolean(shadeName, true);
	}

	public static boolean getShadeName(String shadeName) {
		boolean isShow = SharedPreferenceUtil.getBoolean(shadeName, false);
		return isShow;
	}

	public static void setTUA(String mTUA) {
		if (!TextUtils.isEmpty(mTUA)) {
			SharedPreferenceUtil.putString("TUA", mTUA);
		}
	}

	public static String getTUA() {
		String TUA = SharedPreferenceUtil.getString("TUA", "");
		if (TextUtils.isEmpty(TUA) || (TUA != null && TUA.length() < 30)) {
			SystemUtils systemUtils = SystemUtils.getInstance();
			Channel channel = Channel.getChannel();
			TUA = "ALC-"
					+ channel.getAppId()
					+ "&NA&"
					+ systemUtils.getOSVersion()
					+ "-"
					+ systemUtils.getRootStatu()
					+ "&"
					+ systemUtils.getResolution(MedicineApplication
							.getApplication()) + "&"
					+ systemUtils.getPhoneVendor() + "-"
					+ systemUtils.getPhoneDevice() + "&"
					+ channel.getChannelId() + "&V1";
			setTUA(TUA);
		}
		return TUA;
	}

	public static boolean isOpenGestureLockLine() {
		return SharedPreferenceUtil.getBoolean("isOpenGestureLockLine", true);
	}

	public static void setOpenGestureLockLine(boolean isOpen) {
		SharedPreferenceUtil.putBoolean("isOpenGestureLockLine", isOpen);
	}

	/**
	 * 理财师在升级日当天是否显示Dialog
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isFirstBrokerTime(Context context) {
		SharedPreferences sp = context.getSharedPreferences(XML_GUIDE_SHOW,
				Context.MODE_PRIVATE);
		return sp.getBoolean("first_broker", true);
	}

	public static void setIsFirstBrokerTime(Context context, boolean isFirst) {
		SharedPreferences sp = context.getSharedPreferences(XML_GUIDE_SHOW,
				Context.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putBoolean("first_broker", isFirst);
		ed.commit();
	}

}
