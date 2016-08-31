package com.dachen.mediecinelibraryrealize.adapter;

import java.util.ArrayList;

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

import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.entity.SomeBox.patientSuggest;
import com.dachen.mediecinelibraryrealize.entity.SomeBox.patientSuggest.Uses;
import com.dachen.mediecinelibraryrealize.utils.CompareDatalogic;
import com.dachen.mediecinelibraryrealizedoctor.activity.MedieSpecificationActivity;

public class AdviceAdapter extends BaseAdapter{
	Context context;
	String list;
	ArrayList<patientSuggest> c_patient_drug_suggest_list;
	public AdviceAdapter(Context context,ArrayList<patientSuggest> c_patient_drug_suggest_list,String list){
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
			view = View.inflate(context, R.layout.adapter_adivice, null);
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

			view.setTag(holder);
			 
		}else {
			holder = (ViewHolder) view.getTag();
		}
		 final patientSuggest su = (patientSuggest) getItem(arg0);
		
		holder.tv_weight.setText(su.specification);
		String name = "";
	/*	name = su.general_name;
		if (TextUtils.isEmpty(name)) { 
			name = su.trade_name;
		} 
		if (null != name && name.length() > 9) {
			name = name.substring(0, 6) + "..."
					+ name.substring(name.length() - 2); 
		}*/
		String goodsname = "";
		if (null!=su.drug){
			goodsname = su.drug.title;
		}else if(null!=su.title){
			goodsname = su.title;
		}
		 name = CompareDatalogic.getShowName(su.general_name,su.trade_name,goodsname);
		holder.tv_name.setText(name);
		/*SpannableString names =
				CompareDatalogic.getShowName3(su.general_name, su.trade_name, goodsname,
						holder.tv_name, su.specification, context);
		holder.tv_name.setText(names);*/
		String unit = "";
		if (null!=su.unit&&null!=su.unit.name) {
			unit = su.unit.name;
		}
		holder.tv_unit.setText(unit);
		holder.tv_num.setText(su.requires_quantity);
		holder.tv_company.setText(su.manufacturer); 
		
		String alert = "";
		Uses users;
		String method="";
		String des ="";
		String patient = "";
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

			String eatinterval ="";
			Uses.Period p = users.period;
			if (!TextUtils.isEmpty(users.patients)){
				patient = users.patients+",";
			}

			if (null!=p&&!TextUtils.isEmpty(p.number)&&!times.equals("0")&&!p.number.equals("0")){
				eatinterval = ",每"+p.text+times + "次,";
			}

			if (!TextUtils.isEmpty(users.quantity)&&!users.quantity.equals("null")){
				quantity = ""+"每次"+users.quantity;
				if (!TextUtils.isEmpty(users.doseUnitName)){
					quantity = quantity+users.doseUnitName;
				}
				if (users.quantity.equals("0")){
					quantity = ""+"适量";
				}
			}


			if (null!=list){
				des =patient+ method +eatinterval +quantity;
			}else {
				des =patient+ method+eatinterval+quantity;
			}
			if (TextUtils.isEmpty(des)){
				holder.tv_target_patient_type.setVisibility(View.GONE);
				holder.tv_eat_suggest.setVisibility(View.GONE);
			}else {
				if (des.contains(",,")){
					des = des.replace(",,",",");
				}
				if (des.endsWith(",")&&des.length()>2){
					des = des.substring(0, des.length() - 1);
				}
				if (des.startsWith(",")&&des.length()>2){
					des = des.substring(1, des.length());
				}
				holder.tv_target_patient_type.setVisibility(View.VISIBLE);
				holder.tv_eat_suggest.setVisibility(View.VISIBLE);
			}
		} 
		if (su.flagOpen) {
			holder.rl_upordown_show.setVisibility(View.VISIBLE);
			holder.iv_down.setBackgroundResource(R.drawable.up);
		}else {
			holder.rl_upordown_show.setVisibility(View.GONE);
			holder.iv_down.setBackgroundResource(R.drawable.down);
		}
		holder.tv_eat_suggest.setText(des);


		holder.tv_target_patient_get_des.setVisibility(View.GONE);
		holder.tv_target_patient_get.setVisibility(View.GONE);
		  
		holder.iv_exchange.setVisibility(View.GONE);
		holder.tv_target_patient_sellinfo_des.setVisibility(View.GONE);
		holder.tv_eat_suggest_sellinfo.setVisibility(View.GONE);

		if (null!=list){
			if (TextUtils.isEmpty(su.days)){}
			holder.tv_package.setText(  su.days+"天");
			holder.tv_package_des.setText("【用药天数】");

			holder.tv_target_patient_get_des.setVisibility(View.VISIBLE);
			holder.tv_target_patient_get.setVisibility(View.VISIBLE);
			holder.tv_target_patient_get_des.setText("【备注】") ;

			holder.tv_target_patient_get.setText(method);
		}else {
			holder.tv_package.setText(su.pack_specification + "");
			holder.tv_package_des.setText("【包装规格】");

			holder.tv_target_patient_get_des.setVisibility(View.GONE);
			holder.tv_target_patient_get.setVisibility(View.GONE);
		}


		if (su.data!=null&&null!=su.data1) {
		
		if (  CompareDatalogic.isShow(su.data1.num_syjf,su.data.zyzdsxjfs,su.data.zsmdwypxhjfs)) {
			holder.iv_exchange.setVisibility(View.VISIBLE);

				holder.tv_unit.setText(unit);
			int buyNum = CompareDatalogic.getShowNum(su.data1.num_syjf,
					su.data.zyzdsxjfs, su.data.zsmdwypxhjfs);
			holder.tv_target_patient_get_des.setVisibility(View.VISIBLE);
			holder.tv_target_patient_get.setVisibility(View.VISIBLE);
			holder.tv_target_patient_get_des.setText("【获兑" + buyNum+unit+"药】") ;
			holder.tv_target_patient_get.setText("使用您"+su.data1.num_syjf+"积分可免费兑换"+buyNum+unit+"药");
		}
		
		}
		holder.rl_meidicineinfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			Intent intent = new Intent(context,MedieSpecificationActivity.class);
				if (null!=su.drug&&!TextUtils.isEmpty(su.drug.id)){
					intent.putExtra("id", su.drug.id);
				}else {
					intent.putExtra("id",su.drugid);
				}

				String goodsname = "";
				if (null!=su.drug){
					goodsname = su.drug.title;
				}else if(null!=su.title){
					goodsname = su.title;
				}
			String name =	CompareDatalogic.getShowName(su.general_name,su.trade_name,goodsname);
			intent.putExtra("name", name);
			context.startActivity(intent);
			}
		});
		holder.rl_upordown.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (su.flagOpen) {
					su.flagOpen = false;
					holder.rl_upordown_show.setVisibility(View.GONE);
					holder.iv_down.setBackgroundResource(R.drawable.down);
				}else {
					su.flagOpen = true;
					holder.rl_upordown_show.setVisibility(View.VISIBLE);
					holder.iv_down.setBackgroundResource(R.drawable.up);
				}
				//setListViewHeightBasedOnChildren(holder.lv_list);
				notifyDataSetChanged();
			}
		});

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
	}
}
