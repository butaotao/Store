package com.dachen.medicine.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;

import com.dachen.medicine.R;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.entity.CdrugRecipeitem;
import com.dachen.medicine.logic.CompareData;

public class AdapterChoiceGiveMedieNum extends BaseCustomAdapter<CdrugRecipeitem>{
	Context context;
	public AdapterChoiceGiveMedieNum(Context context, int resId,
			List<CdrugRecipeitem> objects) {
		super(context, resId, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	protected com.dachen.medicine.adapter.BaseCustomAdapter.BaseViewHolder getViewHolder() {
		// TODO Auto-generated method stub
		return new ViewHolder();
	}

	@Override
	protected void fillValues(
			com.dachen.medicine.adapter.BaseCustomAdapter.BaseViewHolder baseViewHolder,
			int position) {
		final CdrugRecipeitem item = getItem(position);
		final ViewHolder holder = (ViewHolder) baseViewHolder;
		// TODO Auto-generated method stub
		//int canBuyNum = item.data1.num_syjf/item.data.zsmdwypxhjfs;
		int canBuyNum = CompareData.showNum(item);
		ChangeSelectItem interfaces = new ChangeSelectItem() {
			
			@Override
			public void changeSelectItem(int selectNum) {
				// TODO Auto-generated method stub
				holder.num = selectNum;
				LogUtils.burtLog("selectNum=="+selectNum);
				holder.tv_store.setText("-"+selectNum*item.data.zsmdwypxhjfs+"  剩余"
						+(item.data1.num_syjf-selectNum*item.data.zsmdwypxhjfs));
			}
		};
		AdapterChoiceGiveMedieNumItem adapter = new AdapterChoiceGiveMedieNumItem(context,
				R.layout.adapter_choicegivemedienum_item, item.lists,canBuyNum,interfaces);
		holder.listview.setAdapter(adapter);
		holder.tv_name.setText(CompareData.getName(item.general_name, item.trade_name, null));
		//holder.tv_gavenum.setText(item.t);//num_syjf
		//最低所需积分item.data.zsmdwypxhjfs 每单位所需积分item.data.zyzdsxjfs 
		
	
		String title = "";
		if (null!=item.unit) {
			if (!TextUtils.isEmpty(item.unit.title)) {
				title = item.unit.title;
			} 
		};
		holder.tv_gavenum.setText("可兑换"+canBuyNum+title);
		holder.tv_unit.setText(item.pack_specification);
		 
	//	holder.tv_store.setText(item.data1.num_syjf);
		if (item.lists!=null&&item.lists.size()>0) {
			 setListViewHeightBasedOnChildren(holder.listview);
			 notifyDataSetChanged();
		} 
	}
	public interface ChangeSelectItem{
		public void changeSelectItem(int selectNum);
	}
	private void setListViewHeightBasedOnChildren(ListView listView) {
		android.widget.ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
	public static class ViewHolder extends BaseViewHolder{
		@Bind(R.id.tv_name)
		TextView tv_name;
		@Bind(R.id.tv_gavenum)
		TextView tv_gavenum;
		//分数
		@Bind(R.id.tv_store)
		TextView tv_store;
		@Bind(R.id.listview)
		ListView listview;
		@Bind(R.id.tv_unit)
		TextView tv_unit;
		int num;
	}
}
