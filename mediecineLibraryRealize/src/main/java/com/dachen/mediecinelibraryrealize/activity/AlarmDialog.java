package com.dachen.mediecinelibraryrealize.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.dachen.medicine.common.utils.Alarm;
import com.dachen.medicine.common.utils.DrugRemind;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.TimeUtils;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.activity.PatientMedieBoxActivity2.RefershData;
import com.dachen.mediecinelibraryrealize.entity.AlarmDao;
import com.dachen.mediecinelibraryrealize.entity.AlarmsTime.AlarmsTimeInfo;
import com.dachen.mediecinelibraryrealize.entity.DrugRemindDao;
import com.dachen.mediecinelibraryrealize.entity.Patients;

/**
 * 自定义dialog
 * 
 * @author sfshine
 * 
 */
public class AlarmDialog implements OnHttpListener{
	Context context;
	Dialogcallback dialogcallback;
	Dialog dialog; 
	TextView content; 
	TextView time;
	AlarmsTimeInfo info;
	RefershData refresh;
	int w;
	int h;
	public String patientid;
	/**
	 * init the dialog
	 * 
	 * @return
	 */
	public AlarmDialog(String id,int w,int h,Context con,AlarmsTimeInfo info,RefershData refresh) {
		this.patientid = id;
		this.info = info;
		this.context = con;
		this.w = w;
		this.h = h;
		dialog = new Dialog(context, R.style.addresspickerstyle);
		// dialog.setContentView(R.layout.dialog);
		this.refresh = refresh;
		View mView = LayoutInflater.from(context)
				.inflate(R.layout.activity_alarm_dialogs, null); 
		LogUtils.burtLog("==============info=="+info);
		
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		float mWidth = con.getResources().getDisplayMetrics().widthPixels;
		float mDensity = con.getResources().getDisplayMetrics().density;
		//dialog.getWindow().setBackgroundDrawableResource(R.drawable.);
		dialog.setContentView(mView);
		dialog.getWindow().setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (int) (mWidth - 48 * mDensity);
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		dialog.findViewById(R.id.left_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {  
				  List<DrugRemind>  listsDrugRemind = DrugRemindDao.getInstance(context).
						  queryByids(AlarmDialog.this.info.id,patientid);
					//  List<Alarm>  lists = AlarmDao.getInstance(context).queryByids(info.id);
					 if (null!=listsDrugRemind) {
						 for (int i = 0; i < listsDrugRemind.size(); i++) {
							if (listsDrugRemind.get(i).id.equals(AlarmDialog.this.info.id)) {
								Collection<Alarm> alarms = listsDrugRemind.get(i).alarms;
								for (Alarm  ala :alarms ) {
									String s = String.format("%02d:%02d", ala.hour, ala.minute);
									if (s.equals(AlarmDialog.this.info.time)) {
										//AlarmBusiness.cancelAlarm(context, ala);
										//ala.eat = System.currentTimeMillis();
										ala.isStep = System.currentTimeMillis();
										ala.eat = -1;
										AlarmDao.getInstance(context).update(ala);
										break;
									}
								}
							}
						 } 
						} ;
				dismiss();
			}
		});
		dialog.findViewById(R.id.right_btn).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						
						getPatient(AlarmDialog.this.info.time,AlarmDialog.this.info.id);
						 List<DrugRemind>  listsDrugRemind = DrugRemindDao.getInstance(context).queryByids(AlarmDialog.this.info.id,patientid);
							//  List<Alarm>  lists = AlarmDao.getInstance(context).queryByids(info.id);
							 if (null!=listsDrugRemind) {
								 for (int i = 0; i < listsDrugRemind.size(); i++) {
									if (listsDrugRemind.get(i).id.equals(AlarmDialog.this.info .id)) {
										Collection<Alarm> alarms = listsDrugRemind.get(i).alarms;
										for (Alarm  ala :alarms ) {
											String s = String.format("%02d:%02d", ala.hour, ala.minute);
											if (s.equals(AlarmDialog.this.time)) {
												//AlarmBusiness.cancelAlarm(context, ala);
												ala.eat = System.currentTimeMillis();
												ala.isStep =-1;
												//ala.isStep = System.currentTimeMillis();
												AlarmDao.getInstance(context).update(ala);
												break;
											}
										}
									}
								 }
						 }
							 	dismiss();
					} 
				});
		if (null!=info) {
			
			time = (TextView) dialog.findViewById(R.id.times);
			content =  (TextView) dialog.findViewById(R.id.content);
			 
			if (null!=time) {
				time.setText("服药时间："+info.time);
			}
		 	if (null!=content) {
		 		content.setText(info.goods_name);
			}
		
		}
	}
	public void getPatient(String alerttime,String owner){ 
		HashMap<String, String> interfaces = new HashMap<String, String>();
		interfaces.put("remind_date", TimeUtils.getTimeDay());
		interfaces.put("owner", owner);
		interfaces.put("remind_time", alerttime);
		interfaces.put("done_time", TimeUtils.getTimes()); 
		 new HttpManager().post(context,
				Params.getInterface("invoke", "c_remind_patient.create_remind_do"),
				Patients.class,
				interfaces,
				this, false, 2,false);
		
	}
	/**
	 * 设定一个interfack接口,使mydialog可以處理activity定義的事情
	 * 
	 * @author sfshine
	 * 
	 */
	public interface Dialogcallback {
		public void dialogdo(String string);
	}

	public void setDialogCallback(Dialogcallback dialogcallback) {
		this.dialogcallback = dialogcallback;
	}

	/**
	 * @category Set The Content of the TextView
	 * */
	public void setContent(String content) { 
	}

	/**
	 * Get the Text of the EditText
	 * */
 

	public void show() {
		
		dialog.show();
	}

	public void hide() {
		dialog.hide();
	}

	public void dismiss() {
		dialog.dismiss();
	}

	@Override
	public void onFailure(Exception arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccess(Result arg0) {
		// TODO Auto-generated method stub
		if (null!=arg0&&arg0.toString().contains( "true")) {
			refresh.getData(true,w,h);
			ToastUtils.showToast(context,"已服用！");
		}
	}

	@Override
	public void onSuccess(ArrayList arg0) {
		// TODO Auto-generated method stub
		
	}
}