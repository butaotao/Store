package com.dachen.dgroupdoctorcompany.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.medicine.config.AppConfig;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


@SuppressLint("SetJavaScriptEnabled")
public class WebActivity extends BaseActivity {
    public static final String GOTO_WEBACTIVITY="WebActivity";
    public static final String GOTO_WEBFROM="WebActivityFrom";
    public static final int FROM_GROUP_NOTICE=1;//集团通知
    public static final int FROM_COMPANY_NOTICE=2;//公司通知
    public static final int FROM_SERVICE_ARTICEL=5; //服務和隐私条款
    @Nullable
    @Bind(R.id.back_step_btn)
    Button btn_back;

//    private TextView webTitle;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);
        ButterKnife.bind(this);
        initViews();
        mWebView.setWebViewClient(new MyWebviewclient());
        //启用支持javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        initData();
    }

     void initViews(){
//        webTitle=(TextView)findViewById(R.id.webTitle);
        mWebView=(WebView)findViewById(R.id.mWeb);
    }

    private void initData(){
        int where=getIntent().getIntExtra(GOTO_WEBFROM, 0);
        String url=getIntent().getStringExtra(GOTO_WEBACTIVITY);
        url = AppConfig.getUrl(url, 1, "80");
        switch (where) {
            case FROM_GROUP_NOTICE:
                setTitle(getText(R.string.group_notification));
                mWebView.loadUrl(url);
                break;
            case FROM_COMPANY_NOTICE:
                setTitle(getText(R.string.company_notification));
                mWebView.loadUrl(url);
                break;
            case FROM_SERVICE_ARTICEL:
                setTitle("服务和隐私");
                mWebView.loadUrl(url);
                break;
            default:
                break;
        }
    }

    private class MyWebviewclient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
           showLoadingDialog();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            closeLoadingDialog();
        }
    }

    @Nullable
    @OnClick(R.id.back_step_btn)
    void onBackStepBtnClicked() {
        finish();
    }

}
