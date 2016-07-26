package com.dachen.mediecinelibraryrealize.activity;



import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.adapter.Params;
import com.dachen.mediecinelibraryrealize.adapter.RecomentAdapter;
import com.dachen.mediecinelibraryrealize.entity.BaiduMapData;
import com.dachen.mediecinelibraryrealize.entity.DrugStore;
import com.dachen.mediecinelibraryrealize.entity.Drugstorefens;
import com.dachen.mediecinelibraryrealize.entity.SharePrefrenceConst;
import com.dachen.mediecinelibraryrealize.utils.JsonUtils.BaiduMapDataTrans;

import java.util.ArrayList;

/**
 * 此demo用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泡
 * 
 */
public class BaiduMapActivity extends Activity  implements LocationListener, OnClickListener, OnHttpListener { 
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
	RecomentAdapter adapter;
	MapView mMapView;
	BaiduMap mBaiduMap;
	ListView listview;
	ArrayList<Drugstorefens> fens;
	// UI相关
	OnCheckedChangeListener radioButtonListener; 
	boolean isFirstLoc = true;// 是否首次定位
	String id = "";
	RelativeLayout rl_back;
	TextView tv_title;
	RelativeLayout rl_des;
	View title;
	String city;
	String mLongitude;
	String mLatitude;
	boolean getlocation = false; 
	LinearLayout llbmapView;
	String doctorAndGroupName;
	String patient;
	LinearLayout ll_baidu;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidumap); 
        llbmapView = (LinearLayout) findViewById(R.id.llbmapView);
		ll_baidu = (LinearLayout) findViewById(R.id.ll_baidu);
	    findViewById(R.id.tv_openbaidudes).setOnClickListener(this);
		SDKInitializer.initialize(getApplicationContext());
	 	mMapView = new MapView(this);
	 	mBaiduMap = mMapView.getMap();
      id =  getIntent().getStringExtra("code");
      city = SharedPreferenceUtil.getString(this, SharePrefrenceConst.MEDICINE_INFO, "");
		doctorAndGroupName = getIntent().getStringExtra("name");
		patient = getIntent().getStringExtra("patient");
	  
	  mLatitude = SharedPreferenceUtil.getString(this, "mLatitude", "");
  	
		  mLongitude =  SharedPreferenceUtil.getString(this, "mLongitude", "");
		  if (!TextUtils.isEmpty(mLatitude)&&!TextUtils.isEmpty(mLongitude)) {
			  LatLng cenpt = new LatLng(Double.parseDouble(mLatitude),Double.parseDouble(mLongitude)); 
			  //定义地图状态
			 MapStatus mMapStatus = new MapStatus.Builder()
			  .target(cenpt)
			  .zoom(14)
			  .build();
			  //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化

			 MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
			  //改变地图状态
			 mBaiduMap.setMapStatus(mMapStatusUpdate);

		}
		  
       getlocation = false;
        mCurrentMode = LocationMode.NORMAL; 
        listview = (ListView) findViewById(R.id.listview);
		listview.setEmptyView(findViewById(R.id.tv_nullinfo));
    	rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("推荐药店");
	 title = View.inflate(this, R.layout.layout_baidutitle, null); 
		// 地图初始化
	
		View child = mMapView.getChildAt(1);
		if (child != null ){            
			if (child instanceof  ImageView|| child instanceof ZoomControls) {
		     child.setVisibility(View.INVISIBLE);  
			}
		}
		        
		//地图上比例尺        
		//mMapView.showScaleControl(false);
		// 隐藏缩放控件
		mMapView.showZoomControls(false);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener); 
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(5000);
		mLocClient.setLocOption(option);
		mLocClient.start();  
		fens = new ArrayList<Drugstorefens>();
		 adapter = new RecomentAdapter(this,fens);
		 
	
			  
			  
		  if (!TextUtils.isEmpty(city)) {
			  getInfo(city,mLatitude,mLongitude);
		}
		 
		  llbmapView.addView(mMapView);
		 listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub 
					if (arg2!=0) {
						Drugstorefens fens = (Drugstorefens) adapter.getItem(arg2-1);
						//ToastUtils.showToast("fens=="+fens);
						Intent intent = new Intent(BaiduMapActivity.this,BaiduMapDesActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("Drugstorefens", fens);
						intent.putExtra("Drugstorefens", bundle);
						intent.putExtra("id", fens.id);
						intent.putExtra("recipe_id", id);
						intent.putExtra("doctorAndGroupName",doctorAndGroupName);
                        intent.putExtra("patient",patient);
						startActivity(intent);
					}
					
				}
			});
		 
	}
