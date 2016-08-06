package com.dachen.dgroupdoctorcompany.utils.DataUtils;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dachen.common.utils.TimeUtils;
import com.dachen.dgroupdoctorcompany.db.dbentity.Reminder;
import com.dachen.medicine.common.utils.Alarm;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Burt on 2016/8/3.
 */
public class AlarmBusiness {
    public static void setAlarm(Context context, Reminder alarm) {


    Intent intent = new Intent("com.dachen.dgrouppatient.receiver.AlarmReceivers");
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    Bundle bundle = new Bundle();
    bundle.putSerializable("alarm", (Serializable) alarm);
    intent.putExtra("alarm", bundle);
        if (alarm!=null){
        PendingIntent pendingIntents = PendingIntent.getBroadcast(context, alarm._id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long firstRingTime = getNextAlarmTimeInMillis(alarm);
        if (alarm.times!=0){
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    firstRingTime,
                    24*60*60*1000+(int)(Math.random()*1000*30), pendingIntents);
        }else {
            if (isAlarmExpired(alarm)) {// 闹钟已过期则取消
               // cancelAlarm(context, alarm) ;
                return;
            }
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    firstRingTime,
                    pendingIntents);
        }
        }
    }
    public static boolean isAlarmExpired(Reminder alarm) {
        long time = alarm.time;
        // 设置时间为23:59:59，即以过期那天的最后一刻作为是否过期标准
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        long endTime = getNextAlarmTimeInMillis(alarm);
        // endTime = time+24*60*60*1000*repeatDays;
        long nowTime = System.currentTimeMillis();
        boolean flag = false;
        if ((endTime-nowTime)>0) {
            flag = false;//m没有过期
        } else {
            flag = true;

            long curTime = alarm.updateTime;
            Calendar c = Calendar.getInstance();
            c.setTime(new Date(curTime));
           /* if (c.get(Calendar.HOUR_OF_DAY)==alarm.hour&&c.get(Calendar.MINUTE)==alarm.minute){
                return true;
            }*/
        }
        return  flag;
    }
    public static long getNextAlarmTimeInMillis(Reminder alarm) {
        Calendar calendar = Calendar.getInstance();
        long nowTime = System.currentTimeMillis();
       long curTime = alarm.updateTime;
        Calendar c = Calendar.getInstance();
        Calendar cnow = Calendar.getInstance();
        c.setTime(new Date(curTime));
        cnow.setTime(new Date(nowTime));
      /*   int dayInWeek = c.get(Calendar.DAY_OF_WEEK);
        String week = TimeUtils.getWeekStr(dayInWeek);
        Collection<WeekSinger> weeks = alarm.weeks;
        for (WeekSinger weekSinger:weeks){
            weekSinger
        }*/
        calendar.set(Calendar.YEAR,c.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, c.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR));
        int hour = cnow.get(Calendar.HOUR_OF_DAY);
        int minute = cnow.get(Calendar.MINUTE);
        /*if (hour==alarm.hour&&minute==alarm.minute){
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE,minute);
            calendar.set(Calendar.SECOND,cnow.get(Calendar.SECOND)+3);
        }else {*/
            calendar.set(Calendar.HOUR_OF_DAY, alarm.hour);
            calendar.set(Calendar.MINUTE, alarm.minute);
       // }
        long  alermTime = calendar.getTimeInMillis();
        return alermTime;
    }
    public static void cancelAlarm(Context context, Reminder alarm) {
        if (alarm == null) {
            return;
        }
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("com.dachen.dgrouppatient.broadcast.AlarmReceivers");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm._id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }
}
