package com.dachen.mediecinelibraryrealize.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.entity.Drugstorefens;
 
public class RecomentAdapter extends BaseAdapter{
	Context context;
	List<Drugstorefens> store;
	public  RecomentAdapter(Context context,List<Drugstorefens> store){
		this.context = context;
		this.store = store;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return store.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return store.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@SuppressWarnings("null")
	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		Drugstorefens store = (Drugstorefens) getItem(arg0); 
		ViewHolder holder ;
		// TODO Auto-generated method stub
		if (null == view) {
			view = View.inflate(context, R.layout.adapter_recomment, null);
			holder = new ViewHolder();
			holder.tv_storename = (TextView) view.findViewById(R.id.tv_storename);
			holder.tv_gavemedie = (TextView) view.findViewById(R.id.tv_gavemedie);
			holder.tv_welfare = (TextView) view.findViewById(R.id.tv_welfare);
			holder.tv_location = (TextView) view.findViewById(R.id.tv_location);
			holder.tv_timephone = (TextView) view.findViewById(R.id.tv_timephone);
			holder.tv_longdis = (TextView) view.findViewById(R.id.tv_longdis);
			holder.tv_recognase = (TextView) view.findViewById(R.id.tv_recognase);
			holder.tv_far = (TextView) view.findViewById(R.id.tv_far);
			holder.tv_bainum = (TextView) view.findViewById(R.id.tv_bainum);
			holder.iv_baidushownum = (ImageView) view.findViewById(R.id.iv_baidushownum);
			holder.tv_ischarge = (TextView) view.findViewById(R.id.tv_ischarge);
			view.setTag(holder);
		}else {
			holder = (ViewHolder) view.getTag();
		}
		holder.tv_storename.setText(""+store.name);
		/*zcyb : Boolean          //是否支持医疗保险购药
		zcsy : Boolean          //是否支持送药
		num_cert : Integer      //有货并认证的品种数
*/      holder.tv_gavemedie.setVisibility(View.GONE);
		holder.tv_welfare.setVisibility(View.GONE);
		holder.tv_recognase.setVisibility(View.GONE);
		if (store.zcsy) {
			holder.tv_gavemedie.setVisibility(View.VISIBLE);
		} 
		if (store.zcyb) {
			holder.tv_welfare.setVisibility(View.VISIBLE);
		}
		if (!TextUtils.isEmpty(store.num_cert)&&!store.num_cert.trim().equals("0")) {
			holder.tv_recognase.setVisibility(View.VISIBLE);
			holder.tv_recognase.setText("("+store.num_cert+"种厂家认证)");
		}
		if(store.is_charge){
			holder.tv_ischarge.setVisibility(View.VISIBLE);
		}else {
			holder.tv_ischarge.setVisibility(View.GONE);
		}
		String telephone = "";
		if (!TextUtils.isEmpty(store.yysjd)) {
			telephone = store.yysjd;
		}
		String des = "";
		if (!TextUtils.isEmpty(store.lxrsj)) {
			des = store.lxrsj;
		}
		holder.tv_timephone.setText(telephone+"  "+des);
		String lxdz = "";
		if (!TextUtils.isEmpty(store.lxdz)) {
			lxdz = store.lxdz;
		}
		holder.tv_location.setText(lxdz+""); 
		
		holder.tv_longdis.setText("匹配"+store.num_on_stack+"种药品");
		holder.tv_far.setText("距您"+store.mm_str);
		holder.tv_bainum.setText(store.fen+"");
		int id = com.dachen.medicine.common.utils.CommonUtils.getId(context, "drawable", "icon_mark"+arg0);
		holder.iv_baidushownum.setBackgroundResource(id);
		return view;
	}
	public class ViewHolder{
		TextView tv_storename;
		TextView tv_gavemedie;
		TextView tv_welfare;
		TextView tv_location;
		TextView tv_timephone;
		TextView tv_longdis;
		TextView tv_recognase;
		TextView tv_far;
		TextView tv_bainum;
		TextView tv_ischarge;
		ImageView iv_baidushownum;
	}
}
