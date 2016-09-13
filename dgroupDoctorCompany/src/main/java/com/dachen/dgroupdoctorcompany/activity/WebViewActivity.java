package com.dachen.dgroupdoctorcompany.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.cms.mylive.MyLiveActivity;
import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.views.InputDialog;

import java.net.URLDecoder;

public class WebViewActivity extends Activity {
    private WebView webView;
    private String  mStrUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = (WebView) this.findViewById(R.id.webview);
        webView.setInitialScale(10);
        WebSettings webSettings = webView.getSettings();
        //支持javascript
        webSettings.setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        //扩大比例的缩放
        webSettings.setUseWideViewPort(true);
        //自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);

//        webView.addJavascriptInterface(new MyJsInterface(WebViewActivity.this),"androidJS");

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                goMyLive(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }
        });
        mStrUrl = this.getIntent().getStringExtra("url");
        if(!TextUtils.isEmpty(mStrUrl)){
            webView.loadUrl(mStrUrl);
        }else{
            webView.loadUrl("file:///android_asset/index2.html");
        }
    }

    private void goMyLive(String url) {
        if (TextUtils.isEmpty(url) || !url.toLowerCase().startsWith("mylive://")) {
            return;
        }
        try {
            url = URLDecoder.decode(url, "utf-8");
            String dataString = url.substring(9);
            final String[] params = dataString.split("&", -1);
            if (params == null || params.length != 7) {
                return;
            }

            InputDialog dialog=new InputDialog(WebViewActivity.this);
            dialog.setOnConfirmListener(new InputDialog.OnConfirmListener() {
                @Override
                public void onConfirm(Dialog dialog, String input) {
                    if (!TextUtils.isEmpty(input)) {
                        start(params[0], params[1], params[2], input, params[4], params[5], params[6]);
                    } else {
                        ToastUtil.showToast(WebViewActivity.this, "请输入密码");
                    }
                }
            });
            dialog.show();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private void start(String domain, String number, String nickName, String joinPwd, String serviceType, String loginAccount, String loginPwd) {
        Intent intent = new Intent(this, MyLiveActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("domain", domain);
        bundle.putString("number", number);
        bundle.putString("nickName", nickName);
        bundle.putString("joinPwd", joinPwd);
        bundle.putString("serviceType", serviceType);
        bundle.putString("loginAccount", loginAccount);
        bundle.putString("loginPwd", loginPwd);
        intent.putExtra("joinMeeting", bundle);
        intent.putExtra("joinMeeting", bundle);
        startActivity(intent);
    }


}
