package com.dachen.medicine.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.R;
import com.dachen.medicine.adapter.SalesclerkAdapter;
import com.dachen.medicine.app.MedicineApplication;
import com.dachen.medicine.common.utils.DrugList;
import com.dachen.medicine.common.utils.JsonUtils.CdrugRecipeitemGiveChange;
import com.dachen.medicine.common.utils.JsonUtils.DrugListTranslate;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.CdrugGive;
import com.dachen.medicine.entity.CdrugRecipeitem;
import com.dachen.medicine.entity.CdrugRecipeitem.Data;
import com.dachen.medicine.entity.CdrugRecipeitem.DataPatient;
import com.dachen.medicine.entity.CdrugRecipeitem.Unit;
import com.dachen.medicine.entity.CdrugRecipeitemGive;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.entity.SubmitInfo;
import com.dachen.medicine.logic.CompareData;
import com.dachen.medicine.logic.ScaningData;
import com.dachen.medicine.net.CustomImagerLoader;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.view.CircleImageView;
import com.dachen.medicine.view.CustomDialog;
import com.dachen.medicine.view.DialogMedieExplain;
import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressWarnings("rawtypes")
public class SalesClerkActivity extends BaseActivity implements OnHttpListener,OnClickListener {
	public ListView lv_medicine_records; 
	SalesclerkAdapter recordAdapter2;
	List<CdrugRecipeitem> list;
	TextView tv_back;
	ViewStub vstub_title;
	RelativeLayout rl_title;
	RelativeLayout ll_sub;
	ImageView edit_title;
	ImageView scan_title;
	ImageView redcircleedit;
	ImageView redcircleescan;
	Button btn_goonscane;
	TextView tv_title;
	TextView tv_name;
	String tiaoxingma;
	RelativeLayout rl_back;
	LinearLayout ll_btn;
	List<CdrugRecipeitem>  listmedies;

