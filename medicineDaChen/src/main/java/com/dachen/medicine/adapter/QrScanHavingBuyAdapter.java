package com.dachen.medicine.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;

import com.dachen.medicine.R;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.entity.CdrugRecipeitem;

public class QrScanHavingBuyAdapter extends BaseCustomAdapter<CdrugRecipeitem>{
	String isopen;
	Context context;
	public QrScanHavingBuyAdapter(Context context, int resId,
			List<CdrugRecipeitem> objects) {
		super(context, resId, objects);
		// TODO Auto-generated constructor stub
		isopen = SharedPreferenceUtil.getString("isopen_" + SharedPreferenceUtil.getString("id", ""), "1");
		this.context = context;
	}

	@Override
	protected com.dachen.medicine.adapter.BaseCustomAdapter.BaseViewHolder getViewHolder() {
		// TODO Auto-generated method stub
		return new ViewHolder();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillValues(
			com.dachen.medicine.adapter.BaseCustomAdapter.BaseViewHolder viewholder,
			int position) {
		// TODO Auto-generated method stub
		CdrugRecipeitem bean = getItem(position);
		ViewHolder holder = (ViewHolder)viewholder;
		String name ;
		if ("1".equals(isopen)) {
			name = bean.general_name;
		}else {
			name = bean.trade_name;
			if (name==null||name.equals("")) {
				name = bean.general_name;
			}
		}
		 
		if (null != name && name.length() > 9) {
			name = name.substring(0, 6) + "..."
					+ name.substring(name.length() - 2); 
		} 
		holder.tv_factory.setText("");	
		holder.iv_circle.setVisibility(View.INVISIBLE);
		holder.tv_name.setText(name + "");
		if (bean.manufacturer.contains("#00#")) {
			holder.tv_name.setText(context.getResources().getString(R.string.not_found_this_code)+"");
			
			holder.tv_factory.setText("");	
			holder.iv_circle.setVisibility(View.INVISIBLE);
		}else if(bean.manufacturer.contains("#0011#")){
			holder.tv_name.setText(context.getResources().getString(R.string.no_effect_code)+"");
			
			holder.tv_factory.setText("");	
			holder.iv_circle.setVisibility(View.INVISIBLE);
		} else {
			holder.tv_name.setText(name+"");
			holder.iv_circle.setVisibility(View.VISIBLE);
			holder.tv_factory.setText(bean.manufacturer+"");
			if (bean.isAdd!=1) {
				if (position==getCount()-1){
					holder.iv_circle.setBackgroundResource(R.drawable.scan_subtraction);
				}else {
					holder.iv_circle.setBackgroundResource(R.drawable.scan_subtraction_light);
				}
				
			}else{
				if (position==getCount()-1){
				holder.iv_circle.setBackgroundResource(R.drawable.scan_plus);
				}else {
					holder.iv_circle.setBackgroundResource(R.drawable.scan_plus_light);
				//	ToastUtils.showToast("p=="+position);
				}
			}
		}
		if (position==getCount()-1) {
			holder.tv_name.setTextColor(context.getResources().getColor(R.color.white));
			holder.tv_factory.setTextColor(context.getResources().getColor(R.color.white));
		}else {
			holder.tv_name.setTextColor(context.getResources().getColor(R.color.color_ccccc7));
			holder.tv_factory.setTextColor(context.getResources().getColor(R.color.color_ccccc7));
		}
	}
	public static class ViewHolder extends BaseViewHolder{
		@Bind(R.id.tv_name)
		TextView tv_name;
		@Bind(R.id.tv_factory)
		TextView tv_factory;
		@Bind(R.id.iv_circle)
		ImageView iv_circle;
	}
}
