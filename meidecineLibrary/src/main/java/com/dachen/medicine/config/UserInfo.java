package com.dachen.medicine.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.dachen.medicine.common.utils.SharedPreferenceConst;
import com.dachen.medicine.net.NetConfig;

import java.util.HashMap;

public class UserInfo {
	private static final String SP_NAME = "login_user_info";// FILE_NAME 
	private static final String KEY_ACCESS_TOKEN = "access_token";
	private static final String KEY_CONTEXT_TOKEN = "context_token";
	public static final String KEY_USER_ID = "user_id";
	public static final String KEY_NAME = "user_name";
	public static final String KEY_USERTYPE = "user_type";
	public static final String KEY_HISTORY_IP = "history_ip";
	public static final String KEY_PACKAGENAME = "packagename";
	SharedPreferences sp ;
	static UserInfo info;
	Context context;
	public static final String VERSION = "VERSION";
	public String getVersion() {
		return sp.getString(VERSION, "");
	}
	public void setVersion(String version){
		sp.edit().putString(VERSION, version).commit();
	}
	public UserInfo(Context c){
		context = c;
		if (null==sp) {
			sp =c.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE); 
		}
	}
	public  static UserInfo getInstance(Context c){
		if (null==info) {
			info = new UserInfo(c);
		}
		return info;
		
	}

	public  String getId(){
		return sp.getString(KEY_USER_ID, ""); 
	}
	public  void setId(String id){
		sp.edit().putString(KEY_USER_ID, id).commit();
	}
	public String getIpNum(){
		return  sp.getString(KEY_HISTORY_IP,getEnvinment());
	}
	public  void setUserType(String userType){
		 sp.edit().putString(KEY_USERTYPE, userType).commit();
	}
	public  String getUserType(){
		return sp.getString(KEY_USERTYPE, "");
	}
	public String getUserName(){
		return sp.getString(KEY_NAME, ""); 
	}
	public String getPackageName(){
		return sp.getString(KEY_PACKAGENAME,"");
	}
	public String getSesstion(){
		return sp.getString(KEY_ACCESS_TOKEN, ""); 
	}
	public void setSession(String token){
		sp.edit().putString(KEY_ACCESS_TOKEN, token).commit();
	}
	public void setContextSession(String access_context){

		sp.edit().putString(KEY_CONTEXT_TOKEN, access_context).commit();
	}
	public String getContextToken(){
		return  sp.getString(KEY_CONTEXT_TOKEN,"");
	}
	public void setIp(String num){
		sp.edit().putString(KEY_HISTORY_IP,num).commit();
	}
	//hippo环境
	public String getUrl(){
		String ip="";
		String ipConfig = sp.getString(KEY_HISTORY_IP, getEnvinment());
		if (ipConfig.equals("0")) {
			ip = NetConfig.API_OTER_URL;
		}else if (ipConfig.equals("1")) {
			ip = NetConfig.KANG_ZHE;
			return "http://"+ip+":"+NetConfig.port+"/"+NetConfig.MEDIELWEBAPI;
		}else if (ipConfig.equals("2")) {
			ip = NetConfig.API_INNER_URL;
		}else if (ipConfig.equals("3")) {
			ip = NetConfig.KANG_ZHE;
		}else if (ipConfig.equals("4")) {
			ip = NetConfig.API_QUJUNLI_URL;
		}
			return "http://"+ip+":"+NetConfig.port+"/"+NetConfig.MEDIELWEBAPI;

	}
	HashMap<String, String> maps;
	public HashMap<String, String> getInterfaceMaps() {
		maps = new HashMap<String, String>();
		// TODO Auto-generated method stub
		String ipConfig =  sp.getString(KEY_HISTORY_IP, getEnvinment());
		String ip="";

		if (ipConfig.equals("0")) {
			ip =  NetConfig.API_OTER_URL;
		}else if (ipConfig.equals("1")) {
			ip = NetConfig.KANG_ZHE;
			maps.put("url","http://"+ip+":"+NetConfig.port+"/"+NetConfig.MEDIELWEBFILES);
			maps.put("ip", "http://"+ip+":"+NetConfig.port);
		}else if (ipConfig.equals("2")) {
			ip = NetConfig.API_INNER_URL;
		}else if (ipConfig.equals("3")) {
			ip = NetConfig.KANG_ZHE;
		}else if (ipConfig.equals("4")) {
			ip = NetConfig.API_QUJUNLI_URL;
		}
		if (null==maps.get("url")) {
			maps.put("url","http://"+ip+"/"+NetConfig.MEDIELWEBFILES);
			maps.put("ip", "http://"+ip+":"+NetConfig.port+"");
		}
		maps.put("session", sp.getString(KEY_ACCESS_TOKEN, ""));
		maps.put("userid", sp.getString(KEY_USER_ID, ""));
		maps.put("packagename", sp.getString(KEY_PACKAGENAME,""));
		return maps;
	}
	//医患环境
	public String getIp(){
		String ip="";
		String ipConfig = sp.getString(KEY_HISTORY_IP, getEnvinment());
		if (ipConfig.equals("0")) {
			ip = NetConfig.API_OTER_URL;
		}else if (ipConfig.equals("1")) {
			ip = NetConfig.KANG_ZHE;
			return "http://"+ip+"";
		}else if (ipConfig.equals("2")) {
			ip = NetConfig.API_INNER_URL;
		}else if (ipConfig.equals("3")) {
			ip = NetConfig.KANG_ZHE_TEST;
		}else if (ipConfig.equals("4")) {
			ip = NetConfig.API_QUJUNLI_URL;
		}
		return "http://"+ip+"";
	}
	public HashMap<String, String> getIP(){
		 	maps = new HashMap<String, String>();
		 		// TODO Auto-generated method stub
						//public static String MEDIC_RUL = "http://"+Constants.IP+":9002/web/api";
								//public static String MEDIC_RUL_CODE = "http://"+Constants.IP+":9002
										String ip="";
		 		String ipConfig = sp.getString(KEY_HISTORY_IP,getEnvinment());
		 		if (ipConfig.equals("0")) {
			 			ip = NetConfig.API_OTER_URL;
			 		}else if (ipConfig.equals("1")) {
						ip = NetConfig.KANG_ZHE;
						maps.put("url","http://"+ip+":80/web/api");
						maps.put("ip", "http://"+ip+":80");
					}else if (ipConfig.equals("2")) {
						ip = NetConfig.API_INNER_URL;
					}else if (ipConfig.equals("3")) {
						ip = NetConfig.KANG_ZHE_TEST;
					}else if (ipConfig.equals("4")) {
						ip = NetConfig.API_QUJUNLI_URL;
					}
				if (null==maps.get("url")) {
						maps.put("url","http://"+ip+":80/web/api");
						maps.put("ip", "http://"+ip+":80");
					}
					maps.put("ipnotport","http://"+ip);
				return maps;
			}

	public String getPackName(){
		return sp.getString(KEY_PACKAGENAME,"");
	}
	public void setPackageName(Context context,String name){
		sp.edit().putString(KEY_PACKAGENAME,name).commit();

	}
	public String getEnvinment(){
		if (UserInfo.getInstance(context).getPackageName().equals(AppConfig.PACKAGENAME_PACIENT_BOGE)
				||UserInfo.getInstance(context).getPackageName().equals(AppConfig.PACKAGENAME_DOCTOR_BOGE)){
			return "0";
		}else if (UserInfo.getInstance(context).getPackageName().equals(AppConfig.PACKAGENAME_DOCTOR_XUANGUAN)
				||UserInfo.getInstance(context).getPackageName().equals(AppConfig.PACKAGENAME_PACIENT_XUANGUAN)){
			return "1";
		}
		return "1";
	}
}
