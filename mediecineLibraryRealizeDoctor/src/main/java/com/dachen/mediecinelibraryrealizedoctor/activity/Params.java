package com.dachen.mediecinelibraryrealizedoctor.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;

import com.dachen.medicine.common.utils.SharedPreferenceUtil;
 
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

	public static Map<String, String> getInterface(String interface1) {
		Map<String, String> params = getMapInstance();
		params.put("interface1", interface1);
		return params;

	}

 
	public static Map<String, String> getLoginoutParams(Context context, String serial) {
		Map<String, String> params = getMapInstance();
		params.put("userKey", ""); // 没有用userKey校验 ，但是这个参数还要传，现在 还没去掉 
		params.put("access_token",SharedPreferenceUtil.getString(context,"session", ""));
		params.put("serial", serial);
		return params;

	}
	// (phone,userType,smsid,ranCode)
	public static Map<String, String> getResetPasswordParams(String phone,
			String userType, String smsid, String ranCode, String password) {
		Map<String, String> params = getMapInstance();
		params.put("phone", phone);
		params.put("userType", userType);
		params.put("smsid", smsid);
		params.put("ranCode", ranCode);
		params.put("password", password);

		return params;

	}

	public static Map<String, String> getSendMSMParams(String phoneNumber,
			String templateId) {
		Map<String, String> params = getMapInstance();

		params.put("telephone", phoneNumber);
		params.put("templateId", templateId);// 短信模板。
		return params;
	}

	public static Map<String, String> getGoodsInfo(String group,String category,
			String __PAGE__) {
		Map<String, String> params = getMapInstance();
		if (!TextUtils.isEmpty(group)) {
			 params.put("group", group);
		}
		
		params.put("category", category); 
		return params;
	}
	public static Map<String, String> getSearchInfo(String keyword,String page,
			String size) {
		Map<String, String> params = getMapInstance();
		params.put("__KEYWORD__", keyword);
		params.put("__PAGE__", page);
		params.put("__PAGE_SIZE__", size);
		return params;
	}
	public static Map<String, String> getSearchInfo(String keyword) {
		Map<String, String> params = getMapInstance();
		params.put("__KEYWORD__", keyword); 
		return params;
	}
	public static Map<String, String> getMedieInfo(String keyword ) {
		Map<String, String> params = getMapInstance();
		params.put("category", keyword); 
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
	public static HashMap<String, String> getMedicineList(String recipe_id) {
		Map<String, String> params = getMapInstance();
		params.put("recipe_id", recipe_id);
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
}
