package com.dachen.mediecinelibraryrealize.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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
import com.dachen.mediecinelibraryrealize.entity.AlarmDao;
import com.dachen.mediecinelibraryrealize.entity.AlarmsTime;
import com.dachen.mediecinelibraryrealize.entity.AlarmsTime.AlarmsTimeInfo;
import com.dachen.mediecinelibraryrealize.entity.DrugRemindDao;
import com.dachen.mediecinelibraryrealize.entity.Patients;
import com.dachen.mediecinelibraryrealize.utils.CompareDatalogic;
import com.dachen.mediecinelibraryrealizedoctor.entity.SearchMedicineEntity;
import com.google.gson.Gson;

public class AdapterTimeDetail extends BaseAdapter implements OnHttpListener{
	Context context; 
	List<AlarmsTimeInfo> infos;
	List<AlarmsTimeInfo> eatinfos;
	AlarmsTimeInfo info;
	String Patientid;
	public AdapterTimeDetail(Context context, List<AlarmsTimeInfo> infos,String Patientid){
		this.context = context;
		this.infos = infos;
		this.Patientid = Patientid;
		eatinfos = new ArrayList<>();
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
			view = View.inflate(context, R.layout.adapter_time_detail, null);
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
		String name = CompareDatalogic.getShowName2(info.general_name,info.goods_name,info.title);
		if (info.position==0) {
			holder.ll_time.setVisibility(View.VISIBLE); 
			holder.name.setText(name);
			holder.time.setText(info.time);
		}else if (info.position!=0) { 
			
				holder.ll_time.setVisibility(View.GONE);
				holder.time.setText(info.intergTime);

			holder.name.setText(name);
			holder.time.setText(info.time);

		}
		if (info.is_done) {
			holder.iv_eatimg.setBackgroundResource(R.drawable.have_eat);
			 holder.btn_eat.setVisibility(View.GONE);
			// holder.btn_step.setVisibility(View.INVISIBLE);
			 holder.have_eat.setVisibility(View.VISIBLE);
			 holder.have_eat.setText("已服用");
		}else {
			holder.iv_eatimg.setBackgroundResource(R.drawable.no_eat);
			 holder.btn_eat.setVisibility(View.VISIBLE);
			// holder.btn_step.setVisibility(View.VISIBLE);
			 holder.have_eat.setVisibility(View.GONE); 
		}
		holder.btn_eat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				eatinfos.clear();
				// TODO Auto-generated method stub
				eatinfos.add(info);
				getPatient(eatinfos);
				List<DrugRemind> listsDrugRemind = DrugRemindDao.getInstance(context).queryByids(info.id, Patientid);
				//  List<Alarm>  lists = AlarmDao.getInstance(context).queryByids(info.id);
				if (null != listsDrugRemind) {
					for (int i = 0; i < listsDrugRemind.size(); i++) {
						if (listsDrugRemind.get(i).id.equals(info.id)) {
							Collection<Alarm> alarms = listsDrugRemind.get(i).alarms;
							for (Alarm ala : alarms) {
								String s = String.format("%02d:%02d", ala.hour, ala.minute);
								if (s.equals(info.time)) {
									//AlarmBusiness.cancelAlarm(context, ala);
									ala.eat = System.currentTimeMillis();
									//ala.isStep = System.currentTimeMillis();
									AlarmDao.getInstance(context).update(ala);
									break;
								}
							}
						}
					}
				}
				holder.btn_eat.setVisibility(View.GONE);
				//holder.btn_step.setVisibility(View.INVISIBLE);
				holder.have_eat.setVisibility(View.VISIBLE);
				holder.have_eat.setText("已服用");

			}
		});
		holder.btn_eat_all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//更新数据库
				for (int k = 0; k < info.infos.size(); k++) {
					AlarmsTimeInfo info1 = info.infos.get(k);
					String id = info1.infos.get(k).id;
					List<DrugRemind> listsDrugRemind = DrugRemindDao.getInstance(context).queryByids(id, Patientid);
					//  List<Alarm>  lists = AlarmDao.getInstance(context).queryByids(info.id);
					if (null != listsDrugRemind) {
						for (int i = 0; i < listsDrugRemind.size(); i++) {
							if (listsDrugRemind.get(i).id.equals(id)) {
								Collection<Alarm> alarms = listsDrugRemind.get(i).alarms;
								for (Alarm ala : alarms) {
									String s = String.format("%02d:%02d", ala.hour, ala.minute);
									if (s.equals(info1.time)) {
										ala.eat = System.currentTimeMillis();
										AlarmDao.getInstance(context).update(ala);
									}
								}
							}
						}
					}
				}
				//更新UI
				eatinfos.clear();
				if (!TextUtils.isEmpty(info.intergTime) && info.infos != null) {
					for (int i = 0; i < infos.size(); i++) {

						AlarmsTimeInfo info1 = infos.get(i);
						if (!TextUtils.isEmpty(info1.time) && info1.time.length() > 3 && info1.time.substring(0, 2).equals(info.intergTime)) {
							ArrayList<AlarmsTimeInfo> infoss = new ArrayList<AlarmsTimeInfo>();

							info1.infos = infoss;
							info1.is_done = true;
							infos.set(i, info1);
							eatinfos.add(info1);
						}
					}
				}
				getPatient(eatinfos);
				AdapterTimeDetail.this.notifyDataSetChanged();
			}
		});
		if (info.size>1&&info.position==info.size-1) {
			holder.rl_all.setVisibility(View.VISIBLE);
			
		}else {
			holder.rl_all.setVisibility(View.GONE);
		}
		holder.view2.setVisibility(View.VISIBLE);
		if ( info.position==0||info.position!=info.size-1) {
			holder.view2.setVisibility(View.GONE);
		}
		boolean flag = false;
	
		holder.have_eat_all.setVisibility(View.GONE);
		if (info.infos.size()>1) {
			for (int i = 0;i<info.infos.size(); i++) {
				if (!info.infos.get(i).is_done) {
					
					flag = true;
					break;
				}
			}	
		}else {
			holder.have_eat_all.setVisibility(View.GONE);
		}
		if (flag) {
			holder.have_eat_all.setVisibility(View.GONE);
			holder.btn_eat_all.setVisibility(View.VISIBLE);
			if (info.size>1&&info.position==info.size-1) {
				holder.rl_all.setVisibility(View.VISIBLE);

			}else {
				holder.rl_all.setVisibility(View.GONE);
			}
			//holder.btn_step_all.setVisibility(View.VISIBLE);
		}else {
			holder.btn_eat_all.setVisibility(View.GONE);
			//holder.btn_step_all.setVisibility(View.INVISIBLE);
			holder.have_eat_all.setVisibility(View.VISIBLE);
			holder.have_eat_all.setText("已服用");
			holder.rl_all.setVisibility(View.GONE);
		} 
		return view;
	} 
	 
