package com.dachen.medicine.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dachen.medicine.common.utils.DisplayUtil;


/**
 * 
 * @描述：           @滚轮适配器
 * @作者：           @蒋诗朋
 * @创建时间：  @2014-05-28
 */

public class WheelTextAdapter extends BaseAdapter {
	ArrayList<TextInfo> mData = null;
	int mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
	int mHeight = 50;
	Context mContext = null;
	
	public WheelTextAdapter(Context context) {
		mContext = context;
		mHeight = (int) DisplayUtil.pixelToDp(context, mHeight);
	}

	public void setData(ArrayList<TextInfo> data) {
		mData = data;
		this.notifyDataSetChanged();
	}
	
	public void setItemSize(int width, int height) {
		mWidth = width;
		mHeight = (int) DisplayUtil.pixelToDp(mContext, height);
	}

	@Override
	public int getCount() {
		return (null != mData) ? mData.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView = null;

		if (null == convertView) {
			convertView = new TextView(mContext);
			convertView.setLayoutParams(new TosGallery.LayoutParams(mWidth,
					mHeight));
			textView = (TextView) convertView;
			textView.setGravity(Gravity.CENTER);
			textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
			textView.setTextColor(Color.BLACK);
		}

		if (null == textView) {
			textView = (TextView) convertView;
		}

		TextInfo info = mData.get(position);
		textView.setText(info.mText);
		textView.setTextColor(info.mColor);

		return convertView;
	}
}