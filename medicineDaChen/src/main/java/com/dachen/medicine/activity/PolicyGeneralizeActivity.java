package com.dachen.medicine.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dachen.medicine.R;
import com.dachen.medicine.adapter.PolicyAdapter;
import com.dachen.medicine.common.utils.JsonUtils.PolicyTranslate;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.entity.MedicineEntity;
import com.dachen.medicine.entity.MedicineEntity.MedicineInfo;
import com.dachen.medicine.entity.Policy;
import com.dachen.medicine.entity.PolicyData;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;

public class PolicyGeneralizeActivity extends BaseActivity implements
		OnClickListener ,OnHttpListener{ 
	RelativeLayout rl_back;
	PolicyAdapter adapter;
	ArrayList<Policy> list_data;
	ListView listview;
	RelativeLayout rl_nocotent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		setContentView(R.layout.activity_policygeneralize); 
		list_data = new ArrayList<Policy>();
		listview = (ListView) findViewById(R.id.lv_policygenerlize);
		rl_nocotent = (RelativeLayout)findViewById(R.id.rl_nocotent);
		showLoadingDialog();
		adapter = new PolicyAdapter(this,R.layout.adapter_policy, list_data);
		tv_title.setText("推广政策");
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		rl_nocotent.setVisibility(View.GONE);
		String s = "promotionPolicy/getDrugStorePolicy";
		HashMap<String,String> maps = new HashMap<>();
		maps.put("access_token", SharedPreferenceUtil.getString("session",""));
		maps.put("drugStoreId",SharedPreferenceUtil.getString("companyId",""));
		new HttpManager().get(this,
				s,
				PolicyData.class,
				maps,
				this, false, 3);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.rl_back:
			super.onBackPressed();
			break;
		default:
			break;
		}
	}
	public void onSuccess(Result response) {
		closeLoadingDialog();
		// TODO Auto-generated method stub
		if (response instanceof PolicyData) {
			PolicyData entity = (PolicyData) response;
			ArrayList<Policy> infos = PolicyTranslate.transData(entity.data);
			if (infos.size()>0) {
				list_data = infos;
				rl_nocotent.setVisibility(View.GONE);
				adapter = new PolicyAdapter(this,R.layout.adapter_policy, list_data);
				listview.setAdapter(adapter);
			}else {
				rl_nocotent.setVisibility(View.VISIBLE);
			}
		}
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