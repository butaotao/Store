package com.dachen.dgroupdoctorcompany.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.js.MyJsInterface;
import com.dachen.dgroupdoctorcompany.views.CustomDialog;

/**
 * Created by weiwei on 2016/3/7.
 */
public class WebActivityForCompany extends BaseActivity{
    public static final String INTENT_NO_CACHE="noCache";
    public static final String INTENT_CHECK_404="check_404";
    public static final String INTENT_SHOW_TITLE="showTitle";

    private WebView webview;
    private TextView tv_title;
    private String url;
    public ProgressDialog mDialog;
    private boolean check404;
    private String mFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_layout);

        url = getIntent().getStringExtra("url");

        mDialog = new ProgressDialog(this, R.style.IMDialog);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setMessage("正在加载");

        tv_title = (TextView) findViewById(R.id.title);
//		tv_title.setText(getIntent().getStringExtra("title"));
        if(!TextUtils.isEmpty(mFrom)&&"heart".equals(mFrom)){
            tv_title.setText("");
        }else{
            tv_title.setText(getIntent().getStringExtra("title"));}

        if(getIntent().getBooleanExtra(INTENT_SHOW_TITLE,false)){
            findViewById(R.id.group_personal_data_top).setVisibility(View.VISIBLE);
        }
        webview = (WebView) findViewById(R.id.webview);
//        webview.setInitialScale(25);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        // 设置可以支持缩放
        settings.setSupportZoom(true);
        // 设置出现缩放工具
        settings.setBuiltInZoomControls(true);
        //扩大比例的缩放
        settings.setUseWideViewPort(true);
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(1024*1024*8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAllowFileAccess(true);

        settings.setAppCacheEnabled(true);
        webview.addJavascriptInterface(new MyJsInterface(WebActivityForCompany.this,webview),"androidJS");
        webview.loadData("", "text/html", null);
        if(getIntent().getBooleanExtra(INTENT_NO_CACHE, false)){
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        }
        check404=getIntent().getBooleanExtra(INTENT_CHECK_404,false);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mDialog != null) {
                    mDialog.dismiss();
                }
            }

        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                // 没有传标题进来时才进行设置
                if(!TextUtils.isEmpty(mFrom)&&"heart".equals(mFrom)){
                    tv_title.setText("");
                }else{
                    if (check404 && "404 Not Found".equals(title)) {
                        webview.loadUrl("file:///android_asset/" + "article_no_found.html");
                        return;
                    }
                    if (TextUtils.isEmpty(getIntent().getStringExtra("title"))) {
                        tv_title.setText(title);
                    }}
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult jsResult) {
                CustomDialog customDialog = new CustomDialog.Builder(view.getContext(), new CustomDialog.CustomClickEvent() {
                    @Override
                    public void onDismiss(CustomDialog customDialog) {
                        customDialog.dismiss();
                    }

                    @Override
                    public void onClick(CustomDialog customDialog) {
                        if (jsResult != null) {
                            jsResult.confirm();
                        }
                        customDialog.dismiss();
                    }
                }).setMessage(message).setPositive("确定").setNegative("取消").create();
                customDialog.show();

                return true;
            }
        });

        if (!TextUtils.isEmpty(url)) {
            if (mDialog != null) {
                mDialog.show();
            }
            webview.loadUrl(url);
        }
    }

    public void onLeftClick(View view) {
        finish();
    }

}
