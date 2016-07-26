package com.dachen.mediecinelibraryrealize.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.medicine.common.utils.Alarm;
import com.dachen.medicine.common.utils.DrugRemind;
import com.dachen.mediecinelibraryrealize.R;

import java.util.Collection;
import java.util.List;

public class AdapterShowAlarm extends BaseAdapter{
	Context context;
	List<DrugRemind>  info_list;
	public AdapterShowAlarm(Context context,List<DrugRemind> info_list){
		this.context = context;
		this.info_list = info_list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return info_list.size();
	}
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return info_list.get(arg0);
	}
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		DrugRemind info = (DrugRemind) getItem(arg0);
		if (null == view) { 
			view = View.inflate(context, R.layout.adapter_showalerm, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			holder.tv_time = (TextView) view.findViewById(R.id.tv_time); 
			holder.tv_lastnum = (TextView) view.findViewById(R.id.tv_lastnum);
			holder.iv_alarm = (ImageView) view.findViewById(R.id.iv_alarm);
			view.setTag(holder);
		}else {
			holder = (ViewHolder) view.getTag();
		}
		if (null != info.drugName) {
			holder.tv_name.setText(info.drugName);
		}
		if (null != info.alarms&&info.alarms.size()>0) {  
			String time ="";
			/*for (int i = 0; i < info.alarms.size(); i++) {
				List<Alarm> alarms =  (List<Alarm>) info.alarms;
				Alarm times = alarms.get(i); 
				time += times.hour+":"+times.minute;
				if (i!=info.alarms.size()-1) {
					time = time+"、";
				} 
			}*/
			//Alarm alarm = getNextAlarm(info.alarms, info.repeatPeriodIndex);
		/*	time = TimeUtils.getSimpleDate(TimeUtils.getNextAlarmTimeInMillis
					(alarm.hour, alarm.minute, info.repeatPeriodIndex)));*/
		//	holder.tv_time.setText(String.format("%02d:%02d", alarm.hour, alarm.minute));
			if (null!=info.alarms) {
				holder.tv_time.setText(getAllAlarm(info.alarms));
			} 
//			if (!TextUtils.isEmpty(info.goods_dqsy)&&!info.goods_dqsy.contains("-")){
//				holder.tv_lastnum.setVisibility(View.VISIBLE);
//				holder.tv_lastnum.setText("("+info.goods_dqsy+")");
//			} else {
//				holder.tv_lastnum.setVisibility(View.GONE);
//			}
			holder.tv_lastnum.setVisibility(View.GONE);
			if (info.isRemind) {
				holder.iv_alarm.setBackgroundResource(R.drawable.openalarm);
			}else {
				holder.iv_alarm.setBackgroundResource(R.drawable.closealarm);
			}
			try {
				if (!TextUtils.isEmpty(info.goods_dqsy)&&Integer.parseInt(info.goods_dqsy)<=10) {
					holder.tv_lastnum.setTextColor(context.getResources().getColor(R.color.color_f74e5b));
				}else {
					holder.tv_lastnum.setTextColor(context.getResources().getColor(R.color.color_333333));
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
		}
		return view;
	}
	public static class ViewHolder{
		TextView tv_name;
		TextView tv_time;
		TextView tv_lastnum;
		ImageView iv_alarm;
	}
	
/*	private Alarm getNextAlarm(Collection<Alarm> alarms, int repeatPeriod) {
		long minTime = Long.MAX_VALUE;
		Alarm nextAlarm = null;
		for (Alarm alarm : alarms) {
			long time = TimeUtils.getNextAlarmTimeInMillis(alarm.hour, alarm.minute, repeatPeriod);
			if (time < minTime) {
				minTime = time;
				nextAlarm = alarm;
			}
		}
		return nextAlarm;
	}*/
	private String getAllAlarm(Collection<Alarm> alarms) {
		long minTime = Long.MAX_VALUE;
		Alarm nextAlarm = null;
		String alarmss = "";
		int i = 0;
		for (Alarm alarm : alarms) {
			if (i!=0) {
				alarmss += "、";
			}
			alarmss += String.format("%02d:%02d", alarm.hour, alarm.minute);
			i++;
		} 
		
		return alarmss;
	}
}
