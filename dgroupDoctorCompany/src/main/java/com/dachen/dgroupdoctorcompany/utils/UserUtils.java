package com.dachen.dgroupdoctorcompany.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.dachen.dgroupdoctorcompany.activity.LoginActivity;
import com.dachen.dgroupdoctorcompany.activity.MainActivity;
import com.dachen.dgroupdoctorcompany.activity.RegisterStep2Activity;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.base.UserLoginc;
import com.dachen.dgroupdoctorcompany.entity.Company;
import com.dachen.dgroupdoctorcompany.entity.LoginGetUserInfo;
import com.dachen.dgroupdoctorcompany.entity.LoginRegisterResult;
import com.dachen.medicine.common.utils.MActivityManager;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/6/20.
 */
public class UserUtils {
    public static void logingetUserType(final Activity context) {
        String s = "org/drugCompanyEmployee/getLoginInfo";
        new HttpManager().post(context, s, LoginRegisterResult.class,
                Params.getUserInfo(context), new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result entity) {

                        if (entity.resultCode==1){
                            if (entity instanceof LoginRegisterResult){
                                LoginRegisterResult info = (LoginRegisterResult) entity;

                                UserLoginc.setUserInfos(info, context);
                                    Intent intent = new Intent(context, MainActivity.class);
                                    intent.putExtra("login", "login");
                                    context.startActivity(intent);
                                if (!(context instanceof RegisterStep2Activity)){
                                    context.finish();
                                }

                                }
                        }else {
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.putExtra("login", "login");
                            context.startActivity(intent);
                            ToastUtils.showToast(context, "" + entity.resultMsg);
                        }
                        if (context instanceof BaseActivity){
                            BaseActivity b = (BaseActivity)context;
                            b.closeLoadingDialog();

                        }
                    }

                    @Override
                    public void onSuccess(ArrayList<Result> response) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {
                        if (context instanceof BaseActivity){
                            BaseActivity b = (BaseActivity)context;
                            b.closeLoadingDialog();

                        }
                    }
                },
                false, 1);
    }
}
