package com.dachen.mediecinelibraryrealize.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.entity.PatientPoints.Potient;
import com.dachen.mediecinelibraryrealize.entity.PointsGet.Point;

public class AdapterPatientPointXH extends BaseAdapter{
	List<Point> patientpoints;
	Context context;
	ViewHolder holder ;
	public  AdapterPatientPointXH(Context context,List<Point> patientpoints){
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
		Point p = (Point) getItem(arg0);
		if (v==null) {
			v = View.inflate(context, R.layout.adapter_patientpointget, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
			holder.tv_weight = (TextView) v.findViewById(R.id.tv_weight);
			holder.tv_des = (TextView) v.findViewById(R.id.tv_des);
			holder.tv_point = (TextView) v.findViewById(R.id.tv_point);
			holder.ll_points = (LinearLayout) v.findViewById(R.id.ll_points);
			holder.tv_pointunit = (TextView) v.findViewById(R.id.tv_pointunit);
			v.setTag(holder);
		}else {
			holder = (ViewHolder) v.getTag();

		}
		if (null!=p.goods) {
			holder.tv_name.setText(p.goods.title+"");
		}
		holder.tv_point.setText("+"+p.num_hqjf+"");
		holder.tv_point.setTextColor(context.getResources().getColor(R.color.color_ff9d6a));
		if (null!=p.goods$unit&&!TextUtils.isEmpty(p.num_dh)) {
			holder.tv_weight.setText("("+p.num_dh+p.goods$unit.title+")");
		}else {
			holder.tv_weight.setVisibility(View.GONE);
		}
		holder.tv_des.setText(p.created_time);
		//holder.tv_weight.setText(text);
		//holder.tv_des.setText(text); 
		if (p.is_receive){
			holder.tv_point.setText("+" + p.num_hqjf + "");
			holder.ll_points.setBackgroundColor(context.getResources().getColor(R.color.translate));
			holder.tv_point.setTextColor(context.getResources().getColor(R.color.color_ff9d6a));
			holder.tv_pointunit.setTextColor(context.getResources().getColor(R.color.color_aaaaaa));
		}else {
			holder.tv_point.setText("领取" + p.num_hqjf + "");
			holder.ll_points.setBackgroundResource(R.drawable.btn_orange_ff976a_all);
			holder.tv_point.setTextColor(context.getResources().getColor(R.color.white));
			holder.tv_pointunit.setTextColor(context.getResources().getColor(R.color.white));
		}
		return v;
	}
	public class ViewHolder{
		TextView tv_name;
		TextView tv_weight;
		TextView tv_des;
		TextView tv_point;
		TextView created_time;
		TextView tv_pointunit;
		LinearLayout ll_points;
	}
}
