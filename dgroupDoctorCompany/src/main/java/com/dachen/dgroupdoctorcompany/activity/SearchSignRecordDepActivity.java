package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.SearchSignRecordDepAdatper;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.RecordDept;
import com.dachen.dgroupdoctorcompany.entity.UserInfos;
import com.dachen.dgroupdoctorcompany.entity.VisitRecordUserInfo;
import com.dachen.dgroupdoctorcompany.utils.DataUtils.GetUserDepId;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Burt on 2016/6/23.
 */
public class SearchSignRecordDepActivity extends BaseActivity implements HttpManager.OnHttpListener,PullToRefreshBase.OnRefreshListener2<ListView> {
    public String deptId;
    public String selectday;
    EditText et_search;
    PullToRefreshListView listview;
    SearchSignRecordDepAdatper adatper;
    ArrayList<UserInfos> infos;
    RelativeLayout rl_back;
    public String type;;
    RelativeLayout rl_notcontent;
    TextView tv_empty;
    private int pageIndex = 0;
    private int pageSize = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchsignrecorddep);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        rl_back.setVisibility(View.GONE);
        infos = new ArrayList<>();
        showSearchTitleView();
        type = getIntent().getStringExtra("type");
        rl_notcontent = (RelativeLayout) findViewById(R.id.rl_notcontent);
        listview = (PullToRefreshListView) findViewById(R.id.listview);
        listview.setEmptyView(rl_notcontent);
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        listview.setDescendantFocusability(ExpandableListView.FOCUS_AFTER_DESCENDANTS);
        listview.setOnRefreshListener(this);
        adatper = new SearchSignRecordDepAdatper(this);
        listview.setAdapter(adatper);
        rl_notcontent.setVisibility(View.GONE);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserInfos info = (UserInfos) adatper.getItem(position-1);
                Intent intent ;
                intent= new Intent(SearchSignRecordDepActivity.this, SearchSignRecordActivity.class);
                if (type.equals("visit")){
                    intent= new Intent(SearchSignRecordDepActivity.this,SearchVisitRecordActivity .class);
                }else if (type.equals("sign")){
                    intent= new Intent(SearchSignRecordDepActivity.this, SearchSignRecordActivity.class);
                }

                intent.putExtra("deptId",info.orgId);
                intent.putExtra("userId",info.userId);
                intent.putExtra("userName",info.name);
                intent.putExtra("deptName",info.departmentNmae);
                startActivity(intent);
            }
        });
    }

    private void getVistRecord(String text) {
        HashMap<String, String> maps = new HashMap<>();
//        String s = Constants.SIGNEDRECORD;
        String s = Constants.GETVISITPEOPLE;//接口统一改成这个了
        String orgId = GetUserDepId.getUserDepId(this);
        maps.put("access_token", UserInfo.getInstance(this).getSesstion());
        maps.put("drugCompanyId", SharedPreferenceUtil.getString(this, "enterpriseId", ""));
        maps.put("keyword",text);
        maps.put("pageSize",String.valueOf(pageSize));
        maps.put("pageIndex",String.valueOf(pageIndex));
        maps.put("orgId", orgId);
        new HttpManager().post(this, s, VisitRecordUserInfo.class,
                maps, this,
                false, 1);

//        if (type.equals("visit")) {
//
//            maps.put("access_token", UserInfo.getInstance(this).getSesstion());
//            maps.put("drugCompanyId", SharedPreferenceUtil.getString(this, "enterpriseId", ""));
//            maps.put("keyword",text);
//            maps.put("pageSize","1000");
//            maps.put("pageIndex","0");
//            maps.put("orgId", orgId);
//            new HttpManager().post(this, s, VisitRecordUserInfo.class,
//                    maps, this,
//                    false, 1);
//
//        }else {
//
//            maps.put("access_token", UserInfo.getInstance(this).getSesstion());
//            maps.put("userId", SharedPreferenceUtil.getString(this, "id", ""));
//            maps.put("companyId", SharedPreferenceUtil.getString(this, "enterpriseId", ""));
//            maps.put("time", /*"2016-06-28"*/TimeUtils.getTimeDay());
//            maps.put("state", "0");
//            maps.put("orgId", orgId);
//            maps.put("name", text);
//            new HttpManager().post(this, s, RecordDept.class,
//                    maps, this,
//                    false, 4);
//        }
        showLoadingDialog();




    }

    @Override
    public void onSuccess(Result response) {
        closeLoadingDialog();
        listview.onRefreshComplete();
//        rl_notcontent.setVisibility(View.VISIBLE);
        if (response.getResultCode() ==1){
            if (response instanceof RecordDept) {
                infos.clear();
                RecordDept s = (RecordDept) response;
                if (s.data != null && s.data.data != null&&s.data.data.size()>0) {
                    infos.addAll(s.data.data);
                    adatper.notifyDataSetChanged();
                    rl_notcontent.setVisibility(View.GONE);
                }

            }else if (response instanceof VisitRecordUserInfo){
                infos.clear();
                VisitRecordUserInfo info = (VisitRecordUserInfo)response;
                if (null!=info&&info.data.pageData.size()>0){
                    rl_notcontent.setVisibility(View.GONE);
                    for (int i=0,size = info.data.pageData.size();i<size;i++){
                        VisitRecordUserInfo.Data.PageData data = info.data.pageData.get(i);
                        UserInfos userInfo = new UserInfos();
                        userInfo.departmentNmae = data.department;
                        userInfo.orgId = data.id;
                        userInfo.userId = data.userId;
                        userInfo.headPic = data.headPicFileName;
                        userInfo.name = data.name;
                        infos.add(userInfo);
                    }
                    if(pageIndex==0){
                        adatper.addData(infos,true);

                    }else{
                        adatper.addData(infos,false);
                    }
                    adatper.notifyDataSetChanged();
                }else{
                    if(pageIndex>0){
                        ToastUtil.showToast(this,"已经没有更多内容了");
                        listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }else {
                        adatper.clearData();
                        adatper.notifyDataSetChanged();
                    }
                }
            }
        }else {
            ToastUtil.showToast(this,response);
        }

    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        closeLoadingDialog();
        listview.onRefreshComplete();
    }

    public void showSearchTitleView() {
        ViewStub vstub_title = (ViewStub) findViewById(R.id.vstub_title);
        RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.ll_sub);
        View view = vstub_title.inflate(this, R.layout.layout_searchrecord_import, rl);
        et_search = (EditText) view.findViewById(R.id.et_search);
        et_search.setHint("搜索姓名");


        et_search.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            // 在这里编写自己想要实现的功能
                            String text = et_search.getText().toString();
                            if (!TextUtils.isEmpty(text)) {
                                pageIndex = 0;
                                listview.setMode(PullToRefreshBase.Mode.BOTH);
                                tv_empty.setText("没有”"+text+"“的相关搜索结果");
                                getVistRecord(text);
                            } else {
                                tv_empty.setText("没有相关搜索结果");
                                ToastUtil.showToast(SearchSignRecordDepActivity.this, "搜索内容不能为空");
                            }
                        }
                        return false;
                    }
                });
        TextView view1 = (TextView) view.findViewById(R.id.btn_record_search);
        view1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_record_search:
                finish();
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        String text = et_search.getText().toString();
        if (!TextUtils.isEmpty(text)) {
            pageIndex = 0;
            listview.setMode(PullToRefreshBase.Mode.BOTH);
            tv_empty.setText("没有”"+text+"“的相关搜索结果");
            getVistRecord(text);
        } else {
            tv_empty.setText("没有相关搜索结果");
            ToastUtil.showToast(SearchSignRecordDepActivity.this, "搜索内容不能为空");
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        String text = et_search.getText().toString();
        if (!TextUtils.isEmpty(text)) {
            pageIndex++;
            tv_empty.setText("没有”"+text+"“的相关搜索结果");
            getVistRecord(text);
        } else {
            tv_empty.setText("没有相关搜索结果");
            ToastUtil.showToast(SearchSignRecordDepActivity.this, "搜索内容不能为空");
        }
    }
}
