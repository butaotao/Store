package com.dachen.dgroupdoctorcompany.utils;

import android.content.Context;

import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.entity.ServerTimeBean;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;

/**
 * @项目名 MedicineProject
 * @Author: zxy on 16/9/9上午11:15.
 * @描述 公共的Http请求工具类
 */
public class HttpUtil {
    /**
     * 获取服务器当前时间
     * @param context context
     * @param onHttpListener onHttpListener
     */
    public static void getServiceTime(Context context,HttpManager.OnHttpListener< Result > onHttpListener){
        new HttpManager().post(context, Constants.GET_SERVERTIME, ServerTimeBean.class,
                Params.getServerTime(context),onHttpListener, false, 1);
    }
}
