package com.dachen.dgroupdoctorcompany.receiver;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.dachen.common.utils.DeviceInfoUtil;
import com.dachen.common.utils.TimeUtils;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.AlarmDialogFullScreenActivity;
import com.dachen.dgroupdoctorcompany.activity.SelectAddressActivity;
import com.dachen.dgroupdoctorcompany.activity.SignListActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.RemindDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.Reminder;
import com.dachen.dgroupdoctorcompany.db.dbentity.WeekSinger;
import com.dachen.dgroupdoctorcompany.service.GaoDeService;
import com.dachen.dgroupdoctorcompany.service.PlayMusicService;
import com.dachen.medicine.common.utils.Alarm;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.AppConfig;
import com.dachen.medicine.config.UserInfo;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.dachen.common.utils.DeviceInfoUtil;
import com.dachen.common.utils.ToastUtil;
import com.dachen.medicine.common.utils.Alarm;
import com.dachen.medicine.config.AppConfig;
import com.dachen.medicine.config.ContextConfig;
import com.dachen.medicine.config.UserInfo;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * 闹钟到时接收通知
 * @author gaozhuo
 * @date 2015年11月28日
 *
 */
public class AlarmReceivers extends BroadcastReceiver {
    private static final String TAG = AlarmReceivers.class.getSimpleName();
    @Override
    public void onReceive( final Context context,  final Intent data) {
        new showNotifactionThread(System.currentTimeMillis()+"",context,data).start();
    }
    public class showNotifactionThread extends Thread{

        Context context;
        Intent data;
        PendingResult result;
        WakeLock wakeLock;
        showNotifactionThread(String cratetime,Context context,Intent data ) {
            super(cratetime);
            this.data = data;
            this.context = context;
            this. result = result;
            wakeLock = getWakeLock(context);
        }

        public void run() {
            handleIntent(context, data);
        }
    }
    private synchronized void handleIntent(Context context, Intent data) {
        Bundle beans = data.getBundleExtra("alarm");
        Reminder alarm2 = null ;
        if (null!=beans) {
            alarm2  = (Reminder) beans.getSerializable("alarm") ;
        }
        Collection<WeekSinger> weeks = alarm2.weeks;
        long time = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
        RemindDao dao = new RemindDao(context);
        Reminder reminder = dao.queryByUserCreateTime(alarm2.createTime);
        boolean show = false;
        String session = SharedPreferenceUtil.getString(context,"session","");
        if (null==reminder||TextUtils.isEmpty(session)||reminder.isOpen==0||
                !SharedPreferenceUtil.getString(context,"id","").equals(alarm2.userloginid)){
            return;
        }


            if (!TextUtils.isEmpty(reminder.weekday) &&reminder.weekday.contains(""+dayInWeek)){
                show = true;
            }

        if (show==false){
            if (reminder.times==0){
                show = true;
            }
        }
        if (show){
            Intent intent = new Intent(context,GaoDeService.class);
            intent.putExtra("nowtime",time);
            context.startService(intent);
            showNotification(context, reminder,time);
        }
    }

    private void showNotification(Context context, Reminder alarm,long time) {
        Intent service = new Intent(context, PlayMusicService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("alarm",  alarm);
        service.putExtra("alarm",  bundle);
        context.startService(service);
        String thisAppPackageName = context.getPackageName()+"";
        Class<? extends Activity> clazz = AlarmDialogFullScreenActivity.class;
        String currentActivity = getCurrentActivity(context);
        String packageName = getCurrentPackageName(context);
        boolean islock = false;
        if (DeviceInfoUtil.isScreenLocked(context)||(!currentActivity.contains(AppConfig.PACKAGENAME_PACIENT_LIBRAY)
                &&!currentActivity.contains(AppConfig.PACKAGENAME_DOCTOR_LIBRARY)&&
                !packageName.contains(thisAppPackageName))) {
            clazz = SelectAddressActivity.class;
            islock = true;
        }
        Intent intent2 = new Intent(context, clazz);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent2.putExtra("nowtime",time);
        intent2.putExtra("alarm", alarm);
        if (!islock){
            context.startActivity(intent2);
            return;
        }
        PendingIntent fullScreenIntent = PendingIntent.getActivity(context, alarm._id, intent2,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker("亲,时间快到了,赶紧去考勤签到吧!" ).setContentTitle("药企圈")
                .setContentText("亲,时间快到了,赶紧去考勤签到吧!" )
                .setContentIntent(fullScreenIntent).setWhen(System.currentTimeMillis()).setOngoing(false)
                .setDefaults(Notification.FLAG_SHOW_LIGHTS).setSmallIcon(R.drawable.ic_launcher_icon).setAutoCancel(true)
				/*.setFullScreenIntent(fullScreenIntent, false)*//*.setSound(getSoundUri(context, alarm))*/;
        //builder.setSound(getSoundUri(context, alarm));
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(alarm._id);
        notificationManager.notify(alarm._id, builder.build());
    }

    private WakeLock getWakeLock(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AlarmWakeLock");
    }

    public String  	getCurrentActivity(Context context){
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
        String shortClassName = info.topActivity.getShortClassName();    //类名
        String className = info.topActivity.getClassName();
        String packAgeName = info.baseActivity.getPackageName();
        return  className;
    }
    public String getCurrentPackageName(Context context){//com.dachen.dgrouppatient
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
        String shortClassName = info.topActivity.getShortClassName();    //类名
        String className = info.topActivity.getClassName();
        String packAgeName = info.baseActivity.getPackageName();
        return  packAgeName;
    }

}