/*	public void getPatient(String alerttime,String owner){
		HashMap<String, String> interfaces = new HashMap<String, String>();
		interfaces.put("remind_date", TimeUtils.getTimeDay());
		interfaces.put("owner", owner);
		interfaces.put("remind_time", alerttime);
		interfaces.put("done_time", TimeUtils.getTimes()); 
		 	new HttpManager().post(context,
				Params.getInterface("invoke", "c_remind_patient.create_remind_do"),
				Patients.class,
				interfaces,
				this, false, 2, false);

	}*/
	public void getPatient(List<AlarmsTimeInfo> eatinfos){
		String s = "org/drugReminder/submitDoseRecord";
		HashMap<String, String> interfaces = new HashMap<String, String>();
		interfaces.put("access_token", UserInfo.getInstance(context).getSesstion());
		interfaces.put("reminderRecord", "2");
		ArrayList<String> ids = new ArrayList<>();
		String id = "";
		for (int i=0;i<eatinfos.size();i++){
			ids.add(eatinfos.get(i).id);
			if (i!=0){
				id +=","+eatinfos.get(i).id;
			}else {
				id +=eatinfos.get(i).id;
			}
		}
		Gson gson = new Gson();
		interfaces.put("doseRecordId", id);
		new HttpManager().post(context,
				s,
				Patients.class,
				interfaces,
				this, false, 1);

	}
	/*public void getPatientList(){
		String list = "json:[";
		//String result = JSON.toJSONString(obj);
		//ArrayList<AlarmInfo> S = 
		for (int i = 0; i < eatinfos.size(); i++) {
			AlarmsTimeInfo info = eatinfos.get(i);
			if (i!=0) {
				list=list+",{"+"\""+"owner"+"\""+":"+"\""+info.id+"\""+","+"\""+"remind_time"+"\""+":"+"\""+info.time+"\""+"}";
			}else {
				list=list+"{"+"\""+"owner"+"\""+":"+"\""+info.id+"\""+","+"\""+"remind_time"+"\""+":"+"\""+info.time+"\""+"}";
			} 
		}
		list+="]";
		HashMap<String, String> interfaces = new HashMap<String, String>();
		interfaces.put("remind_date", TimeUtils.getTimeDay());
		interfaces.put("list_datas", list); 
		interfaces.put("done_time", TimeUtils.getTimes()); 
		new HttpManager().post(context,
				Params.getInterface("invoke", "c_remind_patient.create_remind_dos"),
				Patients.class,
				interfaces,
				this, false, 2,false);
		
	}*/
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
//{"resultCode":1,"resultMsg":"success"}
		if (arg0!=null&&!TextUtils.isEmpty(arg0.resultMsg)&&arg0.resultMsg.contains( "success")) {
			ToastUtils.showToast(context,"已服用！");
		}
	}
	@Override
	public void onSuccess(ArrayList arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
