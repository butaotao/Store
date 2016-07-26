package com.dachen.mediecinelibraryrealize.utils;

import android.content.Context;

import com.dachen.medicine.common.utils.DrugRemind;
import com.dachen.medicine.config.UserInfo;
import com.dachen.mediecinelibraryrealize.entity.AlarmBusiness;
import com.dachen.mediecinelibraryrealize.entity.DrugRemindDao;

import java.util.List;

/**
 * Created by Burt on 2016/3/21.
 */
public class StartAlarm {
    public static void startAlarmAgain(final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<DrugRemind>  drugDao = DrugRemindDao.getInstance(context).queryAll(UserInfo.getInstance(context).getId());
                if (drugDao!=null&&drugDao.size()>0){
                    for (int i=0;i<drugDao.size();i++){
                        AlarmBusiness.setAlarms(context, drugDao.get(i).alarms);
                    }
                }
            }
        }).start();

    }
}
