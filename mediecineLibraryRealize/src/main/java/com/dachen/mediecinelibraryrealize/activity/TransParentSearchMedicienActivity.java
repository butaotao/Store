package com.dachen.mediecinelibraryrealize.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
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
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.entity.MedicienBreedBean;
import com.dachen.mediecinelibraryrealizedoctor.entity.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransParentSearchMedicienActivity extends BaseActivity implements
		OnClickListener, OnHttpListener {

	private ListView mLv_showseachermedicien;
	private TextView mTv_title;
	private RelativeLayout mRl_back;
	private ClearEditText search_edit;

	private String mParamsValue;
	private List<MedicienBreedBean.Data.PageData> set_datas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initView();
		initEvent();

	}

	private void initEvent() {

		search_edit.setOnKeyListener(new OnKeyListener() {// 文本编辑框设置回车键监听

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				mParamsValue = search_edit.getText().toString().trim();


				try {

					if (keyCode == KeyEvent.KEYCODE_ENTER) {

						if (!TextUtils.isEmpty(mParamsValue)) {
							// http://192.168.3.7:9002/web/api/invoke/8805a5c859fe4800a94475c8c484da72/
							// c_user_goods_CB.get_tradeNames?name=
							// 根据输入名字获取商品名集合
							//{"set_datas":["感冒药","999感冒灵","感冒灵"]}

							HashMap<String, String> interfaces = new HashMap<String, String>();
							String access_token = UserInfo.getInstance(TransParentSearchMedicienActivity.this).getSesstion();
							interfaces.put("access_token", access_token);
							interfaces.put("keyword", mParamsValue);
							interfaces.put("pageSize", ""+20000);//分页大小，前端没做分页，直接用最大值
							interfaces.put("pageIndex", ""+0);

							new HttpManager()
									.post(TransParentSearchMedicienActivity.this, Constants.SEARCH_GOODS,
											MedicienBreedBean.class,// 得到商品名集合
											interfaces,
											TransParentSearchMedicienActivity.this,
											false, 1);//
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}
		});
	}

	private void initView() {
		setContentView(R.layout.activity_transparentsearchmedicien);

		// 设置窗体控件透明
		Window window = getWindow();
		WindowManager.LayoutParams wl = window.getAttributes();
		// wl.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		wl.alpha = 0.99f;// 设置透明度,0.0为完全透明，1.0为完全不透明
		window.setAttributes(wl);

		// title_bar
		mTv_title = (TextView) findViewById(R.id.tv_title);
		mRl_back = (RelativeLayout) findViewById(R.id.rl_back);
		mRl_back.setOnClickListener(this);
		mTv_title.setText("选常备药");

		tv_showseachermedicien = (TextView) findViewById(R.id.tv_showseachermedicien);
		search_edit = (ClearEditText) findViewById(R.id.search_edit);
		mLv_showseachermedicien = (ListView) findViewById(R.id.lv_showseachermedicien);

	}

	@Override
	public void onFailure(Exception arg0, String arg1, int arg2) {

	}

	@Override
	public void onSuccess(Result response) {
		closeLoadingDialog();

		if (null != response&&response.resultCode ==1) {
			MedicienBreedBean bean = (MedicienBreedBean) response;
			if (null!=bean.data&&null!=bean.data.pageData){
				// 商品名集合
				set_datas = bean.data.pageData;
				if (null == set_datas || set_datas.size() < 1) {
					mLv_showseachermedicien.setVisibility(View.GONE);
					tv_showseachermedicien.setVisibility(View.VISIBLE);
					tv_showseachermedicien.setText("未找到该药品");
					return;
				} else {
					mLv_showseachermedicien.setVisibility(View.VISIBLE);
					tv_showseachermedicien.setVisibility(View.GONE);
				}
				mLv_showseachermedicien.setAdapter(myAdapter);
				myAdapter.notifyDataSetChanged();
			}else {
				mLv_showseachermedicien.setVisibility(View.GONE);
				tv_showseachermedicien.setVisibility(View.VISIBLE);
				tv_showseachermedicien.setText("未找到该药品");
			}

		}else {
				ToastUtils.showResultToast(this, response);
		}

	}

	@Override
	public void onSuccess(ArrayList arg0) {

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.rl_back) {
			finish();
		}

	}

	MyAdapter myAdapter = new MyAdapter();
	private TextView tv_showseachermedicien;

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (set_datas.size() == 0) {
				return 0;
			} else {

				return set_datas.size();
			}
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return set_datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// 获取数据
			final MedicienBreedBean.Data.PageData medicienBreed = set_datas.get(position);

			ViewHolder viewHolder = null;
			if (convertView != null) {

				viewHolder = (ViewHolder) convertView.getTag();
			} else {

				viewHolder = new ViewHolder();
				convertView = View.inflate(
						TransParentSearchMedicienActivity.this,
						R.layout.item_listview_transparentsearchmedicien, null);

				viewHolder.mTv_lv_medicienname = (TextView) convertView
						.findViewById(R.id.tv_lv_medicienname);
				// 条目中显示的药名
				final String breedname = medicienBreed.generalName;
				viewHolder.mTv_lv_medicienname.setText(breedname);

				final int l = position;
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

					/*	ToastUtils.showToast("透明activity listview" + l
								+ "条目被点击了");*/
						Intent intent = new Intent(
								TransParentSearchMedicienActivity.this,
								ChooseUsualMedicienActivity.class);

						// 传递数据
						intent.putExtra("breedname", breedname);
						intent.putExtra("id", medicienBreed.id);
						TransParentSearchMedicienActivity.this.setResult(1,
								intent);

						finish();
					}
				});

				convertView.setTag(viewHolder);
			}

			// 设置数据
			viewHolder.mTv_lv_medicienname.setText(medicienBreed.generalName);

			return convertView;
		}

	}

	class ViewHolder {

		private TextView mTv_lv_medicienname;

	}
}
