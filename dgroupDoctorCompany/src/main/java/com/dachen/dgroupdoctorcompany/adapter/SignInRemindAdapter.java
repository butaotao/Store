package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dachen.common.widget.UISwitchButton;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.db.dbentity.SearchRecords;

import java.util.List;

public class SignInRemindAdapter extends BaseAdapter{
	Context context;
	List<SearchRecords> listSelected;
	public SignInRemindAdapter(Context context, List<SearchRecords> listSelected){
		this.context = context;
		this.listSelected = listSelected;
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
	public View getView(int arg0, View view, ViewGroup arg2) {
		ViewHolder holder;
		// TODO Auto-generated method stub
		SearchRecords info = (SearchRecords) getItem(arg0);
		if (null==view) {
			view = View.inflate(context, R.layout.item_signin_remind, null);
			holder= new ViewHolder();
			holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
			holder.tv_repeat = (TextView) view.findViewById(R.id.tv_repeat);
			holder.open = (UISwitchButton) view.findViewById(R.id.open);
			view.setTag(holder);
		}else {
			holder = (ViewHolder) view.getTag();
		}
//		String name = "";
//
//
//		holder.tv_time.setText(name);
		return view;
	}
	public class ViewHolder{
		TextView tv_time;
		TextView tv_repeat;
		UISwitchButton open;
	}
}
