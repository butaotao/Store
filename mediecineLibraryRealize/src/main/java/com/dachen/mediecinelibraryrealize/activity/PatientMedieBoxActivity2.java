package com.dachen.mediecinelibraryrealize.activity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.TimeUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.activity.MyDialog.Dialogcallback;
import com.dachen.mediecinelibraryrealize.adapter.BoxImageGalleryAdapter;
import com.dachen.mediecinelibraryrealize.adapter.ImageGalleryAdapter;
import com.dachen.mediecinelibraryrealize.entity.AlarmsTime;
import com.dachen.mediecinelibraryrealize.entity.AlarmsTime.AlarmsTimeInfo;
import com.dachen.mediecinelibraryrealize.entity.PatientBox;
import com.dachen.mediecinelibraryrealize.entity.PatientBoxsData;
import com.dachen.mediecinelibraryrealize.entity.Patients;
import com.dachen.mediecinelibraryrealize.entity.Patients.patient;
import com.dachen.mediecinelibraryrealize.utils.AlarmData;
import com.dachen.mediecinelibraryrealize.utils.JsonUtils.BoxTransLate;

@SuppressLint("ResourceAsColor")
@SuppressWarnings("deprecation")
public class PatientMedieBoxActivity2 extends BaseActivity implements  OnHttpListener, OnItemSelectedListener,OnClickListener {
	AlarmData p;
	private Gallery myGallery;
	BoxImageGalleryAdapter adapter ;
	TextView tv_name;
	RelativeLayout rl_back;
	TextView tv_data;
	ImageView iv_enent_black;
	ImageView iv_morning_black;
	ImageView iv_midday_black;
	ImageView iv_norn_black;
	TextView tv_enent_black;
	TextView tv_norn_black;
	TextView tv_morning_black;
	TextView tv_midday_black;
	LinearLayout ll_medie_alert;
	LinearLayout ll_medie_list;
	LinearLayout ll_often_eat;
	LinearLayout ll_collect;
	ImageView img;
	//指针
	ImageView iv_pointer;
	//蓝色圈
	ImageView iv_circleselect;
	TextView tv_usemediealert;
	TextView tv_medie_list;
	PopupWindow popupWindow;
	ImageView tv_maohao;
	RelativeLayout rl_plus;
	RelativeLayout.LayoutParams layoutParams ;
	HashMap<String, HashMap<String,List<AlarmsTimeInfo>>> maps;
	HashMap<String,HashMap<String, HashMap<String,List<AlarmsTimeInfo>>>> timemaps;
	RelativeLayout rl_circleselect;
	RelativeLayout rl_all;
	int select;
	int w1     ;
	int  h1     ;
	ArrayList<AlarmData>  alarmsTimes  ;
	TextView tv_medie_num;
	Button btn_checkpoint;
	LinearLayout ll_point;
	List<Patients.patient> patients;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
 			
