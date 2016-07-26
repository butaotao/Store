package com.dachen.mediecinelibraryrealize.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dachen.medicine.common.utils.Alarm;
import com.dachen.medicine.common.utils.DrugRemind;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.TimeUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.activity.TimeDetailNotifactionActivity.CloseActivity;
import com.dachen.mediecinelibraryrealize.entity.AlarmBusiness;
import com.dachen.mediecinelibraryrealize.entity.AlarmDao;
import com.dachen.mediecinelibraryrealize.entity.AlarmsTime.AlarmsTimeInfo;
import com.dachen.mediecinelibraryrealize.entity.DrugRemindDao;
import com.dachen.mediecinelibraryrealize.entity.Patients;
import com.dachen.mediecinelibraryrealize.entity.SoundPlayer;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class AdapterTimeDetailNotifacation extends BaseAdapter implements OnHttpListener{
	Context context;
	List<AlarmsTimeInfo> infos;
	AlarmsTimeInfo info;
	CloseActivity close;
	Alarm mAlarm;
	SoundPlayer player ;
	public AdapterTimeDetailNotifacation(Context context, List<AlarmsTimeInfo> infos,Alarm mAlarm,CloseActivity close){
		this.context = context;
		this.infos = infos;
		this.close = close;
		this.mAlarm = mAlarm;
		player = new SoundPlayer(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infos.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return infos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		final ViewHolder holder;
		// TODO Auto-generated method stub
		final AlarmsTimeInfo info = infos.get(arg0);
		this.info = info;
		if (null==view) {
			view = View.inflate(context, R.layout.adapter_time_detailnotifacation, null);
			holder = new ViewHolder();
			holder.ll_time = (LinearLayout) view.findViewById(R.id.ll_time);
			holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
			holder.btn_step = (Button) view.findViewById(R.id.btn_step);
			holder.btn_eat = (Button) view.findViewById(R.id.btn_eat);
			holder.have_eat = (TextView) view.findViewById(R.id.have_eat);
			holder.btn_step_all = (Button) view.findViewById(R.id.btn_step_all);
			holder.btn_eat_all = (Button) view.findViewById(R.id.btn_eat_all);
			holder.name = (TextView) view.findViewById(R.id.name);
			holder.time = (TextView) view.findViewById(R.id.time);
			holder.rl_all = (RelativeLayout) view.findViewById(R.id.rl_all);
			holder.have_eat_all = (TextView) view.findViewById(R.id.have_eat_all);
			holder.view2 = view.findViewById(R.id.view2);
			holder.iv_eatimg = (ImageView) view.findViewById(R.id.iv_eatimg);
			view.setTag(holder);
		}else {
			holder = (ViewHolder) view.getTag();
		}
		holder.tv_time.setText(info.intergTime+"时");

		holder.iv_eatimg.setBackgroundResource(R.drawable.no_eat);

		holder.ll_time.setVisibility(View.GONE);
		//holder.time.setText(info.intergTime);

		holder.name.setText(info.goods_name);
		holder.time.setText(info.time);

		holder.btn_step.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub 

				mAlarm.isStep = System.currentTimeMillis();
				AlarmDao.getInstance(context).update(mAlarm);
				AlarmBusiness.cancelFiveNotification(context, mAlarm);
				AlarmBusiness.cancelNotification(context, mAlarm);
				AlarmBusiness.setAlarmRepeatFive(context, mAlarm);

				player.stop();
				Toast.makeText(context, "5分钟之后再为您提醒", 3000).show();

				if (info.times < 4) {
				}
				close.closeActivity();
			}
		});
		holder.btn_eat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				player.stop();
				// TODO Auto-generated method stub
				getPatient(mAlarm,info);
				mAlarm.times = 0;
				mAlarm.eat = System.currentTimeMillis();
				AlarmDao.getInstance(context).update(mAlarm);
				AlarmBusiness.cancelFiveNotification(context, mAlarm);
				AlarmBusiness.cancelNotification(context, mAlarm);
				holder.btn_eat.setVisibility(View.VISIBLE);
				AlarmBusiness.cancelFiveNotification(context, mAlarm);

			}
		});




		return view;
	}


	public void getPatient(Alarm alarm ,AlarmsTimeInfo info){
		String s = "org/drugReminder/submitDoseRecordByReminderId";
		HashMap<String, String> interfaces = new HashMap<String, String>();
		interfaces.put("access_token", UserInfo.getInstance(context).getSesstion());
		interfaces.put("reminderRecord", "2");
		interfaces.put("reminderTime",info.time);
		interfaces.put("drugReminderId", alarm.drugRemind.id);
		new HttpManager().post(context,
				s,
				Patients.class,
				interfaces,
				this, false, 1);

	}
	public class ViewHolder{
		LinearLayout ll_time;
		TextView tv_time;
		Button btn_step;
		Button btn_eat;
		TextView have_eat;
		Button btn_step_all;
		Button btn_eat_all;
		RelativeLayout rl_all;
		TextView have_eat_all;
		TextView name;
		TextView time;
		View view2;
		ImageView iv_eatimg;
	}
	@Override
	public void onFailure(Exception arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onSuccess(Result arg0) {

		if (arg0!=null&&!TextUtils.isEmpty(arg0.resultMsg)&&arg0.resultMsg.contains( "success")) {
			ToastUtils.showToast(context,"已服用！");
			close.closeActivity();
		}
	}
	@Override
	public void onSuccess(ArrayList arg0) {
		// TODO Auto-generated method stub

	}

}
