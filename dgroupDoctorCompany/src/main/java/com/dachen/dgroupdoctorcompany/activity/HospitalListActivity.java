package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.HospitalListAdapter;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.dachen.dgroupdoctorcompany.entity.HospitalList;
import com.dachen.medicine.common.utils.ListViewUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Burt on 2016/2/22.
 */
public class HospitalListActivity extends BaseActivity implements HttpManager.OnHttpListener{
    ListView listview;
    HospitalListAdapter adapter;
    List<BaseSearch> hospitals;
    ViewStub vstub_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        listview = (ListView) findViewById(R.id.listview);
        hospitals = new ArrayList<>();
        adapter = new HospitalListAdapter(this,R.layout.adapter_hospital_list,hospitals,"");
        listview.setAdapter(adapter);
        ListViewUtils.setListViewHeightBasedOnChildren(listview);
        ButterKnife.bind(this);
        setTitle("条件添加1");
        vstub_title = (ViewStub) findViewById(R.id.vstub_title);
        RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.ll_sub);
        View view = vstub_title.inflate(this,  R.layout.stub_viewtext, rl);
        TextView tv = (TextView) view.findViewById(R.id.tv_search);
        tv.setOnClickListener(this);
        tv.setText("重选条件");
        String cityid = getIntent().getStringExtra("citiyid");
        String province = getIntent().getStringExtra("province");
        if (!TextUtils.isEmpty(cityid)){
            getHospitalInfo("cityId",cityid);

        }else {
            getHospitalInfo("provinceId",province);
        }

    }
 public void getHospitalInfo(String cityOrProvince ,String id){

     HashMap<String ,String > maps = new HashMap<>();
     maps.put("access_token", UserInfo.getInstance(this).getSesstion());
     maps.put("type", 1+"");
     maps.put(cityOrProvince,id);
     maps.put("pageIndex","1");
     maps.put("pageSize","100");
     new HttpManager().post(this, Constants.DRUG+"saleFriend/searchHospitalList", HospitalList.class,
             maps, this,
             false, 1);
 }
    @Override
    public void onSuccess(Result response) {
        closeLoadingDialog();
        if (null!=response){
            if (response instanceof HospitalList){
                HospitalList hospital = (HospitalList)response;
                if(null!=hospital.data&&null!=hospital.data.pageData){
                hospitals.clear();;
                hospitals.addAll((Collection<? extends HospitalList.Data.HospitalDes>) hospital.data.pageData);
               // adapter.notifyDataSetChanged();
                adapter = new HospitalListAdapter(this,R.layout.adapter_hospital_list,hospitals,"");
                listview.setAdapter(adapter);
                }
            }
        }

    }
    @Override
      public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_search:
                Intent intent = new Intent(this,ProvinceActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        closeLoadingDialog();
    }
}
