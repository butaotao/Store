package com.dachen.dgroupdoctorcompany.activity;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.js.MenuButtonBean;
import com.dachen.dgroupdoctorcompany.js.TitleBean;
import com.google.gson.Gson;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.engine.SystemWebChromeClient;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewEngine;

import de.greenrobot1.event.EventBus;

/**
 * @项目名 MedicineProject
 * @Author: zxy on 16/8/26下午3:05.
 * @描述 TODO
 */
public class LitterAppActivity extends CordovaActivity {
    private TextView mTitle;
    private boolean mIsFirstLoadPage = true;//是否是第一次加载页面
    private TextView mClose;
    private Button mMenu;
    private PopupWindow popWindow;
    MenuButtonBean mMenuButtonBean;
    private ProgressBar mProgress;
    private Thread mProgressThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ActionSheetStyleiOS7);
        EventBus.getDefault().register(this);
        String url = getIntent().getStringExtra("url");
        // String url ="http://192.168.3.7/drugorg/web/attachments/JSBridge/";
        loadUrl(url);
    }


    private void loadProgress() {
        mProgress.setVisibility(View.VISIBLE);
        mProgress.setMax(100);
      /*  mProgressThread = new Thread(){
          *//*  @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    mProgress.setProgress(i);
                    SystemClock.sleep(15);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgress.setVisibility(View.GONE);
                    }
                });
            }*//*
        };*/
       // mProgressThread.start();
    }

    @Override
    @SuppressWarnings({"deprecation", "ResourceType"})
    protected void createViews() {
        setContentView(R.layout.activity_litter_app);

        initWebView();

        initView();
        mProgress = (ProgressBar) findViewById(R.id.litter_app_progress);
        loadProgress();
    }


    public void onEventMainThread(TitleBean title){
        Gson gson = new Gson();
        TitleBean titleBean = gson.fromJson(title.title, TitleBean.class);
        mTitle.setText(titleBean.title);
    }
    public void onEventMainThread(MenuButtonBean bean){
        mMenuButtonBean = bean;
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mClose = (TextView) findViewById(R.id.close);
        mMenu = (Button) findViewById(R.id.right_menu);
        mProgress = (ProgressBar) findViewById(R.id.litter_app_progress);
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

        mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popWindow == null) {
                    View contentView = View.inflate(LitterAppActivity.this,R.layout.litterapp_popwindow, null);
                    if (mMenuButtonBean!=null&&"singleMenu".equals(mMenuButtonBean.menuType)) {
                        TextView textView = new TextView(LitterAppActivity.this);
                        textView.setText(mMenuButtonBean.singleMenu.menuText);
                        LinearLayout layout = (LinearLayout) contentView.findViewById(R.id.pop_ll);
                        layout.addView(textView);
                    }
                    popWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    popWindow.setFocusable(true);
                    popWindow.setBackgroundDrawable(new BitmapDrawable());
                    popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                        }
                    });
                   /* rlScan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            if (null != popWindow && popWindow.isShowing()) {
                                popWindow.dismiss();
                            }

                        }
                    });*/
                   /* rlFind.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {

                        }
                    });*/

                }
                popWindow.showAsDropDown(mMenu, 0, 10);
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
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view,newProgress);
            mProgress.setProgress(newProgress);
            if (newProgress == 100) {
                mProgress.setVisibility(View.GONE);
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

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public class AppWebViewClient extends SystemWebChromeClient {
        public AppWebViewClient(SystemWebViewEngine parentEngine) {
            super(parentEngine);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {

        }


    }
}
