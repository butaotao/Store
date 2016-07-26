package com.dachen.medicine.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;

import com.dachen.medicine.R;
import com.dachen.medicine.bean.MedieNum;
import com.dachen.medicine.common.utils.CustomUtils;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.entity.CdrugRecipeitem;
import com.dachen.medicine.entity.DeleteInterace;
import com.dachen.medicine.entity.SelectInterface;

public class EditListAdapter extends BaseCustomAdapter<CdrugRecipeitem>{
	List<CdrugRecipeitem> list;
	String isopen;
	Context context;
	SelectInterface iterface;
	public EditListAdapter(Context context, int resId,
			List<CdrugRecipeitem> list,SelectInterface iterface) {
		super(context, resId, list);
		// TODO Auto-generated constructor stub
		this.list = list;
		isopen = SharedPreferenceUtil.getString("isopen_" + SharedPreferenceUtil.getString("id", ""), "1");
		this.context = context;
		this.iterface = iterface;
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
		// TODO Auto-generated method stub
		final ViewHolder holder = (ViewHolder)baseViewHolder;
		final CdrugRecipeitem drug = getItem(position);
		String name = drug.general_name;
		holder.listview.setVisibility(View.GONE);
		if (isopen.equals("1")) {
			name = drug.general_name;
		}else {
			name = drug.trade_name;
			if (name==null||name.equals("")) {
				name = drug.general_name;
			}
		} 
		if (null != name && name.length() > 9) {
			name = name.substring(0, 6) + "..."
					+ name.substring(name.length() - 2); 
		}  
		holder.tv_name.setText(name + "");
		holder.tv_weight.setText(drug.pack_specification);
		if (null!=drug.lists&&drug.lists.size()>0) {
			holder.listview.setVisibility(View.VISIBLE);
			holder.rl_no_children.setVisibility(View.GONE);
			WrongNumAdapter adapter = new WrongNumAdapter(context,
					R.layout.adapter_wrongnum,drug.lists, new DeleteInterace() {
				@Override
				public void delete(List<MedieNum> objects) {
					drug.lists = (ArrayList<MedieNum>) objects;
					// TODO Auto-generated method stub  (String needBuy,String haveBuy,String nowBuy)
					//int num = (Integer.parseInt(drug.requires_quantity)-objects.size()); 
					int num = CustomUtils.getData(drug.requires_quantity,drug.bought_quantity,objects.size());
					if(num > 0){
						drug.isAdd=0;
					}else if(num==0){ 
						drug.isAdd=2;
					}else if(num<0){  
						drug.isAdd=1; 
					}
					drug.numResone = list.size();
					iterface.getSelect(drug,list.size()); 
					setListViewHeightBasedOnChildren(holder.listview);
					notifyDataSetChanged();
				}
			});
			holder.listview.setAdapter(adapter); 
			drug.isOpen = true;
			setListViewHeightBasedOnChildren(holder.listview); 
			
		}else {
			holder.rl_no_children.setVisibility(View.VISIBLE);
			holder.tv_no_children.setText("无");
		}
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
		@Bind (R.id.tv_name)
		TextView tv_name;
		@Bind (R.id.tv_weight)
		TextView tv_weight;
		@Bind (R.id.tv_no_children)
		TextView tv_no_children;
		@Bind (R.id.rl_no_children)
		RelativeLayout rl_no_children;
		@Bind (R.id.listview)
		ListView listview;
		}
}
