package com.dachen.medicine.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.R;
import com.dachen.medicine.adapter.MypopFeeRecordAdapter;
import com.dachen.medicine.entity.FeesRecords;
import com.dachen.medicine.entity.FeesRecords.FeesRecord;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.Params;

public class MypopFeeRecordActivity extends BaseActivity implements OnClickListener{
	ListView listview;
	MypopFeeRecordAdapter adapter;
	String id = "";
	RelativeLayout rl_back;
	TextView tv_title;
	LinearLayout ll_guize;
	List<FeesRecord> lists;
	TextView tv_totalmoney;
	//List<R>
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mypopfeerecord);
		listview = (ListView) findViewById(R.id.listview);
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		
		tv_totalmoney = (TextView) findViewById(R.id.tv_totalmoney);
		ViewStub vstub_title = (ViewStub) findViewById(R.id.vstub_title);
		RelativeLayout ll_sub = (RelativeLayout) findViewById(R.id.ll_sub);
		View view = vstub_title.inflate(this, R.layout.layout_sub_scantitletext, ll_sub);
		ll_guize = (LinearLayout) view.findViewById(R.id.ll_guize);
		ll_guize.setOnClickListener(this);
		ll_guize.setVisibility(View.GONE);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("打款记录");
		lists = new ArrayList<FeesRecords.FeesRecord>();
		 adapter = new MypopFeeRecordAdapter(this, R.layout.adapter_mypopfeerecord, lists);
		 listview.setAdapter(adapter);
		 listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MypopFeeRecordActivity.this,MypopFeeRecordDetailActivity.class);
				if (null!= adapter.getItem(arg2).last_modifier&&!TextUtils.isEmpty(adapter.getItem(arg2).last_modifier.id)) {
					intent.putExtra("id", adapter.getItem(arg2).id);
					intent.putExtra("time", adapter.getItem(arg2).created_time);
					startActivity(intent);
				}
				
				
			}
		});
		id = getIntent().getStringExtra("myfees_id");
		getdata(id);
	}
	public void getdata(String id){
		/*HashMap<String, String> interfaces = new HashMap<String, String>(); 
		interfaces.put("cash_info", id);
		
		 new HttpManager().get(
				Params.getInterface("invoke", "c_tg_fee_user.select"),
				MyFeesDetails.class,
				interfaces,
				this, false, 2);*/
				 
				 new HttpManager().get(
						Params.getInterface("invoke", "c_cash_info.select"),
						FeesRecords.class,
						null,
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
		case R.id.ll_guize:
			Intent intent = new Intent(this,MypopFeeRoolerActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	public void onSuccess(Result response) {
		
		if (response instanceof FeesRecords) {
			FeesRecords p = (FeesRecords)response;
			lists.clear();
			lists.addAll(p.info_list);
			adapter.notifyDataSetChanged();
			float total = 0;
			if (null!=p.info_list&&p.info_list.size()>0) {
				for (int i = 0; i < p.info_list.size(); i++) {
					total += Float.parseFloat(p.info_list.get(i).fee);
				}
			}
			tv_totalmoney.setText(getdatafloat(total));
		}
	
	}
	public String getdatafloat(float price){
		DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.

		String p=decimalFormat.format(price);//
		return p;
	}

}
