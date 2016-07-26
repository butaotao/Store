package com.dachen.medicine.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Burt on 2016/7/8.
 */
public class AppUtils {
    public static String getVersion(Context context) {
        try {
            if (null!=context){
                PackageManager manager = context.getPackageManager();
                PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
                return   info.versionName;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }
}
