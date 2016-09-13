package com.dachen.dgroupdoctorcompany.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.CompanyContactListAdapter;
import com.dachen.dgroupdoctorcompany.app.CompanyApplication;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.RoleDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.DepAdminsList;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.entity.CompanyDepment;
import com.dachen.dgroupdoctorcompany.fragment.AddressList;
import com.dachen.dgroupdoctorcompany.utils.CommonUitls;
import com.dachen.dgroupdoctorcompany.utils.DataUtils.CompanyContactDataUtils;
import com.dachen.dgroupdoctorcompany.utils.GetAllDoctor;
import com.dachen.dgroupdoctorcompany.views.GuiderHListView;
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
import java.util.Iterator;
import java.util.List;
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
     GuiderHListView mCp_listguilde;
    boolean isEmpty = false;
    public String departName="";
    public String baseDepartName="";
    String parentId;
    View layoutView;
    Activity context;
    private  final  int LISTVIEWITEMCLICK = 1;
    private  final  int GUIDERITEMCLICK = 2;
    private  final  int BACKCLICK = 3;
    private int from;
    String  depName;
    private int currentPosition = 0;
    private boolean isOpenHttp;
    private boolean isHttpClose;
    private boolean isAddPeople = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutView = View.inflate(this, R.layout.activity_companycontactlist, null);
        setContentView(layoutView);
        GetAllDoctor.changeContact.clear();
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
        setBaseDepartName("企业通讯录");
        setTitle(baseDepartName);
        manager = false;
        //有管理权限的管理者跳转过来，管理人员
      /* RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.ll_sub);
        ViewStub stub = (ViewStub) findViewById(R.id.vstub_title);*/
        tv_des = (TextView) findViewById(R.id.tv_des);
        //View view = stub.inflate(this, R.layout.stub_viewtext, rl);
        mCp_listguilde = (GuiderHListView) findViewById(R.id.cp_listguilde);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {//如果部门存在,显示部门,不存在直接显示公司名
            departName = extras.getString("depName");//得到部门名字
        }else {
            departName = SharedPreferenceUtil.getString(CompanyApplication.getInstance(), "enterpriseName", "");
        }

        mCp_listguilde.setOnItemClickListener(this);
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
        setDepartmen(baseDepartName,idDep);
        mCp_listguilde.addTask(departName,idDep);
        mCp_listguilde.setAdapter();
       // listGuideMap.put(currentPosition++, idDep);
        ButterKnife.bind(this);
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
                } else if (adapter.getItem(position) instanceof CompanyDepment.Data.Depaments) {
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
        setShowContent();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            mCp_listguilde.setSelection(mCp_listguilde.getCurrentPosition());
        }
    }
    public void getDepment(BaseSearch contact ,boolean clickRadio){
        CompanyDepment.Data.Depaments  c1 = (CompanyDepment.Data.Depaments) (contact);
        from = LISTVIEWITEMCLICK;
        departName = c1.name;
        parentId = c1.parentId;
        setDepartmen(c1.name, c1.id);
        if (c1 != null) {
            idDep = c1.id;
            getOrganization(idDep);
            adapter.notifyDataSetChanged();
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
            isAddPeople = false;
        if (GetAllDoctor.changeContact!=null&&GetAllDoctor.changeContact.size()>0){
            Iterator<CompanyContactListEntity> entityIterator =GetAllDoctor.changeContact.iterator();
            while (entityIterator.hasNext()){
                CompanyContactListEntity entitye =entityIterator.next();
                boolean find = false;
                for (int i=0;i<list.size();i++){
                    if(list.get(i) instanceof  CompanyContactListEntity){
                        CompanyContactListEntity entity = (CompanyContactListEntity) list.get(i);
                        if ((entity.userId+"").equals(entitye.userId)

                                 ){
                            if ((entity.id+"").equals(entitye.id)){
                                if (entity.status.equals("1")){
                                    list.set(i,entitye);
                                    find = true;
                                }else {
                                    list.remove(entitye);
                                    find = true;
                                }
                                break;
                            }else {
                                if (entity.status.equals("1")){
                                    list.remove(entity);
                                    find = true;
                                }else {
                                    list.remove(entitye);
                                    find = true;
                                }
                                break;
                            }

                        }
                    }
                }
            }
            adapter.notifyDataSetChanged();
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
        int position = mCp_listguilde.getCurrentPosition()-1;//当前任务栈id数
        if (position == 0) {   //只剩联系人了,直接返回,  清空数据释放缓存
            finish();
            return;
        }if (position == 1) {  //公司页面
            setTitle(baseDepartName);
            idDep =  mCp_listguilde.reMoveTaskId();
        }else{//返回
            idDep =  mCp_listguilde.reMoveTaskId();
        }
        from = BACKCLICK;
        departName = mCp_listguilde.getLastDerpartName(position);
        getOrganization(idDep);
    }

    public void getOrganization (String idDep) {
        showLoadingDialog();
        isOpenHttp = true;
        HashMap<String, String> maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(this).getSesstion());
        maps.put("drugCompanyId", SharedPreferenceUtil.getString(this, "enterpriseId", ""));
        maps.put("orgId", idDep);
        maps.put("hideUnassign", "1");

        new HttpManager().post(this, Constants.DEPSTRUCT, CompanyDepment.class,
                maps, this,
                false, 1);
    }


    @Override
    public void onSuccess(Result response) {
/*        isOpenHttp = false;
        if (isHttpClose) {
            isHttpClose= false;
            return;
        }*/
        //tv.setVisibility(View.GONE);
        empteyll.setVisibility(View.GONE);
        if (!isAddPeople) { //如果添加人员不更新
            upDataGuiderList();
        }
        isAddPeople = false;
        setTitle(departName);
        mCp_listguilde.setOldPosition();
        mCp_listguilde.notifyDataSetChanged();
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
                    checkUndefine(companyDepment.data.departments);
                    adapter.setSize(companyDepment.data.departments.size());
                    adapter.notifyDataSetChanged();
                    if (p == 1) {
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
                    //tv_des.setText("");
                    //tv_des.setVisibility(View.VISIBLE);
                    setTitle(baseDepartName);
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
                    } else {//显示空界面
                        if (!haveDep&&getContent()!=editColleageDep) {
                            isEmpty = true;
                            empteyll.setVisibility(View.VISIBLE);
                            empteyll.setOnClickListener(this);
                        }
                    }

                } else {
                    if (!haveDep &&getContent()!=editColleageDep) {
                        list.clear();
                        adapter.notifyDataSetChanged();
                        isEmpty = true;
                        empteyll.setVisibility(View.VISIBLE);
                        empteyll.setOnClickListener(this);
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

    void upDataGuiderList() {
        switch (from) {
            case LISTVIEWITEMCLICK://ListView点击请求数据成功
                mCp_listguilde.addTask(departName,idDep);
                break;
            case GUIDERITEMCLICK:   //导航listView
                mCp_listguilde.addBackTask(currentPosition);
                departName= mCp_listguilde.getLastDerpartName(0);
                break;
            case BACKCLICK:         //返回
                mCp_listguilde.reMoveTask();
                departName= mCp_listguilde.getLastDerpartName(0);
                break;
        }
        from = 0;
    }


    public ArrayList<CompanyDepment.Data.Depaments> checkUndefine(ArrayList<CompanyDepment.Data.Depaments> departments) {
        for (CompanyDepment.Data.Depaments depament : departments) {
            if (!TextUtils.isEmpty(depament.name) && depament.name.equals("未分配")) {
                List<CompanyContactListEntity> entities = companyContactDao.queryByDepID(depament.id);
                if (entities == null || entities.size() == 0) {
                    list.remove(depament);
                }else {
                    int p = -1;
                    for (int i =0;i<departments.size();i++){
                        if (!TextUtils.isEmpty( departments.get(i).type)&&
                                departments.get(i).type.equals("3")){
                            p = i;
                            break;
                        }
                    }
                    if (p!=-1){
                        CompanyDepment.Data.Depaments depamen1 = departments.get(p);
                        CompanyDepment.Data.Depaments depamen2 = departments.get(departments.size()-1);
                        CompanyDepment d= new CompanyDepment();
                        CompanyDepment.Data data = d.new Data();

                        CompanyDepment.Data.Depaments depaments3 = data.new Depaments();
                        depaments3 = depamen1;
                        departments.set(p,depamen2);
                        departments.set(departments.size()-1, depaments3);
                        list.clear();
                        list.addAll(departments);
                    }
                }
                break;
            }
        }
        return departments;
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
        backtofront();
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
                if (resultCode == 300) {

                }  else if (!idDep.equals("-1")&&requestCode!=300/*&&!(null!=pareid&&pareid.equals(AddressList.deptId))*/) {
                    GetAllDoctor.getInstance().getPeople();
                    if (departmentId.size()==0) {
                        getOrganization(AddressList.deptId);
                    }else {//添加成员activity返回
                        if (mCp_listguilde!= null&&mCp_listguilde.getAdapter().getCount()>=1) {
                            GuiderHListView.Guider item = (GuiderHListView.Guider) mCp_listguilde.getAdapter().getItem
                                    (mCp_listguilde.getAdapter().getCount() - 1);
                            getOrganization(item.id);
                        }
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
        if (position == mCp_listguilde.getAdapter().getCount() - 1) {
            return;
        }
        currentPosition = position;
       // idDep = mCp_listguilde.addBackTaskId(currentPosition);
        from = GUIDERITEMCLICK;
        idDep = mCp_listguilde.getBackTaskId(position);
        getOrganization(idDep);

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
    public void setShowContent(){
        RelativeLayout rl_addpeople = (RelativeLayout) findViewById(R.id.rl_addpeople);
        if (getContent()==0||getContent()==editColleageDep){
            rl_addpeople.setVisibility(View.GONE);
        }else if (getContent()==isManager){
            rl_addpeople.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        mCp_listguilde.clearData();
        super.onDestroy();
    }
    public void setBaseDepartName(String baseDepartName) {
        this.baseDepartName = baseDepartName;
    }
}
