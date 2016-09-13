package com.dachen.dgroupdoctorcompany.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.DepAdminsListDao;
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
import com.dachen.medicine.view.CustomDialog;

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
    CompanyContactDao companyContactDao;
    CompanyContactListEntity entity;
    Stack<CompanyDepment.Data.Depaments> departmentId = new Stack<>();
    ArrayList<ArrayList<OrgEntity.Data>> mDepamentsStack  =  new ArrayList<>();
    int mStackCount = 0;
    private ArrayList<OrgEntity.Data> mDepamentsList = new ArrayList<>();
    private OrgSelectAdapter mOrgSelectAdapter;
    private String orgId;
    private String currentId;
    private String backId;
    private int count=0;//打开了多少次当前页面
    View layoutView;
    public String userId = "";
    public String myuserId = "";
    private GuiderHListView mOrgListGuilde;
    DepAdminsListDao depDao ;
    String url;
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
                    if (entity.userId.equals(myuserId)){
                        depDao.clearAll();
                    }
                    finish();
                    break;
            }
        }
    };
    private boolean updataCheck =  false;
    private boolean otherPage = false;
    private TextView mTvDes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layoutView = View.inflate(this,R.layout.activity_org,null);
        myuserId = UserInfo.getInstance(this).getId();

        setContentView(layoutView);
        depDao = new DepAdminsListDao(this);
        initView();
        initData();
        setTitle("选择部门");
    }

    @Override
    public void initView() {
        super.initView();
        mOrgListGuilde = (GuiderHListView) findViewById(R.id.org_listguilde);
        mOrgListGuilde.setOnItemClickListener(this);
        listview = (ListView) findViewById(R.id.lvOrg);
        mTvSave = (TextView) findViewById(R.id.tvSave);
        mTvDes = (TextView) findViewById(R.id.tv_des);
        mTvDes.setVisibility(View.VISIBLE);
        mTvSave.setOnClickListener(this);
        mTvSave.setEnabled(false);
        mTvSave.setTextColor(Color.parseColor("#aaaaaa"));
        mTvDes.setOnClickListener(this);
    }

    private void initData(){
        companyContactDao = new CompanyContactDao(this);
        entity = (CompanyContactListEntity) getIntent().getSerializableExtra("user");
        entity = companyContactDao.queryByUserid(entity.userId+"");
        userId = entity.userId;
        if (userId.equals(myuserId)){
            url = Constants.UPDATE_USER_NAME;
        }else {
            url = Constants.UPDATE_ORG;
        }
        mOrgSelectAdapter = new MyAdapter(OrgActivity.this,mDepamentsList,entity);
        listview.setAdapter(mOrgSelectAdapter);
        String companyName = SharedPreferenceUtil.getString(CompanyApplication.getInstance(), "enterpriseName", "");
        mOrgListGuilde.addTask(companyName,companyName);
        mOrgListGuilde.setAdapter();
        mOrgListGuilde.notifyDataSetChanged();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                OrgEntity.Data depaments = (OrgEntity.Data) parent.getAdapter().getItem(position);
                if (null != depaments) {
                    if (depaments.subList.size() == 0) {
                        boolean isCheck = depaments.isCheck;
                        if (isCheck) {
                            isCheck = false;
                            orgId = "";
                            mTvSave.setEnabled(false);
                            mTvSave.setTextColor(Color.parseColor("#aaaaaa"));
                        }else{
                            isCheck = true;
                            mTvSave.setEnabled(true);
                            mTvSave.setTextColor(Color.parseColor("#3cbaff"));
                            orgId = depaments.id;
                        }
                        for (int i = 0; i < mDepamentsList.size(); i++) {
                            OrgEntity.Data data = mDepamentsList.get(i);
                            if (i == position) {
                                data.isCheck = isCheck;
                            } else {
                                data.isCheck = false;
                            }
                        }
                        mOrgSelectAdapter.update(mDepamentsList);
                        setDepartmen(depaments.name, orgId);
                    } else {
                        mOrgListGuilde.setOldPosition();
                        mOrgListGuilde.addTask(depaments.name, depaments.name);
                        mOrgListGuilde.notifyDataSetChanged();
                        mDepamentsList = depaments.subList;
                        backId();
                        mOrgSelectAdapter.update(mDepamentsList);
                        mDepamentsStack.add(copyToNewList(mDepamentsList));
                        mStackCount++;
                        otherPage = true;
                    }
                }
                mOrgListGuilde.setSelection(mOrgListGuilde.getCurrentPosition());
            }
        });
        String title = this.getIntent().getStringExtra("title");
        if(TextUtils.isEmpty(title)){
           // setTitle("选择部门");
            getOrganization();
        }else{
           // setTitle(title);
            mDepamentsList.clear();
            mDepamentsList = this.getIntent().getParcelableArrayListExtra("list");
            count = this.getIntent().getIntExtra("count",1);
            mOrgSelectAdapter.update(mDepamentsList);

            mDepamentsStack.add(copyToNewList(mDepamentsList));
            mStackCount++;
        }
    }

    @Override
    public void onBackPressed() {
        backtofront();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            mOrgListGuilde.setSelection(mOrgListGuilde.getCurrentPosition());
        }
    }
    private void updateOrg(String id) {

        showLoadingDialog();
        if (entity.userId.equals(myuserId)){
            new HttpManager().post(this, url,Result.class, Params
                    .updateOrgOnself(OrgActivity.this, orgId, id, entity.employeeId), this, false,1);
        }else {
            new HttpManager().post(this, url, Result.class, Params
                    .updateOrg(OrgActivity.this, orgId, id, entity.employeeId), this, false, 1);
        }
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
                 if (orgEntity.data != null && orgEntity.data.size() > 0) {
                     OrgEntity.Data data = new OrgEntity.Data(orgEntity.data.get(0).creatorDate,orgEntity.data.get(0).desc,orgEntity.data.get(0).enterpriseId,
                             orgEntity.data.get(0).id,orgEntity.data.get(0).name+"(根目录)",orgEntity.data.get(0).parentId,new ArrayList<OrgEntity.Data>(),
                             orgEntity.data.get(0).updator,orgEntity.data.get(0).updatorDate,orgEntity.data.get(0).creator,false);
                     mDepamentsList.add(0, data);
                     mDepamentsList.addAll(orgEntity.data.get(0).subList);
                     mOrgSelectAdapter.update(mDepamentsList);

                     mDepamentsStack.add(copyToNewList(mDepamentsList));
                     mStackCount++;

                 }
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
                mTvSave.setEnabled(false);
                mTvSave.setTextColor(Color.parseColor("#aaaaaa"));
            }else{
                isCheck = true;
                mTvSave.setEnabled(true);
                mTvSave.setTextColor(Color.parseColor("#3cbaff"));
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
        if (mDepamentsStack.size() == 1) {   //只剩联系人了,直接返回,  清空数据释放缓存
            finish();
            return;
        }if (mDepamentsStack.size() == 2) {  //公司页面
            mOrgListGuilde.reMoveTask();
            mDepamentsList = mDepamentsStack.get(mStackCount - 2);
            mDepamentsStack.remove(mStackCount-1);
        }else{//返回
           mOrgListGuilde.reMoveTask();
            mDepamentsList = mDepamentsStack.get(mStackCount - 2);
            mDepamentsStack.remove(mStackCount-1);
        }
        mOrgListGuilde.setOldPosition();
        mOrgListGuilde.notifyDataSetChanged();
        mOrgListGuilde.setSelection(mOrgListGuilde.getCurrentPosition());//导航定位到最后
        mStackCount--;
        backId();
        mOrgSelectAdapter.update(mDepamentsList);
    }
    //点击导航,返回时重置checked
    private void backId() {
        for(int i=0;i<mDepamentsList.size();i++){
            if (mDepamentsList.get(i).id.equals(orgId)) {//其他数据是否checked
                mDepamentsList.get(i).isCheck = true;
            }else {
                mDepamentsList.get(i).isCheck = false;
            }
        }
    }

    public ArrayList<OrgEntity.Data> copyToNewList(ArrayList<OrgEntity.Data> list){
        ArrayList<OrgEntity.Data> arrayList = new ArrayList<>();
        arrayList.addAll(list);
        return arrayList;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == mOrgListGuilde.getListGuide().size() - 1) {
            return;
        }

        mDepamentsList = mDepamentsStack.get(position);
        backId();
        mOrgSelectAdapter.update(mDepamentsList);

        mDepamentsStack.add(copyToNewList(mDepamentsList));
        mStackCount++;
        mOrgListGuilde.setOldPosition();
        mOrgListGuilde.addBackTask(position);
        mOrgListGuilde.notifyDataSetChanged();
        mOrgListGuilde.setSelection(mOrgListGuilde.getCurrentPosition());
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
                showDialog();
                break;
            case R.id.tv_des://关闭按钮
                finish();
                break;
        }
    }
    public void showDialog(){
        if(TextUtils.isEmpty(orgId)){
            ToastUtil.showToast(OrgActivity.this,"请先选择要修改的部门");
            return;
        }
        //确定移动到所选部门吗？“，
        final CustomDialog dialog = new CustomDialog(this);
        dialog.showDialog("", "确定移动到所选部门吗?",R.string.cancel,R.string.sure, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dimissDialog();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dimissDialog();
                updateOrg(userId);
            }
        });
    }

    @Override
    protected void onDestroy() {
        mOrgListGuilde.clearData();
        super.onDestroy();
    }
}
