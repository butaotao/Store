package com.dachen.mediecinelibraryrealize.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dachen.mediecinelibraryrealize.R;

/**
 * Created by Burt on 2016/3/29.
 */
public class BaiduMapOpenGPSActivity extends BaseActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidumap_opengps);
       TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("如何启用地理服务");
        findViewById(R.id.rl_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.rl_back) {
            finish();
        }
    }
}
