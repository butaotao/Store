package com.dachen.medicine.volley.custom;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * 通过字符串参数集，请求json参数，并序列号为JsonModel对象
 *
 * @author dty
 *
 * @param <T>
 */
public class StringJsonObjectRequest<T> extends Request<String> {

	public static interface Listener<Result> {
		void onResponse(Result t);
		void onFailure(Exception e, String errorMsg,int s);
	}

	private Listener<Result> mListener;
	private Class<T> mClazz;
	private Map<String, String> mParams;

	private boolean mGzipEnable = false;
	private Gson gson = new Gson();
	private boolean isJson = true;
	Context context;
	/**
	 *
	 * 请求方式post
	 *
	 * @param url
	 *            url地址
	 * @param listener
	 */
	public StringJsonObjectRequest(String url, ErrorListener errorListener,
			Listener<Result> listener, Class<T> clazz,
			Map<String, String> params,Context context) {
		this(Method.POST, url, errorListener, listener, clazz, params,context);
	}

	/**
	 *
	 * @param method
	 *            请求方式，post或者get
	 * @param url
	 *            url地址
	 * @param listener
	 */
	public StringJsonObjectRequest(int method, String url,
			ErrorListener errorListener, Listener<Result> listener,
			Class<T> clazz, Map<String, String> params,Context context) {
		super(method, url, errorListener);
		mListener = listener;
		mClazz = clazz;
		mParams = params;
		this.context = context;
		if (method == Method.GET) {
			spliceGetUrl();
		}
	}
	public StringJsonObjectRequest(int method, String url,
			ErrorListener errorListener, Listener<Result> listener,
			Class<T> clazz, Map<String, String> params,boolean isJson) {
		super(method, url, errorListener);
		mListener = listener;
		mClazz = clazz;
		mParams = params;
		this.isJson = isJson;
		if (method == Method.GET) {
			spliceGetUrl();
		}
	}
	public void setGzipEnable(boolean eanble) {
		mGzipEnable = eanble;
	}

	/* Post 参数设置 */
	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		if (getMethod() != Method.POST && getMethod() != Method.PUT) {
			return null;
		}
		return mParams;
	}
	@Override
	public void deliverError(VolleyError error) {
		// TODO Auto-generated method stub
			if (null!=error&&null!=error.getMessage()&&error.getMessage().contains("ConnectException")) {
				mListener.onFailure(error, "", 3);
			}else {
				mListener.onFailure(error, "", 2);
			}
		super.deliverError(error);
	}
	@Override
	protected VolleyError parseNetworkError(VolleyError volleyError) {
		//System.err.println("volleyError"+volleyError.getMessage());
		LogUtils.burtLog(LogUtils.W,"ERROR"+volleyError.getMessage());
		// TODO Auto-generated method stub
		return super.parseNetworkError(volleyError);
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		//System.err.println("header");
		String useragent = HttpManager.getHeaderAgent(context);
		Map<String, String> headers = super.getHeaders();
		if (headers == null || headers.equals(Collections.emptyMap())) {
			headers = new HashMap<String, String>();
		}
		if (mGzipEnable) {
			/*Map<String, String> headers = new HashMap<String, String>();
			headers.put("Charset", "UTF-8");
			headers.put("Content-Type", "application/x-www-form-urlencoded");
			headers.put("Accept-Encoding", "gzip,deflate");*/
			if (!TextUtils.isEmpty(useragent)){
				headers.put("User-Agent",useragent);
			}
			return headers;
		} else {
		/*	Map<String, String> headers = new HashMap<String, String>();
			headers.put("Charset", "UTF-8");
			//headers.put("content-length", "100000000");
			headers.put("Content-Type", "application/x-www-form-urlencoded");*/

			if (!TextUtils.isEmpty(useragent)){
				headers.put("User-Agent",useragent);
			}
			return headers;
		}
	}

	/* Get 参数拼接 */
	private void spliceGetUrl() {
		if (mParams != null && mParams.size() > 0) {
			String url = getUrl();
			if (TextUtils.isEmpty(url)) {
				return;
			}
			if (url != null && !url.contains("?")) {
				url += "?";
			}
			String param = "";
			for (String key : mParams.keySet()) {
				param += (key + "=" + mParams.get(key) + "&");
			}
			param = param.substring(0, param.length() - 1);// 去掉最后一个&
			setUrl(url + param);
		}
	}

	@Override
	protected void deliverResponse(String arg0) {
		if (isJson) {

		if (mListener == null) {
			return;
		}
		if (TextUtils.isEmpty(arg0)) {
			deliverError(new VolleyError(new NetworkError()));
			return;
		}
		Result result = null;
		try {
			result = (com.dachen.medicine.entity.Result) gson.fromJson(arg0,
					mClazz);
		} catch (Exception e) {
			mListener.onFailure(new VolleyError(new NetworkError()), "", 4);
			// TODO: handle exception
			return;
		}

		if (null== result) {
			deliverError(new VolleyError(new NetworkError()));
			return;
		}

		mListener.onResponse(result);

		}else {
			Result result = new Result();
			result.resultMsg = arg0;
			mListener.onResponse(result);
		}
	}

	private T castValue(Class<T> clazz, String data) {
		try {
			Constructor<T> constructor = clazz.getConstructor(String.class);
			return constructor.newInstance(data);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		String parsed;
		try {
			if (mGzipEnable) {
				parsed = getRealString(response.data);
			} else {
				parsed = new String(response.data,
						HttpHeaderParser.parseCharset(response.headers));
			}
		} catch (UnsupportedEncodingException e) {
			parsed = new String(response.data);
		}
		return Response.success(parsed,
				HttpHeaderParser.parseCacheHeaders(response));
	}

	private int getShort(byte[] data) {
		return (int) ((data[0] << 8) | data[1] & 0xFF);
	}

	/**
	 * GZip解压缩
	 */
	private String getRealString(byte[] data) {
		//System.err.println("11");
		byte[] h = new byte[2];
		h[0] = (data)[0];
		h[1] = (data)[1];
		int head = getShort(h);
		boolean t = head == 0x1f8b;
		InputStream in;
		StringBuilder sb = new StringBuilder();
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			if (t) {
				in = new GZIPInputStream(bis);
			} else {
				in = bis;
			}
			BufferedReader r = new BufferedReader(new InputStreamReader(in),
					1000);
			for (String line = r.readLine(); line != null; line = r.readLine()) {
				sb.append(line);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}
