package com.dachen.dgroupdoctorcompany.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.net.NetConfig;

/**
 * Created by Burt on 2016/7/13.
 */
public class AddFriendDesActivity extends BaseActivity {
    WebView mWebView;
    String mURL ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfrienddes);
       String des = SharedPreferenceUtil.getString(this, "netdes", NetConfig.KANG_ZHE);
                enableBack();
        mURL = "http://"+des+"/org/web/dev/DGroupBusiness/documentation/businessInfo.html";
        setTitle("业务助理介绍");
        mWebView = (WebView) findViewById(R.id.mWeb);
        //启用支持javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl(mURL);
    }

}
