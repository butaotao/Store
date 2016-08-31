package com.dachen.medicine.net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.library.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.MActivityManager;
import com.dachen.medicine.common.utils.MedicineApplication;
import com.dachen.medicine.common.utils.NetUtil;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.TimeUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.AppConfig;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.volley.custom.StringJsonArrayRequest;
import com.dachen.medicine.volley.custom.StringJsonObjectRequest;
import com.google.gson.Gson;
import com.j256.ormlite.stmt.mapped.MappedCreate;

/**
 * 接口请求管理类 作者 yuankangle 时间 2015/7/14
 */
public class HttpManager<T> {
	public static RequestQueue mRequestQueue;
	private Gson mGson;
	private Request<String> requestArray;

	/**
	 * post请求
	 * 
	 * @param context
	 * @param tClass
	 * @param onHttpListener
	 */
	@SuppressWarnings("unchecked")
	public void post(Context context, String interfaces, Class<T> tClass,
			Map<String, String> params,
			final OnHttpListener<Result> onHttpListener, boolean isArray,
			int net) {
		// request(Request.Method.POST, context, url, tClass, onHttpListener);
		request(Request.Method.POST, interfaces, context, tClass, params,
				onHttpListener, isArray, net);
	}
	@SuppressWarnings("unchecked")
	public void post(Context context, String interfaces, Class<T> tClass,
					 Map<String, String> params,
					 final OnHttpListener<Result> onHttpListener, boolean isArray,
					 int net,String port) {
		// request(Request.Method.POST, context, url, tClass, onHttpListener);
		request(Request.Method.POST, interfaces, context, tClass, params,
				onHttpListener, isArray, net,port);
	}
	@SuppressWarnings("unchecked")
	public void post(Context context, HashMap<String, String> interfaces,
			Class<T> tClass, Map<String, String> params,
			final OnHttpListener<Result> onHttpListener, int net) {
		// request(Request.Method.POST, context, url, tClass, onHttpListener);
		request(Request.Method.POST, interfaces, context, tClass, params,
				onHttpListener, false, net);
	}

	@SuppressWarnings("unchecked")
	public void post(Context context,HashMap<String, String> interfaces, Class<T> tClass,
			Map<String, String> params,
			final OnHttpListener<Result> onHttpListener, boolean isarray,
			int net) {
		// request(Request.Method.POST, context, url, tClass, onHttpListener);
		request(Request.Method.POST, interfaces,
				context, tClass, params,
				onHttpListener, isarray, net);
	}
	@SuppressWarnings("unchecked")
	public void post(Context context,HashMap<String, String> interfaces, Class<T> tClass,
			Map<String, String> params,
			final OnHttpListener<Result> onHttpListener, boolean isarray,
			int net,boolean isjson) {
		// request(Request.Method.POST, context, url, tClass, onHttpListener);
		request(Request.Method.POST, interfaces,
				context, tClass, params,
				onHttpListener, isarray, net,isjson);
	}
	@SuppressWarnings("unchecked")
	public void post(Context context,HashMap<String, String> interfaces, Class<T> tClass,
					 Map<String, String> params,
					 final OnHttpListener<Result> onHttpListener, boolean isarray,
					 int net,boolean isjson,String port) {
		// request(Request.Method.POST, context, url, tClass, onHttpListener);
		request(Request.Method.POST, interfaces,
				context, tClass, params,
				onHttpListener, isarray, net,isjson,port);
	}
	/**
	 * get请求
	 * 
	 * @param context
	 * @param tClass
	 * @param onHttpListener
	 */
	@SuppressWarnings("unchecked")
	public void get(Context context, String interfaces, Class<T> tClass,
			Map<String, String> params, OnHttpListener<Result> onHttpListener,
			boolean isArray, int net) {
		request(Request.Method.GET, interfaces, context, tClass, params,
				onHttpListener, isArray, net);
	}
	@SuppressWarnings("unchecked")
	public void get(Context context,Map<String, String> interfaces, Class<T> tClass,
			Map<String, String> params, OnHttpListener<Result> onHttpListener,
			boolean isArray, int net) {
		request(Request.Method.GET, interfaces,
				context, tClass, params,
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
	public void request(int method, Map<String, String> interfaces,
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
			int net,boolean isjson) {

		String fullurl = "";
		fullurl = AppConfig.getUrl(interfaces, net);

		requestBase(method, fullurl, context, tClass, params, onHttpListener,
				isArray, net, isjson);

	}
	@SuppressWarnings("unchecked")
	private void request(int method, Map<String, String> interfaces,
						 final Context context, final Class<T> tClass,
						 Map<String, String> params,
						 final OnHttpListener<Result> onHttpListener, boolean isArray,
						 int net,boolean isjson,String port) {

		String fullurl = "";
		fullurl = AppConfig.getUrl(interfaces, net,port);

		requestBase(method, fullurl, context, tClass, params, onHttpListener,
				isArray, net, isjson);

	}
	@SuppressWarnings("unchecked")
	public void requestBase(int method, String fullurl, final Context context,
			final Class<T> tClass, Map<String, String> params,
			final OnHttpListener<Result> onHttpListener, boolean isArray,
			int net,boolean isjson)  {
		if (!NetUtil.checkNetworkEnable(context)) { 
			onHttpListener.onFailure(null, "",-1);
			showErrToast(context,-1);
			return;
		}
		if (null==mRequestQueue) {
			  mRequestQueue = Volley.newRequestQueue(context);
				mRequestQueue.cancelAll(this);
		}
	
	//LogUtils.burtLog("fullurl===" + fullurl); 
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
					// TODO Auto-generated method stub
					if (specialRequest(context,response,0,null)){
						return;
					}

					onHttpListener.onSuccess((Result) response);

					 
				}

				@Override
				public void onFailure(Exception e, String errorMsg, int s) {
					// TODO Auto-generated method stub
					onHttpListener.onFailure(e, errorMsg, s);
					showErrToast(context,s);
					Log.d("yehj","request----onFailure");
					e.printStackTrace();
				}

			}, tClass, params,isjson);

