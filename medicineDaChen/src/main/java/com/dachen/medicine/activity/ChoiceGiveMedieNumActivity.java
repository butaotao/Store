package com.dachen.medicine.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.R;
import com.dachen.medicine.adapter.AdapterChoiceGiveMedieNum;
import com.dachen.medicine.app.MedicineApplication;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.CdrugRecipeitem;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.entity.SubmitInfo;
import com.dachen.medicine.logic.ScaningData;

public class ChoiceGiveMedieNumActivity extends BaseActivity implements OnClickListener{
	ListView listview;
	AdapterChoiceGiveMedieNum adapter;
	List<CdrugRecipeitem> listScaningCompareScan;
	List<CdrugRecipeitem> listScaningCompareScan2;
	TextView tv_title;
	RelativeLayout rl_back; 
	String fromActivity ="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitygivemedienum);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("扫描");
		//用户可用积分兑换药品，请选择
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		this.findViewById(R.id.btn_over).setOnClickListener(this);
		findViewById(R.id.scan_title).setOnClickListener(this);
		if (null!=getIntent().getStringExtra("intent")) {
			fromActivity = getIntent().getStringExtra("intent");
		}

			listScaningCompareScan = ScaningData.getlistScaningCompareScan();

		listScaningCompareScan2 = new ArrayList<CdrugRecipeitem>();

		for (int i = 0; i < listScaningCompareScan.size(); i++) {
			 
			if (listScaningCompareScan.get(i).lists!=null&&listScaningCompareScan.get(i).lists.size()!=0) {
				listScaningCompareScan2.add(listScaningCompareScan.get(i));
			}
			
		} 
		listview = (ListView) findViewById(R.id.listview);
		
		
		adapter = new AdapterChoiceGiveMedieNum(this,R.layout.adapter_choicegivemedienum,listScaningCompareScan2);
		listview.setAdapter(adapter);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.rl_back:
			if (fromActivity.equals("medienuminconfiormactivity")) {
				Intent intent = new Intent(this,MedieNumInconformityActivity.class);
				intent.putExtra("back", "back");
				startActivity(intent);
			}else if (fromActivity.equals("salesclearkactivity")) {
				Intent intent = new Intent(this,SalesClerkActivity.class);
				startActivity(intent);
			} 
			super.onBackPressed();
			break;
		case R.id.scan_title:
			Intent intent = new Intent(this,MipcaActivityCaptures.class);
				intent.putExtra("code", "tiaoxingma_MedieNumInconformityActivity");
				startActivity(intent);
				super.onBackPressed();
			
			break;
		case R.id.btn_over:
			saveInfo();
			break;
		default:
			break;
		}
	}	//(String salesman,String patient,String recipe_id,String c_buydrugitems,String c_drug_codes) 
	 @Override
	public void onSuccess(Result response) {
		// TODO Auto-generated method stub
		 try {
			 super.onSuccess(response);
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 if(response instanceof SubmitInfo){
			
			try {
			SubmitInfo patientinfo = (SubmitInfo)response; 
			if(null == patientinfo.is_success){
				MedicineApplication.flag = true;
				ToastUtils.showToast(getResources().getString(R.string.toast_submit_fail)); 
				return;
			} 
			if (null!=patientinfo.is_success&&patientinfo.is_success.contains("true")||patientinfo.is_success.equals("1")) {
				ToastUtils.showToast(getResources().getString(R.string.toast_submit_success));
				//MedicineApplication.flag = true;
				Intent intent = new Intent(this,MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			//	super.onBackPressed();
				//MedicineApplication.app.getActivityManager().finishAllActivityExceptMainActivity();
			}else {
				ToastUtils.showToast(getResources().getString(R.string.toast_submit_fail));
			}
			} catch (Exception e) {
				// TODO: handle exception
				ToastUtils.showToast(getResources().getString(R.string.toast_submit_fail));
				//super.onBackPressed();
			}
		}
	}
	public void saveInfo(){

		if(ScaningData.saveSellInfoToServer(this,true,false)){
			super.onBackPressed();
			return;
		}
	}
}
