package com.dachen.dgroupdoctorcompany.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.VisitGroupAdapter;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.JoinVisitGroup;
import com.dachen.dgroupdoctorcompany.entity.VisitGroupEntity;
import com.dachen.dgroupdoctorcompany.utils.GaoDeMapUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by weiwei on 2016/6/23.
 */
public class TogetherVisitActivity extends BaseActivity implements GaoDeMapUtils.LocationListener,HttpManager.OnHttpListener,PullToRefreshBase.OnRefreshListener2<ListView> {
    public static final int        REQUEST_SELECT_ADDRESS = 102;

    public static final int MODE_FROM_SIGN = 1;//从地图列表选择页过来
    public static final int MODE_FROM_VIST_LIST = 2;//从拜访列表页过来
    private int mMode;
    private LinearLayout           vAddress;
    private TextView               mTvAddress;
    private TextView               mTvTips;
    private PullToRefreshListView  mLvTogether;
    private LinearLayout           empty_view;
    private Button                 mBtAdd;
    private LinearLayout           vBottom;
    private TextView               mTvAddVisit;

    private GaoDeMapUtils          mGaoDeMapUtils;
    private double                 latitude;//纬度
    private double                 longitude;//经度
    private String                 city;//城市
    private String                 mStrFloor;
    private String                 mStrAddressName;
    private static TogetherVisitActivity instance;

