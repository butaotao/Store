package com.dachen.mediecinelibraryrealize.activity;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;
import com.baoyz.actionsheet.ActionSheet.ActionSheetListener;
import com.bigkoo.pickerview.OptionsPickerView;
import com.dachen.medicine.common.utils.Alarm;
import com.dachen.medicine.common.utils.DrugRemind;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.MedicineApplication;
import com.dachen.medicine.common.utils.TimeUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.entity.User;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.activity.AlertTimeDialog.InterfaceGetData;
import com.dachen.mediecinelibraryrealize.activity.MyDialog.Dialogcallback;
import com.dachen.mediecinelibraryrealize.activity.RingAlertDialog.InterfaceGetSound;
import com.dachen.mediecinelibraryrealize.entity.AlarmBusiness;
import com.dachen.mediecinelibraryrealize.entity.AlarmStop;
import com.dachen.mediecinelibraryrealize.entity.ChoiceMedieEntity.MedieEntity;
import com.dachen.mediecinelibraryrealize.entity.DeteAlarm;
import com.dachen.mediecinelibraryrealize.entity.DrugRemindDao;
import com.dachen.mediecinelibraryrealize.entity.DrugRemindNoDataBase;
import com.dachen.mediecinelibraryrealize.entity.Patients;
import com.dachen.mediecinelibraryrealize.entity.SaveAlarm;
import com.dachen.mediecinelibraryrealize.entity.SoundPlayer;
import com.dachen.mediecinelibraryrealize.utils.AlarmUtil;
import com.dachen.mediecinelibraryrealize.utils.CompareDatalogic;
import com.dachen.mediecinelibraryrealize.utils.StringUtils;
import com.forexpand.datepicker.DatePicker;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
public class SetAlertActivity extends BaseActivity implements OnClickListener,ActionSheetListener,  OnHttpListener {
	RelativeLayout rl_choicemedie;
	RelativeLayout rl_lastnum;
	RelativeLayout rl_everytime_eat;
	RelativeLayout rl_begin_data;
	RelativeLayout rl_interval_data;
	RelativeLayout rl_continue_data;
	RelativeLayout rl_alert_time;
	RelativeLayout rl_alarm;
	Button btn_delete;
	Button btn_stop;
	TextView tv_title;
	private String mS1 = "--:--";
	RelativeLayout rl_back;
	//提醒间隔天数
	TextView tv_interval;
	//提醒持续时间
	TextView tv_continue;
	//每日提醒时间
	TextView tv_alert_time;
	//每次服用数量
	TextView tv_everytime_eat;
	//当前剩余药量
	TextView tv_lastnum;
	//药品名字
	TextView tv_choice;
	private int mSoundIndex = 2;
	private DrugRemindNoDataBase mDrugRemind;
	private boolean mIsCreate;
	long sleptime = 0;
	//铃声
	TextView tv_alarm;
	//开始服用日期
	TextView tv_begin_data;
	TextView alert_time;
	private String mMData;
	RelativeLayout  rl_plus;
	DrugRemind info;
	// 提醒时间
	public static final String[] REMIND_TIME = new String[49];
	// 重复周期
	public static final ArrayList<String> REPEAT_PERIOD = new ArrayList<String>();
	// 重复天数
	public static final ArrayList<String> REPEAT_DAY = new ArrayList<String>();

