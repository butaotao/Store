package com.dachen.medicine.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.dachen.incomelibrary.utils.ConstantsApp;
import com.dachen.medicine.R;

import butterknife.ButterKnife;

@SuppressLint("SetJavaScriptEnabled")
public class WebActivity extends BaseActivity implements View.OnClickListener {

    private TextView webTitle;
    private WebView mWebView;
    private String mCurrentUrl = "";
    private String mURL;
    private Button btn_close;
    private Button back_step_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);
        ButterKnife.bind(this);
        initView();
        mWebView.setWebViewClient(new MyWebviewclient());
        //启用支持javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        initData();
    }

    private void initView() {
        webTitle = (TextView) findViewById(R.id.webTitle);
        mWebView = (WebView) findViewById(R.id.mWeb);
        btn_close = (Button) findViewById(R.id.btn_close);
        btn_close.setOnClickListener(this);
        back_step_btn = (Button) findViewById(R.id.back_step_btn);
        back_step_btn.setOnClickListener(this);
    }

    private void initData() {
        int where = getIntent().getIntExtra(ConstantsApp.GOTO_WEBFROM, 0);
        mURL = getIntent().getStringExtra(ConstantsApp.GOTO_WEBACTIVITY);
        switch (where) {
            case ConstantsApp.FROM_INCOME_RULE:
                webTitle.setText("规则介绍");
                mWebView.loadUrl(mURL);
                break;
            case ConstantsApp.FROM_INCOME_BALANCE_INTRO:
                webTitle.setText("余额说明");
                mWebView.loadUrl(mURL);
                break;
            case 0:
                mWebView.loadUrl(mURL);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_step_btn:
                if (mCurrentUrl.equals(mURL)) {
                    finish();
                } else {
                    mWebView.goBack();
                }
                break;
            case R.id.btn_close:
                finish();
                break;
        }
    }

    private class MyWebviewclient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            showLoadingDialog();
            mCurrentUrl = url;
            if (!mCurrentUrl.equals(mURL)) {
                btn_close.setVisibility(View.VISIBLE);
            } else {
                btn_close.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            //			mWebView.scrollTo(1, 0);
            //			mWebView.scrollTo(0, 0);
            closeLoadingDialog();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


}
