package com.dachen.dgroupdoctorcompany.utils;

/**
 * Created by Burt on 2016/9/1.
 */
public class TextUtils {
    public static boolean containsChinese(String s){
        String reg = "[\\u4e00-\\u9fa5]+";//
        if (!android.text.TextUtils.isEmpty(s)&&s.contains(s)){
            return true;
        }
        return false;
    }

}
