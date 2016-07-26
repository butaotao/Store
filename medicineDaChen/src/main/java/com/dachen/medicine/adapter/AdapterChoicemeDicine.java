package com.dachen.medicine.adapter;

import java.util.List;

import android.content.Context;
import android.widget.TextView;
import butterknife.Bind;

import com.dachen.medicine.R;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.entity.CdrugRecipeitem;
import com.dachen.medicine.logic.CompareData;

public class AdapterChoicemeDicine extends BaseCustomAdapter<CdrugRecipeitem>{
	public String isopen;
	List<CdrugRecipeitem> objects;
	public AdapterChoicemeDicine(Context context, int resId,
			List<CdrugRecipeitem> objects ) { 
	/*	CdrugRecipeitem item = new CdrugRecipeitem();
		this.objects.add(item);*/
		super(context, resId, objects);
		// TODO Auto-generated constructor stub
		isopen = SharedPreferenceUtil.getString("isopen_" + SharedPreferenceUtil.getString("id", ""), "1");
		this.objects = objects;
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
		CdrugRecipeitem item =getItem(position);
		ViewHolder holder = (ViewHolder) baseViewHolder;
		//if (position<objects.size()-1) {
			holder.tv_company.setText(item.manufacturer); 
			holder.tv_name.setText(CompareData.getName(item.general_name,item.trade_name,isopen));
		/*}else {
			holder.tv_company.setText("新增药品"); 
		}*/
		
		
	}
	public static class ViewHolder extends BaseViewHolder{
		@Bind(R.id.tv_name)
		TextView tv_name;
		@Bind(R.id.tv_company)
		TextView tv_company;
	}
}
