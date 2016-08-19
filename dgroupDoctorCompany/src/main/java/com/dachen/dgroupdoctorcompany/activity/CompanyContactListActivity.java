package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dachen.common.utils.Logger;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.CompanyContactListAdapter;
import com.dachen.dgroupdoctorcompany.adapter.CompanyListGuide;
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

import butterknife.ButterKnife;

/**
 * Created by Burt on 2016/2/26.
 */
public class CompanyContactListActivity extends BaseActivity implements HttpManager.OnHttpListener, AdapterView
        .OnItemLongClickListener, AdapterView.OnItemClickListener {
    CompanyContactListAdapter adapter;
    NoScrollerListView listview;
    ArrayList<BaseSearch> list;
    public String ids;
    String title = "";
    public String idDep = "0";
    HashMap<String, CompanysTitle> listsTitle;
    CompanyContactDao companyContactDao;
    RoleDao roleDao;
    List<BaseSearch> listsHorizon;
    String pareid;
    ArrayList<CompanyContactListEntity> groupUsers;
    boolean manager;
    Button btn_addpeople;
    TextView tv;
    LinearLayout layout_line;
    TextView tv_des;
    RelativeLayout empteyll;
    String firstLevelId = "";
    int frontOrBack = 0;
    int p;
    private PullToRefreshScrollView mPullToRefreshScrollView;
    private ArrayList<Integer> mUserIdList;
    private int mPullIndex;
    private int mPageSize = 50;
    private String mParentId;
    /*-----------------------------------------zxy start-----------------------------------------*/
    private HorizontalListView mCp_listguilde;
    private List<String> mListGuide;
    private CompanyListGuide mListGuideAdapter;
    Map<Integer, String> listGuideMap = new LinkedHashMap<>();
    int currentPosition = 0;
    boolean isEmpty = false;
    /*-----------------------------------------zxy end -----------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companycontactlist);
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
        btn_addpeople = (Button) findViewById(R.id.btn_addpeople);
        btn_addpeople.setOnClickListener(this);
        //有管理权限的管理者跳转过来，管理人员
        RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.ll_sub);
        ViewStub stub = (ViewStub) findViewById(R.id.vstub_title);
        tv_des = (TextView) findViewById(R.id.tv_des);
        View view = stub.inflate(this, R.layout.stub_viewtext, rl);
        tv = (TextView) view.findViewById(R.id.tv_search);
        /*-----------------------------------------zxy start-----------------------------------------*/
        mCp_listguilde = (HorizontalListView) findViewById(R.id.cp_listguilde);
        mListGuide = new ArrayList<>();
        mListGuide.add("通讯录");
        listGuideMap.put(currentPosition++, "通讯录");
        mListGuideAdapter = new CompanyListGuide(this, mListGuide);
        mCp_listguilde.setOnItemClickListener(this);
        mCp_listguilde.setAdapter(mListGuideAdapter);
        /*-----------------------------------------zxy end -----------------------------------------*/
        tv.setOnClickListener(this);
        tv.setText("管理");
        tv.setVisibility(View.GONE);
        tv_des.setOnClickListener(this);
        idDep = AddressList.deptId;
        if (!idDep.equals("-1")) {
            companyContactDao.queryAll();
            manager = true;

        } else {
            idDep = "0";
            getOrganization();
        }

        ButterKnife.bind(this);
        frontOrBack = 0;
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
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                BaseSearch contact = adapter.getItem(position);
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
                Logger.d("onItemClick", "onItemClick-----------" + System.currentTimeMillis());
                BaseSearch contact = adapter.getItem(position);
                CompanyContactListEntity c2 = null;
                CompanyDepment.Data.Depaments c1 = null;

                if (contact instanceof CompanyContactListEntity) {
                    c2 = (CompanyContactListEntity) (contact);
                    Intent intent = new Intent(CompanyContactListActivity.this, ColleagueDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("peopledes", c2);
                    if (manager) {
                        intent.putExtra("manager", "manager");
                    }
                    intent.putExtra("peopledes", bundle);
                    startActivity(intent);
                } else if (contact instanceof CompanyDepment.Data.Depaments) {
                    c1 = (CompanyDepment.Data.Depaments) (contact);
                    CompanysTitle title = new CompanysTitle();
                    title.id = c1.parentId;
                    title.parentDept = c1.name;
                    listsTitle.put(c1.id, title);
                    setTitles(c1.name);
                    /*-----------------------------------------zxy start-----------------------------------------*/
                    mListGuide.add(c1.name);
                    listGuideMap.put(currentPosition++, c1.id);
                    /*-----------------------------------------zxy end -----------------------------------------*/
                    frontOrBack = 1;
                    if (c1 != null) {
                        idDep = c1.id;
                        /*-----------------------------------------zxy start-----------------------------------------*/
                        mListGuideAdapter.notifyDataSetChanged();
                        /*-----------------------------------------zxy end -----------------------------------------*/
                        getOrganization();
                    }

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

    @Override
    protected void onResume() {
        super.onResume();
        if (manager && !idDep.equals("-1")) {
            getOrganization();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        //super.onClick(v);
        switch (v.getId()) {

            case R.id.btn_addpeople:
                intent = new Intent(this, FriendsContactsActivity.class);
                startActivityForResult(intent, 200);
                break;
            case R.id.tv_search:
                intent = new Intent(this, EidtColleagueActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.tv_des:
                finish();
                break;
        }

    }

    void backtofront() {
        /*-----------------------------------------zxy start-----------------------------------------*/
        if (mListGuide.size() - 1 >= 0) {       //列表导航数据更新
            mListGuide.remove(mListGuide.get(mListGuide.size() - 1));
            mListGuideAdapter.notifyDataSetChanged();
        }
        if (isEmpty) {      //空页面时返回
            if (null != listsTitle.get(pareid)) {
                setTitle(listsTitle.get(pareid).parentDept);
            } else {
                setTitle("企业通讯录");
            }
            idDep = pareid;
        }
        /*-----------------------------------------zxy end -----------------------------------------*/
        CompanyDepment.Data.Depaments entity;
        if (list != null && list.size() > 0 && list.get(0) instanceof CompanyDepment.Data.Depaments && !isEmpty) {
            entity = (CompanyDepment.Data.Depaments) list.get(0);
            // idDep = entity.parentId;
            if (null != listsTitle.get(entity.parentId)) {
                idDep = listsTitle.get(entity.parentId).id;
                setTitle(listsTitle.get(entity.parentId).parentDept);
            } else {
                idDep = null;
            }
        } else {
            if (null != listsTitle.get(pareid)) {
                setTitle(listsTitle.get(pareid).parentDept);
            } else {
                setTitle("企业通讯录");
            }
            idDep = pareid;
        }
        if (idDep == null) {
            idDep = "0";
            finish();
            return;
        }
        frontOrBack = 2;
        isEmpty = false;
        getOrganization();
    }

    private void getOrganization() {
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
        tv.setVisibility(View.GONE);
        empteyll.setVisibility(View.GONE);
        boolean haveDep = false;
        boolean havecolleage = false;
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
                if (null != companyDepment.data && null != companyDepment.data.users && companyDepment.data.users
                        .size() > 0) {
                    havecolleage = true;
                    List<CompanyContactListEntity> lists = new ArrayList<>();
                    if (companyDepment.data.users != null) {
                        if (companyDepment.data.users.size() > mPageSize) {
                            mPullIndex = 0;
                            mUserIdList = companyDepment.data.users;
                            List<Integer> integers = mUserIdList.subList(mPullIndex * mPageSize, (mPullIndex + 1) *
                                    mPageSize);
                            mPullIndex++;
                            lists = companyContactDao.queryAndSortByUserIds(integers);
                            mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                        } else {
                            lists = companyContactDao.queryAndSortByUserIds(companyDepment.data.users);
                            mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.DISABLED);
                        }
                    }

                    if (lists != null && lists.size() > 0) {
                        if (!haveDep) {
                            adapter.setSize(0);
                            list.clear();
                        }

                        List<DepAdminsList> adminlists = CompanyContactDataUtils.getManagerDep
                                (CompanyContactListActivity.this);
                        for (int i = 0; i < adminlists.size(); i++) {
                            if (null != lists.get(0) && adminlists.get(i).orgId.equals(lists.get(0).id)) {
                                tv.setVisibility(View.VISIBLE);
                                AddressList.deptId = adminlists.get(i).orgId;
                                break;
                            }
                        }

                        list.addAll(lists);
                        adapter.notifyDataSetChanged();
                    } else {
                        if (!haveDep) {
                            isEmpty = true;
                            empteyll.setVisibility(View.VISIBLE);
                        }
                    }

                } else {
                    /*if (!haveDep && !idDep.equals("0")) {
                        Intent intent = new Intent(CompanyContactListActivity.this,
                                CompanyContactListNoPeopleActivity.class);
                        if (null != listsTitle.get(idDep) && !TextUtils.isEmpty(listsTitle.get(idDep).parentDept)) {
                            intent.putExtra("dept", listsTitle.get(idDep).parentDept);
                        }
                        startActivity(intent);
                        toNoPeople(); //先保留空页面
                    } else*/
                    if (!haveDep /*&& idDep.equals("0")*/) {
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
        Logger.d("onItemClick", "success-----------" + System.currentTimeMillis());
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
        switch (requestCode) {
            case 100:
                if (!idDep.equals("-1")) {
                    if (TextUtils.isEmpty(mParentId)) {
                        GetAllDoctor.getInstance().getPeople();
                        getOrganization();
                    } else {
                        idDep = mParentId;
                        GetAllDoctor.getInstance().getPeople();
                        getOrganization();
                    }
                }
                break;
            default:
                if (resultCode == 300) {

                } else if (!idDep.equals("-1")) {
                    GetAllDoctor.getInstance().getPeople();
                    getOrganization();
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
            return;
        }
        if (position == mListGuide.size() - 1) {
            return;
        }
        idDep = listGuideMap.get(position);
        //更新数据
        for (int i = 0; i < currentPosition - position - 1; i++) {
            listGuideMap.remove(--currentPosition);
            mListGuide.remove(mListGuide.get(currentPosition));
        }
        mListGuideAdapter.notifyDataSetChanged();

        getOrganization();
    }

}
