package com.dachen.dgroupdoctorcompany.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;

/**
 * @项目名 MedicineProject
 * @Author: zxy on 16/8/25下午2:28.
 * @描述 web工作台登入
 */
public class WebQRLoginActivity extends BaseActivity {
    private TextView mWebQrLoginBack;
    private ImageView mWebQrLoginComputer;
    private TextView mWebQrLoginText;
    private Button mWebQrLoginConfirm;
    private TextView mWebQrLoginCancel;

    private void assignViews() {
        mWebQrLoginBack = (TextView) findViewById(R.id.web_qr_login_back);
        mWebQrLoginComputer = (ImageView) findViewById(R.id.web_qr_login_computer);
        mWebQrLoginText = (TextView) findViewById(R.id.web_qr_login_text);
        mWebQrLoginConfirm = (Button) findViewById(R.id.web_qr_login_confirm);
        mWebQrLoginCancel = (TextView) findViewById(R.id.web_qr_login_cancel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_qr_login);
        assignViews();
        initListener();
        initData();
    }

    private void initData() {
        String scanResult = getIntent().getStringExtra("scanResult");
        Log.d("zxy", "initData: scanResult = "+scanResult);
    }

    private void initListener() {
        mWebQrLoginBack.setOnClickListener(this);
        mWebQrLoginConfirm.setOnClickListener(this);
        mWebQrLoginCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.web_qr_login_back:
            Log.d("zxy", "onClick: web_qr_login_back");
                finish();
            break;
            case R.id.web_qr_login_confirm:
            Log.d("zxy", "onClick: 确定登入");
            break;
            case R.id.web_qr_login_cancel:
                Log.d("zxy", "onClick: 取消登入");
            break;
            default:
                Log.d("zxy", "onClick: default");
             break;
        }
    }
}
