package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.AddSelfPartHospitaAdapter;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.DeleteAddSelfPartHospital;
import com.dachen.dgroupdoctorcompany.entity.HospitalDes;
import com.dachen.dgroupdoctorcompany.entity.HospitalMangerData;
import com.dachen.dgroupdoctorcompany.entity.MedieEntity;
import com.dachen.dgroupdoctorcompany.entity.ShowAddSelfPartHospitalsData;
import com.dachen.dgroupdoctorcompany.utils.JsonUtils.AddSelfPartHospitalTrans;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.view.CustomDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Burt on 2016/5/19.
 */
public class AddSelfPartHospitalActivity extends BaseActivity implements HttpManager.OnHttpListener {
    ListView listview;
    AddSelfPartHospitaAdapter adapter;
    public static ArrayList<HospitalDes> data;
    ViewStub vstub_title;
    HospitalMangerData manager;
    TextView tv_mediename;
    Button btn_sure;
    LinearLayout rl_cancel;
    MedieEntity.Medie medie;
    RelativeLayout rl_medicinename;
    private String groupId;
    RelativeLayout rl_sure;
    boolean issure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addselfparthospital);
        listview = (ListView) findViewById(R.id.listview);
        rl_cancel = (LinearLayout) findViewById(R.id.rl_cancel);
        findViewById(R.id.btn_sure).setOnClickListener(this);
        tv_mediename = (TextView) findViewById(R.id.tv_mediename);
        HospitalActivity.hospitals = new ArrayList<>();
        btn_sure = (Button) findViewById(R.id.btn_sure);
        issure = false;
        vstub_title = (ViewStub) findViewById(R.id.vstub_title);
        RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.ll_sub);
        View view = vstub_title.inflate(this, R.layout.stub_viewtext, rl);
        TextView tv = (TextView) view.findViewById(R.id.tv_search);
        rl_medicinename = (RelativeLayout) findViewById(R.id.rl_medicinename);
        tv.setOnClickListener(this);
        tv.setText("添加医院");
        rl_sure = (RelativeLayout) findViewById(R.id.rl_sure);
        findViewById(R.id.btn_cancelleft).setOnClickListener(this);
        findViewById(R.id.btn_cancelright).setOnClickListener(this);
        Bundle bundle = getIntent().getBundleExtra("manager");
        data = new ArrayList<>();

        if (bundle != null) {
            manager = (HospitalMangerData) bundle.getSerializable("manager");
            if (null != manager && manager.hospitalList != null) {
                data = manager.hospitalList;
            }
        }
        if (null != manager) {
            tv_mediename.setText(manager.goodsGroupName + "");
        }
        String fromactivity = getIntent().getStringExtra("fromactivity");
        if (!TextUtils.isEmpty(fromactivity)) {
            if (fromactivity.equals("SelfPartHospitalChoiceMedieActivity")) {
                setTitle("新建分管关系");
                rl_cancel.setVisibility(View.GONE);
                btn_sure.setVisibility(View.VISIBLE);
                issure = true;
                medie = (MedieEntity.Medie) getIntent().getSerializableExtra("selectdata");
                if (null != medie) {
                    tv_mediename.setText(medie.drugName);
                    getHospital();
                }
                groupId = medie.id;
            } else {
                setTitle("编辑分管关系");
                issure = false;
                rl_cancel.setVisibility(View.VISIBLE);
                btn_sure.setVisibility(View.GONE);
                groupId = manager.goodsGroupId;
            }
        }
        rl_sure.setVisibility(View.VISIBLE);
        Collections.sort(data, new PinyinComparator());
        adapter = new AddSelfPartHospitaAdapter(this,issure, data, new RefreshData() {
            @Override
            public void Refreshdata(int size) {
                refreshdata(size);
            }
        }, groupId, this);
        refreshdata(data.size());
        listview.setAdapter(adapter);
        listview.setEmptyView(findViewById(R.id.rl_notcontent));
    }

    @Override
    public void onSuccess(Result response) {
        if (response instanceof ShowAddSelfPartHospitalsData) {
            ShowAddSelfPartHospitalsData datas = (ShowAddSelfPartHospitalsData) response;
            if (datas.data != null) {
                data = AddSelfPartHospitalTrans.getHospital(datas.data.pageData);
                Collections.sort(data, new PinyinComparator());
                adapter = new AddSelfPartHospitaAdapter(this, issure, data, new RefreshData() {
                    @Override
                    public void Refreshdata(int size) {
                        refreshdata(size);
                    }
                }, groupId, this);
                refreshdata(data.size());
                listview.setAdapter(adapter);
                listview.setEmptyView(findViewById(R.id.rl_notcontent));
            }
        }
    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {

    }

    public interface RefreshData {
        void Refreshdata(int size);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_sure:
                /*if (HospitalManagerActivity.totalNumHospital+data.size()>0){
                    showMoreFiftyHosp();
                }*/
                finish();
                break;
            case R.id.tv_search:
                Intent intent = new Intent(this, SearchActivity.class);
                intent.putExtra("AddSelfPartHospitalActivity", "AddSelfPartHospitalActivity");
                intent.putExtra("id", groupId);
                startActivityForResult(intent, 100);
                break;
          /*  case R.id.btn_cancelleft:
                showDeleteMedie();
                break;*/
            case R.id.btn_cancelright:
                    showDeleteHospitel(); 
                break;
        }
    }

    class PinyinComparator implements Comparator<HospitalDes> {
        public int compare(HospitalDes o1, HospitalDes o2) {

            return o1.name.compareTo(o2.name);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshdata(data.size());
        adapter = new AddSelfPartHospitaAdapter(AddSelfPartHospitalActivity.this,issure, data, new RefreshData() {
            @Override
            public void Refreshdata(int size) {
                refreshdata(size);
            }
        }, groupId, this);
        getHospital();
        listview.setAdapter(adapter);
        listview.setEmptyView(findViewById(R.id.rl_notcontent));
    }
public void refreshdata(int size){
    if (size == 0) {
        rl_medicinename.setBackgroundColor(getResources().getColor(R.color.translate));

    } else {
        rl_medicinename.setBackgroundColor(getResources().getColor(R.color.white));

    }
}

    public void showDeleteHospitel() {
        final CustomDialog dialog = new CustomDialog(this);
        dialog.showDialog("提示", "您确定要取消品种的分管关系吗？", R.string.cancel, R.string.sure, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dimissDialog();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dimissDialog();
                deleteHospitalRelation(groupId);
            }
        });
    }
    //

    public void getHospital() {
        String s = Constants.DRUG+"saleFriend/getAssignHospitalList";
        HashMap<String, String> maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(this).getSesstion());
        maps.put("userId", SharedPreferenceUtil.getString(this, "id", ""));
        if (!TextUtils.isEmpty(groupId)) {
            maps.put("goodsGroupId", groupId);
            new HttpManager().post(this, s, ShowAddSelfPartHospitalsData.class,
                    maps, this,
                    false, 1);
            //showLoadingDialog();
        }
    }

    public void deleteHospitalRelation(String id) {
        String s = Constants.DRUG+"assignGoodsGroup/deleteRelationGoodsgroup";
        HashMap<String, String> maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(this).getSesstion());
        maps.put("userId", SharedPreferenceUtil.getString(this, "id", ""));
        if (!TextUtils.isEmpty(id)) {
            maps.put("goodsGroupId", id);
            new HttpManager().post(this, s, DeleteAddSelfPartHospital.class,
                    maps, new HttpManager.OnHttpListener<Result>() {
                        @Override
                        public void onSuccess(Result response) {
                            closeLoadingDialog();
                            if (response.resultCode == 1) {
                                finish();
                            }
                        }

                        @Override
                        public void onSuccess(ArrayList<Result> response) {

                        }

                        @Override
                        public void onFailure(Exception e, String errorMsg, int s) {
                            ToastUtil.showToast(AddSelfPartHospitalActivity.this, "删除失败");
                            closeLoadingDialog();
                        }
                    },
                    false, 1);
            //showLoadingDialog();
        }
    }//
    //
}
