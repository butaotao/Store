package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.SingnInListsAdapter;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.SignInList;
import com.dachen.dgroupdoctorcompany.entity.SignInLists;
import com.dachen.dgroupdoctorcompany.utils.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;
import com.handmark.pulltorefresh.library.PinnedHeaderExpandableListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshPinHeaderExpandableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiwei on 2016/4/5.
 */
public class SignListActivity extends BaseActivity implements HttpManager.OnHttpListener<Result>,PullToRefreshBase.OnRefreshListener2<PinnedHeaderExpandableListView>, ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener {
    private static final int                    REQUEST_UPDATE_SIGN_IN = 101;
    private ExpandableListView                  mLvSign;
    private PullToRefreshPinHeaderExpandableListView mVSignin;
    private TextView                            mTvAll;
    private TextView                            mTvWorking;
    private TextView                            mTvVisit;
    private String                              type="";
    private LinearLayout                        mVType;
    private ImageView                           ivLeft;
    private ImageView                           ivMid;
    private ImageView                           ivRight;
    private TextView                            tvMore;

    private List<SignInLists.DataBean.PageDataBean>      mDataLists = new ArrayList<>();
    private SingnInListsAdapter                 mAdapter;
    private int                                 pageIndex = 0;
    private int                                 pageSize = 20;
    private String coordinate;
    private String address;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_list);
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        mVSignin = (PullToRefreshPinHeaderExpandableListView) findViewById(R.id.vSignin);
        mTvAll = (TextView) findViewById(R.id.tvAll);
        mTvWorking = (TextView) findViewById(R.id.tvWorking);
        mTvVisit = (TextView) findViewById(R.id.tvVisit);
        mVType = (LinearLayout) findViewById(R.id.vType);
        ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivMid = (ImageView) findViewById(R.id.ivMid);
        ivRight = (ImageView) findViewById(R.id.ivRight);
        tvMore = (TextView) findViewById(R.id.tvMore);
        ivLeft.setVisibility(View.VISIBLE);
        ivMid.setVisibility(View.GONE);
        ivRight.setVisibility(View.GONE);
        mTvAll.setOnClickListener(this);
        mTvWorking.setOnClickListener(this);
        mTvVisit.setOnClickListener(this);
        mVSignin.setMode(PullToRefreshBase.Mode.BOTH);
        mVSignin.setFocusable(false);
        mVSignin.getRefreshableView().setOnChildClickListener(this);
        mVSignin.getRefreshableView().setOnGroupClickListener(this);
        mVSignin.getRefreshableView().setOnGroupClickListener(this, false);
        mVSignin.setDescendantFocusability(ExpandableListView.FOCUS_AFTER_DESCENDANTS);
        tvMore.setOnClickListener(this);
        tvMore.setVisibility(View.GONE);
    }

    private void initData(){
        setTitle("签到记录");
        mAdapter = new SingnInListsAdapter(SignListActivity.this);
        mLvSign = mVSignin.getRefreshableView();
        mLvSign.setAdapter(mAdapter);
        mLvSign.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SignListActivity.this,MapDetailActivity.class);
                if(!TextUtils.isEmpty(coordinate)&&coordinate.contains(",")){
                    String[] array = coordinate.split(",");
                    String latitude = array[0];
                    String longitude = array[1];
                    intent.putExtra("latitude", Double.valueOf(latitude));
                    intent.putExtra("longitude", Double.valueOf(longitude));
                } if (!TextUtils.isEmpty(address)){
                    intent.putExtra("address",address);
                }
                startActivity(intent);
            }
        });
        mVSignin.setOnRefreshListener(this);
        mVSignin.setEmptyView(findViewById(R.id.empty_container));
        if(UserInfo.getInstance(SignListActivity.this).isMediePresent()){
            mVType.setVisibility(View.VISIBLE);
        }else{
            mVType.setVisibility(View.GONE);
        }
        getListData();
    }

    private void getListData(){
        new HttpManager().post(this, Constants.GET_MYSIGNEDPAGE, SignInLists.class,
                Params .getList(SignListActivity.this,type,pageIndex,pageSize),
                this,false,4);
//        String url = "http://192.168.3.7:8082/visit/getList";
//        new HttpManager().requestBase(Request.Method.GET,url,this,SignInList.class, Params
//                .getList(SignListActivity.this,type,pageIndex,pageSize),this,false,1);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tvAll:
                mTvAll.setTextColor(getResources().getColor(R.color.color_3cbaff));
                mTvWorking.setTextColor(getResources().getColor(R.color.color_666666));
                mTvVisit.setTextColor(getResources().getColor(R.color.color_666666));
                ivLeft.setVisibility(View.VISIBLE);
                ivMid.setVisibility(View.GONE);
                ivRight.setVisibility(View.GONE);
                mDataLists.clear();
                type = "";
                pageIndex = 0;
                getListData();
                break;
            case R.id.tvWorking:
                mTvAll.setTextColor(getResources().getColor(R.color.color_666666));
                mTvWorking.setTextColor(getResources().getColor(R.color.color_3cbaff));
                mTvVisit.setTextColor(getResources().getColor(R.color.color_666666));
                ivLeft.setVisibility(View.GONE);
                ivMid.setVisibility(View.VISIBLE);
                ivRight.setVisibility(View.GONE);
                mDataLists.clear();
                type = "2";
                pageIndex = 0;
                getListData();
                break;
            case R.id.tvVisit:
                mTvAll.setTextColor(getResources().getColor(R.color.color_666666));
                mTvWorking.setTextColor(getResources().getColor(R.color.color_666666));
                mTvVisit.setTextColor(getResources().getColor(R.color.color_3cbaff));
                ivLeft.setVisibility(View.GONE);
                ivMid.setVisibility(View.GONE);
                ivRight.setVisibility(View.VISIBLE);
                mDataLists.clear();
                type = "1";
                pageIndex = 0;
                getListData();
                break;
            case R.id.tvMore:
                Intent intent = new Intent(this,SignSettingActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onSuccess(Result response) {
        Log.d("zxy :", "178 : SignListActivity : onSuccess : response");
        mVSignin.onRefreshComplete();
        if(null!=response){
            SignInLists signInList = (SignInLists) response;
            SignInLists.DataBean data = signInList.data;
            Log.d("zxy :", "184 : SignListActivity : onSuccess : data"+data);
            if(null != data ){
                int beforeSize = mDataLists.size();
                Log.d("zxy :", "186 : SignListActivity : onSuccess : data"+mDataLists.size());
                if(null != data.pageData && data.pageData.size()>0){
                    Log.d("zxy :", "189 : SignListActivity : onSuccess : data.dataList"+data.pageData);
                    Log.d("zxy :", "190 : SignListActivity : onSuccess : data.dataList"+data.pageData.get(0));
                    mDataLists.addAll(data.pageData);
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
                if(beforeSize>0){
                    int afterSize = mDataLists.size();
                    if(beforeSize == afterSize){
                        ToastUtil.showToast(this,"已经全部加载");
                    }
                }
            //    setExpandableListView();
            }else{
//                if(pageIndex == 0){
//                    findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
//                }
            }

        }else{
            ToastUtil.showErrorNet(SignListActivity.this);
        }
    }

    @Override
    public void onSuccess(ArrayList<Result> response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        ToastUtil.showErrorNet(SignListActivity.this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<PinnedHeaderExpandableListView> refreshView) {
        pageIndex = 0;
        mDataLists.clear();
        getListData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<PinnedHeaderExpandableListView> refreshView) {
        pageIndex++;
        mDataLists.clear();
        getListData();
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int
            childPosition, long id) {
        SignInList.Data.DataList.ListVisitVo  listVisitVo = (SignInList.Data.DataList
                .ListVisitVo) parent.getExpandableListAdapter().getChild(groupPosition,childPosition);

        String type = listVisitVo.type;
        if("1".equals(type)){

            String strId = listVisitVo.id;
            String address = listVisitVo.address;
            address = listVisitVo.address;
            long time = listVisitVo.time;
            coordinate = listVisitVo.coordinate;
            String remark = listVisitVo.remark;
            List<String> lable = listVisitVo.singedTag;
            Intent intent = new Intent(SignListActivity.this,AddSignInActivity.class);
            intent.putExtra("id",strId);
            intent.putExtra("address",address);
            intent.putExtra("time",time);
            intent.putExtra("coordinate",coordinate);
            intent.putExtra("remark",remark);
            if(null != lable && lable.size()>0){
                intent.putStringArrayListExtra("lable", (ArrayList<String>) lable);
            }
            intent.putExtra("mode",AddSignInActivity.MODE_WORKING);
            startActivityForResult(intent,REQUEST_UPDATE_SIGN_IN);
        }else {
            Intent intent = null;
            if("0".equals(listVisitVo.type)){
                intent = new Intent(SignListActivity.this, SelfVisitActivity.class);
                intent.putExtra("mode",SelfVisitActivity.MODE_FROM_SIGN_LIST);
            }else if("2".equals(listVisitVo.type)){
                intent = new Intent(SignListActivity.this, JointVisitActivity.class);
                intent.putExtra("mode",JointVisitActivity.MODE_FROM_SIGN_LIST);
            }
            String mid = listVisitVo.id;
            long time = listVisitVo.time;
            String name = listVisitVo.doctorName;
            String address = listVisitVo.address;
            address = listVisitVo.address;
            String addressName = listVisitVo.addressName;
            String doctorname = listVisitVo.doctorName;
            String remark = listVisitVo.remark;
            coordinate = listVisitVo.coordinate;
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

        return false;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return true;
    }
/*
    private void setExpandableListView(){
        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
            mLvSign.expandGroup(i);
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_UPDATE_SIGN_IN && resultCode == RESULT_OK){
            pageIndex = 0;
            mDataLists.clear();
            mAdapter.notifyDataSetChanged();
            getListData();
        }
    }
}
