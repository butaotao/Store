package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.db.dbentity.SearchRecords;

import java.util.List;

public class SearchAdapter extends BaseAdapter{
	Context context;
	List<SearchRecords> listSelected;
	public SearchAdapter(Context context, List<SearchRecords> listSelected){
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
			view = View.inflate(context, R.layout.adapter_search, null);
			holder= new ViewHolder();
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			view.setTag(holder);
		}else {
			holder = (ViewHolder) view.getTag();
		}
		String name = "";
		 name = info.searchresult;
		if (null==name) {
			name = info.searchresult;
		}if (null != name && name.length() > 9) {
			name = name.substring(0, 6) + "..."
					+ name.substring(name.length() - 2); 
		}

		holder.tv_name.setText(name);
		return view;
	}
	public class ViewHolder{
		TextView tv_name;
	}
}
