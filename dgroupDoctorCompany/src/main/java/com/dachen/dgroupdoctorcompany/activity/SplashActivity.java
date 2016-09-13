package com.dachen.dgroupdoctorcompany.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.base.UserLoginc;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.DoctorDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.RoleDao;
import com.dachen.dgroupdoctorcompany.entity.LoginRegisterResult;
import com.dachen.dgroupdoctorcompany.utils.Umeng;
import com.dachen.imsdk.ImSdk;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Burt on 2016/2/19.
 */
public class SplashActivity extends BaseActivity implements HttpManager.OnHttpListener {
    public final static String SHAREDPREFERENCES_NAME = "first_pref";
    DoctorDao dao;
    CompanyContactDao companyContactDao;
    RoleDao roleDao;
    public static boolean toNoticeWeb = false; //是否浏览器启动app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.splash_company, null);
        setContentView(view);
        startActivitys();
        dao = new DoctorDao(this);
        companyContactDao = new CompanyContactDao(this);
        roleDao = new RoleDao(this);
        //渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(100);
        view.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                autoLogin();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    public void startActivitys(){
        Uri data = getIntent().getData();
        if (data != null) {
/*            Log.d("zxy :", "85 : SplashActivity : startActivitys :  "+data.getEncodedSchemeSpecificPart());
            Log.d("zxy :", "85 : SplashActivity : startActivitys :  "+data.getEncodedPath());
            Log.d("zxy :", "85 : SplashActivity : startActivitys :  "+data.getQuery());
            Log.d("zxy :", "85 : SplashActivity : startActivitys :  "+data.getQueryParameter("id"));
            Log.d("zxy :", "85 : SplashActivity : startActivitys :  "+data.getQueryParameter("title"));
            Log.d("zxy :", "85 : SplashActivity : startActivitys :  data.getPath()"+data.getPath());
            String url = "http:"+data.getPath();
            Log.d("zxy :", "SplashActivity : "+"startActivitys: uri = "+data);*/
            SharedPreferenceUtil.putString(getApplicationContext(),"noticeUri",data.toString());
            toNoticeWeb = true;
        }

       /* Uri uridata = this.getIntent().getData();
        ToastUtil.showToast(this,uridata+"");*/
    }
//启动notice页面
    private void startNoticeWeb() {
        startActivity(new Intent(getApplicationContext(),NoticeWebActivity.class));
        toNoticeWeb = false;
    }

    public void autoLogin() {
        SharedPreferences preferences = getSharedPreferences(
                SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        // 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
        boolean isFirstIn = preferences.getBoolean("isFirstIn", true);
        if (isFirstIn) {
            Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
            startActivity(intent);
            finish();
        } else {
            long expiresInTime = SharedPreferenceUtil.getLong(this, "expires_in", 0);
            long currentTime = System.currentTimeMillis();
           /* if (expiresInTime != 0) {
                if (expiresInTime - currentTime > 0) {
                        addDoctor();
                        loginRequest();
                } else {
                    startLoginActivity();
                }
            } else {
                startLoginActivity();
            }*/
            addDoctor();
            loginRequest();
        }

    }
//user/login/auto
    private void loginRequest(/*String phoneNum, String password*/) {
        String id = SharedPreferenceUtil.getString(this, "id", "");
        String session = SharedPreferenceUtil.getString(this, "session", "");
        boolean isIdExit = TextUtils.isEmpty(id);
        boolean isSessionExit = TextUtils.isEmpty(session);//
        Umeng.getAutoLoginRequestData(id, session);
        if (!isIdExit&& !isSessionExit){

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("userId", SharedPreferenceUtil.getString(this, "id", ""));
            params.put("serial", SharedPreferenceUtil.getString(this, "mRegId", ""));
            params.put("access_token", SharedPreferenceUtil.getString(this, "session", ""));
            params.put("access_context",SharedPreferenceUtil.getString(this,"context_token",""));
            final String userType = Constants.USER_TYPE;
            new HttpManager().post(this, Constants.USER_LORGIN_AUTO + "", LoginRegisterResult.class,
                    params, this,
                    false, 1);

        }else {
            startLoginActivity();
        }

    }
    public void startLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSuccess(Result entity) {
        if (entity instanceof LoginRegisterResult) {
            LoginRegisterResult result = (LoginRegisterResult)entity;
            Umeng.getAutoLoginData(result);
            if (entity.getResultCode() != 1) {
                ToastUtils.showToast(this, entity.getResultMsg());
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
            initUser();
           // UserUtils.logingetUserType(SplashActivity.this);


            LoginRegisterResult logins = (LoginRegisterResult) entity;
            UserLoginc.setUserInfo(logins, SplashActivity.this);
            if (toNoticeWeb) {
                startNoticeWeb();
            }else {
                Intent intent = new Intent(mThis, MainActivity.class);
                intent.putExtra("login", "login");
                mThis.startActivity(intent);
            }
            finish();
        }
    }

    @Override
    public void onSuccess(ArrayList response) {

    }

    @Override
    public void onFailure(Exception e, String errorMsg, int s) {
        initUser();
        if (toNoticeWeb) {
            startNoticeWeb();
        }else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("login", "login");
            startActivity(intent);
        }
    }


    public void addDoctor() {
        //enterprise/doctor/search
        showLoadingDialog();
        if (TextUtils.isEmpty( UserInfo.getInstance(this).getSesstion())){
            return;
        }
        UserLoginc.addDoctor(this, true, false);
    }



    private void initUser() {
        String session = SharedPreferenceUtil.getString(this, "session", "");
        String id = SharedPreferenceUtil.getString(this, "id", "");
        String username = SharedPreferenceUtil.getString(this, "username", "");
        String usernick = SharedPreferenceUtil.getString(this, "usernick", "");
        String avatar = SharedPreferenceUtil.getString(this, id + "head_url", "");
        ImSdk.getInstance().initUser(session, id, username, usernick, avatar);

    }
}
