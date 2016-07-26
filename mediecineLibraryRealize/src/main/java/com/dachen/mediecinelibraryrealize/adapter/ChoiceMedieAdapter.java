package com.dachen.mediecinelibraryrealize.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.activity.ChoiceMedieActivity.ChangeData;
import com.dachen.mediecinelibraryrealize.entity.ChoiceMedieEntity.MedieEntity;

import java.util.List;

public class ChoiceMedieAdapter extends BaseAdapter{
	
	public List<MedieEntity> listdata;
	public Context context;
	public ChangeData change;
	public ChoiceMedieAdapter(Context context,List<MedieEntity> listdata,ChangeData change){
		this.context = context;
		this.listdata = listdata;
		this.change = change;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listdata.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listdata.get(arg0);
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
		final MedieEntity entity = listdata.get(arg0);
		if (null == view) {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.adapter_choicemedie, null);
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			holder.radionbutton = (RadioButton) view.findViewById(R.id.radionbutton);
			holder.rl_item = (RelativeLayout) view.findViewById(R.id.rl_item);
			view.setTag(holder);
		}else {
			holder = (ViewHolder) view.getTag();
		} 
		holder.radionbutton.setChecked(false);
		if (entity.select) { 
			holder.radionbutton.setChecked(true);
		}
		/*if (!TextUtils.isEmpty(entity.general_name)) {
			holder.tv_name.setText(entity.general_name); 
		}else {
			holder.tv_name.setText(entity.name); 
		}*/
		;
		String name = "";
		if (!TextUtils.isEmpty(entity.title)) {
			name = entity.title;
		}else if(!TextUtils.isEmpty(entity.general_name)){
			name = entity.general_name;
		}else if(!TextUtils.isEmpty(entity.name)){
			name= entity.name;
		}
//		String name = CompareDatalogic.getShowName(entity.general_name, entity.name, entity.title);
		holder.tv_name.setText(name);
		holder.rl_item.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg1) {
				// TODO Auto-generated method stub 
				
				for (int i = 0; i < listdata.size(); i++) {
					MedieEntity entity2 =listdata.get(i);
					entity2.select = false;
					listdata.set(i, entity2);
				}
				entity.select = true; 
				holder.radionbutton.setChecked(true);
				listdata.set(arg0, entity); 
				change.change(listdata); 
			}
		});
	
		holder.radionbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg1) {
				// TODO Auto-generated method stub 
				
				for (int i = 0; i < listdata.size(); i++) {
					MedieEntity entity2 =listdata.get(i);
					entity2.select = false;
					listdata.set(i, entity2);
				}
				entity.select = true; 
				holder.radionbutton.setChecked(true);
				listdata.set(arg0, entity); 
				change.change(listdata); 
			}
		});
		
		return view;
	}
	public class ViewHolder{
		TextView tv_name;
		RadioButton radionbutton;
		RelativeLayout rl_item;
	}
}
