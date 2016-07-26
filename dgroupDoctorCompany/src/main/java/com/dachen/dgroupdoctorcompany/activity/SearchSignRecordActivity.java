package com.dachen.dgroupdoctorcompany.activity;

import android.os.Bundle;
import android.view.View;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.entity.SignedRecordLists;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.net.HttpManager;
import com.handmark.pulltorefresh.library.PinnedHeaderExpandableListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.util.HashMap;

/**
 * Created by Burt on 2016/6/23.
 */
public class SearchSignRecordActivity extends BaseRecordActivity implements PullToRefreshBase.OnRefreshListener2<PinnedHeaderExpandableListView> {
    String deptId = "";
    String deptName="";
    String userId = "";
    String userName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deptId = getIntent().getStringExtra("deptId");
        deptName = getIntent().getStringExtra("deptName");
        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("userName");
        ll_select.setVisibility(View.GONE);
        showSearchText(false);
        state = new GetState() {
            @Override
            public int getState() {
                return 3;
            }
        };
        enableSearch();
        pageIndex=0;
        mPullToRefreshPinHeaderExpandableListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshPinHeaderExpandableListView.setOnRefreshListener(this);
        adapter.clearData();
        adapter.notifyDataSetChanged();
        getVistRecord();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(userName+"-"+deptName);
    }

    private void getVistRecord() {
        managers.clear();
        showLoadingDialog();
        HashMap<String ,String > maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(this).getSesstion());
        maps.put("userId", userId);
        maps.put("companyId", SharedPreferenceUtil.getString(this, "enterpriseId", ""));
        maps.put("state","0");
        maps.put("orgId", deptId + "");
        maps.put("pageSize",String.valueOf(pageSize));
        maps.put("pageIndex",String.valueOf(pageIndex));
        new HttpManager().post(this, Constants.GETSINGINFOBYUSERID, SignedRecordLists.class,
                maps, this,
                false, 4);
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_search:
                choicestateDialog.showDialog();
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
