package com.dachen.mediecinelibraryrealize.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.Alarm;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.TimeUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.adapter.AdapterTimeDetailNotifacation;
import com.dachen.mediecinelibraryrealize.entity.AlarmDao;
import com.dachen.mediecinelibraryrealize.entity.AlarmsTime;
import com.dachen.mediecinelibraryrealize.entity.AlarmsTime.AlarmsTimeInfo;

public class TimeDetailNotifactionActivity extends BaseActivity implements OnHttpListener,OnClickListener{
	List<AlarmsTimeInfo> infos;
	ListView listview;
	AdapterTimeDetailNotifacation adapter;
	TextView tv_title;
	RelativeLayout rl_back;
	TextView tv_data;
	int position;
	private Alarm mAlarm;
	TextView tv_time;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_detail);  
	 
			infos = new ArrayList<AlarmsTimeInfo>(); 
		//String period = getIntent().getStringExtra("period"); 
	/*		Bundle bundle = getIntent().getBundleExtra("alarm");
			mAlarm = (Alarm) bundle.getSerializable("alarm"); */
			Alarm mAlarm = (Alarm) getIntent().getSerializableExtra("alarm");
 
		//String pe = getIntent().getStringExtra("pe");

		List<Alarm>  alarmupdate = null ;
		try {
			alarmupdate =   AlarmDao.getInstance(this).getAlarms(mAlarm.hour, mAlarm.minute);
		}catch (Exception e){

		}



		listview = (ListView) findViewById(R.id.listview); 
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("服用药物"); 
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
	//	rl_back.setVisibility(View.GONE);
		AlarmsTime time = new AlarmsTime();
		AlarmsTimeInfo info =  time. new AlarmsTimeInfo();
		if (null!=mAlarm.drugRemind) {
			info.goods_name = mAlarm.drugRemind.drugName;
			info.id = mAlarm.drugRemind.id;
			info.times = mAlarm.times;
			info.eat = mAlarm.eat;
		}
		String s = String.format("%02d:%02d", mAlarm.hour, mAlarm.minute);
		info.time = s;
		/*if (alarmupdate!=null&&alarmupdate.size()>0){
			for (int i=0;i<alarmupdate.size();i++){
				String userid = mAlarm.drugRemind.ownerUserId;
				Alarm alarm =  new Alarm();
				alarm = alarmupdate.get(i);
				if (alarm.drugRemind.ownerUserId.equals(userid)){
					if (alarm.hour==mAlarm.hour&&alarm.minute == mAlarm.minute){
						if (getNextAlarmTimeInMillisThisAlarm(alarm.hour,alarm.minute)==
								getNextAlarmTimeInMillisThisAlarm(mAlarm.hour, mAlarm.minute)){
							AlarmsTime times = new AlarmsTime();
							AlarmsTimeInfo infoother =  time. new AlarmsTimeInfo();
							infoother.goods_name = alarm.drugRemind.drugName;
							infoother.id = alarm.drugRemind.id;
							infoother.times = alarm.times;
							infoother.eat = alarm.eat;
							String ss = String.format("%02d:%02d", mAlarm.hour, mAlarm.minute);
							infoother.time = ss;
							if (infoother.id!=info.id){
								infos.add(infoother);
							}

						}
					}
				}
			}
		}*/

		
		infos.add(info);
		adapter = new AdapterTimeDetailNotifacation(this, infos,mAlarm,new CloseActivity() {
			
			@Override
			public void closeActivity() {
				// TODO Auto-generated method stub
				finish();
			}
		});
		listview.setAdapter(adapter);
		tv_data = (TextView) findViewById(R.id.tv_data);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_data.setText(TimeUtils.getTimeDay2()+"  周"+TimeUtils.parseDate()[3]);
	 	tv_time.setText(info.time+" 您按时吃药了吗？");
	}

	public static long getNextAlarmTimeInMillis(int hour, int minute, String begindata,int interday) {
		Calendar calendar = Calendar.getInstance();
		long time = com.dachen.mediecinelibraryrealize.utils.TimeUtils.getDays(begindata);
		calendar.setTimeInMillis(time);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		// 如果闹钟的时间今天已过，则加上离它下一次铃响的天数,闹钟没有过期的前提下
		//if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
			calendar.add(Calendar.DAY_OF_YEAR, interday);
		//}
		long nowTime = System.currentTimeMillis();
		long alermTime = calendar.getTimeInMillis();
		return alermTime;
	}
	/**
	 * 返回当前时间
	 *
	 * @author butaotao
	 * @return "yyyy-MM-dd HH:mm:ss"格式的时间字符串
	 */
	public static String getTime() {
		// 使用默认时区和语言环境获得一个日历
		Calendar cale = Calendar.getInstance();
		// 将Calendar类型转换成Date类型
		Date tasktime = cale.getTime();
		// 设置日期输出的格式
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		// 格式化输出
		return df.format(tasktime);
	}
	public static long getNextAlarmTimeInMillisThisAlarm(int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		long time = com.dachen.mediecinelibraryrealize.utils.TimeUtils.getDays( getTime());
		calendar.setTimeInMillis(time);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		// 如果闹钟的时间今天已过，则加上离它下一次铃响的天数,闹钟没有过期的前提下
	/*	if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
		calendar.add(Calendar.DAY_OF_YEAR, interday);
		}*/
		long nowTime = System.currentTimeMillis();
		long alermTime = calendar.getTimeInMillis();
		return alermTime;
	}

	@Override
	public void onFailure(Exception arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSuccess(Result arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSuccess(ArrayList arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.rl_back) {
			/*MActivityManager.getInstance().finishActivity(PatientMedieBoxActivity2.class);
			Intent intent = new Intent(this,PatientMedieBoxActivity2.class);
			intent.putExtra("position", position);
			startActivity(intent);*/
			finish();
		}
	}
	public interface CloseActivity{
		public void closeActivity();
	};
}
