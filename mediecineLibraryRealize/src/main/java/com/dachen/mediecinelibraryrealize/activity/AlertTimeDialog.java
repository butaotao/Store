package com.dachen.mediecinelibraryrealize.activity;

import android.app.Activity;
import android.app.Dialog;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.dachen.medicine.common.utils.Alarm;
import com.dachen.medicine.common.utils.DrugRemind;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.MedicineApplication;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.view.NoEditNumPicker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class AlertTimeDialog implements OnClickListener {
	String patientid;
	private Activity mContext;

	private int mWidth;
	private float mDensity;

	private static final String TAG_SUBMIT = "submit";
	private static final String TAG_CANCLEL = "cancel";
	// 提醒时间
	public static final String[] REMIND_TIME = new String[49];

	TextView mAlarm1;

	TextView mAlarm2;

	TextView mAlarm3;

	TextView mAlarm4;
	NoEditNumPicker mNumberPicker1;

	NoEditNumPicker mNumberPicker2;

	NoEditNumPicker mNumberPicker3;

	NoEditNumPicker mNumberPicker4;
	private Button mBtnSubmit;
	private Button mBtnCancel;
	private TextView mTvTitle;

	private LayoutInflater mInflater;
	private Dialog mDialog;
	private View mRootView;
	private boolean mIsCreate;

	InterfaceGetData interfaces;

	private String mS1 = "--:--";
	private String mS2 = "--:--";
	private String mS3 = "--:--";
	private String mS4 = "--:--";

	private int[] mAlarmIndex = new int[4];

	private DrugRemind mDrugRemind;

	private int mId;

	static {
		// 初始化提醒时间
		REMIND_TIME[0] = "--:--";
		for (int i = 1; i <= 24; i++) {
			REMIND_TIME[2 * i - 1] = String.format("%02d:00", i - 1);
			REMIND_TIME[2 * i] = String.format("%02d:30", i - 1);
		}
	}
   
	public AlertTimeDialog(String patientid,Activity context,List<String>  info, InterfaceGetData interfaces) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.interfaces = interfaces;
		this.patientid = patientid;
		initData();
		initView();
		initDialog();
		if (null!=info&&info.size()>0) {
			//String s[]=getNextAlarm(info.alarms);
			for (int i = 0; i < info.size(); i++) {
				for (int j = 0; j < REMIND_TIME.length; j++) {
					if (REMIND_TIME[j].equals(info.get(i))) {
						mAlarmIndex[i]=j;
						if (i==0) {
							mS1 = info.get(i);
						}else if (i==1) {
							mS2 = info.get(i);
						}else if (i==2) {
							mS3 = info.get(i);
						}else if (i==3) {
							mS4 = info.get(i);
						}
						
						LogUtils.burtLog(REMIND_TIME[j]+"=22222222=="+info.get(i));
						break;
					}
					LogUtils.burtLog(REMIND_TIME[j]+"==="+info.get(i));
				}
			} 
		} 
		updateView( mAlarmIndex);
	} 
	private String[] getNextAlarm(Collection<Alarm> alarms ) {
		long minTime = Long.MAX_VALUE;
		Alarm nextAlarm = null;
		String[] time = new String[4];
		int i=0;
		for (Alarm alarm : alarms) {
			
			time[i]= String.format("%02d:%02d", alarm.hour, alarm.minute);
			i++;
		}
		return time;
	}
	private void updateView( int[] mAlarmIndex) {
		//TODO 需要将这些存入数据库
//		mRemidSound.setText(mSoundDescs.get(mDrugRemind.soundIndex));
//
//		mRepeatPeriod.setText(REPEAT_PERIOD.get(mDrugRemind.repeatPeriodIndex));
//		mRepeatDay.setText(REPEAT_DAY.get(mDrugRemind.repeatDayIndex));

		/*	if (mAlarmIndex[0]==0){
				mAlarmIndex[0] = 17;
			}
			if (mAlarmIndex[1]==0){
				mAlarmIndex[1] = 25;
			}
			if (mAlarmIndex[2]==0){
				mAlarmIndex[2] = 33;
			}
			if (mAlarmIndex[3]==0){
				mAlarmIndex[3] = 41;
			}*/
		mNumberPicker1.setValue(mAlarmIndex[0]);
		mNumberPicker2.setValue(mAlarmIndex[1]);
		mNumberPicker3.setValue(mAlarmIndex[2]);
		mNumberPicker4.setValue(mAlarmIndex[3]);

		changeAlarmBackground(mAlarm1, mAlarmIndex[0]);
		changeAlarmBackground(mAlarm2, mAlarmIndex[1]);
		changeAlarmBackground(mAlarm3, mAlarmIndex[2]);
		changeAlarmBackground(mAlarm4, mAlarmIndex[3]);

	}

	/**
	 * 初始化用药提醒数据
	 */
	private void initData() {
		// initAlarmIndex();

		mIsCreate = mContext.getIntent().getBooleanExtra("create", false);
		if (mIsCreate) {
			mDrugRemind = new DrugRemind();
			HashMap<String, String> maps = MedicineApplication.getMapConfig();
			if (null == maps) {
				ToastUtils.showToast(mContext,"获取数据错误");
				closeDialog();
				return;
			}
			mId = mDrugRemind._id;
			mDrugRemind.ownerUserId = UserInfo.getInstance(mContext).getId()+"";
			mDrugRemind.patientId=patientid+"";
			mDrugRemind.createTime = System.currentTimeMillis();
			mDrugRemind.isRemind = true;
			mDrugRemind.createTime = System.currentTimeMillis();
			mDrugRemind.soundIndex = 2;
			mDrugRemind.repeatPeriodIndex = 1;// 重复周期
			mDrugRemind.repeatDayIndex = 3;
		} else {
			mDrugRemind = (DrugRemind) mContext.getIntent()
					.getSerializableExtra("drugRemind");
		}

		initAlarmIndex();
	}

	private void initAlarmIndex() {
		if (mDrugRemind == null || mDrugRemind.alarms == null) {
			return;
		}

		for (Alarm alarm : mDrugRemind.alarms) {
			mAlarmIndex[alarm.number] = alarm.index;
		}
	}

	private void initDialog() {

		mWidth = mContext.getResources().getDisplayMetrics().widthPixels;
		mDensity = mContext.getResources().getDisplayMetrics().density;

		mDialog = new Dialog(mContext, R.style.dialog_style);
		mDialog.setCanceledOnTouchOutside(false);

		mDialog.getWindow().setBackgroundDrawableResource(R.drawable.translate);
		mDialog.setContentView(mRootView);
		mDialog.getWindow().setGravity(Gravity.BOTTOM);
		WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
		lp.width = mWidth;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		
	}

	/**
	 * 初始化视图
	 * 
	 *
	 */
	private void initView() {
		mRootView = mInflater.inflate(R.layout.dialog_setalerttime, null);

		mAlarm1 = (TextView) mRootView.findViewById(R.id.alarm1);
		mAlarm2 = (TextView) mRootView.findViewById(R.id.alarm2);
		mAlarm3 = (TextView) mRootView.findViewById(R.id.alarm3);
		mAlarm4 = (TextView) mRootView.findViewById(R.id.alarm4);
		mNumberPicker1 = (NoEditNumPicker) mRootView
				.findViewById(R.id.numberPicker1);
		mNumberPicker2 = (NoEditNumPicker) mRootView
				.findViewById(R.id.numberPicker2);
		mNumberPicker3 = (NoEditNumPicker) mRootView
				.findViewById(R.id.numberPicker3);
		mNumberPicker4 = (NoEditNumPicker) mRootView
				.findViewById(R.id.numberPicker4);

	/*	mNumberPicker1.setValue(17);
		mNumberPicker2.setValue(25);
		mNumberPicker3.setValue(33);
		mNumberPicker4.setValue(41);*/
		// 带取消和确定的标题
	 	mBtnSubmit = (Button) mRootView.findViewById(R.id.btnSubmit);
		mBtnSubmit.setOnClickListener(this);
		mBtnSubmit.setTag(TAG_SUBMIT);
		mTvTitle = (TextView) mRootView.findViewById(R.id.tvTitle);
		mTvTitle.setText("设置闹铃时间");
		mBtnCancel = (Button) mRootView.findViewById(R.id.btnCancel);
		mBtnCancel.setOnClickListener(this);
		mBtnCancel.setTag(TAG_CANCLEL);

		// 初始化四个时间条
		initNumberPicker(mNumberPicker1);
		initNumberPicker(mNumberPicker2);
		initNumberPicker(mNumberPicker3);
		initNumberPicker(mNumberPicker4);

        mNumberPicker1.mInputText.setClickable(false);
        mNumberPicker2.mInputText.setClickable(false);
        mNumberPicker3.mInputText.setClickable(false);
        mNumberPicker4.mInputText.setClickable(false);
		mNumberPicker1.mInputText.clearFocus();
		mNumberPicker2.mInputText.clearFocus();
		mNumberPicker3.mInputText.clearFocus();
		mNumberPicker4.mInputText.clearFocus();
        mNumberPicker1.mInputText.setInputType(InputType.TYPE_NULL);
        mNumberPicker2.mInputText.setInputType(InputType.TYPE_NULL);
        mNumberPicker3.mInputText.setInputType(InputType.TYPE_NULL);
        mNumberPicker4.mInputText.setInputType(InputType.TYPE_NULL);

	}

	private void initNumberPicker(NoEditNumPicker numberPicker) {

		numberPicker.setDisplayedValues(REMIND_TIME);
		numberPicker.setMinValue(0);
		numberPicker.setMaxValue(REMIND_TIME.length - 1);

		numberPicker.setOnValueChangedListener(new NoEditNumPicker.OnValueChangeListener() {

			@Override
			public void onValueChange(NoEditNumPicker picker, int oldVal,
					int newVal) {
				// TODO Auto-generated method stub
				onValueChangeHandler(picker, oldVal, newVal);

			}
		});
	}

	/**
	 * @param picker
	 *            时间条
	 * @param oldVal
	 *            原来的索引
	 * @param newVal
	 *            现在的索引
	 */
	private void onValueChangeHandler(NoEditNumPicker picker, int oldVal,
			int newVal) { 
		if (picker.getId() == R.id.numberPicker1) {
			mAlarmIndex[0] = newVal;
			changeAlarmBackground(mAlarm1, newVal);
			mS1 = REMIND_TIME[newVal];
		} else if (picker.getId() == R.id.numberPicker2) {
			mAlarmIndex[1] = newVal;
			changeAlarmBackground(mAlarm2, newVal);
			mS2 = REMIND_TIME[newVal];
		
		} else if (picker.getId() == R.id.numberPicker3) {
			mAlarmIndex[2] = newVal;
			changeAlarmBackground(mAlarm3, newVal);
			mS3 = REMIND_TIME[newVal];
		} else if (picker.getId() == R.id.numberPicker4) {
			mAlarmIndex[3] = newVal;
			changeAlarmBackground(mAlarm4, newVal);
			mS4 = REMIND_TIME[newVal];
		}
	}

	/**
	 * 存储设置的闹钟
	 */
	private void saveAlarm() {
		
//		DrugRemind drugRemind = buildDrugRemindFormUI();
//		deleteAlarmIfNeed();
//
//		List<Alarm> alarms = buildAlarmFromUI(drugRemind);
//		for (Alarm alarm : alarms) {
//			AlarmDao.getInstance().createOrUpdate(alarm);
//		}
//
//		drugRemind.alarms = alarms;
//		AlarmBusiness.setAlarms(this, alarms);
//		EventBus.getDefault().post(drugRemind);

	}

	private List<Alarm> buildAlarmFromUI(DrugRemind drugRemind) {
		List<Alarm> alarms = new ArrayList<Alarm>(4);
		for (int i = 0; i < mAlarmIndex.length; i++) {
			if (mAlarmIndex[i] != 0) {
				Alarm alarm = new Alarm();
				alarm._id = getAlarmId(i);
				alarm.hour = getHour((mAlarmIndex[i]));
				alarm.minute = getMinute((mAlarmIndex[i]));
				alarm.number = i;
				alarm.index = mAlarmIndex[i];
				alarm.drugRemind = drugRemind;
				alarms.add(alarm);
			}
		}

		return alarms;
	}

	private int getAlarmId(int number) {
		if (mDrugRemind == null || mDrugRemind.alarms == null) {
			return -1;
		}

		for (Alarm alarm : mDrugRemind.alarms) {
			if (alarm.number == number) {
				return alarm._id;
			}
		}

		return -1;
	}


	private DrugRemind buildDrugRemindFormUI() {
		DrugRemind drugRemind = new DrugRemind();
		drugRemind._id = mId;
//		drugRemind.drugName = mDrugName.getText().toString();
//		drugRemind.patientName = mPatientName.getText().toString();
//		drugRemind.soundIndex = mSoundIndex;
//		drugRemind.soundName = mSoundNames.get(mSoundIndex);
//		drugRemind.soundDesc = mSoundDescs.get(mSoundIndex);
//		drugRemind.repeatPeriodIndex = mRepeatPeriodIndex;
//		drugRemind.repeatDayIndex = mRepeatDayIndex;

		return drugRemind;
	}

	private List<String> getAlarmString() {
		//StringBuilder sBuilder = new StringBuilder();

		List<String> mArrayList = new ArrayList<String>();
		if (!mArrayList.contains(mS1)) {
			mArrayList.add(mS1);
		}
		if (!mArrayList.contains(mS2)) {
			mArrayList.add(mS2);

		}
		if (!mArrayList.contains(mS3)) {
			mArrayList.add(mS3);

		}
		if (!mArrayList.contains(mS4)) {
			mArrayList.add(mS4);
		}
		Collections.sort(mArrayList);
	/*	for (int i = 0; i < mArrayList.size(); i++) {

			String a = mArrayList.get(i);
			System.out.println("a=" + a);
			if (a != "--:--" && a != "") {
				if (i != 0) {

					sBuilder.append("，" + a);
				} else {
					sBuilder.append(a);
				}
			}
		}*/
		return mArrayList;
	}

	/**
	 * 修改闹钟时有可能删除闹钟，比如原来设定了4个闹钟，现在修改为2个，则删除了2个闹钟
	 */
	private void deleteAlarmIfNeed() {
		// if (mDrugRemind == null || mDrugRemind.alarms == null) {
		// return;
		// }
		// List<Alarm> deletedAlarms = new ArrayList<Alarm>(4);
		// for (Alarm alarm : mDrugRemind.alarms) {
		// if (mAlarmIndex[alarm.number] == 0) {
		// deletedAlarms.add(alarm);
		// }
		// }
		// if (deletedAlarms.size() > 0) {
		// mDrugRemind.alarms.removeAll(deletedAlarms);
		// AlarmDao.getInstance().delete(deletedAlarms);
		// AlarmBusiness.cancelAlarms(this, deletedAlarms);
		// }
	}

	private int getHour(int position) {
		if (position == 0) {
			return 0;
		} else {
			String remindTime = REMIND_TIME[position];
			String[] hourMinute = remindTime.split(":");
			return Integer.parseInt(hourMinute[0]);
		}
	}

	private int getMinute(int position) {
		if (position == 0) {
			return 0;
		} else {
			String remindTime = REMIND_TIME[position];
			String[] hourMinute = remindTime.split(":");
			return Integer.parseInt(hourMinute[1]);
		}
	}

	/**
	 * @param textView
	 *            闹钟所在的view控件
	 * @param position
	 *            改变闹钟背景
	 */
	private void changeAlarmBackground(TextView textView, int position) {
		if (position == 0) {
			textView.setTextColor(mContext.getResources().getColor(
					R.color.gray_cccccc));// 改变文本颜色
			textView.setText("添加闹铃");
			textView.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.clock_gray, 0, 0);
		} else {
			textView.setTextColor(mContext.getResources().getColor(
					R.color.blue_30b2cc));
			textView.setText(REMIND_TIME[position]);
			textView.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.clock_blue, 0, 0);
		}
	}

	private int generateAlarmId() {
		return new Random(System.currentTimeMillis()).nextInt();
	}

	public void showDialog() {
		mDialog.show();
	}

	public void closeDialog() {

		mDialog.dismiss();
	}

	@Override
	public void onClick(View v) {
		String tag = (String) v.getTag();
		if (tag == TAG_SUBMIT) {
			closeDialog();
			 ;
			//if (alarm != "") {

				interfaces.getData(getAlarmString(),mAlarmIndex);
			//}

		} else {
			closeDialog();
		}
	}

	public interface InterfaceGetData {
		public void getData(List<String> data, int[] mAlarmIndex);
	}
}
