package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.LoginRegisterResult;
import com.dachen.dgroupdoctorcompany.receiver.HwPushReceiver;
import com.dachen.imsdk.ImSdk;
import com.dachen.imsdk.utils.BuildUtils;
import com.dachen.imsdk.utils.PushUtils;
import com.dachen.medicine.common.utils.MActivityManager;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.SystemUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Burt on 2016/2/20.
 */
public class SettingActivity extends BaseActivity implements HttpManager.OnHttpListener {
    @Nullable
    @Bind(R.id.tv_version)
    TextView tv_version;

    @Nullable
    @OnClick(R.id.ll_edittelphone)
    void startActivityEditTel() {
        Intent intent = new Intent(this,EditTelActivity.class);
        startActivity(intent);
    }

    @Nullable
    @OnClick(R.id.ll_conrectpassword)
    void startActivityConrectPassword() {
        Intent intent = new Intent(this,ModifyPasswordActivity.class);
        startActivity(intent);
    }
    @Nullable
    @OnClick(R.id.ll_logout)
     void logout(){
        onLogoutBtnClicked();
    }
    @Nullable
    @OnClick(R.id.ll_erweima)
    void about(){Intent intent = new Intent(this,AboutUI.class);startActivity(intent);}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitysettings);
        ButterKnife.bind(this);
        setTitle("设置");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            tv_version.setText(info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    void onLogoutBtnClicked(){  ///im/removeDeviceToken.action


        //联网 从服务器登出
        new HttpManager().post(this, Constants.LOGOUT+"", LoginRegisterResult.class,
                Params.getLoginoutParams(SettingActivity.this,SystemUtils.getDeviceId(this)), new HttpManager.OnHttpListener<LoginRegisterResult>() {

                    @Override
                    public void onSuccess(Result response) {

                    }

                    @Override
                    public void onSuccess(ArrayList<LoginRegisterResult> response) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg,
                                          int s) {

                    }
                },
                false,1);
        logOut();
    }
    public void removeregeisterXiaoMi(){
      /*  HashMap<String, String> infaces = new HashMap<String, String>();
        infaces.put("interface1", Constants.XIAOMIREMOVE);*/
    /*    new HttpManager().post(infaces, ResultData.class,
                Params.getRemoveReginsterXiaoMiReceiver(SharedPreferenceUtil.getString(this, "id", ""),
                        SharedPreferenceUtil.getString(this, "mRegId", ""),
                        SharedPreferenceUtil.getString(this, "session", "")), this,
                1, 80);*/


       /* new HttpManager().post(this,Constants.XIAOMIREMOVE+"", ResultData.class,
                Params.getRemoveReginsterXiaoMiReceiver(SharedPreferenceUtil.getString(this, "id", ""),
                        SharedPreferenceUtil.getString(this, "mRegId", ""),
                        SharedPreferenceUtil.getString(this, "session", "")), this,false,
                3);*/
        if(BuildUtils.isHuaweiSystem()){
            PushUtils.removeDevice(SharedPreferenceUtil.getString(this, HwPushReceiver.SP_KEY_TOKEN, ""));
        }else{
            PushUtils.removeDevice(SharedPreferenceUtil.getString(this, "mRegId", ""));
        }
    }
    public void logOut(){
        removeregeisterXiaoMi();
        MActivityManager.getInstance().finishAllActivity();
        closeLoadingDialog();
        SharedPreferenceUtil.putString(SettingActivity.this,"session","");
        SharedPreferenceUtil.putLong(SettingActivity.this,"expires_in", 0L);
        // TODO Auto-generated method stub
        Intent	intent = new Intent(SettingActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        ImSdk.getInstance().logout();
    }
    @Override
    public void onSuccess(Result response) {

    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {

    }
}