    private List<VisitGroupEntity.Data.VistGroupItem> mVistGroupItemList = new ArrayList<>();
    private VisitGroupAdapter  mVisitGroupAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_together_visit);
        instance = this;
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("选择协同拜访");
        vAddress = (LinearLayout) findViewById(R.id.vAddress);
        mTvAddress = (TextView) findViewById(R.id.tvAddress);
        mTvTips = (TextView) findViewById(R.id.tvTips);
        mLvTogether = (PullToRefreshListView) findViewById(R.id.lvTogether);
        empty_view = (LinearLayout) findViewById(R.id.empty_view);
        mBtAdd = (Button) findViewById(R.id.btAdd);
        vBottom = (LinearLayout) findViewById(R.id.vBottom);
        mTvAddVisit = (TextView) findViewById(R.id.tvAddVisit);
        mLvTogether.setEmptyView(empty_view);
        vAddress.setOnClickListener(this);
        mBtAdd.setOnClickListener(this);
        mTvAddVisit.setOnClickListener(this);
    }

    private void initData(){
        mMode = this.getIntent().getIntExtra("mode",MODE_FROM_VIST_LIST);
        mGaoDeMapUtils = new GaoDeMapUtils(this.getApplicationContext(),this);
        mVisitGroupAdapter = new MyAdapter(TogetherVisitActivity.this,mVistGroupItemList);
        mLvTogether.setAdapter(mVisitGroupAdapter);
        mLvTogether.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mLvTogether.setOnRefreshListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(MODE_FROM_VIST_LIST == mMode){
//            showLoadingDialog();
            if(TextUtils.isEmpty(mStrFloor)){
                vAddress.setVisibility(View.VISIBLE);
                mTvAddress.setText("定位中");
                mGaoDeMapUtils.startLocation();
            }

        }else if(MODE_FROM_SIGN == mMode){
            mStrAddressName = this.getIntent().getStringExtra("addressname");
            mStrFloor = this.getIntent().getStringExtra("address");
            latitude = this.getIntent().getDoubleExtra("latitude", 0);
            longitude = this.getIntent().getDoubleExtra("longitude", 0);
            city = this.getIntent().getStringExtra("city");
            mTvAddress.setText(mStrFloor);
        }
        getVisitGroup();
    }

    public static TogetherVisitActivity getInstance() {
        return instance;
    }

    public void getVisitGroup(){
        showLoadingDialog();
        new HttpManager().get(this, Constants.GET_VISIT_GROUP, VisitGroupEntity.class,
                Params.getVisitGroup(TogetherVisitActivity.this,String.valueOf(latitude),String.valueOf(longitude)),
                this,false,4);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent ;
        switch (v.getId()){
            case R.id.vAddress:
                intent = new Intent(TogetherVisitActivity.this,SelectAddressActivity.class);
                intent.putExtra("select_mode",SelectAddressActivity.MODE_SELECT_ADDRESS);
                intent.putExtra("poi","地名地址信息|医疗保健服务|商务住宅|交通设施服务|公司企业|公共设施");
                intent.putExtra("distance",250);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                intent.putExtra("city",city);
                startActivityForResult(intent,REQUEST_SELECT_ADDRESS);
                break;
            case R.id.tvAddVisit:
            case R.id.btAdd:
                intent = new Intent(TogetherVisitActivity.this,ChoiceDoctorForChatActivity.class);
                intent.putExtra("where","AddSignInActivity");
                intent.putExtra("type","selectAddress");
                intent.putExtra("address",mStrFloor);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                intent.putExtra("city",city);
                intent.putExtra("addressName",mStrAddressName);
                intent.putExtra("from",mMode);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onLocation(Object object) {
        if(null != object){
            Map<String,Object> map = (Map<String, Object>) object;
            latitude = (double) map.get("latitude");
            longitude = (double) map.get("longitude");
            city = (String) map.get("city");
            mStrFloor = (String) map.get("floor");
            mTvAddress.setText(mStrFloor);
            mStrAddressName = (String) map.get("address");

            getVisitGroup();
        }else{
            mTvAddress.setText("定位失败");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_SELECT_ADDRESS && resultCode == RESULT_OK && null != data) {
            mStrFloor = data.getStringExtra("floor");
            latitude = data.getDoubleExtra("latitude", 0);
            longitude = data.getDoubleExtra("longitude", 0);
            mStrAddressName = data.getStringExtra("address");
            mTvAddress.setText(mStrFloor);
        }
    }

    @Override
    public void onSuccess(Result response) {
        closeLoadingDialog();
        mLvTogether.onRefreshComplete();
        if(null != response){
            if(response.getResultCode() == 1){
                if(response instanceof VisitGroupEntity){
                    VisitGroupEntity visitGroupEntity = (VisitGroupEntity) response;
                    VisitGroupEntity.Data data = visitGroupEntity.data;
                    if(null != data && null != data.dataList && data.dataList.size()>0){
                        mVistGroupItemList.clear();
                        mVistGroupItemList.addAll(data.dataList);
                        mVisitGroupAdapter.notifyData(mVistGroupItemList);
                        setViewVisibility(1);
                    }else{
                        mVistGroupItemList.clear();
                        mVisitGroupAdapter.notifyData(mVistGroupItemList);
                        setViewVisibility(0);
                    }
                }else if(response instanceof JoinVisitGroup){
                    JoinVisitGroup joinVisitGroup = (JoinVisitGroup) response;
                    JoinVisitGroup.Data data = joinVisitGroup.data;
                    if(null != data){
                        openVisitGroup(data);
                    }
                }
            }else{
                setViewVisibility(0);
                String msg = response.getResultMsg();
                ToastUtil.showToast(TogetherVisitActivity.this,msg);
            }
        }else{
            ToastUtil.showErrorData(TogetherVisitActivity.this);
        }
    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        closeLoadingDialog();
        setViewVisibility(0);
        ToastUtil.showErrorNet(TogetherVisitActivity.this);
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

            Intent intent = new Intent(TogetherVisitActivity.this,SelectVisitPeople.class);
            intent.putExtra("mediea",jsonMediea);
            intent.putExtra("medieaName",listMedieaName);
            intent.putExtra("doctorid",data.dataList.doctorId);
            intent.putExtra("doctorname",data.dataList.doctorName);
            intent.putExtra("latitude",data.dataList.loc.lat);
            intent.putExtra("longitude",data.dataList.loc.lng);
            intent.putExtra("city",city);
            intent.putExtra("address",data.dataList.addressName);
            intent.putExtra("addressName",data.dataList.address);
            intent.putExtra("jsonPeople",jsonPeople);
            intent.putExtra("initatorId",data.dataList.initatorId);
            intent.putExtra("initatorName",data.dataList.initatorName);
            intent.putExtra("headPic",data.dataList.headPic);
            intent.putExtra("id",data.dataList.id);
            intent.putExtra("time",data.dataList.remainTime);
            intent.putExtra("mode",SelectVisitPeople.MODE_JOIN);
            intent.putExtra("from",mMode);
            startActivity(intent);
        }
    }

    private void setViewVisibility(int flag){
        if(flag == 1){//1代表有协同拜访的数据
            vAddress.setVisibility(View.GONE);
            mTvTips.setVisibility(View.VISIBLE);
            vBottom.setVisibility(View.VISIBLE);
        }else{
            vAddress.setVisibility(View.VISIBLE);
            mTvTips.setVisibility(View.GONE);
            vBottom.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mGaoDeMapUtils){
            mGaoDeMapUtils.onDestory();
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        getVisitGroup();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

    }

    class MyAdapter extends VisitGroupAdapter{

        public MyAdapter(Context context, List<VisitGroupEntity.Data.VistGroupItem> list) {
            super(context, list);
        }

        @Override
        public void onClickAdd(VisitGroupEntity.Data.VistGroupItem vistGroupItem) {
            if(null != vistGroupItem){
                showLoadingDialog();
                new HttpManager().get(TogetherVisitActivity.this, Constants.JOIN_VISIT_GROUP, JoinVisitGroup.class,
                        Params.joinVisitGroup(TogetherVisitActivity.this,vistGroupItem.id),
                        TogetherVisitActivity.this,false,4);
            }
        }
    }
}
