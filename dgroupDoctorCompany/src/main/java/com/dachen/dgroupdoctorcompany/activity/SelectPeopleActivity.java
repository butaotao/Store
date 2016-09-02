package com.dachen.dgroupdoctorcompany.activity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dachen.common.async.SimpleResultListener;
import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.CircleCreateGroupAdapter;
import com.dachen.dgroupdoctorcompany.adapter.CompanySelectPeopleListAdapter;
import com.dachen.dgroupdoctorcompany.app.CompanyApplication;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.archive.ArchiveUtils;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.RoleDao;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.entity.CompanyDepment;
import com.dachen.dgroupdoctorcompany.entity.Void;
import com.dachen.dgroupdoctorcompany.fragment.AddressList;
import com.dachen.dgroupdoctorcompany.im.activity.RepresentGroupChatActivity;
import com.dachen.dgroupdoctorcompany.im.events.AddGroupUserEvent;
import com.dachen.dgroupdoctorcompany.im.utils.ChatActivityUtilsV2;
import com.dachen.dgroupdoctorcompany.utils.CallIntent;
import com.dachen.dgroupdoctorcompany.utils.CommonUitls;
import com.dachen.dgroupdoctorcompany.utils.CompareDatalogic;
import com.dachen.dgroupdoctorcompany.utils.GetAllDoctor;
import com.dachen.dgroupdoctorcompany.views.GuiderHListView;
import com.dachen.dgroupdoctorcompany.views.HorizontalListView;
import com.dachen.imsdk.adapter.MsgMenuAdapter;
import com.dachen.imsdk.archive.entity.ArchiveItem;
import com.dachen.imsdk.consts.SessionType;
import com.dachen.imsdk.db.dao.ChatGroupDao;
import com.dachen.imsdk.db.po.ChatMessagePo;
import com.dachen.imsdk.entity.GroupInfo2Bean.Data;
import com.dachen.imsdk.net.MessageSenderV2;
import com.dachen.imsdk.net.SessionGroup;
import com.dachen.imsdk.net.SessionGroup.SessionGroupCallback;
import com.dachen.imsdk.service.ImRequestManager;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.view.CustomDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import de.greenrobot1.event.EventBus;

/**
 * Created by Burt on 2016/2/26.
 */
public class SelectPeopleActivity extends BaseActivity implements HttpManager.OnHttpListener, AdapterView.OnItemLongClickListener, View.OnClickListener, AdapterView.OnItemClickListener {
    public static final String INTENT_EXTRA_GROUP_USERS = "groupUsers";
    public static final String INTENT_EXTRA_GROUP_TYPE = "groupType";
    public static final String INTENT_EXTRA_GROUP_ID = "groupId";
    public static final int REQUEST_SEARCH = 1001;
   public Data data;
    CompanySelectPeopleListAdapter adapter;//R.layout.adapter_companycontactlist
    ListView listview;
    ArrayList<BaseSearch> list;
    public String ids;
    String title = "";
    public String idDep = "0";
    HashMap<String, CompanyDepment.Data.Depaments> listsTitle;
    CompanyContactDao companyContactDao;
    RoleDao roleDao;
    HorizontalListView addlistview;
    CircleCreateGroupAdapter addAdapter;
    public static List<BaseSearch> listsHorizon;
    RadioButton btn_radio_addall;
    boolean selectall;
    SessionGroup groupTool;
    String pareid;
    ArrayList<CompanyContactListEntity> groupUsers;
    private String groupId;
    public String partname;
    Button btn_add;
    private boolean inWork;

