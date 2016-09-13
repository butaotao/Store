package com.dachen.dgroupdoctorcompany.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.ChoiceMediaAdapter;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.AddVisitGroup;
import com.dachen.dgroupdoctorcompany.entity.GoodsGroupEntity;
import com.dachen.dgroupdoctorcompany.utils.DataUtils.GetUserDepId;
import com.dachen.dgroupdoctorcompany.utils.JsonMananger;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by weiwei on 2016/6/27.
 */
public class ChoiceMedieaActivity extends BaseActivity implements HttpManager.OnHttpListener,PullToRefreshBase.OnRefreshListener2<ListView>{
    public static final int        REQUEST_SELECT_ADDRESS = 102;
    public static final int        MODE_SINGLE_VISIT = 1;//独立拜访
    public static final int        MODE_MULTI_VISIT = 2;//协同拜访

    private LinearLayout vInfo;
    private LinearLayout vAddress;
    private TextView mTvAddress;
    private TextView mTvName;
    private TextView mTvSure;
    private PullToRefreshListView mLvMedia;
    private LinearLayout empty_view;
    private TextView tvCancel;

    private String mStrAddress;
    private double latitude;//纬度
    private double longitude;//经度
    private String city;//城市
    private String name;
    private String docterId;
    private int mMode;
    private String mStrAddressName;
    private String initatorId;
    private String initatorName;
    private String headPic;
    private String id;

