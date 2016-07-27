package com.dachen.dgroupdoctorcompany.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.VisitRecordAdapter;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.DepAdminsListDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.DepAdminsList;
import com.dachen.dgroupdoctorcompany.entity.ChoiceDepList;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.entity.SignedRecordLists;
import com.dachen.dgroupdoctorcompany.entity.SignedRecords;
import com.dachen.dgroupdoctorcompany.entity.TogetherVisit;
import com.dachen.dgroupdoctorcompany.entity.VistDatas;
import com.dachen.dgroupdoctorcompany.entity.VistRecorddata;
import com.dachen.dgroupdoctorcompany.entity.VistRecorddataList;
import com.dachen.dgroupdoctorcompany.utils.JsonUtils.SingRecordTrans;
import com.dachen.dgroupdoctorcompany.utils.TitleManager;
import com.dachen.dgroupdoctorcompany.views.ChoiceDepDialog;
import com.dachen.dgroupdoctorcompany.views.ChoiceStateDialog;
import com.dachen.dgroupdoctorcompany.views.ChoiceTimeDialog;
import com.dachen.dgroupdoctorcompany.views.PreDatePicker;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.TimeUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshPinHeaderExpandableListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Burt on 2016/6/28.
 */
public class BaseRecordActivity extends BaseActivity implements View.OnClickListener, HttpManager
        .OnHttpListener {
    public String type;
    ArrayList<DepAdminsList> deps;
    DepAdminsListDao managerDao;
    CompanyContactDao contactDao;
    ImageView ivclose;
    ChoiceDepDialog depDialog;
    ChoiceStateDialog choicestateDialog;
    TextView textstate;
    ChoiceTimeDialog choiceTimeDialog;
    TextView departdes;
    public String selectday;
    VisitRecordAdapter adapter;
    public ArrayList<SignedRecords> managers;
    ArrayList<SignedRecords> managersOrgin;
    ArrayList<VistRecorddataList> orginData;
    ExpandableListView expandableListView;
    public PullToRefreshPinHeaderExpandableListView mPullToRefreshPinHeaderExpandableListView;
    ImageView iv_dep;
    ImageView iv_day;
    ImageView iv_state;
    TextView textdescandler;
    GetRecord record;
    View view;
    RelativeLayout rl_back;
    LinearLayout ll_select;
    ViewStub vstub_title;
    EditText et_search;
    public GetState state;
    int selectState;
    public static String deptId;
    boolean disablesearch = false;
    TextView tv_notcontent;
    ImageView iv_nocontent;

    public int pageIndex = 0;
    public int pageSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_singrecord, null);
        setContentView(view);
        enableBack();
        orginData = new ArrayList<>();
        managersOrgin = new ArrayList<>();
        tv_notcontent = (TextView) findViewById(R.id.tv_notcontent);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        contactDao = new CompanyContactDao(this);
        mPullToRefreshPinHeaderExpandableListView = (PullToRefreshPinHeaderExpandableListView)
                findViewById(R.id.list);
        expandableListView = mPullToRefreshPinHeaderExpandableListView.getRefreshableView();
        mPullToRefreshPinHeaderExpandableListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshPinHeaderExpandableListView.setDescendantFocusability(ExpandableListView.FOCUS_AFTER_DESCENDANTS);
        ll_select = (LinearLayout) findViewById(R.id.ll_select);
        selectState = 0;
        departdes = (TextView) findViewById(R.id.departdes);
        iv_nocontent = (ImageView) findViewById(R.id.iv_nocontent);
        findViewById(R.id.rl_day).setOnClickListener(this);
        findViewById(R.id.rl_depart).setOnClickListener(this);
        findViewById(R.id.rl_state).setOnClickListener(this);
        textstate = (TextView) findViewById(R.id.textstate);
        textstate.setTextColor(getResources().getColor(R.color.color_9eaaaa));
        type = getIntent().getStringExtra("type");
        textdescandler = (TextView) findViewById(R.id.textdescandler);
        deps = new ArrayList<>();
        managers = new ArrayList<>();
        adapter = new VisitRecordAdapter(this);
        selectday = TimeUtils.getTimeDay();
        iv_dep = (ImageView) findViewById(R.id.iv_dep);
        iv_day = (ImageView) findViewById(R.id.iv_day);
        iv_state = (ImageView) findViewById(R.id.iv_state);
        iv_dep.setOnClickListener(this);
        iv_day.setOnClickListener(this);
        iv_state.setOnClickListener(this);
        iv_dep.setBackgroundResource(R.drawable.recordirro);
        iv_day.setBackgroundResource(R.drawable.recordirro);
        iv_state.setBackgroundResource(R.drawable.recordirro);
        managerDao = new DepAdminsListDao(this);
        deptId = "";
        List<CompanyContactListEntity> entities = contactDao.queryByTelephone
                (SharedPreferenceUtil.getString(this, "telephone", ""));
        if (entities.size() > 0) {
            deptId = entities.get(0).id;
        } else {
            deptId = SharedPreferenceUtil.getString(this, "departId", "");
        }


        expandableListView.setAdapter(adapter);
        mPullToRefreshPinHeaderExpandableListView.setEmptyView(findViewById(R.id.ll_nocontent));
    }

    @Override
    protected void onStart() {
        super.onStart();

        final int states = state.getState();
        if(states==2){
            textstate.setTextColor(getResources().getColor(R.color.color_333333));
        }else{
            textstate.setTextColor(getResources().getColor(R.color.color_9eaaaa));
        }
        depDialog = new ChoiceDepDialog(this,
                departdes,
                iv_dep,
                record);
        choicestateDialog = new ChoiceStateDialog(this,
                textstate,
                iv_state,
                states,
                record, new RefreshData() {
            @Override
            public int refresh(int state) {
                if (managers != null) {
                    if (states == 3) {
                        insitDatasign(state, managersOrgin);
                    } else {
                        initVisit(state, orginData);
                    }

                }
                return 0;
            }
        });


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_day:
                selectDate(iv_day);
                //choiceTimeDialog.show();
                break;
            case R.id.rl_depart:
                getDep();
                break;
            case R.id.rl_state:
                final int states = state.getState();
                if(states==2){
                    choicestateDialog.showDialog();
                }else{
                    if (!selectday.equals(TimeUtils.getTimeDay())) {
                        choicestateDialog.showDialog();
                    }
                }
                break;
            case R.id.btn_record_search:
                finish();
                break;

        }
    }

    //请求网络数据
    public interface GetRecord {
        void initConfig();
        void getRecord(String text);
    }

    //得到到底是哪个anctivity
    public interface GetState {
        int getState();
    }

    //筛选数据
    public interface RefreshData {
        int refresh(int state);
    }

    private void getDep() {
        showLoadingDialog();
        HashMap<String, String> maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(this).getSesstion());
        maps.put("userId", SharedPreferenceUtil.getString(this, "id", ""));
        maps.put("drugCompanyId", SharedPreferenceUtil.getString(this, "enterpriseId", ""));
        new HttpManager().post(this, Constants.MANINDEP, ChoiceDepList.class,
                maps, this,
                false, 1);
    }

    @Override
    public void onSuccess(Result response) {
        closeLoadingDialog();
        mPullToRefreshPinHeaderExpandableListView.onRefreshComplete();
        int state = getSelectState();
        if (response.resultCode == 1) {
            if (response instanceof ChoiceDepList) {

                if (response instanceof ChoiceDepList) {
                    ChoiceDepList list = (ChoiceDepList) response;
                    if (null != list.data) {
                        SingRecordTrans.processData(this, list.data);
                        depDialog.showDialog(list.data.orgId);
                    }

                }

            } else if (response instanceof SignedRecordLists) {
                SignedRecordLists lists = (SignedRecordLists) response;
                if (null != lists.data && null != lists.data.pageData && lists.data.pageData.size()>0) {
                    managers = lists.data.pageData;
                    managersOrgin.clear();
                    managersOrgin.addAll(lists.data.pageData);
                    insitDatasign(state, managersOrgin);

                } else if (null != lists.data && null != lists.data.data && null != lists.data.data.pageData && lists.data.data.pageData.size()>0) {
                    managers = lists.data.data.pageData;
                    managersOrgin.clear();
                    managersOrgin.addAll(lists.data.data.pageData);
                    insitDatasign(state, managersOrgin);

                } else {
                    if(pageIndex>0){
                        ToastUtil.showToast(this,"已经没有更多内容了");
                        mPullToRefreshPinHeaderExpandableListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }else{
                        adapter.clearData();
                        adapter.notifyDataSetChanged();
                    }
//                    managers = new ArrayList<>();
//                    adapter = new VisitRecordAdapter(this, managers, false);
//                    expandableListView.setAdapter(adapter);
//                    expandableListView.setEmptyView(findViewById(R.id.ll_nocontent));
                }
                setEmptyView(1);
            } else if (response instanceof VistDatas) {
                managers = new ArrayList<>();
                VistDatas datas = (VistDatas) response;
                if ((null != datas.data && null != datas.data.data && null != datas.data.data
                        .pageData
                        && datas.data.data.pageData.size() > 0)
                        ) {
                    ArrayList<VistRecorddataList> lists = datas.data.data.pageData;
                    orginData.clear();
                    orginData = lists;
                    initVisit(state, lists);
                } else if (null != datas.data && null != datas.data.pageData && datas.data
                        .pageData.size() > 0) {
                    ArrayList<VistRecorddataList> lists = datas.data.pageData;
                    orginData.clear();
                    orginData = lists;
                    initVisit(state, lists);
                } else {
                    if(pageIndex>0){
                        ToastUtil.showToast(this,"已经没有更多内容了");
                        mPullToRefreshPinHeaderExpandableListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }else {
                        adapter.clearData();
                        adapter.notifyDataSetChanged();
                    }
//                    managers = new ArrayList<>();
//                    adapter = new VisitRecordAdapter(this, managers, true);
//                    expandableListView.setAdapter(adapter);

                }
                setEmptyView(2);
            }

        } else {
            ToastUtil.showToast(this, response);
        }
    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    public void initVisit(int state, ArrayList<VistRecorddataList> lists) {
        managers.clear();
        for (int i = 0, size = lists.size(); i < size; i++) {
            SignedRecords signedRecords = new SignedRecords();

            VistRecorddataList data = lists.get(i);
            signedRecords.name = data.name;
            signedRecords.headPic = data.headPic;
            signedRecords.time = data.time;
            if (null != data.visitList && data.visitList.size() > 0) {
                ArrayList<VistRecorddata> visitLiss = new ArrayList<>();
                ArrayList<VistRecorddata> visitList = data.visitList;
                for (int j = 0, sizes = visitList.size(); j < sizes; j++) {
                    VistRecorddata recorddata = visitList.get(j);
                    VistRecorddata d = new VistRecorddata();
                    if (state == 0) {
                        d = data.visitList.get(j);
                        visitLiss.add(d);
                    } else if (state == 2) {
                        if (!TextUtils.isEmpty(recorddata.type) && recorddata.type.equals("2")) {
                            d = data.visitList.get(j);
                            visitLiss.add(d);
                        }
                    } else if (state == 1) {
                        //type ==0 独立拜访
                        if (!TextUtils.isEmpty(recorddata.type) && recorddata.type.equals("0")) {
                            d = data.visitList.get(j);
                            visitLiss.add(d);

                        }
                    }
                    signedRecords.vistlist = visitLiss;
                }
                if (state == 0) {
                    managers.add(signedRecords);
                } else if (null != signedRecords.vistlist && signedRecords.vistlist.size() > 0) {
                    signedRecords.isVisit = true;
                    managers.add(signedRecords);
                }else {
                    managers.add(signedRecords);
                }

            } else {
                managers.add(signedRecords);

            }

        }

        if(pageIndex == 0){
            adapter.addData(managers,true,true);
        }else{
            adapter.addData(managers,false,true);
        }
        adapter.notifyDataSetChanged();
//        adapter = new VisitRecordAdapter(this, managers, true);
//        expandableListView.setAdapter(adapter);
//        expandableListView.setEmptyView(findViewById(R.id.ll_nocontent));
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            expandableListView.expandGroup(i);
        }
        expandableListView.setGroupIndicator(null);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int
                    childPosition, long id) {
                if (adapter.getChild(groupPosition, childPosition) instanceof VistRecorddata) {
                    VistRecorddata data = (VistRecorddata) adapter.getChild(groupPosition,
                            childPosition);
                    Intent intent = new Intent(BaseRecordActivity.this, VisitDetailActivity.class);
                    intent.putExtra("id", data.id);
                    startActivity(intent);
                }

                return false;
            }
        });

    }

    public void insitDatasign(int state, ArrayList<SignedRecords> orgdata) {
        ArrayList<SignedRecords> managersChange = new ArrayList<>();
        if (orgdata.size() > 0) {
            for (int i = 0; i < orgdata.size(); i++) {
                SignedRecords record = orgdata.get(i);
                if (state == 0) {
                    managersChange.add(record);

                } else if (state == 1) {
                    if (!TextUtils.isEmpty(record.state) && (record.state.equals("1")||record.state.equals("3"))) {
                        managersChange.add(record);
                    }
                } else {
                    if (!TextUtils.isEmpty(record.state) && record.state.equals("2")) {
                        managersChange.add(record);
                    }
                }

            }

        }
        managers = managersChange;
        if(pageIndex == 0){
            adapter.addData(managers,true,false);
        }else{
            adapter.addData(managers,false,false);
        }
        adapter.notifyDataSetChanged();

//        adapter = new VisitRecordAdapter(this, managers, false);
//        expandableListView.setEmptyView(findViewById(R.id.ll_nocontent));
//        expandableListView.setAdapter(adapter);
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            expandableListView.expandGroup(i);
        }
        expandableListView.setGroupIndicator(null);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int
                    childPosition, long id) {
                if (adapter.getChild(groupPosition, childPosition) instanceof TogetherVisit) {
                    TogetherVisit data = (TogetherVisit) adapter.getChild(groupPosition,
                            childPosition);
                    Intent intent = new Intent(BaseRecordActivity.this, SiginDetailActivity.class);
                    intent.putExtra("day", data.day);
                    intent.putExtra("hour", data.time);
                    intent.putExtra("address", data.address);
                    intent.putExtra("longTime", data.longTime);
                    if (null != data.tag && data.tag.size() > 0 && !TextUtils.isEmpty(data.tag
                            .get(0))) {
                        intent.putExtra("tag", data.tag.get(0));
                    } else {
                        intent.putExtra("tag", "");
                    }
                    intent.putExtra("remark", data.remark);
                    startActivity(intent);
                }

                return false;
            }
        });

    }

    //1签到，2拜访，3搜索签到，4搜索拜访
    public void setEmptyView(int state) {

        if (!disablesearch) {
            if (state == 1) {
                tv_notcontent.setText("无考勤统计数据");
                iv_nocontent.setBackgroundResource(R.drawable.notsign);
            } else if (state == 2) {
                tv_notcontent.setText("无拜访统计数据");
                iv_nocontent.setBackgroundResource(R.drawable.notsign);
            }
        } else {
            if (state == 1) {
                tv_notcontent.setText("没有搜索到该员工的考勤记录");
                iv_nocontent.setBackgroundResource(R.drawable.notsigsearch);
            } else if (state == 2) {
                tv_notcontent.setText("没有搜索到该员工的拜访记录");
                iv_nocontent.setBackgroundResource(R.drawable.notsigsearch);
            }
        }
    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        closeLoadingDialog();
        mPullToRefreshPinHeaderExpandableListView.onRefreshComplete();
    }

    private void selectDate(final ImageView iv) {
        final Calendar calendar = Calendar.getInstance();
        final PreDatePicker picker = new PreDatePicker(this);
        picker.setRange(2000, calendar.get(Calendar.YEAR));
        picker.setCancelTextColor(getResources().getColor(R.color.color_3cbaff));
        picker.setSubmitTextColor(getResources().getColor(R.color.color_3cbaff));
        iv.setBackgroundResource(R.drawable.recordirro_select);

        textdescandler.setTextColor(getResources().getColor(R.color.color_3cbaff));
        picker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));
        picker.setOnDatePickListener(new PreDatePicker.OnYearMonthDayPickListener() {
                                         @Override
                                         public void onDatePicked(String year, String month,
                                                                  String day) {
                                             //ToastUtil.showToast(SignRecordActivity.this, year
                                             // + "-" + month + "-" + day);
                                             int intday = getGapCount(year, month, day);
                                             selectday = year + "-" + month + "-" + day;
                                             String daydes = year + "/" + month + "/" + day;
                                             textstate.setTextColor(getResources().getColor(R
                                                     .color.color_333333));

                                             if (intday == 0) {
                                                 daydes = "今天";
                                                 final int states = state.getState();
                                                 if(states==2){
                                                     textstate.setTextColor(getResources().getColor(R.color.color_333333));
                                                 }else{
                                                     textstate.setTextColor(getResources().getColor(R.color.color_9eaaaa));
                                                 }

                                             } else if (intday == 1) {
                                                 daydes = "昨天";
                                             } else if (intday == 2) {
                                                 daydes = "前天";
                                             }

                                             textdescandler.setText(daydes);
                                             textdescandler.setTextColor(getResources().getColor
                                                     (R.color.color_333333));
                                             iv.setBackgroundResource(R.drawable.recordirro);
                                             record.initConfig();
                                             record.getRecord("");
                                         }
                                     }
        );
        picker.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                String year = picker.getSelectedYear();
                String month = picker.getSelectedMonth();
                String day = picker.getSelectedDay();
                selectday = year + "-" + month + "-" + day;
                String daydes = year + "/" + month + "/" + day;
                int intday = getGapCount(year, month, day);
                textstate.setTextColor(getResources().getColor(R.color.color_333333));

                if (intday == 0) {
                    daydes = "今天";


                    final int states = state.getState();
                    if(states==2){
                        textstate.setTextColor(getResources().getColor(R.color.color_333333));
                        textstate.setClickable(true);
                    }else{
                        textstate.setTextColor(getResources().getColor(R.color.color_9eaaaa));
                        textstate.setClickable(false);
                    }

                } else if (intday == 1) {
                    daydes = "昨天";
                } else if (intday == 2) {
                    daydes = "前天";
                }
                textdescandler.setText(daydes);
                textdescandler.setTextColor(getResources().getColor(R.color.color_333333));
                iv.setBackgroundResource(R.drawable.recordirro);
