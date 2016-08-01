package com.dachen.dgroupdoctorcompany.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.SignInBaseData;
import com.dachen.dgroupdoctorcompany.utils.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;

import java.util.ArrayList;

/**
 * Created by weiwei on 2016/3/30.
 */
public class SignInActivity extends BaseActivity implements HttpManager.OnHttpListener<Result>,AMapLocationListener {
    private TextView            mTvSignAll;
    private ImageView           mIvChecking;
    private ImageView           mIvVisit;
    private String              POI;
    private int                 distance;
    private LinearLayout vSignin;
    private LinearLayout vVisit;
    private LinearLayout vTogetherVisit;
    private LinearLayout signin_remind;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private double                     latitude;//纬度
    private double                     longitude;//经度
    private String                     city;//城市
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();
        initData();

        //接受广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("action.to.signlist");
        registerReceiver(hasMessageReceiver, filter);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("选择签到类型");
        mTvSignAll = (TextView) findViewById(R.id.tvSignAll);
        mIvChecking = (ImageView) findViewById(R.id.ivChecking);
        mIvVisit = (ImageView) findViewById(R.id.ivVisit);
        vSignin = (LinearLayout) findViewById(R.id.vSignin);
        vVisit = (LinearLayout) findViewById(R.id.vVisit);
        vTogetherVisit = (LinearLayout) findViewById(R.id.vTogetherVisit);
        signin_remind = getViewById(R.id.signin_remind);
        mTvSignAll.setOnClickListener(this);
        vSignin.setOnClickListener(this);
        vVisit.setOnClickListener(this);
        vTogetherVisit.setOnClickListener(this);
        signin_remind.setOnClickListener(this);
    }

    private void initData(){
        if(UserInfo.getInstance(SignInActivity.this).isMediePresent()){
            vVisit.setVisibility(View.VISIBLE);
            vTogetherVisit.setVisibility(View.VISIBLE);
        }else{
            vVisit.setVisibility(View.GONE);
            vTogetherVisit.setVisibility(View.GONE);
        }
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置定位监听
        locationClient.setLocationListener(this);
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

//        String url = "http://192.168.3.7:8082/visit/getBasicData";
//        new HttpManager().requestBase(Request.Method.GET,url,this,SignInBaseData.class, Params
//                .getInfoParams(SignInActivity.this),this,false,1);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tvSignAll:
                Intent intentAll = new Intent(SignInActivity.this,SignListActivity.class);
                startActivity(intentAll);
                break;
            case R.id.vSignin:
                Intent intent = new Intent(SignInActivity.this,SelectAddressActivity.class);
                intent.putExtra("mode",AddSignInActivity.MODE_WORKING);
                intent.putExtra("poi",POI);
                intent.putExtra("distance",distance);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                intent.putExtra("city",city);
                startActivity(intent);
                break;
            case R.id.vVisit:
                Intent intent2 = new Intent(SignInActivity.this,SelectAddressActivity.class);
                intent2.putExtra("mode",AddSignInActivity.MODE_VISIT);
                intent2.putExtra("type","signle");
                intent2.putExtra("poi",POI);
                intent2.putExtra("distance",distance);
                intent2.putExtra("latitude",latitude);
                intent2.putExtra("longitude",longitude);
                intent2.putExtra("city",city);
                startActivity(intent2);
                break;
            case R.id.vTogetherVisit:
                Intent intent3 = new Intent(SignInActivity.this,SelectAddressActivity.class);
                intent3.putExtra("mode",AddSignInActivity.MODE_VISIT);
                intent3.putExtra("type","together");
                intent3.putExtra("poi",POI);
                intent3.putExtra("distance",distance);
                intent3.putExtra("latitude",latitude);
                intent3.putExtra("longitude",longitude);
                intent3.putExtra("city",city);
                startActivity(intent3);
                break;
            case R.id.signin_remind:
                Intent intent4 = new Intent(SignInActivity.this,SigninRemindActivity.class);

                startActivity(intent4);
                break;
        }
    }

    @Override
    public void onSuccess(Result response) {
        if(null!=response){
            if(response instanceof SignInBaseData){
                SignInBaseData signInBaseData= (SignInBaseData) response;
                if(null != signInBaseData){
                    SignInBaseData.Data data = signInBaseData.data;
                    if(null != data && null!=data.map){
                        distance = data.map.distance;
                        POI = data.map.point;
                    }
                }
            }
        }

    }

    @Override
    public void onSuccess(ArrayList<Result> response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if(null != aMapLocation){
            int code = aMapLocation.getErrorCode();
            if(code == 0){
                latitude = aMapLocation.getLatitude();
                longitude = aMapLocation.getLongitude();
                city = aMapLocation.getCity();
            }
        }
        new HttpManager().post(this, Constants.GET_VISIT_BASIC_DATA, SignInBaseData.class, Params
                .getInfoParams(SignInActivity.this),this,false,4);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }

        unregisterReceiver(hasMessageReceiver);
    }

    /** 广播 **/
    BroadcastReceiver hasMessageReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context arg0, Intent intent) {
            String command = intent.getAction();
            if(!TextUtils.isEmpty(command)){
                if("action.to.signlist".equals(command)){
                    Intent intentAll = new Intent(SignInActivity.this,SignListActivity.class);
                    startActivity(intentAll);
                }
            }
        }
    };


}
