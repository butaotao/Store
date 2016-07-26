package com.dachen.medicine.net;

import android.content.Context;

import com.dachen.medicine.common.utils.SharedPreferenceUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @描述： @网络请求参数封装
 * @作者： @蒋诗朋
 * @创建时间： @2014-11-27
 */
public class Params {
    public static Map<String, String> getMapInstance() {
        Map<String, String> map = new HashMap<String, String>();
        return map;
    }

    public static Map<String, String> getLoginVerifyParams(String phoneNum,
                                                           String password) {
        Map<String, String> params = getMapInstance();
        params.put("u", phoneNum);
        params.put("p", password);

        return params;

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

    public static Map<String, String> getLoginParams(String phoneNum,
                                                     String password, String userType) {
        Map<String, String> params = getMapInstance();
        params.put("telephone", phoneNum);
        params.put("password", password);
        params.put("userType", userType);
        params.put("serial",
                SharedPreferenceUtil.getString("mRegId",""));
        params.put("model", "android");
        return params;

    }

/*    public static Map<String, String> getReginsterXiaoMiReceiver(String uid,
                                                                 String deviceToken, String session) {
        Map<String, String> params = getMapInstance();
        params.put("uid", uid);
        params.put("client", "android");
        params.put("userType", "9");
        params.put("deviceToken", deviceToken);
        params.put("access_token", session);
        return params;

    }*/
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
    public static Map<String, String> getRemoveReginsterXiaoMiReceiver(String uid,
                                                                       String deviceToken, String session) {
        Map<String, String> params = getMapInstance();
        params.put("serial", deviceToken);
        params.put("access_token", session);
        return params;

    }

    public static Map<String, String> getLoginoutParams(String serial) {
        Map<String, String> params = getMapInstance();
        params.put("userKey", ""); // 没有用userKey校验 ，但是这个参数还要传，现在 还没去掉
        params.put("access_token", SharedPreferenceUtil.getString("session", ""));
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

    public static HashMap<String, String> getMedicineRecord(String page,
                                                            String pageSize, String filter, String quantity) {
        Map<String, String> params = getMapInstance();
        params.put("__PAGE__", page);
        params.put("__PAGE_SIZE__", pageSize);
        params.put("__FILTER__", filter);
        params.put("quantity", quantity);
        return (HashMap<String, String>) params;
    }

    public static HashMap<String, String> getMedicineRecord2(String page,
                                                             String pageSize, String filter1, String filter2, String quantity) {
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

    public static HashMap<String, String> getMedicineListGive(String goodsid, String patientid) {
        Map<String, String> params = getMapInstance();
        params.put("patient", patientid);
        params.put("goods", goodsid);
        return (HashMap<String, String>) params;
    }

    public static HashMap<String, String> getPatientID(String recipe_id) {
        Map<String, String> params = getMapInstance();
        params.put("id", recipe_id);
        return (HashMap<String, String>) params;
    }

    public static HashMap<String, String> getMedieList
            (String salesman, String patient, String recipe_id, String c_drug_codes) {
        Map<String, String> params = getMapInstance();
        //params.put("salesman", salesman);
        params.put("access_token", SharedPreferenceUtil.getString("session",""));
        params.put("recipeId", recipe_id);
        params.put("buyDrugItemJson", c_drug_codes);
        return (HashMap<String, String>) params;
    }

    public static HashMap<String, String> getMedieListGiveMedieList
            (String patient, String goodsid, String buynum, String c_drug_codes) {
        Map<String, String> params = getMapInstance();
        //params.put("salesman", salesman);
        params.put("patientId", patient);
        params.put("quantity", buynum);
        params.put("goodsId", goodsid);
        params.put("access_token",SharedPreferenceUtil.getString("session",""));
        params.put("c_drug_codes", c_drug_codes);
        return (HashMap<String, String>) params;
    }

    public static HashMap<String, String> getBaseIncomeInfo(String userId) {
        Map<String, String> params = getMapInstance();
        params.put("userId", userId);
        params.put("access_token", SharedPreferenceUtil.getString("session", ""));
        return (HashMap<String, String>) params;
    }

    public static HashMap<String, String> getAllIncomeList(String userId, String pageIndex, String pageSize) {
        Map<String, String> params = getMapInstance();
        params.put("userId", userId);
        params.put("access_token", SharedPreferenceUtil.getString("session", ""));
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        return (HashMap<String, String>) params;
    }

    public static HashMap<String, String> getUnCheckIncomeList(String bizId, String bizType,String pageIndex, String pageSize) {
        Map<String, String> params = getMapInstance();
        params.put("bizId", bizId);
        params.put("bizType",bizType);
        params.put("access_token", SharedPreferenceUtil.getString("session", ""));
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        return (HashMap<String, String>) params;
    }

    public static HashMap<String, String> getAllIncomeDetail(String userId, String goodsId, String pageIndex, String pageSize) {
        Map<String, String> params = getMapInstance();
        params.put("userId", userId);
        params.put("goodsId", goodsId);
        params.put("access_token", SharedPreferenceUtil.getString("session", ""));
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        return (HashMap<String, String>) params;
    }

    public static HashMap<String, String> getUnCheckIncomeDetail(String bizId, String bizType,String goodsId, String pageIndex,
                                                                 String pageSize) {
        Map<String, String> params = getMapInstance();
        params.put("bizId", bizId);
        params.put("bizType",bizType);
        params.put("goodsId", goodsId);
        params.put("access_token", SharedPreferenceUtil.getString("session", ""));
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        return (HashMap<String, String>) params;
    }

    public static HashMap<String, String> getBlanceDetail(String bizId, String pageIndex, String pageSize) {
        Map<String, String> params = getMapInstance();
        params.put("bizId", bizId);
        params.put("access_token", SharedPreferenceUtil.getString("session", ""));
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        return (HashMap<String, String>) params;
    }

    public static Map<String, String> getVersionParams(Context context)  {
        Map<String, String> params = new HashMap<String, String>();
        params.put("appCode", context.getPackageName());
        return params;
    }
}
