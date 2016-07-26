package com.dachen.mediecinelibraryrealize.view;


import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class SlidingRemoveView extends ViewGroup {

	
	private View contentView;
	private View deleteView;
	private int deleteViewWidth;
	private ViewDragHelper mViewDragHelper;

	public SlidingRemoveView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mViewDragHelper = ViewDragHelper.create(this, new MyCallBack());
	}
	
	private boolean isDeleteViewShow = false;
	
	//事件的处理
	private class MyCallBack extends Callback{

		@Override
		public boolean tryCaptureView(View child, int pointerId) {
			// 分析哪个子控件 
			System.out.println(child);
			return child == contentView || child == deleteView;
		}

		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			// left child组件的左上点的横坐标
			//判断拖动的子组件是哪个
			if (child == contentView) {
				//坐标范围
				if (left > 0 ) {
					return 0;
				} else if (left < -deleteView.getMeasuredWidth()) {
					return -deleteView.getMeasuredWidth();
				}
			} else if (child == deleteView) {
				if (left < contentView.getMeasuredWidth() - deleteView.getMeasuredWidth()) {
					return contentView.getMeasuredWidth() - deleteView.getMeasuredWidth();
				} else if (left > contentView.getMeasuredWidth()) {
					return contentView.getMeasuredWidth();
				}
			}
			
			
			return left;
		}

		//child位置改变的回调
		@Override
		public void onViewPositionChanged(View changedView, int left, int top,
				int dx, int dy) {
			if (changedView == contentView) {
				//System.out.println("内容改变");
				//让deleteView跟着改变
				int deleteViewLeft = contentView.getMeasuredWidth() + left;
				int deleteViewTop = 0;
				int deleteViewRight = deleteViewLeft + deleteView.getMeasuredWidth();
				int deleteViewBottom = deleteView.getMeasuredHeight();
				deleteView.layout(deleteViewLeft,deleteViewTop,deleteViewRight, deleteViewBottom);
			} else if (changedView == deleteView) {
				//让contentView跟着改变
				
				int contentViewLeft = left - contentView.getMeasuredWidth();
				int contentViewTop = 0;
				int contentViewRight = contentViewLeft + contentView.getMeasuredWidth();
				int contentViewBottom = contentView.getMeasuredHeight();
				contentView.layout(contentViewLeft,contentViewTop,contentViewRight, contentViewBottom);
			}

		}
		
		
	
		
		

		//事件的松开 
		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			//判断deleteView 是否显示超过一半
			//获取contentView的left
			int left = contentView.getLeft();
			if (left < -deleteViewWidth/2) {
				//完全显示删除
				showDelete();
			} else {
				dismissDelete();
			}
		}
		

	
	}
	
	
	public void toggle(){
		isDeleteViewShow = !isDeleteViewShow;
		if (isDeleteViewShow) {
			showDelete();
		} else {
			dismissDelete();
		}
	}
	public void dismissDelete(){
		
		mViewDragHelper.smoothSlideViewTo(deleteView, contentView.getMeasuredWidth(), 0);
		mViewDragHelper.smoothSlideViewTo(contentView, 0, 0);
		
		invalidate();
	}
	
	
	private void showDelete(){
		
		int deleteViewLeft = contentView.getMeasuredWidth() - deleteView.getMeasuredWidth() ;
		mViewDragHelper.smoothSlideViewTo(deleteView, deleteViewLeft, 0);
		mViewDragHelper.smoothSlideViewTo(contentView, - deleteView.getMeasuredWidth(), 0);
		
		invalidate();
		
	}
	
	@Override
	public void computeScroll() {
		if (mViewDragHelper.continueSettling(true)) {
			invalidate();
		}
	}
	
	//绑定touch事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mViewDragHelper.processTouchEvent(event);
		return true;
	}
	
	

	public SlidingRemoveView(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onFinishInflate() {
		contentView = getChildAt(0);
		deleteView = getChildAt(1);
		
		deleteViewWidth = deleteView.getLayoutParams().width;
		
		super.onFinishInflate();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		
		contentView.measure(widthMeasureSpec, heightMeasureSpec);
		int deleteView_widthMeasureSpec = MeasureSpec.makeMeasureSpec(deleteViewWidth, MeasureSpec.EXACTLY);
		deleteView.measure(deleteView_widthMeasureSpec, heightMeasureSpec);
		
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
		
		
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		initLayout();
	}



	private void initLayout() {
		int dx = 0;
		int contentViewLeft = 0 - dx;
		int contentViewTop = 0;
		int contentViewRight = contentView.getMeasuredWidth()- dx;
		int contentViewBottom = contentView.getMeasuredHeight();
		contentView.layout(contentViewLeft,contentViewTop,contentViewRight, contentViewBottom);
		
		
		int deleteViewLeft = contentView.getMeasuredWidth()- dx;
		int deleteViewTop = 0;
		int deleteViewRight = contentView.getMeasuredWidth() + deleteView.getMeasuredWidth()- dx;
		int deleteViewBottom = deleteView.getMeasuredHeight();
		deleteView.layout(deleteViewLeft,deleteViewTop,deleteViewRight, deleteViewBottom);
	}

}
