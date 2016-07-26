package com.dachen.mediecinelibraryrealize.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.entity.DrugstorefensDes.DrugList;

public class BaiduMedieRestoreDetailsAdapter extends BaseAdapter{
	Context context;
	List<DrugList> list;
	public BaiduMedieRestoreDetailsAdapter(Context context,List<DrugList> list){
		this.context = context;
		this.list = list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		ViewHolder holder;
		// TODO Auto-generated method stub
		DrugList drug = (DrugList) getItem(arg0);
		if (null == view) {
			view = View.inflate(context, R.layout.adapter_baidumedierestoredestails, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			holder.tv_weight = (TextView) view.findViewById(R.id.tv_weight);
			holder.tv_facturyrz = (TextView) view.findViewById(R.id.tv_facturyrz);
			holder.tv_charge = (TextView) view.findViewById(R.id.tv_charge);
			holder.tv_factory = (TextView) view.findViewById(R.id.tv_factory);
			view.setTag(holder);
		}else {
			holder = (ViewHolder) view.getTag();
		}
		holder.tv_name.setText(drug.title);
		String pack = drug.specification;
		if (!TextUtils.isEmpty(pack)){
			if (!TextUtils.isEmpty(drug.pack_specification)){
				pack = pack+"*"+drug.pack_specification;
			}else {
				pack = pack ;
			}

		}else {
			pack = drug.pack_specification;
		}
		holder.tv_weight.setText(pack);
		holder.tv_facturyrz.setVisibility(View.GONE);
		holder.tv_factory.setText(drug.manufacturer);
		if (drug.cert_state!=null&&drug.cert_state.value!=null) {
			if (drug.cert_state.value.equals("9")) {
				holder.tv_facturyrz.setVisibility(View.VISIBLE);
			} 
		}
		//drug.is_charge = true;
		if(drug.is_charge){
			holder.tv_charge.setVisibility(View.VISIBLE);
		}else{
			holder.tv_charge.setVisibility(View.INVISIBLE);
		}
		return view;
	}
	public class ViewHolder{
		TextView tv_name;
		TextView tv_weight;
		TextView tv_facturyrz;
		TextView tv_charge;
		TextView tv_factory;
	}
}
