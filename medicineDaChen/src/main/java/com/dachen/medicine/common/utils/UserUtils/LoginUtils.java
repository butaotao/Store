package com.dachen.medicine.common.utils.UserUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.dachen.medicine.R;
import com.dachen.medicine.activity.LoginActivity;
import com.dachen.medicine.activity.MainActivity;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.LoginVerify;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Burt on 2016/6/20.
 */
public class LoginUtils {
    //http://192.168.3.7:9002/web/api/login?u=12010101010|9&p=123456
    public static void loginVerifyTwice(final Activity context, final boolean isLoginActivity){
        HashMap<String,String> maps = new HashMap<>();
        maps.put("access_token", SharedPreferenceUtil.getString("session", ""));
        maps.put("userId",SharedPreferenceUtil.getString("id",""));
        new HttpManager().get(context, "drugCompanyEmployee/getLoginInfo" + "", LoginVerify.class, maps
                , new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result entity) throws Exception {
                        if(entity instanceof  LoginVerify){
                            LoginVerify verify = (LoginVerify) entity;
                            SharedPreferenceUtil.putString("companyId","");
                            if(verify.data==null||verify.data.companys==null){
                                if (!isLoginActivity){
                                    startLoginActivity(context);
                                }

                            }else {
                                boolean isManager = false;
                                if (null!=verify.data&&verify.data.companys!=null&&verify.data.companys.size()>0){
                                    for (int i=0;i<verify.data.companys.size();i++){
                                        if (!TextUtils.isEmpty(verify.data.companys.get(i).duty)){
                                            if (verify.data.companys.get(i).duty.equals("1")){
                                                SharedPreferenceUtil.putString("shop_manager","店长");

                                                isManager = true;
                                            }
                                        }
                                        SharedPreferenceUtil.putString("companyId",verify.data.companys.get(i).companyId);
                                        break;
                                    }
                                }
                                if (!isManager){
                                    SharedPreferenceUtil.putString("shop_manager","");
                                }
                                if (isLoginActivity) {
                                    ToastUtils.showToast(context.getResources().getString(
                                            R.string.toast_login_success));
                                }
                                startMainActivity(context);
                            }
                        }
                    }

                    @Override
                    public void onSuccess(ArrayList<Result> response) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {

                    }
                },
                false, 3);
    }
    public static void startLoginActivity(Activity context){
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("login", "login");
        context.startActivity(intent);
        context.finish();
    }
    public static void startMainActivity(Activity context){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("login", "login");
        context.startActivity(intent);
        context.finish();
    }
}
