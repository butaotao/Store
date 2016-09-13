package com.dachen.dgroupdoctorcompany.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.dachen.dgroupdoctorcompany.im.utils.ChatActivityUtilsV2;
import com.dachen.imsdk.entity.HwPushBean;

public class HwPushService extends Service {
    public static final String EXTRA_MSG = "msg";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void startAction(Context context, String msg) {
        Intent intent = new Intent(context, HwPushService.class);
        intent.putExtra(EXTRA_MSG, msg);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String jsonStr = "";
        if (intent!=null){
             jsonStr=intent.getStringExtra(EXTRA_MSG);
        }
        if (!TextUtils.isEmpty(jsonStr)){
            HwPushBean bean= JSON.parseObject(jsonStr,HwPushBean.class);
            String groudId = (String) bean.param.get("groupId");
            if (groudId != null) {
                String rtype = (String) bean.param.get("rtype");
                ChatActivityUtilsV2.openUI(this, groudId, rtype);

            }
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
