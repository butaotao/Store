package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.VisitDetailVAdapter;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.PersonModel;
import com.dachen.dgroupdoctorcompany.entity.VistDetails;
import com.dachen.medicine.common.utils.TimeUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.CustomImagerLoader;
import com.dachen.medicine.net.HttpManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Burt on 2016/6/29.
 */
public class VisitDetailActivity extends BaseActivity implements HttpManager.OnHttpListener,View.OnClickListener{
    VisitDetailVAdapter adapter;
    String id = "";
    ListView listview;
    ArrayList<VistDetails.Sum> sums;
    TextView tvWeek;
    TextView tvDate;
    TextView tv_time_location;
    TextView tv_address;
    TextView tvSelected;
    TextView tv_variety;
    ImageView visitor_avatar1;
    ImageView visitor_avatar2;
    ImageView visitor_avatar3;
    LinearLayout ll_getvisitpeople;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vistrecorddetails);
        setTitle("拜访客户详情");
        tvWeek = (TextView) findViewById(R.id.tvWeek);
        tvDate = (TextView) findViewById(R.id.tvDate);
        ll_getvisitpeople = (LinearLayout) findViewById(R.id.ll_getvisitpeople);
        ll_getvisitpeople.setOnClickListener(this);
        visitor_avatar1 = (ImageView) findViewById(R.id.visitor_avatar1);
        visitor_avatar2 = (ImageView) findViewById(R.id.visitor_avatar2);
        visitor_avatar3 = (ImageView) findViewById(R.id.visitor_avatar3);
        tv_time_location = (TextView) findViewById(R.id.tv_time_location);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tvSelected = (TextView) findViewById(R.id.tvSelected);
        tv_variety = (TextView) findViewById(R.id.tv_variety);
        sums = new ArrayList<>();
        adapter = new VisitDetailVAdapter(this,sums);
        listview = (ListView) findViewById(R.id.listview);
        id = getIntent().getStringExtra("id");
        listview.setAdapter(adapter);
        getSingRecord();
        listview.setFocusable(false);
    }
    private void getSingRecord() {
        showLoadingDialog();
        HashMap<String ,String > maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(this).getSesstion());
        maps.put("id", id);

        new HttpManager().post(this, Constants.VISITDETAIL, VistDetails.class,
                maps, this,
                false, 4);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case  R.id.ll_getvisitpeople:
                ArrayList<PersonModel> models = new ArrayList<>();
                if (sums!=null&&sums.size()>0){
                    for (int i=0;i<sums.size();i++){
                        PersonModel model = new PersonModel();
                        model.setId(sums.get(i).userId+"");
                        model.setName(sums.get(i).name);
                        model.setHeadPic(sums.get(i).headPic);
                        models.add(model);
                    }
                }

                if (models.size()>0){
                    Gson gson = new Gson();
                    String jsonVisitPeople = gson.toJson(models);
                    Intent intent = new Intent(this,VisitMemberActivity.class);
                    intent.putExtra("jsonPeople",jsonVisitPeople);
                    startActivity(intent);
                }

                break;
        }
    }

    @Override
    public void onSuccess(Result response) {
        closeLoadingDialog();
        if (response.getResultCode()==1){
            if (response instanceof VistDetails){
                VistDetails details = (VistDetails)response;
                sums.clear();
                if (details.data!=null){
                    if (null!=details.data.visit&&null!=details.data.visit.summaryList){
                        sums.addAll(details.data.visit.summaryList);
                    }

                    VistDetails.Visit visit = details.data.visit;
                    if (null!=visit){
                        tv_address.setText(visit.address);
                        tvWeek.setText(data(visit.time));
                        tvDate.setText(TimeUtils.getTimeDay3(visit.time));
                        String time = "";
                        if (0!=visit.time){
                            time+= TimeUtils.getTimesHourMinute(visit.time);
                        }
                        if (!TextUtils.isEmpty(visit.addressName)){
                            time+=visit.addressName;
                        }
                        tv_time_location.setText(time);
                        String goods = "";
                        if (null!=visit.goodsGroups){
                            for (int i = 0;i<visit.goodsGroups.size();i++){
                                if (i!=0){
                                    goods += ","+visit.goodsGroups.get(i).name;
                                }else {
                                    goods +=  visit.goodsGroups.get(i).name;
                                }
                            }
                        }

                        tv_variety.setText(goods);
                        tvSelected.setText(visit.doctorName);
                    }

                    visitor_avatar1.setVisibility(View.GONE);
                    visitor_avatar2.setVisibility(View.GONE);
                    visitor_avatar3.setVisibility(View.GONE);
                    if (sums!=null&&sums.size()>0){
                        for (int i=0;i<sums.size();i++){
                            if (i==3){break;}
                            if (i==0){
                                CustomImagerLoader.getInstance().loadImage(visitor_avatar1, sums.get(i).headPic);
                                visitor_avatar1.setVisibility(View.VISIBLE);
                            }
                            if (i==1){
                                CustomImagerLoader.getInstance().loadImage(visitor_avatar2,sums.get(i).headPic);
                                visitor_avatar2.setVisibility(View.VISIBLE);
                            }
                            if (i==2){
                                CustomImagerLoader.getInstance().loadImage(visitor_avatar3,sums.get(i).headPic);
                                visitor_avatar3.setVisibility(View.VISIBLE);
                            }
                        }
                        adapter = new VisitDetailVAdapter(this,sums);
                        listview.setAdapter(adapter);
                       SetListViewHeight();
                    }

                }
            }
        }else {
            ToastUtil.showToast(this,response);
        }

    }
public String data(long time){
    String week = "星期";
    Calendar c = Calendar.getInstance();
    c.setTimeInMillis(time);
    if (c.get(Calendar.DAY_OF_WEEK) == 1) {
        week += "天";
    }
    if (c.get(Calendar.DAY_OF_WEEK) == 2) {
        week += "一";
    }
    if (c.get(Calendar.DAY_OF_WEEK) == 3) {
        week += "二";
    }
    if (c.get(Calendar.DAY_OF_WEEK) == 4) {
        week += "三";
    }
    if (c.get(Calendar.DAY_OF_WEEK) == 5) {
        week += "四";
    }
    if (c.get(Calendar.DAY_OF_WEEK) == 6) {
        week += "五";
    }
    if (c.get(Calendar.DAY_OF_WEEK) == 7) {
        week += "六";
    }



    return week;
}
    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        closeLoadingDialog();
    }
    /**
     * ListView根据项数的大小自动改变高度
     */
    private void SetListViewHeight() {
        ListAdapter listAdapter = listview.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listview);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
//
        ViewGroup.LayoutParams params = listview.getLayoutParams();
        params.height = totalHeight + (listview.getDividerHeight() * (listAdapter.getCount() - 1));
        ((ViewGroup.MarginLayoutParams)params).setMargins(0, 0, 0, 0);
        listview.setLayoutParams(params);
    }
}
