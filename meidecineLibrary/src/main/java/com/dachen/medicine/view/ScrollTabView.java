package com.dachen.medicine.view; 

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.android.library.R;
import com.dachen.medicine.common.utils.LogUtils;

/**
 * 自定义tab切换带viewpager控件
 * @author yuankangle
 * 2015-3-31
 */
public class ScrollTabView extends LinearLayout implements OnCheckedChangeListener{

	private int 					mTabCount;
	/**当前选中页数**/
	public int 						mPageIndex;
	private float	 				mCurrentCheckedRadioLeft;//当前被选中的RadioButton距离左侧的距离
	private ImageView 				mImageView;
	private HorizontalScrollView 	mHorizontalScrollView;//上面的水平滚动控件
	private ViewPager 				mViewPager;
	private RadioGroup 				mRadioGroup;
	private RadioButton 			mRadioButton1;
	private RadioButton 			mRadioButton2;
	private RadioButton 			mRadioButton3;
	private RadioButton 			mRadioButton4;
	private RadioButton 			mRadioButton5;
	private boolean 				isFill;//是否全屏
	private int 					width;
	private boolean                isAddAnimation = true;
	private boolean 				isHaveTab = true;
	private OnViewPagerSelectedListener mOnViewPagerSelectedListener;//viewpager子页面切换选中监听

	/**
	 * 
	 * @param context
	 * @param layoutID 页面布局，包括tab和veiwpager
	 * @param tabCount 标签页个数
	 * @param isFill   是否固定为全屏  true：全屏 | false：内容大小
	 * @param mOnViewPagerSelectedListener viewpager当前面前选中监听事件，可用来加载网络数据
	 */
	public ScrollTabView(Activity context,int layoutID,int tabCount,boolean isFill,OnViewPagerSelectedListener mOnViewPagerSelectedListener) {
		super(context);
		this.mTabCount = tabCount;
		this.isFill = isFill;
		this.mOnViewPagerSelectedListener = mOnViewPagerSelectedListener;
		LayoutInflater.from(context).inflate(layoutID, this, true);  
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		initView();
	}
	/**
	 * @param isAddAnimation   是否有动画
	 * @param isHaveTab    顶部是否有tab
	 * @param context 上下文
	 * @param layoutID 页面布局，包括tab和veiwpager
	 * @param tabCount 标签页个数
	 * @param isFill   是否固定为全屏  true：全屏 | false：内容大小
	 * @param mOnViewPagerSelectedListener viewpager当前面前选中监听事件，可用来加载网络数据
	 */
	public ScrollTabView(boolean isAddAnimation,boolean isHaveTab,Activity context,int layoutID,int tabCount,
						 boolean isFill,OnViewPagerSelectedListener mOnViewPagerSelectedListener) {
		super(context);
		this.isAddAnimation = isAddAnimation;
		this.isHaveTab = isHaveTab;
		this.mTabCount = tabCount;
		this.isFill = isFill;
		this.mOnViewPagerSelectedListener = mOnViewPagerSelectedListener;
		LayoutInflater.from(context).inflate(layoutID, this, true);
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		initView();

	}
	/**
	 * 设置viewpager子页面
	 * @param layoutIDs 子页面布局
	 * @param onInitView 子页面初始化
	 */
	public void setViewPagerItemView(int[] layoutIDs,OnInitView onInitView){
		ArrayList<View> mViews = new ArrayList<View>();
    	LayoutInflater mInflater = LayoutInflater.from(getContext());
     	mViews.add(mInflater.inflate(R.layout.layout_0, null));
    	for (int i = 0; i < layoutIDs.length; i++) {
    		View view = mInflater.inflate(layoutIDs[i], null);
    		mViews.add(view);
		}
     	mViews.add(mInflater.inflate(R.layout.layout_0, null));
    	onInitView.onInit(mViews);
		MyPagerAdapter myAdpter;
		if (isAddAnimation){
			myAdpter = new MyPagerAdapter(mViews);
		}else{
			myAdpter = new MyPagerAdapter(mViews,isAddAnimation);
		}

    	mViewPager.setAdapter(myAdpter);//设置ViewPager的适配器
    	mViewPager.setCurrentItem(1);
    	if(!isFill){
			mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();
		}
	}
	
