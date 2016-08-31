package com.dachen.dgroupdoctorcompany.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.QRCodeScannerUI;
import com.dachen.dgroupdoctorcompany.im.view.SessionListViewV2;
import com.dachen.dgroupdoctorcompany.utils.CallIntent;
import com.dachen.dgroupdoctorcompany.utils.CompareDatalogic;
import com.dachen.dgroupdoctorcompany.utils.UserInfo;
import com.dachen.imsdk.net.ImPolling;
import com.dachen.medicine.common.utils.NetUtil;
import com.dachen.medicine.entity.Result;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class InfomationFragment extends BaseFragment implements OnChildClickListener,
        OnClickListener {

    // 总View，总视图
    private View mRootView;
    TextView tv_login_title;
    private SessionListViewV2 lv;
    private ImageButton ibAdd;
    private ImageButton mQRLogin;
    private PopupWindow popWindow;
    private View cover;
    //private NetworkErrorView mNetworkErrorView;
    private LocalBroadcastManager mBroadcastManager;
    private BroadcastReceiver mMItemViewListClickReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = LayoutInflater.from(mActivity).inflate(R.layout.activity_info, null);
        ButterKnife.bind(mActivity);
        tv_login_title = (TextView) mRootView.findViewById(R.id.tv_title);
        lv = (SessionListViewV2) mRootView.findViewById(R.id.list_view);
       /* if (mNetworkErrorView == null) {
            mNetworkErrorView = new NetworkErrorView(mActivity);
            LinearLayout ll = new LinearLayout(mActivity);
            ll.addView(mNetworkErrorView);
            lv.addHeaderView(ll);
        }*/
        lv.setUI(mActivity);
        lv.setEmptyView(mRootView.findViewById(R.id.empty_view));
        tv_login_title.setText("消息");
        ibAdd = (ImageButton) mRootView.findViewById(R.id.btn_add);
        mQRLogin = (ImageButton) mRootView.findViewById(R.id.btn_scannerLogin);
        ibAdd.setOnClickListener(this);
        mQRLogin.setOnClickListener(this);
        cover = mRootView.findViewById(R.id.v_cover);
        registerBroadCast();
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        showNetWorkErr();
        lv.updateView();
        super.onResume();
        ImPolling.getInstance().executeTask();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mMItemViewListClickReceiver);
        lv.unregisterEventBus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                showAddPop();
                break;
            case R.id.btn_scannerLogin: //扫一扫登入工作台
                Intent intent = new Intent(mActivity,QRCodeScannerUI.class);
                /*Intent intent = new Intent(mActivity,LitterAppActivity.class);*/
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void showAddPop() {
        if (popWindow == null) {
            View contentView = getActivity().getLayoutInflater().inflate(R.layout.popwindow, null);
            popWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            popWindow.setFocusable(true);
            popWindow.setBackgroundDrawable(new BitmapDrawable());
            View rlScan = contentView.findViewById(R.id.rl_scan);
            View rlFind = contentView.findViewById(R.id.rl_find);
            popWindow.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss() {
                    cover.setVisibility(View.GONE);
                }
            });
            rlScan.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (null != popWindow && popWindow.isShowing()) {
                        popWindow.dismiss();
                    }
                    if (!CompareDatalogic.isInitContact()) {
                        ToastUtil.showToast(mActivity, "通讯录初始化中...");
                        return;
                    }
                    CallIntent.SelectPeopleActivity(mActivity, null, -1);


                }
            });
            rlFind.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
//					CommonUitls.clearSelectCreateGroup();

                    CallIntent.selectDoctor(mActivity);
                    if (null != popWindow && popWindow.isShowing()) {
                        popWindow.dismiss();
                    }
                }
            });

            if (UserInfo.getInstance(mActivity).isMediePresent()) {
                contentView.findViewById(R.id.iv_line1).setVisibility(View.VISIBLE);
                rlFind.setVisibility(View.VISIBLE);
            } else {
                contentView.findViewById(R.id.iv_line1).setVisibility(View.GONE);
                rlFind.setVisibility(View.GONE);
            }
        }
        popWindow.showAsDropDown(ibAdd, 0, 10);
        cover.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
                                int arg3, long arg4) {
        return false;
    }

    @Override
    public void onSuccess(Result response) {
    }

    @Override
    public void onSuccess(ArrayList response) {
    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {

    }
    //是否显示网络连接异常头View
    private void showNetWorkErr() {

        boolean enable = NetUtil.checkNetworkEnable(mActivity);
        if (enable) {
            Log.d("zxy", "showNetWorkErr: GONE");
            lv.getNetworkErrorView().setVisibility(View.GONE);
            //lv.removeHeaderView(mNetworkErrorView);
        } else  {
            Log.d("zxy", "showNetWorkErr: VISIBLE ");
            //lv.addHeaderView(mNetworkErrorView);
            lv.getNetworkErrorView().setVisibility(View.VISIBLE);
        }
    }

    //注册网络状态广播监听
    private void registerBroadCast() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mMItemViewListClickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                Log.d("zxy", "onReceive: ");
                showNetWorkErr();
            }
        };
        getActivity().registerReceiver(mMItemViewListClickReceiver, intentFilter);
    }
}
