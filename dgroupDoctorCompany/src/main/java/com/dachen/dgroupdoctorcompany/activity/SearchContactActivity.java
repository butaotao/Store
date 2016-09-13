package com.dachen.dgroupdoctorcompany.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.common.async.SimpleResultListener;
import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.CircleCreateGroupAdapter;
import com.dachen.dgroupdoctorcompany.adapter.SearchContactAdapter;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.DoctorDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.SearchRecordsDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
import com.dachen.dgroupdoctorcompany.db.dbentity.SearchRecords;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.entity.CompanyDepment;
import com.dachen.dgroupdoctorcompany.fragment.AddressList;
import com.dachen.dgroupdoctorcompany.im.activity.Represent2DoctorChatActivity;
import com.dachen.dgroupdoctorcompany.utils.CommonUitls;
import com.dachen.dgroupdoctorcompany.utils.ConditionLogic;
import com.dachen.dgroupdoctorcompany.utils.ExitActivity;
import com.dachen.dgroupdoctorcompany.views.HorizontalListView;
import com.dachen.imsdk.adapter.MsgMenuAdapter;
import com.dachen.imsdk.consts.SessionType;
import com.dachen.imsdk.db.dao.ChatGroupDao;
import com.dachen.imsdk.entity.GroupInfo2Bean;
import com.dachen.imsdk.net.SessionGroup;
import com.dachen.imsdk.service.ImRequestManager;
import com.dachen.medicine.common.utils.MActivityManager;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.view.ClearEditText;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchContactActivity extends BaseActivity implements OnClickListener,OnHttpListener,SessionGroup.SessionGroupCallback {
    int totalNum;
    RelativeLayout rl_list;
    RelativeLayout rl_back;
    ImageView iv_search;
    ClearEditText et_search;
    int page = 1;
    ViewStub vstub_title;


    //SearchAdapter adapter;
    RelativeLayout rl_history;
    RelativeLayout rl_nofound;
    Button btn_clear;
    PullToRefreshListView listview;
    SearchContactAdapter adapter;
    CompanyContactDao dao ;
    DoctorDao doctorDao;
    SearchRecordsDao searchRecordsDao;
    List<BaseSearch> hospitals;
    List<BaseSearch> recordses;
    RelativeLayout rl_noresult;
    TextView tv_noresult;
    List<CompanyContactListEntity> company;
    List<Doctor> doctors;
    public String searchText = "";
    int partSize;
    RelativeLayout rl_search;
    boolean finish = true;
    public String seachdoctor;
    public String clear = "_*$@#_clearall_*$@#_";
    private int pageNo = 1;
    private int selectMode;
    private String mDoctorId;
    private boolean mShare;
    private String msgId;
    HorizontalListView addlistview;
    CircleCreateGroupAdapter addAdapter;
    List<BaseSearch> horizonList;
    Button btn_add;
    ArrayList<CompanyContactListEntity> groupUsers;
    private boolean inWork;
    LinearLayout bottom_bar;
    public boolean isShow;
    public boolean showDoctor;
    public boolean showColleague;
    public boolean showAll;
    public int  SHOWCONTENT;
    RefreshDataInterface refreshDataInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_allcontact);
        groupUsers = (ArrayList<CompanyContactListEntity>) getIntent().getSerializableExtra(SelectPeopleActivity.INTENT_EXTRA_GROUP_USERS);
        if (groupUsers == null) {
            groupUsers = new ArrayList<>();
        }



        btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        msgId = getIntent().getStringExtra(MsgMenuAdapter.INTENT_EXTRA_MSG_ID);
        addlistview = (HorizontalListView) findViewById(R.id.addlistview);
        horizonList = CommonUitls.getListsHorizon();
        addAdapter = new CircleCreateGroupAdapter(this, horizonList);
        addlistview.setAdapter(addAdapter);
      //  findViewById(R.id.rl_sure).setVisibility(View.GONE);
        if (TextUtils.isEmpty(seachdoctor)){
            if (ConditionLogic.isAllow(this)){
                et_search.setHint("搜索姓名/简拼/手机号");
            }else {
                et_search.setHint("搜索姓名/简拼");
            }

        }else {
            et_search.setHint("输入医生姓名关键词搜索");
            showDoctor = true;
        }
    }

    /*	*/
    @Override
    public void setContentView(int layoutResID) {
        // TODO Auto-generated method stub
        super.setContentView(layoutResID);
        this.findViewById(R.id.tv_title).setVisibility(View.GONE);
        dao = new CompanyContactDao(this);
        searchRecordsDao = new SearchRecordsDao(this);
        doctorDao = new DoctorDao(this);
        mShare = getIntent().getBooleanExtra("share", false);
        seachdoctor = getIntent().getStringExtra("seachdoctor");
        selectMode  = getIntent().getIntExtra("selectMode", 0);
        rl_history = (RelativeLayout) this.findViewById(R.id.rl_history);
        vstub_title = (ViewStub) findViewById(R.id.vstub_title);
        RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.ll_sub);
        View view = vstub_title.inflate(this, R.layout.layout_search_import, rl);
        view.findViewById(R.id.tv_search).setOnClickListener(this);
        listview = (PullToRefreshListView) findViewById(R.id.listview);
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNo = 1;
                forSearch();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNo++;
                forSearch();
            }
        });
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_back.setVisibility(View.GONE);
        rl_search = (RelativeLayout) findViewById(R.id.rl_search);
        hospitals = new ArrayList<>();
        recordses = new ArrayList<>();

		/*listSearch  = new ArrayList<MedicineInfo>();
		listSearchWord = new ArrayList<MedicineInfo>();  */
        iv_search = (ImageView) view.findViewById(R.id.iv_search);
        findViewById(R.id.rl_search).setOnClickListener(this);
        iv_search.setBackgroundResource(R.drawable.back_exit);
        rl_nofound = (RelativeLayout) this.findViewById(R.id.rl_nofound);
        et_search = (ClearEditText) view.findViewById(R.id.et_search);


        btn_clear = (Button) findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(this);
        et_search.setOnClickListener(this);
        rl_history.setVisibility(View.GONE);
        listview.setVisibility(View.VISIBLE);
        rl_noresult = (RelativeLayout) findViewById(R.id.rl_noresult);
        tv_noresult = (TextView) findViewById(R.id.tv_noresult);
        /*if (null!=dao.queryAllUserInfo()&&dao.queryAllUserInfo().size()>0){
            recordses.clear();
            recordses.addAll(dao.queryAllUserInfo()) ;
            rl_history.setVisibility(View.VISIBLE);
        }
        List<SearchRecords> search =dao.querySearch("1");*/

        company = new ArrayList<>();
        doctors = new ArrayList<>();
        refreshDataInterface = new RefreshDataInterface() {
            @Override
            public void refreshData() {
                if(null!=search&&search.size()>0){
                    rl_search.setVisibility(View.VISIBLE);
                    iv_search.setBackgroundResource(R.drawable.arrow_left);
                    finish = false;
                }
            }
        };
        adapter = new SearchContactAdapter(this,R.layout.adapter_searchcontact,hospitals,company,doctors, refreshDataInterface,seachdoctor);
        adapter.setisShowMore(true);
        bottom_bar = (LinearLayout) findViewById(R.id.bottom_bar);
        bottom_bar.setVisibility(View.GONE);
        SHOWCONTENT = getIntent().getIntExtra(AddressList.SHOWCONTENT,0);
        if (SHOWCONTENT==AddressList.SHOWALL){
            showAll = true;
            isShow = false;
            bottom_bar.setVisibility(View.GONE);
        }
        if (SHOWCONTENT==AddressList.SHOWCOLLEAG){
            showAll = false;
            isShow = true;
            showColleague = true;
            showDoctor = false;
            bottom_bar.setVisibility(View.GONE);
        }
        listview.setAdapter(adapter);
     //   if (selectMode==1){

       // }
        et_search.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            // 在这里编写自己想要实现的功能
                            pageNo = 1;
                            forSearch();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
                        }
                        return false;
                    }
                });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pageNo = 1;
                forSearch();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                arg2 = arg2 - 1;
                if (adapter.getItem(arg2) instanceof CompanyContactListEntity) {
                    CompanyContactListEntity info = (CompanyContactListEntity) adapter.getItem(arg2);
                    // 在这里编写自己想要实现的功能
                    if (selectMode == 1) {          //新建同事对话搜索
                        Intent intent = new Intent();
                        intent.putExtra("data", adapter.getItem(arg2));
                        setResult(RESULT_OK, intent);
                        finish();


                    } else if (selectMode != 1) {
                        Intent intent = new Intent(SearchContactActivity.this, ColleagueDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("peopledes", info);
                        intent.putExtra("peopledes", bundle);
                        startActivity(intent);
                    }

                } else if (adapter.getItem(arg2) instanceof Doctor) {
                    Doctor info = (Doctor) adapter.getItem(arg2);
                    // 在这里编写自己想要实现的功能
                    mDoctorId = info.userId;
                    if (selectMode == 2) {   //新建医生对话搜索
                        createChatGroup(mDoctorId);
                    } else {
                        Intent intent = new Intent(SearchContactActivity.this, DoctorDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("doctordetail", info);
                        intent.putExtra("doctordetail", bundle);
                        startActivity(intent);
                    }
                }

            }
        });
        page = 1;

    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        et_search.requestFocus();

        et_search.setFocusable(true);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) et_search.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(et_search, 0);
                           }

                       },
                998);



    }
    private void createChatGroup(String userId){
        List<String> userIds = new ArrayList<String>();
        userIds.add(userId);
        SessionGroup groupTool=new SessionGroup(this);
        groupTool.setCallback(this);
        groupTool.createGroup(userIds, "3_10");
        finish();
        MActivityManager.getInstance().finishAppointActivity(ChoiceDoctorForChatActivity.class);
    }

    @Override
    public void onGroupInfo(GroupInfo2Bean.Data data, int what) {
        ChatGroupDao dao=new ChatGroupDao();
        dao.saveOldGroupInfo(data);
        if (mShare) {
            ImRequestManager.forwardMsg(msgId,data.gid, 0, new ShareResultListener());
        } else {
            Represent2DoctorChatActivity.openUI(this, data.gname, data.gid, mDoctorId);
        }
    }

    private class ShareResultListener implements SimpleResultListener {

        @Override
        public void onSuccess() {
            ToastUtil.showToast(mThis,"转发成功");
            finish();
            ExitActivity.getInstance().exit();
        }

        @Override
        public void onError(String msg) {
            ToastUtil.showToast(mThis, msg);
        }
    }

    @Override
    public void onGroupInfoFailed(String msg) {

    }


    @Override
    public void onSuccess(Result response) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onSuccess(ArrayList response) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        // TODO Auto-generated method stub
        closeLoadingDialog();

    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        if(v.getId() == R.id.rl_search){
            adapter.setisShowMore(true);
            rl_search.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(searchText)){
                getSearchResult(searchText);
            }

        }else if(v.getId() == R.id.btn_clear){
         /*   rl_history.setVisibility(View.GONE);
            dao.clearAll();
            recordses.clear();
            adapter = new SearchContactAdapter(this,R.layout.adapter_hospital_list,recordses);
            listview.setAdapter(adapter);*/
        }else  if(v.getId() == R.id.tv_search){
            //ToastUtils.showToast(this,"搜索");
            Intent intent = new Intent(this,SelectPeopleActivity.class);
            setResult(RESULT_OK, intent);
            finish();
        }else  if(v.getId() == R.id.btn_add)   {

            finish();
        }


    }

    public void forSearch(){
        if (!TextUtils.isEmpty(et_search.getText())) {
            searchText = et_search.getText().toString().trim();
            SearchRecords records = new SearchRecords();
            records.searchresult = searchText;
            records.userloginid = SharedPreferenceUtil.getString(this,"id","");
            records.serchtype = "2";
            searchRecordsDao.addRole(records,"2");
            getSearchResult(searchText);
        }else {
            getSearchResult(clear);
        }
    }
    public void forSearchHistory(String searchText){
        getSearchResult(searchText);
    }


    private List<BaseSearch> search  = new ArrayList<>();
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refreshResult(String.valueOf(msg.obj));
            if(mDialog != null){
                mDialog.dismiss();
            }
        }
    };


    class MyThread extends Thread {

        private String keyword;

        public MyThread(String keyword) {
            this.keyword = keyword;
        }

        @Override
        public void run() {
            if (showColleague||showAll){
                List<CompanyContactListEntity> tempCompany;

                    tempCompany = dao.querySearchPage(keyword, pageNo,company,true);

            if(pageNo == 1){
                company = tempCompany;
            }

            if (null != tempCompany && tempCompany.size() > 0){
                if(pageNo > 1){
                    company.addAll(tempCompany);
                }
                if(tempCompany.size() < 50){
                    isLoadMore = false;
                }
            }
            }else {
                company.clear();
            }
            if (showDoctor||showAll){
                List<Doctor> tempDoctors = doctorDao.querySearchPage(keyword, pageNo);
                if(pageNo == 1){
                    doctors = tempDoctors;
                }

                if (null != tempDoctors && tempDoctors.size() > 0){
                    if(pageNo > 1){
                        doctors.addAll(tempDoctors);
                    }
                    if(tempDoctors.size() < 50){
                        isLoadMore = false;
                    }
                }
            }else {
                doctors.clear();
            }


            if(TextUtils.isEmpty(seachdoctor)){
                int size = 0;
                for (int i=0; i<company.size(); i++){
                    if ( i<company.size() && null != company.get(i)){
                        if (CommonUitls.getListsHorizon().contains(company.get(i))){
                            continue;
                        }
                        size++;
                        if (showColleague){
                            search.add(company.get(i));
                        }else {
                            if (size<=3){
                                search.add(company.get(i));
                            }else {
                                break;
                            }
                        }



                    }
                }
                for (int j=0;j<3;j++){
                    if (j<doctors.size() && null != doctors.get(j)){
                        search.add(doctors.get(j));
                    }
                }
            }else {
                search.addAll(doctors);
            }

            //发消息通知UI更新页面
            Message msg = new Message();
            msg.arg1 = 0;
            msg.obj = keyword;
            mHandler.sendMessage(msg);
        }
    }

    private MyThread myThread;
    private boolean isLoadMore = true;

    public void getSearchResult(final String keyword){
        String searchWord = keyword;
        if(TextUtils.isEmpty(searchWord)){
            listview.onRefreshComplete();
            return;
        }
        isLoadMore = true;
        search  = new ArrayList<>();
        if(mDialog != null){
            mDialog.show();
        }
        myThread = new MyThread(keyword);
        myThread.start();
    }

    public void refreshResult(String keyword){
        finish = true;
        listview.onRefreshComplete();
        if(isLoadMore){
            listview.setMode(PullToRefreshBase.Mode.BOTH);
        }else {
            listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        }
        rl_history.setVisibility(View.GONE);

        if(null!=search&&search.size()>0){
            hospitals.clear();
            hospitals.addAll(search);
            //listview.setAdapter(adapter);
            rl_noresult.setVisibility(View.GONE);
            List<BaseSearch> searches = CommonUitls.getListsHorizon();
            for (int i=0;i<company.size();i++){
                for (int j=0;j<searches.size();j++){
                    CompanyContactListEntity entity = company.get(i);
                    CompanyContactListEntity entityHorizon = (CompanyContactListEntity)searches.get(j);
                    if (entity.userId.equals((entityHorizon).userId)){
                        entity.select =entityHorizon.select;
                        company.set(i,entityHorizon);
                        continue;
                    }
                }
            }


            adapter.setContact(company);
            adapter.setDoctors(doctors);
            adapter.setPartSize(company.size());
            adapter.sethospitalSize(doctors.size());
            adapter.setisShowMore(true);

            adapter.notifyDataSetChanged();
            btn_add.setText("开始(" + searches.size() + ")");
        }else {
            hospitals.clear();
        ;
            adapter.setContact(company);
            adapter.setDoctors(doctors);
            adapter.setPartSize(company.size());
            adapter.sethospitalSize(doctors.size());
            adapter.notifyDataSetChanged();
            rl_noresult.setVisibility(View.VISIBLE);
            tv_noresult.setText("没有”" + keyword + "“的相关搜索结果");
            btn_add.setText("开始(" + "0)");
        }
        if (keyword.equals(clear)){
            rl_noresult.setVisibility(View.GONE);
        }
        closeLoadingDialog();
    }

    @Override
    public void onBackPressed() {
        if (finish){
            Intent intent = new Intent(this,SelectPeopleActivity.class);
            setResult(RESULT_OK,intent);
            finish();
        }else {
            adapter.setisShowMore(true);
            rl_search.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(searchText)){
                getSearchResult(searchText);
            }
        }

        // super.onBackPressed();
        // this.mApplication.getActivityManager().finishActivity(this.getClass());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        super.onActivityResult(requestCode, resultCode, data);
    }
    public interface RefreshDataInterface{
        void refreshData();
    }
}
