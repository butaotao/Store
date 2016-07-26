package com.dachen.medicine.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.R;
import com.dachen.medicine.adapter.ScaningImparityAdapter;
import com.dachen.medicine.bean.MedieNum;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.CdrugRecipeitem;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.entity.SelectListInterface;
import com.dachen.medicine.logic.CompareData;
import com.dachen.medicine.logic.ScaningData;
import com.dachen.medicine.net.HttpManager.OnHttpListener;

public class MedieNumInconformityActivity extends BaseActivity implements SelectListInterface,OnClickListener,OnHttpListener{
	ListView lv_notSame; 
	//多买或者少买的药品
	List<CdrugRecipeitem>  listmedies_more_less;
	List<CdrugRecipeitem> listdrug;
	List<CdrugRecipeitem> listmedies;
	List<CdrugRecipeitem> listmedieForChoiceReson;
	List<CdrugRecipeitem> listScaningCompareScan;
	Button btn_over;
	RelativeLayout iv_back;
	ViewStub vstub_title;
	RelativeLayout ll_sub;
	ImageView scan_title;
	TextView tv_title;
	TextView tv_back; 
	boolean flag = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medienum_inconformity);
		//不一致的药品
		listmedies_more_less  =ScaningData.getListMedies_more_less();
	 
		lv_notSame = (ListView) findViewById(R.id.lv_notSame);
		listScaningCompareScan = new ArrayList<CdrugRecipeitem>();
		//购药清单
		listdrug = ScaningData.getlistdrug();
		//患者选购的药（包括多少两种状态）
		listmedies = ScaningData.getListmedies();
		
		listmedieForChoiceReson = new ArrayList<CdrugRecipeitem>();

		
		for (int i = 0; i < listmedies_more_less.size(); i++) { 
			int m = 0;
			int n = 0;
			int haveBuy = 0;
			int require = 0;
			CdrugRecipeitem item = listmedies_more_less.get(i); 
			if (0!=item.bought_quantity) {
				haveBuy = listmedies_more_less.get(i).bought_quantity;
			}
			if (item.numResone == -11111) {
				n = 0;
			}else {
				n = item.numResone ;
			}
			if (0!=item.requires_quantity) {
				require = item.requires_quantity;
			}
			m = n + haveBuy;
			if (m > require && n>0) {
				listmedieForChoiceReson.add(listmedies_more_less.get(i));
			}else if(m < require && m>0){
				listmedieForChoiceReson.add(listmedies_more_less.get(i));
			}else if (m == 0) {
				listmedieForChoiceReson.add(listmedies_more_less.get(i));
			}
		}
		ScaningImparityAdapter adapter = new ScaningImparityAdapter(this,R.layout.adapter_scaningimparity,listmedieForChoiceReson,this);
		View viewhead = View.inflate(this, R.layout.layout_view_white, null);
		lv_notSame.addHeaderView(viewhead);
		lv_notSame.setAdapter(adapter);
		btn_over = (Button) findViewById(R.id.btn_over);
		btn_over.setOnClickListener(this);
		iv_back = (RelativeLayout) findViewById(R.id.rl_back); 
		iv_back.setOnClickListener(this);
		vstub_title = (ViewStub) findViewById(R.id.vstub_title);
		ll_sub = (RelativeLayout) findViewById(R.id.ll_sub);
		scan_title = (ImageView) this.findViewById(R.id.scan_title);
		tv_title = (TextView) this.findViewById(R.id.tv_title);
		tv_back = (TextView) findViewById(R.id.tv_back);
		tv_title.setText("扫码销售");
		tv_back.setVisibility(View.VISIBLE);
		tv_back.setText("返回");
		scan_title.setOnClickListener(this); 


		getSelect(listmedieForChoiceReson, 100);

		if (getGaveCompareScan()) {
			btn_over.setBackgroundResource(R.drawable.btn_green);
			btn_over.setText("下一步");
		}else {
			btn_over.setText("完成");
		}
	}

	  @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_over:

			if (flag) {
				
				saveInfo();
			}else {
				ToastUtils.showToast("请选择原因！"); 
			}
			break;
		case R.id.scan_title:
			 intent = new Intent(this,MipcaActivityCaptures.class);

				intent.putExtra("code", "tiaoxingma_MedieNumInconformityActivity");

				startActivity(intent);
			super.onBackPressed();
			break;
		case R.id.rl_back:
			back();
			super.onBackPressed();
			break;
		default:
			break;
		}
	} 
	  @Override
	   public void onBackPressed() { 
		   super.onBackPressed();
		  return;
	  }
	  public boolean getGaveCompareScan(){
			listScaningCompareScan.clear(); 
			for (int i = 0; i < listmedies.size(); i++) {
				if (null != listmedies.get(i).lists) { 
					for (int j = 0; j < listdrug.size(); j++) { 
						if (listmedies.get(i).equals(listdrug.get(j))) { 
							if (CompareData.isShow(listdrug.get(j))&&listmedies.get(i).foundCode) {
								listScaningCompareScan.add(listmedies.get(i)); 
							}
						}
					}
				}
			} 
			if (listScaningCompareScan.size()>0) {
				return true;
			}
			return false;
		}
	  public void saveInfo(){
		  if (getGaveCompareScan()) {
				Intent intent = new Intent(this,ChoiceGiveMedieNumActivity.class);

			  ScaningData.setListScaningCompareScan(listScaningCompareScan);


				intent.putExtra("intent", "medienuminconfiormactivity");
				startActivity(intent);
				super.onBackPressed();
			}else{ 
			  if(ScaningData.saveSellInfoToServer(this,false,false)){
				  super.onBackPressed();
				  return;
			  }
		}
	 }
	@Override
	public void onSuccess(Result response) {
		LogUtils.burtLog(response+"");
		closeLoadingDialog();
		// TODO Auto-generated method stub
		if (null!=response) {
			if(null != response.errormessage){
				ToastUtils.showToast( response.errormessage);
				super.onBackPressed();
				return;
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
	public int getSelect(List<CdrugRecipeitem> listmedies_more_less, int isOver) {
		// TODO Auto-generated method stub
		flag = true;
		for (int i = 0; i < listmedies_more_less.size(); i++) { 
			if (listmedies_more_less.get(i).isSlected.equals("-1")) { 
				flag = false;
				break;
			}else if(listmedies_more_less.get(i).isSlected.equals("-2")){
				flag = false;
				break;
			}
		}
		if (100!=isOver){
			List<CdrugRecipeitem> titles = ScaningData.getlistScaning_showIntitleBar() ;
			for (int k=0;k<titles.size();k++){
				CdrugRecipeitem t  =  titles.get(k);
				boolean flag = false;
				for (int j=0;j<listmedies_more_less.size();j++){
					ArrayList<MedieNum>  medieNums = listmedies_more_less.get(j).lists;
					if (null!=medieNums){
						int num = 0;
						for (int m = 0;m<medieNums.size();m++){
							MedieNum n = medieNums.get(m);
							if (t.scanCode.substring(0, 7).equals(n.num.substring(0,7))){
								num++;
								flag = true;

							}
						}
						if(flag){
							t.scaningNum = num;
							titles.set(k,t);
							break;
						}
					}
				}

				if (!flag ){
					titles.remove(k);
				}
			}
		}

		
		
		for (int i = 0; i < listmedies.size(); i++) {
			for (int j = 0; j < listmedies_more_less.size(); j++) {
				CdrugRecipeitem items = null;
				try {
					items = (CdrugRecipeitem) listmedies_more_less.get(j).deepCopy();
					CdrugRecipeitem itemselect =(CdrugRecipeitem) listmedies.get(i).deepCopy();
					if (itemselect.id.equals(items.id)) {  
						itemselect.isAdd = items.isAdd;
						itemselect.lists = items.lists;
						itemselect.numResone = items.numResone;
						itemselect.isSlected = items.isSlected;
						listmedies.set(i, itemselect);
			ScaningData.setListmedies(listmedies);
					} 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			} 
			
		}
		if (flag) { 
			btn_over.setBackgroundResource(R.drawable.btn_green); 
		}else {
			btn_over.setBackgroundResource(R.drawable.half_btn_green);
		} 
		return 0;
	}
	   public void back(){
	    	//


	         Intent intent = new Intent(this,SalesClerkActivity.class);
	         intent.putExtra("result","edt");
	         startActivity(intent);
	    }

	@Override
	protected void onResume() {
		super.onResume();
	}
}
