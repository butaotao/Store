package com.dachen.medicine.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import butterknife.Bind;

import com.dachen.medicine.R;
import com.dachen.medicine.common.utils.StringUtils;
import com.dachen.medicine.common.utils.TimeUtils;
import com.dachen.medicine.entity.MedicineEntity.MedicineInfo;
import com.dachen.medicine.entity.Policy;
import com.dachen.medicine.logic.CompareData;

public class PolicyAdapter extends BaseCustomAdapter{
	Context context;
	ArrayList<Policy> list_data;
	@SuppressWarnings("unchecked")
	public PolicyAdapter(Context context, int resId, List objects) {
		super(context, resId, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list_data = (ArrayList<Policy>) objects;
	}

	@Override
	protected BaseViewHolder getViewHolder() {
		// TODO Auto-generated method stub
		return new ViewHolder();
	}

	@Override
	protected void fillValues(BaseViewHolder baseViewHolder, int position) {
		// TODO Auto-generated method stub
		Policy info = (Policy) getItem(position);
		ViewHolder holder = (ViewHolder) baseViewHolder;

		String name = CompareData.getName(info.goodsTitle,info.goodsTitle,null);

		 StringUtils.get(holder.tv_name, name, info.packSpecification, context);

	//	holder.tv_unit.setText(info.goods$pack_specification);
		holder.tv_company.setText(info.goodsCompanyId);
		String start_date = TimeUtils.getTime(info.startDate);
		String end_date = TimeUtils.getTime(info.endDate);
		holder.tv_time.setText(start_date + "至" + end_date);
		java.text.DecimalFormat   df=new   java.text.DecimalFormat("0.00");

		holder.tv_money.setText(df.format(info.money/100)+"");

	}
	public class ViewHolder extends BaseViewHolder{
		@Bind(R.id.tv_name)
		TextView tv_name;
	/*	@Bind(R.id.tv_unit)
		TextView tv_unit;*/
		@Bind(R.id.tv_company)
		TextView tv_company;
		@Bind(R.id.tv_money_unit)
		TextView tv_money_unit;
		@Bind(R.id.tv_time)
		TextView tv_time;
		@Bind(R.id.tv_money)
		TextView tv_money;
	}



}
