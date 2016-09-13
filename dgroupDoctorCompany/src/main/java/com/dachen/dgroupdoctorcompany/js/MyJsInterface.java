package com.dachen.dgroupdoctorcompany.js;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
import com.dachen.dgroupdoctorcompany.utils.CallIntent;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by weiwei on 2016/3/7.
 */
public class MyJsInterface {
    private Context mContext;
    private Activity mActivity;
    public Doctor doctorVisit;
    public static GetDoctorInterface getDoctorInterface;
    private WebView mWebView;

    public MyJsInterface(Context context){
        this(context,null);
    }
    public MyJsInterface(Context context,WebView webView) {
        this.mContext = context;
        mActivity = (Activity) context;
        this.mWebView = webView;
    }


    @JavascriptInterface
    public void closeVC(){
        mActivity.finish();
    }

    @JavascriptInterface
    public String setToken(){
        JSONObject jsonObject = new JSONObject();
        String access_token = SharedPreferenceUtil.getString(mContext,"session", "");
        String userid = SharedPreferenceUtil.getString(mContext,"id","");
        try {
            jsonObject.put("token",access_token);
            jsonObject.put("userid",userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @JavascriptInterface
    public void selectDoctor(){
        if(null == mWebView){
            return;
        }
        doctorVisit = new Doctor();
        CallIntent.selectDoctorForVisit(mContext);
        getDoctorInterface = new GetDoctorInterface() {
            @Override
            public void getDoctorInfo(Doctor doctor) {
                doctorVisit = doctor;
                String json = "{\"id\":\""+doctorVisit.userId+"\", \"name\":\""+doctorVisit.name+"\"}";
                mWebView.loadUrl("javascript: selectDoctorCallback("+json+")");
            }
        };


    }
    public interface  GetDoctorInterface{
            public void getDoctorInfo(Doctor doctor);
    }
}
