package com.dachen.medicine.adapter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.widget.TextView;
import butterknife.Bind;

import com.dachen.medicine.R;
import com.dachen.medicine.entity.MypoprecordDetails.MypoprecordDetail;
import com.dachen.medicine.entity.PopularizeData.Datas;
import com.dachen.medicine.logic.CompareData;

public class MypopFeesRecordDetailadapter  extends BaseCustomAdapter<MypoprecordDetail>{
	Context context;
	  private static final String REGEX_HTML = "<[^>]+>"; // 定义HTML标签的正则表达式  
	public MypopFeesRecordDetailadapter(Context context, int resId,
			List<MypoprecordDetail> datas) {
		super(context, resId, datas);
		// TODO Auto-generated constructor stub
		this.context = context;
		
	}

	@Override
	protected com.dachen.medicine.adapter.BaseCustomAdapter.BaseViewHolder getViewHolder() {
		// TODO Auto-generated method stub
		return new ViewHolder();
	}

	@Override
	protected void fillValues(
			com.dachen.medicine.adapter.BaseCustomAdapter.BaseViewHolder baseViewHolder,
			int position) {
		// TODO Auto-generated method stub
		ViewHolder holder = (ViewHolder) baseViewHolder;
		MypoprecordDetail d= getItem(position);
	 
	 	
		holder.tv_name.setText(CompareData.getName(d.goods$general_name, d.goods$trade_name, null));
		holder.tv_unit.setText(d.goods$pack_specification);
		holder.tv_company.setText(d.goods$manufacturer);
		holder.tv_money.setText(""+d.fee);
	}
	public class ViewHolder extends BaseViewHolder{
		@Bind (R.id.tv_name)
		TextView tv_name;
		@Bind (R.id.tv_unit)
		TextView tv_unit; 
		@Bind (R.id.tv_company)
		TextView tv_company; 
		@Bind (R.id.tv_money)
		TextView tv_money;
	}
	 
}
