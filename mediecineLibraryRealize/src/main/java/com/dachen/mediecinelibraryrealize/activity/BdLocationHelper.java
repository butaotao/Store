package com.dachen.mediecinelibraryrealize.activity; 

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.mediecinelibraryrealize.entity.SharePrefrenceConst;

public class BdLocationHelper { 
	private Context mContext;
	private double mLongitude;
	private double mLatitude;
	private String mAddress;
	private String mProvinceName;// 省份
	private String mCityName;// 城市
	private String mDistrictName;// 街道
	private boolean isLocationUpdate;// 本次程序启动后，位置有没有成功更新一次

	private LocationClient mLocationClient = null;
	private int mFaildCount = 0;

	public BdLocationHelper(Context context) {
		mContext = context;
		// 获取上一次的定位数据  
		mLocationClient = new LocationClient(context); // 声明LocationClient类
		mLocationClient.registerLocationListener(mMyLocationListener); // 注册监听函数
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Battery_Saving);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为10s
		option.setIsNeedAddress(true);
		option.setNeedDeviceDirect(false);
		mLocationClient.setLocOption(option);

		requestLocation();
	}

	public void release() {
		if (mLocationClient.isStarted()) {
			mLocationClient.stop();
		}
	}

	public void requestLocation() {
		if (!mLocationClient.isStarted()) {
			mFaildCount = 0;
			mLocationClient.start();
		} else {
			int scanSpan = mLocationClient.getLocOption().getScanSpan();
			if (scanSpan < 1000) {
				mLocationClient.getLocOption().setScanSpan(5000);
			}
		}
	}

	private BDLocationListener mMyLocationListener = new BDLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation location) {
			int resultCode = 0;
			if (location != null) {
				resultCode = location.getLocType();
			}
			// 百度定位失败
			if (resultCode != BDLocation.TypeGpsLocation && resultCode != BDLocation.TypeCacheLocation
					&& resultCode != BDLocation.TypeOffLineLocation && resultCode != BDLocation.TypeNetWorkLocation) {
				 
				mFaildCount++;
				if (mFaildCount > 3) {// 停止定位
					mLocationClient.stop();
				}
				return;
			}

			// 百度定位成功
			mLongitude = location.getLongitude();
			mLatitude = location.getLatitude();

			if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				mAddress = location.getAddrStr();
				mProvinceName = location.getProvince();
				mCityName = location.getCity();
				mDistrictName = location.getDistrict();
				SharedPreferenceUtil.putString(mContext,SharePrefrenceConst.MEDICINE_INFO, mCityName+"");
				SharedPreferenceUtil.putString(mContext,"mLongitude", mLongitude+"");
				SharedPreferenceUtil.putString(mContext,"mLatitude", mLatitude+"");
				SharedPreferenceUtil.putString(mContext,"citycode",location.getCityCode());
					/*LogUtils.burtLog(
							"百度定位信息  City:" + location.getCity() + "  CityCode:" + location.getCityCode() + "  区：" + location.getDistrict());*/
				 
			}
			 
 
			//LogUtils.burtLog( "百度定位信息  mLongitude:" + mLongitude + "  mLatitude:" + mLatitude + "  mAddressDetail:" + mAddress);
		 
			mLocationClient.stop();
			// if (isTimingScan()) {// 停止定时定位
			// mLocationClient.getLocOption().setScanSpan(100);
			// }
		//	mContext.sendBroadcast(new Intent(ACTION_LOCATION_UPDATE));// 发送广播
		}

	};

	// 获取经纬度
	public double getLongitude() {
		return mLongitude;
	}

	// 获取经纬度
	public double getLatitude() {
		return mLatitude;
	}

	// 获取地址详情
	public String getAddress() {
		return mAddress;
	}

	public String getProvinceName() {
		return mProvinceName;
	}

	public String getCityName() {
		return mCityName;
	}

	public String getDistrictName() {
		return mDistrictName;
	}

	public boolean isLocationUpdate() {
		return isLocationUpdate;
	}

	public boolean hasData() {
		return mLatitude != 0 && mLongitude != 0;
	}

}
