package com.dachen.dgroupdoctorcompany.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.dachen.medicine.common.utils.SharedPreferenceUtil;

public class UserInfo {
	private static final String SP_NAME = "login_user_info";// FILE_NAME 
	private static final String KEY_ACCESS_TOKEN = "access_token";
	public static final String KEY_USER_ID = "user_id";
	public static final String KEY_NAME = "user_name";
	public static final String KEY_USERTYPE = "user_type";
	public static final String KEY_HISTORY_IP = "history_ip";
	public static final String USER_TYPE = "10";
	public static final String ROLE = "role";
	public static final String KEY_PACKAGENAME = "packagename";
	SharedPreferences sp ;
	static UserInfo info;
	Context context;
	public UserInfo(Context c){
		if (null==sp) {
			sp =c.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE); 
		}
		this.context = c;
	}
	public  static UserInfo getInstance(Context c){
		if (null==info) {
			info = new UserInfo(c);
		}
		return info;
		
	}
	public  String getId(){
		return SharedPreferenceUtil.getString(context,"id","");
	}
	public  String getUserType(){
		;
		return sp.getString(KEY_USERTYPE, ""); 
	}
	public String getUserName(){
		return SharedPreferenceUtil.getString(context,"username","");
	}
	public String getSesstion(){
		return SharedPreferenceUtil.getString(context,"session","");
	}
	public String getNickName(){

		return SharedPreferenceUtil.getString(context,"nickname","");
	}
	public boolean isMediePresent(){
		if (SharedPreferenceUtil.getString(context,ROLE,"").equals("医药代表")){
			return  true;
		}
		return  false;
	}
	public String getHeadUrl(){

		return SharedPreferenceUtil.getString(context,"head_url","");
	}
	public void setPackageName(Context context,String name){
		sp.edit().putString(KEY_PACKAGENAME,name).commit();
	}
}
