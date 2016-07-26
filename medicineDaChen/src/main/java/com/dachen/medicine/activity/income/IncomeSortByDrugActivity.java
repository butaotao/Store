package com.dachen.medicine.activity.income;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.dachen.medicine.R;
import com.dachen.medicine.activity.BaseActivity;
import com.dachen.medicine.adapter.IncomeSortByDrugAdapter;
import com.dachen.medicine.app.Constants;
import com.dachen.medicine.bean.IncomeListPageData;
import com.dachen.medicine.bean.IncomeListResponse;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 总收入和待审核收入页面
 * Created by TianWei on 2016/3/15.
 */
public class IncomeSortByDrugActivity extends BaseActivity
        implements OnHttpListener, OnItemClickListener, PullToRefreshBase.OnRefreshListener2<ListView> {
    public static String FROM_TAG_ALL_HAVE = "drug_all_have";
    public static String FROM_TAG_WAIT_AUDIT = "drug_wait_audit";
    @Nullable
    @Bind(R.id.tv_title)

    TextView mTitleTv;
    private String mFrom;
    private int mPageIndex = 0;
    private int mPageSize = 15;
    private List<IncomeListPageData> mPageData;
    private IncomeSortByDrugAdapter mAdapter;
    private String mBizId;

    @Nullable
    @OnClick(R.id.rl_back)
    void onBackClicked() {
        finish();
    }

    @Nullable
    @Bind(R.id.refreshlistview)
    PullToRefreshListView mRefreshListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }


    private void initView() {
        setContentView(R.layout.activity_income_sort_by_drug);
        ButterKnife.bind(this);
        mFrom = getIntent().getStringExtra("from");
        if (!TextUtils.isEmpty(mFrom)) {
            if (mFrom.equals(IncomeActivity.FROM_TAG_ALL)) {
                mTitleTv.setText("总收入金额明细");
            } else if (mFrom.equals(IncomeActivity.FROM_TAG_WAIT_AUDIT)) {
                mTitleTv.setText("待审核金额明细");
            }
        }
        mRefreshListView.setOnItemClickListener(this);
        mRefreshListView.setOnRefreshListener(this);
        mAdapter = new IncomeSortByDrugAdapter(this, R.layout.item_income_sort_by_drug);
        mRefreshListView.setAdapter(mAdapter);
        mRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        showLoadingDialog();
        initData();
    }

    private void initData() {
        mBizId = SharedPreferenceUtil.getString("id", "");
        HttpManager httpManager = new HttpManager();
        if (mFrom.equals(IncomeActivity.FROM_TAG_ALL)) {
            httpManager.get(this, Constants.INCOME_ALL_LIST, IncomeListResponse.class, Params.getAllIncomeList
                    (mBizId, mPageIndex + "", mPageSize + ""), this, false, 3);
        } else if (mFrom.equals(IncomeActivity.FROM_TAG_WAIT_AUDIT)) {
            String shop_manager = SharedPreferenceUtil.getString("shop_manager", "");
            String bizType = TextUtils.isEmpty(shop_manager) ? "1" : "2";
            httpManager.get(this, Constants.INCOME_UN_CHECK_LIST, IncomeListResponse.class, Params.getUnCheckIncomeList
                    (mBizId, bizType, mPageIndex + "", mPageSize + ""), this, false, 3);
        }
    }

    @Override
    public void onSuccess(Result response) {
        super.onSuccess(response);
        closeLoadingDialog();
        mRefreshListView.onRefreshComplete();
        if (response.resultCode == 1) {
            if (response instanceof IncomeListResponse) {
                IncomeListResponse data = (IncomeListResponse) response;
                if (data != null && data.data != null) {
                    mPageData = data.data.pageData;
                    if (mPageIndex == 0) {
                        mAdapter.clear();
                    }
                    mAdapter.addItems(mPageData);
                    mAdapter.notifyDataSetChanged();
                    data.data.doPageInfo(mRefreshListView, mPageIndex + 1, data.data.getTotal(), 15);
                }
            }
        } else {
            ToastUtils.showToast(response.resultMsg);
        }


    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        super.onFailure(e, errorMsg, s);
        closeLoadingDialog();
        mRefreshListView.onRefreshComplete();
        ToastUtils.showToast(errorMsg);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        IncomeListPageData data = mPageData.get(position - 1);
        if (mFrom.equals(IncomeActivity.FROM_TAG_ALL)) {
            Intent intent = new Intent(this, IncomeMonthDetailActivity.class);
            intent.putExtra("from", FROM_TAG_ALL_HAVE);
            intent.putExtra("data", data);
            startActivity(intent);
        }
        if (mFrom.equals(IncomeActivity.FROM_TAG_WAIT_AUDIT)) {
            Intent intent = new Intent(this, IncomeMonthDetailActivity.class);
            intent.putExtra("from", FROM_TAG_WAIT_AUDIT);
            intent.putExtra("data", data);
            startActivity(intent);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        mPageIndex = 0;
        initData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        mPageIndex++;
        initData();
    }
}
