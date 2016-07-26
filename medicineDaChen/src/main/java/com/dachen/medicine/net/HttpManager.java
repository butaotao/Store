package com.dachen.medicine.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.dachen.incomelibrary.utils.UserInfo;
import com.dachen.medicine.R;
import com.dachen.medicine.activity.LoginActivity;
import com.dachen.medicine.app.MActivityManager;
import com.dachen.medicine.app.MedicineApplication;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.NetUtil;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.config.AppConfig;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.volley.custom.StringJsonArrayRequest;
import com.dachen.medicine.volley.custom.StringJsonObjectRequest;
import com.google.gson.Gson;

/**
 * 接口请求管理类 作者 yuankangle 时间 2015/7/14
 */
public class HttpManager<T> {

	private Gson mGson;
	private Request<String> requestArray;

	/**
	 * post请求
	 * 
	 * @param context
	 * @param url
	 * @param tClass
	 * @param onHttpListener
	 */
	public void post(Context context, String interfaces, Class<T> tClass,
			Map<String, String> params,
			final OnHttpListener<Result> onHttpListener, boolean isArray,
			int net) {
		// request(Request.Method.POST, context, url, tClass, onHttpListener);
		request(Request.Method.POST, interfaces, context, tClass, params,
				onHttpListener, isArray, net);
	}

	public void post(String interfaces, Class<T> tClass,
			Map<String, String> params,
			final OnHttpListener<Result> onHttpListener, boolean isArray,
			int net) {
		request(Request.Method.POST, interfaces,
				MedicineApplication.getApplication(), tClass, params,
				onHttpListener, isArray, net);
	} 
	public void post(Context context, HashMap<String, String> interfaces,
			Class<T> tClass, Map<String, String> params,
			final OnHttpListener<Result> onHttpListener, int net) {
		// request(Request.Method.POST, context, url, tClass, onHttpListener);
		request(Request.Method.POST, interfaces, context, tClass, params,
				onHttpListener, false, net);
	}

	public void post(HashMap<String, String> interfaces, Class<T> tClass,
			Map<String, String> params,
			final OnHttpListener<Result> onHttpListener, int net) {
		// request(Request.Method.POST, context, url, tClass, onHttpListener);
		request(Request.Method.POST, interfaces,
				MedicineApplication.getApplication(), tClass, params,
				onHttpListener, false, net);
	}
	public void post(HashMap<String, String> interfaces, Class<T> tClass,
			Map<String, String> params,
			final OnHttpListener<Result> onHttpListener, int net,int port) {
		// request(Request.Method.POST, context, url, tClass, onHttpListener);
		request(Request.Method.POST, interfaces,
				MedicineApplication.getApplication(), tClass, params,
				onHttpListener, false, net,port);
	}
	public void post(HashMap<String, String> interfaces, Class<T> tClass,
			Map<String, String> params,
			final OnHttpListener<Result> onHttpListener, boolean isarray,
			int net) {
		// request(Request.Method.POST, context, url, tClass, onHttpListener);
		request(Request.Method.POST, interfaces,
				MedicineApplication.getApplication(), tClass, params,
				onHttpListener, isarray, net);
	}

	/**
	 * get请求
	 * 
	 * @param context
	 * @param url
	 * @param tClass
	 * @param onHttpListener
	 */
	public void get(Context context, String interfaces, Class<T> tClass,
			Map<String, String> params, OnHttpListener<Result> onHttpListener,
			boolean isArray, int net) {
		request(Request.Method.GET, interfaces, context, tClass, params,
				onHttpListener, isArray, net);
	}

	public void get(Map<String, String> interfaces, Class<T> tClass,
			Map<String, String> params, OnHttpListener<Result> onHttpListener,
			boolean isArray, int net) {
		request(Request.Method.GET, interfaces,
				MedicineApplication.getApplication(), tClass, params,
				onHttpListener, isArray, net);
	}

	/**
	 * 防问接口用Gson解析
	 * 
	 * @param method
	 * @param context
	 * @param url
	 * @param tClass
	 * @param onHttpListener
	 */
	@SuppressWarnings("unchecked")
	private void request(int method, Map<String, String> interfaces,
			final Context context, final Class<T> tClass,
			Map<String, String> params,
			final OnHttpListener<Result> onHttpListener, boolean isArray,
			int net) {

		String fullurl = "";
		fullurl = AppConfig.getUrl(interfaces, net);

		requestBase(method, fullurl, context, tClass, params, onHttpListener,
				isArray, net);

	}
	@SuppressWarnings("unchecked")
	private void request(int method, Map<String, String> interfaces,
			final Context context, final Class<T> tClass,
			Map<String, String> params,
			final OnHttpListener<Result> onHttpListener, boolean isArray,
			int net,int port) {

		String fullurl = "";
		fullurl = AppConfig.getUrl(interfaces, net,port);

		requestBase(method, fullurl, context, tClass, params, onHttpListener,
				isArray, net);

	}
	@SuppressWarnings("unchecked")
	private void request(int method, String interfaces, final Context context,
			final Class<T> tClass, Map<String, String> params,
			final OnHttpListener<Result> onHttpListener, boolean isArray,
			int net) {

		String fullurl = "";
		fullurl = AppConfig.getUrl(interfaces, net);
		requestBase(method, fullurl, context, tClass, params, onHttpListener,
				isArray, net);
	}

