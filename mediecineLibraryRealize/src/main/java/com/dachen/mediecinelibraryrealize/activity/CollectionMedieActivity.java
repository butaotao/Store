package com.dachen.mediecinelibraryrealize.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.adapter.AdapterPatientCollectMedieList;
import com.dachen.mediecinelibraryrealize.adapter.AdapterPatientMedieBox;
import com.dachen.mediecinelibraryrealize.entity.PatientMedieBoxs;
import com.dachen.mediecinelibraryrealize.entity.Patients;
import com.dachen.mediecinelibraryrealize.view.DialogChoicePatientGallery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Burt on 2016/1/12.
 */
public class CollectionMedieActivity extends BaseActivity implements HttpManager.OnHttpListener, View.OnClickListener {
    ListView listView;
    AdapterPatientCollectMedieList adapter;
     List<PatientMedieBoxs.Info> infos;
    RelativeLayout rl_back;
    List<Patients.patient> patients;
    String id = "";
    TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        listView = (ListView) findViewById(R.id.listview);
        infos = new ArrayList<PatientMedieBoxs.Info>();
       Bundle bundle =  getIntent().getBundleExtra("patient");
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        if (null!=bundle){
            patients = (List<Patients.patient>) bundle.getSerializable("patient");
        }
        tv_title.setText("我的收藏");
        id = getIntent().getStringExtra("id");

        adapter = new AdapterPatientCollectMedieList(this,infos,id,patients,new RefreshActivity(){
            @Override
            public void refresh(Patients.patient patient){
                id = patient.id;
                Intent intent = new Intent(CollectionMedieActivity.this,PatientMedieBoxActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("name",patient.user_name);
                startActivity(intent);
            }
        });
        listView.setAdapter(adapter);
        refreshData();
    }
    public void refreshData(){
        showLoadingDialog();
        new HttpManager().get(this,
                Params.getInterface("invoke", "c_Recipe.query?__ORDER_BY__=state,created_time%20desc&"),
                PatientMedieBoxs.class,
                Params.getPatientInfoByID(/*maps.get("userid")*/id),
                this, false, 2);
    }
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.rl_back){
            finish();
        }
    }

    @Override
    public void onSuccess(Result response) {
        closeLoadingDialog();
        if (response.resultCode==1){
            if (response instanceof PatientMedieBoxs){
                PatientMedieBoxs p = (PatientMedieBoxs) response;
                if (null!=p.info_list&&p.info_list.size()>0) {
                    infos.clear();
                    infos.addAll(p.info_list);
                    adapter.notifyDataSetChanged();
                }
            }
        }else {
            ToastUtils.showResultToast(this, response);
        }

    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {

    }
    public interface RefreshActivity{
        public void refresh(Patients.patient patient);
    }
}
