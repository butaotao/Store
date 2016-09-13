package com.dachen.dgroupdoctorcompany.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;

import java.util.ArrayList;

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
    private String mScanResult;

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
        mScanResult = getIntent().getStringExtra("scanResult");
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
                finish();
                break;
            case R.id.web_qr_login_confirm:
                LoginWeb();
                break;
            case R.id.web_qr_login_cancel:
                finish();
                break;
            default:
                break;
        }
    }

    private void LoginWeb() {
        new HttpManager<Result>().post(this, Constants.QR_WEB_LONIN_CONFIRM, Result.class, Params
                .getQRWebLoginParams(getApplicationContext(), mScanResult), new HttpManager.OnHttpListener<Result>() {
            @Override
            public void onSuccess(Result response) {
                if (response.resultCode == 1) {
                    ToastUtil.showToast(getApplicationContext(),"登录成功");
                }else/*(response.resultCode == 0)*/{
                    //ToastUtil.showToast(getApplicationContext(),"Key不存在于Redis中！");
                    ToastUtil.showToast(getApplicationContext(),"登录授权已经过期，请刷新二维码后重新扫描");
                }
                finish();
            }

            @Override
            public void onSuccess(ArrayList<Result> response) {
            }

            @Override
            public void onFailure(Exception e, String errorMsg, int s) {
                ToastUtil.showToast(getApplicationContext(),"网络错误,请重试");
            }
        }, false, 1);

    }
}
