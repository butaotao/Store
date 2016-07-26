package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.SelfPartHospitalChoiceMedieAdapter;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.AddSelfPartHospital;
import com.dachen.dgroupdoctorcompany.entity.MedieEntity;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Burt on 2016/5/19.
 */
public class SelfPartHospitalChoiceMedieActivity extends BaseActivity implements HttpManager.OnHttpListener{
    ListView listview;
    SelfPartHospitalChoiceMedieAdapter adapter;
    ArrayList<MedieEntity.Medie> data;
    MedieEntity.Medie medie = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfparthospitalchoicemedie);
        listview = (ListView) findViewById(R.id.listview);
        findViewById(R.id.btn_sure).setOnClickListener(this);
        RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.ll_sub);
        ViewStub stub = (ViewStub) findViewById(R.id.vstub_title);
        View view = stub.inflate(this,  R.layout.stub_viewtext, rl);
        TextView tv = (TextView) view.findViewById(R.id.tv_search);
        tv.setOnClickListener(this);
        tv.setText("确定");
        setTitle("选择品种");
        data = new ArrayList<>();
        Collections.sort(data, new PinyinComparator());
        adapter = new SelfPartHospitalChoiceMedieAdapter(this,data);
        listview.setAdapter(adapter);
        listview.setEmptyView(findViewById(R.id.rl_notcontent));
        showLoadingDialog();
        getUnssinglist();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_sure:
                for (int i=0;i<data.size();i++){
                    if (data.get(i).select){
                        ToastUtils.showToast(this,"i=="+i);
                    }
                }
                Intent intent = new Intent(this,AddSelfPartHospitalActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_search:

                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).select) {
                        medie = data.get(i);
                        break;
                    }
                }
                if (medie != null) {
                    add(medie.id);
                } else {
                    ToastUtils.showToast(this,"请选择一个品种");
                }

               /*  intent = new Intent(this,AddSelfPartHospitalActivity.class);
                intent.putExtra("fromactivity","SelfPartHospitalChoiceMedieActivity");
                intent.putExtra("selectdata",medie);
                startActivity(intent);
                finish();*/
                break;
        }
    }

    @Override
    public void onSuccess(Result response) {
        closeLoadingDialog();
        if (response instanceof MedieEntity){
            MedieEntity entity = (MedieEntity)response;
            if (null!=entity.data){
                data.clear();
                data.addAll(entity.data);
                adapter.notifyDataSetChanged();
            }
        }else if(response instanceof AddSelfPartHospital){
            AddSelfPartHospital add = (AddSelfPartHospital) response;
            if (add.data){
                ToastUtil.showToast(this,"添加成功");
                Intent intent = new Intent(this,AddSelfPartHospitalActivity.class);
                intent.putExtra("fromactivity","SelfPartHospitalChoiceMedieActivity");
                intent.putExtra("selectdata",medie);
                startActivity(intent);
                finish();
            }else {
                ToastUtil.showToast(this,"添加失败");
            }
        }
    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        closeLoadingDialog();
    }

    /**
     * [A brief description]
     *
     * @author huxinwu
     * @version 1.0
     * @date 2015-8-14
     **/
    class PinyinComparator implements Comparator<MedieEntity.Medie> {
        public int compare(MedieEntity.Medie o1, MedieEntity.Medie o2) {

                return o1.drugName.compareTo(o2.drugName);
        }
    }//
    public void getUnssinglist(){
        HashMap<String, String> interfaces = new HashMap<String, String>();
        interfaces.put("access_token", UserInfo.getInstance(this).getSesstion());
        interfaces.put("userId", UserInfo.getInstance(this).getId());
        interfaces.put("companyId", SharedPreferenceUtil.getString(this,"enterpriseId",""));
        new HttpManager().post(this,
                Constants.GETUNSIGNGOODSLIST,
                MedieEntity.class,
                interfaces,
                this, false, 1);

    }
    public void add(String id){
        showLoadingDialog();
        HashMap<String, String> interfaces = new HashMap<String, String>();
        interfaces.put("access_token", UserInfo.getInstance(this).getSesstion());
        interfaces.put("userId", SharedPreferenceUtil.getString(this,"id",""));
        interfaces.put("goodsGroupId",id);
        new HttpManager().post(this,
                Constants.ADDGOODS,
                AddSelfPartHospital.class,
                interfaces,
                this, false, 1);

    }
}
