package com.dachen.dgroupdoctorcompany.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.dachen.dgroupdoctorcompany.db.dbdao.OftenSignPlaceDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.OftenSinPlace;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;

import java.util.List;

/**
 * Created by Burt on 2016/8/11.
 */
public class LocationReceiver extends BroadcastReceiver {
    OftenSignPlaceDao oftenSignPlaceDao;
    double lengh = -1;
    String address;
    long allowDistance = 250;
    @Override
    public void onReceive(Context context, Intent intent) {
        oftenSignPlaceDao = new OftenSignPlaceDao(context);
    }

}
