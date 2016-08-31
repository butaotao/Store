package com.dachen.dgroupdoctorcompany.utils.DataUtils;

import android.app.Activity;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.activity.AddSignInActivity;
import com.dachen.dgroupdoctorcompany.activity.CustomerVisitActivity;
import com.dachen.dgroupdoctorcompany.activity.MenuWithFABActivity;
import com.dachen.dgroupdoctorcompany.activity.SelectAddressActivity;
import com.dachen.dgroupdoctorcompany.activity.SelfVisitActivity;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.SignLable;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Burt on 2016/8/27.
 */
public class SinUtils {
    public static Map<String,String> mapLable2Id = new HashMap<>();
    public static void signDefault(final Activity activity, final String address,
                                   final String coordinate, final String defaltsignLable,
    final boolean finish){

        if (activity instanceof BaseActivity){
            BaseActivity activity1 = (BaseActivity)activity;
            activity1.showLoadingDialog();
        }
        new HttpManager().get(activity, Constants.GET_SIGNED_LABLE, SignLable.class,
                Params.getInfoParams(activity),
                new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result response) {
                        if (response instanceof SignLable) {
                            if (response.getResultCode() == 1) {
                                SignLable signLable = (SignLable) response;
                                if (null != signLable && null != signLable.data) {
                                    List<SignLable.Data.SignLableItem> lableItemList = signLable.data.data;
                                    for (int i = 0; i < lableItemList.size(); i++) {
                                        SignLable.Data.SignLableItem signLableItem = lableItemList.get(i);
                                        String strLable = signLableItem.label;
                                        String strLableId = signLableItem.id;
                                        mapLable2Id.put(strLable, strLableId);
                                    }
                                    sign(activity, address, coordinate, mapLable2Id.get(defaltsignLable), finish);
                                }
                            } else {
                                ToastUtil.showToast(activity, response.getResultMsg());
                            }
                        }
                    }
                    @Override
                    public void onSuccess(ArrayList<Result> response) {
                        if (activity instanceof BaseActivity) {
                            BaseActivity activity1 = (BaseActivity) activity;
                            activity1.closeLoadingDialog();
                        }
                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {

                    }
                }, false, 4);
    }
    public static void sign(final Activity activity, final String address, String coordinate, String signLable, final boolean finish){
        String orgId = GetUserDepId.getUserDepId(activity);
        TelephonyManager TelephonyMgr = (TelephonyManager)activity.getSystemService(activity.TELEPHONY_SERVICE);
        String deviceId = TelephonyMgr.getDeviceId();
        new HttpManager().post(activity, Constants.CREATE_OR_UPDATA_SIGIN_IN, Result.class,
                Params.getWorkingParams(activity, deviceId, "", "", coordinate, address, signLable, orgId),
                new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result response) {
                        if (response.resultCode == 1){

                            if (activity instanceof BaseActivity){
                                BaseActivity activity1 = (BaseActivity)activity;
                                activity1.closeLoadingDialog();
                            }
                            if (finish){
                                activity.finish();
                            }else {
                                if (activity instanceof MenuWithFABActivity){
                                    MenuWithFABActivity activity2 = (MenuWithFABActivity)activity;
                                    activity2.start();
                                }
                            }

                        }else {
                            ToastUtil.showToast(activity,response.getResultMsg());
                        }

                    }

                    @Override
                    public void onSuccess(ArrayList<Result> response) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {
                        if (activity instanceof BaseActivity){
                            BaseActivity activity1 = (BaseActivity)activity;
                            activity1.closeLoadingDialog();
                        }
                    }
                }, false, 4);
    }





    public static void signDefaultvisit(final Activity activity, final String address,
                                   final double longitude, final double latitude, final String city, final String snippet, final String defaltsignLable,
                                   final boolean finish){

        if (activity instanceof BaseActivity){
            BaseActivity activity1 = (BaseActivity)activity;
            activity1.showLoadingDialog();
        }
        new HttpManager().get(activity, Constants.GET_SIGNED_LABLE, SignLable.class,
                Params.getInfoParams(activity),
                new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result response) {
                        if (response instanceof SignLable) {
                            if (response.getResultCode() == 1) {
                                SignLable signLable = (SignLable) response;
                                if (null != signLable && null != signLable.data) {
                                    List<SignLable.Data.SignLableItem> lableItemList = signLable.data.data;
                                    for (int i = 0; i < lableItemList.size(); i++) {
                                        SignLable.Data.SignLableItem signLableItem = lableItemList.get(i);
                                        String strLable = signLableItem.label;
                                        String strLableId = signLableItem.id;
                                        mapLable2Id.put(strLable, strLableId);
                                    }
                                    signvisit(activity, address, longitude,latitude,city,snippet, mapLable2Id.get(defaltsignLable), finish);
                                }
                            } else {
                                ToastUtil.showToast(activity, response.getResultMsg());
                            }
                        }
                    }
                    @Override
                    public void onSuccess(ArrayList<Result> response) {
                        if (activity instanceof BaseActivity) {
                            BaseActivity activity1 = (BaseActivity) activity;
                            activity1.closeLoadingDialog();
                        }
                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {

                    }
                }, false, 4);
    }
    public static void signvisit(final Activity activity, final String address, final double longitude,
                                 final double latitude, final String city, final String snippet,String signLable, final boolean finish){
        String orgId = GetUserDepId.getUserDepId(activity);
        TelephonyManager TelephonyMgr = (TelephonyManager)activity.getSystemService(activity.TELEPHONY_SERVICE);
        String deviceId = TelephonyMgr.getDeviceId();
        new HttpManager().post(activity, Constants.CREATE_OR_UPDATA_SIGIN_IN, Result.class,
                Params.getWorkingParams(activity, deviceId, "", "", latitude+","+longitude, address, signLable, orgId),
                new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result response) {
                        if (response.resultCode == 1){

                            if (activity instanceof BaseActivity){
                                BaseActivity activity1 = (BaseActivity)activity;
                                activity1.closeLoadingDialog();
                            }
                            if (finish){
                                activity.finish();
                                if (activity instanceof SelectAddressActivity){
                                    Intent intent = new Intent(activity, SelfVisitActivity.class);
                                    intent.putExtra("address", address);
                                    intent.putExtra("longitude", longitude);
                                    intent.putExtra("latitude", latitude);
                                    intent.putExtra("addressname", city + address + snippet);
                                    intent.putExtra("mode", CustomerVisitActivity.MODE_FROM_SIGN);
                                    intent.putExtra("city", city);
                                    activity.startActivity(intent);
                                }
                            }

                        }else {
                            ToastUtil.showToast(activity,response.getResultMsg());
                        }

                    }

                    @Override
                    public void onSuccess(ArrayList<Result> response) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {
                        if (activity instanceof BaseActivity){
                            BaseActivity activity1 = (BaseActivity)activity;
                            activity1.closeLoadingDialog();
                        }
                    }
                }, false, 4);
    }
}
