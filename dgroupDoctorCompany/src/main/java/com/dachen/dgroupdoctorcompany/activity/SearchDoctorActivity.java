package com.dachen.dgroupdoctorcompany.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.SearchDoctorAdapter;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.DoctorDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.SearchRecordsDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
import com.dachen.dgroupdoctorcompany.db.dbentity.SearchRecords;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.dachen.dgroupdoctorcompany.entity.SearchDoctorListEntity;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.view.ClearEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchDoctorActivity extends BaseActivity implements OnClickListener, OnHttpListener {
    int totalNum;
    RelativeLayout rl_list;
    //ChoicedItemadapter choicedAdapter;
    ImageView iv_search;
    ClearEditText et_search;
    int page = 1;
    ViewStub vstub_title;
    public String hospitId;
    SearchDoctorAdapter adapter;
    List<Doctor> doctors;
    ListView listview;
    DoctorDao dao;
    List<BaseSearch> doctorsSelect;
    RelativeLayout rl_noresult;
    TextView tv_noresult;
    String searchText = "";
    RelativeLayout rl_back;
    SearchRecordsDao searchRecordsDao;
    List<BaseSearch> recordses;
    RelativeLayout rl_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_doctor);
        this.findViewById(R.id.rl_titlebar).setBackgroundResource(R.color.title_bg_color);
        listview = (ListView) findViewById(R.id.listview);
        doctors = new ArrayList<>();
        dao = new DoctorDao(this);
        searchRecordsDao = new SearchRecordsDao(this);
        recordses = new ArrayList<>();
        searchText = getIntent().getStringExtra("searchText");
        hospitId = getIntent().getStringExtra("hospitId");
        Bundle bundle = getIntent().getBundleExtra("doctorsSelect");
        findViewById(R.id.btn_clear).setOnClickListener(this);
        doctorsSelect = new ArrayList<>();
        if (null != bundle) {
            doctorsSelect = (List<BaseSearch>) bundle.getSerializable("doctorsSelect");
        }

        tv_noresult = (TextView) findViewById(R.id.tv_noresult);
        if (!TextUtils.isEmpty(searchText)) {
            et_search.setText(searchText + "");
            et_search.setSelection(searchText.length());
        }
        if (null != searchRecordsDao.queryAll("2") && searchRecordsDao.queryAll("2").size() > 0) {
            doctorsSelect.clear();
            doctorsSelect.addAll(searchRecordsDao.queryAll("2"));
            rl_history.setVisibility(View.VISIBLE);
        } else {
            rl_history.setVisibility(View.GONE);
        }
        //getSearchResult(searchText);
        adapter = new SearchDoctorAdapter(this, R.layout.adapter_searchdoctor, doctorsSelect);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter.getItem(position) instanceof SearchRecords) {
                    if (null != adapter.getItem(position)) {
                        String searchtext = ((SearchRecords) adapter.getItem(position)).searchresult;
                        if (!TextUtils.isEmpty(searchtext)) {
                            searchText = searchtext;
                            getSearchResult(searchtext);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void setContentView(int layoutResID) {
        // TODO Auto-generated method stub
        super.setContentView(layoutResID);
        this.findViewById(R.id.tv_title).setVisibility(View.GONE);
        vstub_title = (ViewStub) findViewById(R.id.vstub_title);
        RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.ll_sub);
        View view = vstub_title.inflate(this, R.layout.layout_search_import, rl);
        rl_noresult = (RelativeLayout) findViewById(R.id.rl_noresult);
        rl_history = (RelativeLayout) findViewById(R.id.rl_history);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_back.setVisibility(View.GONE);

        view.findViewById(R.id.tv_search).setOnClickListener(this);
        iv_search = (ImageView) view.findViewById(R.id.iv_search);
        iv_search.setOnClickListener(this);
        et_search = (ClearEditText) view.findViewById(R.id.et_search);
        et_search.setHint("搜索医生姓名");
        rl_noresult.setVisibility(View.GONE);
        et_search.setOnClickListener(this);
        hospitId = getIntent().getStringExtra("hospitId");
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 在这里编写自己想要实现的功能
                    forSearch();
                }
                return false;
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

    @Override
    public void onSuccess(Result response) {
        // TODO Auto-generated method stub
        closeLoadingDialog();
        if (response != null && response.resultCode == 1) {
            if (response instanceof SearchDoctorListEntity) {
                rl_history.setVisibility(View.GONE);
                doctors.clear();
                doctorsSelect.clear();
                SearchDoctorListEntity searchDoctorList = (SearchDoctorListEntity) response;
                ArrayList<SearchDoctorListEntity.SearchDoctorInfo> infos = searchDoctorList.data;
                if (null != infos && infos.size() > 0) {
                    for (int i = 0; i < infos.size(); i++) {
                        if (null != infos.get(i).enterpriseUserList && infos.get(i).enterpriseUserList.size() > 0) {
                            for (int j = 0; j < infos.get(i).enterpriseUserList.size(); j++) {
                                doctors.add(infos.get(i).enterpriseUserList.get(j));
                                doctorsSelect.add(infos.get(i).enterpriseUserList.get(j));
                            }
                        }
                    }
                    rl_noresult.setVisibility(View.GONE);
                } else {
                    rl_history.setVisibility(View.GONE);
                    rl_noresult.setVisibility(View.VISIBLE);
                    ///"
                    tv_noresult.setText("未搜索到名字中含有【 " + searchText + " 】的医生");
                }
                adapter.notifyDataSetChanged();
            }
        }

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
        if (v.getId() == R.id.iv_search) {
            forSearch();
        } else if (v.getId() == R.id.tv_search) {
            finish();
        } else if (v.getId() == R.id.btn_clear) {
            searchRecordsDao.clearAll("2");
            doctorsSelect.clear();
            rl_history.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }

    public void forSearch() {
        if (!TextUtils.isEmpty(et_search.getText())) {
            searchText = et_search.getText().toString().trim();
            getSearchResult(searchText);
            SearchRecords records = new SearchRecords();
            records.searchresult = searchText;
            records.userloginid = SharedPreferenceUtil.getString(this, "id", "");
            records.serchtype = "2";
            searchRecordsDao.addRole(records, "2");
        }
    }

    public void getSearchResult(String keyword) {
        String searchWord = keyword;
        if (TextUtils.isEmpty(searchWord)) {
            return;
        }
        HashMap<String, String> maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(this).getSesstion());
        maps.put("hospitalId", hospitId);
        maps.put("keyword", keyword);
        new HttpManager().post(this, "org/saleFriend/getList", SearchDoctorListEntity.class,
                maps, this,
                false, 1);
        showLoadingDialog();
    }

    /*public void getSearchResultWithNull(String keyword){

        HashMap<String ,String > maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(this).getSesstion());
        maps.put("hospitalId", hospitId);
        maps.put("keyword", "");
        new HttpManager().post(this, "org/saleFriend/getList", SearchDoctorListEntity.class,
                maps, this,
                false, 1);
        showLoadingDialog();
    }*/
    @Override
    public void onBackPressed() {
        finish();
        // super.onBackPressed();
        // this.mApplication.getActivityManager().finishActivity(this.getClass());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        super.onActivityResult(requestCode, resultCode, data);
    }

}
