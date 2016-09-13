package com.dachen.dgroupdoctorcompany.utils.DataUtils;

import android.app.Activity;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.activity.CustomerVisitActivity;
import com.dachen.dgroupdoctorcompany.activity.MenuWithFABActivity;
import com.dachen.dgroupdoctorcompany.activity.SelectAddressActivity;
import com.dachen.dgroupdoctorcompany.activity.SelfVisitActivity;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.entity.SignLable;
import com.dachen.dgroupdoctorcompany.entity.SignResult;
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
    public static int sigStep = -1;
    public static String snippets = "";
    public static String addresss;
    public static double longituds;
    public static double latitudes;
    public static String citys;
    public static String snippetss;
    public static String defaltsignLables;
    public static String tags;
    public static String signedId = "";
    public static Map<String,String> mapLable2Id = new HashMap<>();
    public static void signDefault(final Activity activity, final String address,
                                   final String coordinate, final String defaltsignLable,
    final int finish, final int type){

        if (activity instanceof BaseActivity){
            BaseActivity activity1 = (BaseActivity)activity;
            activity1.showLoadingDialog();
        }
        new HttpManager().post(activity, Constants.GET_SIGNED_LABLE, SignLable.class,
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
                                    if (type==-1){
                                        sign(activity, address, coordinate, mapLable2Id.get(defaltsignLable),
                                                defaltsignLable, finish);
                                    }else {
                                        if (activity instanceof MenuWithFABActivity){
                                            MenuWithFABActivity activity1 = (MenuWithFABActivity)activity;
                                            activity1.choicePlaceSign(type, mapLable2Id.get(defaltsignLable));
                                        }

                                    }

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
    public static void sign(final Activity activity, final String address,
                            String coordinate, String signLable,String signLabletab, final int finish){
        String orgId = GetUserDepId.getUserDepId(activity);
        TelephonyManager TelephonyMgr = (TelephonyManager)activity.getSystemService(activity.TELEPHONY_SERVICE);
        String deviceId = TelephonyMgr.getDeviceId();
        new HttpManager().post(activity, Constants.CREATE_OR_UPDATA_SIGIN_IN, Result.class,
                Params.getWorkingParams(activity, deviceId, "", "", coordinate, address, signLable, orgId),
                new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result response) {
                        if (response.resultCode == 1){
                            ToastUtil.showToast(activity,"签到成功");
                            if (activity instanceof BaseActivity){
                                BaseActivity activity1 = (BaseActivity)activity;
                                activity1.closeLoadingDialog();
                            }
                            if (finish==1){
                                MenuWithFABActivity activity2 = (MenuWithFABActivity)activity;
                                activity2.onResume();
                            }else if(finish==2){
                                if (activity instanceof MenuWithFABActivity){
                                    /*MenuWithFABActivity activity2 = (MenuWithFABActivity)activity;
                                    activity2.start();*/

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
                                   final double longitude, final double latitude,
                                        final String city, final String snippet, final String defaltsignLable,
                                   final boolean finish,int type){

        if (activity instanceof BaseActivity){
            BaseActivity activity1 = (BaseActivity)activity;
            activity1.showLoadingDialog();
        }
        new HttpManager().post(activity, Constants.GET_SIGNED_LABLE, SignLable.class,
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
                                    longituds = longitude;
                                    latitudes = latitude;
                                    citys = city;
                                    addresss = address;
                                    defaltsignLables = mapLable2Id.get(defaltsignLable);
                                    defaltsignLables = defaltsignLable;
                                    signvisit(activity, address, longitude,latitude,snippet,
                                            mapLable2Id.get(defaltsignLable), finish,4);
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
                                 final double latitude,   final String snippet,
                                 String signLable,   final boolean finish , final int type){
        String orgId = GetUserDepId.getUserDepId(activity);
        TelephonyManager TelephonyMgr = (TelephonyManager)activity.getSystemService(activity.TELEPHONY_SERVICE);
        String deviceId = TelephonyMgr.getDeviceId();
        new HttpManager().post(activity, Constants.CREATE_OR_UPDATA_SIGIN_IN, SignResult.class,
                Params.getWorkingParams(activity, deviceId, "", "", latitude+","+longitude, address, signLable, orgId),
                new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result response) {
                        if (response.resultCode == 1){
                            ToastUtil.showToast(activity,"签到成功");
                            if (activity instanceof BaseActivity){
                                BaseActivity activity1 = (BaseActivity)activity;
                                activity1.closeLoadingDialog();
                            }
                            snippets = snippet;
                            SignResult result = (SignResult)response;
                            if (null!=result.data&&null!=result.data.signed&&!TextUtils.isEmpty(result.data.signed.id)){
                                signedId = result.data.signed.id;
                            }
                                if (activity instanceof SelectAddressActivity){
                                    sigStep = 1;
                                    snippets = snippet;


                                    activity.finish();
                                    return;
                                }
                            if (type == 4){

                                    Intent intent = new Intent(activity, SelfVisitActivity.class);
                                    intent.putExtra("address", SinUtils.addresss);
                                    intent.putExtra("longitude", SinUtils.longituds);
                                    intent.putExtra("latitude", SinUtils.latitudes);
                                    intent.putExtra("addressname", SinUtils.snippets);
                                if (null!=result.data&&null!=result.data.signed&&!TextUtils.isEmpty(result.data.signed.id)){
                                    intent.putExtra("signedId",result.data.signed.id);
                                }

                                    intent.putExtra("mode", CustomerVisitActivity.MODE_FROM_SIGN);
                                    intent.putExtra("city", citys);
                                    activity.startActivity(intent);
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