	StringJsonArrayRequest<T> requestArray = new StringJsonArrayRequest<T>(
			method, fullurl, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError arg0) {
					// TODO Auto-generated method stub
					onHttpListener.onFailure(arg0, arg0.getMessage(),0);
					showErrToast(context,0);
				}
			}, new StringJsonArrayRequest.Listener<T>() {

				@SuppressWarnings("unchecked")
				@Override
				public void onResponse(ArrayList<T> response) {
					// TODO Auto-generated method stub
					onHttpListener.onSuccess((ArrayList<Result>) response);
				}

				@Override
				public void onFailure(Exception e, String errorMsg, int s) {


					if (specialRequest(context,null,s,null)){
						return;
					}
					// TODO Auto-generated method stub
					onHttpListener.onFailure(e, errorMsg, s);
					showErrToast(context,s);
					e.printStackTrace();
					Log.d("yehj","requestArray----onFailure");
				}
			}, tClass, params);

	if (isArray) {
		requestArray.setRetryPolicy(new DefaultRetryPolicy(30*1000, 1, 1f));
		requestArray.setTag(this); 
		mRequestQueue.add(requestArray);
	} else {
		request.setRetryPolicy(new DefaultRetryPolicy(30*1000, 1, 1f));
		request.setTag(this);
		
		mRequestQueue.add(request);
	}
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
	@SuppressWarnings("unchecked")
	public void request(int method, String interfaces, final Context context,
						 final Class<T> tClass, Map<String, String> params,
						 final OnHttpListener<Result> onHttpListener, boolean isArray,
						 int net,String port) {

		String fullurl = "";
		fullurl = AppConfig.getUrl(interfaces, net,port);
		requestBase(method, fullurl, context, tClass, params, onHttpListener,
				isArray, net);
	}
	@SuppressWarnings("unchecked")
	public void requestBase(int method, String fullurl, final Context context,
			final Class<T> tClass, final Map<String, String> params,
			final OnHttpListener<Result> onHttpListener, boolean isArray,
			int net) {
			if (!NetUtil.checkNetworkEnable(context)) { 
				onHttpListener.onFailure(null, "",-1);
				showErrToast(context,-1);
				return;
			}
			if (null==mRequestQueue) {
				  mRequestQueue = Volley.newRequestQueue(context);
					mRequestQueue.cancelAll(this);
			}
		
		//LogUtils.burtLog("fullurl===" + fullurl); 
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

						// TODO Auto-generated method stub
						onHttpListener.onSuccess((Result) response);

					}

					@Override
					public void onFailure(Exception e, String errorMsg, int s) {
						// TODO Auto-generated method stub
						onHttpListener.onFailure(e, errorMsg, s);
						showErrToast(context,s);
					}

 
				}, tClass, params,context){
			@Override
			protected void deliverResponse(String arg0) {
				if(null!= params&&params.get("interface3_btt")!=null){
					String condition = params.get("interface3_btt");
						String c[]=condition.split(";");
					if(null!=c&&c.length>=3){
						//for (int i=0;i<c.length;i++){
						arg0 =arg0.substring(arg0.indexOf(c[2]));
							arg0 =c[0]+arg0+c[1];
						//}
					}
				}
				super.deliverResponse(arg0);

			}
		};

		StringJsonArrayRequest<T> requestArray = new StringJsonArrayRequest<T>(
				method, fullurl, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						onHttpListener.onFailure(arg0, arg0.getMessage(),0);
						showErrToast(context,0);
					}
				}, new StringJsonArrayRequest.Listener<T>() {

					@SuppressWarnings("unchecked")
					@Override
					public void onResponse(ArrayList<T> response) {
						// TODO Auto-generated method stub

						onHttpListener.onSuccess((ArrayList<Result>) response);
					}

					@Override
					public void onFailure(Exception e, String errorMsg, int s) {
						// TODO Auto-generated method stub
						if (specialRequest(context,null,s,null)){
							return;
						}

						onHttpListener.onFailure(e, errorMsg, s);
						showErrToast(context,s);
					}
				}, tClass, params);

		if (isArray) {
			requestArray.setRetryPolicy(new DefaultRetryPolicy(30*1000, 1, 1f));
			requestArray.setTag(this); 
			mRequestQueue.add(requestArray);
		} else {
			request.setRetryPolicy(new DefaultRetryPolicy(30*1000, 1, 1f));
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
		public void onSuccess(Result response);

		public void onSuccess(ArrayList<T> response);

		public void onFailure(Exception e, String errorMsg,int s);

	}
	@SuppressWarnings("unchecked")
	public  void postJSON(Context context, Map<String, String>  interfaces, Class<T> tClass,
			Map<String, String> params,
			final OnHttpListener<Result> onHttpListener,
			int net) {
		// request(Request.Method.POST, context, url, tClass, onHttpListener);
		postJson(context, interfaces, tClass, params,
				onHttpListener);
	}@SuppressWarnings("unchecked")
	public void postJson(final Context context,Map<String, String>  interfaces, final Class<T> tClass,
			Map<String, String> params,
			final OnHttpListener<Result> onHttpListener){
		RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
		String fullurl = "";
		fullurl = AppConfig.getUrl(interfaces, 2);
		JSONObject jsonObject = new JSONObject(params);
		JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Method.POST,fullurl, jsonObject,
		    new Response.Listener<JSONObject>() {
		        @Override
		        public void onResponse(JSONObject response) {
		        	Gson gson = new Gson();
		        	Result result = null;


						if (specialRequest(context,null,0,response.toString())){
							return;
						};
		    		try {
		    			result = (com.dachen.medicine.entity.Result) gson.fromJson(response.toString(),
		    					tClass);
		    		} catch (Exception e) {
		    			onHttpListener.onFailure(new VolleyError(new NetworkError()), "", 4);
		    			// TODO: handle exception
		    			return;
		    		}
		    		if (null== result) {
		    			showErrToast(context,1);
		    			onHttpListener.onFailure(null,"",1);
		    			return;
		    		}
					if (specialRequest(context,result,0,null)){
						return;
					}

		    		onHttpListener.onSuccess(result);
		        }
		    }, new Response.ErrorListener() {
		        @Override
		        public void onErrorResponse(VolleyError error) {  
		        	onHttpListener.onFailure(null,"",1);
		        	showErrToast(context,1);
		    }
		    }
		    )
		    { 
		             
		    @Override
		    public Map<String, String> getHeaders() {
				Map<String, String> headers = null;
				try {
					headers = super.getHeaders();
				} catch (AuthFailureError authFailureError) {
					authFailureError.printStackTrace();
				}
				if (headers == null || headers.equals(Collections.emptyMap())) {
					headers = new HashMap<String, String>();
				}
		  /*      HashMap<String, String> headers = new HashMap<String, String>();
		        headers.put("Accept", "application/json");
		        headers.put("Content-Type", "application/json; charset=UTF-8");*/
				String agent = getHeaderAgent(context);
				if (!TextUtils.isEmpty(agent)){
						headers.put("User-Agent",agent);
				}
		        return headers;
		    }
		    @Override
			public void deliverError(VolleyError error) {
				// TODO Auto-generated method stub 
				if (null!=error&&error.getMessage().contains("ConnectException")) {
					onHttpListener.onFailure(error, "", 3);
					showErrToast(context,3);
				}else {
					onHttpListener.onFailure(error, "", 2);
					showErrToast(context,2);
				} 
				super.deliverError(error);
			}
			@Override
			protected VolleyError parseNetworkError(VolleyError volleyError) {
				System.err.println("volleyError"+volleyError.getMessage()); 
				// TODO Auto-generated method stub 
				return super.parseNetworkError(volleyError);
			}
		};
		jsonRequest.setRetryPolicy(new DefaultRetryPolicy(1000, 5, 1));
		jsonRequest.setTag(this);
		requestQueue.add(jsonRequest);

	}
	
	
	public void showErrToast(Context context,int s){ 
		if (s==2) {
			ToastUtils.showToast(context,context.getString(R.string.http_timeout));
		}else  if(s==-1){
			ToastUtils.showToast(context,context.getString(R.string.network_not_connected));
			
		}else if(s ==3){
			ToastUtils.showToast(context,context.getString(R.string.connect_error));
		}else if(s ==4){
			ToastUtils.showToast(context,context.getString(R.string.error_response));
		}else{
			ToastUtils.showToast(context,context.getString(R.string.data_exception));
		}
	}
	public static String getHeaderAgent(Context context){
		String usertype = UserInfo.getInstance(context).getUserType();
		String useragent = "";
		String version = getVersion(context);
		if (TextUtils.isEmpty(version)){
			version = UserInfo.getInstance(context).getVersion();
		}
		String property = System.getProperty("http.agent");

		String packageAllname = UserInfo.getInstance(context).getPackageName();
		String packagename = "";
		/*if (!TextUtils.isEmpty(packageAllname)&&packageAllname.contains(".")){
			if (packageAllname.length()>(packageAllname.lastIndexOf(".")+1)) {

				packagename = packageAllname.substring(packageAllname.lastIndexOf("."));
			}
		}*/

		if (!TextUtils.isEmpty(usertype)){
			if (usertype.equals("1")){
				packagename = "DGroupPatient";
				if (UserInfo.getInstance(context).getPackageName().equals(AppConfig.PACKAGENAME_PACIENT_BOGE)){
					packagename = "DGroupPatient_BDJL";
				}
			}else if(usertype.equals("3")){
				packagename = "DGroupDoctor";
				if (UserInfo.getInstance(context).getPackageName().equals(AppConfig.PACKAGENAME_DOCTOR_BOGE)){
					packagename = "DGroupDoctor_BDJL";
				}
			} else if(usertype.equals("10")){
				packagename = "Dgroupdoctorcompany";
			}
		}else {
			packagename = packageAllname;
		}
		useragent = packagename+"/"+version+"/"+property;
		return  useragent;
	}
	/**
	 * 获取版本号
	 * @return 当前应用的版本号
	 */
	public static String getVersion(Context context) {
		try {
			if (null!=context){
				PackageManager manager = context.getPackageManager();
				PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
				String version = info.versionName;
				return   version;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return "";
	}
	public static String getPackageName(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			String packageName = info.packageName;
			return   packageName;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	public boolean specialRequest(Context context,Result response,int code,String json){

		if (null!=response){
			if (response.resultCode==response.CODE_TOKEN_ERROR||response.resultCode==response.CODE_NO_TOKEN){
				if (null!=MedicineApplication.getCallApplicationInterface()){
					MActivityManager.getInstance().finishAllActivity();
					SharedPreferenceUtil.putString(context, "session", "");
					MedicineApplication.getCallApplicationInterface().startLoginActivity(context);
				}
				return true;
			}else if (response.resultCode==response.CODE_UPDATE_VERSION){
				if (null!=MedicineApplication.getCallApplicationInterface()&&null!=MedicineApplication.interfaces){
					MedicineApplication.interfaces.showUpdateDilog(context,response.getResultMsg());
					if (context!=null&&context instanceof Activity){

					}
					return  true;
				}
			}

		}else if (code==response.CODE_TOKEN_ERROR||code==response.CODE_NO_TOKEN){
			if (null!=MedicineApplication.getCallApplicationInterface()){
				MActivityManager.getInstance().finishAllActivity();
				SharedPreferenceUtil.putString(context, "session", "");
				MedicineApplication.getCallApplicationInterface().startLoginActivity(context);
			}
			return true;
		}else if (null!= json&&json.contains("没有登录或登录已超时")){
				MActivityManager.getInstance().finishAllActivity();
				SharedPreferenceUtil.putString(context, "session", "");
				MedicineApplication.getCallApplicationInterface().startLoginActivity(context);
				return true;
		}

		return false;
	}
	public static boolean hippohandlerRequest(String response){

		return  false;
	}
}
