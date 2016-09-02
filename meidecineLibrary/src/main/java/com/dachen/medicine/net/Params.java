package com.dachen.medicine.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.SystemUtils;
import com.dachen.medicine.config.UserInfo;

import java.util.HashMap;
import java.util.Map;

public class Params {
	public static Map<String, String> getMapInstance() {
		Map<String, String> map = new HashMap<String, String>();
		return map;
	}

	public static HashMap<String, String> getInterface(String interface1,
			String interface2) {
		HashMap<String, String> params = (HashMap<String, String>) getMapInstance();
		params.put("interface1", interface1);
		params.put("interface2", interface2);
		return params;

	}
	public static Map<String, String> getReginsterXiaoMiReceiver(Context context,String uid,
																 String deviceToken, String session,String userType) {
		Map<String, String> params = getMapInstance();
		params.put("uid", uid);
		params.put("client", "android");
		params.put("userType", userType);
		params.put("deviceToken",  /*SystemUtils.getDeviceId(context)*/deviceToken);
		params.put("access_token", session);
		return params;

	}
	public static Map<String, String> getRemoveReginsterXiaoMiReceiver(String uid,
																	   String deviceToken, String session) {
		Map<String, String> params = getMapInstance();
		params.put("serial", deviceToken);
		params.put("access_token", session);
		return params;

	}
	public static Map<String, String> getReginsterXiaoMiReceiverRe(Context context,String uid,
																 String deviceToken, String session,String userType) {
		Map<String, String> params = getMapInstance();
	//	params.put("uid", uid);
		params.put("model", "android");
	//	params.put("userType", userType);
		params.put("serial",  /*SystemUtils.getDeviceId(context)*/deviceToken);
		params.put("access_token", session);
		return params;

	}

	public static Map<String, String>getGoodsGroupList(Context context,String keyword,int pageSize,int pageIndex){
		Map<String, String> params = getMapInstance();
		params.put("access_token", SharedPreferenceUtil.getString(context, "session", ""));
		if(!TextUtils.isEmpty(keyword)){
			params.put("keyword", keyword);
		}
		params.put("companyId", SharedPreferenceUtil.getString(context, "enterpriseId", ""));
		params.put("pageSize", String.valueOf(pageSize));
		params.put("pageIndex", String.valueOf(pageIndex));
		return params;
	}

	public static Map<String, String> getMedieManagementParams(Context context, String userId) {
		Map<String, String> params = getMapInstance();
		params.put("access_token", SharedPreferenceUtil.getString(context, "session", ""));
		params.put("userId", userId);
		return params;
	}
	public static Map<String, String> getUserInfo(Context context ) {
		Map<String, String> params = getMapInstance();
		params.put("access_token", SharedPreferenceUtil.getString(context, "session", ""));
		params.put("userId", SharedPreferenceUtil.getString(context, "id", ""));
		return params;
	}
	public static Map<String, String> getInterface(String interface1) {
		Map<String, String> params = getMapInstance();
		params.put("interface1", interface1);
		return params;

	}

	public static Map<String,String>deletVisit(Context context,String id){
		Map<String, String> params = getMapInstance();
		params.put("access_token", SharedPreferenceUtil.getString(context, "session", ""));
		params.put("id", id);
		return params;
	}

	public static Map<String,String>addVisitGroup(Context context,String orgId,String userName,String lat,String lng,String doctorId,String doctorName,String address,String addressName,String goodsGroupList){
		Map<String, String> params = getMapInstance();
		params.put("access_token", SharedPreferenceUtil.getString(context, "session", ""));
		params.put("companyId", SharedPreferenceUtil.getString(context, "enterpriseId", ""));
		params.put("orgId", orgId);
		params.put("userName", userName);
		params.put("loc.lat", lat);
		params.put("loc.lng", lng);
		if(!TextUtils.isEmpty(doctorId)){
			params.put("doctorId", doctorId);
		}
		if(!TextUtils.isEmpty(doctorName)){
			params.put("doctorName", doctorName);
		}
		if(!TextUtils.isEmpty(address)){
			params.put("address", address);
		}
		if(!TextUtils.isEmpty(addressName)){
			params.put("addressName", addressName);
		}
		params.put("goodsGroupList", goodsGroupList);
		return params;
	}

	public static  Map<String,String>deleteVisitGroup(Context context,String synergGroupId){
		Map<String, String> params = getMapInstance();
		params.put("access_token", SharedPreferenceUtil.getString(context, "session", ""));
		params.put("synergGroupId",synergGroupId);
		return params;
	}

	public static  Map<String,String>joinVisitGroup(Context context,String synerGroupId){
		Map<String, String> params = getMapInstance();
		params.put("access_token", SharedPreferenceUtil.getString(context, "session", ""));
		params.put("synerGroupId",synerGroupId);
		return params;
	}

