package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.SingnInListsAdapter;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.SignInLists;
import com.dachen.dgroupdoctorcompany.utils.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiwei on 2016/4/5.
 */
public class SignListActivity extends BaseActivity implements HttpManager.OnHttpListener<Result>,AdapterView.OnItemClickListener {
    private static final int                    REQUEST_UPDATE_SIGN_IN = 101;
    //private ListView mLvSign;
    private PullToRefreshListView mVSignin;
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
    private List<SignInLists.DataBean.PageDataBean>      mVisitLists = new ArrayList<>();
    private List<SignInLists.DataBean.PageDataBean>      mSignists = new ArrayList<>();
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
        mVSignin = (PullToRefreshListView) findViewById(R.id.vSignin);
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
        mTvAll.setOnClickListener(this);//需求变更,暂时废弃
        mTvWorking.setOnClickListener(this);//需求变更,暂时废弃
        mTvVisit.setOnClickListener(this);//需求变更,暂时废弃
        mVSignin.setMode(PullToRefreshBase.Mode.BOTH);
        mVSignin.setFocusable(false);
        mVSignin.setOnItemClickListener(this);
        mVSignin.setDescendantFocusability(ListView.FOCUS_AFTER_DESCENDANTS);
        tvMore.setOnClickListener(this);
        tvMore.setVisibility(View.GONE);
    }

    private void initData(){
        setTitle("签到记录");
        mAdapter = new SingnInListsAdapter(SignListActivity.this);
        mVSignin.setAdapter(mAdapter);
        mVSignin.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 0;
                getListData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex++;
                getListData();
            }
        });
        mVSignin.setEmptyView(findViewById(R.id.empty_container));
        if(UserInfo.getInstance(SignListActivity.this).isMediePresent()){
           /// mVType.setVisibility(View.VISIBLE);
        }else{
            //mVType.setVisibility(View.GONE);
        }
        getListData();
    }

    private void getListData(){
        new HttpManager().post(this, Constants.GET_MYSIGNEDPAGE, SignInLists.class,
                Params .getList(SignListActivity.this,type,pageIndex,pageSize),
                this,false,4);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tvAll://需求变更,暂时废弃
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
            case R.id.tvWorking://需求变更,暂时废弃
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
            case R.id.tvVisit://需求变更,暂时废弃
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
        mVSignin.onRefreshComplete();
        if(null!=response){
            SignInLists signInList = (SignInLists) response;
            SignInLists.DataBean data = signInList.data;
            if(null != data ){
                int beforeSize = mDataLists.size();
                if(null != data.pageData && data.pageData.size()>0){
                    mDataLists.clear();
                    mDataLists.addAll(data.pageData);
                    if(pageIndex==0){
                        mAdapter.addData(mDataLists,true);
                    }else{
                        mAdapter.addData(mDataLists,false);
                    }
                    mAdapter.notifyDataSetChanged();
                }
                //分离数据
                if(beforeSize>0){
                    int afterSize = mDataLists.size();
                    if(beforeSize == afterSize){
                        ToastUtil.showToast(this,"已经全部加载");
                    }
                }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_UPDATE_SIGN_IN && resultCode == RESULT_OK){
            pageIndex = 0;
            mDataLists.clear();
            mAdapter.notifyDataSetChanged();
            getListData();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SignInLists.DataBean.PageDataBean dataBean  =
                (SignInLists.DataBean.PageDataBean) mAdapter.getItem(position-1);// position 从1开始???醉了!!!
        String type = dataBean.tag.get(0);
       // if("拜访".equals(type)||"协同拜访".equals(type)){
            Intent intent = null;
            /*if("拜访".equals(type)){
                intent = new Intent(SignListActivity.this, AddSignInActivity.class);
                intent.putExtra("mode",AddSignInActivity.MODE_FROM_SIGN_LIST);
            }else if("协同拜访".equals(type)){
                intent = new Intent(SignListActivity.this, JointVisitActivity.class);
                intent.putExtra("mode",JointVisitActivity.MODE_FROM_SIGN_LIST);
            }
            String mid = dataBean.signedId;
            long time = dataBean.longTime;
            String name = dataBean.userName;
            String address = dataBean.address;
            String addressName = dataBean.address;
            String doctorname = dataBean.userName;
            String remark = dataBean.remark;
            coordinate = dataBean.coordinate;
            String doctorid = dataBean.signedId;
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
           // intent.putExtra("mode",AddSignInActivity.MODE_VISIT);
            intent.putExtra("doctorid", doctorid);
            startActivity(intent);*/





             intent = new Intent(SignListActivity.this, SiginDetailActivity.class);
            intent.putExtra("day", "");
            intent.putExtra("hour","");
            intent.putExtra("remark",dataBean.remark);
            intent.putExtra("address", dataBean.address);
            intent.putExtra("longTime", dataBean.longTime);
            intent.putExtra("signedid",dataBean.signedId);
         /*    if(coordinate.contains(",")){
            String[] array = coordinate.split(",");
            String latitude = array[0];
            String longitude = array[1];
            intent.putExtra("latitude", Double.valueOf(latitude));
            intent.putExtra("longitude", Double.valueOf(longitude));
                }*/
            intent.putExtra("id",dataBean.visitId);
            if (null != dataBean.tag && dataBean.tag.size() > 0 && !TextUtils.isEmpty(dataBean.tag
                    .get(0))) {
                intent.putExtra("tag", dataBean.tag.get(0));
            } else {
                intent.putExtra("tag", "");
            }
            startActivity(intent);
       /* }else {
            String strId = dataBean.signedId;
            String address = dataBean.address;
            address = dataBean.address;
            long time = dataBean.longTime;
            coordinate = dataBean.coordinate;
            String remark = dataBean.remark;
            List<String> lable = dataBean.tag;
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
            Log.d("zxy :", "376 : SignListActivity : onItemClick : id = "+strId+", "+address+", "+time+", "+coordinate+", "+remark);
            startActivityForResult(intent,REQUEST_UPDATE_SIGN_IN);

        }*/

    }
}
