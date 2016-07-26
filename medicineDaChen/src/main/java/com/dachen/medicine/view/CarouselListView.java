package com.dachen.medicine.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.dachen.medicine.adapter.SalesclerkAdapter;

/**
 * 轮播列表
 * @author yuankangle
 * 2015-4-7 上午11:51:35
 */
public class CarouselListView extends ListView {
	private final static long DELAYED_TIME = 5000L;
	private int position = 1;
	private int height;
	private SalesclerkAdapter mYdyBuyInfoAdapter;
//	private boolean start;
	public CarouselListView(Context context) {
		super(context);
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CarouselListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void init(){
		height = getHeight();
		setFocusable(false);
		setFocusableInTouchMode(false);
		setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(scrollState == 0 && position > 0){
					try {
						view.setSelection(position-1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
	}
	
	public void setYdyBuyInfoAdapter(SalesclerkAdapter mYdyBuyInfoAdapter){
		this.mYdyBuyInfoAdapter = mYdyBuyInfoAdapter;
		setAdapter(mYdyBuyInfoAdapter);
	}
	
	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		position = 1;
		handler.removeMessages(0);
		handler.sendEmptyMessageDelayed(0, DELAYED_TIME);
	}
	
//	public void setStart(boolean start){
//		this.start = start;
//	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return true;
	}
	
	Runnable mRunnable = new Runnable() {
		@SuppressLint("NewApi")
		@Override
		public void run() {
			try {
				if(Build.VERSION.SDK_INT >= 11)
					smoothScrollToPositionFromTop(position, 0, 500);
				else{
					smoothScrollBy(getHeight(), 500);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			position ++;
			handler.sendEmptyMessageDelayed(0, DELAYED_TIME);
		}
	};
	
	private Handler handler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			if(mYdyBuyInfoAdapter != null && mYdyBuyInfoAdapter.getItemSize() > 0)
				post(mRunnable);
			return true;
		}
	});
	
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus); 
		try {
			if(mYdyBuyInfoAdapter != null && mYdyBuyInfoAdapter.getItemSize() > 0){
				post(new Runnable() {
					@Override
					public void run() {
						position = 0;
						if(Build.VERSION.SDK_INT >= 11)
							smoothScrollToPosition(position);
						else
							scrollBy(0, height*position);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