//                record.initConfig();
//                record.getRecord("");
            }
        });
        picker.show();
    }

    /**
     * 获取两个日期之间的间隔天数
     *
     * @return
     */
    public static int getGapCount(String year, String month, String day) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.set(Calendar.YEAR, fromCalendar.get(Calendar.YEAR));
        fromCalendar.set(Calendar.MONTH, fromCalendar.get(Calendar.MONTH));
        fromCalendar.set(Calendar.DAY_OF_MONTH, fromCalendar.get(Calendar.DAY_OF_MONTH));
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.set(Calendar.YEAR, Integer.parseInt(year));
        toCalendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        toCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((fromCalendar.getTime().getTime() - toCalendar.getTime().getTime()) / (1000
                * 60 * 60 * 24));
    }

    public void enableSearch() {
        disablesearch = true;
        ll_select.setVisibility(View.GONE);
    }

    public void showSearchText(boolean enable) {
        if (enable) {
            TitleManager.showText(this, view, this, "搜索");
        } else {
            TitleManager.showText(this, view, this, "状态");
        }
    }

    public int getSelectState() {
        selectState = 0;
        String state = textstate.getText().toString();
        if (!TextUtils.isEmpty(state)) {
            if (state.contains("全部")) {
                selectState = 0;
            } else if (state.contains("独立") || state.contains("正常")) {
                selectState = 1;
            } else if (state.contains("协同") || state.contains("异常")) {
                selectState = 2;
            } else if (state.contains("待定")) {
                selectState = 3;
            }
        }
        return selectState;

    }
}
