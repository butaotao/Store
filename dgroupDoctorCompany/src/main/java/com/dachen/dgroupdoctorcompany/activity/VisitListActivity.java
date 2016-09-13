package com.dachen.dgroupdoctorcompany.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.VisitListAdapter;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.entity.JoinVisitGroup;
import com.dachen.dgroupdoctorcompany.entity.SignInList;
import com.dachen.dgroupdoctorcompany.utils.DataUtils.GetUserDepId;
import com.dachen.dgroupdoctorcompany.utils.GaoDeMapUtils;
import com.dachen.dgroupdoctorcompany.views.CustomDialog;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by weiwei on 2016/5/17.
 */
public class VisitListActivity extends BaseActivity implements HttpManager.OnHttpListener, PullToRefreshBase.OnRefreshListener2<ListView>,GaoDeMapUtils.LocationListener {
    private Button mBtAdd;
    private Button mBtTogtherAdd;
    private PullToRefreshListView mLvVisitList;
    private List<SignInList.Data.DataList.ListVisitVo> mDataLists = new ArrayList<>();
    private VisitListAdapter mVisitListAdapter;

    private int pageIndex = 0;
    private int pageSize = 20;

    private GaoDeMapUtils mGaoDeMapUtils;
    private double latitude;//纬度
    private double longitude;//经度
    private String city;//城市
    private String mStrFloor;
    private String mStrAddressName;
    private TextView mStatistics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_list);
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        mBtAdd = (Button) findViewById(R.id.btAdd);
        mBtAdd.setOnClickListener(this);
        mStatistics = (TextView) findViewById(R.id.tv_title_save);
        if(isDeptartManager()){
            mStatistics.setText("统计");
            mStatistics.setVisibility(View.VISIBLE);
            mStatistics.setTextColor(Color.parseColor("#333333"));
            mStatistics.setOnClickListener(this);
        }else {
            mStatistics.setVisibility(View.GONE);
        }
        mLvVisitList = (PullToRefreshListView) findViewById(R.id.lvVisitList);
        mBtTogtherAdd = (Button) findViewById(R.id.btTogtherAdd);
        mBtTogtherAdd.setOnClickListener(this);
        mVisitListAdapter = new MyAdapter(VisitListActivity.this);
        mLvVisitList.setAdapter(mVisitListAdapter);
    }

    private void initData() {
        setTitle("客户拜访");
        mLvVisitList.setMode(PullToRefreshBase.Mode.BOTH);
        mLvVisitList.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
        mLvVisitList.setOnRefreshListener(this);
        mLvVisitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SignInList.Data.DataList.ListVisitVo listVisitVo = (SignInList.Data.DataList.ListVisitVo) parent.getAdapter().getItem(position);
                Intent intent = null;
                if("0".equals(listVisitVo.type)){
                    intent = new Intent(VisitListActivity.this, SelfVisitActivity.class);
                    intent.putExtra("mode",SelfVisitActivity.MODE_FROM_VIST_LIST_ITEM);
                }else if("2".equals(listVisitVo.type)){
                    intent = new Intent(VisitListActivity.this, JointVisitActivity.class);
                    intent.putExtra("mode",JointVisitActivity.MODE_FROM_VIST_LIST_ITEM);
                }
                String mid = listVisitVo.id;
                long time = listVisitVo.time;
                String name = listVisitVo.doctorName;
                String address = listVisitVo.address;
                String addressName = listVisitVo.addressName;
                String doctorname = listVisitVo.doctorName;
                String remark = listVisitVo.remark;
                String coordinate = listVisitVo.coordinate;
                String doctorid = listVisitVo.doctorId;
                String state = listVisitVo.state;
                intent.putExtra("id", mid);
                intent.putExtra("name", name);
                intent.putExtra("address", address);
                intent.putExtra("addressname", addressName);
                intent.putExtra("doctorName", doctorname);
                intent.putExtra("doctorId", doctorid);
                intent.putExtra("remark", remark);
                intent.putExtra("coordinate", coordinate);
                if(coordinate.contains(",")){
                    String[] array = coordinate.split(",");
                    String latitude = array[0];
                    String longitude = array[1];
                    intent.putExtra("latitude", Double.valueOf(latitude));
                    intent.putExtra("longitude", Double.valueOf(longitude));
                }
                intent.putExtra("time", time);
                intent.putExtra("doctorid", doctorid);
                startActivity(intent);
            }
        });
        mGaoDeMapUtils = new GaoDeMapUtils(getApplicationContext(),this);
        mGaoDeMapUtils.startLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pageIndex = 0;
        mDataLists.clear();
        getVisiListData();
        mBtTogtherAdd.setClickable(true);
    }

    private void getVisiListData() {
        String type = "0";//0代表拜访列表
        showLoadingDialog();
        new HttpManager().get(this, Constants.GET_VISIT_LIST, SignInList.class,
                Params.getList(VisitListActivity.this, type, null, pageIndex, pageSize),
                this, false, 4);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btAdd:
                Intent intent = new Intent(VisitListActivity.this, SelfVisitActivity.class);//SelfVisitActivity
                intent.putExtra("mode",SelfVisitActivity.MODE_FROM_VIST_LIST);
                startActivity(intent);
                break;
            case R.id.tv_title_save://拜访统计
                intent = new Intent(this,VisitRecordActivity.class);
                intent.putExtra("type","visit");
                startActivity(intent);
                break;
            case R.id.btTogtherAdd:
                if(latitude == 0){//说明还没定位成功，不做任何操作 此时
                    ToastUtil.showToast(this,"正在加载数据，请稍后重试");
                    return;
                }
                mBtTogtherAdd.setClickable(false);
                startVisitGroup();
//                Intent intent1 = new Intent(VisitListActivity.this, TogetherVisitActivity.class);
//                startActivity(intent1);
                break;
        }
    }

    private void startVisitGroup(){
        showLoadingDialog();
        String orginId = GetUserDepId.getUserDepId(this);
        new HttpManager().post(this, Constants.CREATE_AND_JOIN_VISIT_GROUP, JoinVisitGroup.class,
                Params.startVisitGroup(VisitListActivity.this,orginId,String.valueOf(latitude),String.valueOf(longitude),mStrAddressName,mStrFloor),
                this, false, 4);
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

            Intent intent = new Intent(VisitListActivity.this,SelectVisitPeople.class);
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
            intent.putExtra("from",TogetherVisitActivity.MODE_FROM_VIST_LIST);
            startActivity(intent);
        }
    }

    @Override
    public void onSuccess(Result response) {
        closeLoadingDialog();
        mLvVisitList.onRefreshComplete();
        if (null != response && response.getResultCode() == 1) {
            if (response instanceof SignInList) {
                SignInList signInList = (SignInList) response;
                SignInList.Data data = signInList.data;
                if (null != data && null != data.dataList) {

                    int beforeSize = mDataLists.size();
                    for (int i = 0; i < data.dataList.size(); i++) {
                        SignInList.Data.DataList dataList = data.dataList.get(i);
                        mDataLists.addAll(dataList.listVisitVo);

                    }
                    if(pageIndex==0){
                        mVisitListAdapter.addData(mDataLists,true);

                    }else{
                        mVisitListAdapter.addData(mDataLists,false);
                    }

////                    mVisitListAdapter = new MyAdapter(VisitListActivity.this, mDataLists);
//                    mLvVisitList.setAdapter(mVisitListAdapter);
                    mVisitListAdapter.notifyDataSetChanged();
                    if(beforeSize>0){
                        int afterSize = mDataLists.size();
                        if(beforeSize == afterSize){
                            ToastUtil.showToast(this,"已经全部加载");
                        }
                    }

                    if(mVisitListAdapter.getmDataLists() == null || mVisitListAdapter.getmDataLists().size() == 0){
                        View view = LayoutInflater.from(this).inflate(R.layout.layout_empty, null);
                        TextView txtview = (TextView) view.findViewById(R.id.tv_empty);
//                        ImageView img_empty = (ImageView)view.findViewById(R.id.img_empty);
//                        img_empty.setVisibility(View.GONE);
                        txtview.setVisibility(View.VISIBLE);
                        txtview.setText("还没有拜访数据");
                        mLvVisitList.setEmptyView(view);
                    }
                }
            } else if (response instanceof JoinVisitGroup){
                JoinVisitGroup joinVisitGroup = (JoinVisitGroup) response;
                JoinVisitGroup.Data data = joinVisitGroup.data;
                if(null != data){
                    openVisitGroup(data);
                }else{
                    mBtTogtherAdd.setClickable(true);
                }
            }else {
                ToastUtil.showToast(VisitListActivity.this, "删除拜访成功");
                pageIndex = 0;
                mDataLists.clear();
                getVisiListData();
            }

        }else{
            mBtTogtherAdd.setClickable(true);
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
        mLvVisitList.onRefreshComplete();
        mBtTogtherAdd.setClickable(true);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        mDataLists.clear();
        pageIndex = 0;
        getVisiListData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        mDataLists.clear();
        pageIndex++;
        getVisiListData();
    }

    @Override
    public void onLocation(Object object) {
        if(null != object){
            Map<String,Object> map = (Map<String, Object>) object;
            latitude = (double) map.get("latitude");
            longitude = (double) map.get("longitude");
            city = (String) map.get("city");
            mStrFloor = (String) map.get("floor");
            mStrAddressName = (String) map.get("address");
        }
    }

    class MyAdapter extends VisitListAdapter {

        public MyAdapter(Context context) {
            super(context);
        }

        @Override
        protected void onDeleteItem(final SignInList.Data.DataList.ListVisitVo item) {
            CustomDialog customDialog = new CustomDialog.Builder(VisitListActivity.this, new
                    CustomDialog.CustomClickEvent() {
                        @Override
                        public void onClick(CustomDialog customDialog) {
                            showLoadingDialog();
                            new HttpManager().get(VisitListActivity.this, Constants.DELETE_VISIT, Result.class,
                                    Params.deletVisit(VisitListActivity.this, item.id),
                                    VisitListActivity.this, false, 4);
                            customDialog.dismiss();
                        }

                        @Override
                        public void onDismiss(CustomDialog customDialog) {
                            customDialog.dismiss();
                        }
                    }).setMessage("确定删除这条拜访记录吗?").setNegative("取消").setPositive("确定").create();
            customDialog.show();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mGaoDeMapUtils){
            mGaoDeMapUtils.onDestory();
        }
    }

    /**
     * 判断是否是管理员
     * @return
     */
    private boolean isDeptartManager() {
        CompanyContactDao dao = new CompanyContactDao(getApplicationContext());
        CompanyContactListEntity entity = dao.queryByUserid(SharedPreferenceUtil.getString(this, "id", ""));
        return entity.deptManager == 1;
    }
}
