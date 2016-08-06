package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.common.utils.TimeUtils;
import com.dachen.common.widget.UISwitchButton;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.AddSigninRemindActivity;
import com.dachen.dgroupdoctorcompany.activity.SigninRemindActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.RemindDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.Reminder;
import com.dachen.dgroupdoctorcompany.db.dbentity.SearchRecords;

import java.util.List;

public class SignInRemindAdapter extends BaseAdapter{
	Context context;
	List<Reminder> listSelected;
	RemindDao dao;
	public SignInRemindAdapter(Context context, List<Reminder> listSelected){
		this.context = context;
		this.listSelected = listSelected;
		dao = new RemindDao(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listSelected.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listSelected.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int arg0, View view, ViewGroup arg2) {
		final ViewHolder holder;
		// TODO Auto-generated method stub
		final Reminder info = (Reminder) getItem(arg0);
		//if (null==view) {
			view = View.inflate(context, R.layout.item_signin_remind, null);
			holder= new ViewHolder();
			holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
			holder.tv_repeat = (TextView) view.findViewById(R.id.tv_repeat);
			holder.open = (UISwitchButton) view.findViewById(R.id.open);
			holder.rl_time = (RelativeLayout) view.findViewById(R.id.rl_time);
			holder.rl_item = (RelativeLayout) view.findViewById(R.id.rl_item);
			view.setTag(holder);
		//}else {
		//	holder = (ViewHolder) view.getTag();
		//}
//		String name = "";
//
        holder.tv_time.setText(String.format("%02d:%02d", info.hour, info.minute));
		holder.tv_repeat.setText(getShowTime(info.weekday));
		holder.rl_time.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, AddSigninRemindActivity.class);
				intent.putExtra("reminder", info);
				context.startActivity(intent);
			}
		});
		holder.open.setChecked(true);
		holder.rl_item.setBackgroundColor(context.getResources().getColor(R.color.white));
		if (dao.queryByUserCreateTime(info.createTime).isOpen==0){
			holder.open.setChecked(false);
			holder.rl_item.setBackgroundColor(context.getResources().getColor(R.color.content_background));
		}
		holder.open.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (!isChecked) {
					info.isOpen = 0;
				//	holder.open.setChecked(true);
					holder.rl_item.setBackgroundColor(context.getResources().getColor(R.color.content_background));
				} else {
					info.isOpen = 1;
					holder.rl_item.setBackgroundColor(context.getResources().getColor(R.color.white));
					//holder.open.setChecked(false);
				}
				listSelected.set(arg0, info);
				/*notifyDataSetChanged();*/
				dao.addRemind(info);
				//notifyDataSetChanged();
			}
		});
		return view;
	}
	public class ViewHolder{
		TextView tv_time;
		TextView tv_repeat;
		RelativeLayout rl_time;
		UISwitchButton open;
		RelativeLayout rl_item;
	}
	public String getShowTime(String time){
		if (!TextUtils.isEmpty(time)){
			if (time.contains("7")&&time.contains("6")&&time.length()<5){
				return "周末";
			}else if (!time.contains("6")&&!time.contains("7")&&time.length()>9){
				return "工作日";
			} else if (time.length()>13){
				return "每天";
			} else {
				String times = "";
				String info[] = time.split(",");
				for(int i=0;i<info.length;i++){
					String t = info[i];
					if (t.contains("1")){
						times += "周一 ";
					}else if(t.contains("2")){
						times += "周二 ";
					}else if(t.contains("3")){
						times += "周三 ";
					}else if(t.contains("4")){
						times += "周四 ";
					}else if(t.contains("5")){
						times += "周五 ";
					}else if(t.contains("6")){
						times += "周六 ";
					}else if(t.contains("7")){
						times += "周日 ";
					}
				}

				return times;
			}
		}else {
			return "永不";
		}
	}
}
