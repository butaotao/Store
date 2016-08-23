package com.dachen.dgroupdoctorcompany.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.OrgSelectAdapter;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.CompanyDepment;
import com.dachen.dgroupdoctorcompany.entity.OrgEntity;
import com.dachen.dgroupdoctorcompany.utils.GetAllDoctor;
import com.dachen.medicine.common.utils.MActivityManager;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Created by weiwei on 2016/5/20.
 */
public class OrgActivity extends BaseActivity implements HttpManager.OnHttpListener{
    private ListView listview;
    private TextView mTvSave;
    Stack<CompanyDepment.Data.Depaments> departmentId = new Stack<>();
    private List<OrgEntity.Data> mDepamentsList = new ArrayList<>();
    private OrgSelectAdapter mOrgSelectAdapter;
    private String orgId;
    private int count=0;//打开了多少次当前页面
    View layoutView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutView = View.inflate(this,R.layout.activity_org,null);
        setContentView(layoutView);
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        listview = (ListView) findViewById(R.id.lvOrg);
        mTvSave = (TextView) findViewById(R.id.tvSave);
        mTvSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tvSave:
                updateOrg();
                break;
        }
    }

    private void initData(){
        mOrgSelectAdapter = new MyAdapter(OrgActivity.this,mDepamentsList);
        listview.setAdapter(mOrgSelectAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrgEntity.Data depaments = (OrgEntity.Data) parent.getAdapter().getItem(position);
                if(null != depaments){
                    if(depaments.subList.size() == 0){
                        boolean isCheck = depaments.isCheck;
                        if(isCheck){
                            isCheck = false;
                            orgId = "";
                        }else{
                            isCheck = true;
                            orgId = depaments.id;
                        }
                        for(int i=0;i<mDepamentsList.size();i++){
                            OrgEntity.Data data = mDepamentsList.get(i);
                            if(i == position){
                                data.isCheck = isCheck;
                            }else{
                                data.isCheck = false;
                            }
                        }
                        mOrgSelectAdapter.update(mDepamentsList);
                        setDepartmen(depaments.name,orgId);
                    }else{
                        Intent intent = new Intent(OrgActivity.this,OrgActivity.class);
                        intent.putExtra("title",depaments.name);
                        intent.setExtrasClassLoader(OrgEntity.Data.class.getClassLoader());
                        intent.putParcelableArrayListExtra("list",depaments.subList);

                        count++;
                        intent.putExtra("count",count);
                        startActivity(intent);
                    }
                }
            }
        });
        String title = this.getIntent().getStringExtra("title");
        if(TextUtils.isEmpty(title)){
            setTitle("选择部门");
            getOrganization();
        }else{
            setTitle(title);
            mDepamentsList.clear();
            mDepamentsList = this.getIntent().getParcelableArrayListExtra("list");
            count = this.getIntent().getIntExtra("count",1);
            mOrgSelectAdapter.update(mDepamentsList);
        }

    }

    private void updateOrg(){
        if(TextUtils.isEmpty(orgId)){
            ToastUtil.showToast(OrgActivity.this,"请先选择要修改的部门");
            return;
        }
        showLoadingDialog();
        new HttpManager().post(this, Constants.UPDATE_ORG,Result.class, Params
                .updateOrg(OrgActivity.this,orgId),this,false,1);
    }

    private void getOrganization(){
        showLoadingDialog();
        HashMap<String ,String > maps = new HashMap<>();
        maps.put("access_token", SharedPreferenceUtil.getString(this, "session", ""));
        maps.put("drugCompanyId", SharedPreferenceUtil.getString(this, "enterpriseId", ""));
        maps.put("getUnassigned", "1");
//        maps.put("id",idDep);

        new HttpManager().post(this, Constants.GET_ENTER_ORG, OrgEntity.class,
                maps, this,
                false, 1);
    }

    @Override
    public void onSuccess(Result response) {
        closeLoadingDialog();
        if(response!=null&&response.resultCode==1){
             if(response instanceof OrgEntity){
                OrgEntity orgEntity =(OrgEntity)(response);
                 mDepamentsList.clear();
                 OrgEntity.Data data = new OrgEntity.Data(orgEntity.data.get(0).creatorDate,orgEntity.data.get(0).desc,orgEntity.data.get(0).enterpriseId,
                         orgEntity.data.get(0).id,orgEntity.data.get(0).name+"(根目录)",orgEntity.data.get(0).parentId,new ArrayList<OrgEntity.Data>(),
                         orgEntity.data.get(0).updator,orgEntity.data.get(0).updatorDate,orgEntity.data.get(0).creator,false);
                 mDepamentsList.add(0, data);
                 mDepamentsList.addAll(orgEntity.data.get(0).subList);
                 mOrgSelectAdapter.update(mDepamentsList);

            }else{
                 GetAllDoctor.getInstance().getPeople(OrgActivity.this);
                 ToastUtil.showToast(OrgActivity.this,"修改成功");
                 for(int i=0;i<count;i++){
                     MActivityManager.getInstance().finishActivity(OrgActivity.class);
                 }
                 finish();
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

    private class MyAdapter extends OrgSelectAdapter{
        public MyAdapter(Context context, List<OrgEntity.Data> data) {
            super(context, data);
        }

        @Override
        protected void onCheckBoxCheck(OrgEntity.Data item) {
            boolean isCheck = item.isCheck;
            if(isCheck){
                isCheck = false;
                orgId = "";
            }else{
                isCheck = true;
                orgId = item.id;
            }
            for(int i=0;i<mDepamentsList.size();i++){
                OrgEntity.Data data = mDepamentsList.get(i);
                if(data.id == item.id){
                    data.isCheck = isCheck;
                }else{
                    data.isCheck = false;
                }
            }
            mOrgSelectAdapter.update(mDepamentsList);
        }
    }
    public int getContent() {
        return 0;
    }
    public void setDepartmen(String name,String idDep){
        CompanyDepment depment = new CompanyDepment();
        CompanyDepment.Data data = depment.new Data();

        CompanyDepment.Data.Depaments c1 = data.new Depaments();
        c1.id = idDep;
        c1.name = name;
        if (departmentId.size()>1&&departmentId.get(departmentId.size()-1).id.equals(idDep)){
            return;
        }
        departmentId.add(c1);
    }
    public void backtofront() {

    }
}
