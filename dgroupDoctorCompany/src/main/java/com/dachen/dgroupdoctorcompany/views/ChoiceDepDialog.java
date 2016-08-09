package com.dachen.dgroupdoctorcompany.views;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.BaseRecordActivity;
import com.dachen.dgroupdoctorcompany.adapter.ChoiceAdpHorizonAdapter;
import com.dachen.dgroupdoctorcompany.adapter.ChoiceAdpVertiAdapter;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.DepAdminsListDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.DepAdminsList;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.view.ScrollTabView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Burt on 2016/6/23.
 */
public class ChoiceDepDialog extends Dialog implements View.OnClickListener,ScrollTabView.OnInitView {
    Activity mActivity;
    HorizontalListView horizonlistview;
    ListView listView;
    List<DepAdminsList> deps;
    List<DepAdminsList> horizon;
    List<DepAdminsList> vertiList;
    ChoiceAdpHorizonAdapter adpHorizonAdapter;
    ChoiceAdpVertiAdapter vertiAdapter;
    ListView verListview;
    DepAdminsListDao managerDao;
    TextView departdes;
    DepAdminsList dep;
    ImageView iv_dep;
    String select = "";
    CompanyContactDao contactDao;
    BaseRecordActivity.GetRecord record;
    public ChoiceDepDialog(Activity activity,TextView departdes,ImageView iv_dep,BaseRecordActivity.GetRecord record) {
        super(activity, R.style.dialog_with_alpha);
        this.mActivity = activity;
        this.deps = deps;
        this.departdes = departdes;
        this.iv_dep = iv_dep;
        managerDao = new DepAdminsListDao(mActivity);
        this.record = record;
        contactDao = new CompanyContactDao(activity);
        List<CompanyContactListEntity> entities =  contactDao.queryByTelephone
                (SharedPreferenceUtil.getString(activity, "telephone", ""));
        if (entities.size()>0){
            select = entities.get(0).department;
            departdes.setText(select);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {//dialog_medieinfo
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choicedep);

        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);

        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.Umengstyle);
        setCancelable(true);
        this.setCanceledOnTouchOutside(true);

        lp.width = (int) (metric.widthPixels);; // 宽度
        lp.height = (int) (metric.heightPixels);; // 高度/*
       /* lp.alpha = 0.7f; // 透明度*/
        window.setAttributes(lp);
    }





    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_close) {
            departdes.setText(select);
            CloseDialog();
        }else if(v.getId() == R.id.cancel){
//            CloseDialog();
            iv_dep.setBackgroundResource(R.drawable.recordirro);
            departdes.setTextColor(mActivity.getResources().getColor(R.color.color_333333));
            if(isShowing())
                dismiss();
        }
    }

    public void showDialog(){
        iv_dep.setBackgroundResource(R.drawable.recordirro_select);
        departdes.setTextColor(mActivity.getResources().getColor(R.color.color_3cbaff));
        if(!isShowing() ){

            show();

        }

    }
    public void showDialog(String id){
        iv_dep.setBackgroundResource(R.drawable.recordirro_select);
        departdes.setTextColor(mActivity.getResources().getColor(R.color.color_3cbaff));
        if(!isShowing() ){

            show();

        }
        findViewById(R.id.tv_close).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        horizonlistview = (HorizontalListView) findViewById(R.id.horizonlistview);
        verListview = (ListView) findViewById(R.id.listview);
        dep = new DepAdminsList();
        dep.orgId = "-1";
        dep.orgName = "请选择";


        horizon = new ArrayList<>();
        horizon.add(dep);
        adpHorizonAdapter = new ChoiceAdpHorizonAdapter(mActivity,horizon);
        horizonlistview.setAdapter(adpHorizonAdapter);


        vertiList = new ArrayList<>();
        vertiList =managerDao.queryManagerid(id);
        vertiAdapter = new ChoiceAdpVertiAdapter(mActivity,vertiList);
        verListview.setAdapter(vertiAdapter);


        horizonlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (horizon.size()>=1){
                    List<DepAdminsList> horizonremove = new ArrayList<DepAdminsList>();
                    horizonremove = horizon.subList(0, position + 1);
                    // horizon.clear();
                    //horizon.addAll(horizonremove);
                    horizon.remove(dep);
                    adpHorizonAdapter = new ChoiceAdpHorizonAdapter(mActivity, horizonremove);
                    horizonlistview.setAdapter(adpHorizonAdapter);
                    horizon = horizonremove;
                    DepAdminsList dep = horizon.get(horizon.size() - 1);
                    vertiList.clear();
                    String depid = "0";
                    depid = dep.cid;
                    vertiList = managerDao.queryManagerSiner(depid);
                    vertiAdapter = new ChoiceAdpVertiAdapter(mActivity, vertiList);
                    verListview.setAdapter(vertiAdapter);
                    select = horizon.get(position).orgName;
                    BaseRecordActivity.deptId = horizon.get(position).cid;
                }
                //departdes.setText(horizon.get(position).orgName);

            }
        });
        verListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DepAdminsList depv = (DepAdminsList) vertiAdapter.getItem(position);
                //departdes.setText(depv.orgName);
                select = depv.orgName;
                BaseRecordActivity.deptId = depv.cid;
                horizon.remove(dep);
                vertiList.clear();
                if (managerDao.queryManagerSiner(depv.cid).size() > 0) {


                    vertiList = managerDao.queryManagerSiner(depv.cid);

                }else {
                    //  CloseDialog();
                }
                vertiAdapter = new ChoiceAdpVertiAdapter(mActivity, vertiList);
                verListview.setAdapter(vertiAdapter);

                horizon.add(depv);

                adpHorizonAdapter.notifyDataSetChanged();
            }
        });

    }
    public void CloseDialog(){
        iv_dep.setBackgroundResource(R.drawable.recordirro);
        departdes.setTextColor(mActivity.getResources().getColor(R.color.color_333333));
        if(isShowing())
            dismiss();
        record.initConfig();
        record.getRecord("");
    }



    @Override
    public void onInit(ArrayList<View> arg0) {
        // TODO Auto-generated method stub

    }
}
