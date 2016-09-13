package com.dachen.dgroupdoctorcompany.activity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.net.NetConfig;

/**
 * Created by Burt on 2016/7/13.
 */
public class HowRegisterActivity extends BaseActivity{
    WebView mWebView;
    String mURL = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfrienddes);
        enableBack();
        String des = SharedPreferenceUtil.getString(this, "netdes", NetConfig.KANG_ZHE);
        enableBack();
        mURL = "http://"+des+"/"+Constants.DRUG+"web/dev/DGroupBusiness/documentation/regInfo.html";

        setTitle("如何注册账号");
        mWebView = (WebView) findViewById(R.id.mWeb);
        //启用支持javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl(mURL);
    }
}