	public static  Map<String,String>getVisitGroup(Context context,String lat,String lng){
		Map<String, String> params = getMapInstance();
		params.put("access_token", SharedPreferenceUtil.getString(context, "session", ""));
		params.put("companyId", SharedPreferenceUtil.getString(context, "enterpriseId", ""));
		params.put("lat",lat);
		params.put("lng",lng);
		return params;
	}

	public static Map<String,String>startVisitGroup(Context context,String orgId,String lat,String lng,String address,String addressName){
		Map<String, String> params = getMapInstance();
		params.put("access_token", SharedPreferenceUtil.getString(context, "session", ""));
		params.put("companyId", SharedPreferenceUtil.getString(context, "enterpriseId", ""));
		params.put("userName", SharedPreferenceUtil.getString(context, "username", ""));
		params.put("orgId", orgId);
		params.put("loc.lat", lat);
		params.put("loc.lng", lng);
		params.put("address", address);
		params.put("addressName", addressName);
		return params;
	}

	public static Map<String,String>getList(Context context,String type,String state,int pageIndex,int pageSize){
		Map<String, String> params = getMapInstance();
		params.put("access_token", SharedPreferenceUtil.getString(context, "session", ""));
		params.put("companyId", SharedPreferenceUtil.getString(context, "enterpriseId", ""));
		if(!TextUtils.isEmpty(type)){
			params.put("type",type);
		}
		if(!TextUtils.isEmpty(state)){
			params.put("state",state);
		}
		params.put("pageIndex",String.valueOf(pageIndex));
		params.put("pageSize",String.valueOf(pageSize));
		return params;
	}
	public static Map<String,String>getList(Context context,String type,int pageIndex,int pageSize){
		Map<String, String> params = getMapInstance();
		params.put("access_token", SharedPreferenceUtil.getString(context, "session", ""));
		if(!TextUtils.isEmpty(type)){
			params.put("type",type);
		}
		params.put("pageIndex",String.valueOf(pageIndex));
		params.put("pageSize",String.valueOf(pageSize));
		Log.d("zxy :", "179 : Params : getList : access_token = "+SharedPreferenceUtil.getString(context, "session", ""));
		Log.d("zxy :", "180 : Params : getList : type = "+type+", "+pageIndex+", "+pageSize);
		return params;
	}

	public static Map<String,String>updateJobTitle(Context context,String orgId,String newTitle,String userid,String employeeId){
		Map<String, String> params = getMapInstance();
		params.put("access_token", SharedPreferenceUtil.getString(context, "session", ""));
		params.put("userId", userid);
		params.put("orgId", orgId);
		params.put("newTitle", newTitle);
		return params;
	}

	public static Map<String,String>updateOrg(Context context,String newOrgId,String userid,String employeeId){
		Map<String, String> params = getMapInstance();
		params.put("access_token", SharedPreferenceUtil.getString(context, "session", ""));
		params.put("userId", userid);
		params.put("drugCompanyId",
				 SharedPreferenceUtil.getString(context, "enterpriseId", "") );
		params.put("newOrgId", newOrgId);
		params.put("employeeId",employeeId);
		return params;
	}

	public static Map<String,String>updateUserName(Context context,String newName,String userId,String employeeId){
		Map<String, String> params = getMapInstance();
		params.put("access_token", SharedPreferenceUtil.getString(context, "session", ""));
		params.put("userId", userId);
		params.put("name", newName);
		params.put("employeeId",employeeId);
		params.put("drugCompanyId",
				 SharedPreferenceUtil.getString(context, "enterpriseId", "") );
		return params;
	}

	public static Map<String,String>getSignDetail(Context context,String id){
		Map<String, String> params = getMapInstance();
		params.put("access_token", SharedPreferenceUtil.getString(context, "session", ""));
		params.put("id", id );
		return params;
	}

	public static Map<String,String>getVisitParams(Context context,String addressName,String state,String doctorId,String doctorName,String remark,String id,String coordinate,String address){
		Map<String, String> params = getMapInstance();
		params.put("access_token", SharedPreferenceUtil.getString(context, "session", ""));
		params.put("userId", SharedPreferenceUtil.getString(context, "id", ""));
		params.put("state", state);
		if(!TextUtils.isEmpty(doctorId)){
			params.put("doctorId", doctorId);
		}
		if(!TextUtils.isEmpty(doctorName)){
			params.put("doctorName", doctorName);
		}

		params.put("remark", remark);
		if(!TextUtils.isEmpty(id)){
			params.put("id", id);
		}

		params.put("coordinate", coordinate);
		params.put("address", address);
		params.put("addressName", addressName);
		return params;
	}

