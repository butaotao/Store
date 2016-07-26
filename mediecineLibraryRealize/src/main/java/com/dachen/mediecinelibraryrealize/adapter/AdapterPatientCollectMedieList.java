package com.dachen.mediecinelibraryrealize.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.activity.AdviceActivity;
import com.dachen.mediecinelibraryrealize.activity.BaiduMapActivity;
import com.dachen.mediecinelibraryrealize.activity.CollectionMedieActivity;
import com.dachen.mediecinelibraryrealize.activity.ErcordingProductActivity;
import com.dachen.mediecinelibraryrealize.entity.PatientMedieBoxs.Info;
import com.dachen.mediecinelibraryrealize.entity.Patients;
import com.dachen.mediecinelibraryrealize.view.DialogChoicePatientGallery;

import java.util.List;

public class AdapterPatientCollectMedieList extends BaseAdapter{
	List<Info> infos;
	Context context;
	String id;
	DialogChoicePatientGallery gallery;
	List<Patients.patient> patients;
	CollectionMedieActivity.RefreshActivity refreshActivity;
	public AdapterPatientCollectMedieList(Context context, List<Info> infos, String id,List<Patients.patient> patients,CollectionMedieActivity.RefreshActivity refreshActivity){
		this.context = context;
		this.infos = infos;
		this.id = id;
		this.patients = patients;
		this.refreshActivity = refreshActivity;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infos.size();
	}

	@Override
	public Object getItem(int p) {
		// TODO Auto-generated method stub
		return infos.get(p);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int p, View view, ViewGroup arg2) {
		ViewHolder holder;
		if (null == view) {
			view = View.inflate(context, R.layout.adapterpatientcollectionmedie, null);
			holder = new ViewHolder();
			//holder.tv_company = (TextView) view.findViewById(R.id.tv_company);
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
			holder.tv_total_des = (TextView) view.findViewById(R.id.tv_total_des);
			holder.rl_showlist = (RelativeLayout) view.findViewById(R.id.rl_showlist);
			holder.rl_recomend = (RelativeLayout) view.findViewById(R.id.rl_recomend);
			holder.rl_detail = (RelativeLayout) view.findViewById(R.id.rl_detail);
			//holder.tv_line = (TextView) view.findViewById(R.id.tv_line);
			holder.iv_havebuy = (ImageView) view.findViewById(R.id.iv_havebuy);
			view.setTag(holder);
		}else {
			holder = (ViewHolder) view.getTag();
		}
		final Info info = (Info) getItem(p);
		String companyname = "";
		if (null!=info.group) {
			//holder.tv_line.setVisibility(View.VISIBLE);
			//holder.tv_company.setVisibility(View.VISIBLE);
			//holder.tv_company.setText(info.group.name);
			companyname = info.group.name;
		}else {
			//holder.tv_line.setVisibility(View.GONE);
			//holder.tv_company.setVisibility(View.GONE);
		}
		if (null!=info.doctor) {
			if(TextUtils.isEmpty(companyname)){
				holder.tv_name.setText("用药建议:"+info.doctor.name);
			}else {
				holder.tv_name.setText("用药建议:"+info.doctor.name+"-"+companyname);
			}

		}else {

				holder.tv_name.setText(info.patientName+"的自建药方");

			
		}
		if (null!=info.date) {
			info.date = info.date.replace("-", "/");
		}
		//holder.tv_time.setText(info.date);
		holder.tv_time.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gallery = new DialogChoicePatientGallery(context,patients,refreshActivity);
				gallery.show();

			}
		});
		holder.tv_total_des.setText("0");
			holder.tv_total_des.setText(info.species_number+"");

		holder.iv_havebuy.setVisibility(View.GONE);
		if (null!=info.state&&null!=info.state.value) {
			if (info.state.value.equals("9")) {
				holder.iv_havebuy.setVisibility(View.GONE);
			}
		}
		holder.rl_showlist.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,ErcordingProductActivity.class); 
				intent.putExtra("ercode", info.id);
				context.startActivity(intent);
			}
		});
			holder.rl_recomend.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
					 	Intent intent = new Intent(context,BaiduMapActivity.class);
						intent.putExtra("code", info.id);
						context.startActivity(intent); 
						}
					});
			holder.rl_detail.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context,AdviceActivity.class);
					intent.putExtra("recipe_id", info.id);
					intent.putExtra("patient", id);
					if (null!=info.doctor) {
						intent.putExtra("name", info.doctor.name);
					}else {
						intent.putExtra("name", "");
					}
					
					context.startActivity(intent);
				}
			});
		// TODO Auto-generated method stub
		return view;
	}
	public static class ViewHolder{
		TextView tv_company;
		TextView tv_name;
		TextView tv_time;
		TextView tv_total_des;
		TextView tv_line;
		RelativeLayout rl_showlist;
		RelativeLayout rl_recomend;
		RelativeLayout rl_detail;
		ImageView iv_havebuy;
	}
}
