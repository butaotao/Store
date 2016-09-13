package com.dachen.dgroupdoctorcompany.net;

import android.app.Activity;
import android.content.Intent;

import com.dachen.dgroupdoctorcompany.activity.RegisterActivity;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.LoginRegisterResult;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/8/6.
 */
public class LoginLogic {
    //0 表示登录，1表示修改手机号码
    public static final int LOGIN = 0;
    public static final int EDITPHONE = 1;
    public static final String FROMACTIVITY = "PreResetPasswdActivity";
    public static void loginRequest(String phoneNum, String password, final Activity context, final int type ) {
        final String userType = Constants.USER_TYPE;
        new HttpManager().post(context, Constants.LOGIN + "", LoginRegisterResult.class,
                Params.getLoginParams(phoneNum, password, userType, context), new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result entity) {
                        if (context instanceof BaseActivity){
                            BaseActivity baseActivity = (BaseActivity)context;
                            baseActivity.closeLoadingDialog();
                        }
                        if (entity instanceof  LoginRegisterResult){
                            if (entity.getResultCode() != 1) {

                                if (type == EDITPHONE){
                                    ToastUtils.showToast(context, "密码输入错误,请重新输入");
                                }
                                return;
                            }
                            if (EDITPHONE == type){
                                Intent intent = new Intent(context, RegisterActivity.class);
                                intent.putExtra("title","手机号码添加");
                                intent.putExtra("from",FROMACTIVITY);
                                context.startActivity(intent);
                            }

                        }
                    }

                    @Override
                    public void onSuccess(ArrayList<Result> response) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {
                        if (context instanceof BaseActivity){
                            BaseActivity baseActivity = (BaseActivity)context;
                            baseActivity.closeLoadingDialog();
                        }
                    }
                },
                false, 1);

    }
}