	/**
	 * 初始化控件
	 */
	private void initView() {
		mRadioGroup   = (RadioGroup)findViewById(R.id.radioGroup);
		if(mTabCount >= 1)
			mRadioButton1 = (RadioButton)findViewById(R.id.btn1);
		if(mTabCount >= 2)
			mRadioButton2 = (RadioButton)findViewById(R.id.btn2);
		if(mTabCount >= 3)
			mRadioButton3 = (RadioButton)findViewById(R.id.btn3);
		if(mTabCount >= 4)
			mRadioButton4 = (RadioButton)findViewById(R.id.btn4);
		if(mTabCount >= 5)
			mRadioButton5 = (RadioButton)findViewById(R.id.btn5);

		mRadioGroup.setOnCheckedChangeListener(this);
		
		mImageView = (ImageView)findViewById(R.id.img1);
		if(isFill){
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width/mTabCount,4);
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
			mImageView.setLayoutParams(params);
		}
		
		mHorizontalScrollView = (HorizontalScrollView)findViewById(R.id.horizontalScrollView);
		
		mViewPager = (ViewPager)findViewById(R.id.pager);
		
		mViewPager.setOnPageChangeListener(new MyPagerOnPageChangeListener());
		
	}
	
	public ScrollTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * tab切换监听事件
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		AnimationSet _AnimationSet = new AnimationSet(true);
		TranslateAnimation _TranslateAnimation;

		if (checkedId == R.id.btn1 && mTabCount >= 1) {
			mPageIndex = 1 ;
			if(isFill){
				_TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, width*0/mTabCount, 0f, 0f);
			}else{
				_TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, getResources().getDimension(R.dimen.rdo1), 0f, 0f);
			}
			_AnimationSet.addAnimation(_TranslateAnimation);
			_AnimationSet.setFillBefore(false);
			_AnimationSet.setFillAfter(true);
			_AnimationSet.setDuration(100);
			/*LayoutParams _LayoutParams1 = new LayoutParams(100, 4);
			_LayoutParams1.setMargins(0, 0, 0, 0);
			_LayoutParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);*/
			//mImageView.bringToFront();
			mImageView.startAnimation(_AnimationSet);//开始上面蓝色横条图片的动画切换
			//mImageView.setLayoutParams(_LayoutParams1);
			mViewPager.setCurrentItem(1);//让下方ViewPager跟随上面的HorizontalScrollView切换
			if(mRadioButton1 != null)
				mRadioButton1.setTextColor(0xff7ab6ff);
			if(mRadioButton2 != null)
				mRadioButton2.setTextColor(Color.BLACK);
			if(mRadioButton3 != null)
				mRadioButton3.setTextColor(Color.BLACK);
			if(mRadioButton4 != null)
				mRadioButton4.setTextColor(Color.BLACK);
			if(mRadioButton5 != null)
				mRadioButton5.setTextColor(Color.BLACK);
		}else if (checkedId == R.id.btn2 && mTabCount >= 2) {
			mPageIndex = 2;
			if(isFill){
				_TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, width*1/mTabCount, 0f, 0f);
			}else{
				_TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, getResources().getDimension(R.dimen.rdo2), 0f, 0f);
			}
			_AnimationSet.addAnimation(_TranslateAnimation);
			_AnimationSet.setFillBefore(false);
			_AnimationSet.setFillAfter(true);
			_AnimationSet.setDuration(100);

			//mImageView.bringToFront();
			mImageView.startAnimation(_AnimationSet);
			
			mViewPager.setCurrentItem(2);
			if(mRadioButton1 != null)
				mRadioButton1.setTextColor(Color.BLACK);
			if(mRadioButton2 != null)
				mRadioButton2.setTextColor(0xff7ab6ff);
			if(mRadioButton3 != null)
				mRadioButton3.setTextColor(Color.BLACK);
			if(mRadioButton4 != null)
				mRadioButton4.setTextColor(Color.BLACK);
			if(mRadioButton5 != null)
				mRadioButton5.setTextColor(Color.BLACK);
		}else if (checkedId == R.id.btn3 && mTabCount >= 3) {
			mPageIndex = 3;
			if(isFill){
				_TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, width*2/mTabCount, 0f, 0f);
			}else{
				_TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, getResources().getDimension(R.dimen.rdo3), 0f, 0f);
			}
			_AnimationSet.addAnimation(_TranslateAnimation);
			_AnimationSet.setFillBefore(false);
			_AnimationSet.setFillAfter(true);
			_AnimationSet.setDuration(100);
			
			//mImageView.bringToFront();
			mImageView.startAnimation(_AnimationSet);
			
			mViewPager.setCurrentItem(3);
			if(mRadioButton1 != null)
				mRadioButton1.setTextColor(Color.BLACK);
			if(mRadioButton2 != null)
				mRadioButton2.setTextColor(Color.BLACK);
			if(mRadioButton3 != null)
				mRadioButton3.setTextColor(0xff7ab6ff);
			if(mRadioButton4 != null)
				mRadioButton4.setTextColor(Color.BLACK);
			if(mRadioButton5 != null)
				mRadioButton5.setTextColor(Color.BLACK);
		}else if (checkedId == R.id.btn4 && mTabCount >= 4) {
			mPageIndex = 4;
			if(isFill){
				_TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, width*3/mTabCount, 0f, 0f);
			}else {
				_TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, getResources().getDimension(R.dimen.rdo4), 0f, 0f);
			}
			_AnimationSet.addAnimation(_TranslateAnimation);
			_AnimationSet.setFillBefore(false);
			_AnimationSet.setFillAfter(true);
			_AnimationSet.setDuration(100);
			//mImageView.bringToFront();
			mImageView.startAnimation(_AnimationSet);
			mViewPager.setCurrentItem(4);
			if(mRadioButton1 != null)
				mRadioButton1.setTextColor(Color.BLACK);
			if(mRadioButton2 != null)
				mRadioButton2.setTextColor(Color.BLACK);
			if(mRadioButton3 != null)
				mRadioButton3.setTextColor(Color.BLACK);
			if(mRadioButton4 != null)
				mRadioButton4.setTextColor(0xff7ab6ff);
			if(mRadioButton5 != null)
				mRadioButton5.setTextColor(Color.BLACK);
		}else if (checkedId == R.id.btn5 && mTabCount >= 5) {
			mPageIndex = 5;
			if(isFill) {
				_TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, width * 4 / mTabCount, 0f, 0f);
			}else {
				_TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, getResources().getDimension(R.dimen.rdo5), 0f, 0f);
			}
			_AnimationSet.addAnimation(_TranslateAnimation);
			_AnimationSet.setFillBefore(false);
			_AnimationSet.setFillAfter(true);
			_AnimationSet.setDuration(100);
			//mImageView.bringToFront();
			mImageView.startAnimation(_AnimationSet);
			
			mViewPager.setCurrentItem(5);
			if(mRadioButton1 != null)
				mRadioButton1.setTextColor(Color.BLACK);
			if(mRadioButton2 != null)
				mRadioButton2.setTextColor(Color.BLACK);
			if(mRadioButton3 != null)
				mRadioButton3.setTextColor(Color.BLACK);
			if(mRadioButton4 != null)
				mRadioButton4.setTextColor(Color.BLACK);
			if(mRadioButton5 != null)
				mRadioButton5.setTextColor(0xff7ab6ff);
		}
		
		if(isFill){
			mCurrentCheckedRadioLeft = width/mTabCount*(mPageIndex-1);
			mHorizontalScrollView.smoothScrollTo((int) mCurrentCheckedRadioLeft, 0);
		}else{
			mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();//更新当前蓝色横条距离左边的距离
			mHorizontalScrollView.smoothScrollTo((int)mCurrentCheckedRadioLeft-(int)getResources().getDimension(R.dimen.rdo2), 0);
		}
	}
	

	/**
     * 获得当前被选中的RadioButton距离左侧的距离
     */
	private float getCurrentCheckedRadioLeft() {
		if (mRadioButton1.isChecked() && mTabCount >= 1) {
			return getResources().getDimension(R.dimen.rdo1);
		}else if (mRadioButton2.isChecked() && mTabCount >= 2) {
			return getResources().getDimension(R.dimen.rdo2);
		}else if (mRadioButton3.isChecked() && mTabCount >= 3) {
			return getResources().getDimension(R.dimen.rdo3);
		}else if (mRadioButton4.isChecked() && mTabCount >= 4) {
			return getResources().getDimension(R.dimen.rdo4);
		}else if (mRadioButton5.isChecked() && mTabCount >= 5) {
			return getResources().getDimension(R.dimen.rdo5);
		}
		return 0f;
	}
	
	/**
	 * viewpager监听事件
	 * @author yuankangle
	 * 2015-3-31 下午3:23:57
	 */
	private class MyPagerOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
		/**
		 * 滑动ViewPager的时候,让上方的HorizontalScrollView自动切换
		 */
		@Override
		public void onPageSelected(int position) {
			mOnViewPagerSelectedListener.onPageSelected(position);
			if (position == 0) {
				if (isAddAnimation){
				mViewPager.setCurrentItem(1);
				}else {
				mViewPager.setCurrentItem(0);
				}
			}
			if (position == 1 && mTabCount >= 1&&isHaveTab) {
				mRadioButton1.performClick();
			}else if (position == 2 && mTabCount >= 2&&isHaveTab) {
				mRadioButton2.performClick();
			}else if (position == 3 && mTabCount >= 3&&isHaveTab) {
				mRadioButton3.performClick();
			}else if (position == 4 && mTabCount >= 4&&isHaveTab) {
				mRadioButton4.performClick();
			}else if (position == 5 && mTabCount >= 5&&isHaveTab) {
				mRadioButton5.performClick();
			}

			else if (position == mTabCount + 1) {
				mViewPager.setCurrentItem(mTabCount);
			}
		}
		
	}
	public int getCurrentSelectTab(){
		return mViewPager.getCurrentItem();
	}
	/**
	 * 改变显示的tab，position从1开始
	 * @param position
	 */
	public void setSelectedTab(int position){
		if(mViewPager == null) return;
		if (position == 0) {
			if (isAddAnimation){
				mViewPager.setCurrentItem(1);

			}else{
				mViewPager.setCurrentItem(0);
			}

		}else if (position == 1 && mTabCount >= 1 && mRadioButton1 != null&&isHaveTab) {
			mRadioButton1.performClick();
		}else if (position == 2 && mTabCount >= 2 && mRadioButton2 != null&&isHaveTab) {
			mRadioButton2.performClick();
		}else if (position == 3 && mTabCount >= 3 && mRadioButton3 != null&&isHaveTab) {
			mRadioButton3.performClick();
		}else if (position == 4 && mTabCount >= 4 && mRadioButton4 != null&&isHaveTab) {
			mRadioButton4.performClick();
		}else if (position == 5 && mTabCount >= 5 && mRadioButton5 != null&&isHaveTab) {
			mRadioButton5.performClick();
		}else if (position == mTabCount + 1) {
			LogUtils.burtLog("position22===="+position);
			mViewPager.setCurrentItem(mTabCount);
		}else if(position == (mTabCount-1)&&!isAddAnimation){
				mViewPager.setCurrentItem(position);
		}
	}

	/**
	 * 初始化UI
	 */
	public interface OnInitView{
		/**
		 * 因为多了一个缓冲界面，viewpager第一个界面为view[1]
		 * @param views
		 */
		public void onInit(ArrayList<View> views);
	}

	/**
	 * tab切换
	 */
	public interface OnViewPagerSelectedListener{
		/**
		 * viewpager切换后选中事件
		 * @param position
		 */
		public void onPageSelected(int position);
	}

}