 			/* layoutParams = new RelativeLayout.LayoutParams(50, 50);
				layoutParams.topMargin=h1;     
				layoutParams.leftMargin=w1;      
				layoutParams.rightMargin=8;    
				layoutParams.bottomMargin=8;  
				img = new ImageView(PatientMedieBoxActivity2.this); 
				
 			img.setBackgroundResource(R.drawable.have_eat);
			 img.setLayoutParams(new LayoutParams(100, 150));
			  rl_circleselect.addView(img,layoutParams); */
			addView();
		};
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_patientmedieboxs);
		alarmsTimes = new ArrayList<>();
		timemaps = new HashMap();
		tv_name = (TextView) this.findViewById(R.id.tv_title);
		patients = new ArrayList<patient>();
		myGallery = (Gallery) findViewById(R.id.myGallery);
		tv_medie_num = (TextView) findViewById(R.id.tv_medie_num);
		adapter = new BoxImageGalleryAdapter(this,alarmsTimes);
		myGallery.setAdapter(adapter);
		myGallery.setOnItemClickListener(new OnItemClickListenerImpl());
		myGallery.setOnItemSelectedListener(this);
		rl_all = (RelativeLayout) findViewById(R.id.rl_all);
		btn_checkpoint = (Button) findViewById(R.id.btn_checkpoint);
		btn_checkpoint.setOnClickListener(this);
		RelativeLayout ll_sub = (RelativeLayout) findViewById(R.id.ll_sub);
		ViewStub vstub_title = (ViewStub) findViewById(R.id.vstub_title);
		View view = vstub_title.inflate(this, R.layout.layout_plus_medie, ll_sub);
		//tv_maohao = (ImageView) view.findViewById(R.id.iv_plus);
	/*	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(90, 50); 
		params.topMargin = 64;
		params.leftMargin = 84;
		tv_maohao.setLayoutParams(params); 
		tv_maohao.setBackgroundResource(R.drawable.maohao);*/
		tv_name.setText("我的药箱");
		rl_plus = (RelativeLayout) view.findViewById(R.id.rl_plus);
		rl_plus.setOnClickListener(this);
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		rl_circleselect = (RelativeLayout) findViewById(R.id.rl_circleselect);
		rl_all.setVisibility(View.GONE);
		ll_point = (LinearLayout) findViewById(R.id.ll_point);
		initView();
		select = getIntent().getIntExtra("position", 0);

	}
	void initView(){
		tv_data = (TextView) findViewById(R.id.tv_data);
		iv_enent_black = (ImageView) findViewById(R.id.iv_enent_black);
		iv_morning_black = (ImageView) findViewById(R.id.iv_morning_black);
		iv_midday_black = (ImageView) findViewById(R.id.iv_midday_black);
		iv_norn_black = (ImageView) findViewById(R.id.iv_norn_black);
		tv_morning_black = (TextView) findViewById(R.id.tv_morning_black);
		tv_midday_black = (TextView) findViewById(R.id.tv_midday_black);
		tv_enent_black = (TextView) findViewById(R.id.tv_enent_black);
		tv_norn_black = (TextView) findViewById(R.id.tv_norn_black);
		tv_usemediealert = (TextView) findViewById(R.id.tv_usemediealert);
		tv_medie_list = (TextView) findViewById(R.id.tv_medie_list);
		this.findViewById(R.id.ll_medie_alert).setOnClickListener(this);
		this.findViewById(R.id.ll_medie_list).setOnClickListener(this);

		iv_enent_black.setOnClickListener(this);
		iv_morning_black.setOnClickListener(this);
		iv_midday_black.setOnClickListener(this);
		iv_norn_black.setOnClickListener(this);
		//iv_pointer = (ImageView) findViewById(R.id.iv_pointer);
		iv_circleselect = (ImageView) findViewById(R.id.iv_circleselect);
		showLoadingDialog();
		getPatient();
		if (null!=parseDate(TimeUtils.getTime())&&parseDate(TimeUtils.getTime()).length>3) {
			String[] s = parseDate(TimeUtils.getTime());
			tv_data.setText(s[1]+"月"+s[2]+"日  "+"  周"+s[3]);
		}
		maps = new HashMap<String, HashMap<String,List<AlarmsTimeInfo>>>();
		myGallery.setCallbackDuringFling(false);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	//http://192.168.3.7:9002/web/api/invoke/f6cf5b72a45e489c9305182c4653d22d/c_Recipe.get_user_patient_list
	public void getPatient(){
		String s = "org/drugReminder/getTodayDoseRecordLegendList";
		HashMap<String,String> interfaces = new HashMap<String,String>();
		interfaces.put("userId", UserInfo.getInstance(this).getId());
		interfaces.put("access_token", UserInfo.getInstance(this).getSesstion());
		new HttpManager().post(this,
				s,
				PatientBoxsData.class,
				interfaces,
				this, false, 1);

	}
	int[] getImageViewSize(ImageView iv){
		int dw = iv.getDrawable().getBounds().width();
		int dh = iv.getDrawable().getBounds().height();

		//获得ImageView中Image的变换矩阵
		Matrix m = iv.getImageMatrix();
		float[] values = new float[10];
		m.getValues(values);

		//Image在绘制过程中的变换矩阵，从中获得x和y方向的缩放系数
		float sx = values[0];
		float sy = values[4];

		//计算Image在屏幕上实际绘制的宽高
		int cw = (int)(dw * sx);
		int ch = (int)(dh * sy);
		return new int[]{cw,ch};

	}
	void addView(){
		int width = rl_circleselect.getWidth();
		int height = rl_circleselect.getHeight();
		int[] imageSize = getImageViewSize(iv_circleselect);
		int imagew = imageSize[0]/2;
		int imageh = imageSize[1]/2;

		int[] location = new int[2];
		rl_circleselect.getLocationOnScreen(location);
		rl_circleselect.getLeft();
		int w = 0;
		int h = 0;
		w = rl_circleselect.getLeft()+width/2;
		h = rl_circleselect.getTop()+height/2 ;
		float imageMargin =getResources().getDimension(R.dimen.imagesize);
		rl_circleselect.removeAllViews();
		for (int i = 0; i < 4; i++) {
			final List<AlarmsTimeInfo> infos = getInfos(maps,((i+1)+""));

			for ( int j = 0; j < infos.size(); j++) {
				int ra1 = (int)(Math.random()/1.5*imagew);;
				int ra2 = (int)(Math.random()/1.5*imageh);
				if (i==3) {
					if (ra1<imageMargin) {
						ra1 =  (int) (ra1+imageMargin);
					}
					if (ra2<imageMargin) {
						ra2 = (int) (ra2+imageMargin);
					}
					w1 = w  - ra1;
					h1 = h  - ra2;
				}else if (i ==0) {
					if (ra1>imagew/1.5-50) {
						ra1 =  (int) (imagew/1.5-imageMargin);//60
					}
					if (ra2<imageMargin) {
						ra2 = (int) (ra2+imageMargin);
					}
					w1 = w  + ra1;
					h1 = h  - ra2;
				}else if (i ==1) {
					if (ra1>imagew/1.5-50) {
						ra1 =  (int) (imagew/1.5-imageMargin);//
					}
					if (ra2>imagew/1.5-50) {
						ra2 = (int) (imageh/1.5-imageMargin);
					}
					w1 = w  + ra1;
					h1 = h  + ra2;
				}else if (i ==2) {
					if (ra1<imageMargin) {
						ra1 =  (int) (ra1+imageMargin);
					}
					if (ra2>imageh/1.5-50) {
						ra2 = (int) (ra2-imageMargin);
					}
					w1 = w  - ra1;
					h1 = h  + ra2;
				}
				final AlarmsTimeInfo info = infos.get(j);
				float size =getResources().getDimension(R.dimen.imagesize);
				layoutParams = new RelativeLayout.LayoutParams((int)size, (int)size);
				layoutParams.topMargin=h1;
				layoutParams.leftMargin=w1;
				layoutParams.rightMargin=8;
				layoutParams.bottomMargin=8;
				img = new ImageView(this);
				if (info.is_done) {
					img.setBackgroundResource(R.drawable.have_eat);
				}else {
					img.setBackgroundResource(R.drawable.no_eat);
					final Integer ws1 = new Integer(w1);
					final Integer wh1 = new Integer(h1);
					img.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							AlarmDialog dialog = new AlarmDialog(p.patientId,ws1,wh1,PatientMedieBoxActivity2.this,info,new RefershData() {

								@Override
								public void getData(boolean isSuccess , int w,int h) {
									// TODO Auto-generated method stub

									if (isSuccess) {
										w1= w;
										h1=h;
										handler.sendEmptyMessage(0);
									}
								}
							});
							//dialog.show();
						}
					});
				}

				img.setLayoutParams(new LayoutParams((int) size, (int) size));
				rl_circleselect.addView(img, layoutParams);
				//handler.sendEmptyMessage(0);
			}
		}


	}
	public interface RefershData{
		public void getData(boolean isSuccess, int w, int h);
	}
	private class OnItemClickListenerImpl implements OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View view, int position,

								long id) {

		/*	Toast.makeText(PatientMedieBoxActivity2.this,
					String.valueOf(position),

					Toast.LENGTH_SHORT).show();*/

		}

	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
							   long arg3) {
		// TODO Auto-generated method stub
		adapter.setSelectItem(position);  //当滑动时，事件响应，调用适配器中的这个方法。
		select = position;
		if (null!=alarmsTimes&&alarmsTimes.size()>0) {
			p =alarmsTimes.get(position);

			maps = timemaps.get(p.patientId);
			if (0!=p.reminderNumer) {
				tv_usemediealert.setText("用药提醒"+"("+p.reminderNumer+")");
			} else {
				tv_usemediealert.setText("用药提醒(0)");
			}
			if (0!=p.recipeNumber) {
				tv_medie_list.setText("药品清单"+"("+p.recipeNumber+")");
			}else {
				tv_medie_list.setText("药品清单(0)");
			}
			//CompareDatalogic.isShow()
			if (p.pointsNumber!=0){
				tv_medie_num.setText("您还有"+p.pointsNumber+"个可兑换药品");
				ll_point.setVisibility(View.VISIBLE);
			}else{
				ll_point.setVisibility(View.GONE);
			}

			if (null!=parseDate(TimeUtils.getTime())&&parseDate(TimeUtils.getTime()).length>4) {
				String[] s = parseDate(TimeUtils.getTime());
				int hour = Integer.parseInt(s[5]);
				if (hour>=4&&hour<12) {
					iv_circleselect.setImageResource(R.drawable.turntableselect_1);

					iv_morning_black.setBackgroundResource(R.drawable.morning_light);
					iv_midday_black.setBackgroundResource(R.drawable.midday_black);
					iv_norn_black.setBackgroundResource(R.drawable.norn_black);
					iv_enent_black.setBackgroundResource(R.drawable.enent_black);

					tv_morning_black.setTextColor(getResources().getColor(R.color.color_333333));
					tv_midday_black.setTextColor(getResources().getColor(R.color.color_d3d3d3));
					tv_norn_black.setTextColor(getResources().getColor(R.color.color_d3d3d3));
					tv_enent_black.setTextColor(getResources().getColor(R.color.color_d3d3d3));

				}else if(hour>=12&&hour<18){
					iv_morning_black.setBackgroundResource(R.drawable.morning_black);
					iv_morning_black.setBackgroundResource(R.drawable.morning_black);
					iv_midday_black.setBackgroundResource(R.drawable.midday_light);
					iv_norn_black.setBackgroundResource(R.drawable.norn_black);
					iv_enent_black.setBackgroundResource(R.drawable.enent_black);
					tv_morning_black.setTextColor(getResources().getColor(R.color.color_d3d3d3));
					tv_midday_black.setTextColor(getResources().getColor(R.color.color_333333));
					tv_norn_black.setTextColor(getResources().getColor(R.color.color_d3d3d3));
					tv_enent_black.setTextColor(getResources().getColor(R.color.color_d3d3d3));
				}else if(hour>=18&&hour<24){
					iv_circleselect.setImageResource(R.drawable.turntableselect_3);
					iv_morning_black.setBackgroundResource(R.drawable.morning_black);
					iv_midday_black.setBackgroundResource(R.drawable.midday_black);
					iv_norn_black.setBackgroundResource(R.drawable.norn_light);
					iv_enent_black.setBackgroundResource(R.drawable.enent_black);
					tv_morning_black.setTextColor(getResources().getColor(R.color.color_d3d3d3));
					tv_midday_black.setTextColor(getResources().getColor(R.color.color_d3d3d3));
					tv_norn_black.setTextColor(getResources().getColor(R.color.color_cccccc));
					tv_enent_black.setTextColor(getResources().getColor(R.color.color_d3d3d3));

				}else if(hour>=0&&hour<4){
					iv_circleselect.setImageResource(R.drawable.turntableselect_4);
					iv_morning_black.setBackgroundResource(R.drawable.morning_black);
					iv_midday_black.setBackgroundResource(R.drawable.midday_black);
					iv_norn_black.setBackgroundResource(R.drawable.norn_black);
					iv_enent_black.setBackgroundResource(R.drawable.event_light);
					tv_morning_black.setTextColor(getResources().getColor(R.color.color_d3dcdc));
					tv_midday_black.setTextColor(getResources().getColor(R.color.color_d3dcdc));
					tv_norn_black.setTextColor(getResources().getColor(R.color.color_d3dcdc));
					tv_enent_black.setTextColor(getResources().getColor(R.color.color_333333));
				}
			}

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					handler.sendEmptyMessage(0);
				}
			}).start();

		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onClick(View v) {
		Intent intents = new Intent(this,TimeDetailActivity.class);
		if (null!=p&& !TextUtils.isEmpty(p.patientId)){
			intents.putExtra("Patientid", p.patientId);
		}

		Bundle bundle = new Bundle();
		// TODO Auto-generated method stub
		if (v.getId() == R.id.rl_back) {
			finish();

		} else if (v.getId() == R.id.ll_medie_alert) {
			if (null!=p&& !TextUtils.isEmpty(p.patientId)){

				Intent intent = new Intent(this,ShowAlertActivity.class);
				intent.putExtra("Patientid", p.patientId);
				intent.putExtra("Patientname", p.patientName);
				startActivityForResult(intent,1);

			}
		}else if (v.getId() == R.id.ll_medie_list) {
			if (null!=p&& !TextUtils.isEmpty(p.patientId)) {
				Intent intent = new Intent(this, PatientMedieBoxActivity.class);
				intent.putExtra("id", p.patientId);
				intent.putExtra("name", p.patientName);
				startActivityForResult(intent, 100);
			}
		}else if (v.getId() == R.id.ll_often_eat) {
			Intent intent = new Intent(this,ChooseUsualMedicienActivity.class);
			startActivityForResult(intent, 300);
			//showpopup();
			popupWindow.dismiss();
		}else if (v.getId() == R.id.ll_points) {
			if (null!=p&& !TextUtils.isEmpty(p.patientId)) {
				Intent intent = new Intent(this, PatientPointsActivity.class);
				Bundle bundles = new Bundle();
				bundles.putSerializable("patients", (Serializable) patients);
				intent.putExtra("patients", bundles);
				intent.putExtra("id", p.patientId);
				startActivity(intent);
			}
			//showpopup();
			popupWindow.dismiss();
		}
		else if (v.getId() == R.id.iv_enent_black) {
			if (null!=p&& !TextUtils.isEmpty(p.patientId)) {
				overridePendingTransition(R.anim.i_slide_in_left, R.anim.i_slide_in_right);
				bundle.putSerializable("times", (Serializable) getInfos(maps, (4 + "")));
				intents.putExtra("period", "凌晨");
				intents.putExtra("times", bundle);
				intents.putExtra("position", select);
				intents.putExtra("pe", "00:00-04:00");
				startActivity(intents);
			}
		}
		else if (v.getId() == R.id.iv_morning_black) {
			if (null!=p&& !TextUtils.isEmpty(p.patientId)) {
				overridePendingTransition(R.anim.i_slide_in_left, R.anim.i_slide_in_right);
				bundle.putSerializable("times", (Serializable) getInfos(maps, (1 + "")));

				intents.putExtra("period", "上午");
				intents.putExtra("times", bundle);
				intents.putExtra("pe", "04:00-12:00");
				intents.putExtra("position", select);
				startActivity(intents);
			}
		}
		else if (v.getId() == R.id.iv_midday_black) {
			if (null!=p&& !TextUtils.isEmpty(p.patientId)) {
				overridePendingTransition(R.anim.i_slide_in_left, R.anim.i_slide_in_right);
				bundle.putSerializable("times", (Serializable) getInfos(maps, (2 + "")));

				intents.putExtra("period", "下午");
				intents.putExtra("pe", "12:00-18:00");
				intents.putExtra("position", select);
				intents.putExtra("times", bundle);
				startActivity(intents);
			}
		}
		else if (v.getId() == R.id.iv_norn_black) {
			if (null!=p&& !TextUtils.isEmpty(p.patientId)) {
				overridePendingTransition(R.anim.i_slide_in_left, R.anim.i_slide_in_right);
				bundle.putSerializable("times", (Serializable) getInfos(maps, (3 + "")));

				intents.putExtra("period", "夜晚");
				intents.putExtra("pe", "18:00-24:00");
				intents.putExtra("position", select);
				intents.putExtra("times", bundle);
				startActivity(intents);
			}
		}else if (v.getId() == R.id.rl_plus) {
			showpopup();
		}/*else if (v.getId() == R.id.ll_collections){
			*//*Intent intent = new Intent(this,CollectionMedieActivity.class);
			if (null!=patients&&patients.size()>0){
				bundle.putSerializable("patient", (Serializable) patients);
				intent.putExtra("patient", bundle);
				intent.putExtra("id", p.id);
				intent.putExtra("name", p.user_name);
				startActivity(intent);
			}*//*

		}*/else if (v.getId() == R.id.btn_checkpoint){
			if (p.recipeNumber==0){
				ToastUtils.showToast(this,"没有可领取的药品");
			}else{
			/*	Intent intent = new Intent(this,PointDetailActivity.class);
				intent.putExtra("id", p.id);
				startActivity(intent);*/
				if (null!=p&& !TextUtils.isEmpty(p.patientId)) {
					Intent intent = new Intent(this, PatientPointsActivity.class);
					Bundle bundles = new Bundle();
					bundles.putSerializable("patients", (Serializable) patients);
					intent.putExtra("patients", bundles);
					intent.putExtra("id", p.patientId);
					startActivity(intent);
				}
			}

		}
	}
	public List<AlarmsTimeInfo> getInfos(HashMap<String, HashMap<String,List<AlarmsTimeInfo>>> mapss , String key){
		List<AlarmsTimeInfo> infos = new ArrayList<AlarmsTimeInfo>();
		if (null != mapss.get(key)) {
			//LogUtils.burtLog("mapss.get(key)=="+mapss.get(key).size());
			HashMap<String,List<AlarmsTimeInfo>> mapsinfos = mapss.get(key);
			Iterator iter = mapsinfos.entrySet().iterator();
			List<List<AlarmsTimeInfo>> infosNew = new ArrayList<>();
			while (iter.hasNext()) {

				Map.Entry entry = (Map.Entry) iter.next();

				List<AlarmsTimeInfo> val = (List<AlarmsTimeInfo> )entry.getValue();
				infosNew.add(val);
				try {
					Collections.sort(infosNew, new Comparator<List<AlarmsTimeInfo>>() {
						@Override
						public int compare(List<AlarmsTimeInfo> lhs, List<AlarmsTimeInfo> rhs) {
							if (!TextUtils.isEmpty(lhs.get(0).time)&&!TextUtils.isEmpty(rhs.get(0).time)){
								String[] s1 = lhs.get(0).time.split(":");
								String[] s2 = rhs.get(0).time.split(":");
								long time1 = TimeUtils.getTime(Integer.parseInt(s1[0]), Integer.parseInt(s1[1]));
								long time2 = TimeUtils.getTime(Integer.parseInt(s2[0]),Integer.parseInt(s2[1]));
								if ((time1-time2)>0){
									return 1;
								}
								return -1;
							}
							return -1;
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			for (int k=0;k<infosNew.size();k++){
				List<AlarmsTimeInfo> val = (List<AlarmsTimeInfo> )infosNew.get(k);
				try {
					Collections.sort(val, new Comparator<AlarmsTimeInfo>() {
						@Override
						public int compare(AlarmsTimeInfo lhs, AlarmsTimeInfo rhs) {
							if (!TextUtils.isEmpty(lhs.time)&&!TextUtils.isEmpty(rhs.time)){
								String[] s1 = lhs.time.split(":");
								String[] s2 = rhs.time.split(":");
								long time1 = TimeUtils.getTime(Integer.parseInt(s1[0]), Integer.parseInt(s1[1]));
								long time2 = TimeUtils.getTime(Integer.parseInt(s2[0]),Integer.parseInt(s2[1]));
								if ((time1-time2)>0){
									return 1;
								}
							}
							return -1;
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				ArrayList<AlarmsTimeInfo> list = new ArrayList<AlarmsTimeInfo>();
				for (int i = 0; i < val.size(); i++) {
					AlarmsTimeInfo info =val.get(i);
					info.size = val.size();
					info.position = i;
					list.add(info);
				}
				for (int i = 0; i < val.size(); i++) {
					AlarmsTimeInfo info =val.get(i);
					info.size = val.size();
					info.position = i;
					info.infos = list;
					infos.add(info);
				}

			}

		}

		return infos;
	}
	@Override
	public void onFailure(Exception arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub
		ToastUtils.showToast(this,"没有数据");
	}

	@Override
	protected void onPause() {
		super.onPause();
		closeLoadingDialog();
	}

	@Override
	public void onSuccess(Result arg0) {
		// TODO Auto-generated method stub
		closeLoadingDialog();
		if (arg0.resultCode==1){
		if (arg0 instanceof PatientBoxsData) {

			PatientBoxsData box = (PatientBoxsData)arg0;
			alarmsTimes = BoxTransLate.getAlarm(box.data);
			if (alarmsTimes.size()==0){
				return;
			}
			rl_all.setVisibility(View.VISIBLE);

			adapter = new BoxImageGalleryAdapter(this,alarmsTimes);
			myGallery.setAdapter(adapter);
			if (select!=0) {
				myGallery.setSelection(select);
			}else {
				myGallery.setSelection(0);
			}
			patients = BoxTransLate.getPatients(alarmsTimes);

			for(int j=0;j<alarmsTimes.size();j++){
				if (null!=alarmsTimes.get(j)) {
					List<AlarmsTimeInfo> list_datas;
					if (null!= alarmsTimes.get(j)&&null!=alarmsTimes.get(j).alarmsTimes) {
						list_datas  = alarmsTimes.get(j).alarmsTimes;

					}else {
						continue;
					}
					HashMap<String, HashMap<String,List<AlarmsTimeInfo>>> maps = new HashMap<>();
					for (int i = 0; i < list_datas.size(); i++) {
						AlarmsTimeInfo info = list_datas.get(i);
						HashMap<String,List<AlarmsTimeInfo>> times = new HashMap<String, List<AlarmsTimeInfo>>();;
						List<AlarmsTimeInfo> infolist = new ArrayList<AlarmsTimeInfo>(); ;
						String type = "0";
						if (null!=info&&!TextUtils.isEmpty(list_datas.get(i).time)&&list_datas.get(i).time.length()>3){
							type = list_datas.get(i).time.substring(0,2);
							HashMap<String, List<AlarmsTimeInfo>> sets = (HashMap<String, List<AlarmsTimeInfo>>)
									maps.get(list_datas.get(i).time_type+"");
							if (sets!=null&&sets.size()>0) {
								times = sets;
								if (null==sets.get(type)) {
									infolist = new ArrayList<AlarmsTimeInfo>();
								}else {
									infolist = times.get(type+"");
								}

							}else {
								times = new HashMap<String, List<AlarmsTimeInfo>>();
								infolist = new ArrayList<AlarmsTimeInfo>();

							}
						}
						info.intergTime=type;
						infolist.add(info);
						times.put(type,infolist);
						maps.put(list_datas.get(i).time_type+"",times);
					}

					timemaps.put(alarmsTimes.get(j).patientId,maps);
				}
			}

		}else {
			ToastUtils.showToast(this,"没有数据");
			rl_all.setVisibility(View.GONE);
		}
		}else {
			ToastUtils.showResultToast(this,arg0);
		}
	}
	@Override
	public void onSuccess(ArrayList arg0) {
		// TODO Auto-generated method stub

	}
	public static String[] parseDate(String strDate) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date returnDate = null;
		try {
			returnDate = dateFormat.parse(strDate);
		} catch (ParseException e) {

		}
		Calendar c = Calendar.getInstance();
		c.setTime(returnDate);
		String[] s = new String[6];
		s[0] = (c.get(Calendar.YEAR)) + "";
		s[1] = (c.get(Calendar.MONTH) + 1) + "";
		s[2] = c.get(Calendar.DATE) + "";
		s[3] = change((c.get(Calendar.DAY_OF_WEEK) - 1) + "");

		long mills = 0;
		mills = c.getTimeInMillis();
		s[4] = mills+"";
		s[5] = c.get(Calendar.HOUR_OF_DAY)+"";
		return s;
	}
	public static String change(String s){
		String week =s;
		if (s.equals("1")) {
			week = "一";
		}else if (s.equals("2")) {
			week = "二";
		}else if (s.equals("3")) {
			week = "三";
		}else if (s.equals("4")) {
			week = "四";
		}else if (s.equals("5")) {
			week = "五";
		}else if (s.equals("6")) {
			week = "六";
		}else if (s.equals("7")) {
			week = "日";
		}else if (s.equals("0")) {
			week = "日";
		}
		return week;
	}
	public void showCustomDialog(){
		MyDialog myDialog = new MyDialog(PatientMedieBoxActivity2.this);
		myDialog.setDialogCallback(dialogcallback);
		myDialog.show();
	}
	Dialogcallback dialogcallback = new Dialogcallback() {
		@Override
		public void dialogdo(String string) {


		}
	};

	public void showpopup(){
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.popupwindow_mybox, null);

		RelativeLayout rl_bottom = (RelativeLayout) layout.findViewById(R.id.rl_bottom);
		popupWindow = new PopupWindow(layout);
		popupWindow.setFocusable(true);// 取得焦点
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.getContentView().findViewById(R.id.ll_often_eat).setOnClickListener(this);
		popupWindow.getContentView().findViewById(R.id.ll_points).setOnClickListener(this);
//	 popupWindow.getContentView().findViewById(R.id.ll_collections).setOnClickListener(this);
		// 控制popupwindow的宽度和高度自适应
/*	rl_bottom.measure(View.MeasureSpec.UNSPECIFIED,  
            View.MeasureSpec.UNSPECIFIED); */
		//   popupWindow.setWidth(rl_bottom.getMeasuredWidth()+30);
		// popupWindow.setHeight((rl_bottom.getMeasuredHeight() + 20));

		popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
	 /*       // 控制popupwindow点击屏幕其他地方消失  
       popupWindow.setBackgroundDrawable(this.getResources().getDrawable(  
	                R.drawable.bg_popupwindow));// 设置背景图片，不能在布局中设置，要通过代码来设置  
*/       popupWindow.setOutsideTouchable(true);// 
		popupWindow.showAsDropDown(rl_plus, 0,3);

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (null!=data&&null!=data.getStringExtra("refresh")) {
			int size = data.getIntExtra("size", -1);
			if (-1 != size) {
				tv_medie_list.setText("药品清单"+"("+size+")");
			}else {
				tv_medie_list.setText("药品清单(0)");
			}
		}
		if(requestCode==1){
			getPatient();
		}

	}

}