package com.dachen.mediecinelibraryrealize.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.net.CustomImagerLoader;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.entity.SomeBox.patientSuggest;
import com.dachen.mediecinelibraryrealize.entity.SomeBox.patientSuggest.Uses;
import com.dachen.mediecinelibraryrealize.utils.CompareDatalogic;
import com.dachen.mediecinelibraryrealizedoctor.activity.MedieSpecificationActivity;
import com.dachen.mediecinelibraryrealizedoctor.utils.StringUtils;

import java.util.ArrayList;

public class AdviceCareAdapter extends BaseAdapter{
	Context context;
	String list;
	ArrayList<patientSuggest> c_patient_drug_suggest_list;
	public AdviceCareAdapter(Context context, ArrayList<patientSuggest> c_patient_drug_suggest_list, String list){
		this.context = context;
		this.c_patient_drug_suggest_list = c_patient_drug_suggest_list;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return c_patient_drug_suggest_list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return c_patient_drug_suggest_list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {  
		final ViewHolder holder;
		if (null==view) {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.adapter_adivice_care, null);
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			holder.tv_weight = (TextView) view.findViewById(R.id.tv_weight);
			holder.tv_unit = (TextView) view.findViewById(R.id.tv_unit);
			holder.tv_num = (TextView) view.findViewById(R.id.tv_num);
			holder.tv_company = (TextView) view.findViewById(R.id.tv_company);
			holder.rl_upordown = (RelativeLayout) view.findViewById(R.id.rl_upordown);
			holder.rl_upordown_show  = (LinearLayout) view.findViewById(R.id.rl_upordown_show);
			holder.iv_down = (ImageView) view.findViewById(R.id.iv_down);
			holder.tv_eat_suggest = (TextView) view.findViewById(R.id.tv_eat_suggest);
			holder.tv_target_patient_type = (TextView) view.findViewById(R.id.tv_target_patient_type);
			holder.rl_meidicineinfo = (RelativeLayout) view.findViewById(R.id.rl_meidicineinfo);
			holder.tv_package = (TextView)view.findViewById(R.id.tv_package);
			holder.tv_package_des = (TextView) view.findViewById(R.id.tv_package_des);
			holder.tv_target_patient_get_des = (TextView)view.findViewById(R.id.tv_target_patient_get_des);
			holder.tv_target_patient_get = (TextView)view.findViewById(R.id.tv_target_patient_get);
			holder.tv_eat_suggest_sellinfo = (TextView)view.findViewById(R.id.tv_eat_suggest_sellinfo);
		 	holder.tv_target_patient_sellinfo_des = (TextView) view.findViewById(R.id.tv_target_patient_sellinfo_des);
			holder.iv_exchange = (ImageView)view.findViewById(R.id.iv_exchange);
			holder.tv_easy_sell = (ImageView)view.findViewById(R.id.tv_easy_sell);
			holder.begindate = (TextView) view.findViewById(R.id.begindate);
			holder.iv_medie = (ImageView) view.findViewById(R.id.iv_medie);
			holder.rl_begin_data = (RelativeLayout) view.findViewById(R.id.rl_begin_data);
			view.setTag(holder);
			 
		}else {
			holder = (ViewHolder) view.getTag();
		}
		holder.rl_begin_data.setVisibility(View.GONE);
		 final patientSuggest su = (patientSuggest) getItem(arg0);
		String periodDay = "";
		holder.tv_weight.setText(su.specification);
		String name = "";

		String goodsname = "";
		if (null!=su.drug){
			goodsname = su.drug.title;
		}else if(null!=su.title){
			goodsname = su.title;
		}
		 name = CompareDatalogic.getShowName(su.general_name,su.trade_name,goodsname);
		holder.tv_name.setText(name);
		
		if (!TextUtils.isEmpty(su.units)) {
			holder.tv_unit.setText(su.units);
		}else {
			holder.tv_unit.setText("");
		}
		
		holder.tv_num.setText(su.requires_quantity);
		holder.tv_company.setText(su.manufacturer);


		String alert = "";
		Uses users;
		String method="";
		String des ="";
		if (su.c_drug_usage_list.size()>0) {
			users = su.c_drug_usage_list.get(0);
			String times;
			if (TextUtils.isEmpty(users.times)){
				times = "0";
			}else{
				times = users.times;
			}

			if (!TextUtils.isEmpty(users.method)){
				method = users.method;
			}
			String quantity = "";

			String period = "";
			String intervalday = "";
			if (!TextUtils.isEmpty(users.periodNum) ){
				intervalday = users.periodNum;
			}
			if (!TextUtils.isEmpty(users.periodUnit)&&!times.equals("0")){
				periodDay = users.periodUnit;
					period = "每"+intervalday+periodDay + times + "次";
			}
			if (!TextUtils.isEmpty(users.quantity)){
				quantity = ";每次"+users.quantity;
				if (!TextUtils.isEmpty(users.unit)){
					quantity = quantity+users.unit;
				}
			}
/*
			String day = "";
			if (TextUtils.isEmpty(su.days)){
				day = ";用药"+su.days+"天。";
			}*/
			String patients ="";
			if (users!=null&&!TextUtils.isEmpty(users.patients)){
				patients = users.patients+";";
			}
			String methods = "";
			if (users!=null&&!TextUtils.isEmpty(users.method)){
				methods = ";"+users.method;
			}

			if (null!=list){
				des =  patients+period+quantity+methods+"。";
			}else {
				des = patients+period+quantity+methods+"。";
			}
			if (TextUtils.isEmpty(des)){
				holder.tv_target_patient_type.setVisibility(View.GONE);
				holder.tv_eat_suggest.setVisibility(View.GONE);
			}else {
				if (des.contains(",,")){
					des = des.replace(",,",",").replace(";;",";");
				}
				if (des.endsWith(",")&&des.length()>2){
					des = des.substring(0,des.length()-1);
				}
				holder.tv_target_patient_type.setVisibility(View.VISIBLE);
				holder.tv_eat_suggest.setVisibility(View.VISIBLE);
			}
		} 

		holder.tv_eat_suggest.setText(des);
		String urls ="";
		if (!TextUtils.isEmpty(su.image)){
			 urls = su.image;
		}

		CustomImagerLoader.getInstance().loadImage(holder.iv_medie, urls, R.drawable.no_images, R.drawable.no_images) ;
		holder.tv_target_patient_get_des.setVisibility(View.GONE);
		holder.tv_target_patient_get.setVisibility(View.GONE);
		holder.tv_target_patient_get.setText("计划第" + su.dateSeq + "天");
		holder.iv_exchange.setVisibility(View.GONE);
		holder.tv_target_patient_sellinfo_des.setVisibility(View.GONE);
		holder.tv_eat_suggest_sellinfo.setVisibility(View.GONE);

			holder.tv_package.setText(su.days + periodDay);
			holder.tv_package_des.setText("【用药天数】");



		holder.rl_meidicineinfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, MedieSpecificationActivity.class);
				if (null != su.drug) {
					intent.putExtra("id", su.drug.id);
				} else {
					intent.putExtra("id", su.drugid);
				}

