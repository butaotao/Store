package com.dachen.medicine.activity.income;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dachen.incomelibrary.activity.BindBankCardActivity;
import com.dachen.incomelibrary.activity.SettlementActivity;
import com.dachen.incomelibrary.bean.AccountInfo;
import com.dachen.incomelibrary.http.HttpCommClient;
import com.dachen.incomelibrary.utils.ConstantsApp;
import com.dachen.incomelibrary.utils.UserInfo;
import com.dachen.medicine.R;
import com.dachen.medicine.activity.BaseActivity;
import com.dachen.medicine.activity.WebActivity;
import com.dachen.medicine.app.Constants;
import com.dachen.medicine.bean.IncomeInfoData;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.ContextConfig;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;
import com.dachen.medicine.view.CustomDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的收入页面
 */
public class IncomeActivity extends BaseActivity implements OnHttpListener, PullToRefreshBase.OnRefreshListener2<ScrollView> {

    private LinearLayout mGuizeLayout;
    public static String FROM_TAG_ALL = "all_have";
    public static String FROM_TAG_WAIT_AUDIT = "wait_audit";
    public static String FROM_TAG_WAIT_GIVE = "wait_give";

    @Nullable
    @OnClick(R.id.rl_back)
    void onBackLayoutClicked() {
        finish();
    }

    @Nullable
    @OnClick(R.id.rl_waitgivemoney)
    void onWaitGiveMoneyClicked() {
        Intent intent = new Intent(this, IncomeMonthDetailActivity.class);
        intent.putExtra("from", IncomeActivity.FROM_TAG_WAIT_GIVE);
        startActivity(intent);
    }

    @Nullable
    @OnClick(R.id.rl_allhavemoney)
    void onAllMoneyClicked() {
        Intent intent = new Intent(this, IncomeSortByDrugActivity.class);
        intent.putExtra("from", FROM_TAG_ALL);
        startActivity(intent);
    }

    @Nullable
    @OnClick(R.id.rl_waitaudit)
    void onWaitAuditClicked() {
        Intent intent = new Intent(this, IncomeSortByDrugActivity.class);
        intent.putExtra("from", FROM_TAG_WAIT_AUDIT);
        startActivity(intent);
    }

    @Nullable
    @OnClick(R.id.rl_rule)
    void onRuleClicked() {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(ConstantsApp.GOTO_WEBFROM, ConstantsApp.FROM_INCOME_RULE);
        intent.putExtra(ConstantsApp.GOTO_WEBACTIVITY, com.dachen.incomelibrary.utils.Constants.INCOME_RULE);
        startActivity(intent);
    }

    @Nullable
    @Bind(R.id.tv_title)
    TextView mTitleTv;

    @Nullable
    @Bind(R.id.tv_waitgive)
    TextView mWaitGiveMoneyTv;

    @Nullable
    @Bind(R.id.tv_allhavemoney)
    TextView mAllHaveMoneyTv;

    @Nullable
    @Bind(R.id.tv_waitaudit)
    TextView mWaitAuditTv;

    @Nullable
    @Bind(R.id.vstub_title)
    ViewStub mTitleVs;

    @Nullable
    @Bind(R.id.ll_sub)
    RelativeLayout mStubLayout;

    @Nullable
    @Bind(R.id.refreshScrollView)
    PullToRefreshScrollView mRefreshScrollView;


