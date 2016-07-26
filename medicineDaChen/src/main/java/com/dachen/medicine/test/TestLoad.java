package com.dachen.medicine.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.test.AndroidTestCase;

import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.IllEntity;
import com.dachen.medicine.entity.MedicineEntity;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.google.gson.Gson;

public class TestLoad extends AndroidTestCase implements OnHttpListener {

	public void testencode() {
		String filter = "datetime>2015-01-01&&datetime<2016-01-01";
		String encode = URLEncoder.encode(filter);
		System.err.println("encode==" + encode);
	}

	public void testLoad() {
		final Gson mGson = new Gson();
		String json = "";
		InputStream is;
		try {
			is = getContext().getAssets().open("tree.txt");

			int size = is.available();

			// Read the entire asset into a local byte buffer.
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();

			// Convert the buffer into a string.
			json = new String(buffer, "GB2312");
			json = json.substring(1);
			json = json.substring(0, json.length() - 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		IllEntity entity = mGson.fromJson(json, IllEntity.class);
		LogUtils.burtLog("===================" + entity);
		ToastUtils.showToast(entity.toString());
		System.err.println(entity);
	}

	public void test() {
		/*
		 * new HttpManager().post(MedicineApplication.app, "invoke",
		 * Constants.GOOD_SELECT, MedicineEntity.class,
		 * Params.getGoodsInfo("EB", "1"), this, false, 2);
		 */
	}

	public void testLoad21() {
		final Gson mGson = new Gson();
		String json = "";
		InputStream is;
		try {
			is = getContext().getAssets().open("medie.txt");

			int size = is.available();

			// Read the entire asset into a local byte buffer.
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();

			// Convert the buffer into a string.
			json = new String(buffer, "GB2312");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		MedicineEntity entity = mGson.fromJson(json, MedicineEntity.class);
		LogUtils.burtLog("===================" + entity);
		ToastUtils.showToast(entity.toString());
		System.err.println(entity);
	}

	public void testLoad2() {
		String json = "123456";
		json = json.substring(1);
		json = json.substring(0, json.length() - 1);
		ToastUtils.showToast(json);
		LogUtils.burtLog("===================" + json);
		System.err.println(json);
	}

	// http://192.168.3.116:9283/web/api/invoke/04eda2699fd0412a93857f73d6f699e1/c_GoodsIndication.select?category=EB
	// (Context context,String url,Class<T> tClass, Map<String, String>
	// params,OnHttpListener<Result> onHttpListener)
	public void httptest() {
		System.err.println("=================");
		/*
		 * new HttpManager().post(MedicineApplication.app,Constants.GOOD_SELECT,
		 * Result.class, Params.getGoodsInfo("EB"), this);
		 */
		/*
		 * new
		 * HttpManager().post(MedicineApplication.app,Constants.SEND_AUTH_CODE,
		 * Result.class, Params.getSendMSMParams("", tem"plateID), this);
		 */
	}

	private void loginRequests(String phoneNum, String templateID) {
		System.err.println("=================");
		/*
		 * new
		 * HttpManager().post(MedicineApplication.app,Constants.SEND_AUTH_CODE,
		 * Result.class, Params.getSendMSMParams(phoneNum, templateID), this);
		 */
	}

	public void onSuccess(Result entity) {
		// TODO Auto-generated method stub
		System.err.println("entity==" + entity);
	}
 

	@Override
	public void onSuccess(ArrayList response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailure(Exception e, String errorMsg, int s) {
		// TODO Auto-generated method stub
		
	}

}
