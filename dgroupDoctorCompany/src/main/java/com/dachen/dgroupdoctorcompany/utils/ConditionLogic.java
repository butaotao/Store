package com.dachen.dgroupdoctorcompany.utils;

import android.content.Context;

import com.dachen.medicine.common.utils.SharedPreferenceUtil;

/**
 * Created by Burt on 2016/9/9.
 */
public class ConditionLogic {
    public static boolean isAllow(Context context){
       if (SharedPreferenceUtil.getString(context, "enterpriseId", "").equals("577e2c24f509e2557056a184")){
           return false;
       };
        return  true;
    }
    public static boolean isAllowCall(String userId){
        if (userId.equals("19833")){
            return false;
        }
        return true;
    }
}
