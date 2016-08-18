package com.dachen.dgroupdoctorcompany.service;

import java.lang.reflect.Method;
import java.util.Random;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
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
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.utils.UserInfo;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.net.CustomImagerLoader;


public class CallSmsSafeService extends Service {
	public static final String TAG = "CallSmsSafeService";
	WindowManager.LayoutParams params;
	private TelephonyManager tm;
	private MyPhoneListener listener;
	private WindowManager wm;
	private OutCallReceiver receiver;
	CompanyContactDao dao;
	Uri url = Uri.parse("content://call_log/calls");
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
					if (null!=entity&&!TextUtils.isEmpty(entity.telephone)){
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
		super.onDestroy();
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

		//resolver.delete(url, "number=?", new String[]{entity.telephone});
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
				if (entity!=null){
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
		CustomImagerLoader.getInstance().loadImage(imageView,entity.url);
		if (!TextUtils.isEmpty(entity.name)){
			tv.setText(entity.name);
			tv_position.setVisibility(View.VISIBLE);
		}else {
			tv.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(entity.position)){
			tv_position.setText(entity.position);
			tv_position.setVisibility(View.VISIBLE);
		}else {
			tv_position.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(entity.telephone)){
			tv_phone.setText(entity.telephone+"来自药企圈");
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
		params = new WindowManager.LayoutParams();
		params.gravity = Gravity.TOP + Gravity.LEFT;

		params.x = SharedPreferenceUtil.getInt(CallSmsSafeService.this,userId+"lastx", 0);
		params.y = SharedPreferenceUtil.getInt(CallSmsSafeService.this,userId+"lasty", 0);
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		// 窗体的类型。
		//params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		params.type = WindowManager.LayoutParams.TYPE_TOAST;
		wm.addView(view, params);
	}

}
