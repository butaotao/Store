package com.dachen.dgroupdoctorcompany.utils;

import android.text.TextUtils;

import com.dachen.dgroupdoctorcompany.app.CompanyApplication;
import com.dachen.dgroupdoctorcompany.entity.LoginRegisterResult;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.SystemUtils;
import com.dachen.medicine.common.utils.TimeUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

/**
 * Created by Burt on 2016/8/15.
 */
public class Umeng {
    public static void getLoginRequestData( String phoneNum,String password){
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("phone",phoneNum);
        map.put("password",password);
        map.put("phoneType", SystemUtils.getDeviceId(CompanyApplication.context));
        map.put("time", TimeUtils.getTime() );
        map.put("loginParams", "time="+TimeUtils.getTime()+",phone="+phoneNum+
                ",password="+password+",phoneType="+SystemUtils.getDeviceId(CompanyApplication.context) );
        MobclickAgent.onEvent(CompanyApplication.context, "loginParams", map);
    }
    public static void getLoginServerData(LoginRegisterResult loginInfo,String phoneNum){
        HashMap<String,String> map = new HashMap<String,String>();

        map.put("resultMsg",loginInfo.resultMsg+"");
        map.put("phone",phoneNum);
        map.put("phoneType",SystemUtils.getDeviceId(CompanyApplication.context));
        map.put("time",TimeUtils.getTime());
        map.put("resultcode",loginInfo.getResultCode()+"");
        map.put("loginSevers", "time="+TimeUtils.getTime()+",phone="+phoneNum+
                ",resultcode="+loginInfo.getResultCode()+""+",phoneType="+SystemUtils.getDeviceId(CompanyApplication.context)+
                ",resultMsg="+loginInfo.resultMsg+"");
        MobclickAgent.onEvent(CompanyApplication.context, "loginSevers", map);
    }
    public static void getAutoLoginRequestData(String userId,String access_token){
        HashMap<String,String> map = new HashMap<String,String>();
        String phone = SharedPreferenceUtil.getString(CompanyApplication.context, "telephone", "");
        map.put("phone",phone);
        map.put("userId",userId);
        map.put("phoneType",SystemUtils.getDeviceId(CompanyApplication.context));
        map.put("time",TimeUtils.getTime());
        map.put("access_token",access_token);
        map.put("loginSevers", "time="+TimeUtils.getTime()+",phone="+phone+
                        ",access_token="+access_token+""+",phoneType="+SystemUtils.getDeviceId(CompanyApplication.context)+
                ",userId="+userId+"");
        MobclickAgent.onEvent(CompanyApplication.context, "autoLoginParams", map);
    }
    public static void getAutoLoginData(LoginRegisterResult loginInfo){
        HashMap<String,String> map = new HashMap<String,String>();

        String phone = SharedPreferenceUtil.getString(CompanyApplication.context, "telephone", "");
        map.put("resultMsg",loginInfo.resultMsg+"");
        map.put("time",TimeUtils.getTime());
        map.put("phoneType",SystemUtils.getDeviceId(CompanyApplication.context));
        map.put("resultcode",loginInfo.getResultCode()+"");
        map.put("phone", phone);
        map.put("loginSevers", "time="+TimeUtils.getTime()+",phone="+phone+
                        ",resultcode="+loginInfo.getResultCode()+""+""+",phoneType="+SystemUtils.getDeviceId(CompanyApplication.context)+
                ",resultMsg="+loginInfo.resultMsg+""+"");
        MobclickAgent.onEvent(CompanyApplication.context, "autoLoginSevers", map);
    }
}
