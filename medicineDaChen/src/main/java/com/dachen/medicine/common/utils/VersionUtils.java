package com.dachen.medicine.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.text.TextUtils;

/**
 * Created by gzhuo on 2016/6/17.
 */
public class VersionUtils {

    /**
     * 判断是否有新版本
     *
     * @param context
     * @param serverVersion 服务端版本号
     * @return
     */
    public static boolean hasNewVersion(Context context, String serverVersion) {
        if (TextUtils.isEmpty(serverVersion)) {
            return false;
        }
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String localVersion = info.versionName;
            if (TextUtils.isEmpty(localVersion)) {
                return false;
            }
            String[] serverVersionArr = serverVersion.split("\\.", -1);
            String[] localVersionArr = localVersion.split("\\.", -1);

            int len = Math.min(serverVersionArr.length, localVersionArr.length);
            for (int i = 0; i < len; i++) {
                int serverCode = Integer.parseInt(serverVersionArr[i]);
                int localCode = Integer.parseInt(localVersionArr[i]);
                if (serverCode > localCode) {
                    return true;
                } else if (serverCode < localCode) {
                    return false;
                }
            }

            if (serverVersionArr.length > localVersionArr.length) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
