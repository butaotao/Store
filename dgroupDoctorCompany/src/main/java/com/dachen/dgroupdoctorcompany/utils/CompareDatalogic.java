package com.dachen.dgroupdoctorcompany.utils;

import android.text.TextUtils;

import com.dachen.dgroupdoctorcompany.app.CompanyApplication;

/**
 * Created by Burt on 2016/2/22.
 */
public class CompareDatalogic {

    public static String getShowName(String generanName,String tradeName,String goodsName){
        String name = "";
        if (!TextUtils.isEmpty(goodsName)) {
            name = goodsName;
        }else if(!TextUtils.isEmpty(generanName)){
            name = generanName;
        }else if(!TextUtils.isEmpty(tradeName)){
            name= tradeName;
        }
        if (null != name && name.length() > 9) {
            name = name.substring(0, 6) + "..."
                    + name.substring(name.length() - 2);
        }
        return name;
    }
    public static boolean isInitContact(){
        return CompanyApplication.getInitContactList() != 2;
    }
}
