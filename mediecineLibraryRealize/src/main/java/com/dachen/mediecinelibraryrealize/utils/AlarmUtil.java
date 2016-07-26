package com.dachen.mediecinelibraryrealize.utils;

import android.text.TextUtils;

import com.dachen.medicine.common.utils.Alarm;

/**
 * Created by Burt on 2016/3/28.
 */
public class AlarmUtil {
    public static int getId(Alarm alarm){
        String id = "";
        if (null==alarm.drugRemind|| TextUtils.isEmpty(alarm.drugRemind.createTime+"")){
           id = System.currentTimeMillis()+"";
        }else {
            id = alarm.drugRemind.createTime+"" ;
        }
        if (id.length()>10){
           id =  id.substring(8,id.length());
        }
        id = id+alarm.index;
        return Integer.parseInt(id);
    }
}