	public static Map<String,String>getWorkingParams(Context context,String deviceId,String remark,String id,String coordinate,
													 String address,String singedTag,String orgId){
		Map<String, String> params = getMapInstance();
		params.put("access_token", SharedPreferenceUtil.getString(context, "session", ""));
		params.put("userId", SharedPreferenceUtil.getString(context, "id", ""));
		params.put("companyId", SharedPreferenceUtil.getString(context, "enterpriseId", ""));
		params.put("remark", remark);
		params.put("deviceId", deviceId);
		params.put("singedTagId", singedTag);
		if(!TextUtils.isEmpty(id)){
			params.put("id", id);
		}

		params.put("coordinate", coordinate);
		params.put("address", address);
		params.put("orgId", orgId);
		return params;
	}

	public static Map<String, String> getInfoParams(Context context) {
		Map<String, String> params = getMapInstance();
		params.put("access_token", SharedPreferenceUtil.getString(context, "session", ""));
		params.put("userId",SharedPreferenceUtil.getString(context, "id", ""));
		params.put("drugCompanyId",SharedPreferenceUtil.getString(context, "enterpriseId", ""));
		return params;
	}
	public static Map<String, String> getSinOftenPlace(Context context,String drugCompanyId) {
		Map<String, String> params = getMapInstance();
		params.put("access_token", SharedPreferenceUtil.getString(context, "session", ""));
		params.put("drugCompanyId", drugCompanyId);
		params.put("pageIndex","0");
		params.put("pageSize","1000");
		params.put("updateTime","0");
		return params;
	}
	public static Map<String, String> getMedieDocParams(Context context, String bizId, int pageIndex, int pageSize) {
		return getMedieDocParams(context, bizId, null, null, null, -1, pageIndex, pageSize);
	}

	public static Map<String, String> getMedieDocParams(Context context, String bizId, String fileName, String type, String sortAttr, int sortType, int pageIndex, int pageSize) {
		Map<String, String> params = getMapInstance();
		params.put("access_token", SharedPreferenceUtil.getString(context, "session", ""));
		//params.put("userId",userId);
		params.put("bizId", bizId);
		if (!TextUtils.isEmpty(fileName)) {
			params.put("fileName", fileName);
		}
		if (!TextUtils.isEmpty(type)) {
			params.put("type", type);
		}
		if (!TextUtils.isEmpty(sortAttr)) {
			params.put("sortAttr", sortAttr);
		}
		params.put("sortType", "" + sortType);
		params.put("pageIndex", "" + pageIndex);
		params.put("pageSize", "" + pageSize);
		return params;
	}

	public static Map<String, String> getLoginoutParams(Context context, String serial) {
		Map<String, String> params = getMapInstance();
		params.put("userKey", ""); // 没有用userKey校验 ，但是这个参数还要传，现在 还没去掉
		params.put("access_token", SharedPreferenceUtil.getString(context, "session", ""));
		params.put("serial", serial);
		return params;

	}

	// (phone,userType,smsid,ranCode)
	public static Map<String, String> getResetPasswordParams(String phone,
															 String userType, String smsid,
															 String ranCode, String password,Context context) {
		Map<String, String> params = getMapInstance();
		params.put("telephone", phone);
		params.put("userType", userType);
		params.put("smsid", smsid);
		params.put("ranCode", ranCode);
		params.put("password", password);
		params.put("model","android");
		params.put("serial",
				SystemUtils.getDeviceId(context));
		return params;

	}

	public static Map<String, String> getLoginParams(String phoneNum,
													 String password, String userType, Context context) {
		Map<String, String> params = getMapInstance();
		params.put("telephone", phoneNum);
		params.put("password", password);
		params.put("userType", userType);
/*		params.put("serial",
				SharedPreferenceUtil.getString(context, "mRegId", ""));*/
 		params.put("serial",
				SystemUtils.getDeviceId(context));
		SharedPreferenceUtil.getString(context, "mRegId", "");
		params.put("model", "android");
		return params;

	}
	public static Map<String, String> getLoginVerifyParams(String phoneNum,
													 String password) {
		Map<String, String> params = getMapInstance();
		params.put("u", phoneNum);
		params.put("p", password);

		return params;

	}
	public static Map<String, String> getSendMSMParams(String phoneNumber,
			String templateId) {
		Map<String, String> params = getMapInstance();

		params.put("telephone", phoneNumber);
		params.put("templateId", templateId);// 短信模板。
		return params;
	}

	public static Map<String, String> getGoodsInfo(String category,
			String __PAGE__) {
		Map<String, String> params = getMapInstance();

		params.put("category", category);
		params.put("__PAGE__", __PAGE__);
		return params;
	}

