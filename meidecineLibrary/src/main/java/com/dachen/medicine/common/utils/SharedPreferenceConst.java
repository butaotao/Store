package com.dachen.medicine.common.utils;

/**
 * 
 * @描述： @TODO
 * @作者： @butaotao
 * @创建时间： @2015-7-8
 */
public class SharedPreferenceConst {
	// 按home键出去，再次进来是否需要密码
	public static final String IS_NEED_PW = "isNeedGPWNow";
	// 从splash页面进来是否需要密码
	public static final String IS_NEED_PW_SPLASH = "isNeedGPW";
	// 是否需要显示广告
	public static final String ADD_IMAGE_FLAG = "ADD_IMAGE_FLAG";
	// 当推送消息到来时候，如果app在后台运行，则跳到应用不需要密码（此时这个值为false）
	public static final String PUSH_FLAG = "PUSH_FLAG";
	// 当第三方分享时候，这个时候跳转到第三方分享，然后再回到本app的时候不需要密码
	public static final String ADD_THIRD_FLAG = "ADD_THIRD_SHARE_FLAG";
	// 是否在设置中打开需要手势密码
	public static final String IS_NEED_GESTURE_PW = "switch_gesture_psw";
	// 用户ID
	public static final String USER_ID = "userId";
	// 用户名
	public static final String USER_NAME = "username";
	// 用户头像url
	public static final String USER_HEAD_URL = "headUrl";
	// Sharepreference 的key getSharedPreferences(SharedPreferenceConst.LOCK,
	// MODE_PRIVATE);
	public static final String LOCK = "lock";
	// 本地的手势密码
	public static final String LOCK_KEY = "lock_key";
	// 是否显示广告
	public static final String Is_Show_Account_Ad = "isShowAccountAd";
	// 是否显示广告
	public static final String Is_Account_AdNot_Show = "isAccountAdNotShow";
}
