package com.dachen.dgroupdoctorcompany.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyTrafficStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.AddressListAdapter;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.JoinVisitGroup;
import com.dachen.dgroupdoctorcompany.utils.DataUtils.GetUserDepId;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;
import com.dachen.medicine.view.ClearEditText;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiwei on 2016/3/30.
 */
public class SelectAddressActivity extends BaseActivity implements LocationSource,
        AMapLocationListener,PoiSearch.OnPoiSearchListener, AMap
                .OnMapClickListener, AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener,
        AMap.InfoWindowAdapter,PullToRefreshBase.OnRefreshListener2<ListView>, AMap
                .OnCameraChangeListener, AMap.OnMapTouchListener,HttpManager.OnHttpListener {
    public static final int MODE_SELECT_ADDRESS = 11;//选择地点之后当前页面finish，返回地点
    public static final int MODE_SELECT = 12;//选择地点之后，跳到添加签到页面
    private int mSelectedMode;
    public static  String POI = "地名地址信息|医疗保健服务|商务住宅|交通设施服务|公司企业|生活服务";//poi搜索类型
    private ClearEditText et_search;
    private PullToRefreshListView lvAddress;
    private AMap                        mAMap;
    private MapView mMapView;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private OnLocationChangedListener   mListener;
    private PoiResult                   poiResult;//poi返回的结果
    private int                         currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query             query;//poi查询条件类
    private PoiSearch                   poiSearch;//POI搜索
    private LatLonPoint lp;
    private Marker locationMarker; // 选择的点
    private Marker mlastMarker;//最后选择的点
    private myPoiOverlay poiOverlay;// poi图层
    private String keyWord = "";//搜索关键词
    private List<PoiItem> mListPoiItems = new ArrayList<>();
    private MyTrafficStyle mMyTrafficStyle;
    private Circle circle;
    private RelativeLayout vInfo;
    private TextView tvInfo;
    private View head_view;

    private String                      city;//定位所在的城市
    private AddressListAdapter          mAdapter;
    private int                         distance = 250;//定位范围
    private int                         mMode;
    private boolean                     isTouch = false;
    private int level = 17;
    double mlatitude = 0;
    double mlongitude = 0;
    String type;
    private int mSelectType;
    private String address_name;
    private String fromActivity;
    long nowtime;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写

        initViews();
        initData();
        startSignActivity();
    }
    public void startSignActivity(){
        String latitude = SharedPreferenceUtil.getString(this, "nowtimelatitude",  "");
        String longitude = SharedPreferenceUtil.getString(this, "nowtimelongitude",  "");
        long nowtime = SharedPreferenceUtil.getLong(this, "nowtime", 0);
        long servceTime = getIntent().getLongExtra("nowtime",0);
        if ((nowtime !=0)&&(nowtime==servceTime)&&!TextUtils.isEmpty(latitude)&&!TextUtils.isEmpty(longitude)){
            double la = Double.parseDouble(latitude);
            double lo = Double.parseDouble(longitude);
            if (SignInActivity.compareDistance(lo,la,SelectAddressActivity.this)){
                Intent intent = new Intent(this, AddSignInActivity.class);

                intent.putExtra("name", SignInActivity.address);
                intent.putExtra("longitude", longitude);
                intent.putExtra("latitude", latitude);
                intent.putExtra("mode", AddSignInActivity.MODE_WORKING);
                intent.putExtra("allow",true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }
    }
    public void initViews() {
        super.initView();
        setTitle("选择地点");
        et_search = (ClearEditText) findViewById(R.id.et_search);
        et_search.setOnEditorActionListener(new MyEditorActionListener());
        lvAddress = (PullToRefreshListView) findViewById(R.id.lvAddress);
        vInfo = (RelativeLayout) findViewById(R.id.vInfo);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        lvAddress.setEmptyView(vInfo);

        mSelectType = this.getIntent().getIntExtra("select_type",0);
        if(getIntent().hasExtra("address_name")){
            address_name = getIntent().getStringExtra("address_name");
        }
        if (getIntent().hasExtra("fromActivity")){
            fromActivity = getIntent().getStringExtra("fromActivity");
        }
    }

   private void  initData(){
       mMode = this.getIntent().getIntExtra("mode",AddSignInActivity.MODE_WORKING);
       mSelectedMode = this.getIntent().getIntExtra("select_mode",MODE_SELECT);
       type = this.getIntent().getStringExtra("type");
       String poi = this.getIntent().getStringExtra("poi");
       if(!TextUtils.isEmpty(poi)){
           POI = poi;
       }
       int ndistance = this.getIntent().getIntExtra("distance",0);
       if(ndistance>0){
           distance = ndistance;
       }
       mlatitude = this.getIntent().getDoubleExtra("latitude",0);
       mlongitude = this.getIntent().getDoubleExtra("longitude",0);
       lp = new LatLonPoint(mlatitude,mlongitude);
       city = this.getIntent().getStringExtra("city");
       mAdapter = new AddressListAdapter(SelectAddressActivity.this,new ArrayList<PoiItem>());
       lvAddress.setAdapter(mAdapter);
       lvAddress.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
       lvAddress.setOnRefreshListener(this);
       lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               if(null==mListPoiItems || mListPoiItems.size()<=0){
                   return;
               }
               for(int i=0; i<mListPoiItems.size();i++){
                   PoiItem poiItem = mListPoiItems.get(i);
                   if(i == (position-1)){
                       poiItem.setIndoorMap(true);//设置选中
                   }else{
                       poiItem.setIndoorMap(false);
                   }
               }
               mAdapter.notifyDataSetChanged();
               PoiItem poiItem = (PoiItem) parent.getAdapter().getItem(position);

               if(mSelectType==1){
                   String name = poiItem.getTitle();
                   double longitude = poiItem.getLatLonPoint().getLongitude();
                   double latitude = poiItem.getLatLonPoint().getLatitude();
                   String city = poiItem.getCityName();
                   String address = poiItem.getAdName();
                   String snippet = poiItem.getSnippet();
                   Intent intent = new Intent();
                   intent.putExtra("name", name);
                   intent.putExtra("longitude", longitude);
                   intent.putExtra("latitude", latitude);
                   setResult(RESULT_OK,intent);
                   finish();
               }else {
                   if (mSelectedMode == MODE_SELECT) {
                       String name = poiItem.getTitle();
                       double longitude = poiItem.getLatLonPoint().getLongitude();
                       double latitude = poiItem.getLatLonPoint().getLatitude();
                       String city = poiItem.getCityName();
                       String address = poiItem.getAdName();
                       String snippet = poiItem.getSnippet();
                       if (mMode == AddSignInActivity.MODE_VISIT) {
                           if ("signle".equals(type)) {
                               Intent intent = new Intent(SelectAddressActivity.this, SelfVisitActivity.class);
                               intent.putExtra("address", name);
                               intent.putExtra("longitude", longitude);
                               intent.putExtra("latitude", latitude);
                               intent.putExtra("addressname", city + address + snippet);
                               intent.putExtra("mode", CustomerVisitActivity.MODE_FROM_SIGN);
                               intent.putExtra("city", city);
                               startActivity(intent);
                           } else if ("together".equals(type)) {
                               startVisitGroup(latitude, longitude, (city + address + snippet), name);
//                           Intent intent = new Intent(SelectAddressActivity.this,TogetherVisitActivity.class);
//                           intent.putExtra("address",name);
//                           intent.putExtra("longitude",longitude);
//                           intent.putExtra("latitude",latitude);
//                           intent.putExtra("addressname",city+address+snippet);
//                           intent.putExtra("mode",TogetherVisitActivity.MODE_FROM_SIGN);
//                           intent.putExtra("city",city);
//                           startActivity(intent);
                           }else if ("selectVisitPeopleposition".equals(type)){
                               startVisitGroup(latitude, longitude, (city + address + snippet), name);
                           }

                       } else {
                           Intent intent = new Intent(SelectAddressActivity.this, AddSignInActivity.class);
                           intent.putExtra("name", name);
                           intent.putExtra("longitude", longitude);
                           intent.putExtra("latitude", latitude);
                           intent.putExtra("mode", mMode);
                           startActivity(intent);
                       }

                   } else if (mSelectedMode == MODE_SELECT_ADDRESS) {
                       Intent intent = new Intent();
                       String name = poiItem.getTitle();
                       String city = poiItem.getCityName();
                       String address = poiItem.getAdName();
                       String snippet = poiItem.getSnippet();
                       double longitude = poiItem.getLatLonPoint().getLongitude();
                       double latitude = poiItem.getLatLonPoint().getLatitude();
                       intent.putExtra("longitude", longitude);
                       intent.putExtra("latitude", latitude);
                       intent.putExtra("floor", name);
                       intent.putExtra("address", city + address + snippet);
                       setResult(RESULT_OK, intent);
                       finish();
                   }
               }
           }
       });
       if(null == mAMap){
           mAMap = mMapView.getMap();
           setUpMap();
       }

       if(null!=city){
           setAMap();
           doSearchQuery();
       }
    }

    private void startVisitGroup(double latitude,double longitude,String address,String addressname){
        showLoadingDialog();
        String orginId = GetUserDepId.getUserDepId(this);
        new HttpManager().post(this, Constants.CREATE_AND_JOIN_VISIT_GROUP, JoinVisitGroup.class,
                Params.startVisitGroup(SelectAddressActivity.this,orginId,String.valueOf(latitude),String.valueOf(longitude),address,addressname),
                this, false, 4);
    }

    private void setUpMap(){
        mMyTrafficStyle = new MyTrafficStyle();
        mMyTrafficStyle.setSeriousCongestedColor(0xff92000a);
        mMyTrafficStyle.setCongestedColor(0xffea0312);
        mMyTrafficStyle.setSlowColor(0xffff7508);
        mMyTrafficStyle.setSmoothColor(0xff00a209);
        mAMap.setMyTrafficStyle(mMyTrafficStyle);
        if(null == city ){//如果没有定位数据则再次定位
            mAMap.setLocationSource(this);// 设置定位监听
        }
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
//        mAMap.getUiSettings().setScrollGesturesEnabled(false);//禁止滑动地图
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        mAMap.setMapType(AMap.LOCATION_TYPE_LOCATE);// 设置定位的类型为定位模式，参见类AMap。
        mAMap.setOnMapClickListener(this);
        mAMap.setOnMarkerClickListener(this);
        mAMap.setOnInfoWindowClickListener(this);
        mAMap.setInfoWindowAdapter(this);
        mAMap.setOnCameraChangeListener(this);// 对amap添加移动地图事件监听器
        mAMap.setOnMapTouchListener(this);
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(level));

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

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
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        if(query != null && poiSearch != null && poiResult != null){
            if(poiResult.getPageCount() - 1 > currentPage){
                currentPage++;
                query.setPageNum(currentPage);// 设置查后一页
                poiSearch.searchPOIAsyn();
            }else{
                ToastUtil.showToast(SelectAddressActivity.this,"对不起，没有搜索到相关数据！");
            }
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if(isTouch){
            if(null!=locationMarker){
                locationMarker.setPosition(cameraPosition.target);
            }
        }


    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if(isTouch){
            double latitude = cameraPosition.target.latitude;//获取纬度
            double longitude = cameraPosition.target.longitude;//获取经度
            lp = new LatLonPoint(latitude,longitude);

            isTouch = false;
            mListPoiItems.clear();
            currentPage = 0;
            mAdapter.notifyDataSetChanged();
            if(null!=circle && !circle.contains(new LatLng(lp.getLatitude(),lp.getLongitude()))){

            }else{
                doSearchQuery();
            }
        }

    }

    @Override
    public void onTouch(MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
            isTouch = true;
        }
    }

    private void openVisitGroup(JoinVisitGroup.Data data){
        if(null != data.dataList){
            Gson g = new Gson();
            String listMedieaName="";
            String jsonMediea = "";
            if(null!=data.dataList.goodsGroups && data.dataList.goodsGroups.size()>0){
                jsonMediea = g.toJson(data.dataList.goodsGroups);
                for(int i=0;i<data.dataList.goodsGroups.size();i++){
                    JoinVisitGroup.Data.DataList.GoodsGroups item = data.dataList.goodsGroups.get(i);
                    if(TextUtils.isEmpty(listMedieaName)){
                        listMedieaName = item.name;
                    }else{
                        listMedieaName = listMedieaName + ","+item.name;
                    }
                }
            }

            String jsonPeople = "";
            if(null != data.groupDetails && data.groupDetails.size()>0){
                jsonPeople = g.toJson(data.groupDetails);
            }

            Intent intent = new Intent(this,SelectVisitPeople.class);
            intent.putExtra("mediea",jsonMediea);
            intent.putExtra("medieaName",listMedieaName);
            intent.putExtra("doctorid",data.dataList.doctorId);
            intent.putExtra("doctorname",data.dataList.doctorName);
            intent.putExtra("latitude",data.dataList.loc.lat);
            intent.putExtra("longitude",data.dataList.loc.lng);
            intent.putExtra("address",data.dataList.address);
            intent.putExtra("addressName",data.dataList.addressName);
            intent.putExtra("jsonPeople",jsonPeople);
            intent.putExtra("initatorId",data.dataList.initatorId);
            intent.putExtra("initatorName",data.dataList.initatorName);
            intent.putExtra("headPic",data.dataList.headPic);
            intent.putExtra("id",data.dataList.id);
            intent.putExtra("time",data.dataList.remainTime);
            intent.putExtra("from",TogetherVisitActivity.MODE_FROM_SIGN);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onSuccess(Result response) {
        closeLoadingDialog();
        if (null != response && response.getResultCode() == 1){
            if(response instanceof JoinVisitGroup){
                JoinVisitGroup joinVisitGroup = (JoinVisitGroup) response;
                JoinVisitGroup.Data data = joinVisitGroup.data;
                if(null != data){
                    openVisitGroup(data);
                }
            }
        }else{
            if(null != response){
                String msg = response.getResultMsg();
                if(TextUtils.isEmpty(msg)){
                    msg = "数据异常";
                }
                ToastUtil.showToast(this,msg);
            }
        }
    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        closeLoadingDialog();
        ToastUtil.showErrorNet(this);
    }

    private class MyEditorActionListener implements EditText.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                keyWord = et_search.getText().toString();
                if(TextUtils.isEmpty(keyWord)){
                    lp = new LatLonPoint(mlatitude,mlongitude);//回到初始地点
                }
                mListPoiItems.clear();
                currentPage = 0;
                mAdapter.notifyDataSetChanged();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_search.getWindowToken(),0);
                doSearchQuery();
            }
            return false;
        }
    }

    private void doSearchQuery(){
        showLoadingDialog();
        currentPage = 0;
        if(null == city){
            city = "";
        }
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query(keyWord,POI,city);
        query.setPageSize(20);//设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);//设置查第一页
        if(null != lp){
            poiSearch = new PoiSearch(this,query);
            poiSearch.setOnPoiSearchListener(this);
            // 设置搜索区域为以lp点为圆心，其周围distance米范围,默认distance为500米
            poiSearch.setBound(new PoiSearch.SearchBound(lp,distance,false));
//            poiSearch.
            poiSearch.searchPOIAsyn();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if(null != locationClient){
            locationClient.onDestroy();
        }
    }

    private void setAMap(){
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lp.getLatitude(), lp.getLongitude()), level));

        locationMarker = mAMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .position(new LatLng(lp.getLatitude(), lp.getLongitude())));
        circle = mAMap.addCircle(new CircleOptions()
                .center(new LatLng(lp.getLatitude(), lp.getLongitude())).radius(distance+60)
                .strokeColor(Color.argb(30, 1, 1, 1))
                .fillColor(Color.argb(20, 1, 1, 1))
                .strokeWidth(4));
        locationMarker.showInfoWindow();
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if( null != mListener && null != aMapLocation){
            if(null != aMapLocation){
                int code = aMapLocation.getErrorCode();
                if(code == 0) {
//                    mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
//                ToastUtil.showToast(SelectAddressActivity.this,"定位成功");
                    //定位成功回调信息，设置相关消息
                    int type = aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    mlatitude = aMapLocation.getLatitude();//获取纬度
                    mlongitude = aMapLocation.getLongitude();//获取经度
                    lp = new LatLonPoint(mlatitude,mlongitude);
                    city = aMapLocation.getCity();
                    setAMap();
                    doSearchQuery();
                }else{
                    ToastUtil.showToast(SelectAddressActivity.this,"定位失败");
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    }
    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if(null==locationClient){
            locationClient = new AMapLocationClient(this);
            // 设置定位监听
            locationClient.setLocationListener(this);
            locationOption = new AMapLocationClientOption();
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
    }
    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if(null != locationClient){
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        locationClient = null;
    }

    @Override
    public void onPoiSearched(PoiResult result, int code) {
        closeLoadingDialog();
        if(currentPage>0){
            lvAddress.onRefreshComplete();
        }
        if(code == 1000){
            if(null != result && null != result.getQuery()){//搜索POI的结果
                if(result.getQuery().equals(query)){//是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    if(null != poiItems && poiItems.size()>0){
//                        if(!TextViewUtils.isEmpty(keyWord)){
//                            mAMap.clear();// 清理之前的图标
//                            PoiOverlay poiOverlay = new PoiOverlay(mAMap,poiItems);
//                            poiOverlay.removeFromMap();
//                            poiOverlay.addToMap();
//                            poiOverlay.zoomToSpan();
//
//                        }

                        locationMarker.setPosition(new LatLng(lp.getLatitude(), lp.getLongitude()));
//                        mlastMarker = mAMap.addMarker(new MarkerOptions()
//                                .anchor(0.5f, 0.5f)
//                                .position(new LatLng(lp.getLatitude(), lp.getLongitude())));
                        if(null!=circle && !circle.isVisible()){
                            circle = mAMap.addCircle(new CircleOptions()
                                .center(new LatLng(lp.getLatitude(),lp.getLongitude())).radius(distance+60)
                                .strokeColor(Color.BLUE)
                                .fillColor(Color.argb(50, 1, 1, 1))
                                .strokeWidth(4));
                        }


                        if(null != circle){
                            for(int i=0;i<poiItems.size();i++){
                                PoiItem poiItem = poiItems.get(i);
                                double longitude = poiItem.getLatLonPoint().getLongitude();
                                double latitude = poiItem.getLatLonPoint().getLatitude();
                                if(circle.contains(new LatLng(latitude,longitude))){
                                    mListPoiItems.add(poiItem);
                                }
                            }
                        }else{
                            mListPoiItems.addAll(poiItems);
                        }
                        if(mSelectType==1){
                            for(int i=0;i<mListPoiItems.size();i++){
                                if(address_name.equals(mListPoiItems.get(i).getTitle())){
                                    mListPoiItems.get(i).setIndoorMap(true);//默认第一个地址选中
                                    break;
                                }
                            }
                        }else{
                            if(currentPage==0){
                                mListPoiItems.get(0).setIndoorMap(true);//默认第一个地址选中
                            }
                        }
                        mAdapter = new AddressListAdapter(SelectAddressActivity.this, (ArrayList
                                <PoiItem>) mListPoiItems);
                        lvAddress.setAdapter(mAdapter);
                        lvAddress.getRefreshableView().setSelection(currentPage*20);//一次加载20条，所以乘以20
                        mAdapter.notifyDataSetChanged();
                    }else if(null != suggestionCities && suggestionCities.size()>0){
                        if(mListPoiItems.size()<=0){
                            showSuggestCity(suggestionCities);
                        }

                    }else{
                        if(mListPoiItems.size()<=0){
                            ToastUtil.showToast(SelectAddressActivity.this,"对不起，没有搜索到相关数据！");
                        }

                    }
                }
            }else{
                ToastUtil.showToast(SelectAddressActivity.this,"对不起，没有搜索到相关数据！");
            }
        }else{
            ToastUtil.showErrorData(SelectAddressActivity.this);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> suggestionCities){
        String infomation = "推荐城市\n";
        for (int i = 0; i < suggestionCities.size(); i++) {
            infomation += "城市名称:" + suggestionCities.get(i).getCityName() + "城市区号:"
                    + suggestionCities.get(i).getCityCode() + "城市编码:"
                    + suggestionCities.get(i).getAdCode() + "\n";
        }
        ToastUtil.showToast(SelectAddressActivity.this,infomation);
    }

    /**
     * 自定义PoiOverlay
     *
     */
    private class myPoiOverlay {
        private AMap mamap;
        private List<PoiItem> mPois;
        private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();
        public myPoiOverlay(AMap amap ,List<PoiItem> pois) {
            mamap = amap;
            mPois = pois;
        }

        /**
         * 添加Marker到地图中。
         * @since V2.1.0
         */
        public void addToMap() {
            for (int i = 0; i < mPois.size(); i++) {
                Marker marker = mamap.addMarker(getMarkerOptions(i));
                PoiItem item = mPois.get(i);
                marker.setObject(item);
                mPoiMarks.add(marker);
            }
        }

        /**
         * 去掉PoiOverlay上所有的Marker。
         *
         * @since V2.1.0
         */
        public void removeFromMap() {
            for (Marker mark : mPoiMarks) {
                mark.remove();
            }
        }

        /**
         * 移动镜头到当前的视角。
         * @since V2.1.0
         */
        public void zoomToSpan() {
            if (mPois != null && mPois.size() > 0) {
                if (mamap == null)
                    return;
                LatLngBounds bounds = getLatLngBounds();
                mamap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }
        }

        private LatLngBounds getLatLngBounds() {
            LatLngBounds.Builder b = LatLngBounds.builder();
            for (int i = 0; i < mPois.size(); i++) {
                b.include(new LatLng(mPois.get(i).getLatLonPoint().getLatitude(),
                        mPois.get(i).getLatLonPoint().getLongitude()));
            }
            return b.build();
        }

        private MarkerOptions getMarkerOptions(int index) {
            return new MarkerOptions()
                    .position(
                            new LatLng(mPois.get(index).getLatLonPoint()
                                    .getLatitude(), mPois.get(index)
                                    .getLatLonPoint().getLongitude()))
                    .title(getTitle(index)).snippet(getSnippet(index));
        }

        protected String getTitle(int index) {
            return mPois.get(index).getTitle();
        }

        protected String getSnippet(int index) {
            return mPois.get(index).getSnippet();
        }

        /**
         * 从marker中得到poi在list的位置。
         *
         * @param marker 一个标记的对象。
         * @return 返回该marker对应的poi在list的位置。
         * @since V2.1.0
         */
        public int getPoiIndex(Marker marker) {
            for (int i = 0; i < mPoiMarks.size(); i++) {
                if (mPoiMarks.get(i).equals(marker)) {
                    return i;
                }
            }
            return -1;
        }

        /**
         * 返回第index的poi的信息。
         * @param index 第几个poi。
         * @return poi的信息。poi对象详见搜索服务模块的基础核心包（com.amap.api.services.core）中的类 <strong><a href="../../../../../../Search/com/amap/api/services/core/PoiItem.html" title="com.amap.api.services.core中的类">PoiItem</a></strong>。
         * @since V2.1.0
         */
        public PoiItem getPoiItem(int index) {
            if (index < 0 || index >= mPois.size()) {
                return null;
            }
            return mPois.get(index);
        }

    }
}
