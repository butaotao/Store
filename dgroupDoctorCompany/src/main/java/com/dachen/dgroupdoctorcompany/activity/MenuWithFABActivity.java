package com.dachen.dgroupdoctorcompany.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.SingnTodayAdapter;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.entity.SignTodayInList;
import com.dachen.dgroupdoctorcompany.utils.DataUtils.SinUtils;
import com.dachen.dgroupdoctorcompany.utils.TitleManager;
import com.dachen.dgroupdoctorcompany.views.CustomButtonFragment;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.TimeUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MenuWithFABActivity extends SignInActivity implements View.OnClickListener{
    PullToRefreshListView refreshScrollView;
    int pageIndex = 0;
    private int pageSize = 20;
    public List<SignTodayInList.Data.DataList> mDataLists = new ArrayList<>();
    SingnTodayAdapter mAdapter;
    TextView tv_week;
    TextView tv_time;
    ImageView iv_alert;
    RelativeLayout rl_titlebar;
    ImageView iv_back;
    TextView tv_back;
    public long ytdayOffTime;
    public long ytdayWorkTime;
    public long timeStamp;
    public CustomButtonFragment fragment;
    int refresh = 0;
    //adapter_menusign
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_menu_sing, null);
        setContentView(view);
        setTitle("签到");
        SinUtils.signedId = "";
        SinUtils.sigStep = -1;
        setTitlecolor(getResources().getColor(R.color.white));
        rl_titlebar = (RelativeLayout) findViewById(R.id.rl_titlebar);
        rl_titlebar.setBackgroundColor(getResources().getColor(R.color.color_3cbaff));
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setTextColor(Color.WHITE);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setBackgroundResource(R.drawable.icon_back_n);
        findViewById(R.id.line_titlebar).setVisibility(View.GONE);
        TitleManager.showImage(this, view, this, "", R.drawable.notice_nomorl);
        changerTitleBar();
        findViewById(R.id.btn_sinrecord).setOnClickListener(this);
        Button teamRecord = (Button)findViewById(R.id.btn_teamrecord);
        if (isDeptartManager()) {//如果是部门主管则显示团队统计按钮
            teamRecord.setOnClickListener(this);
            teamRecord.setVisibility(View.VISIBLE);
            showGuider(true);
        }else {
            teamRecord.setVisibility(View.GONE);
            showGuider(false);
        }
        tv_week = (TextView) findViewById(R.id.tv_week);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_week.setText(TimeUtils.getWeek(System.currentTimeMillis()));
        tv_time.setText(TimeUtils.getTimeDay());
        iv_alert = (ImageView) findViewById(R.id.iv_alert);
        refreshScrollView = (PullToRefreshListView) findViewById(R.id.refresh_scroll_view);
        refreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        refreshScrollView.setFocusable(false);
        fragment = new CustomButtonFragment();
        fragment.setActivity(this,System.currentTimeMillis());
        IntentFilter filter = new IntentFilter();
        filter.addAction("action.to.signlisttoday");
        registerReceiver(hasMessageReceiver, filter);
        refreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 0;
                //mMeetingList.clear();
                getListData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //refreshScrollView.onRefreshComplete();
                refreshScrollView.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        refreshScrollView.onRefreshComplete();
                    }
                }, 1000);
            }
        });
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
        mAdapter = new SingnTodayAdapter(this);
        refreshScrollView.setAdapter(mAdapter);
        refresh = 0;
    }

    /**
     * 展示这个版本的签到导航
     * @param isManager 管理员
     */
    private void showGuider(Boolean isManager) {//
        Intent intent = new Intent(getApplicationContext(),GuiderActivity.class);
        if (isManager && SharedPreferenceUtil.getBoolean(getApplicationContext(),"isFirst",true)) {
            intent.putExtra("guider","manager");
            startActivity(intent);
        }else if(SharedPreferenceUtil.getBoolean(getApplicationContext(),"isFirst",true)) {
            intent.putExtra("guider","user");
            startActivity(intent);
        }
        SharedPreferenceUtil.putBoolean(getApplicationContext(),"isFirst",false);
    }

    private boolean isDeptartManager() {
        CompanyContactDao dao = new CompanyContactDao(getApplicationContext());
        CompanyContactListEntity entity = dao.queryByUserid(SharedPreferenceUtil.getString(this, "id", ""));
        return entity != null && entity.deptManager == 1;
    }

    @Override
    public void onResume() {
        super.onResume();
        getListData();

    }

    private void getListData() {
        showLoadingDialog();//signed/getTodaySignedList
        String type = "";
        new HttpManager().post(this, Constants.GET_VISIT_LIST_TODAY, SignTodayInList.class,
                Params.getList(MenuWithFABActivity.this, type, "", pageIndex, pageSize),
                this, false, 4);
    }
    public void start(){
        Intent intent2 = new Intent(this,SelectAddressActivity.class);
        intent2.putExtra("mode",AddSignInActivity.MODE_VISIT);
        intent2.putExtra("type","signle");
        intent2.putExtra("poi",this.POI);
        intent2.putExtra("time",timeStamp);
        intent2.putExtra("distance",this.distance);
        intent2.putExtra("latitude",this.latitude);
        intent2.putExtra("longitude",this.longitude);
        intent2.putExtra("city", this.city);
        startActivity(intent2);
    }
    @Override
    public void onSuccess(Result response) {
        super.onSuccess(response);
        closeLoadingDialog();
        refreshScrollView.onRefreshComplete();
        if(null!=response){
            if (response instanceof SignTodayInList){
                if (response.resultCode==1){
                    SignTodayInList signInList = (SignTodayInList) response;
                    SignTodayInList.Data data = signInList.data;

                    if(null != data ){

                        ytdayOffTime = data.ytdayOffTime;
                        ytdayWorkTime = data.ytdayWorkTime;
                        timeStamp = data.timeStamp;
                        tv_week.setText(TimeUtils.getWeek(timeStamp));
                        tv_time.setText(TimeUtils.getTimeDay(timeStamp));
                        fragment.setActivity(this,timeStamp);
                        int beforeSize = mDataLists.size();
                        if(null != data.signedList && data.signedList.size()>0){
                            if (pageIndex==0){
                                mDataLists.clear();
                            }
                            Collections.reverse(data.signedList);
                            mDataLists.addAll(data.signedList);
//                    findViewById(R.id.empty_view).setVisibility(View.GONE);
                        }else{
//                    if(pageIndex == 0){
//                        findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
//                    }
                        }

                        if(pageIndex==0){

                            mAdapter.addData(mDataLists,true);
                        }else{
                            mAdapter.addData(mDataLists,false);
                        }
                        mAdapter.notifyDataSetChanged();
                        /*if (refresh!=0){
                            refreshScrollView.scrollTo(0, refreshScrollView.getBottom());
                        }*/
                        refresh ++;
                        if(beforeSize>0){
                            int afterSize = mDataLists.size();
                            if(beforeSize == afterSize){
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        if (SinUtils.sigStep == 1){
                            SinUtils.sigStep = -1;

                            Intent intent = new Intent(MenuWithFABActivity.this, SelfVisitActivity.class);
                            intent.putExtra("address", SinUtils.addresss);
                            intent.putExtra("longitude", SinUtils.longituds);
                            intent.putExtra("latitude", SinUtils.latitudes);
                            intent.putExtra("addressname", SinUtils.snippets);
                            intent.putExtra("time",timeStamp);
                            intent.putExtra("mode", CustomerVisitActivity.MODE_FROM_SIGN);
                            intent.putExtra("city", city);
                            intent.putExtra("signedId",SinUtils.signedId);
                            startActivity(intent);
                            SinUtils.signedId = "";
                        }
                    }else{
                    }
                }
                else {
                    ToastUtil.showToast(MenuWithFABActivity.this,response.getResultMsg());
                }
            }
            }else {
            ToastUtil.showToast(MenuWithFABActivity.this,response.getResultMsg());
        }

    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        closeLoadingDialog();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_sinrecord:
                Intent signIntent = new Intent(this,SignListActivity.class);
                startActivity(signIntent);
                if (null!=fragment&&null!=fragment.circleMenu){
                    fragment.circleMenu.close(true);
                }

                break;
            case R.id.btn_teamrecord:
                Intent teamIntent = new Intent(this,SignRecordActivity.class);
                teamIntent.putExtra("type","sign");
                startActivity(teamIntent);
                if (null!=fragment&&null!=fragment.circleMenu){
                    fragment.circleMenu.close(true);
                }
                break;
            case R.id.iv_stub:
                Intent intent4 = new Intent(this,SigninRemindActivity.class);
                startActivity(intent4);
                if (null!=fragment&&null!=fragment.circleMenu){
                    fragment.circleMenu.close(true);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null!=hasMessageReceiver){
            unregisterReceiver(hasMessageReceiver);
        }
    }

    BroadcastReceiver hasMessageReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context arg0, Intent intent) {
            String command = intent.getAction();
            if(!TextUtils.isEmpty(command)){
                if("action.to.signlisttoday".equals(command)){
                    pageIndex = 0;
                    getListData();
                }
            }
        }
    };
    public void choicePlaceSign(int type,String tabid){
        Intent intent = new Intent(this,SelectAddressActivity.class);
        intent.putExtra("mode",AddSignInActivity.MODE_WORKING);
        intent.putExtra("poi",POI);
        intent.putExtra("singmode",type);
        intent.putExtra("tabid",tabid);
        intent.putExtra("time",timeStamp);
        intent.putExtra("distance",distance);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
        intent.putExtra("city",city);
        startActivity(intent);
    }
}