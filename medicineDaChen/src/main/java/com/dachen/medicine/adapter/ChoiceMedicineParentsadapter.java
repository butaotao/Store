package com.dachen.medicine.adapter;

import java.util.List;

import android.content.Context;
import android.widget.TextView;
import butterknife.Bind;

import com.dachen.medicine.R;
import com.dachen.medicine.entity.DepartmentEntity;

public class ChoiceMedicineParentsadapter extends
		BaseCustomAdapter<DepartmentEntity> {

	public ChoiceMedicineParentsadapter(Context context, int resId) {
		super(context, resId);
		// TODO Auto-generated constructor stub
	}

	public ChoiceMedicineParentsadapter(Context context, int resId,
			List<DepartmentEntity> objects) {
		super(context, resId, objects);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected com.dachen.medicine.adapter.BaseCustomAdapter.BaseViewHolder getViewHolder() {
		// TODO Auto-generated method stub
		return new ViewHolder();
	}

	@Override
	protected void fillValues(
			com.dachen.medicine.adapter.BaseCustomAdapter.BaseViewHolder baseViewHolder,
			int position) {
		// TODO Auto-generated method stub
		ViewHolder holder = (ViewHolder) baseViewHolder;
		final DepartmentEntity department = getItem(position);
		holder.tv_name.setText(department.getName());

	}

	public static class ViewHolder extends BaseViewHolder {
		@Bind(R.id.tv_name)
		TextView tv_name;
	}
}
