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

import com.android.library.R;
import com.dachen.medicine.common.utils.DisplayUtil;

public class WheelPopUpFromBottomAdapter extends BaseAdapter {

	private int mWidth  			     = ViewGroup.LayoutParams.MATCH_PARENT;
	private int mHeight 				 = 50;
	private Context mContext = null;
	private ArrayList<String> mData;
	
	public WheelPopUpFromBottomAdapter(Context context) {
		mContext = context;
		mHeight = (int) DisplayUtil.pixelToDp(context, mHeight);
	}
	
	public void setData(ArrayList<String> data) {
		mData = data;
		this.notifyDataSetChanged();
	}
	
	public void setItemSize(int width, int height) {
		mWidth = width;
		mHeight = (int) DisplayUtil.pixelToDp(mContext, height);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (null != mData) ? mData.size() : 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
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
		textView.setText(mData.get(position));
		textView.setTextColor(mContext.
				getResources().
				getColor(R.color.label_color_blue));
		return convertView;
	}

}