//http://192.168.3.7:9002/web/api/invoke/855c6320993b4cf0968ae1503a849a5a/c_Recipe.drugstore_recommended?
    //recipe_id=DF0F1BAD8DFA46889DC34EAF5AB04E32&area_city=深圳&longitude=113.946635&latitude
    public void getInfo(String city,String latitude,String longitude){
		// id = "5750f9e2bae14d45db4bb6fd";
		//latitude = "39.812510";
		//longitude = "116.265";
		city = SharedPreferenceUtil.getString(this,"citycode","");
    	//if (!getlocation) {
		String s = "org/goods/salesLog/getRecommendedDrugStoreList";
    		new HttpManager().get(BaiduMapActivity.this,
    				s,
					BaiduMapData.class,
    				Params.getMapInfo(this,id,city,latitude+"",longitude+""),
    				BaiduMapActivity.this, false, 1);
		//}
    
    	//if (!TextUtils.isEmpty(city)&&!TextUtils.isEmpty(latitude)&&!TextUtils.isEmpty(longitude)) {
    		
		//}
    
    }
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) { 
        	if (null!=location.getCity()) {
				city = location.getCity();
			} 
        	if (!TextUtils.isEmpty(city)) {
        		SharedPreferenceUtil.putString(BaiduMapActivity.this, SharePrefrenceConst.MEDICINE_INFO, city);


			}
			String code = location.getCityCode();
			if (!TextUtils.isEmpty(code)){
				SharedPreferenceUtil.putString(BaiduMapActivity.this,"citycode",code);
			}

        	if (!TextUtils.isEmpty(location.getLatitude()+"")) {
        		SharedPreferenceUtil.putString(BaiduMapActivity.this,"mLatitude", location.getLatitude()+"");
			}
			if (!TextUtils.isEmpty(location.getLongitude()+"")) {
				SharedPreferenceUtil.putString(BaiduMapActivity.this,"mLongitude", location.getLongitude()+"");
			}
			
        	if (!TextUtils.isEmpty(location.getLatitude()+"")&&!TextUtils.isEmpty(location.getLongitude()+"")) {
        		 getInfo(city, location.getLatitude() + "", location.getLongitude() + "");
				/*if(!TextUtils.isEmpty(city)){
					ll_baidu.setVisibility(View.GONE);
				}else {
					ll_baidu.setVisibility(View.VISIBLE);
				}*/

			}else {
				ll_baidu.setVisibility(View.VISIBLE);
			}

            // map view 销毁后不在处理新接收的位置
          /*  if (location == null || mMapView == null) {
                return;
            }*/
			double getLatitude = location.getLatitude();
			double getLongitude = location.getLongitude();
			city = location.getCity();

		/*	if (!TextUtils.isEmpty(location.getCity())){
				ll_baidu.setVisibility(View.GONE);
			}else {
				ll_baidu.setVisibility(View.VISIBLE);
			}*/
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
			if (!TextUtils.isEmpty(ll.latitude+"")&&!(ll.latitude+"").equals("0.0")){
				ll_baidu.setVisibility(View.GONE);
			}else {
				ll_baidu.setVisibility(View.VISIBLE);
			}
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(u);
        }

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		if (null!=mMapView){
			mMapView.onDestroy();
			mMapView = null;
		}
		super.onDestroy();
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFailure(Exception arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccess(Result result) {
		// TODO Auto-generated method stub
		if (!getlocation) {
			if(result instanceof BaiduMapData){
				BaiduMapData data = (BaiduMapData) result;
				if (result.resultCode ==1){
					ArrayList<Drugstorefens> drugstorefens = BaiduMapDataTrans.drugstorefens(data.data);

					fens = drugstorefens;

					if (null!=fens&&fens.size()>0) {
						adapter = new RecomentAdapter(this,fens);
						listview.removeHeaderView(title);
						listview.addHeaderView(title);
						listview.setAdapter(adapter);
						getlocation = true;
						if (isFirstLoc) {
							isFirstLoc = false;
							for (int i = 0; i < fens.size()&&i<10; i++) {
								int id = com.dachen.medicine.common.utils.CommonUtils.getId(BaiduMapActivity.this, "drawable", "icon_mark"+i);
								// 定义Maker坐标点
								LatLng point = new LatLng(fens.get(i).latitude, fens.get(i).longitude);
								// 构建Marker图标
								BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(id);
								// 构建MarkerOption，用于在地图上添加Marker
								OverlayOptions option1 = new MarkerOptions().position(point).icon(bitmap).title(""+i);
								if (null!=mMapView&&mMapView.getMap()!=null){
									// 在地图上添加Marker，并显示
									mMapView.getMap().addOverlay(option1);
								}

							}
						}
					}
				}else {
					ToastUtils.showResultToast(this,result);
				}
			}

		}
	}

	@Override
	public void onSuccess(ArrayList arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId()==R.id.rl_back) {
			finish();
		}else if(v.getId() == R.id.tv_openbaidudes){
			Intent intent = new Intent(this,BaiduMapOpenGPSActivity.class);
			startActivity(intent);
		}
	}

}
