package com.dachen.medicine.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;

import com.dachen.medicine.R;
import com.dachen.medicine.entity.MedicineEntity;
import com.dachen.medicine.net.CustomImagerLoader;
import com.dachen.medicine.view.AddAndSubView;

public class ChoicedItemadapter extends BaseCustomAdapter<MedicineEntity> {
	AddAndSubView addAndSubView1;
	Context context;
	public List<MedicineEntity> list;
	ChoiceItemNumInterface numChoices;
	HashMap<String, MedicineEntity> choicedMap;

	public ChoicedItemadapter(Context context, int resId,
			HashMap<String, MedicineEntity> choicedMap,
			List<MedicineEntity> list, ChoiceItemNumInterface numChoice) {
		super(context, resId, list);
		this.context = context;
		this.list = list;
		numChoices = numChoice;
		this.choicedMap = choicedMap;
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
			final int position) {
		// TODO Auto-generated method stub
		ViewHolder holder = (ViewHolder) baseViewHolder;
		final MedicineEntity media = getItem(position);
		if (null != holder.tv_select_nums) {
			holder.tv_select_nums.setText(media.num + "");
		}

		String url = media.url;
		if (!TextUtils.isEmpty(url)) {
			CustomImagerLoader.getInstance().loadImage(holder.imageView, url);
		} else {
			holder.imageView
					.setImageResource(R.drawable.image_download_fail_icon);

		}

		holder.iv_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				choicedMap.remove(list.get(position).name);
				numChoices.getList(choicedMap);
			}
		});

	}

	public static class ViewHolder extends BaseViewHolder {
		@Bind(R.id.imageView)
		ImageView imageView;
		@Bind(R.id.iv_delete)
		ImageView iv_delete;
		@Bind(R.id.tv_select_nums)
		TextView tv_select_nums;

	}
}