    private PullToRefreshScrollView mPullToRefreshScrollView;
    private ArrayList<Integer> mUserIdList;
    private int mPullIndex;
    private int mPageSize = 50;
    private TextView mNoContactsTips;
    private TextView mSearch;
    private LinearLayout layout_search;
    private boolean mShare;
    private String msgId;
    ArchiveItem mItem;
    private String groupIds;
    private GuiderHListView mListGuider;
    private TextView mTvDes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectpeople);
        mShare = getIntent().getBooleanExtra("share", false);
        msgId = getIntent().getStringExtra(MsgMenuAdapter.INTENT_EXTRA_MSG_ID);
        mItem = (ArchiveItem) getIntent().getSerializableExtra(ArchiveUtils.INTENT_KEY_ARCHIVE_ITEM);
        mPullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.refresh_scroll_view);
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.DISABLED);
        groupIds = getIntent().getStringExtra(MsgMenuAdapter.INTENT_EXTRA_GROUP_ID);
        layout_search = getViewById(R.id.layout_search);
        mSearch = getViewById(R.id.et_search);
        btn_add = (Button) findViewById(R.id.btn_add);
        mTvDes = (TextView) findViewById(R.id.tv_des);
        mTvDes.setVisibility(View.VISIBLE);
        mTvDes.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        layout_search.setOnClickListener(this);
        groupTool = new SessionGroup(this);
        listsTitle = new HashMap<>();
        groupTool.setCallback(callback);
        listview = (ListView) findViewById(R.id.listview);
        listview.setEmptyView(findViewById(R.id.empty_container));
        mNoContactsTips = (TextView) findViewById(R.id.tv_info);
        companyContactDao = new CompanyContactDao(SelectPeopleActivity.this);
        roleDao = new RoleDao(SelectPeopleActivity.this);
        setTitle("选择联系人");
        idDep = "0";
        findViewById(R.id.rl_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                backtofront();
            }
        });
        listsHorizon = CommonUitls.getListsHorizon();
        listsHorizon.clear();
        list = new ArrayList<>();
        groupUsers = (ArrayList<CompanyContactListEntity>) getIntent().getSerializableExtra(INTENT_EXTRA_GROUP_USERS);
        if (groupUsers == null) {
            groupUsers = new ArrayList<>();
        }
        groupId = getIntent().getStringExtra(INTENT_EXTRA_GROUP_ID);
        ButterKnife.bind(this);
        //listsHorizon = new ArrayList<>();

        GetAllDoctor.getInstance().getPeople();

        // list.addAll(companyContactDao.queryAll());
        btn_radio_addall = (RadioButton) findViewById(R.id.btn_radio_addall);
        btn_radio_addall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectall();
            }
        });
        findViewById(R.id.selectall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectall();
            }
        });
        addAdapter = new CircleCreateGroupAdapter(this, listsHorizon);
        addlistview = getViewById(R.id.addlistview);
        addlistview.setAdapter(addAdapter);
        addlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaseSearch  baseSearch= (BaseSearch) addAdapter.getItem(position);
                listsHorizon.remove(baseSearch);
                int baseSearchlist=0;
                if (list!=null&&list.size()>0){
                     baseSearchlist=list.indexOf(baseSearch);
                }
                if (baseSearchlist>0){
                    CompanyContactListEntity entity = (CompanyContactListEntity) list.get(baseSearchlist);
                    entity.select = false;
                    list.set(baseSearchlist,entity);
                }

                addAdapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
            }
        });

        adapter = new CompanySelectPeopleListAdapter(this, R.layout.adapter_selectpeoplelist, list, 0);
        listview.setAdapter(adapter);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                BaseSearch contact = adapter.getItem(position);
                CompanyContactListEntity c2 = null;
                CompanyDepment.Data.Depaments c1 = null;
                if (contact instanceof CompanyContactListEntity) {
                    c2 = (CompanyContactListEntity) (contact);
                    final CustomDialog dialog = new CustomDialog(SelectPeopleActivity.this);
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
                BaseSearch contact = adapter.getItem(position);
                CompanyContactListEntity c2 = null;
                CompanyDepment.Data.Depaments c1 = null;
                if (contact instanceof CompanyContactListEntity) {
                    c2 = (CompanyContactListEntity) (contact);
                    if (c2.select) {
                        c2.select = false;
                        listsHorizon.remove(c2);
                    } else {
                        c2.select = true;
                        if (!groupUsers.contains(c2) && !c2.userId.equals(SharedPreferenceUtil.getString(SelectPeopleActivity.this, "id", ""))) {
                            CommonUitls.addCompanyContactListEntity(c2);
                        }
                    }
                    addAdapter.notifyDataSetChanged();
                    list.set(position, c2);
                    adapter.notifyDataSetChanged();
                    btn_add.setText("开始(" + listsHorizon.size() + ")");
                } else if (contact instanceof CompanyDepment.Data.Depaments) {
                    c1 = (CompanyDepment.Data.Depaments) (contact);
                    //   listsTitle.put(c1.id, c1.parentId);
                    listsTitle.put(c1.id, c1);
                    if (c1 != null) {
                        mListGuider.addTask(c1.name,c1.id);
                        mListGuider.setOldPosition();
                        mListGuider.notifyDataSetChanged();
                        //String title = mListGuider.getListGuide().get(mListGuider.getListGuide().size()-1);
                        //setTitle(title);
                        idDep = c1.id;
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
                        List<Integer> integers = mUserIdList.subList(mPullIndex * mPageSize, (mPullIndex + 1) * mPageSize);
                        new LoadContactsTask(true).execute(integers);
                        mPullIndex++;
                    } else {
                        List<Integer> integers = mUserIdList.subList(mPullIndex * mPageSize, mUserIdList.size());
                        new LoadContactsTask(true).execute(integers);
                        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.DISABLED);
                    }
                }
                mPullToRefreshScrollView.onRefreshComplete();
            }
        });
        /*-----------------------------------------zxy start-----------------------------------------*/
        mListGuider = (GuiderHListView) findViewById(R.id.org_listguilde);
        mListGuider.setOnItemClickListener(this);
        String companyName = SharedPreferenceUtil.getString(CompanyApplication.getInstance(), "enterpriseName", "");
        mListGuider.addTask(companyName,idDep);
        mListGuider.setAdapter();
        mListGuider.notifyDataSetChanged();
        /*-----------------------------------------zxy end -----------------------------------------*/

        getOrganization();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            mListGuider.setSelection(mListGuider.getCurrentPosition());
        }
    }
    //水平导航条目点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == mListGuider.getListGuide().size() - 1) {
            return;
        }
        idDep = mListGuider.addBackTask(position);
        mListGuider.notifyDataSetChanged();
        //String title = mListGuider.getListGuide().get(mListGuider.getListGuide().size()-1);
       // setTitle(title);
        getOrganization();
    }

    private List<CompanyContactListEntity> addHaveAdd(List<CompanyContactListEntity> departments) {
        if (departments != null && departments.size() > 0 && groupUsers != null && groupUsers.size() > 0)
            for (int i = 0; i < departments.size(); i++) {
                for (int j = 0; j < groupUsers.size(); j++) {
                    if (groupUsers.get(j).equals(departments.get(i))) {
                        CompanyContactListEntity entity = departments.get(i);
                        entity.haveSelect = true;
                        departments.set(i, entity);
                        break;
                    }

                }
            }
        return departments;

    }

    void backtofront() {
       /* if (list != null && list.size() > 0 && list.get(0) instanceof CompanyDepment.Data.Depaments) {
            CompanyDepment.Data.Depaments entity = (CompanyDepment.Data.Depaments) list.get(0);
            // idDep = entity.parentId;
            if (null != listsTitle.get(entity.parentId) && null != listsTitle.get(entity.parentId).parentId) {
                idDep = listsTitle.get(entity.parentId).parentId;
            } else {
                idDep = null;
                setTitle("选择联系人");
            }

            // setTitle(entity.name);
        } else {
            idDep = pareid;
            setTitle("选择联系人");
        }
        if (idDep == null) {
            idDep = "0";
            finish();
            return;
        }*/
        int position = mListGuider.getCurrentPosition()-1;//当前任务栈id数
        if (position == 0) {   //只剩联系人了,直接返回,  清空数据释放缓存
            finish();
            return;
        }if (position == 1) {  //公司页面
            //setTitle("企业通讯录");
            idDep = mListGuider.reMoveTask();
        }else{//返回
            idDep = mListGuider.reMoveTask();
        }
        //String derpartName = mListGuider.getLastDerpartName(position);
        //setTitle(derpartName);
        mListGuider.setOldPosition();
        mListGuider.notifyDataSetChanged();
        getOrganization();
    }

    void selectall() {
        boolean ispeople = false;
        if (list != null && list.size() > 0) {
            if (selectall) {
                for (int i = 0; i < list.size(); i++) {
                    BaseSearch search = list.get(i);
                    if (search instanceof CompanyContactListEntity) {
                        CompanyContactListEntity entity = (CompanyContactListEntity) list.get(i);
                        entity.select = false;
                        selectall = false;
                        list.set(i, entity);
                        ispeople = true;
                    }
                }
                if (ispeople) {
                    listsHorizon.removeAll(list);
                    btn_radio_addall.setSelected(false);
                }

            } else {
                for (int i = 0; i < list.size(); i++) {
                    BaseSearch search = list.get(i);
                    if (search instanceof CompanyContactListEntity) {
                        CompanyContactListEntity entity = (CompanyContactListEntity) list.get(i);
                        entity.select = true;
                        selectall = true;
                        list.set(i, entity);
                        ispeople = true;
                        if (!groupUsers.contains(entity) && !entity.userId.equals(SharedPreferenceUtil.getString(SelectPeopleActivity.this, "id", ""))) {
                            CommonUitls.addCompanyContactListEntity(entity);
                        }

                    }

                }
                if (ispeople) {
                    // listsHorizon.addAll(list);
                    btn_radio_addall.setSelected(true);
                }
            }
            for (int i = 0; i < list.size(); i++) {
                BaseSearch search = list.get(i);
                if (search instanceof CompanyContactListEntity) {
                    CompanyContactListEntity entity = (CompanyContactListEntity) list.get(i);
                    if (entity.haveSelect) {
                        entity.haveSelect = true;
                        list.set(i, entity);
                    }
                }

            }
            addAdapter.notifyDataSetChanged();
            adapter.notifyDataSetChanged();
        }
        ispeople = false;
    }

    @Override
    public void onClick(View v) {
       /* if (v.getId()!=R.id.rl_back){
            super.onClick(v);
        }*/
        switch (v.getId()) {
            case R.id.btn_add:
                if (inWork) return;
                inWork = true;
                if (listsHorizon.size() == 0) {
                    ToastUtil.showToast(this, "您未选择任何人");
                    return;
                }
//                CallIntent.getSelectData.getData(listsHorizon);
                int groupType = getIntent().getIntExtra(INTENT_EXTRA_GROUP_TYPE, 0);
                showLoadingDialog();
                    /*groupTool.setCallback(new SessionGroupCallback() {

                        @Override
                        public void onGroupInfo(Data data, int what) {
                            CallIntent.startMainActivity(SelectPeopleActivity.this);
                        }

                        @Override
                        public void onGroupInfoFailed(String msg) {

                        }
                    });*/
                if (groupUsers.size() == 0)
                    groupTool.createGroup(getIdsList(false), "10");
                else {
                    if (groupType == SessionType.session_double) {
                        groupTool.createGroup(getIdsList(true), "10");
                    } else{
                        groupTool.addGroupUser(getIdsList(false), getIntent().getStringExtra(INTENT_EXTRA_GROUP_ID));
                    }
                }

                break;
            case R.id.iv_back:
                backtofront();
                break;
            case R.id.layout_search:
            case R.id.et_search:
                if (CompareDatalogic.isInitContact()) {
                    Intent intent = new Intent(this, SearchContactActivity.class);
                    intent.putExtra(INTENT_EXTRA_GROUP_USERS,groupUsers);
                    intent.putExtra(AddressList.SHOWCONTENT,AddressList.SHOWCOLLEAG);
                    intent.putExtra("selectMode", 1);    //搜索选择返回多选页
                    startActivityForResult(intent,REQUEST_SEARCH);
                } else {
                    ToastUtils.showToast(this, "通讯录初始化中...");
                }
                break;

            case R.id.tv_des:
                finish();
                break;
        }
    }

    private List<String> getIdsList(boolean needCurrent) {
//        String str="";
        List<String> list = new ArrayList<>();
        for (BaseSearch bs : listsHorizon) {
            CompanyContactListEntity user = (CompanyContactListEntity) bs;
            if (user.haveSelect == false) {
                list.add(user.userId);
            }
        }
        if (needCurrent) {
            for (CompanyContactListEntity user : groupUsers) {
                list.add(user.userId);
            }
        }
        listsHorizon.clear();
        return list;
    }


    private void getOrganization() {
        if("0".equals(idDep)){
            mNoContactsTips.setText("你的通讯录里还没有医生");
        }else {
            mNoContactsTips.setText("该部门没有成员");
        }
        showLoadingDialog();
        HashMap<String, String> maps = new HashMap<>();
    /*    maps.put("access_token", UserInfo.getInstance(this).getSesstion());
        maps.put("drugCompanyId", SharedPreferenceUtil.getString(this, "enterpriseId", ""));
        maps.put("id", idDep);*/

        maps.put("access_token", UserInfo.getInstance(this).getSesstion());
        maps.put("drugCompanyId", SharedPreferenceUtil.getString(this, "enterpriseId", ""));
        maps.put("orgId", idDep);
        maps.put("hideUnassign","1");
        //"org/enterprise/dept/getDepartmentsByParentIdAndEId"
        new HttpManager().get(this, Constants.DEPSTRUCT, CompanyDepment.class,
                maps, this,
                false, 1);
    }

    @Override
    public void onSuccess(Result response) {
        closeLoadingDialog();
        boolean haveDep = false;
        if (response != null && response.resultCode == 1) {
            if (response instanceof CompanyDepment) {
                CompanyDepment companyDepment = (CompanyDepment) (response);
                if (null != companyDepment.data && null != companyDepment.data.departments && companyDepment.data.departments.size() > 0) {
                    list.clear();
                    haveDep = true;
                    list.addAll(companyDepment.data.departments);
                    pareid = companyDepment.data.departments.get(0).parentId;
                    if (list != null && list.size() > 0 && list.get(0) instanceof CompanyDepment.Data.Depaments) {
                        CompanyDepment.Data.Depaments entity = (CompanyDepment.Data.Depaments) list.get(0);
                        if (null != listsTitle && listsTitle.size() > 0 && null != entity && entity.parentId != null && null != listsTitle.get(entity.parentId)
                                && null != listsTitle.get(entity.parentId).name) {
                            setTitle(listsTitle.get(entity.parentId).name + "");
                        } else {
                            setTitle("选择联系人");
                        }
                    }

                    adapter.setSize(companyDepment.data.departments.size());
                    adapter.notifyDataSetChanged();

                } else {
                    haveDep = false;
                    adapter.setSize(0);
                    closeLoadingDialog();
                }

                if (null != companyDepment.data && null != companyDepment.data.users && companyDepment.data.users.size() > 0) {



                    if (companyDepment.data.users.size() > mPageSize) {
                        mPullIndex = 0;
                        mUserIdList = companyDepment.data.users;
                        List<Integer> integers = mUserIdList.subList(mPullIndex * mPageSize, (mPullIndex + 1) * mPageSize);
                        new LoadContactsTask(haveDep).execute(integers);
                        mPullIndex++;
                        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                    } else {
                        new LoadContactsTask(haveDep).execute(companyDepment.data.users);
                        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.DISABLED);
                    }
                }else {
                    if(!haveDep){//显示空白提示view
                        list.clear();
                        adapter.notifyDataSetChanged();
                    }
                }

            }
        }
    }


    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {

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

    private SessionGroupCallback callback = new SessionGroupCallback() {
        @Override
        public void onGroupInfo(Data data, int what) {
            closeLoadingDialog();
            String groupId = getIntent().getStringExtra(INTENT_EXTRA_GROUP_ID);
            if (what == SessionGroup.CREATE) {
                String gname = data.gname;
                String gid = data.gid;
                SelectPeopleActivity.this.data = data;
//				ArrayList<UserInfo> uids = CommonUitls.getGroupUserIds(res.getData().getUserList());
                ArrayList<Data.UserInfo> uids = new ArrayList<>(Arrays.asList(data.userList));
                ChatGroupDao dao = new ChatGroupDao();
                dao.saveOldGroupInfo(data);
//                if (Doctor2DoctorChatActivity.class.getSimpleName().equals(from)) {
//                }
                EventBus.getDefault().post(new AddGroupUserEvent(groupId));
//                RepresentGroupChatActivity.openUI(mThis, gname, gid, uids);
                ArrayList<Data.UserInfo> users = null;
                if (data.userList != null)
                    users = new ArrayList<>(Arrays.asList(data.userList));
                if (mShare) {
                    //转发信息
                    if (mItem!=null){
                     //   ImRequestManager.forwardMsg(mItem.po.msgId, data.gid, 0, new ShareResultListener());
//                        HashMap<String, Object> params = new HashMap<>();
//                        params.put("share_files",mItem);
//                        RepresentGroupChatActivity.openUI(mThis, data.gname, data.gid, users, params);
                        ImRequestManager.sendArchive(mItem, data.gid,new ShareItemFileListener());
                    }else {
                        ImRequestManager.forwardMsg(msgId, data.gid, 0, new ShareResultListener());
                    }
                } else {
                    ChatActivityUtilsV2.openUI(mThis, gid, "10");
                    finish();
                   // CallIntent.startMainActivity(SelectPeopleActivity.this);
                }
                EventBus.getDefault().post(new AddGroupUserEvent(groupId));
                setResult(RESULT_OK);
               // ImUtils.closeChat(groupIds);
            } else {
                ArrayList<Data.UserInfo> users = null;
                if (data.userList != null)
                    users = new ArrayList<>(Arrays.asList(data.userList));
                if (mShare) {
                    //转发信息
                    if (mItem!=null){
//                        HashMap<String, Object> params = new HashMap<>();
//                        params.put("share_files",mItem);
//                        RepresentGroupChatActivity.openUI(mThis, data.gname, data.gid, users, params);
                        ImRequestManager.sendArchive(mItem, data.gid, new ShareItemFileListener());
                    }else {
                        ImRequestManager.forwardMsg(msgId, data.gid, 0, new ShareResultListener());
                    }
                } else {
                    RepresentGroupChatActivity.openUI(mThis, data.gname, data.gid, users);
                    finish();
                }
                EventBus.getDefault().post(new AddGroupUserEvent(groupId));
                setResult(RESULT_OK);
                //ImUtils.closeChat(groupIds);
               // CallIntent.startMainActivity(SelectPeopleActivity.this);
            }
        }
        @Override
        public void onGroupInfoFailed(String msg) {
            closeLoadingDialog();
            inWork = false;
            ToastUtil.showToast(mThis, msg);
        }
    };
    private class ShareItemFileListener implements MessageSenderV2.MessageSendCallbackV2{

        @Override
        public void sendSuccessed(ChatMessagePo msg, String groudId, String msgId, long time) {

            CallIntent.startMainActivity(SelectPeopleActivity.this);
            HashMap<String, Object> params = new HashMap<>();
            params.put("share_files", mItem);
            if (data!=null){
                ArrayList<Data.UserInfo>  users = new ArrayList<>(Arrays.asList(data.userList));
                RepresentGroupChatActivity.openUI(SelectPeopleActivity.this, data.gname, data.gid, users);
            }

        }

        @Override
        public void sendFailed(ChatMessagePo msg, int resultCode, String resultMsg) {

        }
    }
    private class ShareResultListener implements SimpleResultListener {

        @Override
        public void onSuccess() {
            CallIntent.startMainActivity(SelectPeopleActivity.this);
            ToastUtil.showToast(mThis, "转发成功");

        }

        @Override
        public void onError(String msg) {
            ToastUtil.showToast(mThis, msg);
        }
    }

    class LoadContactsTask extends AsyncTask<List<Integer>, Void, List<CompanyContactListEntity>> {
        private boolean haveDep;

        public LoadContactsTask(boolean haveDep) {
            this.haveDep = haveDep;
        }

        @Override
        protected List<CompanyContactListEntity> doInBackground(List<Integer>... params) {
            List<CompanyContactListEntity> entityList = companyContactDao.queryByUserIds(params[0]);
            return entityList;
        }

        @Override
        protected void onPostExecute(List<CompanyContactListEntity> entityList) {
            if (entityList != null && entityList.size() > 0) {
                if (!haveDep) {
                    adapter.setSize(0);
                    list.clear();
                }
                entityList = addHaveAdd(entityList);
                for (int i = 0; i < entityList.size(); i++) {
                    for (int j = 0; j < listsHorizon.size(); j++) {
                        if (entityList.get(i).equals(listsHorizon.get(j))) {
                            CompanyContactListEntity entity = entityList.get(i);
                            entity.select = true;
                            entityList.set(i, entity);
                            break;
                        }
                    }
                }
                list.addAll(entityList);
                adapter.notifyDataSetChanged();
            }

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SEARCH:

                  CompanyContactListEntity c2 = (CompanyContactListEntity) intent.getSerializableExtra("data");
                    if (null!=c2){
                        if (c2.select) {
                        } else {
                            c2.select = true;
                            if (!groupUsers.contains(c2) && !c2.userId.equals(SharedPreferenceUtil.getString(SelectPeopleActivity.this, "id", ""))) {
                                CommonUitls.addCompanyContactListEntity(c2);
                            }
                        }
                        addAdapter.notifyDataSetChanged();
                        for (int i = 0; i < list.size(); i++) {
                            BaseSearch search = list.get(i);
                            if (search instanceof CompanyContactListEntity) {
                                CompanyContactListEntity entity = (CompanyContactListEntity) list.get(i);
                                if (entity.userId.equals(c2.userId)) {
                                    list.set(i, c2);
                                }
                            }
                        }
                    }
                        adapter.notifyDataSetChanged();
                    addAdapter.notifyDataSetChanged();
                        btn_add.setText("开始(" + listsHorizon.size() + ")");
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        backtofront();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        mListGuider.clearData();
        super.onDestroy();
    }
}
