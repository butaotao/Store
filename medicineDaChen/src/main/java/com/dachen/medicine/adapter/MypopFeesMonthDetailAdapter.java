package com.dachen.medicine.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;

import com.dachen.medicine.R;
import com.dachen.medicine.entity.MyFeesDetails;
import com.dachen.medicine.entity.MyFeesDetails.MyFees;
import com.dachen.medicine.entity.MyFeesMonthDetails;
import com.dachen.medicine.logic.SubString;

public class MypopFeesMonthDetailAdapter extends BaseCustomAdapter<MyFeesMonthDetails.MyFees>{

	 Context context;

	public MypopFeesMonthDetailAdapter(Context context, int resId,
			List<MyFeesMonthDetails.MyFees> objects) {
		super(context, resId, objects);
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
		MyFeesMonthDetails.MyFees myfees = getItem(position);
		if (myfees.isMon == true) {
			holder.tv_data.setText(myfees.calendar[0]+"年"+myfees.calendar[1]+"月");
			holder.rl_item1.setVisibility(View.VISIBLE);
			holder.rl_item2.setVisibility(View.GONE);
		}else {
			holder.tv_time.setText(myfees.calendar[1]+"-"+myfees.calendar[2]+" "+myfees.calendar[5]+":"+myfees.calendar[6]);
			holder.tv_buynum.setText("售出"+SubString.getMediedata(myfees.drug_code));
			
			holder.rl_item1.setVisibility(View.GONE);
			holder.rl_item2.setVisibility(View.VISIBLE);
			holder.tv_auto.setText(myfees.fee + "元");
			/*if (null!=myfees.state&&!TextUtils.isEmpty(myfees.state.value)) {
			
			if (myfees.state.value.equals("1")) {
				holder.tv_auto.setTextColor(context.getResources().getColor(R.color.color_cccccc));
				holder.tv_auto.setText("待审批");
			}else if (myfees.state.value.equals("9")) {//
				holder.tv_auto.setTextColor(context.getResources().getColor(R.color.color_5bc7b9));
				holder.tv_auto.setText("已审批");
			}
			
			}*/
		}

	}

	public class ViewHolder extends BaseViewHolder{
		
		
		
		@Bind(R.id.tv_auto)
		TextView tv_auto;
		@Bind(R.id.tv_time)
		TextView tv_time;
		@Bind(R.id.tv_data)
		TextView tv_data;
		@Bind(R.id.tv_buynum)
		TextView tv_buynum;
		@Bind(R.id.rl_item1)
		RelativeLayout rl_item1;
		@Bind(R.id.rl_item2)
		RelativeLayout rl_item2;
	}
	private Spanned getTimeSpan(String num,String money) { 
		Spanned dTimeStr; 
			dTimeStr = Html.fromHtml(
					 "<font color=\"#aaaaaa\">" + "售出" + "</font>"
					+"<font color=\"#333333\">"+num +"</font>"
					+"<font color=\"#aaaaaa\">" + "盒    " + "</font>"
					+"<font color=\"#ff9d6a\">"+money +"</font>"
					+"<font color=\"#aaaaaa\">" + " 元" + "</font>"); 
		return dTimeStr; 
	}
}
