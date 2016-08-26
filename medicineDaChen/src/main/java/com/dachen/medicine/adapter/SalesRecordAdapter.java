package com.dachen.medicine.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;

import com.dachen.medicine.R;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.entity.Info;

public class SalesRecordAdapter extends BaseCustomAdapter<Info> {
	List<Info> objects;
	String isopen = "-1";
	Context context;
	public SalesRecordAdapter(Context context, int resId, List<Info> objects) {
		super(context, resId, objects);
		// TODO Auto-generated constructor stub
		this.objects = objects;
		isopen = SharedPreferenceUtil.getString("isopen_" + SharedPreferenceUtil.getString("id", ""), "1");
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

		final Info media = getItem(position);
		// LogUtils.burtLog("media.isMon=="+media.isMon);
		if (media.isMon) {
			holder.ll_month.setVisibility(View.VISIBLE);
			holder.rl_infodeail.setVisibility(View.GONE);
			holder.tv_month.setText((media.calendar[1]) + "月");
		} else {
			holder.ll_month.setVisibility(View.GONE);
			holder.rl_infodeail.setVisibility(View.VISIBLE);
			holder.tv_data.setText(media.calendar[2]);
			holder.tv_week.setText("周" + media.calendar[3]);
			if (media.buyType==1) {
				holder.tv_selled.setText("赠送");
			}else {
				holder.tv_selled.setText("售出");
			}
			 
			holder.iv_circle.setBackgroundResource(R.drawable.record_line);
			
			
			
			String name  = "";
			if (isopen.equals("1")) {
				name = media.drug$general_name;
				if (name.equals("")) {
					name = 	media.drug$trade_name;
				}
			}else {
				name = 	media.drug$trade_name;
				if (name==null || name.equals("")) {
					name = 	media.drug$general_name;
				}
			}
			if (null != name && name.length() > 9) {
			name = name.substring(0, 6) + "..."
					+ name.substring(name.length() - 2);
			} 
			holder.tv_name.setText(name);
			if (!"0".equals(media.quantity+"")) {
				holder.tv_num.setVisibility(View.VISIBLE);
				//holder.tv_num.setText(media.quantity + " " + media.drug$unit.title);
				String showmessage;
				showmessage = media.quantity+"";
				if (null!=media.drug$unit){
					showmessage = showmessage+ media.drug$unit.title;
				}
				holder.tv_num.setText(showmessage);
			}else {
				holder.tv_num.setVisibility(View.GONE);
			}
			
			//LogUtils.burtLog("currentTimeMillis=="+System.currentTimeMillis()+"=="+Long.parseLong(media.calendar[4])+"=="+((System.currentTimeMillis() - Long.parseLong(media.calendar[4]+""))<1000*60*60*2));
			if ((System.currentTimeMillis() - Long.parseLong(media.calendar[4]+""))<1000*60*60*2) {
				holder.tv_week.setTextColor(context.getResources().getColor(R.color.color_ff8948));
				holder.tv_data.setTextColor(context.getResources().getColor(R.color.color_ff8948));
				holder.tv_week.setTextColor(context.getResources().getColor(R.color.color_ff8948));
				holder.tv_selled.setTextColor(context.getResources().getColor(R.color.color_ff8948)); 
				holder.iv_circle.setBackgroundResource(R.drawable.orange_circle_selected);
				holder.tv_name.setTextColor(context.getResources().getColor(R.color.color_ff8948));
				holder.tv_num.setTextColor(context.getResources().getColor(R.color.color_ff8948));
			}else { 
				holder.tv_data.setTextColor(context.getResources().getColor(R.color.color_9eaaaa));
				holder.tv_week.setTextColor(context.getResources().getColor(R.color.color_9eaaaa));
				holder.tv_selled.setTextColor(context.getResources().getColor(R.color.color_333333)); 
				holder.iv_circle.setBackgroundResource(R.drawable.record_line);
				holder.tv_name.setTextColor(context.getResources().getColor(R.color.color_333333));
				holder.tv_num.setTextColor(context.getResources().getColor(R.color.color_333333));
			}
			if (null!=media.state&&null!=media.state.value&&media.state.value.equals("9")) {
				holder.iv_money.setVisibility(View.VISIBLE);
			}else {
				holder.iv_money.setVisibility(View.GONE);
			}
		}
	}

	public static class ViewHolder extends BaseViewHolder {
		@Bind(R.id.rl_infodeail)
		RelativeLayout rl_infodeail;
		@Bind(R.id.ll_month)
		LinearLayout ll_month;
		@Bind(R.id.tv_month)
		TextView tv_month;
		@Bind(R.id.tv_data)
		TextView tv_data;
		@Bind(R.id.tv_week)
		TextView tv_week;
		@Bind(R.id.tv_selled)
		TextView tv_selled;
		@Bind(R.id.tv_name)
		TextView tv_name;
		@Bind(R.id.tv_num)
		TextView tv_num;
		@Bind(R.id.iv_circle)
		ImageView iv_circle;
		@Bind(R.id.iv_arrow)
		ImageView iv_arrow;
		@Bind(R.id.iv_money)
		ImageView iv_money;
	}
}
