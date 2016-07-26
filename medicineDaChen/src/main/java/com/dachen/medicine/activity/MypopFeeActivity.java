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

public class MypopFeeActivity extends BaseActivity implements OnHttpListener,OnClickListener{
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
	String selectPage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mypopfee);
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		listview = (ListView) findViewById(R.id.listview);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_totalmoney = (TextView) findViewById(R.id.tv_totalmoney);
		tv_title.setText("我的推广费");
		tv_totalmoney.setText(num);
		selectPage = getIntent().getStringExtra("selectpage");
		if (selectPage.equals("audit")){
			tv_title.setText("待审核金额明细");
		}else if(selectPage.equals("waitgive")){
			tv_title.setText("账户余额收入明细");
		}else if(selectPage.equals("allhavemoney")){
			tv_title.setText("总收入金额明细");
		}



				tv_needverify = (TextView) findViewById(R.id.tv_needverify);
		findViewById(R.id.tv_records).setOnClickListener(this); 
		this.findViewById(R.id.btn_out).setOnClickListener(this);
		list_datas = new ArrayList<PopularizeData.Datas>();
	 	adapter = new MypopFeesadapter(this, R.layout.adapter_mypopfees, list_datas);
	 	listview.setAdapter(adapter);
	 	listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MypopFeeActivity.this,MypopFeesMonthDetailsActivity.class);
				boolean flag = false;
				try {
					float min = Float.parseFloat(p.money_min);
					float fee = Float.parseFloat(p.fee);
					if (fee>=min) {
						flag = true;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				if (null!=list_datas.get(arg2).goods) {
					intent.putExtra("id", list_datas.get(arg2).goods.id);
					intent.putExtra("minfee", p.money_min+"");
					intent.putExtra("name", list_datas.get(arg2).goods.title);
					intent.putExtra("flag", flag);
					intent.putExtra("selectpage",selectPage);
					startActivity(intent);
				}
				
			}
		});
		getdata();
	}//http://192.168.3.7:9002/web/util/api_viewer.jsp?entity=c_tg_fee_user&operation=select_goods  
	//  arg0==={"money_min":"800.00","list_datas":[],"fee":"0.00"}

	public void getdata(){
		showLoadingDialog();
		//c_tg_fee_user.select_goods
		if (selectPage.equals("audit")){
			//待审核的推广费
			new HttpManager().get(
					Params.getInterface("invoke", "c_tg_fee_store_user.get_tg_fee_1"),
					PopularizeData.class,
					null,
					this, false, 2);
		}else if(selectPage.equals("waitgive")){
			//待发放
			new HttpManager().get(
					Params.getInterface("invoke", "c_tg_fee_store_user.get_tg_fee_2"),
					PopularizeData.class,
					null,
					this, false, 2);
		}else if(selectPage.equals("allhavemoney")){
			//总收入
			new HttpManager().get(
					Params.getInterface("invoke", "c_tg_fee_store_user.get_tg_fee_3"),
					PopularizeData.class,
					null,
					this, false, 2);
		}


	}
	public void onSuccess(Result response) {
		closeLoadingDialog();
		if (response instanceof PopularizeData) {
			 p = (PopularizeData)response;
			tv_totalmoney.setText(p.fee_audit);
			tv_needverify.setText(p.fee_unaudit);
			if (p.data!=null&&p.data.size()>0) {
				list_datas.clear();
				list_datas.addAll(p.data);
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
		case R.id.tv_records:
			Intent intent = new Intent(this,MypopFeeRecordActivity.class);
			//intent.putExtra(name, value);
			startActivity(intent);
			break;
			case R.id.rl_waitaudit:

		default:
			break;
		}
	} 
	
}