	List<CdrugRecipeitem> listdrug;
	List<CdrugRecipeitem> listScaning; 
	List<CdrugRecipeitem> listScaningCompareScan; 
	CircleImageView head_icon;
//	ListView refreshableView ;
	int isnext = -1;boolean flag = false;
	CustomDialog dialog;
	RelativeLayout rl_nosell; 
	CdrugRecipeitem item;
	TextView tv_listtitle;
	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_salesclerk);
		lv_medicine_records = (ListView) findViewById(R.id.lv_medicine_records); 
		tiaoxingma = getIntent().getStringExtra("result");
		rl_nosell = (RelativeLayout) findViewById(R.id.rl_nosell); 
		isnext = -1;  
		head_icon = (CircleImageView) findViewById(R.id.head_icon);
		ll_btn = (LinearLayout) findViewById(R.id.ll_btn);
		rl_nosell.setVisibility(View.GONE);
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);

		tv_back = (TextView) findViewById(R.id.tv_back);
		tv_back.setVisibility(View.VISIBLE); 
		list = new ArrayList<CdrugRecipeitem>();
		listScaning  = new ArrayList<CdrugRecipeitem>();
		listScaningCompareScan = new ArrayList<CdrugRecipeitem>();
		View viewhead = View.inflate(this, R.layout.layout_title_header, null);
		redcircleedit = (ImageView)viewhead.findViewById(R.id.redcircleedit);
		redcircleescan = (ImageView) findViewById(R.id.redcircleescan);
		recordAdapter2 = new SalesclerkAdapter(this,
				R.layout.adapter_salesclerk, list,false,redcircleedit,redcircleescan);
		item = ScaningData.getGaveInfo();
		
		

		//refreshableView = lv_medicine_records.getRefreshableView();
		lv_medicine_records.addHeaderView(viewhead);
		//refreshableView.setBackgroundResource(R.drawable.bulk_green_rightangle);
		rl_title = (RelativeLayout) findViewById(R.id.rl_title);
		tv_listtitle = (TextView) viewhead.findViewById(R.id.tv_listtitle);
		ll_sub = (RelativeLayout) findViewById(R.id.ll_sub); 
		edit_title = (ImageView) viewhead.findViewById(R.id.edit_title);
		scan_title = (ImageView) this.findViewById(R.id.scan_title);
		edit_title.setOnClickListener(this);
		scan_title.setOnClickListener(this); 
		lv_medicine_records.setAdapter(recordAdapter2);
		lv_medicine_records.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				// TODO Auto-generated method stub
				if ((arg2 - 1 >= 0) && null != recordAdapter2.getItem(arg2 - 1)) {
					//ToastUtils.showToast(recordAdapter2.getItem(arg2+1)+"");
					DialogMedieExplain dialog = new DialogMedieExplain(SalesClerkActivity.this, recordAdapter2.getItem(arg2 - 1));
					dialog.show();
				}

			}
		});
		btn_goonscane = (Button) findViewById(R.id.btn_goonscane);
		btn_goonscane.setOnClickListener(this);
		tv_title = (TextView) this.findViewById(R.id.tv_title);
		tv_title.setText(getString(R.string.scan_sell));
		tv_listtitle.setText("药品清单");
		tv_name = (TextView) this.findViewById(R.id.tv_name);
		//User user = UserInfoInDB.getUser(SharedPreferenceUtil.getString("id", ""));
		listmedies = new ArrayList<CdrugRecipeitem>();
		
		Intent intent = getIntent();
		if (!TextUtils.isEmpty(tiaoxingma)&&tiaoxingma.length()>=4) {
			isnext =1;
			edit_title.setVisibility(View.GONE);
		
	
		initlist();
		SharedPreferenceUtil.putString("tiaoxingma", tiaoxingma.substring(3, tiaoxingma.length()) + "");
		btn_goonscane.setText("扫药监码");
		//getPatientInfo(tiaoxingma);
		}else if(!TextUtils.isEmpty(tiaoxingma)&&tiaoxingma.equals("red")){
			//扫描后
			getPreviousActivityInfo();
		}else if(!TextUtils.isEmpty(tiaoxingma)&&tiaoxingma.equals("edt")){
			getPreviousActivityInfo();
		}
		if (!SharedPreferenceUtil.getString("patient_name", "").equals("")) {
			tv_name.setText(SharedPreferenceUtil.getString("patient_name", ""));
		}
	}

	public void initlist(){
	 
		getList();
	}
	public void getPreviousActivityInfo(){
		edit_title.setVisibility(View.VISIBLE);

		//购药清单
		listdrug  = ScaningData.getlistdrug();
		listmedies =  ScaningData.getListmedies();
		List<CdrugRecipeitem>  listmedies2 = new ArrayList<CdrugRecipeitem>();
		listmedies2.addAll(listmedies);
		for (int i=0;i<listmedies.size();i++) { 
			if (listmedies.get(i).notExitInlist) {
				if((null!=listmedies.get(i).lists&&listmedies.get(i).lists.size()<1)
						||null==listmedies.get(i).lists){
					listmedies2.remove(i);
				}
			}
		}
		list.clear();
		list.addAll(listmedies2);
		recordAdapter2.setshow(true);
		recordAdapter2.notifyDataSetChanged();
		int size = listdrug.size(); 
		isnext = 3;
		if (size!=-1) {
			 if (listmedies.size()!=size) {
				 isnext = 2;
				}else{
					for (int i = 0; i < listmedies.size(); i++) {
						if (listmedies.get(i).isAdd!=2) {
							isnext = 2;
							break;
						}
						
					}
				}
		}
		if (isnext==2&&ScaningData.sCanFlag!=5) {
			btn_goonscane.setText("下一步");
		}else {
			btn_goonscane.setText("完成");
		}
	}
	@Override
	public void onSuccess(Result response) {
		closeLoadingDialog();
		// TODO Auto-generated method stub
		//lv_medicine_records.onRefreshComplete(); 
		rl_nosell.setVisibility(View.GONE);
		ll_btn.setVisibility(View.VISIBLE);
		if (null != response) {
			if (response.resultCode ==1){
				if (response instanceof DrugList) {
					tv_title.setText(R.string.scan_sell);
					tv_listtitle.setText("购药清单");

					DrugList record = (DrugList) response;
					ArrayList<CdrugRecipeitem> lists = DrugListTranslate.getRecipeitems(record.data.receiptList);
					if(null==lists){
						Intent intent = new Intent(this,NotFoundListActivity.class);
						startActivity(intent);
						super.onBackPressed();
						return;
					};

					getAvatarImage(record.data.patientInfo.headPicFileName);
					com.dachen.medicine.common.utils.PatientInfo info = record.data.patientInfo;
					list.clear();
					list.addAll((List<CdrugRecipeitem>) lists);
					recordAdapter2.setObjects(lists);
					recordAdapter2.notifyDataSetChanged();
					if (null!=record&&!TextUtils.isEmpty(info.name)){
						tv_name.setText(info.name);
					}
					if (null!=info&&null!=info.name){
						//1表示男，2女，其他不确定
						if (info.sex == 1){
							tv_name.setText(info.name.substring(0,1)+"先生");
						}else if(info.sex == 2){
							tv_name.setText(info.name.substring(0,1)+"女士");
						}else {
							tv_name.setText(info.name.substring(0,1)+"先生/女士");
						}
					}
					if (null!=info.name) {
						SharedPreferenceUtil.putString("patient_name",info.name);
					}
					if (0!=info.id) {
						SharedPreferenceUtil.putString("patientid", info.id+"");
					}
					if (null!= list&&list.size()>0&&null!=record.data.receiptList.get(0)&&null!=record.data.receiptList.get(0).recipeId) {
						SharedPreferenceUtil.putString("receivedId", record.data.receiptList.get(0).recipeId);
					}

				}else if(response instanceof SubmitInfo){

					try {
						SubmitInfo patientinfo = (SubmitInfo)response;
						if(null == patientinfo.is_success){
							MedicineApplication.flag = true;
							ToastUtils.showToast(getResources().getString(R.string.toast_submit_fail));
							return;
						}
						if (null!=patientinfo.is_success&&patientinfo.is_success.contains("true")||patientinfo.is_success.equals("1")) {
							ToastUtils.showToast(getResources().getString(R.string.toast_submit_success));
							ScaningData.sCanFlag=-1;
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

				}else if(response instanceof CdrugGive){
					tv_title.setText("扫码换药");
					tv_listtitle.setText("药品清单");
					CdrugGive datas = (CdrugGive)response;
					CdrugRecipeitemGive.CdrugRecipeitemGiveInfo give = CdrugRecipeitemGiveChange.getData(datas);
					item.requires_quantity = 0;
					item.requires_quantity = ScaningData.getGiveRequireNum(give.zsmdwypxhjfs ,give.num_syjf);
					item.givePresent = true;
					item.bought_quantity = 0;
					item.is_own = give.is_own;
					item.general_name = give.goods$general_name;
					item.trade_name = give.goods$trade_name;
					item.manufacturer = give.goods$manufacturer;
					item.pack_specification = give.goods$pack_specification;
					item.specification = give.goods$specification;
					Data data = item.new Data();
					data.zsmdwypxhjfs = give.zsmdwypxhjfs;
					data.zyzdsxjfs = give.zyzdsxjfs;
					item.data = data;
					DataPatient data1 = item.new DataPatient();
					data1.num_syjf = give.num_syjf;
					Unit unit =  item.new Unit();
					if (null!=give.goods$unit) {
						unit.name = give.goods$unit.name;
						unit.title = give.goods$unit.name;
					}
					item.unit = unit;
					if (null!=give.goods) {
						item.id = give.goods.id;
					}
					list.clear();
					list.add(item);
					ScaningData.setlistdrug(list);
					recordAdapter2.setObjects(list);
					//recordAdapter2.notifyDataSetChanged();
					tv_name.setText(give.patient$user_name);
					ScaningData.sCanFlag = 4;
					ScaningData.setItem(item);
					if ( null!=give.sex&&!TextUtils.isEmpty(give.patient$user_name)){
						//1表示男，2女，其他数字表示不确定
						if (give.sex.equals("1")){
							tv_name.setText(give.patient$user_name.substring(0,1)+"先生");
						}else if(give.sex.equals("2")){
							tv_name.setText(give.patient$user_name.substring(0,1)+"女士");
						}else {
							tv_name.setText(give.patient$user_name.substring(0,1)+"先生/女士");
						}
					}


					if (null!=give.patient$user_name) {
						SharedPreferenceUtil.putString("patient_name", give.patient$user_name);
					}
					if (null!=give.patient) {
						SharedPreferenceUtil.putString("patientid", give.patient);
					}

				}

			}else {
				ToastUtils.showToast(response);
			}
		}

	}
	@Override
	public void onSuccess(ArrayList response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailure(Exception e, String errorMsg,int s) { 
		// TODO Auto-generated method stub
		//list = new ArrayList<CdrugRecipeitem>();
		if (s==-1&&list.size()==0) { 
			rl_nosell.setVisibility(View.VISIBLE);
			ll_btn.setVisibility(View.GONE);
			lv_medicine_records.setAdapter(recordAdapter2);
			ToastUtils.showToast("网络不可用，请稍后重试！");
		}else if(s==-1){
			ToastUtils.showToast("网络不可用，请稍后重试！");
			
		}else {
			 ToastUtils.showToast("系统繁忙"); 
		}
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (SharedPreferenceUtil.getBoolean("scan_sale",false)) {
			redcircleescan.setVisibility(View.GONE);
		}
		if (SharedPreferenceUtil.getBoolean("edit_sale",false)){
			redcircleedit.setVisibility(View.GONE);
		}

	}
	
	@Override
	public void onClick(View v) {
		final Intent intent ;
		
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.edit_title:
			startActivity(EditListActivity.class, false);
			SharedPreferenceUtil.putBoolean("edit_sale",true);
			super.onBackPressed();
			break;
		case R.id.scan_title:
			SharedPreferenceUtil.putBoolean("scan_sale",true);
			if(isnext == 1){
				//扫描条形码
				intent = new Intent(this, MipcaActivityCaptures.class);
				intent.putExtra("erweima_code",tiaoxingma);
				intent.putExtra("code","tiaoxingma");
				startActivity(intent); 
				
				}else{
					startActivity(MipcaActivityCaptures.class,true);
				}
			 
			super.onBackPressed(); 
			
			break;
		case R.id.btn_goonscane: 
			if (ScaningData.sCanFlag ==5) {
				saveInfo();
			}else {
				if(isnext == 2){
					//下一步，数量不一致 
						startActivity(MedieNumInconformityActivity.class,false); 
						SalesClerkActivity.super.onBackPressed();
				} else if(isnext == 1){
					//扫描条形码
					intent = new Intent(this, MipcaActivityCaptures.class);
					intent.putExtra("erweima_code", tiaoxingma);
					intent.putExtra("code","tiaoxingma");

					startActivity(intent); 
					super.onBackPressed();
				}else if(isnext == 3){
					//提交
					saveInfo();
					//startActivity();
				} 
			}
			
			break;
		case R.id.rl_back:
		
			if(isnext==1){
			   intent = new Intent(this,MipcaActivityCaptures.class);
				intent.putExtra("code", "erweima");
				startActivity(intent);
				super.onBackPressed();
			}else { 
				final CustomDialog dialog = new CustomDialog(this);
		 
				dialog.showDialog("", getResources().getString(R.string.dialog_content_sumbitlist), new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dialog.dimissDialog();
						flag = true; 
						if(isnext == 2){
						 startActivity(MedieNumInconformityActivity.class,false);  
						SalesClerkActivity.super.onBackPressed();
						}else if(isnext == 3){
							//提交
							saveInfo();
						} 
					}
				}, new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dialog.dimissDialog();
						flag = false;
						SalesClerkActivity.super.onBackPressed();
					}
				});
				flag = false;
			}
			
			break;
		default:
			break;
		}
	}  

	public void startActivity(Class<?> cls,boolean flag) {
	        Intent intent = new Intent(this, cls);
	        if (flag) {
	        	intent.putExtra("code", "tiaoxingma_SalesClerkActivity");
			}

		ScaningData.setListmedies(listmedies);

			if (null == listdrug) {
				ToastUtils.showToast("药单为空！");
				return;
			}


		startActivity(intent);
	}
	//(String salesman,String patient,String recipe_id,String c_buydrugitems,String c_drug_codes) 
	public boolean getGaveCompareScan(){
		listScaningCompareScan.clear() ;
		for (int i = 0; i < listmedies.size(); i++) {
			if (null != listmedies.get(i).lists) {
				for (int j = 0; j < listdrug.size(); j++) {
					if (listmedies.get(i).equals(listdrug.get(j))) {
						if (CompareData.isShow(listdrug.get(j))) {
							if (listdrug.get(j).foundCode) {
								listScaningCompareScan.add(listdrug.get(j));
							}
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


			ScaningData.setListmedies(listmedies);
			ScaningData.setListScaningCompareScan(listScaningCompareScan);
			intent.putExtra("intent", "salesclearkactivity");
			startActivity(intent);
			SalesClerkActivity.super.onBackPressed();
		}else {
			if(ScaningData.saveSellInfoToServer(this,false,true)){
				super.onBackPressed();
				return;
			}

		 
	 }
	}
	public void getList(){
		//showLoadingDialog();
		//得到购药清单

		if(tiaoxingma.startsWith("dch")){
			String  s ="goods/salesLog/getPatientToExchangeGoodsMsg";
			HashMap<String,String> maps = new HashMap<>();
			maps.put("access_token",SharedPreferenceUtil.getString("session",""));
			maps.put("goodsId",tiaoxingma.substring(3, tiaoxingma.indexOf("and")));
			maps.put("patientId",tiaoxingma.substring(tiaoxingma.indexOf("and")+3));
		//专用赠药二维码请求  
			new HttpManager().get(this,
					s,
					CdrugGive.class,
					maps,
					this, false, 3);
		}else {
			tiaoxingma = tiaoxingma.substring(3, tiaoxingma.length());
			String s = "goods/salesLog/getRecipeitemsListById";
			HashMap<String,String> maps = new HashMap<>();
			maps.put("access_token",SharedPreferenceUtil.getString("session",""));
			maps.put("recipeId",tiaoxingma);
			//普通二维码请求
			new HttpManager().get(this,
					s,
					DrugList.class,
					maps,
					this, false, 3);
		}
	}
	
	private void getAvatarImage(String avatarUri) {
		// 获取头像 

		//TODO   			  
			if(!TextUtils.isEmpty(avatarUri))
			{
				CustomImagerLoader.getInstance().loadImage(head_icon,avatarUri);
			}	
			else head_icon.setImageResource(R.drawable.head_icon);
		//}
	}
	  @Override
	    public void onBackPressed() {
		  Intent intent;
		  if(isnext==1){
			   intent = new Intent(this,MipcaActivityCaptures.class);
				intent.putExtra("code", "erweima");
				startActivity(intent);
				super.onBackPressed();
			}else { 
				final CustomDialog dialog = new CustomDialog(this);
		 
				dialog.showDialog("", getResources().getString(R.string.dialog_content_sumbitlist), new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dialog.dimissDialog();
						flag = true; 
						if(isnext == 2){
						startActivity(MedieNumInconformityActivity.class,false);
						SalesClerkActivity.super.onBackPressed();
						}else if(isnext == 3){
							//提交
							saveInfo();
							//startActivity();
						} 
					}
				}, new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dialog.dimissDialog();
						flag = false;
						SalesClerkActivity.super.onBackPressed();
					}
				}); 
				flag = false;
			}
	}
}
