package com.dachen.medicine.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;

import com.dachen.medicine.R;
import com.dachen.medicine.bean.MedieNum;
import com.dachen.medicine.entity.CdrugRecipeitem;
import com.dachen.medicine.entity.DeleteInterace;
import com.dachen.medicine.logic.ScaningData;

public class WrongNumAdapter extends BaseCustomAdapter<MedieNum>{
	DeleteInterace interfaces;
	List<MedieNum> objects;
	public WrongNumAdapter(Context context, int resId, List<MedieNum> objects,DeleteInterace interfaces) {
		super(context, resId, objects);
		// TODO Auto-generated constructor stub
		this.interfaces = interfaces;
		this.objects = objects;
	}

	@Override
	protected com.dachen.medicine.adapter.BaseCustomAdapter.BaseViewHolder getViewHolder() {
		// TODO Auto-generated method stub
		return new ViewHolder();
	}

	@Override
	protected void fillValues(
			com.dachen.medicine.adapter.BaseCustomAdapter.BaseViewHolder baseViewHolder,
			final int position) {
		// TODO Auto-generated method stub
		final MedieNum mnum = getItem(position);
		ViewHolder holder = (ViewHolder) baseViewHolder;
		holder.tv_wrongnum.setText(mnum.num);
		holder.iv_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				objects.remove(position);
				interfaces.delete(objects);
				removeTitleList(mnum.num,objects);
				notifyDataSetChanged();
			}
		});
	}
	public void removeTitleList(String num,List<MedieNum> objects){
		List<CdrugRecipeitem> lists = ScaningData.getlistScaning_showIntitleBar();
		if (!TextUtils.isEmpty(num)){
			for (int i = 0; i < lists.size(); i++) {
				if (null!=lists.get(i).scanCode){
					if (num.length()>7&&lists.get(i).scanCode.length()>7){
						if (num.substring(0,7).equals(lists.get(i).scanCode.substring(0,7))){
							if (lists.get(i).scaningNum >0){
								lists.get(i).scaningNum =  objects.size();
							}
							break;
						}
					}


				}
			}
		}

	}
	public static class ViewHolder extends BaseViewHolder{
		@Bind (R.id.tv_wrongnum)
		TextView tv_wrongnum;
		@Bind(R.id.iv_delete)
		ImageView iv_delete;
	}
}
