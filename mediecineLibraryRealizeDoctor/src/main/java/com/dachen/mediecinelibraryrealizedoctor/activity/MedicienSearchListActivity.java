package com.dachen.mediecinelibraryrealizedoctor.activity;


import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
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
import com.dachen.medicine.net.Params;
import com.dachen.mediecinelibraryrealizedoctor.R;
import com.dachen.mediecinelibraryrealizedoctor.entity.Constants;
import com.dachen.mediecinelibraryrealizedoctor.entity.DrugData;
import com.dachen.mediecinelibraryrealizedoctor.entity.DrugDtaList;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicienInfo;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicienInfoBean;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicienInfoFactory;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineEntity;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo;
import com.dachen.mediecinelibraryrealizedoctor.entity.MeidecineSearchList;
import com.dachen.mediecinelibraryrealizedoctor.utils.Json.DrugChange;

/**
 * @author znouy 药品搜索列表
 */
public class MedicienSearchListActivity extends BaseActivity implements
		OnClickListener, OnHttpListener {
	MyAdapter myAdapter;
	private ListView mLv_showsearchlist;
	private ArrayList<MedicineInfo>  mInfo_list ;
	private TextView mTv_title;
	private RelativeLayout mRl_back;
	private String id = "";
	Intent intent  ;
	public int checkPosition  = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initView();
		intent = new Intent();;

	}

	private void initData() {
		String trade_name = getIntent().getStringExtra("trade_name");
		if (!TextUtils.isEmpty(trade_name)) {
			showLoadingDialog();


			String SEARCH_DRUG_BY_GROUPID = "org/goods/getGoodsListByGroupId";
			String id = this.getIntent().getStringExtra("id");
			//id = "573c0c2ada1a9e20a0709f8e";
			Map<String, String> params = new HashMap<String, String>();
			String access_token = UserInfo.getInstance(MedicienSearchListActivity.this).getSesstion();
			params.put("access_token", access_token);
			params.put("groupId", id);
			params.put("source", ""+0);//来源 0 平台 1 药企
			new HttpManager().get(this, SEARCH_DRUG_BY_GROUPID,
					DrugDtaList.class,
					params,this, false, 1);
		}

	}
	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);


	}
	private void initView() {
		setContentView(R.layout.activity_mediciensearchlist);
		ViewStub vstub_title = (ViewStub) findViewById(R.id.vstub_title);
		RelativeLayout ll_sub = (RelativeLayout) findViewById(R.id.ll_sub);
		View view = vstub_title.inflate(this, R.layout.viewstub_text, ll_sub);
		TextView tv = (TextView) view.findViewById(R.id.tv_save);
		tv.setText("确定");
		tv.setOnClickListener(this);
		// title_bar
		mTv_title = (TextView) findViewById(R.id.tv_title);
		mRl_back = (RelativeLayout) findViewById(R.id.rl_back);
		mRl_back.setOnClickListener(this);
		mInfo_list = new ArrayList<MedicineInfo>();;
		String trade_name = getIntent().getStringExtra("trade_name");
		id = getIntent().getStringExtra("id");
		mTv_title.setText(trade_name);
		myAdapter = new MyAdapter(mInfo_list);
		mLv_showsearchlist = (ListView) findViewById(R.id.lv_showsearchlist);
		// mLv_showsearchlist.setAdapter(myAdapter);
		// myAdapter.notifyDataSetChanged();
		initData();
	}

	@Override
	public void onFailure(Exception arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(Result response) {
		closeLoadingDialog();
		if (response.resultCode ==1 ) {
			if (response instanceof DrugDtaList) {
				DrugDtaList bean = (DrugDtaList) response;
				ArrayList<DrugData> mInfo_list = bean.data;
				ArrayList<MedicineInfo> infos = DrugChange.getMedicineList(mInfo_list);

				if (null == mInfo_list || mInfo_list.size() < 1) {
					mLv_showsearchlist.setVisibility(View.GONE);
					return;
				} else {
					mLv_showsearchlist.setVisibility(View.VISIBLE);
				}
				// 刷新数据
//			mLv_showsearchlist.setAdapter(myAdapter);
				myAdapter = new MyAdapter(infos);
				myAdapter.notifyDataSetChanged();
				mLv_showsearchlist.setAdapter(myAdapter);
			}
		}else {
			ToastUtils.showResultToast(this, response);
		}

	}

	@Override
	public void onSuccess(ArrayList response) {

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.rl_back) {
			finish();
		}else if (v.getId() == R.id.tv_save){
			setResult(RESULT_OK, intent);
			finish();
		}

	}


	class MyAdapter extends BaseAdapter {
		ArrayList<MedicineInfo> mInfo_lists;
		public MyAdapter(ArrayList<MedicineInfo> mInfo_list){
			this.mInfo_lists = mInfo_list;
		}
		@Override
		public int getCount() {
			return mInfo_lists.size();

		}

		@Override
		public Object getItem(int position) {
			return mInfo_lists.get(position);

		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;

			// 获取数据
			MedicineInfo medicienInfo = mInfo_lists.get(position);

			if (convertView != null) {
				viewHolder = (ViewHolder) convertView.getTag();
			} else {

				viewHolder = new ViewHolder();
				convertView = View.inflate(MedicienSearchListActivity.this,
						R.layout.item_listview_choosemediciendoctor, null);

				viewHolder.mIv_lv_usualmedicien = (ImageView) convertView
						.findViewById(R.id.iv_lv_usualmedicien);
				viewHolder.mTv_lv_medicienname = (TextView) convertView
						.findViewById(R.id.tv_lv_medicienname);
				viewHolder.mTv_lv_company = (TextView) convertView
						.findViewById(R.id.tv_lv_company);
				// 箭头
				viewHolder.rl_lv_right_arrow = (RelativeLayout) convertView
						.findViewById(R.id.rl_lv_right_arrow);


				convertView.setTag(viewHolder);
			}
			if (!medicienInfo.check){
				viewHolder.mIv_lv_usualmedicien.setImageResource(R.drawable.medicien_unchecked);
			}else {
				viewHolder.mIv_lv_usualmedicien.setImageResource(R.drawable.choice);
			}

			viewHolder.rl_lv_right_arrow.setVisibility(View.GONE);
			// 点击条目。将数据返回
			final ViewHolder finalViewHolder = viewHolder;
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MedicineInfo medicienInfo = mInfo_lists.get(position);
					Bundle bundle = new Bundle();
					checkPosition = position;
					for (int i = 0;i<mInfo_lists.size();i++){
						MedicineInfo medicien  = mInfo_lists.get(i);
						if (i==position){
							medicien.check = true;
						}else {
							medicien.check = false;
						}
						mInfo_lists.set(i,medicien);
					}
				/*	MedicineInfo info = new MedicineInfo();
					info.goods$image_url = medicienInfo.image_url;
					if (null!=medicienInfo.goods$manufacturer) {
						info.goods$manufacturer = medicienInfo.goods$manufacturer;
					}else {
						info.goods$manufacturer = medicienInfo.manufacturer;
					}
					info.goods$pack_specification = medicienInfo.pack_specification;
					info.goods$specification = medicienInfo.specification;
					info.goods$general_name = medicienInfo.general_name;
					info.goods$biz_type = medicienInfo.biz_type;
					info.goods$type = medicienInfo.type;
					info.goods$trade_name = medicienInfo.trade_name;
					info.goods$number = medicienInfo.number;
					info.goods_usages_goods = medicienInfo.goods_usages;
					info.goods$type = medicienInfo.type;
					MedicineInfo.GoodForm form = info.new GoodForm();
					if (null!=medicienInfo.form){
						form.name = medicienInfo.form.name;
					}
					info.goods$form = form;
					info.form = medicienInfo.form;
					info.id = id;
					MedicineEntity entity = new MedicineEntity();
					MedicineEntity.Goods goods = entity.new Goods();
					goods.id = medicienInfo.id;
					goods.title = medicienInfo.title;
					info.goods = goods;
					info.title = medicienInfo.title;*/
					bundle.putSerializable("choice", (Serializable)medicienInfo);
					intent.putExtra("choice", bundle);
					notifyDataSetChanged();
				}
			});
			// 绑定数据
			viewHolder.mTv_lv_medicienname.setText(medicienInfo.title);
			viewHolder.mTv_lv_company.setText(medicienInfo.goods$manufacturer);

			return convertView;
		}

	}

	class ViewHolder {
		private ImageView mIv_lv_usualmedicien;

		private TextView mTv_lv_medicienname;

		private TextView mTv_lv_company;

		private RelativeLayout rl_lv_right_arrow;

	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

}
