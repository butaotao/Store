package com.dachen.dgroupdoctorcompany.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.dachen.common.toolbox.CommonUiTools;
import com.dachen.dgroupdoctorcompany.activity.LoginActivity;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.imsdk.ImSdk;
import com.dachen.medicine.common.utils.Alarm;
import com.dachen.medicine.common.utils.CallApplicationInterface;
import com.dachen.medicine.common.utils.DrugRemind;
import com.dachen.medicine.common.utils.IllEntity;
import com.dachen.medicine.common.utils.MActivityManager;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.j256.ormlite.dao.Dao;

import java.util.HashMap;

/**
 * Created by Burt on 2016/4/19.
 */
public class Configs implements CallApplicationInterface {
    @Override
    public HashMap<String, String> getInterfaceMaps() {
        return null;
    }

    @Override
    public Bitmap getBitmap(String str) {
        return null;
    }

    @Override
    public Dao<Alarm, Integer> getInterfacedbAlarm() {
        return null;
    }

    @Override
    public Dao<DrugRemind, Integer> getInterfacedbDrugRemind() {
        return null;
    }

    @Override
    public Dao<IllEntity, Integer> getInterfaceIllEntity() {
        return null;
    }

    @Override
    public void resumeIm() {

    }

    @Override
    public void pauseIm() {

    }

    @Override
    public int startLoginActivity(Context context) {
        MActivityManager.getInstance().finishAllActivity();
        SharedPreferenceUtil.putString(context, "session", "");
        SharedPreferenceUtil.putLong(context,"expires_in", 0L);
        // TODO Auto-generated method stub
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        ImSdk.getInstance().logout();
        return 0;
    }

    @Override
    public void showUpdateDilog(Context context,String s) {
        CommonUiTools.getInstance().appVersionUpdate(context, s);
        closeDialog(context);
    }
    @Override
    public void closeDialog(Context activity) {
        if (null!=activity) {
            if (activity instanceof BaseActivity) {
                BaseActivity activity1 = (BaseActivity) activity;
                activity1.closeLoadingDialog();
            }
        }
    }
}
