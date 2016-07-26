package com.dachen.medicine.view; 
import java.util.ArrayList;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
/**
 * ViewPager的适配器
 * @author zj
 * 2012-5-24 下午2:26:57
 */
public class MyPagerAdapter extends PagerAdapter{
	private ArrayList<View> mViews;
	
	public MyPagerAdapter(ArrayList<View> mViews) {
		// TODO Auto-generated constructor stub
		this.mViews = mViews;
	}
	public MyPagerAdapter(ArrayList<View> mViews,boolean isAddAnimation) {
		// TODO Auto-generated constructor stub
		this.mViews = mViews;
		if (!isAddAnimation){
			this.mViews.remove(mViews.size()-1);
			this.mViews.remove(0);
		}
	}
	@Override
	public void destroyItem(View v, int position, Object obj) {
		// TODO Auto-generated method stub
		((ViewPager)v).removeView(mViews.get(position));
	}

	@Override
	public void finishUpdate(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mViews.size();
	}

	@Override
	public Object instantiateItem(View v, int position) {
		((ViewPager)v).addView(mViews.get(position));
		return mViews.get(position);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
		// TODO Auto-generated method stub
		
	}
	
}