package com.dachen.mediecinelibraryrealize.entity;



import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.dachen.medicine.common.utils.Alarm;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.SystemUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.mediecinelibraryrealize.utils.AlarmUtil;
import com.dachen.mediecinelibraryrealize.utils.TimeUtils;

/**
 * 处理闹钟的业务类
 * 
 * @author gaozhuo
 * @date 2015年12月10日
 */
public class AlarmBusiness {

	public static void setAlarm(Context context, Alarm alarm) {
		if (alarm == null || alarm.drugRemind == null) {
			return;
		}
	 	if (isAlarmExpired(alarm, alarm.drugRemind.begindata, alarm.drugRemind.repeatDayIndex)) {// 闹钟已过期则取消
			cancelAlarm(context, alarm) ;
			return;
		}
	/*	if (alarm.drugRemind.repeatPeriodIndex == 0) {// 每天
			if (getAlarmTimeInMillis(alarm.hour, alarm.minute) <= System.currentTimeMillis()) {// 闹钟时间已过,取消闹钟
			 	cancelAlarm(context, alarm);
				return;
			}
		}*/
		AlarmDao.getInstance(context).update(alarm);
		Intent intent = new Intent("com.dachen.dgrouppatient.broadcast.AlarmReceiver");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Bundle bundle = new Bundle();
		bundle.putSerializable("alarm", (Serializable) alarm);
		intent.putExtra("alert", "aa");
		intent.putExtra("alarm", bundle);

		PendingIntent pendingIntents = PendingIntent.getBroadcast(context, AlarmUtil.getId(alarm), intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		if (null!=alarm.drugRemind) {
			long firstRingTime = getNextAlarmTimeInMillis(alarm.hour, alarm.minute, alarm);
			if (firstRingTime!=-1){
				//续时间为1天的除外
				if (alarm.drugRemind.repeatDayIndex!=0
					/*	//持续时间为1天的除外
						&&alarm.drugRemind.repeatDayIndex!=1*/
						//间隔天数如果选择不重复则除外
						/*&&alarm.drugRemind.repeatPeriodIndex!=0*/) {
					//alarmManager.setInexactRepeating();
					//随机数是解决相同的闹钟同时响的问题
					alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
							firstRingTime,
							(alarm.drugRemind.repeatPeriodIndex+1)*24*60*60*1000+(int)(Math.random()*1000*30), pendingIntents);
				}else{
					alarmManager.set(AlarmManager.RTC_WAKEUP,
							firstRingTime,
							pendingIntents);
				}
			}
		}
	}

