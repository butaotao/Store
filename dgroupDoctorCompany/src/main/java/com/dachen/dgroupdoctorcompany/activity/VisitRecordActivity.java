package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.entity.VistDatas;
import com.dachen.dgroupdoctorcompany.utils.DataUtils.GetUserDepId;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.net.HttpManager;
import com.handmark.pulltorefresh.library.PinnedHeaderExpandableListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.util.HashMap;

/**
 * Created by Burt on 2016/6/23.
 */
public class VisitRecordActivity extends BaseRecordActivity implements PullToRefreshBase.OnRefreshListener2<PinnedHeaderExpandableListView>{
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        record = new BaseRecordActivity.GetRecord() {
            @Override
            public void initConfig() {
                pageIndex = 0;
                mPullToRefreshPinHeaderExpandableListView.setMode(PullToRefreshBase.Mode.BOTH);
            }

            @Override
            public void getRecord(String text) {
                getVistRecord();
            }
        };
        state = new GetState() {
            @Override
            public int getState() {
                return 2;
            }
        };
        record.getRecord("");
        showSearchText(true);
        setTitle("拜访统计");
        initData();
    }

    private void initData(){
        mPullToRefreshPinHeaderExpandableListView.setOnRefreshListener(this);
    }

    private void getVistRecord() {
        managers.clear();
        showLoadingDialog();
//        deptId = GetUserDepId.getUserDepId(this);
        HashMap<String ,String > maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(this).getSesstion());
        maps.put("userId", SharedPreferenceUtil.getString(this, "id", ""));
        maps.put("companyId", SharedPreferenceUtil.getString(this, "enterpriseId", ""));
        maps.put("orgId", deptId);
        maps.put("time", selectday);
        maps.put("pageIndex",String.valueOf(pageIndex));
        maps.put("pageSize",String.valueOf(pageSize));
     /* maps.put("userId", "145261");
        maps.put("companyId", "5745675f4f19d15769f3dc3d");
        maps.put("orgId", "575e6f8ad1badb56a7fca510");*/
        new HttpManager().post(this, Constants.VISITRECORD, VistDatas.class,
                maps, this,
                false, 4);
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_search:
                Intent intent = new Intent(this,SearchSignRecordDepActivity.class);
                intent.putExtra("type",type);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<PinnedHeaderExpandableListView> refreshView) {
        mPullToRefreshPinHeaderExpandableListView.setMode(PullToRefreshBase.Mode.BOTH);
        pageIndex = 0;
        getVistRecord();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<PinnedHeaderExpandableListView> refreshView) {
        pageIndex++;
        getVistRecord();
    }
}
