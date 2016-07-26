package com.dachen.medicine.adapter;

import java.util.List;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.medicine.entity.Result;

/**
 * 通讯录黑名单列表适配器
 * 
 * @param Friend
 *            暂时个体类
 */
public class BlackListAdapter extends BaseCustomAdapter<Result> {

	public BlackListAdapter(Context context, int resId, Result[] objects) {
		super(context, resId, objects);
		// TODO Auto-generated constructor stub
	}

	public BlackListAdapter(Context context, int resId, List<Result> objects) {
		super(context, resId, objects);
		// TODO Auto-generated constructor stub
	}

	public BlackListAdapter(Context context, int resId) {
		super(context, resId);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected BaseViewHolder getViewHolder() {
		// TODO Auto-generated method stub
		return new ViewHolder();
	}

	@Override
	protected void fillValues(BaseViewHolder baseViewHolder, int position) {
		// TODO Auto-generated method stub
		ViewHolder holder = (ViewHolder) baseViewHolder;
		final Result friend = getItem(position); /*
												 * String headPicFileName =
												 * friend.getHeadPicFileName();
												 * String avatarUrl =
												 * StringUtils
												 * .getAvatarUrl(userId,
												 * headPicFileName);
												 */
		/*
		 * String url = AppConfig.getUrl(avatarUrl); if(!TextUtils.isEmpty(url))
		 * { CustomImagerLoader.getInstance().loadImage(holder.avatar_img,
		 * avatarUrl); } else { holder.avatar_img
		 * .setImageResource(R.drawable.avatar_normal);
		 * 
		 * } holder.nick_name_tv.setText(friend.getShowName());
		 * holder.des_tv.setText(friend.getDescription());
		 */
	}

	public static class ViewHolder extends BaseViewHolder {
		ImageView avatar_img;
		TextView catagory_title;
		TextView nick_name_tv;
		TextView des_tv;
	}

}