    List<GoodsGroupEntity.Data.PageData> list;
    private ChoiceMediaAdapter mChoiceMediaAdapter;
    List<HashMap<String,String>> listMediea;
    String listMedieaName="";
    int time;
    int from;
    int pageSize = 20;
    int pageIndex = 0;
    List<BasicMeida> listSelectMediea = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_media);
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("选择品种");
        vInfo = (LinearLayout) findViewById(R.id.vInfo);
        vAddress = (LinearLayout) findViewById(R.id.vAddress);
        mTvAddress = (TextView) findViewById(R.id.tvAddress);
        mTvName = (TextView) findViewById(R.id.tvName);
        mTvSure = (TextView) findViewById(R.id.tvSure);
        mLvMedia = (PullToRefreshListView) findViewById(R.id.lvMedia);
        empty_view = (LinearLayout) findViewById(R.id.empty_view);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        empty_view.setVisibility(View.GONE);

        vAddress.setOnClickListener(this);
        mTvSure.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    private void initData(){
        mMode = this.getIntent().getIntExtra("mode",MODE_SINGLE_VISIT);
        from = this.getIntent().getIntExtra("from",TogetherVisitActivity.MODE_FROM_VIST_LIST);
        pageIndex = 0;
        pageSize = 20;
        if(MODE_SINGLE_VISIT == mMode){
            vInfo.setVisibility(View.GONE);
        }else if(MODE_MULTI_VISIT == mMode){
            vInfo.setVisibility(View.VISIBLE);
            mLvMedia.setEmptyView(empty_view);
            mStrAddress = this.getIntent().getStringExtra("address");
            latitude = this.getIntent().getDoubleExtra("latitude",0);
            longitude = this.getIntent().getDoubleExtra("longitude",0);
            city = this.getIntent().getStringExtra("city");
            name = this.getIntent().getStringExtra("doctorname");
            docterId = this.getIntent().getStringExtra("doctorid");
            mStrAddressName = this.getIntent().getStringExtra("addressName");
            if(!TextUtils.isEmpty(name)){
                mTvName.setText(name);
            }else{
                mTvName.setText("不记名拜访");
            }

            mTvAddress.setText(mStrAddress);
        }

        String strMediea = this.getIntent().getStringExtra("mediea");
        if(!TextUtils.isEmpty(strMediea)){
            listSelectMediea = JsonMananger.jsonToList(strMediea,BasicMeida.class);
        }

        listMediea = new ArrayList<>();
        list = new ArrayList<>();
        mChoiceMediaAdapter = new MyAdapter(ChoiceMedieaActivity.this,list);
        mLvMedia.setAdapter(mChoiceMediaAdapter);
        mLvMedia.setMode(PullToRefreshBase.Mode.BOTH);
        mLvMedia.setOnRefreshListener(this);
        mLvMedia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoodsGroupEntity.Data.PageData medie = (GoodsGroupEntity.Data.PageData) parent.getAdapter().getItem(position);
                if(null != medie){
                    boolean check = !medie.select;
                    for(int i=0; i<list.size();i++){
                        GoodsGroupEntity.Data.PageData item = list.get(i);
                        if((item.id).equals(medie.id)){
                            item.select = check;
                        }
                    }

                    mChoiceMediaAdapter.notifyData(list);
                }
            }
        });
        getMeida();
    }

    private void getMeida(){
        showLoadingDialog();
        new HttpManager().post(this, Constants.GET_GOODSGROUP_LIST , GoodsGroupEntity.class,
                Params.getGoodsGroupList(ChoiceMedieaActivity.this,null,pageSize,pageIndex), this,
                false, 1);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()){
            case R.id.vAddress:
                intent = new Intent(ChoiceMedieaActivity.this,SelectAddressActivity.class);
                intent.putExtra("select_mode",SelectAddressActivity.MODE_SELECT_ADDRESS);
                intent.putExtra("poi",SelectAddressActivity.POI);
                intent.putExtra("distance",250);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                intent.putExtra("city",city);
                startActivityForResult(intent,REQUEST_SELECT_ADDRESS);
                break;
            case R.id.tvSure:
                getMedieaSelect();
                Gson g = new Gson();
                String jsonMediea = g.toJson(listMediea);
                if(MODE_SINGLE_VISIT == mMode){
                    intent = new Intent();
                    intent.putExtra("mediea",jsonMediea);
                    intent.putExtra("medieaName",listMedieaName);
                    setResult(RESULT_OK,intent);
                    finish();
                }else if(MODE_MULTI_VISIT == mMode){
                    createVisitGroup();
//                    openVisitGroup();
                }
                break;
            case R.id.tvCancel:
                getMedieaSelect();
                createVisitGroup();
                break;
        }
    }

    private void openVisitGroup(){
        getMedieaSelect();
        Gson g = new Gson();
        String jsonMediea = g.toJson(listMediea);

        Intent intent = new Intent(ChoiceMedieaActivity.this,SelectVisitPeople.class);
        intent.putExtra("mediea",jsonMediea);
        intent.putExtra("medieaName",listMedieaName);
        intent.putExtra("doctorid",docterId);
        intent.putExtra("doctorname",name);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
        intent.putExtra("city",city);
        intent.putExtra("address",mStrAddress);
        intent.putExtra("addressName",mStrAddressName);
        intent.putExtra("initatorId",initatorId);
        intent.putExtra("initatorName",initatorName);
        intent.putExtra("headPic",headPic);
        intent.putExtra("id",id);
        intent.putExtra("time",time);
        intent.putExtra("mode",SelectVisitPeople.MODE_ADD);
        intent.putExtra("from",from);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_SELECT_ADDRESS && resultCode == RESULT_OK && null != data) {
            mStrAddress = data.getStringExtra("floor");
            latitude = data.getDoubleExtra("latitude", 0);
            longitude = data.getDoubleExtra("longitude", 0);
            mTvAddress.setText(mStrAddress);
            mStrAddressName = data.getStringExtra("address");
        }
    }

    private void createVisitGroup(){
        String orginId = GetUserDepId.getUserDepId(this);
        String userName = SharedPreferenceUtil.getString(this,"username","");
        if(latitude==0 || longitude==0){
            ToastUtil.showToast(this,"定位失败，请重新选择地址");
            return;
        }
        Gson g = new Gson();
        String json = g.toJson(listMediea);
        showLoadingDialog();
        new HttpManager().post(this, Constants.ADD_VISIT_GROUP, AddVisitGroup.class,
                Params.addVisitGroup(ChoiceMedieaActivity.this,orginId,userName,String.valueOf(latitude),String.valueOf(longitude),
                        docterId,name,mStrAddressName,mStrAddress,json),
                this,false,4);
    }

    @Override
    public void onSuccess(Result response) {
        mLvMedia.onRefreshComplete();
        closeLoadingDialog();
        if(null != response){
            if(response.getResultCode() == 1){
                if(response instanceof GoodsGroupEntity){
                    GoodsGroupEntity goodsGroupEntity = (GoodsGroupEntity) response;
                    if(null != goodsGroupEntity.data && null != goodsGroupEntity.data.pageData && goodsGroupEntity.data.pageData.size()>0){
                        list.addAll(goodsGroupEntity.data.pageData);
                        if(null != listSelectMediea && listSelectMediea.size()>0){
                            for(int i=0;i<listSelectMediea.size();i++){
                                BasicMeida basicMeida = listSelectMediea.get(i);
                                for(int j=0;j<list.size();j++){
                                    GoodsGroupEntity.Data.PageData medie = list.get(j);
                                    if((basicMeida.id).equals(medie.id)){
                                        medie.select = true;
                                    }
                                }
                            }
                        }
                        mChoiceMediaAdapter.notifyData(list);
                    }
                }else if(response instanceof AddVisitGroup){
                    AddVisitGroup addVisitGroup = (AddVisitGroup) response;
                    if(null != addVisitGroup.data && null != addVisitGroup.data.dataList){
                        initatorId = addVisitGroup.data.dataList.initatorId;
                        initatorName = addVisitGroup.data.dataList.initatorName;
                        headPic = addVisitGroup.data.dataList.headPic;
                        id = addVisitGroup.data.dataList.id;
                        time = addVisitGroup.data.dataList.remainTime;
                        openVisitGroup();
                    }
                }
            }else{
                String msg = response.getResultMsg();
                ToastUtil.showToast(ChoiceMedieaActivity.this,msg);
            }
        }else{
            ToastUtil.showErrorData(ChoiceMedieaActivity.this);
        }
    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        mLvMedia.onRefreshComplete();
        closeLoadingDialog();
        ToastUtil.showErrorNet(ChoiceMedieaActivity.this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        list.clear();
        pageIndex = 0;
        getMeida();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        pageIndex++;
        getMeida();
    }

    class MyAdapter extends ChoiceMediaAdapter{

        public MyAdapter(Context context, List<GoodsGroupEntity.Data.PageData> list) {
            super(context, list);
        }

        @Override
        protected void onCheckBoxCheck(GoodsGroupEntity.Data.PageData medie) {
            boolean check = !medie.select;
            for(int i=0; i<list.size();i++){
                GoodsGroupEntity.Data.PageData item = list.get(i);
                if((item.id).equals(medie.id)){
                    item.select = check;
                }
            }

            mChoiceMediaAdapter.notifyData(list);
        }
    }

    private void getMedieaSelect(){
        listMediea.clear();
        listMedieaName = "";

        for(int i=0;i<list.size();i++){
            GoodsGroupEntity.Data.PageData item = list.get(i);
            if(item.select){
                HashMap<String,String> mapMediea = new HashMap<>();
                String id = item.id;
                String name = "";
                if(!TextUtils.isEmpty(item.tradeName)){
                    name = item.tradeName;
                }else{
                    name = item.generalName;
                }
                mapMediea.put("id",id);
                mapMediea.put("name",name);
                if(TextUtils.isEmpty(listMedieaName)){
                    listMedieaName = name;
                }else{
                    listMedieaName = listMedieaName+","+name;
                }
                listMediea.add(mapMediea);
            }
        }
    }
}
