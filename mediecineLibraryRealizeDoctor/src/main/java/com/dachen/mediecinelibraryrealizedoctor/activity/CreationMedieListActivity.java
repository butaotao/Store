package com.dachen.mediecinelibraryrealizedoctor.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.mediecinelibraryrealizedoctor.R;
import com.dachen.mediecinelibraryrealizedoctor.activity.BaseSearchActivity.CleanAllRefreshinferface;
import com.dachen.mediecinelibraryrealizedoctor.adapter.PreparedMedieAdapter;
import com.dachen.mediecinelibraryrealizedoctor.entity.Constants;
import com.dachen.mediecinelibraryrealizedoctor.entity.CreateMedieList;
import com.dachen.mediecinelibraryrealizedoctor.entity.CreateMedieList.Medies;
import com.dachen.mediecinelibraryrealizedoctor.entity.GoodsUsagesGoods;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineList;
import com.dachen.mediecinelibraryrealizedoctor.entity.ResponseID;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreationMedieListActivity extends BaseSearchActivity implements OnClickListener,OnHttpListener,CleanAllRefreshinferface{
	ListView listview;
	PreparedMedieAdapter adapter;
	List<MedicineInfo> lists;
	TextView tv_title;
	RelativeLayout rl_back;
	SharedPreferences mSharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creationmedie);

	}
	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
		if (null!=getIntent().getStringExtra("canceltoback")){
			//finish();

		};
		initdata();
		getBuyCarData(true,false);
		getInterface();
		String name = SharedPreferenceUtil.getString(this,"createmeidelist_username", "");
		if (null!= findViewById(R.id.btn_add)) {
			findViewById(R.id.btn_add).setOnClickListener(this);
		}
		if (null!= findViewById(R.id.btn_saves)) {
			findViewById(R.id.btn_saves).setOnClickListener(this);
		}
		listview = (ListView) findViewById(R.id.listview);
		lists = new ArrayList<MedicineInfo>();
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("创建"+name+"的自定义药单");
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		ArrayList< MedicineInfo> maps = null;
		try {
			//lists = ChoiceMedicineLogic.deSerialization(ChoiceMedicineLogic.getObject(this,"shoppingcart"),true);
			lists = MedicineList.getMedicineList().getShopingcard(this);
			adapter = new PreparedMedieAdapter(this, lists, choice,3);
			if (null!=lists) {
				listview.setAdapter(adapter);
			}


		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		if (v.getId() == R.id.rl_back) {
			finish();
			//MActivityManager.getInstance().finishActivity(Crem);
		/*	Intent intent = new Intent(this,);
			startActivity(intent);*/
		}else if (v.getId() == R.id.btn_add) {
			String name = SharedPreferenceUtil.getString(this,"createmeidelist_username", "");
			String id = SharedPreferenceUtil.getString(this, "createmeidelist_userid", "");
			MedicineList.getMedicineList().infoCurrent = new ArrayList<>();
			List<MedicineInfo> info = MedicineList.getMedicineList().getShopingcard(CreationMedieListActivity.this);
			if (info!=null&&info.size()>0){
				for (int i=0;i<info.size();i++){
					try {
						MedicineList.getMedicineList().infoCurrent.add((MedicineInfo) info.get(i).deepCopy());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			Intent intent = new Intent(this,PreparedMedieActivity.class);
			intent.putExtra("name", name);
			intent.putExtra("id", id);
			startActivity(intent);
			finish();
		}else if (v.getId() == R.id.btn_saves) {
			CreateMedieList list = new CreateMedieList();
			List<Medies> jsons = new ArrayList<Medies>();

			boolean create = false;
			if (null!=lists&&lists.size()>0) {
				for (int i = 0; i < lists.size(); i++) {
					Medies m = list.new Medies();
					if (null!=lists.get(i).goods) {
						m.goodsId = lists.get(i).goods.id;
						m.goodsNumber = lists.get(i).num;
						if (lists.get(i).num!=0){
							create = true;
						}else {
							continue;
						}
						CreateMedieList l = new CreateMedieList();
						List<GoodsUsagesGoods>goodsUsagesGoodsList = lists.get(i).goods_usages_goods;
						if(null!=goodsUsagesGoodsList && goodsUsagesGoodsList.size()>0){
							GoodsUsagesGoods goodsUsagesGood = goodsUsagesGoodsList.get(0);
							m.patients = goodsUsagesGood.patients+"";
							CreateMedieList.Period period =l.new Period();
//							m.period = period;
							if(null!=goodsUsagesGood.period){
								m.periodNum = goodsUsagesGood.period.text+"";
//								m.period.text = goodsUsagesGood.period.text+"";
								//m.periodTimes表示服药单位（month） ,goodsUsagesGood.period.unit表示服药单位（month）
								m.periodUnit = goodsUsagesGood.period.unit;
								m.doseUnit = goodsUsagesGood.period.medieUnit+"";
								m.doseDays = "0";
								m.doseQuantity = goodsUsagesGood.quantity;
							}else {
								m.periodNum = "";
//								m.period.text = "";
								m.periodUnit = "";
								//m.unit = "";
							}
							m.periodTimes = goodsUsagesGood.times;
							//m.doseUnit = goodsUsagesGood.quantity+"";
							if (TextUtils.isEmpty(goodsUsagesGood.method)){
								m.doseMothod = "";
							}else {
								m.doseMothod = goodsUsagesGood.method+"";
							}

						}
						jsons.add(m);
					}
				}
				if (!create){
					ToastUtils.showToast(this,"请添加药品数量后再创建药方");
					return;
				}
				list.json = jsons;
				HashMap<String, String> maps = new HashMap<String, String>();

				Gson g = new Gson();
				String json = g.toJson(jsons);
//				if (json.contains("}")&&json.contains("{")) {
//					json = json.substring(1,json.length()-1);
//					json = json.replace("\"json\":", "json:");
//				}
				mSharedPreferences = getSharedPreferences("login_user_info",
						Context.MODE_PRIVATE);
				String patient = mSharedPreferences.getString("user_id", "");
				String userId = UserInfo.getInstance(CreationMedieListActivity.this).getId();
				String access_token = UserInfo.getInstance(CreationMedieListActivity.this).getSesstion();
				String id = SharedPreferenceUtil.getString(this,"createmeidelist_userid", "");
				maps.put("access_token",access_token);
				maps.put("userId", userId);
				maps.put("patientId", id);
				maps.put("type", "1");//1代表患者自建
				maps.put("detailJson", json);

				new HttpManager().post (this, Constants.ADD_RECIPE,
						ResponseID.class,
						maps,
						this,false, 1);
				ClenAll(true);
			}else {
				return;
			}

		}
	}@Override
	 public void onFailure(Exception arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub
		ToastUtils.showToast(this,"创建失败");
	}

	@Override
	public void onSuccess(Result arg0) {
		// TODO Auto-generated method stub
		if (arg0 instanceof ResponseID) {
			ResponseID ids = (ResponseID)arg0;
			if (ids.getResultCode() == 1) {
				Intent intent = new Intent();
				intent.putExtra("refresh", "refresh");
				setResult(11, intent);
				ToastUtils.showToast(this,"创建成功");
				finish();
			}else {
				ToastUtils.showToast(this,"创建失败");
			}
		}
	}
	@Override
	public CleanAllRefreshinferface getInterface() {
		// TODO Auto-generated method stub
		return this;
	}
	@Override
	public void refreshClear(boolean clear) {
		// TODO Auto-generated method stub

	}
}