    private CustomDialog dialogNotBinding;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantsApp.HANDLER_GET_BANK_CARDS:
                    closeLoadingDialog();
                    if (msg.arg1 == 1) {
                        if (msg.obj != null && msg.obj instanceof List) {
                            List<AccountInfo> accountInfoList = (List<AccountInfo>) msg.obj;
                            if (accountInfoList != null && accountInfoList.size() == 0) {
                                isShowDialog();
                            }
                        }
                    } else {
                        ToastUtils.showToast(String.valueOf(msg.obj));
                    }
            }
        }
    };

    public void isShowDialog() {
        dialogNotBinding = new CustomDialog(this, "去绑定", null);
        dialogNotBinding.showDialog(null,
                "您还未绑定收款银行卡，为了不影响您的推广费发放，请尽快绑定银行卡", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = SharedPreferenceUtil.getString("id", "");
                        String userName = SharedPreferenceUtil.getString("username", "");
                        String userType = SharedPreferenceUtil.getString("usertype", "");
                        String session = SharedPreferenceUtil.getString("session", "");
                        BindBankCardActivity.openUI(IncomeActivity.this, id, userName, userType, session, "MyIncomeActivity");
                        dialogNotBinding.dimissDialog();
                    }
                }, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        ButterKnife.bind(this);
        initView();
        getHistoryIp();
        initData();
    }

    private void initView() {
        mTitleTv.setText("我的收入");
        View view = mTitleVs.inflate(this, R.layout.layout_sub_scantitletext, mStubLayout);
        mGuizeLayout = (LinearLayout) view.findViewById(R.id.ll_guize);
        TextView guizeTv = (TextView) view.findViewById(R.id.edit_title);
        guizeTv.setText("银行卡");
        mGuizeLayout.setOnClickListener(this);
        mRefreshScrollView.setOnRefreshListener(this);

    }

    private void initData() {
        showLoadingDialog();
        HttpCommClient.getInstance().queryBindBankAccount(this, mHandler, ConstantsApp.HANDLER_GET_BANK_CARDS,
                SharedPreferenceUtil.getString("session", ""));
        new HttpManager().get(this, Constants.INCOME_BASE_INFO, IncomeInfoData.class, Params.getBaseIncomeInfo
                (SharedPreferenceUtil.getString("id", "")), this, false, 3);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_guize:
                String id = SharedPreferenceUtil.getString("id", "");
                String userName = SharedPreferenceUtil.getString("username", "");
                String userType = SharedPreferenceUtil.getString("usertype", "");
                String session = SharedPreferenceUtil.getString("session", "");
                SettlementActivity.openUI(IncomeActivity.this, id, userName, userType, session);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        if (dialogNotBinding != null) {
            dialogNotBinding.dimissDialog();
        }
        super.onBackPressed();

    }

    @Override
    public void onSuccess(Result response) {
        super.onSuccess(response);
        closeLoadingDialog();
        mRefreshScrollView.onRefreshComplete();
        if (response.resultCode == 1) {
            if (response instanceof IncomeInfoData) {
                IncomeInfoData incomeInfoData = (IncomeInfoData) response;
                if (incomeInfoData != null && incomeInfoData.data != null) {
                    long balance = incomeInfoData.data.accountBalance;
                    long unbalance = incomeInfoData.data.unAuditedAmount;
                    long totalIncome = incomeInfoData.data.totalIncome;
                    float balanceIncome = (float) balance / 100;
                    float unbalanceIncome = (float) unbalance / 100;
                    float totalIncomeData = (float) totalIncome / 100;
                    DecimalFormat df = new DecimalFormat("0.00");
                    mWaitGiveMoneyTv.setText(String.valueOf(df.format((balanceIncome))));
                    mAllHaveMoneyTv.setText(String.valueOf(df.format((totalIncomeData))));
                    mWaitAuditTv.setText(String.valueOf(df.format((unbalanceIncome))));
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
        mRefreshScrollView.onRefreshComplete();
        ToastUtils.showToast(errorMsg);

    }

    private void getHistoryIp() {
        String keyNet = UserInfo.getInstance(this).getKeyNet();
        if (!TextUtils.isEmpty(keyNet)) {
            if (keyNet.equals(ContextConfig.API_OTER_URL)) {
                com.dachen.incomelibrary.utils.Constants.changeIp(ContextConfig.API_OTER_URL);
            } else if (keyNet.equals(ContextConfig.KANG_ZHE)) {
                com.dachen.incomelibrary.utils.Constants.changeIp(ContextConfig.KANG_ZHE);
            } else if (keyNet.equals(ContextConfig.API_INNER_URL)) {
                com.dachen.incomelibrary.utils.Constants.changeIp(ContextConfig.API_INNER_URL);
            }else if (keyNet.equals(ContextConfig.KANG_ZHE_TEST)) {
                com.dachen.incomelibrary.utils.Constants.changeIp(ContextConfig.KANG_ZHE_TEST);
            }
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        //        showLoadingDialog();
        new HttpManager().get(this, Constants.INCOME_BASE_INFO, IncomeInfoData.class, Params.getBaseIncomeInfo
                (SharedPreferenceUtil.getString("id", "")), this, false, 3);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

    }
}
