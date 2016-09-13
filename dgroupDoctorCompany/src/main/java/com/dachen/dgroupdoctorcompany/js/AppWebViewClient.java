package com.dachen.dgroupdoctorcompany.js;

import android.webkit.WebView;

import org.apache.cordova.engine.SystemWebChromeClient;
import org.apache.cordova.engine.SystemWebViewEngine;

/**
 * @项目名 MedicineProject
 * @Author: zxy on 16/9/12下午5:26.
 * @描述 TODO
 */
public class AppWebViewClient extends SystemWebChromeClient {
    public AppWebViewClient(SystemWebViewEngine parentEngine) {
        super(parentEngine);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {

    }


}
