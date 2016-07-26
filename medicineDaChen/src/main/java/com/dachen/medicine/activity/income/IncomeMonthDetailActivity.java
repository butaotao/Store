package com.dachen.medicine.activity.income;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.incomelibrary.utils.ConstantsApp;
import com.dachen.medicine.R;
import com.dachen.medicine.activity.BaseActivity;
import com.dachen.medicine.activity.WebActivity;
import com.dachen.medicine.adapter.IncomeMonthDetailAdapter;
import com.dachen.medicine.app.Constants;
import com.dachen.medicine.bean.IncomeDetailBean;
import com.dachen.medicine.bean.IncomeDetailResponse;
import com.dachen.medicine.bean.IncomeListPageData;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;
import com.handmark.pulltorefresh.library.PinnedHeaderExpandableListView;
import com.handmark.pulltorefresh.library.PinnedHeaderExpandableListView.OnHeaderUpdateListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshPinHeaderExpandableListView;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 账户余额 总收入 待审核收入详情页
 * Created by TianWei on 2016/3/15.
 */
public class IncomeMonthDetailActivity extends BaseActivity implements OnHttpListener, OnHeaderUpdateListener,
        ExpandableListView.OnGroupClickListener, PullToRefreshBase.OnRefreshListener2<PinnedHeaderExpandableListView> {
    private LinearLayout mGuizeLayout;
    @Nullable
    @Bind(R.id.tv_title)
    TextView mTitleTv;
    private String mFrom;
    private List<IncomeDetailBean> mPageData;
    private IncomeMonthDetailAdapter mAdapter;
    private String mBizId;

    @Nullable
    @Bind(R.id.vstub_title)
    ViewStub mTitleVs;

    @Nullable
    @Bind(R.id.ll_sub)
    RelativeLayout mStubLayout;

    @Nullable
    @OnClick(R.id.rl_back)
    void onBackClicked() {
        finish();
    }

    @Nullable
    @Bind(R.id.refreshlistview)
    PullToRefreshPinHeaderExpandableListView mRefreshListView;

    private int mPageIndex = 0;
    private int mPageSize = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_income_month);
        ButterKnife.bind(this);
        mFrom = getIntent().getStringExtra("from");
        mAdapter = new IncomeMonthDetailAdapter(this);
        mRefreshListView.getRefreshableView().setAdapter(mAdapter);
        mRefreshListView.getRefreshableView().setOnHeaderUpdateListener(this);
        mRefreshListView.getRefreshableView().setOnGroupClickListener(this, false);
        mRefreshListView.setDescendantFocusability(ExpandableListView.FOCUS_AFTER_DESCENDANTS);
        mRefreshListView.setOnRefreshListener(this);
        mRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mBizId = SharedPreferenceUtil.getString("id", "");
        if (!TextUtils.isEmpty(mFrom)) {
            if (mFrom.equals(IncomeActivity.FROM_TAG_WAIT_GIVE)) {
                View view = mTitleVs.inflate(this, R.layout.layout_sub_scantitletext, mStubLayout);
                mGuizeLayout = (LinearLayout) view.findViewById(R.id.ll_guize);
                TextView guizeTv = (TextView) view.findViewById(R.id.edit_title);
                guizeTv.setText("余额说明");
                mGuizeLayout.setOnClickListener(this);
            }
        }
        showLoadingDialog();
        getData();
    }

    private void getData() {
        HttpManager httpManager = new HttpManager();
        if (!TextUtils.isEmpty(mFrom)) {
            if (mFrom.equals(IncomeActivity.FROM_TAG_WAIT_GIVE)) {//余额明细
                mTitleTv.setText("余额明细");
                httpManager.get(this, Constants.INCOME_BALANCE_DETAIL, IncomeDetailResponse.class, Params.getBlanceDetail(mBizId,
                        mPageIndex + "", mPageSize + ""), this, false, 3);
                mAdapter.setFrom(IncomeActivity.FROM_TAG_WAIT_GIVE);

            } else if (mFrom.equals(IncomeSortByDrugActivity.FROM_TAG_ALL_HAVE)) {//总收入详细
                Serializable data = getIntent().getSerializableExtra("data");
                if (data instanceof IncomeListPageData) {
                    IncomeListPageData pageData = (IncomeListPageData) data;
                    mTitleTv.setText(pageData.drugName + "收入明细");
                    httpManager.get(this, Constants.INCOME_ALL_DETAIL, IncomeDetailResponse.class, Params.getAllIncomeDetail(
                            mBizId, pageData.goodsId, mPageIndex + "", mPageSize + ""), this, false, 3);
                    mAdapter.setFrom(IncomeSortByDrugActivity.FROM_TAG_ALL_HAVE);
                }
            } else if (mFrom.equals(IncomeSortByDrugActivity.FROM_TAG_WAIT_AUDIT)) {//待审核详细
                Serializable data = getIntent().getSerializableExtra("data");
                if (data instanceof IncomeListPageData) {
                    IncomeListPageData pageData = (IncomeListPageData) data; ;
                    mTitleTv.setText(pageData.drugName + "收入明细");
                    String shop_manager = SharedPreferenceUtil.getString("shop_manager", "");
                    String bizType = TextUtils.isEmpty(shop_manager) ? "1" : "2";
                    httpManager.get(this, Constants.INCOME_UN_CHECK_DETAIL, IncomeDetailResponse.class,
                            Params.getUnCheckIncomeDetail(mBizId, bizType,pageData.goodsId, mPageIndex + "", mPageSize + ""),
                            this, false, 3);
                    mAdapter.setFrom(IncomeSortByDrugActivity.FROM_TAG_WAIT_AUDIT);
                }
            }
        }
    }

    @Override
    public void onSuccess(Result response) {
        super.onSuccess(response);
        closeLoadingDialog();
        mRefreshListView.onRefreshComplete();
        if (response.resultCode == 1) {
            if (response instanceof IncomeDetailResponse) {
                IncomeDetailResponse data = (IncomeDetailResponse) response;
                if (data != null && data.data != null) {
                    mPageData = data.data.pageData;
                    if (mPageIndex == 0) {
                        mAdapter.clear();
                    }
                    mAdapter.addData(mPageData);
                    mAdapter.notifyDataSetChanged();
                    setExpandableListView();
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
    public View getPinnedHeader() {
        View headerView = getLayoutInflater().inflate(R.layout.item_income_month_detail_group, null);
        headerView.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return headerView;
    }

    @Override
    public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
        TextView textView = (TextView) headerView.findViewById(R.id.tv_month);
        if (firstVisibleGroupPos < 0) {
            return;
        }
        IncomeDetailBean bean = (IncomeDetailBean) mAdapter.getGroup(firstVisibleGroupPos);
        textView.setText(bean.yM);
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return true;
    }

    private void setExpandableListView() {
        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
            mRefreshListView.getRefreshableView().expandGroup(i);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<PinnedHeaderExpandableListView> refreshView) {
        mPageIndex = 0;
        getData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<PinnedHeaderExpandableListView> refreshView) {
        mPageIndex++;
        getData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_guize:
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra(ConstantsApp.GOTO_WEBFROM, ConstantsApp.FROM_INCOME_BALANCE_INTRO);
                intent.putExtra(ConstantsApp.GOTO_WEBACTIVITY, com.dachen.incomelibrary.utils.Constants.INCOME_BLANCE_RULE);
                startActivity(intent);
                break;
        }
    }
}

