package com.dachen.dgroupdoctorcompany.activity;

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

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.adapter.HospitalListAdapter;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.SearchRecordsDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.SearchRecords;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.dachen.dgroupdoctorcompany.entity.HospitalList;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.view.ClearEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchResultActivity extends BaseActivity implements OnClickListener,OnHttpListener {
	int totalNum;
	RelativeLayout rl_list;
	/*	ChoicedItemadapter choicedAdapter;
        List<MedicineInfo> listSearch;
        List<MedicineInfo> listSearchWord;  */
	ImageView iv_search;
	ClearEditText et_search;
	int page = 1;
	ViewStub vstub_title;
	//SearchAdapter adapter;
	RelativeLayout rl_history;
	RelativeLayout rl_nofound;
	Button btn_clear;
	ListView listview;
	HospitalListAdapter adapter;
	SearchRecordsDao dao ;
	List<BaseSearch> hospitals;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		dao = new SearchRecordsDao(this);
	}
	/*	*/
	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
		this.findViewById(R.id.tv_title).setVisibility(View.GONE);
		rl_history = (RelativeLayout) this.findViewById(R.id.rl_history);
		vstub_title = (ViewStub) findViewById(R.id.vstub_title);
		this.findViewById(R.id.rl_titlebar).setBackgroundResource(R.color.title_bg_color);
		RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.ll_sub);
		View view = vstub_title.inflate(this, R.layout.layout_search_import, rl);
		view.findViewById(R.id.tv_search).setOnClickListener(this);
		listview = (ListView) findViewById(R.id.listview);
		hospitals = new ArrayList<>();
		adapter = new HospitalListAdapter(this,R.layout.adapter_hospital_list,hospitals,"");
		listview.setAdapter(adapter);
		/*listSearch  = new ArrayList<MedicineInfo>();
		listSearchWord = new ArrayList<MedicineInfo>();  */
		iv_search = (ImageView) view.findViewById(R.id.iv_search);
		iv_search.setOnClickListener(this);
		rl_nofound = (RelativeLayout) this.findViewById(R.id.rl_nofound);
		et_search = (ClearEditText) view.findViewById(R.id.et_search);
		et_search.setHint("输入搜索医院关键词");
		btn_clear = (Button) findViewById(R.id.btn_clear);
		btn_clear.setOnClickListener(this);
		et_search.setOnClickListener(this);
		rl_history.setVisibility(View.VISIBLE);
		findViewById(R.id.iv_search).setVisibility(View.GONE);
		listview.setVisibility(View.VISIBLE);
		dao.queryAll("1");
		/*
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
		listview.setAdapter(adapter);*/
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
				//	MedicineInfo info =	(MedicineInfo) adapter.getItem(arg2);
			/* if (!TextUtils.isEmpty(info.search)) {
				 getSearchResult(info.search);
			} */
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
		timer.schedule(new TimerTask() {

						   public void run() {
							   InputMethodManager inputManager =
									   (InputMethodManager) et_search.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
							   inputManager.showSoftInput(et_search, 0);
						   }

					   },
				998);

	}

	@Override
	public void onSuccess(Result response) {
		// TODO Auto-generated method stub
		closeLoadingDialog();
		if (response instanceof HospitalList){
			HospitalList hospital = (HospitalList)response;
			if(null!=hospital.data&&null!=hospital.data.pageData){
				hospitals.clear();;
				hospitals.addAll((Collection<? extends HospitalList.Data.HospitalDes>) hospital.data.pageData);
				// adapter.notifyDataSetChanged();
				adapter = new HospitalListAdapter(this,R.layout.adapter_hospital_list,hospitals,"");
				listview.setAdapter(adapter);
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
		closeLoadingDialog();

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		if(v.getId() == R.id.iv_search){
			forSearch();
		}else if(v.getId() == R.id.btn_clear){
			/*try {
				ChoiceMedicineLogic.saveObject(this,"","search");
				listSearchWord.clear();
				rl_history.setVisibility(View.GONE);
				adapter.notifyDataSetChanged();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}else  if(v.getId() == R.id.tv_search){
			//ToastUtils.showToast(this,"搜索");
			finish();
		}
	}

	public void forSearch(){
		if (!TextUtils.isEmpty(et_search.getText())) {
			String searchText = et_search.getText().toString().trim();
			SearchRecords records = new SearchRecords();
			records.searchresult = searchText;
			records.userloginid = SharedPreferenceUtil.getString(this,"id","");
			dao.addRole(records,"1");
			getSearchResult(searchText);
			/*try {
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
			} */
		}
	}
	public void getSearchResult(String keyword){
		String searchWord =keyword;
		if(TextUtils.isEmpty(searchWord)){
			return;
		}

		HashMap<String ,String > maps = new HashMap<>();
		maps.put("access_token", UserInfo.getInstance(this).getSesstion());
		maps.put("type", 2+"");
		maps.put("keyword",keyword);
		maps.put("pageIndex","1");
		maps.put("pageSize","100");
		new HttpManager().post(this, Constants.SEARCHHOSPITALS, HospitalList.class,
				maps, this,
				false, 1);
		showLoadingDialog();

	}
	@Override
	public void onBackPressed() {
		finish();
		// super.onBackPressed();
		// this.mApplication.getActivityManager().finishActivity(this.getClass());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		super.onActivityResult(requestCode, resultCode, data);
	}

}
