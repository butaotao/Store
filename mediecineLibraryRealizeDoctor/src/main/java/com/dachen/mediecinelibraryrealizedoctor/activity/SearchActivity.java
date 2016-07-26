package com.dachen.mediecinelibraryrealizedoctor.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.view.ClearEditText;
import com.dachen.mediecinelibraryrealizedoctor.R;
import com.dachen.mediecinelibraryrealizedoctor.activity.BaseSearchActivity.CleanAllRefreshinferface;
import com.dachen.mediecinelibraryrealizedoctor.adapter.ChoicedItemadapter;
import com.dachen.mediecinelibraryrealizedoctor.adapter.SearchAdapter;
import com.dachen.mediecinelibraryrealizedoctor.entity.DrugDtaList;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineEntity;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo;
import com.dachen.mediecinelibraryrealizedoctor.entity.PreparedMedie;
import com.dachen.mediecinelibraryrealizedoctor.entity.SearchDrugDtaList;
import com.dachen.mediecinelibraryrealizedoctor.entity.SearchMedicineEntity;
import com.dachen.mediecinelibraryrealizedoctor.entity.SearchMedicineInfo;
import com.dachen.mediecinelibraryrealizedoctor.utils.Json.DrugChange;

public class SearchActivity extends BaseSearchActivity implements OnClickListener,OnHttpListener,CleanAllRefreshinferface {
	int totalNum;
	RelativeLayout rl_list;
	ChoicedItemadapter choicedAdapter;
	List<MedicineInfo> listSearch;
	List<MedicineInfo> listSearchWord;
		ImageView iv_search;
		ClearEditText et_search;
		int page = 1;
		ViewStub vstub_title;
		ListView listview;
		SearchAdapter adapter;
		RelativeLayout rl_history;
		RelativeLayout rl_nofound;
		Button btn_clear;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

	}

	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
		initdata();
		getBuyCarData(true,false);
		getInterface();
		this.findViewById(R.id.tv_title).setVisibility(View.GONE);
		rl_history = (RelativeLayout) this.findViewById(R.id.rl_history);
		vstub_title = (ViewStub) findViewById(R.id.vstub_title);
		RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.ll_sub);
		View view = vstub_title.inflate(this, R.layout.layout_search_import, rl);
		listview = (ListView) findViewById(R.id.listview);
		listSearch  = new ArrayList<MedicineInfo>();
		listSearchWord = new ArrayList<MedicineInfo>();
		this.findViewById(R.id.rl_back).setOnClickListener(this);
		iv_search = (ImageView) view.findViewById(R.id.iv_search);
		iv_search.setOnClickListener(this);
	 	rl_nofound = (RelativeLayout) this.findViewById(R.id.rl_nofound);
		et_search = (ClearEditText) view.findViewById(R.id.et_search);
		btn_clear = (Button) findViewById(R.id.btn_clear);
		btn_clear.setOnClickListener(this);
		et_search.setOnClickListener(this);
		rl_history.setVisibility(View.VISIBLE);
		listview.setVisibility(View.VISIBLE);
		try {
			listSearchWord = ChoiceMedicineLogic.deSerialization(ChoiceMedicineLogic.getObject(this, "search"),false);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null==listSearchWord) {
			rl_history.setVisibility(View.GONE);
			listSearchWord = new ArrayList<MedicineInfo>();
		}else {
			rl_history.setVisibility(View.VISIBLE);
		}
		adapter = new SearchAdapter(this,listSearchWord);
		listview.setAdapter(adapter);
		et_search.setOnEditorActionListener(
				 new TextView.OnEditorActionListener() {
				           public boolean onEditorAction(TextView v, int actionId,
				                             KeyEvent event){
				                        if (actionId == EditorInfo.IME_ACTION_SEARCH)
				                         {
				                             // 在这里编写自己想要实现的功能
				                        	forSearch();
				                         }
				                         return false;
				           }
				 });
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				MedicineInfo info =	(MedicineInfo) adapter.getItem(arg2);
			 if (!TextUtils.isEmpty(info.search)) {
				 getSearchResult(info.search);
			}
			}
		});
		 page = 1;
	}
  @Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	et_search.requestFocus();

	et_search.setFocusable(true);
	 Timer timer = new Timer();
	     timer.schedule(new TimerTask()
	      {

	           public void run()
	         {
	              InputMethodManager inputManager =
	                   (InputMethodManager)et_search.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
	               inputManager.showSoftInput(et_search, 0);
	           }
	       },
	           998);
}

	@Override
	public void onSuccess(Result response) {
		// TODO Auto-generated method stub
		if (response.resultCode==1){
			if(response instanceof SearchDrugDtaList){
				SearchDrugDtaList entitys = (SearchDrugDtaList)response;
				ArrayList<MedicineInfo> infos = null;
				if (entitys.data!=null&&entitys.data.pageData!=null&&entitys.data.pageData.size()>0){
					infos = DrugChange.getMedicineList(entitys.data.pageData);
				}

				MedicineEntity entity = new MedicineEntity();
				entity.info_list = new ArrayList<MedicineInfo>();
				if (infos!=null&&infos.size()>0){
					entity.info_list = infos;
				}


				closeLoadingDialog();
				//LogUtils.burtLog("==="+entity.info_list.size());
				if (null!=infos&&infos.size()>0) {
					rl_history.setVisibility(View.VISIBLE);
					listview.setVisibility(View.VISIBLE);
					rl_nofound.setVisibility(View.GONE);
					startSerachActivity(entity);
				}else {
					rl_history.setVisibility(View.GONE);
					listview.setVisibility(View.GONE);
					rl_nofound.setVisibility(View.VISIBLE);
				}



			}
		}else {
			ToastUtils.showResultToast(this, response);
		}

	}
	@Override
	public void onSuccess(ArrayList response) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onFailure(Exception e, String errorMsg, int s) {
		// TODO Auto-generated method stub
		closeLoadingDialog();

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		if(v.getId() == R.id.rl_back){
			/* Intent intent = new Intent(this,ChoiceMedicineActivity.class);
			  startActivity(intent);*/
			Intent intent = new Intent();
			setResult(3, intent);
			finish();
		}else if(v.getId() == R.id.btn_tra){

		}else if(v.getId() == R.id.iv_search){
			forSearch();
		}else if(v.getId() == R.id.btn_clear){
			try {
				ChoiceMedicineLogic.saveObject(this,"","search");
				listSearchWord.clear();
				rl_history.setVisibility(View.GONE);
				adapter.notifyDataSetChanged();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void startSerachActivity(MedicineEntity info){
		Intent intent = new Intent(SearchActivity.this,SearchResultActivity.class);

		Bundle bundle= new Bundle();
		bundle.putSerializable("search", info);
		intent.putExtra("search", bundle);
		SearchActivity.this.startActivityForResult(intent, 2);
	}
	public void forSearch(){
		if (!TextUtils.isEmpty(et_search.getText())) {
			String searchText = et_search.getText().toString().trim();
			try {
				listSearchWord = ChoiceMedicineLogic.deSerialization(ChoiceMedicineLogic.getObject(this, "search"),false);
				if (null == listSearchWord) {
					listSearchWord = new ArrayList<MedicineInfo>();
				}
				boolean isadd = false;
				for (int i = 0; i < listSearchWord.size(); i++) {
					if (null!=listSearchWord.get(i).search&&listSearchWord.get(i).search.equals(""+searchText)) {
						isadd = true;
						break;
					}
				}
				if (!isadd) {
					MedicineInfo info = new MedicineInfo();
					info.search = ""+searchText;
					listSearchWord.add(info);
				}

				if (null!=listSearchWord&&listSearchWord.size()>0) {
					ChoiceMedicineLogic.saveObject(this,ChoiceMedicineLogic.serialize(listSearchWord),"search");
				}
				getSearchResult(searchText);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void getSearchResult(String keyword){
		String searchWord =keyword;
				if(TextUtils.isEmpty(searchWord)){
					return;
				}
			/*	HashMap<String, String> interfaces = new HashMap<String, String>();
				interfaces.put("interface1", "invoke");
				interfaces.put("interface2", "c_Goods.select?");*/
				/*	new HttpManager().post(this,interfaces, SearchMedicineEntity.class,
							Params.getSearchInfo (searchWord),
							this,false, 2); */
	/*	access_tokeString
		tokenpageIndex Integer 页码 从0开始
		pageSize Integer
		每页大小 keyword
		 */

 	HashMap<String, String> interfaces = new HashMap<String, String>();
				interfaces.put("access_token", UserInfo.getInstance(this).getSesstion());
				interfaces.put("pageIndex", "0");
				interfaces.put("pageSize", "100");
				interfaces.put("keyword", searchWord);
		if (UserInfo.getInstance(this).getUserType().equals("3")){
			interfaces.put("userId", UserInfo.getInstance(this).getId());
			if (!TextUtils.isEmpty(ChoiceMedicineActivity.groupID)){
				interfaces.put("groupId", ChoiceMedicineActivity.groupID);
			}
		}
//goods/searchGoodsList
		new HttpManager().post(SearchActivity.this,
				"org/goods/searchGoodsList", SearchDrugDtaList.class,
				interfaces,
				SearchActivity.this, false, 1);
					 // new HttpManager().postJson(this, interfaces, SearchMedicineEntity.class, Params.getSearchInfo (searchWord), this);
					showLoadingDialog();
		}
	  @Override
	    public void onBackPressed() {
		  Intent intent = new Intent(this,ChoiceMedicineActivity.class);
		  startActivity(intent);
		  finish();
	       // super.onBackPressed();
	        // this.mApplication.getActivityManager().finishActivity(this.getClass());
	    }
	  @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		  initdata();
		  getBuyCarData(true,false);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public CleanAllRefreshinferface getInterface() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public void refreshClear(boolean clear) {
		// TODO Auto-generated method stub

	}
}
