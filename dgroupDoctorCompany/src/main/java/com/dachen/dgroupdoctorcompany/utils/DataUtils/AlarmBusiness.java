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
    public static long alowTime = 10*60*1000;;
    public static void setAlarm(Context context, Reminder alarm){
        setAlarm(context, alarm, true);
    };
    public static void setAlarm(Context context, Reminder alarm,boolean firstSet) {


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
            int day = 0;
            if (isAlarmExpired(alarm)) {// 闹钟已过期则取消
                //if (firstSet){
                    day = 1;
               /* }else {
                    return;
                }*/

            }
            firstRingTime = firstRingTime+day*24*60*60*1000;
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    firstRingTime,
                    24*60*60*1000+(int)(Math.random()*1000*30), pendingIntents);
        }else {
          /*  if (isAlarmExpired(alarm)) {// 闹钟已过期则取消
               // cancelAlarm(context, alarm) ;
                return;
            }*/
            int day = 0;
            if (isAlarmExpired(alarm)) {// 闹钟已过期则取消
                //if (firstSet){
                    day = 1;
               /* }else {
                    return;
                }*/

            }
            firstRingTime = firstRingTime+day*24*60*60*1000;
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
        long curTime;
        if (alarm.times==0){
            curTime = alarm.updateTime;
        }else {
            curTime = System.currentTimeMillis();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(new Date(curTime));

        calendar.set(Calendar.YEAR,c.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, c.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR));

            calendar.set(Calendar.HOUR_OF_DAY, alarm.hour);
            calendar.set(Calendar.MINUTE, alarm.minute);
        long  alermTime = calendar.getTimeInMillis();
        return alermTime;
    }

    public static boolean getNextAlarmTimeInMillis2(Reminder alarm) {
        Calendar calendar = Calendar.getInstance();
        long curTime = 0;
        if (alarm.times==0){
            curTime = alarm.updateTime;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(new Date(curTime));

        calendar.set(Calendar.YEAR,c.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, c.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR));

        calendar.set(Calendar.HOUR_OF_DAY, alarm.hour);
        calendar.set(Calendar.MINUTE, alarm.minute);
        long  alermTime = calendar.getTimeInMillis();

        boolean flag = false;
        int day = 0;
        if ((alermTime-alarm.updateTime)>0) {
            flag = false;//m没有过期
            day = 0;
        } else {
            flag = true;
            day = 1;
            Calendar c2 = Calendar.getInstance();
            Calendar c1 = Calendar.getInstance();
            c1.setTime(new Date(curTime));

            c2.set(Calendar.YEAR, c1.get(Calendar.YEAR));
            c2.set(Calendar.MONTH, c1.get(Calendar.MONTH));
            c2.set(Calendar.DAY_OF_YEAR, c1.get(Calendar.DAY_OF_YEAR)+1);

            c2.set(Calendar.HOUR_OF_DAY, alarm.hour);
            c2.set(Calendar.MINUTE, alarm.minute);
            alermTime = c2.getTimeInMillis();
        }
        long wucha = Math.abs(alermTime - System.currentTimeMillis());
        if ( wucha <= alowTime){
            return true;
        }else {
            return false;
        }

    }

    public static boolean getNextAlarmTimeInMillis3(Reminder alarm) {
        Calendar calendar = Calendar.getInstance();
        long curTime = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(curTime));
        calendar.set(Calendar.YEAR,c.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, c.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR));
        calendar.set(Calendar.HOUR_OF_DAY, alarm.hour);
        calendar.set(Calendar.MINUTE, alarm.minute);
        long  alermTime = calendar.getTimeInMillis();
        long wucha = Math.abs(alermTime-curTime);
        if ( wucha <= alowTime){
            return true;
        }else {
            return false;
        }

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
