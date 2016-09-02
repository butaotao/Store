package com.dachen.dgroupdoctorcompany.utils;

import android.app.Activity;
import android.content.Intent;

import com.dachen.dgroupdoctorcompany.activity.LoginActivity;
import com.dachen.dgroupdoctorcompany.activity.MainActivity;
import com.dachen.dgroupdoctorcompany.activity.ResetPasswdActivity;
import com.dachen.dgroupdoctorcompany.app.CompanyApplication;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.base.UserLoginc;
import com.dachen.dgroupdoctorcompany.entity.LoginRegisterResult;
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
        String s = Constants.DRUG+"/companyUser/getMajorUserByUserId";
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
    public static void loginRequest(final Activity context, final String phoneNum, String password) {
        final String userType = Constants.USER_TYPEC+"";
        Umeng.getLoginRequestData(phoneNum, password);
        new HttpManager().post(context, Constants.LOGIN + "", LoginRegisterResult.class,
                Params.getLoginParams(phoneNum, password, userType, context), new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result entity) {
                        if (entity instanceof  LoginRegisterResult){
                            LoginRegisterResult result = (LoginRegisterResult)entity;
                            Umeng.getLoginServerData(result,phoneNum);

                            CompanyApplication.setInitContactList(2);
                            LoginRegisterResult logins = (LoginRegisterResult) entity;
                            UserLoginc.setUserInfo(logins, context);
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("login", "login");
                            context.startActivity(intent);
                            if (context instanceof ResetPasswdActivity){
                                ResetPasswdActivity activity = (ResetPasswdActivity)context;
                                activity.closeLoadingDialog();
                                ToastUtils.showToast(context,"密码重置成功");
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
                false, 1);

    }
}
