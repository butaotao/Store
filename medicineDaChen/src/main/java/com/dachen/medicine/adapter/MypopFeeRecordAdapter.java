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
import com.dachen.medicine.entity.FeesRecords;
import com.dachen.medicine.entity.FeesRecords.FeesRecord;

public class MypopFeeRecordAdapter extends BaseCustomAdapter<FeesRecord>{

	public MypopFeeRecordAdapter(Context context, int resId,
			List<FeesRecord> objects) {
		super(context, resId, objects);
		// TODO Auto-generated constructor stub
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
		FeesRecord record = getItem(position);
		ViewHolder holder = (ViewHolder) baseViewHolder;
	 	holder.tv_buynum.setText(getTimeSpan(record.fee));
	  	holder.tv_time.setText(record.created_time);
	}
	public static class ViewHolder extends BaseViewHolder{
		@Bind(R.id.tv_time)
		TextView tv_time;
		@Bind(R.id.tv_buynum)
		TextView tv_buynum;
	}
	private Spanned getTimeSpan( String money) { 
		Spanned dTimeStr; 
			dTimeStr = Html.fromHtml( 
					"<font color=\"#aaaaaa\">" + "收到款项    " + "</font>"
					+"<font color=\"#ff9d6a\">"+money +"</font>"
					+"<font color=\"#aaaaaa\">" + " 元" + "</font>"); 
		return dTimeStr; 
	}
}
