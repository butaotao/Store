package com.dachen.dgroupdoctorcompany.service;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.dachen.common.utils.DeviceInfoUtil;
import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.CompanyApplication;
import com.dachen.dgroupdoctorcompany.db.dbentity.Reminder;
import com.dachen.medicine.common.utils.Alarm;
import java.io.IOException;

/**
 * Created by Burt on 2016/3/26.
 */
public class PlayMusicService extends Service{
    private SoundPool                  mSoundPool;
    private int                        mSoundId;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);
        mSoundId = mSoundPool.load(this, R.raw.sign_add,1);
        if (null!=intent){
            Bundle bundle= intent.getBundleExtra("alarm");

            if (null!=bundle){
                if (  CompanyApplication.getMediaPlayer().isPlaying()) {


                    // 到初始化状态，这里需要判断是否正在响铃，如果直接在开启一次会出现2个铃声一直循环响起，您不信可以尝试


                } else if  (!CompanyApplication.getMediaPlayer().isPlaying()) {


                    try {
                       // CompanyApplication.getMediaPlayer().play(getSoundUri(this));
                        playAddSign();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
    public static Uri getSoundUri(Context context) {

        return Uri.parse("android.resource://" + context.getPackageName() + "/raw/" + "sign_add");
    }

    private void playAddSign(){
        mSoundPool.play(mSoundId,1, 1, 0, 0, 1);
    }

}