				String goodsname = "";
				if (null != su.drug) {
					goodsname = su.drug.title;
				} else if (null != su.title) {
					goodsname = su.title;
				}
				String name = CompareDatalogic.getShowName(su.general_name, su.trade_name, goodsname);
				intent.putExtra("name", name);
				context.startActivity(intent);
			}
		});
		if (arg0>0){
			  patientSuggest suprevice = (patientSuggest) getItem(arg0-1);
			if (!TextUtils.isEmpty(suprevice.takeMedicalTime)&&!TextUtils.isEmpty(su.takeMedicalTime)){
				if (suprevice.takeMedicalTime.trim().equals(su.takeMedicalTime.trim())){
					holder.begindate.setVisibility(View.GONE);
				}else {
					holder.begindate.setVisibility(View.VISIBLE);
				}
			}
		}else {
			holder.begindate.setVisibility(View.VISIBLE);
		}
		holder.begindate.setVisibility(View.VISIBLE);
		if (!TextUtils.isEmpty(su.takeMedicalTime)){
			SpannableString spannableString = com.dachen.mediecinelibraryrealize.utils.StringUtils.getStringDifSizeCare("开始服用时间:"+su.takeMedicalTime,7,context);
			holder.begindate.setText("开始服用时间:"+su.takeMedicalTime);
			if (arg0 == 0){
				holder.begindate.setVisibility(View.VISIBLE);
			}else if (arg0>0){
				patientSuggest su1 = c_patient_drug_suggest_list.get(arg0-1);


				if (!TextUtils.isEmpty(su1.takeMedicalTime)&&
						!TextUtils.isEmpty(su.takeMedicalTime)&&
						su1.takeMedicalTime.equals(su.takeMedicalTime)){
					holder.begindate.setVisibility(View.GONE);
					holder.rl_begin_data.setVisibility(View.VISIBLE);
				}else {
					holder.begindate.setVisibility(View.VISIBLE);
				}
			}
		}else {
			//holder.begindate.setVisibility(View.GONE);
			holder.begindate.setText("第" + su.dateSeq + "天");
			if (arg0 == 0){
				holder.begindate.setVisibility(View.VISIBLE);
			}else if (arg0>0){
				patientSuggest su1 = c_patient_drug_suggest_list.get(arg0-1);
				if (su1.dateSeq == su.dateSeq){
					holder.begindate.setVisibility(View.GONE);
				}else {
					holder.begindate.setVisibility(View.VISIBLE);
				}
			}

		}

		// TODO Auto-generated method stub
		return view;
	}
	public static class ViewHolder{
		TextView tv_name;
		TextView tv_weight;
		TextView tv_unit;
		TextView tv_num;
		TextView tv_company;
		TextView tv_eat_suggest;
		TextView tv_target_patient_type;
		RelativeLayout rl_upordown;
		LinearLayout rl_upordown_show;
		ImageView iv_down;
		RelativeLayout rl_meidicineinfo;
		TextView tv_package;
		TextView tv_package_des;
		TextView tv_target_patient_get_des;
		TextView tv_target_patient_get;
		TextView tv_eat_suggest_sellinfo;
		ImageView iv_exchange;
		ImageView tv_easy_sell;
		TextView tv_target_patient_sellinfo_des;
		TextView begindate;
		ImageView iv_medie;
		RelativeLayout rl_begin_data;
	}
}
