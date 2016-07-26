package com.dachen.medicine.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.R;
import com.dachen.medicine.adapter.MypopFeesRecordDetailadapter;
import com.dachen.medicine.entity.MypoprecordDetails;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.entity.MypoprecordDetails.MypoprecordDetail;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;

public class MypopFeeRecordDetailActivity extends BaseActivity{
	MypopFeesRecordDetailadapter adapter;
	ListView listview;
	String id = "";
	TextView tv_title;
	String time = "";
	RelativeLayout rl_back;
	List<MypoprecordDetail> lists;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mypopfeerecorddetail);
		listview = (ListView) findViewById(R.id.listview);
		tv_title = (TextView) findViewById(R.id.tv_title);
		time = getIntent().getStringExtra("time");
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		lists = new ArrayList<MypoprecordDetails.MypoprecordDetail>();
		if (!TextUtils.isEmpty(time)&&time.length()>10) {
			 time = time.substring(0,10);
		}
		tv_title.setText(time+"打款明细");
		 adapter = new MypopFeesRecordDetailadapter(this, R.layout.adapter_mypopfees_record_detail, lists);
		 listview.setAdapter(adapter);
		id = getIntent().getStringExtra("id");
		getData(id);
	}
	public void getData(String id){
		HashMap<String, String> maps = new HashMap<String, String>();
		maps.put("cash_info", id);
		 new HttpManager().get(
					Params.getInterface("invoke", "c_tg_fee_user.select"),
					MypoprecordDetails.class,
					maps,
					this, false, 2);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.rl_back:
			finish();
			break;

		default:
			break;
		}
	}
	@Override
	public void onSuccess(Result response) {
		// TODO Auto-generated method stub
		super.onSuccess(response);
		if (response instanceof MypoprecordDetails) {
			MypoprecordDetails details = (MypoprecordDetails) response;
			lists.clear();
			lists.addAll(details.info_list);
			adapter.notifyDataSetChanged();
		}
	}
}
