package com.dachen.dgroupdoctorcompany.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.CompanyApplication;
import com.dachen.dgroupdoctorcompany.utils.GaoDeMapUtils;

/**
 * Created by Burt on 2016/3/26.
 */
public class GaoDeService extends Service{
    private GaoDeMapUtils mGaoDeMapUtils;
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
        mGaoDeMapUtils = new GaoDeMapUtils( this);
        mGaoDeMapUtils.startLocation();
        return super.onStartCommand(intent, flags, startId);
    }

}