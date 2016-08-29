package com.dachen.dgroupdoctorcompany.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.engine.SystemWebChromeClient;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewEngine;

/**
 * @项目名 MedicineProject
 * @Author: zxy on 16/8/26下午3:05.
 * @描述 TODO
 */
public class LitterAppActivity extends CordovaActivity {
    private TextView mTitle;
    private boolean mIsFirstLoadPage = true;//是否是第一次加载页面
    private TextView mClose;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ActionSheetStyleiOS7);
        String url ="http://192.168.3.46:8081/community/test";
        loadUrl(url);
    }

    @Override
    @SuppressWarnings({"deprecation", "ResourceType"})
    protected void createViews() {
        setContentView(R.layout.activity_litter_app);

        initWebView();

        initView();

    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mClose = (TextView) findViewById(R.id.close);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.basicInfoActivity_back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appView.canGoBack()) {
                    appView.backHistory();
                    mClose.setVisibility(View.VISIBLE);
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
            mClose.setVisibility(View.VISIBLE);
        }
        return super.dispatchKeyEvent(event);
    }
}
