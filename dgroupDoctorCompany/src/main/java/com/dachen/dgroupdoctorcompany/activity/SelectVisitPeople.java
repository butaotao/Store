package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.amap.api.maps.model.MyTrafficStyle;
import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.CompanyApplication;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.AddVisitGroup;
import com.dachen.dgroupdoctorcompany.entity.ConfirmVisit;
import com.dachen.dgroupdoctorcompany.entity.VisitPeople;
import com.dachen.dgroupdoctorcompany.utils.CommonUitls;
import com.dachen.dgroupdoctorcompany.utils.DataUtils.GetUserDepId;
import com.dachen.dgroupdoctorcompany.utils.JsonMananger;
import com.dachen.dgroupdoctorcompany.views.RadarViewGroup;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;
import com.dachen.medicine.view.CustomDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by weiwei on 2016/6/22.
 */
public class SelectVisitPeople extends BaseActivity implements HttpManager.OnHttpListener, LocationSource,
        AMapLocationListener, AMap.OnInfoWindowClickListener,
        AMap.InfoWindowAdapter, AMap
        .OnCameraChangeListener, AMap.OnMapTouchListener{
    public final static int MODE_ADD = 1;
    public final static int MODE_JOIN = 2;
    private int mMode;
    private final static int MSG_UPDATE_TIME = 101;
    private RadarViewGroup mRadarViewGroup;
    private Button mBtSure;
    private ImageView mIvPicture;
    private String mStrAddress;
    private double latitude;//纬度
    private double longitude;//经度
    private String mStrAddressName;
    private String docterName;
    private String docterId;
    private String jsonMediea;
    private String jsonMedieaName;
    private String jsonVisitPeople;
    private String initatorId;
    private String initatorName;
    private String headPic;
    private String id;
    private AMap mAMap;
    private List<VisitPeople>mVisitPeopleList = new ArrayList<>();
    private SparseArray<VisitPeople>mVisitPeopleSparseArray = new SparseArray<>();
    public int from;
    private boolean isPressCancel = true;//用来区分点击的是取消还是返回按钮
    private static SelectVisitPeople instance;
    private int nCount = 1;//记录人数
    private int cancelVistitTogetter;
    private TextView mTvAddress;
    private TextView mTvName;
    private TextView tvMediea;
    private TextView tvTimeCount;
    private TextView tvSure;
    private int time;
    private MyHandler mMyHandler;
    private Button btn_choiceposition;
    private MapView mMapView;
    private int level = 17;
    double mlatitude = 0;
    private String                      city;//定位所在的城市
    double mlongitude = 0;
    private MyTrafficStyle mMyTrafficStyle;
    private Marker locationMarker; // 选择的点
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
    public void onInfoWindowClick(Marker marker) {

    }


    @Override
    public void onTouch(MotionEvent motionEvent) {

    }



    public class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_UPDATE_TIME:
                    String strTime = tvTimeCount.getText().toString().trim();
                    int nTime = Integer.valueOf(strTime);
                    if(nTime>0){
                        nTime--;
                        tvTimeCount.setText(nTime+"");
                        mMyHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME,1000);
                    }else{
                        final CustomDialog dialog = new CustomDialog(SelectVisitPeople.this);
                        dialog.showDialog("提示", "协同拜访已超时", R.string.godelete,
                                0, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dimissDialog();
                                        finish();
                                    }
                                }, null);


                    }
                    break;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_visit_people);
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        instance = this;
        initView();
        initData();
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
        mAMap.showMapText(false);
        UiSettings uiSettings = mAMap.getUiSettings();
        uiSettings.setScaleControlsEnabled(false);
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setAllGesturesEnabled(false);
        uiSettings.setZoomGesturesEnabled(false);
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setCompassEnabled(false);

        setAMap();
    }
    private void setAMap(){
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), level));

       /* locationMarker = mAMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .position(new LatLng(latitude, longitude)));

        locationMarker.showInfoWindow();*/
    }
    public static SelectVisitPeople getInstance(){
        return instance;
    }

    @Override
    public void initView() {
        super.initView();
//        setTitle("寻找拜访人员");
        mRadarViewGroup = (RadarViewGroup) this.findViewById(R.id.radarViewGroup);
        mBtSure = (Button) this.findViewById(R.id.btSure);
        mBtSure.setOnClickListener(this);
        btn_choiceposition = (Button) this.findViewById(R.id.btn_choiceposition);
        btn_choiceposition.setOnClickListener(this);
        btn_choiceposition.setVisibility(View.VISIBLE);
        mIvPicture = (ImageView) mRadarViewGroup.findViewById(R.id.ivPicture);
        mTvAddress = (TextView) this.findViewById(R.id.tvAddress);
        mTvName = (TextView) this.findViewById(R.id.tvName);
        tvMediea = (TextView) this.findViewById(R.id.tvMediea);
        tvTimeCount = (TextView) this.findViewById(R.id.tvTimeCount);
        tvSure = (TextView) this.findViewById(R.id.tvSure);

        this.findViewById(R.id.tvCancel).setOnClickListener(this);
    }

    private void initData(){
        mMyHandler = new MyHandler();
//        mMode = this.getIntent().getIntExtra("mode",MODE_ADD);
        from = this.getIntent().getIntExtra("from",TogetherVisitActivity.MODE_FROM_VIST_LIST);

        mStrAddress = this.getIntent().getStringExtra("addressName");
        latitude = this.getIntent().getDoubleExtra("latitude",0);
        longitude = this.getIntent().getDoubleExtra("longitude",0);
        mStrAddressName = this.getIntent().getStringExtra("address");
        docterName = this.getIntent().getStringExtra("doctorname");
        docterId = this.getIntent().getStringExtra("doctorid");
        jsonMediea = this.getIntent().getStringExtra("mediea");
        jsonMedieaName = this.getIntent().getStringExtra("medieaName");
        jsonVisitPeople = this.getIntent().getStringExtra("jsonPeople");
        initatorId = this.getIntent().getStringExtra("initatorId");
        initatorName = this.getIntent().getStringExtra("initatorName");
        headPic = this.getIntent().getStringExtra("headPic");
        id = this.getIntent().getStringExtra("id");
        time = this.getIntent().getIntExtra("time",0);

        mTvAddress.setText(mStrAddress);
        if(!TextUtils.isEmpty(docterName)){
            mTvName.setText(docterName);
        }else{
            mTvName.setText("不记名拜访");
        }

        String userId = SharedPreferenceUtil.getString(this, "id", "");
        if((userId).equals(initatorId)){
            mMode = MODE_ADD;
        }else{
            mMode = MODE_JOIN;
        }
        btn_choiceposition.setVisibility(View.GONE);
        if(MODE_ADD == mMode){
            mBtSure.setVisibility(View.VISIBLE);
            tvSure.setVisibility(View.GONE);
            setTitle("寻找拜访人员");
            btn_choiceposition.setVisibility(View.VISIBLE);
        }else if(MODE_JOIN == mMode){
            mBtSure.setVisibility(View.GONE);
            tvSure.setVisibility(View.VISIBLE);
            setTitle(initatorName+"的协同拜访");
        }

        if(!TextUtils.isEmpty(jsonMedieaName)){
            tvMediea.setText(jsonMedieaName);
        }else{
            tvMediea.setText("无");
        }


        tvTimeCount.setText(""+time);
        tvSure.setText("等待" + initatorName + "确认...");

//        mBtSure.setText("确定选择(1)人");
        btn_choiceposition.setOnClickListener(this);
        mMyHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME,1000);

        if(!TextUtils.isEmpty(headPic)){
            ImageLoader.getInstance().displayImage(headPic,mIvPicture, CompanyApplication.mAvatarCircleImageOptions);
        }else{
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.head_icon);
            Bitmap circleBitMap = CommonUitls.getRoundedCornerBitmap(bitmap);
            mIvPicture.setImageBitmap(circleBitMap);
        }

        if(!TextUtils.isEmpty(jsonVisitPeople)){
            mVisitPeopleList = new ArrayList<>();
            mVisitPeopleList = JsonMananger.jsonToList(jsonVisitPeople,VisitPeople.class);
            for(int i=0;i<mVisitPeopleList.size();i++){//先将创建者从中去掉
                VisitPeople visitPeople = mVisitPeopleList.get(i);
                if((initatorId).equals(visitPeople.id)){
                    mVisitPeopleList.remove(visitPeople);
                    break;
                }
            }

            for(int i=0;i<mVisitPeopleList.size();i++){
                VisitPeople visitPeople = mVisitPeopleList.get(i);
                mVisitPeopleSparseArray.put(i,visitPeople);
            }
            mRadarViewGroup.setDatas(mVisitPeopleSparseArray);

        }
