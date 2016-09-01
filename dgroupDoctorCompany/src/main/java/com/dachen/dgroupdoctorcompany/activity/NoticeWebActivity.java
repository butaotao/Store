package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.engine.SystemWebChromeClient;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewEngine;

/**
 * @项目名 MedicineProject
 * @Author: zxy on 16/8/31下午8:37.
 * @描述 展示公告
 */
public class NoticeWebActivity extends CordovaActivity {

    private TextView mTitle;
    private boolean mIsFirstLoadPage = true;//是否是第一次加载页面

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String sUri = SharedPreferenceUtil.getString(getApplicationContext(), "noticeUri", "");
        Uri data = Uri.parse(sUri);
        String url = "http:" + data.getPath() + "?" + data.getQuery();
        Log.d("zxy :", "35 : NoticeWebActivity : onCreate :  url = " + url);
        if (!"".equals(url)) {
            loadUrl(url);
        } else {
            ToastUtil.showToast(getApplicationContext(), "网页不存在");
            finish();
        }
    }

    @Override
    @SuppressWarnings({"deprecation", "ResourceType"})
    protected void createViews() {
        setContentView(R.layout.activity_notice_web);
        initWebView();
        initView();
        initListener();
        initData();
    }

    private void initListener() {
    }

    private void initData() {

    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);

        findViewById(R.id.basicInfoActivity_back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appView.canGoBack()) {
                    appView.backHistory();
                } else {
                    finish();
                }
            }
        });
    }

    private void initWebView() {
        appView.getView().setId(R.id.cordova_web_view_id);
        appView.getView().setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.container);
        viewGroup.addView(appView.getView());

        if (preferences.contains("BackgroundColor")) {
            int backgroundColor = preferences.getInteger("BackgroundColor", Color.BLACK);
            appView.getView().setBackgroundColor(backgroundColor);
        }

        appView.getView().requestFocusFromTouch();
    }

    @Override
    protected void inited() {
        //必须在初始化后才能设置WebChromeClient，不然systemWebView.getSystemWebViewEngine()为空
        SystemWebView systemWebView = (SystemWebView) appView.getView();
        systemWebView.setWebChromeClient(new SystemWebChromeClientEx(systemWebView.getSystemWebViewEngine()));
    }

    class SystemWebChromeClientEx extends SystemWebChromeClient {

        public SystemWebChromeClientEx(SystemWebViewEngine parentEngine) {
            super(parentEngine);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            mTitle.setText(title);
            if (mIsFirstLoadPage) {
                //清除红点
                // ImSpUtils.spCommon().edit().putBoolean(AppImConst.USER_SP_DOC_COMMUNITY_NEW, false).apply();
                //EventBus.getDefault().post(new UnreadEvent(UnreadEvent.TYPE_NEW_COMMUNITY, 0));
                mIsFirstLoadPage = false;
            }

        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onDestroy() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onDestroy();
    }
}
