package com.dachen.mediecinelibraryrealize.utils.JsonUtils;

import android.text.TextUtils;

import com.dachen.mediecinelibraryrealize.entity.AlarmsTime;
import com.dachen.mediecinelibraryrealize.entity.PatientBox;
import com.dachen.mediecinelibraryrealize.entity.PatientBoxs;
import com.dachen.mediecinelibraryrealize.entity.PatientBoxsData;
import com.dachen.mediecinelibraryrealize.entity.Patients;
import com.dachen.mediecinelibraryrealize.utils.AlarmData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Burt on 2016/6/8.
 */
public class BoxTransLate {
    public static ArrayList<AlarmData> getAlarm(ArrayList<PatientBoxs> data){
        ArrayList<ArrayList<AlarmsTime.AlarmsTimeInfo>> alarmsTimes = new ArrayList<>();
        ArrayList<AlarmData> datas = new ArrayList<>();
       if (null!=data&&data.size()>0){
            for (int i=0;i<data.size();i++){
                AlarmData adata = new AlarmData();
                PatientBoxs boxs = data.get(i);
                ArrayList<AlarmsTime.AlarmsTimeInfo> list_datas = new ArrayList<>();
                AlarmsTime alarmsTime = new AlarmsTime();
                if (null!=boxs.doseRecordList&&boxs.doseRecordList.size()>0){
                    for (int j=0;j<boxs.doseRecordList.size();j++){
                        AlarmsTime.AlarmsTimeInfo info = alarmsTime.new AlarmsTimeInfo();
                        PatientBox box = boxs.doseRecordList.get(j);
                        info.goods_name =box.goodsName;
                        info.title = box.title;
                        info.id = box.id;
                        info.time = box.reminderTime;
                        info.time_type = box.timeType;
                        if (!TextUtils.isEmpty(box.reminderRecord)&&box.reminderRecord.equals("2")){
                            info.is_done = true;
                        }else {
                            info.is_done = false;
                        }

                        list_datas.add(info);
                    }
                }
                    alarmsTimes.add(list_datas);
                    adata.alarmsTimes = list_datas;
                    adata.patientName = boxs.patientName;
                    adata.patientId = boxs.patientId;
                    adata.logo = boxs.patientIconPath;
                    adata.recipeNumber = boxs.recipeNumber;
                    adata.reminderNumer = boxs.reminderNumer;
                    adata.pointsNumber = boxs.pointsNumber;

                    datas.add(adata);

            }
       }
        return datas;

    }
   public static List<Patients.patient> getPatients(ArrayList<AlarmData> alarmDatas){

       List<Patients.patient> list = new ArrayList<>();
       if (null!=alarmDatas&&alarmDatas.size()>0){
           for (int i=0;i<alarmDatas.size();i++){
               Patients patients = new Patients();
               Patients.patient p = patients.new patient();
               AlarmData data = alarmDatas.get(i);
               p.num_recipes = data.recipeNumber;
               p.num_remind = data.reminderNumer;
               p.logo = data.logo;
               p.num_dhs = data.pointsNumber;
               p.id = data.patientId;
               p.user_name = data.patientName;
               list.add(p);
           }
       }
       return list;
   }
}
