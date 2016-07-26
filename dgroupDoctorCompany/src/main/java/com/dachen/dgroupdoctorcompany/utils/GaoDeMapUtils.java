package com.dachen.dgroupdoctorcompany.utils;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by weiwei on 2016/6/17.
 */
public class GaoDeMapUtils  {
    private  AMapLocationClient locationClient = null;
    private  AMapLocationClientOption locationOption = null;
    private  double                     latitude;//纬度
    private  double                     longitude;//经度
    private  String                     city;//城市
    private  String                     mStrFloor ;
    private  String                     mStrAddress ;
    private Context mContext;
    private LocationListener mLocationListener;

    public GaoDeMapUtils(Context context,LocationListener locationListener){
        this.mContext = context;
        this.mLocationListener = locationListener;
    }

    public  void startLocation(){
        locationClient = new AMapLocationClient(mContext);
        locationOption = new AMapLocationClientOption();
        // 设置定位监听
        locationClient.setLocationListener(new MyLocationListner());
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        locationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        locationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        locationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        locationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
//       locationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        locationClient.setLocationOption(locationOption);
        //启动定位
        locationClient.startLocation();
    }

    public  class MyLocationListner implements AMapLocationListener{

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if(null != aMapLocation){
                int code = aMapLocation.getErrorCode();
                if(code == 0){
                    latitude = aMapLocation.getLatitude();
                    longitude = aMapLocation.getLongitude();
                    city = aMapLocation.getCity();
                    mStrFloor = aMapLocation.getAoiName();
                    mStrAddress = aMapLocation.getAddress();

                    Map<String,Object>map = new HashMap<>();
                    map.put("latitude",latitude);
                    map.put("longitude",longitude);
                    map.put("city",city);
                    map.put("floor",mStrFloor);
                    map.put("address",mStrAddress);
                    mLocationListener.onLocation(map);
                }else{
                    mLocationListener.onLocation(null);
                }
            }else{
                mLocationListener.onLocation(null);
            }
        }
    }

    public interface LocationListener{
        public abstract void onLocation(Object object);
    }

    public void onDestory(){
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

}
