package com.dachen.dgroupdoctorcompany.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.CompanyContactListAdapter;
import com.dachen.dgroupdoctorcompany.adapter.CompanyListGuide;
import com.dachen.dgroupdoctorcompany.app.CompanyApplication;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.RoleDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.DepAdminsList;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.entity.CompanyDepment;
import com.dachen.dgroupdoctorcompany.entity.CompanysTitle;
import com.dachen.dgroupdoctorcompany.fragment.AddressList;
import com.dachen.dgroupdoctorcompany.utils.CommonUitls;
import com.dachen.dgroupdoctorcompany.utils.DataUtils.CompanyContactDataUtils;
import com.dachen.dgroupdoctorcompany.utils.GetAllDoctor;
import com.dachen.dgroupdoctorcompany.views.HorizontalListView;
import com.dachen.dgroupdoctorcompany.views.NoScrollerListView;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.view.CustomDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import butterknife.ButterKnife;

/**
 * Created by Burt on 2016/2/26.
 */
public  class CompanyContactListActivity extends BaseActivity implements HttpManager.OnHttpListener,
        AdapterView.OnItemLongClickListener,AdapterView.OnItemClickListener{

    CompanyContactListAdapter adapter;
    NoScrollerListView listview;
    ArrayList<BaseSearch> list;
    Stack<CompanyDepment.Data.Depaments> departmentId = new Stack<>();
    public String ids;
    String title = "";
    public String idDep = "0";
    HashMap<String, CompanysTitle> listsTitle;
    CompanyContactDao companyContactDao;
    RoleDao roleDao;
    List<BaseSearch> listsHorizon;
    String pareid;
    ArrayList<CompanyContactListEntity> groupUsers;
    //0为公司组织架构，1为管理者进入的界面
    int showContent = 0;
    boolean manager;
    LinearLayout layout_line;
    TextView tv_des;
    RelativeLayout empteyll;
    String firstLevelId = "";
    int p;
    private PullToRefreshScrollView mPullToRefreshScrollView;
    private ArrayList<Integer> mUserIdList;
    private int mPullIndex;
    private int mPageSize = 50;
    private String mParentId;
    public static int isManager = 1;
    public static int editColleageDep = 2;
    public String companyid;
    /*-----------------------------------------zxy start-----------------------------------------*/
    private HorizontalListView mCp_listguilde;
    private ArrayList<String> mListGuide = new ArrayList<>();               //导航Listview数据
    private Map<Integer,ArrayList<String>> departList = new LinkedHashMap<>();   //导航Listview数据任务栈
    private CompanyListGuide mListGuideAdapter;
    private Map<Integer, String > listGuideMap = new LinkedHashMap<>(); //公司部门id任务栈;
    private int currentPosition = 0;                                    //任务栈任务数
    private int oldPosition = 0;
    boolean isEmpty = false;
    public String departName="";
    String parentId;
    View layoutView;
    Activity context;
    /*-----------------------------------------zxy end -----------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutView = View.inflate(this, R.layout.activity_companycontactlist, null);
        setContentView(layoutView);
        context = this;
        listview = (NoScrollerListView) findViewById(R.id.listview);
        layout_line = (LinearLayout) findViewById(R.id.layout_line);
        mPullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.refresh_scroll_view);
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.DISABLED);
        companyContactDao = new CompanyContactDao(CompanyContactListActivity.this);
        roleDao = new RoleDao(CompanyContactListActivity.this);
        listsHorizon = CommonUitls.getListsHorizon();
        listsHorizon.clear();
        p = 0;
        GetAllDoctor.getInstance().getPeople(CompanyContactListActivity.this);
        empteyll = (RelativeLayout) findViewById(R.id.empteyll);
        empteyll.setVisibility(View.GONE);
        setTitle("企业通讯录");
        manager = false;
        //有管理权限的管理者跳转过来，管理人员
        RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.ll_sub);
        ViewStub stub = (ViewStub) findViewById(R.id.vstub_title);
        tv_des = (TextView) findViewById(R.id.tv_des);
        View view = stub.inflate(this, R.layout.stub_viewtext, rl);
        /*-----------------------------------------zxy start-----------------------------------------*/
        mCp_listguilde = (HorizontalListView) findViewById(R.id.cp_listguilde);
        mListGuide.add("联系人");
        departList.put(currentPosition, copyToNewList(mListGuide));
        listGuideMap.put(currentPosition++, "联系人");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {//如果部门存在,显示部门,不存在直接显示公司名
            String depName = extras.getString("depName");//得到部门名字
            mListGuide.add(depName);
        }else {
            String companyName = SharedPreferenceUtil.getString(CompanyApplication.getInstance(), "enterpriseName", "");
            mListGuide.add(companyName);
        }
        departList.put(currentPosition, copyToNewList(mListGuide));
        mListGuideAdapter = new CompanyListGuide(this, mListGuide);
        mCp_listguilde.setOnItemClickListener(this);
        mCp_listguilde.setAdapter(mListGuideAdapter);
        /*-----------------------------------------zxy end -----------------------------------------*/
        //tv.setOnClickListener(this);
        //tv.setText("管理");
        //tv.setVisibility(View.GONE);
        tv_des.setOnClickListener(this);
        idDep = AddressList.deptId;
        companyid= AddressList.deptId;;
        if (!idDep.equals("-1")) {
            manager = true;

        } else {
            idDep = "0";
            companyid = "0";
        }
        setDepartmen("企业通讯录",idDep);
        getOrganization(idDep);
        ButterKnife.bind(this);
        listsTitle = new HashMap<>();
        findViewById(R.id.rl_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                backtofront();
            }
        });
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backtofront();
            }
        });
        list = new ArrayList<>();
        adapter = new CompanyContactListAdapter(this, R.layout.adapter_companycontactlist, list, 0);
        listview.setAdapter(adapter);
        getOrganization(idDep);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                BaseSearch contact = (BaseSearch) adapter.getItem(position);
                CompanyContactListEntity c2 = null;
                CompanyDepment.Data.Depaments c1 = null;
                if (contact instanceof CompanyContactListEntity) {
                    c2 = (CompanyContactListEntity) (contact);
                    final CustomDialog dialog = new CustomDialog(CompanyContactListActivity.this);
                    dialog.showDialog(null, "删除联系人", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dimissDialog();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dimissDialog();
                        }
                    });
                }
                return true;
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter.getItem(position) instanceof CompanyContactListEntity) {
                    onColleagueEdit((CompanyContactListEntity) adapter.getItem(position), position);
                }else  if (adapter.getItem(position) instanceof CompanyDepment.Data.Depaments) {
                    getDepment(adapter.getItem(position), false);
                }
            }
        });
        mPullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (mUserIdList != null) {
                    if (mUserIdList.size() > (mPullIndex + 1) * mPageSize) {
                        List<Integer> integers = mUserIdList.subList(mPullIndex * mPageSize, (mPullIndex + 1) *
                                mPageSize);
                        mPullIndex++;
                        List<CompanyContactListEntity> queryList = companyContactDao.queryAndSortByUserIds(integers);
                        list.addAll(queryList);
                        adapter.notifyDataSetChanged();
                    } else {
                        List<Integer> integers = mUserIdList.subList(mPullIndex * mPageSize, mUserIdList.size());
                        List<CompanyContactListEntity> queryList = companyContactDao.queryAndSortByUserIds(integers);
                        list.addAll(queryList);
                        adapter.notifyDataSetChanged();
                        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.DISABLED);
                    }
                }
                mPullToRefreshScrollView.onRefreshComplete();
            }
        });
    }
    public void getDepment(BaseSearch contact ,boolean clickRadio){
        CompanyDepment.Data.Depaments  c1 = (CompanyDepment.Data.Depaments) (contact);
        CompanysTitle title = new CompanysTitle();
        title.id = c1.parentId;
        departName = c1.name;
        title.parentDept = c1.name;
        listsTitle.put(c1.id, title);
        setTitles(c1.name);
        mListGuide.add(c1.name);
        departList.put(currentPosition, copyToNewList(mListGuide));
        listGuideMap.put(currentPosition++, c1.id);
        Log.d("zxy", "onCreate: currentPosition = " + currentPosition + ", idDep = " + c1.id);
        parentId = c1.parentId;
        setDepartmen(c1.name, c1.id);
                    /*-----------------------------------------zxy start-----------------------------------------*/
                    /*-----------------------------------------zxy end -----------------------------------------*/
        if (c1 != null) {
            idDep = c1.id;
            getOrganization(idDep);
            adapter.notifyDataSetChanged();
                        /*-----------------------------------------zxy start-----------------------------------------*/
            mListGuideAdapter.notifyDataSetChanged();
                        /*-----------------------------------------zxy end -----------------------------------------*/

        }

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
    public void onColleagueEdit(CompanyContactListEntity c2,int position){
        Intent intent = new Intent(CompanyContactListActivity.this, ColleagueDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("peopledes", c2);
        if (manager) {
            intent.putExtra("manager", "manager");
        }
        intent.putExtra("peopledes", bundle);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (manager && !idDep.equals("-1")&&getContent()!=1) {
            getOrganization(idDep);
        }
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        //super.onClick(v);
        switch (v.getId()) {

            case R.id.tv_search:
                intent = new Intent(this, EidtColleagueActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.tv_des:
                finish();
                break;
        }

    }

    public void backtofront() {

        int position = currentPosition-2;//当前任务栈id数
        Log.d("zxy", "backtofront: currentPosition = "+currentPosition);
        if (position == 0) {   //只剩联系人了,直接返回,  清空数据释放缓存
            Log.d("zxy", "backtofront: listGuideMap.size() = "+listGuideMap.size()+"finish");
            listGuideMap.clear();
            listGuideMap = null;
            mListGuide.clear();
            mListGuide = null;
            departList.clear();
            departList = null;
            finish();
            return;
        }if (position == 1) {  //公司页面
            setTitle("企业通讯录");
            idDep = listGuideMap.get(position);//得到任务栈的前一个任务id
            mListGuide.clear();
            mListGuide.addAll(departList.get(position));    //得到前一个任务栈导航列表数据集合
            listGuideMap.remove(position+1);//移除当前任务
            departList.remove(position+1);//移除
        }else{//返回
            idDep = listGuideMap.get(position);//得到任务栈的前一个任务id
            mListGuide.clear();
            mListGuide.addAll(departList.get(position));  //得到前一个任务栈导航列表数据集合
            listGuideMap.remove(position+1);//移除
            departList.remove(position+1);//移除
        }
        --currentPosition;//栈任务数减1
        mListGuideAdapter.notifyDataSetChanged();
        getOrganization(idDep);
    }

    public void getOrganization(String idDep) {
        showLoadingDialog();
        HashMap<String, String> maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(this).getSesstion());
        maps.put("drugCompanyId", SharedPreferenceUtil.getString(this, "enterpriseId", ""));
        maps.put("id", idDep);


        new HttpManager().post(this, Constants.DEPSTRUCT, CompanyDepment.class,
                maps, this,
                false, 1);
    }


    @Override
    public void onSuccess(Result response) {
        //tv.setVisibility(View.GONE);
        empteyll.setVisibility(View.GONE);
        boolean haveDep = false;
        p++;
        if (response != null && response.resultCode == 1) {
            if (response instanceof CompanyDepment) {
                CompanyDepment companyDepment = (CompanyDepment) (response);
                if (null != companyDepment.data && null != companyDepment.data.departments && companyDepment.data
                        .departments.size() > 0) {
                    haveDep = true;
                    list.clear();
                    idDep = companyDepment.data.departments.get(0).id;
                    mParentId = companyDepment.data.departments.get(0).parentId;

                    list.addAll(companyDepment.data.departments);
                    pareid = companyDepment.data.departments.get(0).parentId;

                    adapter.setSize(companyDepment.data.departments.size());
                    adapter.notifyDataSetChanged();
                    if (p == 1) {
                        checkUndefine(companyDepment.data.departments);
                        firstLevelId = companyDepment.data.departments.get(0).id;
                        if (list.size() == 0) {
                            haveDep = false;
                            idDep = "0";
                            pareid = null;
                        }
                    }


                }else if (getContent()==editColleageDep){
                    setTitle(departName);
                } else {
                    mParentId = "";
                    adapter.setSize(0);
                    haveDep = false;
                }
                if (firstLevelId.equals(idDep) || idDep.equals("0")) {
                    tv_des.setText("");
                    tv_des.setVisibility(View.VISIBLE);
                    setTitle("企业通讯录");
                } else {
                    tv_des.setText("关闭");
                    tv_des.setVisibility(View.VISIBLE);
                }
                if (getContent()!=editColleageDep&&null != companyDepment.data &&
                        null != companyDepment.data.users && companyDepment.data.users.size() > 0) {

                    List<CompanyContactListEntity> lists = new ArrayList<>();
                    if (companyDepment.data.users != null) {
                        if (companyDepment.data.users.size() > mPageSize) {
                            mPullIndex = 0;
                            mUserIdList = companyDepment.data.users;
                            List<Integer> integers = mUserIdList.subList(mPullIndex * mPageSize, (mPullIndex + 1) * mPageSize);
                            mPullIndex++;
                            lists = companyContactDao.queryAndSortByUserIds(integers);
                            mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                        } else {
                            lists = companyContactDao.queryAndSortByUserIds(companyDepment.data.users);
                            mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.DISABLED);
                        }
                    }

                    if (lists != null && lists.size() > 0) {
                        if (!haveDep&&getContent()!=editColleageDep) {
                            adapter.setSize(0);
                            list.clear();
                        }

                        List<DepAdminsList> adminlists = CompanyContactDataUtils.getManagerDep(CompanyContactListActivity.this);
                        for (int i = 0; i < adminlists.size(); i++) {
                            if (null != lists.get(0) && adminlists.get(i).orgId.equals(lists.get(0).id)) {
                                //tv.setVisibility(View.VISIBLE);
                                AddressList.deptId = adminlists.get(i).orgId;
                                break;
                            }
                        }

                        list.addAll(lists);
                        adapter.notifyDataSetChanged();
                    } else {
                        if (!haveDep&&getContent()!=editColleageDep) {
                            isEmpty = true;
                            empteyll.setVisibility(View.VISIBLE);
                        }
                    }

                } else {
                    if (!haveDep &&getContent()!=editColleageDep) {
                        isEmpty = true;
                        empteyll.setVisibility(View.VISIBLE);
                    }
                    mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.DISABLED);
                }

            }
        }

        if (adapter.getCount() == 0) {
            layout_line.setVisibility(View.GONE);
        } else {
            layout_line.setVisibility(View.VISIBLE);
        }
        closeLoadingDialog();
    }


    public void checkUndefine(ArrayList<CompanyDepment.Data.Depaments> departments) {
        for (CompanyDepment.Data.Depaments depament : departments) {
            if (!TextUtils.isEmpty(depament.name) && depament.name.equals("未分配")) {
                List<CompanyContactListEntity> entities = companyContactDao.queryByDepID(depament.id);
                if (entities == null || entities.size() == 0) {
                    list.remove(depament);
                }
                break;
            }

        }
    }

    public void setTitles(String name) {
        List<CompanyContactListEntity> entities
                = companyContactDao.queryByParentId(pareid);
 /*   if (entities.size()>0){
        setTitle(entities.get(0).department);
    }*/
        setTitle(name);

    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        closeLoadingDialog();
    }

    public void back() {
      /*  CompanyContactListEntity.CompanyContactEntity newItem =  maps.get(ids);
        getListData(newItem);
        listsTitle.remove(newItem);*/
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        return false;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backtofront();
        //		this.mApplication.getActivityManager().finishActivity(this.getClass());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 100:
                if (!idDep.equals("-1")) {
                    if (TextUtils.isEmpty(mParentId)) {
                        GetAllDoctor.getInstance().getPeople();
                        getOrganization(idDep);
                    } else {
                        idDep = mParentId;
                        GetAllDoctor.getInstance().getPeople();
                        getOrganization(idDep);
                    }
                }
                break;
            default:
              //  String companyId = com.dachen.dgroupdoctorcompany.utils.UserInfo.getInstance(this).getCompanyId();
                if (resultCode == 300) {

                }  else if (!idDep.equals("-1")&&requestCode!=300/*&&!(null!=pareid&&pareid.equals(AddressList.deptId))*/) {
                    GetAllDoctor.getInstance().getPeople();
                    if (departmentId.size()==0) {
                        getOrganization(AddressList.deptId);
                    }else {
                        getOrganization(departmentId.get(departmentId.size()-1).id);
                    }
                }
                break;
        }
    }

    /**
     * zxy  导航列表条目点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCp_listguilde.setLastPosition();
        if (position == 0) {
            finish();
            listGuideMap.clear();
            listGuideMap = null;
            mListGuide.clear();
            mListGuide = null;
            departList.clear();
            departList = null;
            return;
        }
        if (position == mListGuide.size() - 1) {
            return;
        }

        //返回后到上一级目录
          /* // CompanyDepment.Data.Depaments c1 = (CompanyDepment.Data.Depaments) (listGuideMap.get(position));
            //setDepartmen("",c1.id);
            idDep = listGuideMap.get(position);
            int forCount = currentPosition - position - 1;
            for (int i = 0; i < forCount; i++) {
                listGuideMap.remove(--currentPosition);
                mListGuide.remove(mListGuide.get(currentPosition));
            }*/
        int forCount = oldPosition - position;
        Log.d("zxy", "onItemClick: oldPosition = "+oldPosition+", position = "+position);
        for (int i = 0; i < forCount; i++) {
            Log.d("zxy", "onItemClick: remove");
            mListGuide.remove(oldPosition -i);
        }
        departList.put(currentPosition,copyToNewList(mListGuide));
        listGuideMap.put(currentPosition++, listGuideMap.get(position));
        idDep = listGuideMap.get(position);
        Log.d("zxy", "onItemClick: currentPosition = "+currentPosition+", idDep = "+listGuideMap.get(position)+", mListGuide"+mListGuide);
        mListGuideAdapter.notifyDataSetChanged();
        getOrganization(idDep);
        oldPosition = position;
        //更新数据

    }

    public  int  getContent(){
        return 0;
    };


    /**
     * 将集合拷贝到一个新集合中
     */
    public ArrayList<String> copyToNewList(ArrayList<String> list){
        ArrayList<String > arrayList = new ArrayList<>();
        arrayList.addAll(list);
        return arrayList;
    }
}
