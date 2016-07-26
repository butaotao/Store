package com.dachen.mediecinelibraryrealize.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.entity.PatientPoints.Potient;

public class AdapterPatientPointGet extends BaseAdapter{
	List<Potient> patientpoints;
	Context context;
	ViewHolder holder ;
	  public  AdapterPatientPointGet(Context context,List<Potient> patientpoints){
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
		Potient p = (Potient) getItem(arg0);
		if (v==null) {
			v = View.inflate(context, R.layout.adapter_patientpointget, null);
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
			holder.tv_point.setText("-"+p.num_xfjf+"");
			holder.tv_point.setTextColor(context.getResources().getColor(R.color.color_666666));
			if (null!=p.goods$unit) {
				holder.tv_weight.setText(p.goods$unit.title);	
				
			}
			holder.tv_weight.setVisibility(View.GONE);
			holder.tv_des.setText(p.created_time);

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
