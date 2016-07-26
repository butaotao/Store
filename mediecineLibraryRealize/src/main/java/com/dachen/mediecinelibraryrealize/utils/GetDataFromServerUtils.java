package com.dachen.mediecinelibraryrealize.utils;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.dachen.medicine.common.utils.Alarm;
import com.dachen.medicine.common.utils.DrugRemind;
import com.dachen.medicine.config.AppConfig;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.mediecinelibraryrealize.activity.SetAlertActivity;
import com.dachen.mediecinelibraryrealize.entity.AlarmBusiness;
import com.dachen.mediecinelibraryrealize.entity.AlarmDao;
import com.dachen.mediecinelibraryrealize.entity.AlarmInfo;
import com.dachen.mediecinelibraryrealize.entity.DrugRemindDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Burt on 2016/3/19.
 */
public class GetDataFromServerUtils {
    //  HashMap<String,String> maps = new HashMap<>();

    public static void getData(final Context context, Map<String, String> maps, String session, final String id, final String name) {


        String[] s;
        s = maps.get(id).split(",");

        for (int i = 0; i < s.length; i++) {
            final Map<String, String> params = new HashMap<>();
            String idq = s[i].replace("\"", "");
            idq = idq.replace("[", "");
            idq = idq.replace("]", "");
            params.put("id", idq);
            params.put("access_token",UserInfo.getInstance(context).getSesstion());
            final String names = "org/drugReminder/getDoseReminderById";
          /*  String data = "web/api/data/"+session;
            String url = AppConfig.getUrlByParams(data, names);*/
            new HttpManager().post(context,
                    names,
                    AlarmInfo.class,
                    params,
                    new HttpManager.OnHttpListener<Result>() {
                        @Override
                        public void onSuccess(Result response) {
                            if ((response instanceof AlarmInfo)&&response.resultCode ==1) {
                                processData(context, (AlarmInfo) response, false);
                            }
                        }

                        @Override
                        public void onSuccess(ArrayList<Result> response) {

                        }

                        @Override
                        public void onFailure(Exception e, String errorMsg, int s) {

                        }
                    }, false, 2);
        }

    }

    public static void processData(Context context, AlarmInfo alarm, boolean addalarm) {
        if (null != alarm) {
            if (!TextUtils.isEmpty(alarm.patientId + "")) {
                DrugRemindDao.getInstance(context).clearByID("##112233##", alarm.patientId + "");
            }

            AlarmInfo info = alarm;
            List<DrugRemind> haveremind =
                    DrugRemindDao.getInstance(context).queryByids(info.id, alarm.patientId);
            DrugRemind remind = new DrugRemind();
            if (null != haveremind && haveremind.size() > 0) {
                remind._id = haveremind.get(0)._id;
            } else {
                remind._id = -1;
            }
            String begindate = "";
            if (!TextUtils.isEmpty(info.start_date)) {
                begindate = com.dachen.medicine.common.
                        utils.TimeUtils.getTime(Long.parseLong(info.start_date));
            }

            remind.begindata = begindate;
            remind.idmedie = info.goodsId;
            remind.drugName = info.goodsName;
            remind.goods_dqsy = info.goods_dqsy;
            remind.goods_mcfy = info.goods_mcfy;
            remind.ownerUserId = UserInfo.getInstance(context).getId() + "";
            remind.patientId = alarm.patientId;
            if (info.status == 1) {
                remind.isRemind = true;
            } else {
                remind.isRemind = false;
            }
            remind.patientName = alarm.patientName;
            remind.repeatDayIndex = info.days_remind_cx;
            remind.repeatPeriodIndex = info.days_remind_jg;
            remind.id = info.id;
            remind.soundDesc = info.remind_ly;
            remind.createTime = Long.parseLong(info.number);
            for (int j = 0; j < SetAlertActivity.mSoundDescs.size(); j++) {
                if (SetAlertActivity.mSoundDescs.get(j).contains(info.remind_ly)) {
                    remind.soundName = SetAlertActivity.mSoundNames.get(j);
                    remind.soundIndex = j;
                    break;
                }
            }
            Collection<Alarm> alarms = new ArrayList<Alarm>();
            Alarm alar1 = addAlarm(context, info.reminderTime1, remind, 1);
            if (alar1.add){
                alarms.add(alar1);
            }
            Alarm alarm2 = addAlarm(context, info.reminderTime2, remind, 2);
            if (alarm2.add){
                alarms.add(alarm2);
            }
            Alarm alarm3 = addAlarm(context, info.reminderTime3, remind, 3);
            if (alarm3.add){
                alarms.add(alarm3);
            }
            Alarm alarm4 = addAlarm(context, info.reminderTime4, remind, 4);
            if (alarm4.add){
                    alarms.add(alarm4);
            }

            remind.alarms = alarms;
            List<DrugRemind> druglist = new ArrayList<DrugRemind>();
            if (!TextUtils.isEmpty(alarm.patientId + "")) {
                druglist = DrugRemindDao.getInstance(context).queryByids(info.id, alarm.patientId + "");
            }

            if (addalarm) {
                AlarmBusiness.setAlarms(context, (List<Alarm>) alarms);
            }
            if (druglist == null || druglist.size() == 0) {

                DrugRemindDao.getInstance(context).addOrUpdate(remind);
                for (Alarm alarm1 : remind.alarms) {
                    AlarmDao.getInstance(context).createOrUpdate(alarm1);
                }
                if (!addalarm) {
                    AlarmBusiness.setAlarms(context, (List<Alarm>) alarms);
                }
            } else {
            }

        }
    }

    public static Alarm addAlarm(Context context, String time, DrugRemind remind,int position) {
        Alarm ala4 = new Alarm();
        ala4.add = false;
        if (!TextUtils.isEmpty(time) && time.length() >= 3) {
            String time4 = time;
            ala4.drugRemind = remind;
            ala4.add = true;
            ala4.hour = Integer.parseInt(time4.substring(0, 2));
            ala4.minute = Integer.parseInt(time4.substring(3));
            ala4.index = position;
            ala4.number = position;
            ala4.times = 0;
            ala4.eat = 0;
            ArrayList<Alarm> alarmupdates4 = (ArrayList<Alarm>)
                    AlarmDao.getInstance(context).
                            queryByCreateTime(ala4);
            if (null != alarmupdates4 && alarmupdates4.size() > 0) {
                ala4._id = alarmupdates4.get(0)._id;
            } else {
                ala4._id = -1;
            }
        } else {
            return ala4;
        }
        return ala4;
    }
}