	public static void setAlarmRepeatFive(Context context, Alarm alarm) {
		if (alarm == null || alarm.drugRemind == null) {
			return;
		}
		Intent intent = new Intent("com.dachen.dgrouppatient.broadcast.AlarmReceiver");
		Bundle bundle = new Bundle();
		bundle.putSerializable("alarm", (Serializable) alarm);
		intent.putExtra("fiveminutes", "fiveminutes");
		intent.putExtra("alarm", bundle);
		//101010为每5分钟一次的广播id
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AlarmUtil.getId(alarm) + 10000000, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		 if (null!=alarm.drugRemind) {
				alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5 * 60 * 1000 + 30,
						pendingIntent);
		}

	}
	public static long getNextAlarmTimeInMillis(int hour, int minute, Alarm alarm) {
		Calendar calendar = Calendar.getInstance();
		long time = TimeUtils.getDays(alarm.drugRemind.begindata);
		calendar.setTimeInMillis(time);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		long nowTime = System.currentTimeMillis();
		long alermTime = calendar.getTimeInMillis();
		// 如果闹钟的时间今天已过，则加上离它下一次铃响的天数,闹钟没有过期的前提下

 if (alermTime < nowTime) {

			int between_days=(int)((nowTime-alermTime)/(1000*3600*24));
				int day = 0;
			if (between_days==0){
				day = alarm.drugRemind.repeatPeriodIndex+1;
			}else {
				int d = 0;
					d = alarm.drugRemind.repeatDayIndex/(alarm.drugRemind.repeatPeriodIndex+1);

				for (int i=0;i<=d;i++){
					Calendar calendar2 = Calendar.getInstance();
					calendar2.setTimeInMillis(time);
					calendar2.set(Calendar.HOUR_OF_DAY, hour);
					calendar2.set(Calendar.MINUTE, minute);
					calendar2.set(Calendar.SECOND, 0);
					calendar2.set(Calendar.MILLISECOND, 0);
					calendar2.add(Calendar.DAY_OF_YEAR, i*(alarm.drugRemind.repeatPeriodIndex+1));
					long ringtime = calendar2.getTimeInMillis();
					//当所有闹钟中遇到第一个闹钟大于现在时间时候，那么重新设置的闹钟就从这个闹钟开始
					if (ringtime>nowTime){
						if (i>0){
							day =  i*(alarm.drugRemind.repeatPeriodIndex+1);
						}
						break;
					};
					//最后一次的闹钟比现在小，则说明已经过期
					if (ringtime<nowTime&&i==d){
						return -1;
					}
				}

			}
			calendar.add(Calendar.DAY_OF_YEAR, day);
		}
		 alermTime = calendar.getTimeInMillis();
		return alermTime;
	}
	public static long getAlarmTimeInMillis(int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		long nowTime = System.currentTimeMillis();
		long alermTime = calendar.getTimeInMillis();
		return alermTime;
	}
	/**
	 * 重新设置所以闹钟
	 * 
	 * @param context
	 */
	public static void resetAllAlarms(Context context) {
		List<Alarm> alarms = AlarmDao.getInstance(context).queryAll();
		setAlarms(context, alarms);
	}

	/**
	 * 判断闹钟是否过期
	 * 
	 * @param alarm
	 * @param
	 * @param repeatDays
	 * @return
	 */
	public static boolean isAlarmExpired(Alarm alarm, String begindata, int repeatDays) {
		long time = TimeUtils.getDays(begindata);
		// 设置时间为23:59:59，即以过期那天的最后一刻作为是否过期标准
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		calendar.set(Calendar.HOUR_OF_DAY, alarm.hour);
		calendar.set(Calendar.MINUTE, alarm.minute);
		calendar.set(Calendar.SECOND, 3);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DAY_OF_YEAR, repeatDays);

		long endTime = calendar.getTimeInMillis();
		// endTime = time+24*60*60*1000*repeatDays;
		long nowTime = System.currentTimeMillis();
		boolean flag = false;
		if ((endTime-nowTime)>0) {
			flag = false;//m没有过期
		} else {
			flag = true;
		}
		return  flag;
	}

	public static void setAlarms(Context context, List<Alarm> alarms) {
		if (alarms != null && alarms.size() > 0) {
			for (Alarm alarm : alarms) {

				cancelAlarm(context,alarm);
				setAlarm(context, alarm);
			}
		}
	}
	public static void setAlarms(Context context, Collection<Alarm> alarms) {
		if (alarms != null && alarms.size() > 0) {
			for (Alarm alarm : alarms) {
				cancelAlarm(context, alarm);
				setAlarm(context, alarm);
			}
		}
	}
	public static void cancelAlarm(Context context, Alarm alarm) {
		if (alarm == null) {
			return;
		}
	 	AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent("com.dachen.dgrouppatient.broadcast.AlarmReceiver"); 
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AlarmUtil.getId(alarm), intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.cancel(pendingIntent);
	}
	public static void cancelAlarmFive(Context context, Alarm alarm) {
		 
	 	AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent("com.dachen.dgrouppatient.broadcast.AlarmReceiver"); 
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AlarmUtil.getId(alarm)+100000, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.cancel(pendingIntent);
	}

	public static void cancelAlarms(Context context, List<Alarm> alarms) {
		if (alarms != null && alarms.size() > 0) {
			for (Alarm alarm : alarms) {
				cancelAlarm(context, alarm);
			}
		}
	}
	public static void cancelAlarms(Context context, Collection<Alarm> alarms) {
		if (alarms != null && alarms.size() > 0) {
			for (Alarm alarm : alarms) {
				cancelAlarm(context, alarm);
			}
		}
	}
	public static void cancelNotification(Context context, Alarm alarm) {
		if (alarm == null) {
			return;
		}
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(AlarmUtil.getId(alarm));

	}
	public static void cancelFiveNotification(Context context, Alarm alarm) {
	
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(AlarmUtil.getId(alarm));
		cancelAlarmFive(context, alarm);
	}
}
