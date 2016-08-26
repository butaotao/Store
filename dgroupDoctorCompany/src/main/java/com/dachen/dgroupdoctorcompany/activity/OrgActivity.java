package com.dachen.dgroupdoctorcompany.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.OrgSelectAdapter;
import com.dachen.dgroupdoctorcompany.app.CompanyApplication;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.entity.CompanyDepment;
import com.dachen.dgroupdoctorcompany.entity.OrgEntity;
import com.dachen.dgroupdoctorcompany.utils.GetAllDoctor;
import com.dachen.dgroupdoctorcompany.utils.UserInfo;
import com.dachen.dgroupdoctorcompany.views.GuiderHListView;
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
public class OrgActivity extends BaseActivity implements HttpManager.OnHttpListener, AdapterView.OnItemClickListener {
    private ListView listview;
    private TextView mTvSave;
    CompanyContactListEntity entity;
    Stack<CompanyDepment.Data.Depaments> departmentId = new Stack<>();
    ArrayList<ArrayList<OrgEntity.Data>> mDepamentsStack  =  new ArrayList<>();
    int mStackCount = 0;
    private ArrayList<OrgEntity.Data> mDepamentsList = new ArrayList<>();
    private OrgSelectAdapter mOrgSelectAdapter;
    private String orgId;
    private int count=0;//打开了多少次当前页面
    View layoutView;
    public String userId = "";
    private GuiderHListView mOrgListGuilde;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1100:
                    closeLoadingDialog();
                    ToastUtil.showToast(OrgActivity.this,"修改成功");
                    for(int i=0;i<count;i++){
                        MActivityManager.getInstance().finishActivity(OrgActivity.class);
                    }
                    finish();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutView = View.inflate(this,R.layout.activity_org,null);
        userId = UserInfo.getInstance(this).getId();
        setContentView(layoutView);
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        mOrgListGuilde = (GuiderHListView) findViewById(R.id.org_listguilde);
        mOrgListGuilde.setOnItemClickListener(this);
        listview = (ListView) findViewById(R.id.lvOrg);
        mTvSave = (TextView) findViewById(R.id.tvSave);
        mTvSave.setOnClickListener(this);
    }


    private void initData(){
        entity = (CompanyContactListEntity) getIntent().getSerializableExtra("user");
        mOrgSelectAdapter = new MyAdapter(OrgActivity.this,mDepamentsList,entity);
        listview.setAdapter(mOrgSelectAdapter);
        mOrgListGuilde.addTask("选择部门","选择部门");
        String companyName = SharedPreferenceUtil.getString(CompanyApplication.getInstance(), "enterpriseName", "");
        mOrgListGuilde.addTask(companyName,companyName);
        mOrgListGuilde.setAdapter();
        mOrgListGuilde.notifyDataSetChanged();
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
                        mOrgListGuilde.setOldPosition(mOrgListGuilde.getListGuideAdapter().getCount());
                        mOrgListGuilde.addTask(depaments.name,depaments.name);
                        setTitle(depaments.name);
                        mOrgListGuilde.notifyDataSetChanged();
                        mDepamentsList = depaments.subList;
                        mOrgSelectAdapter.update(mDepamentsList);
                        mDepamentsStack.add(copyToNewList(mDepamentsList));
                        mStackCount++;
                        /*Intent intent = new Intent(OrgActivity.this,OrgActivity.class);
                        intent.putExtra("title",depaments.name);
                        intent.setExtrasClassLoader(OrgEntity.Data.class.getClassLoader());
                        intent.putParcelableArrayListExtra("list",depaments.subList);

                        count++;
                        intent.putExtra("count",count);
                        startActivity(intent);*/
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

            mDepamentsStack.add(copyToNewList(mDepamentsList));
            mStackCount++;
            Log.d("zxy", "initData: mStackCount = "+mStackCount);
        }

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            mOrgListGuilde.setSelection(mOrgListGuilde.getCurrentPosition());
        }
    }
    private void updateOrg(String id){
        if(TextUtils.isEmpty(orgId)){
            ToastUtil.showToast(OrgActivity.this,"请先选择要修改的部门");
            return;
        }
        showLoadingDialog();
        new HttpManager().post(this, Constants.UPDATE_ORG, Result.class, Params
                .updateOrg(OrgActivity.this, orgId, id), this, false, 1);
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

        if(response!=null&&response.resultCode==1){
             if(response instanceof OrgEntity){
                 closeLoadingDialog();
                OrgEntity orgEntity =(OrgEntity)(response);
                 mDepamentsList.clear();
                 OrgEntity.Data data = new OrgEntity.Data(orgEntity.data.get(0).creatorDate,orgEntity.data.get(0).desc,orgEntity.data.get(0).enterpriseId,
                         orgEntity.data.get(0).id,orgEntity.data.get(0).name+"(根目录)",orgEntity.data.get(0).parentId,new ArrayList<OrgEntity.Data>(),
                         orgEntity.data.get(0).updator,orgEntity.data.get(0).updatorDate,orgEntity.data.get(0).creator,false);
                 mDepamentsList.add(0, data);
                 mDepamentsList.addAll(orgEntity.data.get(0).subList);
                 mOrgSelectAdapter.update(mDepamentsList);

                 mDepamentsStack.add(copyToNewList(mDepamentsList));
                 mStackCount++;
                Log.d("zxy", "onSuccess: mStackCount = "+mStackCount);
            }else{
                 GetAllDoctor.getInstance().getPeople(OrgActivity.this, handler);

             }
        }else {
            closeLoadingDialog();
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
        public MyAdapter(Context context, List<OrgEntity.Data> data,CompanyContactListEntity entity) {
            super(context, data,entity);
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
        //Log.d("zxy", "backtofront: ");
        if (mDepamentsStack.size() == 1) {   //只剩联系人了,直接返回,  清空数据释放缓存
          //  Log.d("zxy", "backtofront: 1");
            mOrgListGuilde.clearData();
            finish();
            return;
        }if (mDepamentsStack.size() == 2) {  //公司页面
            setTitle("选择部门");
            mOrgListGuilde.reMoveTask();
            mDepamentsList = mDepamentsStack.get(mStackCount - 2);
            mDepamentsStack.remove(mStackCount-1);
           // Log.d("zxy", "backtofront: 2  mStackCount = "+mStackCount+", mDepamentsList = " +mDepamentsList);
        }else{//返回
           mOrgListGuilde.reMoveTask();
            mDepamentsList = mDepamentsStack.get(mStackCount - 2);
            mDepamentsStack.remove(mStackCount-1);
          //  Log.d("zxy", "backtofront: 3 mStackCount = "+mStackCount+", mDepamentsList = " +mDepamentsList);
        }

        String title = mOrgListGuilde.getListGuide().get(mOrgListGuilde.getListGuide().size()-1);
        setTitle(title);
        mStackCount--;
       // Log.d("zxy", "backtofront: 4  mStackCount = "+mStackCount);
        mOrgSelectAdapter.update(mDepamentsList);
    }

    public ArrayList<OrgEntity.Data> copyToNewList(ArrayList<OrgEntity.Data> list){
        ArrayList<OrgEntity.Data> arrayList = new ArrayList<>();
        arrayList.addAll(list);
        return arrayList;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            mOrgListGuilde.clearData();
            finish();
            return;
        }
        if (position == mOrgListGuilde.getListGuide().size() - 1) {
            return;
        }

        mDepamentsList = mDepamentsStack.get(position-1);
        mOrgSelectAdapter.update(mDepamentsList);

        mDepamentsStack.add(copyToNewList(mDepamentsList));
        mStackCount++;
        mOrgListGuilde.addBackTask(position);
        mOrgListGuilde.notifyDataSetChanged();
        String title = mOrgListGuilde.getListGuide().get(mOrgListGuilde.getListGuide().size()-1);
        setTitle(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                backtofront();
                break;
            case R.id.rl_back:
                backtofront();
                break;
            case R.id.tvSave:
                updateOrg(userId);
                break;
        }
    }
}
