package com.dachen.medicine.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.ViewTreeObserver;
import android.widget.TextView;



public class StringUtils {
	private final static Pattern email_pattern = Pattern
			.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

	/**
	 * 将list拼接成str1,str2,str3形式的字符串
	 * 
	 * @param list
	 * @return
	 */
	public static String spliceString(List<String> list) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		int len = list.size();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(list.get(i));
			if (i < len - 1) {
				sb.append(",");
			}
		}

		return sb.toString();
	}

	/**
	 * EditText显示Error
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static CharSequence editTextHtmlErrorTip(Context context, int resId) {
		CharSequence html = Html.fromHtml("<font color='red'>"
				+ context.getString(resId) + "</font>");
		return html;
	}

	public static CharSequence editTextHtmlErrorTip(Context context, String text) {
		CharSequence html = Html.fromHtml("<font color='red'>" + text
				+ "</font>");
		return html;
	}

	static Pattern phoneNumberPat = Pattern
			.compile("^((13[0-9])|(147)|(15[0-3,5-9])|(17[0,6-8])|(18[0-9]))\\d{8}$");
	static Pattern nickNamePat = Pattern
			.compile("^[\u4e00-\u9fa5_a-zA-Z0-9_]{3,15}$");// 3-10个字符
	static Pattern searchNickNamePat = Pattern
			.compile("^[\u4e00-\u9fa5_a-zA-Z0-9_]*$");// 不限制长度，可以为空字符串
	static Pattern companyNamePat = Pattern
			.compile("^[\u4e00-\u9fa5_a-zA-Z0-9_]{3,50}$");// 3-50个字符

	/* 是否是手机号 */
	public static boolean isMobileNumber(String mobiles) {
		Matcher mat = phoneNumberPat.matcher(mobiles);
		return mat.matches();
	}

	/* 检测之前，最好检测是否为空。检测是否是正确的昵称格式 */
	public static boolean isNickName(String nickName) {
		if (TextUtils.isEmpty(nickName)) {
			return false;
		}
		Matcher mat = nickNamePat.matcher(nickName);
		return mat.matches();
	}

	public static boolean isSearchNickName(String nickName) {
		if (nickName == null) {// 防止异常
			return false;
		}
		Matcher mat = searchNickNamePat.matcher(nickName);
		return mat.matches();
	}

	public static boolean isCompanyName(String name) {
		if (TextUtils.isEmpty(name)) {
			return false;
		}
		Matcher mat = companyNamePat.matcher(name);
		return mat.matches();
	}

	/**
	 * 判断是不是一个合法的电子邮件地址
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.trim().length() == 0)
			return false;
		return email_pattern.matcher(email).matches();
	}

	public static boolean strEquals(String s1, String s2) {
		if (s1 == s2) {// 引用相等直接返回true
			return true;
		}
		boolean emptyS1 = s1 == null || s1.trim().length() == 0;
		boolean emptyS2 = s2 == null || s2.trim().length() == 0;
		if (emptyS1 && emptyS2) {// 都为空，认为相等
			return true;
		}
		if (s1 != null) {
			return s1.equals(s2);
		}
		if (s2 != null) {
			return s2.equals(s1);
		}
		return false;
	}

	/**
	 * 去掉特殊字符
	 */
	public static String replaceSpecialChar(String str) {
		if (str != null && str.length() > 0) {
			return str.replaceAll("&#39;", "’").replaceAll("&#039;", "’")
					.replaceAll("&nbsp;", " ").replaceAll("\r\n", "\n")
					.replaceAll("\n", "\r\n");
		}
		return "";
	}

	/**
	 * 获取头像url
	 */
	public static String getAvatarUrl(String userId, String headPicFileName) {
		String avatarUrl = null;
		int t = Integer.parseInt(userId) % 1000;
		if (!TextUtils.isEmpty(headPicFileName)) {
			if (headPicFileName.contains("avatar/")) {
				avatarUrl = headPicFileName;
			} else {
				// TODO 测试员说头像有些模糊，所以这里修改为获取原因。
				// avatarUrl = Constants.DOWNLOAD_AVATAR_BASE_URL + "avatar/o/"
				// + t + "/" + headPicFileName;
			}
		}
		return avatarUrl;
	}

	public static String getSexStr(int sex) {
		String sexString = "";
		if (sex == 1) {
			sexString = "男";
		} else if (sex == 2) {
			sexString = "女";
		} else {
			sexString = "保密";
		}
		return sexString;
	}

	public static final String getFileType(String fileName) {
		if (fileName.endsWith(".png") || fileName.endsWith(".PNG")) {
			return "image/png";
		} else if (fileName.endsWith(".jpg") || fileName.endsWith(".JPEG")
				|| fileName.endsWith(".JPG")) {
			return "image/jpg";
		} else if (fileName.endsWith(".bmp") || fileName.endsWith(".BMP")) {
			return "image/bmp";
		} else {
			return "application/octet-stream";
		}
	}

	public static String convert(long mill) {
		Date date = new Date(mill);
		String strs = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			strs = sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strs;
	}

	public static String convert1(long mill) {
		Date date = new Date(mill);
		String strs = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			strs = sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strs;
	}

	/**
	 * s缩略图url转原图url
	 * 
	 * @param thumbnailUrl
	 * @return
	 */
	public static String thumbnailUrl2originalUrl(String thumbnailUrl) {
		if (thumbnailUrl == null) {
			return null;
		}
		String originalUrl = new String(thumbnailUrl);
		return originalUrl.replace("/t/", "/o/");
	}
	public static String getUrlEncode(String url){
		try {
			return URLEncoder.encode(url,"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}
	public static boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		if (TextUtils.isEmpty(str)){
			return false;
		}

		Matcher isNum = pattern.matcher(str.trim().replace(" ",""));
		if( !isNum.matches() ){
			return false;
		}
		return true;
	}
	public static boolean containsChinese(String s){
		String reg = "[\\u4e00-\\u9fa5]+";//
		if (!android.text.TextUtils.isEmpty(s)){
			for(int i =0 ;i < s.length() ; i ++)
			 {	if (s.substring(i, i+1).matches(reg)){
				return true;
			 }
			}

			return false;
		}
		return false;
	}
	public static boolean isEnglish(String s){
		if (s.matches("^[a-zA-Z]*")){
			return true;
		}
		return false;
	}
}