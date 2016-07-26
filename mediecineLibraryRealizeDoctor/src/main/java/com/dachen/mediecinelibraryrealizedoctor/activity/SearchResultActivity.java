package com.dachen.mediecinelibraryrealizedoctor.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.mediecinelibraryrealizedoctor.R;
import com.dachen.mediecinelibraryrealizedoctor.activity.BaseSearchActivity.CleanAllRefreshinferface;
import com.dachen.mediecinelibraryrealizedoctor.adapter.ChoiceMedicineChildrenadapter;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineEntity;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineList;
import com.dachen.mediecinelibraryrealizedoctor.utils.DataUtils;

public class SearchResultActivity extends BaseSearchActivity  implements OnClickListener, OnHttpListener,CleanAllRefreshinferface{
	int page = 1;




	ArrayList<MedicineInfo> lists_childrens;
	ArrayList<MedicineInfo> lists_childrensOrigindata;
	RelativeLayout rl_back;
	ArrayList<MedicineInfo> list2;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 1:
					if (null==childrenAdapter ){
						childrenAdapter = new ChoiceMedicineChildrenadapter(SearchResultActivity.this,
								R.layout.item_listview_choice_medicine_children,
								lists_childrens, lists_parents, choice);
						listview_medicine_children .setAdapter(childrenAdapter);
					}else {
						childrenAdapter.notifyDataSetChanged();
					}

					closeLoadingDialog();
					break;
				default:
					break;
			}

		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchresult);

	}
	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
		initdata();
		getInterface();
		TextView title = (TextView) this.findViewById(R.id.tv_title);
		title.setText("选择药品");
		getBuyCarData(true,false);
		Bundle bundle = getIntent().getBundleExtra("search");
		MedicineEntity entity = (MedicineEntity) bundle.getSerializable("search");
		lists_childrens = new ArrayList<MedicineInfo>();
		lists_childrensOrigindata =new ArrayList<MedicineInfo>();
		lists_childrens = entity.info_list;
		lists_childrensOrigindata = entity.info_list;
		list2 = new ArrayList<MedicineInfo>();
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		refresh();


		listview_medicine_children = (ListView) findViewById(R.id.listview_medicine_children);
	//	getBuyCarData(false,true);
		handler.sendEmptyMessage(1);
	}
	public void refresh(){
		ArrayList<MedicineInfo>  lists = DataUtils.getList(false, this, list2, lists_childrens);
		list2.clear();
		lists.addAll(lists);
	}
	@Override
	public void onSuccess(Result response) {
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
		if (v.getId() == R.id.rl_back) {
			Intent intent = new Intent(this,SearchActivity.class);
			startActivity(intent);
			finish();

		}
	}
	@Override
    public void onBackPressed() {
		Intent intent = new Intent(this,SearchActivity.class);
		SearchResultActivity.this.setResult(3, intent);
		finish();
	    }
	@Override
	public void refreshClear(boolean clear) {
		// TODO Auto-generated method stub
		if (clear){
			for (int i=0;i<lists_childrensOrigindata.size();i++){
				MedicineInfo info = lists_childrensOrigindata.get(i);
				info.num = 0;
				lists_childrensOrigindata.set(i,info);
			}

			list2.clear();
			list2.addAll(lists_childrensOrigindata);
			childrenAdapter.notifyDataSetChanged();
		}else {
			refresh();
			handler.sendEmptyMessage(1);
		}

	}
	@Override
	public CleanAllRefreshinferface getInterface() {
		// TODO Auto-generated method stub
		return this;
	}
}
