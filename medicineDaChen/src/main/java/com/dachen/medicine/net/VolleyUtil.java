package com.dachen.medicine.net;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * 工具类 单例
 * 
 * @author WANG
 * 
 */
public class VolleyUtil {

	private volatile static RequestQueue requestQueue;

	public static RequestQueue getQueue(Context context) {
		if (requestQueue == null) {
			synchronized (VolleyUtil.class) {
				if (requestQueue == null) {
					requestQueue = Volley.newRequestQueue(context
							.getApplicationContext());
					// requestQueue.start();
				}
			}
		}

		return requestQueue;
	}

	public static <T> void setTimeOut(Request<T> request, int timeOut) {
		request.setRetryPolicy(new DefaultRetryPolicy(timeOut, 0, 0));
	}

	public static <T> void addRequest(Context context, Request<T> request) {
		setTimeOut(request, 10000);
		getQueue(context).add(request);
	}

	public static void cancelAll(Context context, Object tag) {
		getQueue(context).cancelAll(tag);
	}

}
