package com.dachen.mediecinelibraryrealizedoctor.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.IllEntity;
import com.dachen.medicine.common.utils.ImageUtil;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.MActivityManager;
import com.dachen.medicine.common.utils.MedicineApplication;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.mediecinelibraryrealizedoctor.R;
import com.dachen.mediecinelibraryrealizedoctor.adapter.ChoiceItemNumInterface;
import com.dachen.mediecinelibraryrealizedoctor.adapter.ChoiceMedicineChildrenadapter;
import com.dachen.mediecinelibraryrealizedoctor.adapter.ChoicedItemadapter;
import com.dachen.mediecinelibraryrealizedoctor.entity.DataChanges;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineList;

public abstract class BaseSearchActivity extends BaseActivity implements OnClickListener, OnHttpListener{
	int totalNum;
	RelativeLayout rl_list;
	ChoicedItemadapter choicedAdapter;
	List<MedicineInfo> listSelected;
	List<MedicineInfo> listSelected2;
	ListView horizontal_list;
	RelativeLayout rl_ivnum;
	Button iv_num;
	Button iv_num_listtitle;
	TextView tv_alertnullnum;
	ImageView ivbuycar_listtitle ;
	ImageView ivbuycar;
	TextView btn_clean;
	List<MedicineInfo> listSearch;
	List<IllEntity> lists_parents;
	ListView listview_medicine_children;
	List<MedicineInfo> selected = null;
	ChoiceMedicineChildrenadapter childrenAdapter;
	Button btn_confirm;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 1:
					//if(null==choicedAdapter){
						choicedAdapter = new ChoicedItemadapter(
								BaseSearchActivity.this,
								R.layout.item_hri_listview_been_choiced_medicine,
								listSelected2, choice);
						horizontal_list.setAdapter(choicedAdapter);
					/* }else {

						choicedAdapter.notifyDataSetChanged();
					}*/
					//setListViewHeight();
					break;
				case 0:
					getInterface().refreshClear(false);

					break;
				default:
					break;
			}

		};
	};

	private void setListViewHeight() {
		ListAdapter listAdapter = horizontal_list.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, horizontal_list);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
