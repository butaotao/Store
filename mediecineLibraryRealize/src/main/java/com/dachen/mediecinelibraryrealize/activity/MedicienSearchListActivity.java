package com.dachen.mediecinelibraryrealize.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.entity.MedicienInfo;
import com.dachen.mediecinelibraryrealize.entity.MedicienInfoBean;

/**
 * @author znouy 药品搜索列表
 */
public class MedicienSearchListActivity extends BaseActivity implements
		OnClickListener, OnHttpListener {

	private ListView mLv_showsearchlist;
	private List<MedicienInfo> mInfo_list = new ArrayList<MedicienInfo>();
	private TextView mTv_title;
	private RelativeLayout mRl_back;
	MyAdapter	myAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initView();

		initData();
	}

	private void initView() {
		setContentView(R.layout.activity_mediciensearchlist);

		// title_bar
		mTv_title = (TextView) findViewById(R.id.tv_title);
		mRl_back = (RelativeLayout) findViewById(R.id.rl_back);
		mRl_back.setOnClickListener(this);

		String trade_name = getIntent().getStringExtra("trade_name");
		mTv_title.setText(trade_name);
		myAdapter = new MyAdapter(mInfo_list);
		mLv_showsearchlist = (ListView) findViewById(R.id.lv_showsearchlist);
		 mLv_showsearchlist.setAdapter(myAdapter);
		 myAdapter.notifyDataSetChanged();
	}

	@Override
	public void onFailure(Exception arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(Result response) {
		closeLoadingDialog();
		LogUtils.burtLog("response===" + response);
		if (null != response) {
			MedicienInfoBean bean = (MedicienInfoBean) response;

			mInfo_list = bean.info_list;
			if (null == mInfo_list || mInfo_list.size() < 1) {
				mLv_showsearchlist.setVisibility(View.GONE);
				return;
			} else {
				mLv_showsearchlist.setVisibility(View.VISIBLE);
			}
			// 刷新数据
			myAdapter = new MyAdapter(mInfo_list);
			 mLv_showsearchlist.setAdapter(myAdapter);
			myAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onSuccess(ArrayList response) {

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.rl_back) {
			finish();
		}

	}
 

	class MyAdapter extends BaseAdapter {
		List<MedicienInfo> mInfo_lists;
		public MyAdapter(List<MedicienInfo> mInfo_list){
			this.mInfo_lists = mInfo_list;
		}
		@Override
		public int getCount() {
			if (mInfo_lists.size() != 0) {
				return mInfo_lists.size();
			} else {
				return 0;
			}
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
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;

			// 获取数据
			final MedicienInfo medicienInfo = mInfo_lists.get(position);

			if (convertView != null) {

				viewHolder = (ViewHolder) convertView.getTag();
			} else {

				viewHolder = new ViewHolder();
				convertView = View.inflate(MedicienSearchListActivity.this,
						R.layout.item_listview_choosemedicien, null);

				viewHolder.mIv_lv_usualmedicien = (ImageView) convertView
						.findViewById(R.id.iv_lv_usualmedicien);
				viewHolder.mTv_lv_medicienname = (TextView) convertView
						.findViewById(R.id.tv_lv_medicienname);
				viewHolder.mTv_lv_company = (TextView) convertView
						.findViewById(R.id.tv_lv_company);
				// 箭头
				viewHolder.rl_lv_right_arrow = (RelativeLayout) convertView
						.findViewById(R.id.rl_lv_right_arrow);
				viewHolder.rl_lv_right_arrow.setVisibility(View.GONE);

				// 点击条目。将数据返回
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(
								MedicienSearchListActivity.this,
								ChooseUsualMedicienActivity.class);

						// 传递数据
						Bundle bundle = new Bundle();
						bundle.putSerializable("medicienInfo", medicienInfo);
						LogUtils.burtLog("==================medicienInfo=="+medicienInfo);
						intent.putExtra("medicienInfo",bundle);
						MedicienSearchListActivity.this.setResult(2, intent);

						finish();
					}
				});
				convertView.setTag(viewHolder);
			}

			// 绑定数据
			viewHolder.mTv_lv_medicienname.setText(medicienInfo.title);
			viewHolder.mTv_lv_company.setText(medicienInfo.manufacturer);

			return convertView;
		}

	}

	class ViewHolder {
		private ImageView mIv_lv_usualmedicien;

		private TextView mTv_lv_medicienname;

		private TextView mTv_lv_company;

		private RelativeLayout rl_lv_right_arrow;

	}

	private void initData() {
		String trade_name = getIntent().getStringExtra("trade_name");
		if (trade_name != "") {
			// http://192.168.3.7:9002/web/api/invoke/8805a5c859fe4800a94475c8c484da72/
			// c_Goods.select?trade_name=%E6%84%9F%E5%86%92%E7%81%B5

			HashMap<String, String> interfaces = new HashMap<String, String>();

			try {
				String value = URLEncoder.encode(trade_name, "utf-8");
				interfaces.put("trade_name", value); 
				new HttpManager().get(this,
						Params.getInterface("invoke", "c_Goods.select"),
						MedicienInfoBean.class, interfaces, this, false, 2);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}

	}

}
