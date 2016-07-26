package com.dachen.medicine.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import com.dachen.medicine.adapter.MypopFeesMonthDetailAdapter;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.MyFeesMonthDetails;
import com.dachen.medicine.entity.MyFeesMonthDetails;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.logic.SubString;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;

public class MypopFeesMonthDetailsActivity extends BaseActivity implements OnHttpListener,OnClickListener{
	TextView tv_title;
	RelativeLayout rl_back;
	ListView listview;
	String medicineName = "";
	MypopFeesMonthDetailAdapter adapter;
	List<MyFeesMonthDetails.MyFees> myfees;
	List<MyFeesMonthDetails.MyFees> myfees2;
	TextView tv_des;
	String num = "";
	String id = "";
	boolean flag;
	Button btn_out;
	RelativeLayout rl_des;
	String selectpage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mypopfees_monthsdetails);
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_des = (RelativeLayout) findViewById(R.id.rl_des);
		rl_back.setOnClickListener(this);
		btn_out = (Button) findViewById(R.id.btn_out);
		btn_out.setOnClickListener(this);
		listview = (ListView) findViewById(R.id.listview);
		tv_title = (TextView) findViewById(R.id.tv_title);
		myfees = new ArrayList<MyFeesMonthDetails.MyFees>();
		myfees2 = new ArrayList<MyFeesMonthDetails.MyFees>();
		medicineName = getIntent().getStringExtra("name");
		tv_title.setText(SubString.getTitle(medicineName)+"收入明细");
		id = getIntent().getStringExtra("id");
		tv_des = (TextView) findViewById(R.id.tv_des);
		num = getIntent().getStringExtra("minfee");
		
		this.findViewById(R.id.btn_out).setOnClickListener(this);

		flag = getIntent().getBooleanExtra("flag", false);
		selectpage = getIntent().getStringExtra("selectpage");
		getdata(id);
		adapter = new MypopFeesMonthDetailAdapter(this,R.layout.adapter_mypopfeesmonths,myfees2);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
			/*	Intent intent = new Intent(MypopFeesMonthDetailsActivity.this,MypopFeeRecordActivity.class);
				MyFees myfees =  adapter.getItem(arg2);
				intent.putExtra("myfees_id",myfees.id);
				startActivity(intent);*/
			}
		});
		if (flag) {
			rl_des.setVisibility(View.GONE);
			btn_out.setBackgroundResource(R.drawable.btn_green_all2);
			btn_out.setTextColor(getResources().getColor(R.color.white));
			btn_out.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
				}
			});
		}else {
			rl_des.setVisibility(View.GONE);
			//tv_des.setText("该药最低体现额度为"+num+"元，继续加油哦！");
			btn_out.setTextColor(getResources().getColor(R.color.color_64ffffff));
			btn_out.setBackgroundResource(R.drawable.btn_green_all_notselect);
			btn_out.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					ToastUtils.showToast("该药最低体现额度为"+num+"元，继续加油哦！");
				}
			});
		}
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
		default:
			break;
		}
	}
	public void getdata(String id){
		HashMap<String, String> interfaces = new HashMap<String, String>();
		//interfaces.put("cash_info", "");
		interfaces.put("goods", id);
		String parameter = "";
		//interfaces.put("__ORDER_BY__", "created_time%20desc");
		if (selectpage.equals("audit")) {
			//待审核的推广费
			parameter = "c_tg_fee_store_user.get_tg_fees_1";
		}else if(selectpage.equals("waitgive")){
			parameter = "c_tg_fee_store_user.get_tg_fees_2";
		}else if(selectpage.equals("allhavemoney")){
			parameter = "c_tg_fee_store_user.get_tg_fees_3";
		}


		//   c_tg_fee_user.select
		 new HttpManager().get(
				Params.getInterface("invoke", parameter),
				 MyFeesMonthDetails.class,
				interfaces,
				this, false, 2);
	}	public void onSuccess(Result response) {
		// TODO Auto-generated method stub
		if (response instanceof MyFeesMonthDetails) {
			MyFeesMonthDetails fees = (MyFeesMonthDetails) response;
			if (fees.data!=null&&fees.data.size()>0) {
				myfees.clear();
				myfees2.clear();
				myfees.addAll(fees.data);
			}
			 	// TODO Auto-generated method stub
		
				if (  myfees.size() > 0) {

					for (int i = 0; i < myfees.size(); i++) {
						// LogUtils.burtLog("i=="+i);
						MyFeesMonthDetails fees2 = new MyFeesMonthDetails();
						MyFeesMonthDetails.MyFees info = fees2.new MyFees();
						info = myfees.get(i);
						MyFeesMonthDetails.MyFees info2 = fees2.new MyFees();
						if (i == 0) {
							boolean flag = false;  
								info2.setMon(true);
								info2.datetime = info.created_time;
								info2.calendar = parseDate(info.created_time);
								myfees2.add(info2);  
						} else {
							//取前面一个数据，如果相等，表示同一个月的，则多加入一条数据info2
							MyFeesMonthDetails.MyFees info3 = myfees.get(i - 1);
							if (!info.created_time.substring(0, 7).equals(
									info3.created_time.substring(0, 7))) {
								info2.setMon(true);
								info2.created_time = info.created_time;
								info2.calendar = parseDate(info.created_time);
								myfees2.add(info2); 
							}
						} 
						info.setMon(false);
						info.calendar = parseDate(info.created_time);
						myfees2.add(info);
					}
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
	public static String[] parseDate(String strDate) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date returnDate = null;
		try {
			returnDate = dateFormat.parse(strDate);
		} catch (ParseException e) {

		}
		Calendar c = Calendar.getInstance();
		c.setTime(returnDate);
		String[] s = new String[7];
		s[0] = (c.get(Calendar.YEAR)) + "";
		s[1] = (c.get(Calendar.MONTH) + 1) + "";
		s[2] = c.get(Calendar.DATE) + "";
		s[3] = change((c.get(Calendar.DAY_OF_WEEK) - 1) + "");

		long mills = 0;
		mills = c.getTimeInMillis();
		s[4] = mills + "";
		s[5] = c.get(Calendar.HOUR_OF_DAY)+"";
		s[6] = c.get(Calendar.MINUTE)+"";
		return s;
	}
	public static String change(String s) {
		String week = s;
		if (s.equals("1")) {
			week = "一";
		} else if (s.equals("2")) {
			week = "二";
		} else if (s.equals("3")) {
			week = "三";
		} else if (s.equals("4")) {
			week = "四";
		} else if (s.equals("5")) {
			week = "五";
		} else if (s.equals("6")) {
			week = "六";
		} else if (s.equals("7")) {
			week = "日";
		}else if (s.equals("0")) {
			week = "日";
		}
		return week;
	}
}
