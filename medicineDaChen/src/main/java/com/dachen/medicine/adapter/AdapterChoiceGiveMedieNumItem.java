package com.dachen.medicine.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;

import com.dachen.medicine.R;
import com.dachen.medicine.adapter.AdapterChoiceGiveMedieNum.ChangeSelectItem;
import com.dachen.medicine.bean.MedieNum;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.ToastUtils;

public class AdapterChoiceGiveMedieNumItem extends BaseCustomAdapter<MedieNum>{
	ArrayList<MedieNum> lists;
	int canBuyNum;
	ChangeSelectItem item;
	int selectNums = 0;
	public AdapterChoiceGiveMedieNumItem(Context context, int resId,
			ArrayList<MedieNum> lists,int canBuyNum,ChangeSelectItem item) {
		super(context, resId, lists);
		// TODO Auto-generated constructor stub
		this.lists = lists;
		this.canBuyNum = canBuyNum;
		this.item = item;
		selectNums=0;
		for (int j = 0; j < lists.size(); j++) {
			if (lists.get(j).isselect) {
				selectNums++;
			} 
		} 
		item.changeSelectItem(selectNums); 
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
		final MedieNum num = getItem(position);
		final ViewHolder holder = (ViewHolder) baseViewHolder;
		holder.tv_num.setText(num.num+"");
		if (!num.isselect) {
			holder.iv_choice.setBackgroundResource(R.drawable.notchoice); 
		}else {
			holder.iv_choice.setBackgroundResource(R.drawable.choice); 
		}
		
		//
		holder.iv_choice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub 
				for (int i = 0; i < lists.size(); i++) {
					if (lists.get(i).isselect) {
						if (position == i) {
							if (num.isselect) { 
								num.isselect = false;
								lists.set(position, num);
								 
							}else { 
								num.isselect = true;
								lists.set(position, num);
								
							}
							
							
							selectNums=0;
							for (int j = 0; j < lists.size(); j++) {
								if (lists.get(j).isselect) {
									selectNums++;
								} 
							} 
							item.changeSelectItem(selectNums);
							
							
							notifyDataSetChanged();
							return;
						}
						
					}
				}
				int selectNum = 0;
				for (int i = 0; i < lists.size(); i++) {
					if (lists.get(i).isselect) {
						selectNum++;
						boolean find = false;
						if (selectNum>=canBuyNum) {
							
							for (int j = 0; j < lists.size(); j++) {
								if (lists.get(j).isselect) {
									 
									MedieNum num = lists.get(j);
									num.isselect = false;
									lists.set(j, num);
									find = true;
									break;
								}
							}
						}
						if (find) {
							break;
						}
					}
				}
				if (num.isselect) { 
					num.isselect = false;
					lists.set(position, num);
					 
				}else { 
					num.isselect = true;
					lists.set(position, num);
					
				}
			
				notifyDataSetChanged();
				selectNums=0;
				for (int i = 0; i < lists.size(); i++) {
					if (lists.get(i).isselect) {
						selectNums++;
					} 
				} 
				item.changeSelectItem(selectNums);
			}
		});
		
	} 
	public static class ViewHolder extends BaseViewHolder{
		@Bind(R.id.iv_choice)
		ImageView iv_choice;
		@Bind(R.id.tv_num)
		TextView tv_num;
	}
}
