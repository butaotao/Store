package com.dachen.dgroupdoctorcompany.service;

import java.lang.reflect.Method;
import java.util.Random;

import android.app.Notification;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.RawContacts;


import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.CompanyApplication;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.utils.CommonUitls;
import com.dachen.dgroupdoctorcompany.utils.ConditionLogic;
import com.dachen.dgroupdoctorcompany.utils.UserInfo;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.net.CustomImagerLoader;
import com.nostra13.universalimageloader.core.ImageLoader;


public class CallSmsSafeService extends Service {
	public static final String TAG = "CallSmsSafeService";
	WindowManager.LayoutParams params;
	private TelephonyManager tm;
	private MyPhoneListener listener;
	private WindowManager wm;
	private OutCallReceiver receiver;
	CompanyContactDao dao;
	public int GRAY_SERVICE_ID = 10000;
	Uri url = Uri.parse("content://call_log/calls");
	JobScheduler	mJobScheduler;
	/**
	 * 归属地显示的view对象
	 */
	private View view;
	String userId;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}



	@Override
	public void onCreate() {
		dao = new CompanyContactDao(this);
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		listener = new MyPhoneListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);//监听电话的状态
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(receiver, filter);
		userId = UserInfo.getInstance(CallSmsSafeService.this).getId();
		super.onCreate();
	}

	private class MyPhoneListener extends PhoneStateListener{
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
				case TelephonyManager.CALL_STATE_RINGING://响铃状态
						//监视呼叫记录的生成，如果呼叫记录产生了。删除呼叫记录。

					CompanyContactListEntity entity = dao.queryByTelephone(incomingNumber);
					if (null!=entity&&!TextUtils.isEmpty(entity.telephone)&&UserInfo.getInstance(CallSmsSafeService.this).isLogin()){
						getContentResolver().registerContentObserver(url, true,
								new CallLogObserver(new Handler(),entity));
						// 立刻把电话挂断了。  但是呼叫记录的生成 并不是一个同步的代码。 是一个异步代码

						showMyToast(entity);
					}

					break;
				case TelephonyManager.CALL_STATE_IDLE:// 空闲状体
					dismissView();
					break;
			}
		}
	}
	void dismissView(){
		if (view != null) {
			wm.removeView(view);
			view = null;
		}
	}
	private class CallLogObserver extends ContentObserver{
		private CompanyContactListEntity entity;
		public CallLogObserver(Handler handler, CompanyContactListEntity entity) {
			super(handler);
			this.entity = entity;
		}
		//数据变化会调用onchage方法
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			getContentResolver().unregisterContentObserver(this);
			//updateCalllog(this.entity);
		}
	}


	@Override
	public void onDestroy() {
		unregisterReceiver(receiver);
		receiver = null;
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
		try {//判断是否有闹钟，没有则关闭闹钟服务


		/*	if (!alarm.equals("[]")) {
				if (daemonService != -1) {*/
			startService(new Intent(this, CallSmsSafeService.class));
			/*	}
			} else {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					mJobScheduler.cancel(GRAY_SERVICE_ID);
				}

			}
			unbindService(mConnection); //解除绑定服务。*/
		} catch (Exception e) {

		}
	}

	/**
	 * 清除呼叫记录
	 * @param
	 */
	public void updateCalllog( CompanyContactListEntity entity) {
		ContentResolver resolver = getContentResolver();
		Uri url = Uri.parse("content://call_log/calls");
		ContentValues values = new ContentValues();

		Uri rawContactUri = getContentResolver().insert(
				ContactsContract.RawContacts.CONTENT_URI, values);
		            long rawContactId = ContentUris.parseId(rawContactUri);
		             // 向data表插入姓名数据
		            if (!TextUtils.isEmpty(entity.name)&&!TextUtils.isEmpty(entity.telephone)) {
			                 values.clear();
			                values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
			                values.put(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
			            values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, entity.name);
						values.put(Phone.NUMBER, entity.telephone);
						values.put(Phone.TYPE, Phone.TYPE_MOBILE);
						getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
								                        values);

	}
		ContentValues contentValues = new ContentValues();
		contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,entity.name);
		contentValues.put(Phone.TYPE, Phone.TYPE_MOBILE);
		contentValues.put(Phone.NUMBER,entity.telephone);
		resolver.update(url, contentValues, "number=?", new String[]{entity.telephone});
	}
	private class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			String phone = getResultData();
			if (!TextUtils.isEmpty(phone)){
				CompanyContactListEntity entity = dao.queryByTelephone(phone);
				getContentResolver().registerContentObserver(url, true,
						new CallLogObserver(new Handler(), entity));
				if (entity!=null&&!"6".equals(entity.userStatus+"")&&UserInfo.getInstance(context).isLogin()){
					showMyToast(entity);
				}

			}

		}
	}
	public void showMyToast(CompanyContactListEntity entity) {
		// 直接利用窗体管理器 添加一个view对象到整个手机系统的窗体上
		view = View.inflate(this, R.layout.toast_address, null);
		view.setOnTouchListener(new View.OnTouchListener() {
			int startX;
			int startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						startX = (int) event.getRawX();
						startY = (int) event.getRawY();
						break;
					case MotionEvent.ACTION_MOVE:
						int newX = (int) event.getRawX();
						int newY = (int) event.getRawY();
						int dx = newX - startX;
						int dy = newY - startY;
						params.x +=dx;
						params.y +=dy;
						if(params.x<0){
							params.x = 0;
						}
						if(params.y<0){
							params.y = 0;
						}
						if(params.x > wm.getDefaultDisplay().getWidth()-view.getWidth()){
							params.x  = wm.getDefaultDisplay().getWidth()-view.getWidth();
						}
						if(params.y > wm.getDefaultDisplay().getHeight()-view.getHeight()){
							params.y  = wm.getDefaultDisplay().getHeight()-view.getHeight();
						}
						wm.updateViewLayout(view, params);
						//重新初始化手指的位置
						startX = (int) event.getRawX();
						startY = (int) event.getRawY();
						break;
					case MotionEvent.ACTION_UP:

						SharedPreferenceUtil.putInt(CallSmsSafeService.this, userId+"lastx", params.x);
						SharedPreferenceUtil.putInt(CallSmsSafeService.this,userId+"lasty", params.y);
						break;
				}
				return true;
			}
		});
		//view.setBackgroundResource(bgs[which]);
		TextView tv = (TextView) view.findViewById(R.id.tv_name);
		TextView tv_position = (TextView) view.findViewById(R.id.tv_position);
		TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
		ImageView imageView = (ImageView) view.findViewById(R.id.iv_icon);
		if(!TextUtils.isEmpty(entity.headPicFileName)){
			ImageLoader.getInstance().displayImage(entity.headPicFileName, imageView, CompanyApplication.mAvatarCircleImageOptions);
		}else{
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.head_icon);
			Bitmap circleBitMap = CommonUitls.getRoundedCornerBitmap(bitmap);
			imageView.setImageBitmap(circleBitMap);
		}

		if (!TextUtils.isEmpty(entity.name)){
			tv.setText(entity.name);
			tv_position.setVisibility(View.VISIBLE);
		}else {
			tv.setVisibility(View.GONE);
		}
		String peopledes ="";
		if (!TextUtils.isEmpty(entity.department)&&TextUtils.isEmpty(entity.position)){
			peopledes = entity.department;
		}
		if (TextUtils.isEmpty(peopledes)&&!TextUtils.isEmpty(entity.position)){
			peopledes =  entity.position;
		}
		if (!TextUtils.isEmpty(peopledes)&&!TextUtils.isEmpty(entity.position)){
			peopledes = entity.department +" | "+entity.position;
		}
		if (!TextUtils.isEmpty(peopledes)){
			tv_position.setText(peopledes);
			tv_position.setVisibility(View.VISIBLE);
		}else {
			tv_position.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(entity.telephone)){
			tv_phone.setText(entity.telephone);
			tv_phone.setVisibility(View.VISIBLE);
		}else {
			tv_phone.setVisibility(View.GONE);
		}
		view.findViewById(R.id.rl_delete).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismissView();
			}
		});
		DisplayMetrics metric = new DisplayMetrics();
		params = new WindowManager.LayoutParams();
		params.gravity = Gravity.TOP + Gravity.CENTER_HORIZONTAL;

		params.x = SharedPreferenceUtil.getInt(CallSmsSafeService.this,userId+"lastx", 0);
		params.y = SharedPreferenceUtil.getInt(CallSmsSafeService.this,userId+"lasty", 0);
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		/*params.width = (int) (metric.widthPixels);; // 宽度
		params.height = (int) (metric.heightPixels);; // 高度*//**/
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		// 窗体的类型。
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			params.type = WindowManager.LayoutParams.TYPE_TOAST;
		}else {
			params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		}
		if (ConditionLogic.isAllow(this)){
			wm.addView(view, params);
		}

	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
			JobInfo.Builder builder = new JobInfo.Builder(GRAY_SERVICE_ID,
					new ComponentName(getPackageName(), CallSmsSafeService.class.getName()));

			builder.setPeriodic(1 * 1000); //每隔60秒运行一次
			builder.setRequiresCharging(true);
			builder.setPersisted(true); //设置设备重启后，是否重新执行任务
			builder.setRequiresDeviceIdle(true);
/*
			if (mJobScheduler.schedule(builder.build()) <= 0) {
				//If something goes wrong
			}*/
		}

		startForeground(GRAY_SERVICE_ID, new Notification());
		return super.onStartCommand(intent, flags, startId);
	}

	public void checkAlarms(){
		new Thread(new Runnable() {
			@Override
			public void run() {

			}
		}).start();
	}

}
