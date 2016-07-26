package com.dachen.mediecinelibraryrealizedoctor.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.mediecinelibraryrealizedoctor.R;
import com.dachen.mediecinelibraryrealizedoctor.activity.BaseSearchActivity.CleanAllRefreshinferface;
import com.dachen.mediecinelibraryrealizedoctor.adapter.PreparedMedieAdapter;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineList;
import com.dachen.mediecinelibraryrealizedoctor.utils.DataUtils;

public class CollectChoiceMedieActivity extends BaseSearchActivity implements OnClickListener ,CleanAllRefreshinferface{
	ListView listview;
	PreparedMedieAdapter adapter;
	List<MedicineInfo> lists;
	TextView tv_title;
	ArrayList<MedicineInfo> list2;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {

			adapter.notifyDataSetChanged();
			closeLoadingDialog();

		};
	};

	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		LogUtils.burtLog("==layoutResID==="+layoutResID);
		super.setContentView(layoutResID);
		showLoadingDialog();
		initdata();
		getBuyCarData(true, false);
		getInterface();
		listview = (ListView) findViewById(R.id.listview);
		findViewById(R.id.rl_back).setOnClickListener(this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("我的最近选药");
		listview = (ListView) findViewById(R.id.listview);
		this.findViewById(R.id.rl_back).setOnClickListener(this);
		list2= new ArrayList<MedicineInfo>();
		adapter = new PreparedMedieAdapter(CollectChoiceMedieActivity.this, list2, choice,1);
		listview.setAdapter(adapter);
		refresh();
	}
	public void refresh(){
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				list2.clear();
				lists = MedicineList.getMedicineList().getCollectiong(CollectChoiceMedieActivity.this);
				if (null==lists||lists.size()==0) {
					lists =  new ArrayList<MedicineInfo>();
				}else {
					ArrayList<MedicineInfo> infos = DataUtils.getList(false, CollectChoiceMedieActivity.this, list2, (ArrayList<MedicineInfo>) lists);
					list2.clear();
					list2.addAll(infos);
				/*	ArrayList<MedicineInfo> 	record = MedicineList.getMedicineList().getShopingcard(CollectChoiceMedieActivity.this);
					for(MedicineInfo s: lists){
						MedicineInfo info = new MedicineInfo();
						info=s;
						if (null==record||record.size()==0) {
							if (s.goods!=null&&!TextUtils.isEmpty(s.goods.id)) {
								if(!list2.contains(s)) {
									info.num = 0;
									list2.add(info);
								}
							}
						}else  {
							boolean add = false;
							for (int i = 0; i < record.size(); i++) {
								if (!list2.contains(s)&&info.equals(record.get(i))) {
									info.num = record.get(i).num;
									list2.add(info);
									add = true;
									//break;
								}
							}
							if (!add&&!list2.contains(s)) {
								info.num = 0;
								list2.add(info);
							}
						}
					}*/
					try {
						if (list2.size()>0){
							ChoiceMedicineLogic.saveObject(CollectChoiceMedieActivity.this,ChoiceMedicineLogic.serialize(list2),
									"near");
						}

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//LogUtils.burtLog("===s111====="+list2.size()+"=="+lists.size());
					//	lists =  ChoiceMedicineLogic.deSerialization(ChoiceMedicineLogic.getObject(CollectChoiceMedieActivity.this,"near"),true);
					//LogUtils.burtLog("===s11111====="+list2.size()+"=="+lists.size());
				}

				handler.sendEmptyMessage(0);
			}
		}).start();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearchoice);

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		if (v.getId() == R.id.rl_back) {
			Intent intent = new Intent();
			setResult(5, intent);
			finish();
		}
	}
	@Override
	public void refreshClear(boolean clear) {
		if (clear){
			ArrayList<MedicineInfo> infos = MedicineList.getMedicineList().getCollectiong(CollectChoiceMedieActivity.this);
			list2.clear();



			for(MedicineInfo s: infos){
				MedicineInfo info =s;
				if (s.goods!=null&&!TextUtils.isEmpty(s.goods.id)) {
					if(!list2.contains(s)) {


						info.num = 0;

						list2.add(info);

					}
				}
			}
			adapter.notifyDataSetChanged();
		} else {
			refresh();
		}

	}
	@Override
	public CleanAllRefreshinferface getInterface() {
		// TODO Auto-generated method stub
		return this;
	}
} 