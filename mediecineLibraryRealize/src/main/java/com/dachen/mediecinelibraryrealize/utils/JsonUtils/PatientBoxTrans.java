package com.dachen.mediecinelibraryrealize.utils.JsonUtils;

import android.text.TextUtils;

import com.dachen.mediecinelibraryrealize.entity.AlarmsTime;
import com.dachen.mediecinelibraryrealize.entity.PatientBox;
import com.dachen.mediecinelibraryrealize.entity.PatientBoxs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Burt on 2016/6/7.
 */
public class PatientBoxTrans {
 /*  public List<List<AlarmsTime.AlarmsTimeInfo>> list_datas(ArrayList<PatientBoxs> data){
        List<List<AlarmsTime.AlarmsTimeInfo>> lists = new ArrayList<>();
        if (null!=data&&data.size()>0){
            for (int i=0;i<data.size();i++){
                List<AlarmsTime.AlarmsTimeInfo> dataList = new ArrayList<>();
                for (int j=0;j<data.get(i).doseRecordList.size();j++){
                    PatientBox b= data.get(i).doseRecordList.get(j);
                    AlarmsTime alarmsTime = new AlarmsTime();
                    AlarmsTime.AlarmsTimeInfo info = alarmsTime.new AlarmsTimeInfo();
                    //info.goods_name = b.
                    if (!TextUtils.isEmpty(b.reminderRecord)&&b.reminderRecord.equals("2")){
                        info.is_done = true;
                    }else {
                        info.is_done = false;
                    }
                    info.time=b.doseDate;
                       info.time_type
                }
            }
        }
    }*/
}
