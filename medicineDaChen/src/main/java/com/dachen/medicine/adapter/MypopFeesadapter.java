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
import com.dachen.medicine.entity.PopularizeData.Datas;

public class MypopFeesadapter  extends BaseCustomAdapter<Datas>{
	Context context;
	  private static final String REGEX_HTML = "<[^>]+>"; // 定义HTML标签的正则表达式  
	public MypopFeesadapter(Context context, int resId,
			List<Datas> datas) {
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
		Datas d= getItem(position);
		if (null!=d.goods) {
			holder.tv_name.setText(d.goods.title);
		}
	 	
		holder.tv_unit.setText(d.goods$pack_specification);
		holder.tv_money.setText(d.fee);
		holder.tv_company.setText(d.goods$manufacturer); 
	}
	public class ViewHolder extends BaseViewHolder{
		@Bind (R.id.tv_name)
		TextView tv_name;
		@Bind (R.id.tv_unit)
		TextView tv_unit;
		@Bind (R.id.tv_money)
		TextView tv_money;
		@Bind (R.id.tv_company)
		TextView tv_company;
	}
	private SpannableString getTimeSpan(String name,String unit) { 
		Spanned dTimeStr; 
			dTimeStr = Html.fromHtml(
					 "<font color=\"#333333\">" + name + "</font>"
					+"<font color=\"#aaaaaa\">"+unit +"</font>"); 
			 
			 int end = dTimeStr.length();  
		                SpannableString ss = new SpannableString(Html.fromHtml(dTimeStr.toString()));  
			                 URLSpan[] urls = ss.getSpans(0, end, URLSpan.class);  
			   
			                 String resultStr = dTimeStr.toString();  
			             Pattern p_html = Pattern.compile(REGEX_HTML, Pattern.CASE_INSENSITIVE);  
			                 Matcher m_html = p_html.matcher(resultStr);  
			                 resultStr = m_html.replaceAll(""); // 过滤html标签  
			                  SpannableString span = new SpannableString(resultStr);  
			                  for (URLSpan url : urls) {  
			                      int startIndex = ss.getSpanStart(url);  
			                      int endIndex = ss.getSpanEnd(url);  
			                     // 原字两倍大小  
			                     span.setSpan(new AbsoluteSizeSpan(16), startIndex, endIndex,  
			                             Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
			                      // 设置样式2  
			                      span.setSpan(new ForegroundColorSpan(Color.WHITE),  
			                             startIndex, endIndex,  
			                              Spannable.SPAN_EXCLUSIVE_INCLUSIVE);  
			                      // 设置样式3  
			                     span.setSpan(new StyleSpan(Typeface.BOLD),  
			                             startIndex, endIndex,  
			                             Spannable.SPAN_EXCLUSIVE_INCLUSIVE);  
			                }  

			
			
		return span;
		
	}
}
