package com.dachen.dgroupdoctorcompany.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyTrafficStyle;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/8/8.
 */
public class MapDetailActivity extends BaseActivity implements HttpManager.OnHttpListener, AMap.OnMarkerClickListener, LocationSource,
        AMapLocationListener, AMap.OnInfoWindowClickListener,
        AMap.InfoWindowAdapter, AMap
                .OnCameraChangeListener, AMap.OnMapTouchListener,AMap.OnMapClickListener {
    MapView mMapView;
    private AMap mAMap;
    private MyTrafficStyle mMyTrafficStyle;
    private double latitude;
    private double longitude;
    private int level = 17;
    private String city;//城市
    private Marker locationMarker; // 选择的点
    private String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitymapdetail);
        setTitle("查看地点");
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude",0);
        address = getIntent().getStringExtra("address");
        initView();
        if(null == mAMap){
            mAMap = mMapView.getMap();
            setUpMap();
        }
    }
    private void setUpMap(){
        mMyTrafficStyle = new MyTrafficStyle();
        mMyTrafficStyle.setSeriousCongestedColor(0xff92000a);
        mMyTrafficStyle.setCongestedColor(0xffea0312);
        mMyTrafficStyle.setSlowColor(0xffff7508);
        mMyTrafficStyle.setSmoothColor(0xff00a209);
        mAMap.setMyTrafficStyle(mMyTrafficStyle);
        mAMap.setMyTrafficStyle(mMyTrafficStyle);
        if(null == city ){//如果没有定位数据则再次定位
            mAMap.setLocationSource(this);// 设置定位监听
        }
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
//        mAMap.getUiSettings().setScrollGesturesEnabled(false);//禁止滑动地图
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        mAMap.setMapType(AMap.LOCATION_TYPE_LOCATE);// 设置定位的类型为定位模式，参见类AMap。

        mAMap.setOnInfoWindowClickListener(this);
        mAMap.setInfoWindowAdapter(this);
        mAMap.setOnCameraChangeListener(this);// 对amap添加移动地图事件监听器
        mAMap.setOnMapTouchListener(this);
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(level));
        mAMap.setOnMarkerClickListener(this);
        mAMap.setOnMapClickListener(this);



        // mAMap.showMapText(false);
        UiSettings uiSettings = mAMap.getUiSettings();
     /*   uiSettings.setScaleControlsEnabled(false);
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setAllGesturesEnabled(false);
        uiSettings.setZoomGesturesEnabled(false);
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setCompassEnabled(false);*/

        setAMap();
    }
    private void setAMap(){
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), level));

        locationMarker = mAMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .position(new LatLng(latitude, longitude)));

        locationMarker.showInfoWindow();
        if (!TextUtils.isEmpty(address)){
            locationMarker.setTitle("" + address);
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }

    @Override
    public void onSuccess(Result response) {

    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onTouch(MotionEvent motionEvent) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

       /* if ( locationMarker.isInfoWindowShown()) {
            locationMarker.hideInfoWindow();
        }else {
            locationMarker.showInfoWindow();
        }*/
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (locationMarker != null) {
            locationMarker.hideInfoWindow();
        }
    }
}
