package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.medicine.config.AppConfig;

public class MeetingTipsActivity extends BaseActivity {
    private static final String PATH = "health/web/attachments/live/LiveIntroduction.html";
    private String mFrom;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_tips);
        setTitle("召开会议说明");

        initVariable();

        initViews();
    }

    private void initVariable() {
        mFrom = getIntent().getStringExtra("from");
    }

    private void initViews() {
        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        mWebView = (WebView) this.findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
                showLoadingDialog();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                closeLoadingDialog();
            }
        });
        mWebView.loadUrl(AppConfig.getUrl(PATH, 1, "80"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
            case R.id.rl_back:
                close();
                break;
        }

    }

    private void close() {
        if (mFrom != null && mFrom.equals(CreateAndEditMeetingActivity.class.getSimpleName())) {
            Intent intent = new Intent(mThis, MeetingListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else {
            finish();
        }
    }
}