//
		ViewGroup.LayoutParams params = horizontal_list.getLayoutParams();
		params.height = totalHeight + (horizontal_list.getDividerHeight() * (listAdapter.getCount() - 1));
		((ViewGroup.MarginLayoutParams)params).setMargins(0, 0, 0, 0);
		horizontal_list.setLayoutParams(params);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		listSelected2 = new ArrayList<MedicineInfo>();
	}
	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
	}


	@Override
	public void onClick(View v) {

		// TODO Auto-generated method stub
		if (v.getId()==R.id.btn_confirm) {
			commitData();
		}
	}
	public void commitData(){
		List<MedicineInfo> info  = MedicineList.getMedicineList().getShopingcard(BaseSearchActivity.this);
		final List<MedicineInfo> infos = new ArrayList<>();
		for (int i=0;i<info.size();i++){
			try {
				infos.add((MedicineInfo) info.get(i).deepCopy());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		MedicineList.getMedicineList().setSaveCollectiongWhenFinish(BaseSearchActivity.this,infos);

		try {
			if (null==info||info.size()==0) {
				MActivityManager.getInstance().finishAllActivityMedie();
				ClenAll(true); DataChanges.info = null;
				return;
			}
			if (null == DataChanges.info) {
				MActivityManager.getInstance().finishAllActivityMedie();
				Intent intent = new Intent(this,CreationMedieListActivity.class);
				startActivity(intent);
				return;
			}else if (null != DataChanges.info) {
				HashMap<String, String> maps ;
				maps = MedicineApplication.getMapConfig();
				String ip = maps.get("ip");
				String session = maps.get("session");
				for (int i = 0; i < info.size(); i++) {
					MedicineInfo medie;
					try {
						medie = (MedicineInfo) info.get(i).deepCopy();
						if (null!=medie.goods$image_url) {
							medie.goods$image_url = ImageUtil.getUrl(medie.goods$image_url,ip,session,1);
							info.set(i, medie);
						}else if (null!=medie.goods$image) {
							medie.goods$image_url = ImageUtil.getUrl(medie.goods$image,ip,session,1);
							info.set(i, medie);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				DataChanges.info.DateChangeNotify(info);
				MActivityManager.getInstance().finishAllActivityMedie();
				ClenAll(true); DataChanges.info = null;
				return;
			}
			MActivityManager.getInstance().finishAllActivityMedie();
			ClenAll(true); DataChanges.info = null;


		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	public abstract CleanAllRefreshinferface getInterface();
	public void ClenAll(boolean clean) {
		getInterface().refreshClear(true);
		totalNum = 0;
		if (clean) {
			MedicineList.getMedicineList().setShoppingcard(this,null);
		}

		rl_list.setVisibility(View.GONE);
		rl_ivnum.setVisibility(View.VISIBLE);
		listSelected.clear();
		tv_alertnullnum.setText("尚未选择药品");

		showtotalNum( totalNum);
		ivbuycar_listtitle.setBackgroundResource(R.drawable.buycar_nobuy);
		ivbuycar.setBackgroundResource(R.drawable.buycar_nobuy);
	}
	public interface  CleanAllRefreshinferface{
		public void refreshClear(boolean isclean);
	};
	public void Confirm() {
		if (rl_list.getVisibility() == View.VISIBLE) {
			rl_list.setVisibility(View.GONE);
			rl_ivnum.setVisibility(View.VISIBLE);
			handler.sendEmptyMessage(0);
		}else {
			rl_list.setVisibility(View.VISIBLE);
			rl_ivnum.setVisibility(View.GONE);
			showtotalNum( totalNum);
			listSelected2.clear();
			listSelected2.addAll(listSelected);
			handler.sendEmptyMessage(1);
		}
	}
	public void showtotalNum(int total){
		if (total==0) {
			iv_num.setText(""+totalNum+"");
			iv_num_listtitle.setText(""+totalNum);
			iv_num.setVisibility(View.INVISIBLE);
			iv_num_listtitle.setVisibility(View.INVISIBLE);
			btn_confirm.setBackgroundResource(R.drawable.btn_green_all3);
			btn_confirm.setClickable(false);

		}else {
			btn_confirm.setClickable(true);
			btn_confirm.setBackgroundResource(R.drawable.btn_green_all2);
			iv_num.setText("" + totalNum + "");
			iv_num_listtitle.setText("" + totalNum);
			iv_num.setVisibility(View.VISIBLE);
			iv_num_listtitle.setVisibility(View.VISIBLE);

		}
		showNum();
	}
	public void showNum(){
		int listSize = MedicineList.getMedicineList().getShopingcard(BaseSearchActivity.this).size();
		if (listSize==0) {
			tv_alertnullnum.setText("尚未选择药品");
			ivbuycar_listtitle.setBackgroundResource(R.drawable.buycar_nobuy);
			ivbuycar.setBackgroundResource(R.drawable.buycar_nobuy);
			rl_list.setVisibility(View.GONE);
			rl_ivnum.setVisibility(View.VISIBLE);

		}else {
			tv_alertnullnum.setText("已选择了"+listSize+"种药品");
			ivbuycar_listtitle.setBackgroundResource(R.drawable.buycar_havebuy);
			ivbuycar.setBackgroundResource(R.drawable.buycar_havebuy);
		}
	}
	@Override
	public void onSuccess(Result response) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onSuccess(ArrayList response) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onFailure(Exception e, String errorMsg, int s) {
		// TODO Auto-generated method stub

	}

	public OnClickListener nw = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub//btn_tra
			if(v.getId() == R.id.ivbuycar || v.getId() == R.id.ivbuycar_listtitle ){
				List<MedicineInfo> lists_children1 = null;
				try {
					lists_children1 =MedicineList.getMedicineList().getShopingcard(BaseSearchActivity.this);

					if (lists_children1.size()>0) {

						Confirm();
						listSelected2.clear();
						listSelected2.addAll(lists_children1);
						handler.sendEmptyMessage(1);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				};

			}else if(v.getId() == R.id.btn_clean){
				ClenAll(true);
			}
		}
	};

	ChoiceItemNumInterface choice = new ChoiceItemNumInterface() {
		//搜索，选药等list数据
		@Override
		public void getList(List< MedicineInfo> position) {
			selected = position;
			List<MedicineInfo> select = new ArrayList<MedicineInfo>();
			List<MedicineInfo> select2 = new ArrayList<MedicineInfo>();
			select2.addAll(position);
			totalNum = 0;
			ArrayList<MedicineInfo> selects = new ArrayList<MedicineInfo>() ;
			ArrayList<MedicineInfo> selects2 = new ArrayList<MedicineInfo>() ;
			ArrayList<MedicineInfo> record = MedicineList.getMedicineList().getShopingcard(BaseSearchActivity.this);

			select2.addAll(record);
			for (int i = 0; i < select2.size(); i++) {
				if (!select.contains(select2.get(i))&&select2.get(i).num!=0) {
					select.add(select2.get(i));
					totalNum += select2.get(i).num;
				}
			}
			selects.addAll(select);
			selects2.addAll(selects);
			MedicineList.getMedicineList().setShoppingcard(BaseSearchActivity.this, selects2);
			MedicineList.getMedicineList().setSaveCollectiong(BaseSearchActivity.this, selects);
			listSelected.clear();
			listSelected2.clear();
			listSelected2.addAll(selects2);
			handler.sendEmptyMessage(1);

			showtotalNum(totalNum);
			if (totalNum==0){
				ClenAll(true);
			}


		}

	};
	public void getBuyCarData(boolean isOncreate,boolean showNumAgain){
		ArrayList< MedicineInfo> maps = MedicineList.getMedicineList().getShopingcard(BaseSearchActivity.this);
		listSelected.clear();

		if (null!=maps&&(isOncreate)) {

			totalNum = 0;
			for (int j=0;j< maps.size();j++) {

				if (maps.get(j).num>0) {
					listSelected.add(maps.get(j));
					totalNum += maps.get(j).num;

				}
			}

		}
		if (null == maps||maps.size()==0) {
			totalNum = 0;
		}
		showtotalNum( totalNum);
		listSelected2.clear();
		listSelected2.addAll(listSelected);
		handler.sendEmptyMessage(1);
	}

	public void initdata(){
		rl_list = (RelativeLayout) findViewById(R.id.rl_list);
		selected = new ArrayList<MedicineInfo>();
		listSearch  = new ArrayList<MedicineInfo>();
		lists_parents = new ArrayList<IllEntity>();

		horizontal_list = (ListView) findViewById(R.id.horizontal_list);
		listSelected = new  ArrayList<MedicineInfo>();
		iv_num = (Button) findViewById(R.id.iv_num);
		ivbuycar = (ImageView) this.findViewById(R.id.ivbuycar);
		ivbuycar.setOnClickListener(nw);
		btn_clean = (TextView) findViewById(R.id.btn_clean);
		btn_clean.setOnClickListener(nw);
		iv_num_listtitle = (Button) findViewById(R.id.iv_num_listtitle);
		tv_alertnullnum = (TextView) findViewById(R.id.tv_alertnullnum);
		ivbuycar_listtitle = (ImageView) findViewById(R.id.ivbuycar_listtitle);
		ivbuycar_listtitle.setOnClickListener(nw);
		btn_confirm = (Button) this.findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(this);
		rl_ivnum = (RelativeLayout) findViewById(R.id.rl_ivnum);
		choicedAdapter = new ChoicedItemadapter(this,
				R.layout.item_hri_listview_been_choiced_medicine,
				listSelected2, choice);

		this.findViewById(R.id.btn_tra).setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {

				List<MedicineInfo> lists_children1 = null;
				try {
					lists_children1 =MedicineList.getMedicineList().getShopingcard(BaseSearchActivity.this);

					if (lists_children1.size()>0) {

						Confirm();
						listSelected2.clear();
						listSelected2.addAll(lists_children1);
						handler.sendEmptyMessage(1);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				};

				rl_list.setVisibility(View.GONE);
				rl_ivnum.setVisibility(View.VISIBLE);
				// TODO Auto-generated method stub
				return true;
			}
		});
	}
}
