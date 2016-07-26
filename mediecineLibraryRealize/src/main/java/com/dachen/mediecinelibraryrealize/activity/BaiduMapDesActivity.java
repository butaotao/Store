package com.dachen.mediecinelibraryrealize.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.adapter.BaiduMedieRestoreDetailsAdapter;
import com.dachen.mediecinelibraryrealize.adapter.Params;
import com.dachen.mediecinelibraryrealize.entity.Drugstorefens;
import com.dachen.mediecinelibraryrealize.entity.DrugstorefensDes;
import com.dachen.mediecinelibraryrealize.entity.DrugstorefensDes.DrugList;

import java.util.ArrayList;
import java.util.List;

public class BaiduMapDesActivity extends BaseActivity implements OnHttpListener,OnClickListener{
	ListView listview;
	TextView tv_name;
	TextView tv_desta;
	TextView tv_locationdes;
	TextView tv_time;
	TextView tv_phone;
	TextView tv_support1;
	TextView tv_support2;
	TextView rl_matchnum;
	TextView tv_distance;
	Drugstorefens store; 
	TextView tv_support22;
	List<DrugList> list;
	BaiduMedieRestoreDetailsAdapter adapter;
	RelativeLayout rl_back;
	TextView tv_title;
	Button mBtAsk;
	String recipe_id;
	String doctorAndGroupName;
	String patient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baidudes); 
		recipe_id = getIntent().getStringExtra("recipe_id");
		String id = getIntent().getStringExtra("id");
		doctorAndGroupName = getIntent().getStringExtra("doctorAndGroupName");
		patient = getIntent().getStringExtra("patient");
		
		intView();
		/*Bundle bundle = new Bundle();
		bundle.putSerializable("Drugstorefens", fens);
		intent.putExtra("Drugstorefens", bundle);*/
		Bundle bundle = getIntent().getBundleExtra("Drugstorefens");
		if (bundle!=null) {
			store = (Drugstorefens) bundle.getSerializable("Drugstorefens");
		} 
		list = new ArrayList<DrugList>();
		if (store.list!=null){
			list = store.list;
		}
		adapter = new BaiduMedieRestoreDetailsAdapter(this, list);
		listview.setAdapter(adapter);
		setListViewHeightBasedOnChildren (listview);
		intViews(store);
	}
	public void setListViewHeightBasedOnChildren(ListView listView) {
		       // 获取ListView对应的Adapter
		        ListAdapter listAdapter = listView.getAdapter();
		       if (listAdapter == null) {
		            return;
		        }

	       int totalHeight = 0;
		        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			             // listAdapter.getCount()返回数据项的数目
			            View listItem = listAdapter.getView(i, null, listView);
			            // 计算子项View 的宽高
		             listItem.measure(0, 0);
			            // 统计所有子项的总高度
			            totalHeight += listItem.getMeasuredHeight();
		       }

		       ViewGroup.LayoutParams params = listView.getLayoutParams();
		       params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		         // listView.getDividerHeight()获取子项间分隔符占用的高度
		        // params.height最后得到整个ListView完整显示需要的高度
		         listView.setLayoutParams(params);
		     }

	@Override
	public void onFailure(Exception arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub
		closeLoadingDialog();
		 
	}
	@Override
	public void onSuccess(Result arg0) {
		// TODO Auto-
	}
	@Override
	public void onSuccess(ArrayList arg0) {
		// TODO Auto-generated method stub 
	}
	void intView(){
		listview = (ListView) findViewById(R.id.listview);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_desta = (TextView) findViewById(R.id.tv_desta);
		tv_locationdes = (TextView) findViewById(R.id.tv_locationdes);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_support1 = (TextView) findViewById(R.id.tv_support1);
		tv_support2 = (TextView) findViewById(R.id.tv_support2);
		rl_matchnum = (TextView) findViewById(R.id.rl_matchnum);
		tv_distance = (TextView) findViewById(R.id.tv_distance);
		tv_support22 = (TextView)findViewById(R.id.tv_support22);
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("药店详情");
		mBtAsk = (Button) findViewById(R.id.btAsk);
		mBtAsk.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId()==R.id.rl_back) {
			finish();
		}else if(v.getId()==R.id.btAsk){
			Intent intent = new Intent();
			intent.setClass(BaiduMapDesActivity.this,AskPriceActivity.class);
			intent.putExtra("recipe_id",recipe_id);
			intent.putExtra("doctorAndGroupName",doctorAndGroupName);
			intent.putExtra("patient",patient);
			startActivity(intent);
		}
		
	}
	public void intViews(Drugstorefens des){
		if (null==des){
			return;
		}
		tv_name.setText(""+des.name);
			tv_desta.setText(""+des.area);
		if (!TextUtils.isEmpty(des.yysjd)){
			tv_time.setText(""+des.yysjd);
		}else {
			tv_time.setText("");
		}

		String lxdz = "";
		if (null!=des) {
			if (!TextUtils.isEmpty(des.lxdz)) {
				lxdz = des.lxdz;
			}
			tv_locationdes.setText(lxdz);
			//	tv_time.setText(store.yysjd);
			String tele = "";
			if (!TextUtils.isEmpty(des.lxrsj)) {
				tele = des.lxrsj;
			}
			tv_phone.setText(tele);
			tv_support1.setText("");
			if (des.zcyb) {
				tv_support1.setText("支持医疗保险购药");
				tv_support1.setTextColor(getResources().getColor(R.color.color_background));
				//30b2cc
			}else {
				tv_support1.setText("不支持医疗保险购药");
				tv_support1.setTextColor(getResources().getColor(R.color.color_f74e5b));
				//f74e5b
			}
			if (des.zcsy) {
				tv_support22.setVisibility(View.VISIBLE);
				tv_support2.setTextColor(getResources().getColor(R.color.color_6e8fcf));
				//6e8fcf
				tv_support2.setText("支持送货上门");
				//tv_support22.setText("购药总价低于20元不送,送药时间为");
				tv_support22.setText("("+des.sybz+")");
			}else {
				tv_support22.setVisibility(View.GONE);
				tv_support2.setText("不支持送货上门");
				tv_support2.setTextColor(getResources().getColor(R.color.color_f74e5b));
				//f74e5b
			}

		}
		if (null!=store){
			rl_matchnum.setText(""+store.fen);//
			tv_distance.setText("距您"+store.mm_str);
		}
	}
}
