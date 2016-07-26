package com.dachen.mediecinelibraryrealize.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;
import com.baoyz.actionsheet.ActionSheet.ActionSheetListener;
import com.dachen.medicine.common.utils.Alarm;
import com.dachen.medicine.common.utils.DrugRemind;
import com.dachen.medicine.common.utils.MedicineApplication;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.adapter.AdapterShowAlarm;
import com.dachen.mediecinelibraryrealize.entity.AlarmBusiness;
import com.dachen.mediecinelibraryrealize.entity.AlarmDao;
import com.dachen.mediecinelibraryrealize.entity.AlarmInfo;
import com.dachen.mediecinelibraryrealize.entity.Alarms;
import com.dachen.mediecinelibraryrealize.entity.DrugRemindDao;
import com.dachen.mediecinelibraryrealize.utils.GetDataFromServerUtils;
import com.dachen.mediecinelibraryrealize.utils.TimeUtils;

public class ShowAlertActivity extends BaseActivity implements OnClickListener,ActionSheetListener,  OnHttpListener {
 
	TextView tv_title;
	RelativeLayout rl_back;
	TextView tv_interval;
	TextView tv_continue;
	TextView tv_alert_time;
	TextView tv_everytime_eat;
	TextView tv_lastnum;
	TextView tv_choice;
	private int mSoundIndex = 2;
	private DrugRemind mDrugRemind;
	private boolean mIsCreate;
	TextView tv_alarm;
	TextView tv_begin_data;
	RelativeLayout  rl_plus;
	List<AlarmInfo>  info_list;
	String patientId = "";
	int remindInterval = 1;
	int keep = 1;
	AdapterShowAlarm adapter;
	ListView listview;
	String patientName ="";
	String save;
	RelativeLayout rl_des;
	List<DrugRemind> lists;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case 0:
					adapter = new AdapterShowAlarm(ShowAlertActivity.this,lists);
					listview.setAdapter(adapter);
					getPatient();
					break;
				case 1:
					adapter = new AdapterShowAlarm(ShowAlertActivity.this,lists);
					listview.setAdapter(adapter);
					closeLoadingDialog();
					break;
			}

		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_showalert);
		new BdLocationHelper(this);
		patientId = getIntent().getStringExtra("Patientid"); 
		patientName = getIntent().getStringExtra("Patientname");
		save = getIntent().getStringExtra("save");
		initView();
		initData();

	}
	void initView(){ 
		RelativeLayout ll_sub = (RelativeLayout) findViewById(R.id.ll_sub);
		ViewStub vstub_title = (ViewStub) findViewById(R.id.vstub_title);
		View view = vstub_title.inflate(this, R.layout.layout_plus_time, ll_sub); 
		rl_plus = (RelativeLayout) view.findViewById(R.id.rl_plus); 
		rl_plus.setOnClickListener(this); 
		tv_begin_data = (TextView) findViewById(R.id.tv_begin_data);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(patientName+"用药提醒");
		tv_title.setOnClickListener(this);
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this); 
		listview = (ListView) findViewById(R.id.listview);
		rl_des = (RelativeLayout) findViewById(R.id.rl_des);

	}
	void initData(){
		
		info_list = new ArrayList<AlarmInfo>();
		showLoadingDialog();
	new Thread(new Runnable() {
		@Override
		public void run() {

			 lists = DrugRemindDao.getInstance(ShowAlertActivity.this).queryByPatientId(patientId);
			if (null==lists) {
				lists = new ArrayList<DrugRemind>();
			}
			handler.sendEmptyMessage(0);
		}
	}).start();

		lists = new ArrayList<DrugRemind>();
		//ToastUtils.showToast(ShowAlertActivity.this,lists.size()+"-------");
		adapter = new AdapterShowAlarm(this,lists);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				DrugRemind info = (DrugRemind) adapter.getItem(arg2);
				// TODO Auto-generated method stub
				Intent intent = new Intent(ShowAlertActivity.this, SetAlertActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("patient", (Serializable) info);
				intent.putExtra("patient", bundle);
				intent.putExtra("Patientid", patientId);
				intent.putExtra("Patientname", patientName);
				startActivityForResult(intent, 100);
			}
		});
		

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 if (v.getId() == R.id.rl_plus) {
		 
				// TODO Auto-generated method stub
				Intent intent = new Intent(ShowAlertActivity.this,SetAlertActivity.class);
				intent.putExtra("Patientid", patientId); 
				intent.putExtra("Patientname", patientName);
				startActivityForResult(intent,100);
			//getPatient();
		
		}else if (v.getId() == R.id.rl_back) {
			onBackPressed();
		}else if (v.getId() == R.id.tv_title) {
			DrugRemindDao.getInstance(this).clearAll(patientId);
		}
	}
	public void getPatient(){
		showLoadingDialog(); 
		HashMap<String, String> maps = MedicineApplication.getMapConfig();
		if (null==maps) {  
			return;
		}
		//
		/*HashMap<String, String> interfaces = new HashMap<String, String>();
		//患者
		interfaces.put("patient", patientId);  
		new HttpManager().post (this,
				Params.getInterface("invoke", "c_remind_patient.select"),
				Alarms.class,
				interfaces,
		 		this,false, 2); */
		HashMap<String, String> interfaces = new HashMap<String, String>();
		//患者
		interfaces.put("patientId", patientId);
		interfaces.put("access_token",UserInfo.getInstance(this).getSesstion());
		new HttpManager().post (this,
				"org/drugReminder/getDoseReminderListByPatientId",
				Alarms.class,
				interfaces,
				this,false, 1);
		
	}
	 
	@Override
	public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onOtherButtonClick(ActionSheet actionSheet, int index) {
		// TODO Auto-generated method stub
		
	}
 
	@Override
	public void onFailure(Exception arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSuccess(Result arg0) {
		// TODO Auto-generated method stub
closeLoadingDialog();
		if (arg0 instanceof Alarms) { 


			List<DrugRemind>  reminds =DrugRemindDao.getInstance(this).queryByPatientId(patientId);
			final Alarms alarm = (Alarms)arg0;
			if (reminds!=null&&reminds.size()>=0){
				for (int i=0;i<reminds.size();i++){
					AlarmBusiness.cancelAlarms(this,reminds.get(i).alarms);
				}
			}
			DrugRemindDao.getInstance(this).clearAll(patientId);
			if (null!=alarm.data&&alarm.data.size()>0) {
				rl_des.setVisibility(View.GONE);
				new Thread(new Runnable() {
					@Override
					public void run() {
						List<DrugRemind> listsss = new ArrayList<>();

						//	DrugRemindDao.getInstance(this).clearAll(patientId);
						info_list = alarm.data;

						DrugRemindDao.getInstance(ShowAlertActivity.this).clearByID("##112233##", patientId);

						for (int i = 0; i < info_list.size(); i++) {

							AlarmInfo info = info_list.get(i);
							/*List<DrugRemind> haveremind =
									DrugRemindDao.getInstance(ShowAlertActivity.this).
											queryByids(info.id, info.patientId);
							DrugRemind remind = new DrugRemind();
							if (null!=haveremind&&haveremind.size()>0){
								remind._id = haveremind.get(0)._id;
							}else {
								remind._id = -1;
							}
							String begindate = "";
							if (TextUtils.isEmpty(info.start_date)) {
								begindate = com.dachen.medicine.common.
										utils.TimeUtils.getTime(Long.parseLong(info.start_date));
							}
							remind.begindata = begindate;
							remind.createTime = Long.parseLong(info.number);
							if (null!= info.goods) {
								remind.drugName = info.goods.title;

								remind.idmedie=info.goods.id;
							}else {
								remind.idmedie=info.goodsId;
								remind.drugName = info.goodsName;
							}
							remind.goods_dqsy=info.goods_dqsy;
							remind.goods_mcfy=info.goods_mcfy;
							remind.ownerUserId= UserInfo.getInstance(ShowAlertActivity.this).getId()+"";
							remind.patientId=info.patientId;
							if(info.status ==1){
								remind.isRemind = true;
							}else {
								remind.isRemind = false;
							}
							remind.patientName = patientName;
							remind.repeatDayIndex=info.days_remind_cx;
							remind.repeatPeriodIndex=info.days_remind_jg;
							remind.id=info.id;
							remind.soundDesc = info.remind_ly;
							for (int j = 0; j < SetAlertActivity.mSoundDescs.size(); j++) {
								if (SetAlertActivity.mSoundDescs.get(j).contains(info.remind_ly)) {
									remind.soundName = SetAlertActivity.mSoundNames.get(j);
									remind.soundIndex = j;
									break;
								}
							}
							Collection<Alarm> alarms = new ArrayList<Alarm>();
						//	for (int j2 = 0; j2 < info.times_remind_oneday.size(); j2++) {
							if (!TextUtils.isEmpty(info.reminderTime1)&&info.reminderTime1.length()>=3) {
								String times = info.reminderTime1;
								;
								Alarm ala1 = new Alarm();
								ala1.drugRemind = remind;
								ala1.hour = Integer.parseInt(times.substring(0, 2));
								ala1.minute = Integer.parseInt(times.substring(3));
								ala1.index = 1;
								ala1.number = 1;
								ala1.times = 0;
								ala1.eat = 0;
								ArrayList<Alarm> alarmupdates = (ArrayList<Alarm>) AlarmDao.getInstance(ShowAlertActivity.this).
										queryByCreateTime(ala1);
								if (null != alarmupdates && alarmupdates.size() > 0) {
									ala1._id = alarmupdates.get(0)._id;
								} else {
									ala1._id = -1;
								}
								alarms.add(ala1);
							}
							if (!TextUtils.isEmpty(info.reminderTime2)&&info.reminderTime2.length()>=3) {
								String time2 = info.reminderTime2;
								;
								Alarm ala2 = new Alarm();
								ala2.drugRemind = remind;
								ala2.hour = Integer.parseInt(time2.substring(0, 2));
								ala2.minute = Integer.parseInt(time2.substring(3));
								ala2.index = 2;
								ala2.number = 2;
								ala2.times = 0;
								ala2.eat = 0;
								ArrayList<Alarm> alarmupdates2 = (ArrayList<Alarm>)
										AlarmDao.getInstance(ShowAlertActivity.this).queryByCreateTime(ala2);
								if (null != alarmupdates2 && alarmupdates2.size() > 0) {
									ala2._id = alarmupdates2.get(0)._id;
								} else {
									ala2._id = -1;
								}
								alarms.add(ala2);

							}
							if (!TextUtils.isEmpty(info.reminderTime3)&&info.reminderTime3.length()>=3) {
								String time3 = info.reminderTime3;
								Alarm ala3 = new Alarm();
								ala3.drugRemind = remind;
								ala3.hour = Integer.parseInt(time3.substring(0, 2));
								ala3.minute = Integer.parseInt(time3.substring(3));
								ala3.index = 3;
								ala3.number = 3;
								ala3.times = 0;
								ala3.eat = 0;
								ArrayList<Alarm> alarmupdates3 = (ArrayList<Alarm>)
										AlarmDao.getInstance(ShowAlertActivity.this).
										queryByCreateTime(ala3);
								if (null != alarmupdates3 && alarmupdates3.size() > 0) {
									ala3._id = alarmupdates3.get(0)._id;
								} else {
									ala3._id = -1;
								}
								alarms.add(ala3);
							}
							if (!TextUtils.isEmpty(info.reminderTime4)&&info.reminderTime4.length()>=3){
								String time4 = info.reminderTime4;;
								Alarm ala4 = new Alarm();
								ala4.drugRemind = remind;
								ala4.hour = Integer.parseInt(time4.substring(0,2));
								ala4.minute = Integer.parseInt(time4.substring(3));
								ala4.index = 4;
								ala4.number = 4;
								ala4.times = 0;
								ala4.eat = 0;
								ArrayList<Alarm>	alarmupdates4 = (ArrayList<Alarm>) AlarmDao.getInstance(ShowAlertActivity.this).
										queryByCreateTime(ala4);
								if (null!=alarmupdates4&&alarmupdates4.size()>0){
									ala4._id=alarmupdates4.get(0)._id;
								}else {
									ala4._id=-1;
								}
								alarms.add(ala4);
							}




							//}
							remind.alarms = alarms;
							listsss.add(remind);

							List<DrugRemind> druglist = DrugRemindDao.getInstance(ShowAlertActivity.this).
									queryByids(info.id, patientId);
							AlarmBusiness.setAlarms(ShowAlertActivity.this, (List<Alarm>) alarms);
							if (druglist == null || druglist.size() == 0) {

								DrugRemindDao.getInstance(ShowAlertActivity.this).addOrUpdate(remind);
								for (Alarm alarm1 : remind.alarms) {
									AlarmDao.getInstance(ShowAlertActivity.this).createOrUpdate(alarm1);

								}

							} else {
							}*/
							GetDataFromServerUtils.processData(ShowAlertActivity.this,info,true);
						}
						List<DrugRemind> listsdb = DrugRemindDao.getInstance(ShowAlertActivity.this).queryByPatientId(patientId);

						lists.clear();
						lists.addAll(listsdb);
					 handler.sendEmptyMessage(1);
					}
				}).start();


			}else {
				closeLoadingDialog();
				rl_des.setVisibility(View.VISIBLE);
			}
		}else {
			closeLoadingDialog();
			rl_des.setVisibility(View.VISIBLE);
		}
	} 
	@Override
	public void onSuccess(ArrayList arg0) {
		// TODO Auto-generated method stub
		
	}  
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(!TextUtils.isEmpty(save)){
		/*	MActivityManager.getInstance().finishActivity(PatientMedieBoxActivity2.class);
			LogUtils.burtLog("save11=="+save);
			finish();*/
			Intent intent = new Intent(ShowAlertActivity.this,PatientMedieBoxActivity2.class); 
			ShowAlertActivity.this.setResult(1, intent);
		}else {
			finish();
		}
//		this.mApplication.getActivityManager().finishActivity(this.getClass());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		//if (null!=data) {
			//patientId = data.getStringExtra("Patientid"); 
			//patientName = data.getStringExtra("Patientname");
			//save = data.getStringExtra("save");
			initView();
			initData();
			super.onActivityResult(requestCode, resultCode, data);
		//}
	}
}
