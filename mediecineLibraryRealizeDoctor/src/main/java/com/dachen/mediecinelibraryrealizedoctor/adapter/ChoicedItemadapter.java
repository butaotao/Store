package com.dachen.mediecinelibraryrealizedoctor.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.view.AddAndSubView;
import com.dachen.medicine.view.AddAndSubView.OnNumChangeListener;
import com.dachen.mediecinelibraryrealizedoctor.R;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo;
import com.dachen.mediecinelibraryrealizedoctor.utils.CompareDatalogic;

public class ChoicedItemadapter extends BaseAdapter {
	AddAndSubView addAndSubView1;
	Context context;
	public List<MedicineInfo> list;
	ChoiceItemNumInterface numChoices;

	public ChoicedItemadapter(Context context, int resId,
			List<MedicineInfo> list, ChoiceItemNumInterface numChoice) {
		this.context = context;
		this.list = list;
		numChoices = numChoice;
	/*	for (int i = 0; i < list.size(); i++) {
			if (list .get(i).num==0) {
			}else {
				this.list .add(list.get(i));
			}
		}*/
		// TODO Auto-generated constructor stub
	}
	public void refreshData(List<MedicineInfo> list){
		this.list.clear();
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	public static class ViewHolder  {
		TextView tv_name;
		TextView tv_weight;
		AddAndSubView subview_add;
		RelativeLayout iv_single;
		ImageView iv_add_single;
		TextView tv_factory;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int p) {
		// TODO Auto-generated method stub
		return list.get(p);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (null == view) {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.item_hri_listview_been_choiced_medicine, null);
			holder.iv_single = (RelativeLayout) view.findViewById(R.id.iv_single);
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			holder.tv_weight = (TextView) view.findViewById(R.id.tv_weight);
			holder.iv_add_single = (ImageView) view.findViewById(R.id.iv_add_single);
			holder.subview_add = (AddAndSubView) view.findViewById(R.id.subview_add);
			holder.tv_factory = (TextView) view.findViewById(R.id.tv_factory);
			view.setTag(holder);
		}else {
			holder = (ViewHolder) view.getTag();
		}
		final MedicineInfo media = (MedicineInfo) getItem(position);
		//goods$general_name
		String name = media.goods$trade_name;
/*		if (null==name) {
			name = media.goods$general_name;
		}if (null != name && name.length() > 9) {
			name = name.substring(0, 6) + "..."
					+ name.substring(name.length() - 2);
		}*/

		String goodsname="";
			goodsname = media.title;
		name = CompareDatalogic.getShowName(media.goods$general_name, media.goods$trade_name, goodsname);
			holder.tv_name.setText(name + "");
			holder.tv_weight.setText(media.goods$specification);
			holder.tv_factory.setText(media.goods$manufacturer);


			holder.subview_add.setSubVisiAble();
			holder.subview_add.setVisibility(View.VISIBLE);
			holder.subview_add.setButtonBgResource(R.drawable.imports,R.drawable.addpress);
			holder.subview_add.setButtonLayoutParm(34,34);

			if (media.getNum()>0) {

			holder.subview_add.setNum(media.getNum());
			holder.subview_add.setOnNumChangeListener(new OnNumChangeListener() {
				@Override
				public void onNumChange(View view, int num) {

					MedicineInfo entity  = (MedicineInfo) getItem(position);

					if (null != entity) {

						if (entity.num >= 0) {
							entity.num = (num );
							list.set(position, entity);
						}

					} else {
						entity = list.get(position);
						entity.time = (com.dachen.medicine.common.utils.TimeUtils
								.getNowTime());
						entity.num = num;
						list.set(position, entity);
					}

					numChoices.getList(list);
				if (num>0) {
					holder.iv_single.setVisibility(View.INVISIBLE);
					holder.subview_add.setVisibility(View.VISIBLE);
					holder.subview_add.setSubVisiAble();
			    }else {
			    	holder.iv_single.setVisibility(View.VISIBLE);
					//holder.subview_add.setVisibility(View.INVISIBLE);
					holder.subview_add.setSubInvisiAble();
				}
				 notifyDataSetChanged();
				}
			});

			}else {
				holder.subview_add.setSubInvisiAble();

				holder.subview_add.setOnNumChangeListener(new OnNumChangeListener() {
					@Override
					public void onNumChange(View view, int num) {
						MedicineInfo entity = (MedicineInfo) getItem(position);
						if (null != entity) {

							if (entity.num >= 0) {
								entity.num = (1 );
								list.set(position , entity);
							}

						} else {
							entity = list.get(position );
							entity.time = (com.dachen.medicine.common.utils.TimeUtils
									.getNowTime());
							entity.num = 1;
							list.set(position , entity);
						}
						numChoices.getList(list);
						holder.iv_single.setVisibility(View.GONE);
						holder.subview_add.setSubVisiAble();
						 notifyDataSetChanged();
					}
				});
			}
		return view;
	}
}
