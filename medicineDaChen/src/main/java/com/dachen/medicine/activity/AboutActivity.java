package com.dachen.medicine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.R;
import com.dachen.medicine.app.Constants;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.common.utils.VersionUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.entity.VersionInfo;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;
import com.dachen.medicine.service.VersionUpdateService;
import com.dachen.medicine.view.MessageDialog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity implements OnClickListener {
    RelativeLayout rl_back;
    TextView tv_title;

    @Bind(R.id.versionUpdate)
    TextView mVersionUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("关于");
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_back:
                super.onBackPressed();
                break;

            default:
                break;
        }
    }

    @OnClick(R.id.versionUpdate)
    void onVersionUpdateClick(View v) {
        mVersionUpdate.setTextColor(0x503db4ff);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mVersionUpdate.setTextColor(0xff3db4ff);
            }
        }, 300);
        getVersion();

    }

    /**
     * 获取版本号
     */
    private void getVersion() {
        new HttpManager().post(this, Constants.GET_VERSION, VersionInfo.class,
                Params.getVersionParams(this), new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result response) {
                        if (response.getResultCode() == 1) {
                            if (response instanceof VersionInfo) {
                                final VersionInfo versionInfo = (VersionInfo) response;
                                if (versionInfo.data != null && VersionUtils.hasNewVersion(AboutActivity.this, versionInfo.data.version)) {
                                    final MessageDialog messageDialog = new MessageDialog(AboutActivity.this, "取消", "马上更新", versionInfo.data.info);
                                    messageDialog.setBtn1ClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            messageDialog.dismiss();
                                        }
                                    });
                                    messageDialog.setBtn2ClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            messageDialog.dismiss();
                                            Intent intent = new Intent(AboutActivity.this, VersionUpdateService.class);
                                            intent.putExtra("desc", getString(R.string.app_name));
                                            intent.putExtra("fileName", "company_release_v" + versionInfo.data.version + ".apk");
                                            intent.putExtra("url", versionInfo.data.downloadUrl);
                                            startService(intent);
                                        }
                                    });
                                    messageDialog.show();
                                    return;
                                }
                            }
                        }

                        ToastUtils.showToast("当前版本即是最新版本");
                    }

                    @Override
                    public void onSuccess(ArrayList<Result> response) {
                        ToastUtils.showToast("当前版本即是最新版本");
                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {
                        ToastUtils.showToast("当前版本即是最新版本");

                    }
                },
                false, 1);
    }

}
