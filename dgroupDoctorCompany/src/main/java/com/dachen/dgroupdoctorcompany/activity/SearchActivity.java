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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.HospitalListAdapter;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.SearchRecordsDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.SearchRecords;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.dachen.dgroupdoctorcompany.entity.HospitalDes;
import com.dachen.dgroupdoctorcompany.entity.HospitalList;
import com.dachen.dgroupdoctorcompany.entity.HospitalManager;
import com.dachen.dgroupdoctorcompany.entity.HospitalMangerData;
import com.dachen.dgroupdoctorcompany.entity.Results;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.view.ClearEditText;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends BaseActivity implements OnClickListener, OnHttpListener {
    int totalNum;
    RelativeLayout rl_list;
    RelativeLayout rl_back;
    ImageView iv_search;
    ClearEditText et_search;
    int page = 1;
    ViewStub vstub_title;
    RelativeLayout rl_history;
    RelativeLayout rl_nofound;
    Button btn_clear;
    ListView listview;
    HospitalListAdapter adapter;
    SearchRecordsDao dao;
    List<BaseSearch> hospitals;
    List<BaseSearch> recordses;
    RelativeLayout rl_noresult;
    String fromActivity;
    Button btnsure;
    String id = "";
    TextView tv_noresult;
    public List<HospitalDes> addHospital;
    View view;
    String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        findViewById(R.id.btnsure).setOnClickListener(this);
        tv_noresult = (TextView) findViewById(R.id.tv_noresult);
        Bundle bundle = getIntent().getBundleExtra("addHospital");
        id = getIntent().getStringExtra("id");
        this.findViewById(R.id.rl_titlebar).setBackgroundResource(R.color.title_bg_color);
        if (bundle != null) {
            addHospital = (List<HospitalDes>) bundle.getSerializable("addHospital");
        }

    }

    /*	*/
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        this.findViewById(R.id.tv_title).setVisibility(View.GONE);
        dao = new SearchRecordsDao(this);
        btnsure = (Button) findViewById(R.id.btnsure);
        rl_history = (RelativeLayout) this.findViewById(R.id.rl_history);
        vstub_title = (ViewStub) findViewById(R.id.vstub_title);
        view = View.inflate(SearchActivity.this, R.layout.layout_searchfooter, null);
        ;
        RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.ll_sub);
        View view = vstub_title.inflate(this, R.layout.layout_search_import, rl);
        view.findViewById(R.id.tv_search).setOnClickListener(this);
        listview = (ListView) findViewById(R.id.listview);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_back.setVisibility(View.GONE);
        hospitals = new ArrayList<>();
        recordses = new ArrayList<>();
        fromActivity = getIntent().getStringExtra("AddSelfPartHospitalActivity");
        iv_search = (ImageView) view.findViewById(R.id.iv_search);
        iv_search.setOnClickListener(this);
        rl_nofound = (RelativeLayout) this.findViewById(R.id.rl_nofound);
        et_search = (ClearEditText) view.findViewById(R.id.et_search);
        et_search.setHint("输入医院关键词搜索");
        btn_clear = (Button) findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(this);
        et_search.setOnClickListener(this);
        rl_history.setVisibility(View.GONE);
        findViewById(R.id.iv_search).setVisibility(View.GONE);
        listview.setVisibility(View.VISIBLE);
        rl_noresult = (RelativeLayout) findViewById(R.id.rl_noresult);
        if (null != dao.queryAll("1") && dao.queryAll("1").size() > 0) {
            recordses.clear();
            recordses.addAll(dao.queryAll("1"));
            rl_history.setVisibility(View.VISIBLE);
        }
        adapter = new HospitalListAdapter(this, R.layout.adapter_hospital_list, recordses, "", new RefreshData() {
            @Override
            public void refreshData(List<BaseSearch> recordses) {
                int total = 0;
                total = HospitalListAdapter.getTotal(recordses);
                btnsure.setText("确定 (" + total + ")");
            }
        });
        listview.setAdapter(adapter);
        et_search.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            // 在这里编写自己想要实现的功能
                            forSearch();
                        }
                        return false;
                    }
                });
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (adapter.getItem(arg2) instanceof SearchRecords) {
                    SearchRecords info = (SearchRecords) adapter.getItem(arg2);
                    //                    if (!TextUtils.isEmpty(info.searchresult)) {
                    // 在这里编写自己想要实现的功能
                    searchText = info.searchresult;
                    forSearchHistory(info.searchresult);
                    //                    }
                }

            }
        });
        page = 1;
    }

    public interface RefreshData {
        public void refreshData(List<BaseSearch> recordses);
    }

    @Override
    protected void onResume() {
        super.onResume();
        et_search.requestFocus();
        getRelationShip();
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
        closeLoadingDialog();
        if (null == response || response.getResultCode() != 1) {
            ToastUtil.showToast(this, response);
            return;
        }
        if (response instanceof HospitalList) {
            rl_history.setVisibility(View.GONE);
            btnsure.setText("确定 (0)");

            HospitalList hospital = (HospitalList) response;
            if (null != hospital.data && null != hospital.data.pageData && hospital.data.pageData.size() > 0) {
                listview.setVisibility(View.VISIBLE);
                hospitals.clear();

                hospitals.addAll((Collection<? extends HospitalList.Data.HospitalDes>) hospital.data.pageData);
                ArrayList<HospitalList.Data.HospitalDes> des = new ArrayList<>();
                for (int i = 0; i < hospitals.size(); i++) {
                    boolean find = false;
                    if (hospitals.get(i) instanceof HospitalList.Data.HospitalDes) {

                        HospitalList.Data.HospitalDes de = (HospitalList.Data.HospitalDes) hospitals.get(i);
                        if (null != AddSelfPartHospitalActivity.data) {
                            for (int j = 0; j < AddSelfPartHospitalActivity.data.size(); j++)
                                if (de.id.equals(AddSelfPartHospitalActivity.data.get(j).id)) {
                                    find = true;
                                    break;
                                }
                        }
                    }
                    if (!find) {
                        des.add((HospitalList.Data.HospitalDes) hospitals.get(i));
                    }
                }
                hospitals.clear();
                hospitals.addAll(des);
                adapter.notifyDataSetChanged();
                adapter = new HospitalListAdapter(this, R.layout.adapter_hospital_list, hospitals, fromActivity,
                        new RefreshData() {
                            @Override
                            public void refreshData(List<BaseSearch> recordses) {
                                int total = 0;
                                total = HospitalListAdapter.getTotal(recordses);
                                btnsure.setText("确定 (" + total + ")");
                            }
                        });
                listview.setAdapter(adapter);
                rl_noresult.setVisibility(View.GONE);
            } else {
                tv_noresult.setText("未搜索到任何“" + searchText + "“相关结果");
                rl_noresult.setVisibility(View.VISIBLE);
                listview.setVisibility(View.GONE);
            }
        } else if (response instanceof Results) {
            Results r = (Results) response;
            if (r.resultCode == 1) {
                    /*Intent intent = new Intent();
					setResult(200,intent);*/
                ToastUtils.showToast(this, "添加成功");
                finish();
            }
        } else if (response instanceof HospitalManager && response.resultCode == 1) {
            HospitalManager manager = (HospitalManager) response;

            if (null != manager.data && null != manager.data.pageData) {
                ArrayList<HospitalMangerData> datas = manager.data.pageData;
                HospitalManagerActivity.totalNumHospital = manager.data.total;
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.iv_search) {
            forSearch();
        } else if (v.getId() == R.id.btn_clear) {
            rl_history.setVisibility(View.GONE);
            dao.clearAll("1");
            recordses.clear();
            adapter = new HospitalListAdapter(this, R.layout.adapter_hospital_list, recordses, fromActivity, new RefreshData() {
                @Override
                public void refreshData(List<BaseSearch> recordses) {
                    int total = 0;
                    total = HospitalListAdapter.getTotal(recordses);
                    btnsure.setText("确定 (" + total + ")");
                }
            });
            listview.setAdapter(adapter);
            //listview.removeFooterView(view);
        } else if (v.getId() == R.id.tv_search) {
            //ToastUtils.showToast(this,"搜索");
            finish();
        } else if (v.getId() == R.id.btnsure) {
            if (hospitals == null || hospitals.size() == 0) {
                ToastUtils.showToast(this, "请选择需要添加分管关系的医院");
                return;
            }

            String id = "";
            for (BaseSearch des : hospitals) {
                boolean add = false;
                if (des instanceof HospitalList.Data.HospitalDes) {

                    HospitalList.Data.HospitalDes h = (HospitalList.Data.HospitalDes) des;
                    if (h.select == true) {
                        if (AddSelfPartHospitalActivity.data != null && AddSelfPartHospitalActivity.data.size() > 0) {
                            for (int i = 0; i < AddSelfPartHospitalActivity.data.size(); i++) {
                                if (!TextUtils.isEmpty(AddSelfPartHospitalActivity.data.get(i).id)) {
                                    if (AddSelfPartHospitalActivity.data.get(i).id.equals(h.id)) {
                                        add = true;
                                        break;
                                    }
                                }

                            }
                        }
                        HospitalDes d = new HospitalDes();
                        d.id = h.id;
                        d.name = h.name;
                        if (TextUtils.isEmpty(id)) {
                            id = h.id;
                        } else {
                            id += "," + h.id;
                        }
                        if (!add) {
                            AddSelfPartHospitalActivity.data.add(d);
                        }
                    }

                }

            }
            if (TextUtils.isEmpty(id)) {
                ToastUtils.showToast(this, "请选择需要添加分管关系的医院");
                return;
            }
            addHospital(id);

        }
    }

    public void forSearch() {
        searchText = et_search.getText().toString().trim();
        SearchRecords records = new SearchRecords();
        records.searchresult = searchText;
        records.userloginid = SharedPreferenceUtil.getString(this, "id", "");
        records.serchtype = "1";
        if (!TextUtils.isEmpty(searchText)) {
            dao.addRole(records, "1");
        }
        getSearchResult(searchText);
    }

    public void forSearchHistory(String searchText) {
        getSearchResult(searchText);
    }

    public void getSearchResult(String keyword) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(this).getSesstion());
        maps.put("type", 0 + "");
        maps.put("keyword", keyword);
        maps.put("pageIndex", "1");
        maps.put("pageSize", "100");
        new HttpManager().post(this, Constants.SEARCHHOSPITALS, HospitalList.class,
                maps, this,
                false, 1);
        showLoadingDialog();

    }

    public void addHospital(String ids) {

        HashMap<String, String> maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(this).getSesstion());
        maps.put("userId", SharedPreferenceUtil.getString(this, "id", ""));
        maps.put("goodsGroupId", id);
        maps.put("hospitalIds", ids);
        new HttpManager().post(this, Constants.ADDHOSPITAL, Results.class,
                maps, this,
                false, 1);
        showLoadingDialog();

    }

    @Override
    public void onBackPressed() {
        finish();
        // super.onBackPressed();
        // this.mApplication.getActivityManager().finishActivity(this.getClass());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void getRelationShip() {
        HashMap<String, String> interfaces = new HashMap<String, String>();
        interfaces.put("access_token", UserInfo.getInstance(this).getSesstion());
        interfaces.put("userId", SharedPreferenceUtil.getString(this, "id", ""));
        new HttpManager().get(this,
                Constants.GETMANAGERHOSPITAL,
                HospitalManager.class,
                interfaces,
                this, false, 1);

    }

}
