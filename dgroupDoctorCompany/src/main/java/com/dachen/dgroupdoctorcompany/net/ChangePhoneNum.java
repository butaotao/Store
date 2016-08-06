package com.dachen.dgroupdoctorcompany.net;

import android.app.Activity;
import android.content.Intent;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.activity.LoginActivity;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.CheckPhoneOnSys;
import com.dachen.dgroupdoctorcompany.entity.SearchDoctorListEntity;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Burt on 2016/8/6.
 */
public class ChangePhoneNum {
    public static final int LOGIN = 0;
    public static final int EDITPHONE = 1;
    public static void verifyPhoneExit(final String phoneNum, final Activity context, final int type ) {
        //drugCompanyEmployee/checkNewPhoneIfOnSystem

        HashMap<String,String> maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(context).getSesstion());
        maps.put("newPhone", phoneNum);
        new HttpManager().post(context, Constants.DRUG +"drugCompanyEmployee/checkNewPhoneIfOnSystem", CheckPhoneOnSys.class,
                maps, new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result response) {
                       if (response.resultCode ==1){
                           CheckPhoneOnSys sys = (CheckPhoneOnSys)response;
                           if (sys.data==1){
                               ToastUtil.showToast(context,"该手机号已被绑定或使用，请更换新号码重试");
                               return;
                           }else {
                               changePhoneNum(phoneNum,context,type);
                           }

                        }else {
                           ToastUtil.showToast(context,response.resultMsg);
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
    public static void changePhoneNum(String phoneNum, final Activity context, final int type ) {
        //drugCompanyEmployee/modifyUserPhone
        HashMap<String,String> maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(context).getSesstion());
        maps.put("newPhone", phoneNum);
        maps.put("drugCompanyId", SharedPreferenceUtil.getString(context,"enterpriseId",""));
        new HttpManager().post(context, Constants.DRUG +"drugCompanyEmployee/modifyUserPhone", Result.class,
                maps, new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result response) {
                        if (response.resultCode ==1){
                            ToastUtil.showToast(context,"修改手机号成功，请使用新手机号登录");
                            if (context instanceof BaseActivity){
                                BaseActivity baseActivity = (BaseActivity)context;
                                baseActivity.closeLoadingDialog();
                            }
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                            context.finish();
                        }else {

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
