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

        long firstRingTime = getNextAlarmTimeInMillis( alarm);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                firstRingTime,
                24*60*60*1000+(int)(Math.random()*1000*30), pendingIntents);
        }

    }
    public static long getNextAlarmTimeInMillis(Reminder alarm) {
        Calendar calendar = Calendar.getInstance();

       long curTime = alarm.createTime;
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(curTime));
      /*   int dayInWeek = c.get(Calendar.DAY_OF_WEEK);
        String week = TimeUtils.getWeekStr(dayInWeek);
        Collection<WeekSinger> weeks = alarm.weeks;
        for (WeekSinger weekSinger:weeks){
            weekSinger
        }*/
        calendar.set(Calendar.YEAR,c.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH,c.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR));
        calendar.set(Calendar.HOUR, Integer.parseInt(alarm.hour));
        calendar.set(Calendar.MINUTE,Integer.parseInt(alarm.minute));


        long  alermTime = calendar.getTimeInMillis();
        return alermTime;
    }
}