	// ###################开始############################//
	// 铃声名
	public static final List<String> mSoundNames = new ArrayList<String>();
	// 铃声描述
	public static final List<String> mSoundDescs = new ArrayList<String>();
	//设置时间
	public List<String> data;
	String patientId;
	int remindInterval = 0;
	int keep = 0;
	View line4;
	String Patientname;
	String goodsId = "";
	String alertid = "";
	public String goodName = "";
	LinearLayout ll_btncontrol;
	int[] mAlarmIndex;
	static {
		// 初始化提醒时间
		REMIND_TIME[0] = "--:--";
		for (int i = 1; i <= 24; i++) {
			REMIND_TIME[2 * i - 1] = String.format("%02d:00", i - 1);
			REMIND_TIME[2 * i] = String.format("%02d:30", i - 1);
		}

		// 初始化重复周期
//		REPEAT_PERIOD.add("不重复");
		REPEAT_PERIOD.add("每天提醒");
		REPEAT_PERIOD.add("每隔1天提醒");
		REPEAT_PERIOD.add("每隔2天提醒");
		REPEAT_PERIOD.add("每隔3天提醒");
		REPEAT_PERIOD.add("每隔4天提醒");
		REPEAT_PERIOD.add("每隔5天提醒");
		REPEAT_PERIOD.add("每隔6天提醒");
		REPEAT_PERIOD.add("每隔7天提醒");

		// 初始化重复天数
//		REPEAT_DAY.add("不重复");
		for (int i = 1; i <= 30; i++) {
			REPEAT_DAY.add(i + "天");
		}
		// ###################开始############################//
		// 初始化铃声
		mSoundNames.add("drug_remind_01");
		mSoundDescs.add("班得瑞钢琴曲");
		mSoundNames.add("drug_remind_02");
		mSoundDescs.add("传统好听的特效铃声");
		mSoundNames.add("drug_remind_03");
		mSoundDescs.add("单纯清脆和缓铃声");
		mSoundNames.add("drug_remind_04");
		mSoundDescs.add("经典铃声");
		mSoundNames.add("drug_remind_05");
		mSoundDescs.add("流水细腻轻柔铃声");
		mSoundNames.add("drug_remind_06");
		mSoundDescs.add("清爽铃声");
		mSoundNames.add("drug_remind_07");
		mSoundDescs.add("十分悦耳的铃声");
		mSoundNames.add("drug_remind_08");
		mSoundDescs.add("天使简约优美铃声");
		mSoundNames.add("drug_remind_09");
		mSoundDescs.add("唯美的铃声");
		mSoundNames.add("drug_remind_10");
		mSoundDescs.add("温馨的铃声");
		mSoundNames.add("drug_remind_11");
		mSoundDescs.add("预约舒缓适宜铃声");
		// ###################结束############################//
	}
	private SoundPlayer mSoundPlayer;
	private static final int DIALOG_REMIND_SOUND = 1;
	// 重复周期选择器
	OptionsPickerView<String> mOptionsPicker1;
	// 重复天数选择器
	OptionsPickerView<String> mOptionsPicker2;
	// 重复天数选择器
	OptionsPickerView<String> mOptionsPicker3;
	protected String mDes;
	int _id = -1;
	Alarm alarmInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setalert);
		patientId = getIntent().getStringExtra("Patientid");
		Patientname = getIntent().getStringExtra("Patientname");
		LogUtils.burtLog("Patientid11=="+patientId+"Patientname=="+Patientname);
		initData();
		initView();
	}

	void initView() {
		rl_choicemedie = (RelativeLayout) findViewById(R.id.rl_choicemedie);
		rl_lastnum = (RelativeLayout) findViewById(R.id.rl_lastnum);
		rl_everytime_eat = (RelativeLayout) findViewById(R.id.rl_everytime_eat);
		rl_begin_data = (RelativeLayout) findViewById(R.id.rl_begin_data);
		rl_interval_data = (RelativeLayout) findViewById(R.id.rl_interval_data);
		rl_continue_data = (RelativeLayout) findViewById(R.id.rl_continue_data);
		rl_alert_time = (RelativeLayout) findViewById(R.id.rl_alert_time);
		rl_alarm = (RelativeLayout) findViewById(R.id.rl_alarm);
		tv_lastnum = (TextView) findViewById(R.id.tv_lastnum);
		tv_everytime_eat = (TextView) findViewById(R.id.tv_everytime_eat);
		tv_choice = (TextView) findViewById(R.id.tv_choice);

		ll_btncontrol = (LinearLayout) findViewById(R.id.ll_btncontrol);
		btn_delete = (Button) findViewById(R.id.btn_delete);
		btn_stop = (Button) findViewById(R.id.btn_stop);


		RelativeLayout ll_sub = (RelativeLayout) findViewById(R.id.ll_sub);
		ViewStub vstub_title = (ViewStub) findViewById(R.id.vstub_title);
		View view = vstub_title.inflate(this, R.layout.layout_modi_time, ll_sub);
		rl_plus = (RelativeLayout) view.findViewById(R.id.rl_plus);
		rl_plus.setOnClickListener(this);
		rl_choicemedie.setOnClickListener(this);
		rl_lastnum.setOnClickListener(this);
		rl_everytime_eat.setOnClickListener(this);
		rl_begin_data.setOnClickListener(this);
		rl_interval_data.setOnClickListener(this);
		rl_continue_data.setOnClickListener(this);
		rl_alert_time.setOnClickListener(this);
		rl_alarm.setOnClickListener(this);
		btn_delete.setOnClickListener(this);
		btn_stop.setOnClickListener(this);
		tv_begin_data = (TextView) findViewById(R.id.tv_begin_data);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("添加提醒");
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		tv_interval = (TextView) findViewById(R.id.tv_interval);
		tv_continue = (TextView) findViewById(R.id.tv_continue);
		tv_alarm = (TextView) findViewById(R.id.tv_alarm);
		tv_alert_time = (TextView) findViewById(R.id.alert_time);
		line4 = (View)findViewById(R.id.line4);

		if (null!=info) {
			tv_title.setText("修改提醒");
			keep = info.repeatDayIndex;
			remindInterval = info.repeatPeriodIndex;
			String repeatPeriodIndex = REPEAT_PERIOD.get(info.repeatPeriodIndex);
			tv_interval.setText(repeatPeriodIndex);
			_id = info._id;
			int index = info.repeatDayIndex;
			if(index>0){
				index--;
			}
			String repeatDayIndex = REPEAT_DAY.get(index);
			tv_continue.setText(repeatDayIndex);
			tv_begin_data.setText(info.begindata+"");
			//	tv_interval.setText(info.days_remind_jg+"");
			//tv_continue.setText(info.days_remind_cx+"");
			if (null!=info.alarms&&info.alarms.size()>0) {

				String times = getAllAlarm(info.alarms);
				tv_alert_time.setText(times);
			}
			tv_everytime_eat.setText(info.goods_mcfy+"");
			tv_lastnum.setText(info.goods_dqsy+"");
//			rl_everytime_eat.setVisibility(View.VISIBLE);
//			rl_lastnum.setVisibility(View.VISIBLE);
//			if ((!TextUtils.isEmpty(info.goods_mcfy)&&info.goods_mcfy.contains("-"))||info.goods_mcfy.equals("0")){
//				tv_everytime_eat.setText("忽略数量");
//				rl_everytime_eat.setVisibility(View.GONE);
//			}
//			if ((!TextUtils.isEmpty(info.goods_dqsy)&&info.goods_dqsy.contains("-"))||info.goods_dqsy.equals("0")){
//				tv_lastnum.setText("忽略数量");
//				rl_everytime_eat.setVisibility(View.GONE);
//				rl_lastnum.setVisibility(View.VISIBLE);
//			}

			tv_choice.setText(info.drugName+"");
			if (info.soundDesc.contains("系统")) {
				tv_alarm.setText("班得瑞钢琴曲");
			}else {
				tv_alarm.setText(info.soundDesc);
			}

			goodsId = info.idmedie;
			alertid = info.id;
			ll_btncontrol.setVisibility(View.VISIBLE);
			if (info.isRemind) {
				btn_stop.setText("暂停提醒");
			}else {
				btn_stop.setText("开启提醒");
			}
		}else {
			ll_btncontrol.setVisibility(View.GONE);
			tv_begin_data.setText(TimeUtils.getTimeDay()+"");
			tv_interval.setText("每天提醒");
			tv_continue.setText("1天");
			//if (null!=info.times_remind_oneday&&info.times_remind_oneday.size()>0) {
			tv_alert_time.setText("");
			tv_alarm.setText("班得瑞钢琴曲");
			//}
//			rl_everytime_eat.setVisibility(View.GONE);
			line4.setVisibility(View.GONE);
			tv_lastnum.setText("");
			tv_choice.setText("");
		}
		initOptionsPickerView();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.rl_choicemedie) {
			Intent intent = new Intent(this,ChoiceMedieActivity.class);
			LogUtils.burtLog("Patientid==" + patientId);
			intent.putExtra("Patientid", patientId);
			startActivityForResult(intent, 1);
		}else if (v.getId() == R.id.rl_lastnum) {
//			showCustomDialog(tv_lastnum);
		}else if (v.getId() == R.id.rl_everytime_eat) {
//			showCustomDialog(tv_everytime_eat);
		}else if (v.getId() == R.id.rl_begin_data) {
			tv_begin_data.setText(TimeUtils.getTimeDay()+"");
			DatePicker dataPicker = new DatePicker(SetAlertActivity.this);

			dataPicker.selectDateDialog(tv_begin_data, "1777-11-01");
		} else if (v.getId() == R.id.rl_interval_data) {
			/********************/

//			ToastUtils.showToast(this,"提醒间隔天数"); // TODO
			mOptionsPicker1.show();
		} else if (v.getId() == R.id.rl_continue_data) {
			/********************/
//			ToastUtils.showToast(this,"提醒持续时间"); // TODO
			mOptionsPicker2.show();
		} else if (v.getId() == R.id.rl_alert_time) {
			/*if (!TextUtils.isEmpty(mMData)) {
				alert_time.setText(mMData);
			}*/

			//
//			 Intent intent = new Intent(this, AddDurgRemindActivity.class);
//			 startActivity(intent);

			AlertTimeDialog dialog = new AlertTimeDialog(patientId,this,data,
					new InterfaceGetData() {
						@Override
						public void getData(List<String> datas ,int[] mAlarmIndex) {
							StringBuilder sBuilder = new StringBuilder();
							sBuilder.append( "");
							data.clear();
							SetAlertActivity.this.mAlarmIndex = mAlarmIndex;

							for (int i = 0; i < datas.size(); i++) {

								String a = datas.get(i);
								if (!TextUtils.isEmpty(a) && !a.equals(mS1)  ) {
									if (i != 0&&!TextUtils.isEmpty(sBuilder)) {

										sBuilder.append("、" + a);
									} else {
										sBuilder.append(a);
									}
									data.add(a);
								}
							}

							tv_alert_time.setText(sBuilder);
							mMData = "--";
						}
					});
			dialog.showDialog();
		} else if (v.getId() == R.id.rl_alarm) {
			// showDialog(DIALOG_REMIND_SOUND);

			RingAlertDialog dialogs = new RingAlertDialog(this,info,
					new InterfaceGetSound() {
						@Override
						public void getSound(String des) {

							if (des != null) {
								mDes = des;
							}
							tv_alarm.setText(mDes);
						}
					});
			dialogs.showDialog();
		} else if (v.getId() == R.id.btn_delete) {

			showDialogDelete();

		} else if (v.getId() == R.id.btn_stop) {


			HashMap<String, String> interfaces = new HashMap<String, String>();
			interfaces.put("id", info.id);//drugReminder/updateStatusById
			interfaces.put("access_token", UserInfo.getInstance(SetAlertActivity.this).getSesstion());
			if (info.isRemind) {
				interfaces.put("status", "2");

				new HttpManager().post (this,
						"org/drugReminder/updateStatusById",
						AlarmStop.class,
						interfaces,
						this,false, 1);
			}else {
				interfaces.put("status", "1");
				new HttpManager().post (this,
						"org/drugReminder/updateStatusById",
						AlarmStop.class,
						interfaces,
						this,false, 1);
			}
			
			
			
			
			
		/*	*/
		} else if (v.getId() == R.id.rl_back) {
			finish();
		}else if (v.getId() == R.id.rl_plus) {
			String goods =tv_choice.getText().toString().trim();
			String beginData = tv_begin_data.getText().toString().trim();
			String  lastMedie = tv_lastnum.getText().toString().trim();
			String  eatNum = tv_everytime_eat.getText().toString().trim();
			String alert_times = tv_alert_time.getText().toString().trim();
			String remindNoice = tv_alarm.getText().toString().trim();
			//String remindInterval = tv_interval.getText().toString().trim();
			//String keep = tv_continue.getText().toString().trim();
			boolean start = true;
			if (TextUtils.isEmpty(goods)) {
				ToastUtils.showToast(this,"请选择药品");
				return;
			}else if(TextUtils.isEmpty(beginData)){
				ToastUtils.showToast(this,"请选择开始日期");
				return;
			}else if(TextUtils.isEmpty(alert_times)){
				ToastUtils.showToast(this,"请选择每日提醒时间");
				return;
			}
			if (!TextUtils.isEmpty(lastMedie)&&!lastMedie.equals("0")&&!lastMedie.equals("忽略数量")){

				if (TextUtils.isEmpty(eatNum)||(!TextUtils.isEmpty(eatNum)&&eatNum.equals("0"))){
					ToastUtils.showToast(this,"每次服用数量不能为0");
					return;
				}
			}

			if(keep<7){
				if(remindInterval>keep){
					ToastUtils.showToast(this,"提醒重复周期不应该大于服药持续时长");
					return;
				}
			}
			//goods = "0007A13B3CA94A1994AC5BE16E9AB640"; 
			if (info==null) {
				getPatient(null, goodsId, beginData, remindInterval, keep, data, remindNoice
						, lastMedie, eatNum, start+"");
			}else {
				getPatient( info.id,goodsId, beginData, remindInterval, keep, data, remindNoice
						, lastMedie, eatNum, start+"");
			}


		}
	}
	public void showDialogDelete(){
		final com.dachen.mediecinelibraryrealizedoctor.entity.CustomDialog dialog = new com.dachen.mediecinelibraryrealizedoctor.entity.CustomDialog(this);
		dialog.showDialog("", "确定删除该用药提醒？", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dimissDialog();
				if (null != info) {
					HashMap<String, String> interfaces = new HashMap<String, String>();
					interfaces.put("id", info.id);
					interfaces.put("access_token", UserInfo.getInstance(SetAlertActivity.this).getSesstion());
					new HttpManager().post(SetAlertActivity.this,
							"org/drugReminder/deleteDrugReminderById",
							DeteAlarm.class,
							interfaces,
							SetAlertActivity.this, false, 1);
				} else {
				}
			}
		}, new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//finish();
				dialog.dimissDialog();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (null!=data) {
			Bundle bundle = data.getBundleExtra("choice");
			MedieEntity entity =  (MedieEntity) bundle.get("choice");
		/*	if (!TextUtils.isEmpty(entity.general_name)) {
				tv_choice.setText(entity.general_name);
			}else {
				tv_choice.setText(entity.name);
			}*/
			String name = CompareDatalogic.getShowName(entity.general_name,entity.name,entity.title);
			tv_choice.setText(name);
			//tv_choice.setText(entity.name);
			goodsId = entity.id;
			goodName = name;
//			rl_everytime_eat.setVisibility(View.VISIBLE);
			int bought_quantity = entity.bought_quantity ;
			String quantity = entity.quantity;
			String pack_specification = entity.pack_specification;

//			if (0!=bought_quantity&&0!= StringUtils.getNum(pack_specification)){
//				int num = bought_quantity*StringUtils.getNum(pack_specification);
//				tv_lastnum.setText(""+num);
//				rl_everytime_eat.setVisibility(View.VISIBLE);
//				rl_lastnum.setVisibility(View.VISIBLE);
//			}
//			if(0==bought_quantity || 0==StringUtils.getNum(pack_specification)){
//				tv_lastnum.setText("忽略数量");
//				rl_everytime_eat.setVisibility(View.GONE);
//				rl_lastnum.setVisibility(View.VISIBLE);
//			}
			if (0!=StringUtils.getNum(quantity)){
				tv_everytime_eat.setText(""+StringUtils.getNum(quantity));
			}
			line4.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub 


		super.onResume();
		Bundle bundle = getIntent().getBundleExtra("choice");
		if (bundle!=null) {
			MedieEntity entity =  (MedieEntity) bundle.get("choice");
			tv_choice.setText(entity.name);
			goodsId = entity.id;
//			rl_everytime_eat.setVisibility(View.VISIBLE);
			line4.setVisibility(View.VISIBLE);
		}
	}
	public void getPatient(String id,String goods,String beginData,int remindInterval,
						   int keep,List<String> remindTime,String remindNoice
			,String lastMedie,String eatNum,String start){
		//showLoadingDialog(); 
		HashMap<String, String> maps = MedicineApplication.getMapConfig();
		if (null==maps) {
			return;
		}
		HashMap<String, Object> interfaces = new HashMap<String, Object>();
		//患者
		interfaces.put("patientId", patientId);
		interfaces.put("access_token",UserInfo.getInstance(this).getSesstion());
		interfaces.put("userId",UserInfo.getInstance(this).getId());
 		interfaces.put("reminderRinging",remindNoice);
		//goods 品种
		interfaces.put("goodsId", goods);
		Long begindates = com.dachen.mediecinelibraryrealize.utils.TimeUtils.getDays(beginData);
		//开始日期
			interfaces.put("startDate", begindates+"");
		//提醒间隔天数
		interfaces.put("reminderIntervals", remindInterval+"");
		//提醒持续天数
		if(keep == 0){
			keep++;//经修改后keep至少是从1开始的
		}
		interfaces.put("reminderDays", keep+"");

		Alarm alarm = new Alarm();
		DrugRemind remind = new DrugRemind();
		Collection<Alarm> alarms = new ArrayList<Alarm>() ;
		remind.ownerUserId = UserInfo.getInstance(this).getId()+"";
		remind.patientId=patientId+"";
		remind.patientName = Patientname;
		if (info!=null) {
			remind.id = alertid;
		}else {
			remind.id = "##112233##";
		}

		remind.begindata = beginData;
		remind.goods_mcfy = eatNum;
		remind.goods_dqsy = lastMedie;
		remind.repeatDayIndex = keep;
		remind.repeatPeriodIndex = remindInterval;
		remind.createTime = TimeUtils.getNowTime();
		String name =tv_choice.getText().toString().trim();
		remind.drugName = name;
		if (start.contains("tr")) {
			remind.isRemind = true;
		}else {
			remind.isRemind = false;
		}

		remind._id = _id;
		for (int i = 0; i < mSoundDescs.size(); i++) {
			if (mSoundDescs.get(i).contains(remindNoice)) {
				AudioManager audioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
				if(audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT){//mSoundDescs不包含静音，所以如果当前系统是静音的话要去掉
					audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
				}
				remind.soundDesc = remindNoice;
				remind.soundIndex = i;
				if (i<9) {
					remind.soundName  = "drug_remind_0"+(i+1);

				}else {
					remind.soundName  = "drug_remind_"+(i+1);
				}
				break;
			}

		}

		alarm.drugRemind = remind;
		for (int i = 0; i < remindTime.size(); i++) {
			Alarm alarm1 = new Alarm();
			String t = remindTime.get(i);

			if (!TextUtils.isEmpty(t)&&t.length()>=4){
				alarm.minute = Integer.parseInt(t.substring(3));
				alarm.hour = Integer.parseInt(t.substring(0, 2));
				alarm._id = getAlarmId(i);
				alarm1.minute = Integer.parseInt(t.substring(3));
				alarm1.hour = Integer.parseInt(t.substring(0,2));
				alarm1.drugRemind = remind;
				if (null!=mAlarmIndex) {
					alarm1.index= mAlarmIndex[i];
				}else {
					alarm1.index= 0;
				}
				interfaces.put("reminderTime"+(i+1), t);
				alarm1._id = getAlarmId(i);
				alarms.add(alarm1);
			}


		}
		remind.alarms = alarms;

		//每日提醒时间


		if (TextUtils.isEmpty(lastMedie)||"--".equals(lastMedie)||(!TextUtils.isEmpty(lastMedie)&&lastMedie.contains("忽略"))) {
			//当前剩余药量
			interfaces.put("goods_dqsy", "-1");
		}else {
			interfaces.put("goods_dqsy", lastMedie);
		}
		if (TextUtils.isEmpty(eatNum)||"--".equals(eatNum)||(!TextUtils.isEmpty(eatNum)&&eatNum.contains("忽略"))) {
			//每次服药数量
			interfaces.put("goods_mcfy", "-1");
		}else {
			interfaces.put("goods_mcfy", eatNum);
		}
		interfaces.put("patientName",Patientname);
		interfaces.put("goodsName",goodName);
		if (info!=null) {//
			saveAlarm(remind, true);
			/*new HttpManager().post(this,
					Params.getInterface("invoke", "c_remind_patient.edit"),
					Patients.class,
					interfaces,
					this, false, 2, false);*/
			interfaces.put("id",info.id);
			new HttpManager().post (this,
					"org/drugReminder/updateDrugReminderById",
					SaveAlarm.class,
					interfaces,
					this,false, 1);
		} else {
			saveAlarm(remind, true);
			/*new HttpManager().post(this,
					Params.getInterface("invoke", "c_remind_patient.create"),
					Patients.class,
					interfaces,
					this, false, 2, false);*/
			new HttpManager().post (this,
					"org/drugReminder/addDrugReminder",
					SaveAlarm.class,
					interfaces,
					this,false, 1);
		}
	}
	private int getAlarmId(int number) {
		if (info == null || info.alarms == null) {
			return -1;
		}

		for (Alarm alarm : info.alarms) {
			if (alarm.number == number) {
				return AlarmUtil.getId(alarm);
			}
		}

		return -1;
	}
	private List<String> getlistAlarm(Collection<Alarm> alarms) {
		List<String> lists = new ArrayList<String>();

		for (Alarm alarm : alarms) {
			String alarmss = new String();
			alarmss = String.format("%02d:%02d", alarm.hour, alarm.minute);
			lists.add(alarmss);

		}
		return lists;
	}

	private void saveAlarm(DrugRemind drugRemind  ,boolean reset) {
		if (info==null) {

			CreateOrUpdateStatus status = DrugRemindDao.getInstance(this).addOrUpdate(drugRemind);
			for (Alarm alarm : drugRemind.alarms) {
				if (reset){
					alarm.times=0;
					alarm.eat = 0;
					alarm.ringTime = 0;
				}

				//AlarmDao.getInstance(this).createOrUpdate(alarm);
			}

		}
		List<Alarm> alarms = (List<Alarm>) drugRemind.alarms;
		for (int i = 0; i < alarms.size(); i++) {
			Alarm a = alarms.get(i);
			if (reset){
				a.times=0;
				a.eat = 0;
				a.ringTime = 0;
			}
			alarms.set(i, a);
		}
		AlarmBusiness.setAlarms(this, alarms);
		//finish();
	}
	private void initOptionsPickerView() {
		mOptionsPicker1 = new OptionsPickerView<String>(this);
		//提醒间隔天数不应该大于提醒持续时间
		if(keep>=7){
			mOptionsPicker1.setPicker(REPEAT_PERIOD);
		}else{
			ArrayList<String> arrayList = new ArrayList<String>();
			for(int i=0;i<keep;i++){
				arrayList.add(REPEAT_PERIOD.get(i));
			}
			if(keep == 0){
				arrayList.add(REPEAT_PERIOD.get(0));
			}
			mOptionsPicker1.setPicker(arrayList);
		}
		mOptionsPicker1.setTitle("重复周期");
		mOptionsPicker1.setCyclic(false, true, true);
		mOptionsPicker1.setSelectOptions(remindInterval);

		mOptionsPicker1.setCancelable(true);
		mOptionsPicker1.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

			@Override
			public void onOptionsSelect(int options1, int option2,
										int options3) {
				tv_interval.setText(REPEAT_PERIOD.get(options1));
				// mDrugRemind.repeatPeriod = options1;
				remindInterval = options1;
			}
		});

		mOptionsPicker2 = new OptionsPickerView<String>(this);
		mOptionsPicker2.setPicker(REPEAT_DAY);
		mOptionsPicker2.setTitle("服药持续时长");
		mOptionsPicker2.setCancelable(true);
		mOptionsPicker2.setCyclic(false, true, true);
		int index = keep;
		if(index>0){
			index--;
		}
		mOptionsPicker2.setSelectOptions(index);
		mOptionsPicker2.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

			@Override
			public void onOptionsSelect(int options1, int option2, int options3) {
				tv_continue.setText(REPEAT_DAY.get(options1));
				//mDrugRemind.repeatDay = options1;
				keep = options1+1;
				//提醒间隔天数不应该大于提醒持续时间
				if(keep>=7){
					mOptionsPicker1.setPicker(REPEAT_PERIOD);
				}else{
					ArrayList<String> arrayList = new ArrayList<String>();
					for(int i=0;i<keep;i++){
						arrayList.add(REPEAT_PERIOD.get(i));
					}
					if(keep == 0){
						arrayList.add(REPEAT_PERIOD.get(0));
					}
					mOptionsPicker1.setPicker(arrayList);
				}
			}
		});

	}

	private void initData() {
		if (null!=getIntent().getBundleExtra("patient")) {
			Bundle bundle = getIntent().getBundleExtra("patient");
			info = (DrugRemind) bundle.get("patient");
		}
		mIsCreate = getIntent().getBooleanExtra("create", false);
		data = new ArrayList<String>();
		if (null!=info) {
			data = getlistAlarm(info.alarms);
		}

		if (mIsCreate) {
			mDrugRemind = new DrugRemindNoDataBase();

			HashMap<String, String> maps = MedicineApplication.getMapConfig();
			if (null == maps) {
				// ToastUtils.showToast("获取数据错误");
				finish();
				return;
			}
			mDrugRemind.ownerUserId = UserInfo.getInstance(this).getId()+"";
			mDrugRemind.patientId=patientId;
			mDrugRemind.createTime = System.currentTimeMillis();
			mDrugRemind.isRemind = true;
			mDrugRemind.soundDesc = mSoundDescs.get(2);
			mDrugRemind.soundIndex = 2;
			mDrugRemind.repeatPeriod = 1;
			mDrugRemind.repeatDay = 3;
		} else {
			mDrugRemind = (DrugRemindNoDataBase) getIntent().getSerializableExtra(
					"drugRemind");
		}
		mSoundPlayer = new SoundPlayer(this);
		loadData();
	}

	// ###################开始############################//
	@SuppressLint("NewApi")

	@Override
	public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

	}
	Dialogcallback dialogcallback2 = new Dialogcallback() {
		@Override
		public void dialogdo(String string) {
			tv_everytime_eat.setText(string);
			if (TextUtils.isEmpty(string)) {
				//	tv_everytime_eat.setText("");
			}
		}
	};
	@Override
	public void onFailure(Exception arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(Result arg0) {
		// TODO Auto-generated method stub
		closeLoadingDialog();

		if (null!=arg0 && arg0 instanceof DeteAlarm) {
			DeteAlarm delete = (DeteAlarm) arg0;
			if(delete.resultCode==1){
				if (null!=info) {
					DrugRemindDao.getInstance(this).clearByID(info.id,patientId);
					if (null!=info.alarms){
						AlarmBusiness.cancelAlarms(this, info.alarms);
					}
				}
				sleptime = 10;
				ToastUtils.showToast(this,"删除成功");
				//MActivityManager.getInstance().finishActivity(ShowAlertActivity.class);
				final long finalSleptime = sleptime;
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(finalSleptime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Intent intent = new Intent();
						intent.putExtra("Patientname", Patientname);
						intent.putExtra("Patientid", patientId);
						intent.putExtra("save", "save");
						setResult(200, intent);
						finish();
					}
				}).start();
			}


		}else if(arg0 instanceof SaveAlarm){
			SaveAlarm save = (SaveAlarm)arg0;
			if (save.resultCode==1){
					sleptime = 10;
					ToastUtils.showToast(this,"保存成功");
				//MActivityManager.getInstance().finishActivity(ShowAlertActivity.class);
				final long finalSleptime = sleptime;
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(finalSleptime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Intent intent = new Intent();
						intent.putExtra("Patientname", Patientname);
						intent.putExtra("Patientid", patientId);
						intent.putExtra("save", "save");
						setResult(200, intent);
						finish();
					}
				}).start();
			}
		}
		else if(arg0 instanceof AlarmStop){
			AlarmStop stop = (AlarmStop)arg0;
			if (stop.resultCode==1){
				if (null!=info) {
					if (info.isRemind) {
						info.isRemind = false;
						ToastUtils.showToast(this,"暂停提醒成功");
						btn_stop.setText("开启提醒");
						if (null!=info.alarms){
							for (Alarm alarm : info.alarms) {
								AlarmBusiness.cancelAlarmFive(this, alarm);
							}
						}
					}else {
						btn_stop.setText("暂停提醒");
						ToastUtils.showToast(this,"开启提醒成功");
						info.isRemind = true;
						AlarmBusiness.setAlarms(this, info.alarms);
					}
					if (null!=info.alarms){
						AlarmBusiness.cancelAlarms(this, info.alarms);
					}
					DrugRemindDao.getInstance(this).update(info);
				}
			}
		}
	}



	@Override
	public void onSuccess(ArrayList arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onOtherButtonClick(ActionSheet actionSheet, int index) {
		// TODO Auto-generated method stub

	}

	private void loadData() {
		List<DrugRemind> drugReminds = DrugRemindDao.getInstance(this).queryByPatientId(patientId);

		if (drugReminds == null || drugReminds.isEmpty()) {
			return;
		}
	}
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