	private void requestBase(int method, String fullurl, final Context context,
			final Class<T> tClass, Map<String, String> params,
			final OnHttpListener<Result> onHttpListener, boolean isArray,
			int net) {
			if (!NetUtil.checkNetworkEnable(context)) { 
				onHttpListener.onFailure(null, context.getResources().getString(R.string.toast_network_exception),-1);
  
				return;
			}
		RequestQueue mRequestQueue = Volley.newRequestQueue(context);
		mRequestQueue.cancelAll(this);
		mGson = new Gson();
		JSONObject jsonObject = null;
		if (null != params) {
			jsonObject = new JSONObject(params);
		} else {

		}

		StringJsonObjectRequest<T> request = new StringJsonObjectRequest<T>(
				method, fullurl, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						onHttpListener.onFailure(error, error.getMessage(),0);
					}
				}, new StringJsonObjectRequest.Listener<Result>() {

					@Override
					public void onResponse(Result response) {

						if (specialRequest(context,response,0,null)){
							return;
						}
						if ( hippohandlerRequest(response.toString())) {

							onHttpListener.onFailure(new VolleyError(new NetworkError()), "", 4);
							return;
						}
						// TODO Auto-generated method stub
						try {
							onHttpListener.onSuccess((Result) response);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, tClass, params);

		StringJsonArrayRequest<T> requestArray = new StringJsonArrayRequest<T>(
				method, fullurl, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						onHttpListener.onFailure(arg0, arg0.getMessage(),0);
					}
				}, new StringJsonArrayRequest.Listener<T>() {

					@Override
					public void onResponse(ArrayList<T> response) {

						onHttpListener.onSuccess((ArrayList<Result>) response);
					}

			@Override
			public void onFailure(Exception e, String errorMsg, int s) {
				if (specialRequest(context,null,0,null)){
					return;
				}
			}
		}, tClass, params);

		if (isArray) {
			requestArray.setRetryPolicy(new DefaultRetryPolicy(3000, 1, 1));
			requestArray.setTag(this); 
			mRequestQueue.add(requestArray);
		} else {
			request.setRetryPolicy(new DefaultRetryPolicy(3000, 1, 1));
			request.setTag(this);
			
			mRequestQueue.add(request);
		}
	}

	/**
	 * 网络回调接口
	 * 
	 * @param <T>
	 */
	public interface OnHttpListener<T> {
		public void onSuccess(Result response) throws Exception;

		public void onSuccess(ArrayList<T> response);

		public void onFailure(Exception e, String errorMsg,int s);

	}
	public static String getHeaderAgent(Context context){
		String usertype = UserInfo.getInstance(context).getUserType();
		String useragent = "";
		String version = getVersion(context);
		String property = System.getProperty("http.agent");

		if (TextUtils.isEmpty(version)){
			version = UserInfo.getInstance(context).getVersion();
		}
		if (!TextUtils.isEmpty(usertype)){
		/*	if (usertype.equals("1")){
				useragent = "DGroupPatient/"+version+"/"+property;
			}else if(usertype.equals("3")){
				useragent = "DGroupDoctor/"+version+"/"+property;
			} else if(usertype.equals("10")){
				useragent = "Dgroupdoctorcompany/"+version+"/"+property;
			}else if(usertype.equals("9")){//*/
				useragent = "DGroupShop/"+version+"/"+property;
			/*}*/
		}
		return  useragent;
	}
	/**
	 * 获取版本号
	 * @return 当前应用的版本号
	 */
	public static String getVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			String version = info.versionName;
			return   version;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public boolean specialRequest(Context context,Result response,int code,String json){
		if (null!=response){
			if (response.resultCode==response.CODE_TOKEN_ERROR||response.resultCode==response.CODE_NO_TOKEN){

					MActivityManager.getInstance().finishAllActivity();
					SharedPreferenceUtil.putString("session", "");
					Intent intent = new Intent(context,LoginActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				return true;
			}else if(101==response.resultCode){

				CommonUiTools.appVersionUpdate(context, response.getResultMsg());
				return true;
			}
		}else if (code==response.CODE_TOKEN_ERROR||code==response.CODE_NO_TOKEN){

				MActivityManager.getInstance().finishAllActivity();
				SharedPreferenceUtil.putString("session", "");
				Intent intent = new Intent(context,LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			return true;
		}else if (null!= json&&json.contains("没有登录或登录已超时")){
			MActivityManager.getInstance().finishAllActivity();
			SharedPreferenceUtil.putString("session", "");
			Intent intent = new Intent(context,LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			return true;
		}

		return false;
	}
	public static boolean hippohandlerRequest(String response){
		if (!TextUtils.isEmpty(response)&&response.toString().contains("#type")&&response.toString().contains("#message")
				&&response.toString().contains("hippo")){
			return true;
		}
		return  false;
	}
}
