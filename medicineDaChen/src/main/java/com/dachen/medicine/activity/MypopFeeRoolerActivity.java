package com.dachen.medicine.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.R;
import com.dachen.medicine.adapter.MypopFeesadapter;
import com.dachen.medicine.entity.PopularizeData;
import com.dachen.medicine.entity.PopularizeData.Datas;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;

public class MypopFeeRoolerActivity extends BaseActivity implements OnHttpListener,OnClickListener{
	//
	MypopFeesadapter adapter;
	ListView listview;
	RelativeLayout rl_back;
	TextView tv_title;
	TextView tv_totalmoney;
	String num = "";
	List<Datas> list_datas;
	PopularizeData p;
	Button btn_out;
	TextView tv_needverify; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mypopfeerooler);
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		listview = (ListView) findViewById(R.id.listview);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText( "收入规则");
		//tv_title.setVisibility(View.GONE);
		getdata();
	}//http://192.168.3.7:9002/web/util/api_viewer.jsp?entity=c_tg_fee_user&operation=select_goods  
	//  arg0==={"money_min":"800.00","list_datas":[],"fee":"0.00"}

	public void getdata(){
	/*	 new HttpManager().post(
				Params.getInterface("invoke", "c_tg_fee_user.select_goods"),
				PopularizeData.class,
				null,
				this, false, 2);*/
	}
	public void onSuccess(Result response) {
		
		if (response instanceof PopularizeData) {
			 p = (PopularizeData)response;
			tv_totalmoney.setText(p.fee_audit);
			tv_needverify.setText(p.fee_unaudit);
			if (p.list_datas!=null&&p.list_datas.size()>0) {
				list_datas.clear();
				list_datas.addAll(p.list_datas);
				adapter.notifyDataSetChanged();
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
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.rl_back:
			finish();
			break; 
		case R.id.btn_out:
			break;
		case R.id.ll_verify:
			/*Intent intent = new Intent(this,);
			startActivity(intent);*/
			break;
		default:
			break;
		}
	} 
	
}