	public static HashMap<String, String> getBuyMedicineList(String id) {
		Map<String, String> params = getMapInstance();
		params.put("id", id);
		return (HashMap<String, String>) params;
	}
	public static HashMap<String, String> getMedieInfoByID(String id) {
		Map<String, String> params = getMapInstance();
		params.put("id", id);
		return (HashMap<String, String>) params;
	}
	public static HashMap<String, String> getPatientInfoByID(String patient) {
		Map<String, String> params = getMapInstance();
		params.put("patient", patient);
		return (HashMap<String, String>) params;
	}
	public static HashMap<String, String> getMedicineRecord(String page,
			String pageSize, String filter,String quantity) {
		Map<String, String> params = getMapInstance();
		params.put("__PAGE__", page);
		params.put("__PAGE_SIZE__", pageSize);
		params.put("__FILTER__", filter);
		params.put("quantity", quantity);
		return (HashMap<String, String>) params;
	}
	public static HashMap<String, String> getMedicineRecord2(String page,
			String pageSize, String filter1,String filter2,String quantity) {
		Map<String, String> params = getMapInstance();
		params.put("__PAGE__", page);
		params.put("__PAGE_SIZE__", pageSize);
		params.put("datetime>", filter1);
		params.put("datetime<", filter2);
		params.put("quantity>", quantity);
		return (HashMap<String, String>) params;
	}
	public static HashMap<String, String> getMedicineList(String recipe_id,String id) {
		Map<String, String> params = getMapInstance();
		params.put("recipe_id", recipe_id);
		return (HashMap<String, String>) params;
	}
	public static HashMap<String, String> getMedicineDetal(String id,Context context) {
		Map<String, String> params = getMapInstance();
		params.put("id", id);
		params.put("access_token", UserInfo.getInstance(context).getSesstion());
		return (HashMap<String, String>) params;
	}
	public static HashMap<String, String> getPatientID(String recipe_id) {
		Map<String, String> params = getMapInstance();
		params.put("id", recipe_id);
		return (HashMap<String, String>) params;
	}
	public static HashMap<String, String> getMedieList
		(String salesman,String patient,String recipe_id,String c_buydrugitems,String c_drug_codes) {
		Map<String, String> params = getMapInstance();
		params.put("salesman", salesman);
		params.put("patient", patient);
		params.put("recipe_id", recipe_id);
		params.put("c_buydrugitems", c_buydrugitems);
		params.put("c_drug_codes", c_drug_codes);
		return (HashMap<String, String>) params;
	}

	public static Map<String,String>getSelfVisitParams(Context context,String addressName,String state,String doctorId,String doctorName,String remark,String id,String coordinate,String address
	,String deviceId,String orgId,String goodsGroupList,String urlList){
		Map<String, String> params = getMapInstance();
		params.put("access_token", SharedPreferenceUtil.getString(context, "session", ""));
		params.put("userId", SharedPreferenceUtil.getString(context, "id", ""));
		params.put("state", state);
		if(!TextUtils.isEmpty(doctorId)){
			params.put("doctorId", doctorId);
		}else{
			params.put("doctorId", "");
		}
		if(!TextUtils.isEmpty(doctorName)){
			params.put("doctorName", doctorName);
		}else{
			params.put("doctorName", "");
		}

		params.put("remark", remark);
		if(!TextUtils.isEmpty(id)){
			params.put("id", id);
		}

		params.put("coordinate", coordinate);
		params.put("address", address);
		params.put("addressName", addressName);

		params.put("deviceId", deviceId);
		params.put("orgId", orgId);
		params.put("companyId",SharedPreferenceUtil.getString(context, "enterpriseId", ""));
		if(!TextUtils.isEmpty(goodsGroupList)){
			params.put("goodsGroupList", goodsGroupList);
		}else{
			params.put("goodsGroupList", "[]");
		}
		params.put("imgUrls", urlList);
		return params;
	}

	public static Map<String,String>getVisitDetail(Context context,String id){
		Map<String, String> params = getMapInstance();
		params.put("access_token", SharedPreferenceUtil.getString(context, "session", ""));
		if(!TextUtils.isEmpty(id)){
			params.put("id", id);
		}
		Log.d("zxy", "461 : Params : getVisitDetail : id = "+id+", access_token = " +SharedPreferenceUtil.getString(context, "session", ""));
		return params;
	}

	public static Map<String, String> getVersionParams(Context context)  {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appCode", context.getPackageName());
		return params;
	}
	public static Map<String, String> getQRWebKeyParams(String  key)  {
		Map<String, String> params = new HashMap<String, String>();
		params.put("key", key);
		return params;
	}
	public static Map<String, String> getQRWebLoginParams(Context context ,String  key)  {
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_context", SharedPreferenceUtil.getString(context, "context_token", ""));
		params.put("key", key);
        return params;
	}
}
