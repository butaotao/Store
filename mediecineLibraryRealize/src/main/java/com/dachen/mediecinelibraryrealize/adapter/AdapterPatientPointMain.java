package com.dachen.mediecinelibraryrealize.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.entity.PatientPoints.Potient;
import com.dachen.mediecinelibraryrealize.entity.PointCanExchanges;

import java.util.List;

public class AdapterPatientPointMain extends BaseAdapter{
	List<PointCanExchanges.PointCanExchange> patientpoints;
	Context context;
	ViewHolder holder ;
	public AdapterPatientPointMain(Context context, List<PointCanExchanges.PointCanExchange> patientpoints){
		this.context = context;
		this.patientpoints = patientpoints;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return patientpoints.size();
	}

	@Override
	public Object getItem(int arg0) {

		// TODO Auto-generated method stub
		return patientpoints.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View v, ViewGroup arg2) {
		// TODO Auto-generated method stub
		PointCanExchanges.PointCanExchange p = (PointCanExchanges.PointCanExchange) getItem(arg0);
		if (v==null) {
			v = View.inflate(context, R.layout.adapter_patientpointmain, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
			holder.tv_weight = (TextView) v.findViewById(R.id.tv_weight);
			holder.tv_des = (TextView) v.findViewById(R.id.tv_des);
			holder.tv_point = (TextView) v.findViewById(R.id.tv_point);
			v.setTag(holder);
		}else {
			holder = (ViewHolder) v.getTag();

		}
		if (null!=p.goods) {
			holder.tv_name.setText(p.goods.title+"");
		}
		holder.tv_point.setText(p.num_syjf + "");

		if (p.num_syjf>=p.zyzdsxjfs&&p.num_syjf>=p.zsmdwypxhjfs){
			holder.tv_point.setTextColor(context.getResources().getColor(R.color.color_ff9d6a));
		}else {
			holder.tv_point.setTextColor(context.getResources().getColor(R.color.color_666666));
		}

		String unit= "";
		if (null!=p.goods$unit&& !TextUtils.isEmpty(p.goods$unit.name)) {
			unit = p.goods$unit.name;


		}
		holder.tv_weight.setText(p.goods$pack_specification);
		//if (p.zsmdwypxhjfs!=0){
			holder.tv_des.setText("使用"+p.zsmdwypxhjfs+"积分可获赠一"+unit);
		//}
		//holder.tv_weight.setText(text);
		//holder.tv_des.setText(text); 

		return v;
	}
	public class ViewHolder{
		TextView tv_name;
		TextView tv_weight;
		TextView tv_des;
		TextView tv_point;
		TextView created_time;
	}
}
