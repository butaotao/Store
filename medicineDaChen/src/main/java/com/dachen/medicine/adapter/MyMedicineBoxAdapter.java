package com.dachen.medicine.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dachen.medicine.R;
import com.dachen.medicine.entity.Patients;

public class MyMedicineBoxAdapter extends BaseAdapter {
	Context context;
	int resId;
	List<Patients> objects;

	public MyMedicineBoxAdapter(Context context, int resId,
			List<Patients> objects) {
		this.context = context;
		this.resId = resId;
		this.objects = objects;
	}

	public static class ViewHolder {
		TextView tv_name_medi;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return objects.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return objects.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View v = convertView;
		ViewHolder holder;
		if (null == v) {
			v = LayoutInflater.from(context).inflate(R.layout.adapter_name,
					null);
			holder = new ViewHolder();
			holder.tv_name_medi = (TextView) v.findViewById(R.id.tv_name_medi);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		Patients department = (Patients) getItem(position);
		System.err.println("department==" + department);
		holder.tv_name_medi.setText(department.getName() + "");
		return v;
	}
}