// else{
//            for(int i=0;i<2;i++){
//                VisitPeople visitPeople = new VisitPeople();
//                visitPeople.name = i+"上帝";
//                visitPeople.id = "i";
//                mVisitPeopleSparseArray.put(i,visitPeople);
//            }
//            mRadarViewGroup.setDatas(mVisitPeopleSparseArray);
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btSure:
                Set<VisitPeople> set = new HashSet<>();
                set.addAll(mVisitPeopleList);
                int size = set.size();
                if(size<=0){
                    ToastUtil.showToast(this,"协同拜访至少需要2人参加");
                    return;
                }
                showLoadingDialog();
                new HttpManager().get(this, Constants.CONFIRM_VISIT_GROUP, ConfirmVisit.class,
                        Params.joinVisitGroup(SelectVisitPeople.this,id),
                        this,false,4);
                /*
                Intent intent = new Intent(this,JointVisitActivity.class);
                intent.putExtra("mode",CustomerVisitActivity.MODE_FROM_SIGN);
                intent.putExtra("addressname",mStrAddressName);
                intent.putExtra("address",mStrAddress);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                intent.putExtra("mediea",jsonMediea);
                intent.putExtra("medieaName",jsonMedieaName);
                intent.putExtra("jsonPeople",jsonVisitPeople);
                intent.putExtra("docterName",docterName);
                intent.putExtra("docterId",docterId);
                startActivity(intent);*/
                break;
            case R.id.iv_back:
            case R.id.rl_back:
                onBackPressed();
                break;
            case R.id.btn_choiceposition:
                cancelVistitTogetter = 1;
                deleteVisitGroup();
                break;
            case R.id.tvCancel:
                isPressCancel = true;
                if(MODE_ADD == mMode){
                    final CustomDialog dialog = new CustomDialog(SelectVisitPeople.this);
                    dialog.showDialog("提示", "是否取消该协同拜访", R.string.visit_group_no, R.string.visit_group_yes, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dimissDialog();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dimissDialog();
                            deleteVisitGroup();
                        }
                    });

                }else if(MODE_JOIN == mMode){
                    final CustomDialog dialog = new CustomDialog(SelectVisitPeople.this);
                    dialog.showDialog("提示", "是否退出该协同拜访", R.string.visit_group_no, R.string.visit_group_yes, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dimissDialog();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dimissDialog();
                            cancelVisitGroup();
                        }
                    });
                }
                break;
        }
    }
    //拜访组成员取消拜访
    private void cancelVisitGroup(){
        showLoadingDialog();
        new HttpManager().get(this, Constants.CANCEL_VISIT_GROUP, Result.class,
                Params.joinVisitGroup(SelectVisitPeople.this, id),
                this,false,4);
    }

    //拜访创建者取消拜访
    private void deleteVisitGroup(){
        showLoadingDialog();
        new HttpManager().get(this, Constants.DELETE_VISIT_GROUP, Result.class,
                Params.deleteVisitGroup(SelectVisitPeople.this, id),
                this,false,4);
    }

    @Override
    public void onSuccess(Result response) {
        closeLoadingDialog();

        if(null!=response){
            if(response.getResultCode() == 1){
                if(response instanceof ConfirmVisit){//确定协同拜访
                    ToastUtil.showToast(this,"请求成功");
                }else if(response instanceof AddVisitGroup){
//                    mBtSure.setText("确定选择(1)人");
                    btn_choiceposition.setVisibility(View.GONE);
                    for(int i=0;i<mVisitPeopleList.size();i++){
                        VisitPeople visitPeople = mVisitPeopleList.get(i);
                        mRadarViewGroup.removeCircleView(visitPeople);
                    }
                    mVisitPeopleList.clear();
                    tvTimeCount.setText(120+"");
                    mMyHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME,1000);
                }else {
                    if (cancelVistitTogetter!=1){
                        ToastUtil.showToast(this, "取消协同拜访成功");
                      //  if(isPressCancel){

                            onExit();
                       // }
                    }else {
                        choicePlace();
                    }

                }
            }else{
                String msg = response.getResultMsg();
                ToastUtil.showToast(this,msg);
            }
        }else{
            ToastUtil.showErrorData(this);
        }
    }
    public void choicePlace(){
        Intent intent = new Intent(this,SelectAddressActivity.class);
        intent.putExtra("type","selectVisitPeopleposition");
        intent.putExtra("mode",AddSignInActivity.MODE_VISIT);
        startActivity(intent);
        finish();
    }
    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        closeLoadingDialog();
        ToastUtil.showErrorNet(this);
        if(!isPressCancel){
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        isPressCancel = false;
        if(MODE_ADD == mMode){
            final CustomDialog dialog = new CustomDialog(SelectVisitPeople.this);
            dialog.showDialog("提示", "是否取消该协同拜访", R.string.visit_group_no, R.string.visit_group_yes, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dimissDialog();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dimissDialog();
                    deleteVisitGroup();
                }
            });

        }else if(MODE_JOIN == mMode){
            final CustomDialog dialog = new CustomDialog(SelectVisitPeople.this);
            dialog.showDialog("提示", "是否退出该协同拜访", R.string.visit_group_no, R.string.visit_group_yes, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dimissDialog();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dimissDialog();
                    cancelVisitGroup();
                }
            });

        }
    }

    public void addVisitPeople(VisitPeople visitPeople){

        if(null != visitPeople){
            if(!isAddPeople(visitPeople)){
                SparseArray<VisitPeople> sparseArray = new SparseArray<>();
                sparseArray.put(0, visitPeople);
                mRadarViewGroup.setDatas(sparseArray);
                mVisitPeopleList.add(visitPeople);

                Set<VisitPeople> set = new HashSet<>();
                set.addAll(mVisitPeopleList);
                int size = set.size();
                size = size+1;
                if (size>=2){
                    mBtSure.setText("确定" + size + "人");

                    btn_choiceposition.setVisibility(View.GONE);
                }else {
                    mBtSure.setText("确定拜访");
                    btn_choiceposition.setVisibility(View.VISIBLE);
                }

                tvSure.setVisibility(View.GONE);

            }

        }
        if(mMode == MODE_JOIN){
            tvSure.setText("等待" + initatorName + "确认...");
            tvSure.setVisibility(View.VISIBLE);
        }
    }

    private boolean isAddPeople(VisitPeople people){
        boolean isAdd = false;
        for(int i=0;i<mVisitPeopleList.size();i++){
            VisitPeople visitPeople = mVisitPeopleList.get(i);
            if((people.id).equals(visitPeople.id)){
                isAdd = true;
                break;
            }
        }
        return isAdd;
    }
    //创建者取消协同拜访时，参与者应做的操作
    public void createDeleteVisit(){
        final CustomDialog dialog = new CustomDialog(this);
        String name = !TextUtils.isEmpty(initatorName)?initatorName:"创建者";
        dialog.showDialog("提示", name + "已取消协同拜访", R.string.visit_group_yes,
                0, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dimissDialog();
                        onExit();
                    }
                }, null);
    }

    public void onExit(){
        Intent intent;
        if(TogetherVisitActivity.MODE_FROM_VIST_LIST == from){
          /*  intent = new Intent(this,VisitListActivity.class);
            startActivity(intent);*/
        }else if(TogetherVisitActivity.MODE_FROM_SIGN == from){
            /*intent = new Intent(this,SignInActivity.class);
            startActivity(intent);*/
        }

        if (MODE_ADD == mMode){
            //MActivityManager.getInstance().finishAppointActivity(VisitListActivity.class);
        }
        finish();
    }

    public void removeView(VisitPeople visitPeople){
        if(null != mRadarViewGroup){
            for(int i=0;i<mVisitPeopleList.size();i++){
                VisitPeople item = mVisitPeopleList.get(i);
                if((visitPeople.id).equals(item.id)){
                    mVisitPeopleList.remove(item);
                }
            }

            Set<VisitPeople> set = new HashSet<>();
            set.addAll(mVisitPeopleList);
            int size = set.size()+1;
            mRadarViewGroup.removeCircleView(visitPeople);

            if (size>=2){
                mBtSure.setText("确定" + size + "人");

                btn_choiceposition.setVisibility(View.GONE);
            }else {
                mBtSure.setText("确定拜访");
                btn_choiceposition.setVisibility(View.VISIBLE);
            }
            tvSure.setVisibility(View.GONE);
        }
        if(mMode == MODE_JOIN){
            tvSure.setText("等待" + initatorName + "确认...");
            tvSure.setVisibility(View.VISIBLE);
        }

    }

    public void createVisitGroup(){
        String orginId = GetUserDepId.getUserDepId(this);
        String userName = SharedPreferenceUtil.getString(this,"username","");
        if(latitude==0 || longitude==0){
            ToastUtil.showToast(this,"定位失败，请重新选择地址");
            return;
        }
        showLoadingDialog();
        new HttpManager().post(this, Constants.ADD_VISIT_GROUP, AddVisitGroup.class,
                Params.addVisitGroup(SelectVisitPeople.this,orginId,userName,String.valueOf(latitude),String.valueOf(longitude),
                        docterId,docterName,mStrAddressName,mStrAddress,jsonMediea),
                this,false,4);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cancelVistitTogetter = 0;
    }
}
