/*
 * HorizontalListView.java v1.5
 *
 *
 * The MIT License
 * Copyright (c) 2011 Paul Soucy (paul@dev-smart.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package com.dachen.dgroupdoctorcompany.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Scroller;

import com.dachen.dgroupdoctorcompany.R;

import java.util.LinkedList;
import java.util.Queue;

public class HorizontalListView extends AdapterView<ListAdapter> {

    public boolean mAlwaysOverrideTouch = true;
    protected ListAdapter mAdapter;
    private int mLeftViewIndex = -1;
    private int mRightViewIndex = 0;
    protected int mCurrentX;
    protected int mNextX;
    private int mMaxX = Integer.MAX_VALUE;
    private int mDisplayOffset = 0;
    protected Scroller mScroller;
    private GestureDetector mGesture;
    private Queue<View> mRemovedViewQueue = new LinkedList<View>();
    private OnItemSelectedListener mOnItemSelected;
    private OnItemClickListener mOnItemClicked;
    private OnItemLongClickListener mOnItemLongClicked;
    private boolean mDataChanged = false;
    /*-----------------------------------------zxy end -----------------------------------------*/
    // 相对布局位置
    private int mGravity;

    // 靠上布局
    private static final int GRAVITY_TOP = 0;

    // 居中布局
    private static final int GRAVITY_CENTER = 1;

    // 靠底布局
    private static final int GRAVITY_BOTTOM = 2;
    // 实际布局到当前视图的第一个 item 位置
    private int mFirstPosition;
    // 当前选中位置
    private int mSelectedPosition;
    // item 缩放因子
    private float mItemScaleFactor;

    // item 缩放时常
    private int mItemScaleDuration;
    // 控件总长度
    private int mFillInWidth;
    // 最后滚动时间戳
    private long mLastScrollTimeStamp;

    // item 宽度
    private int mItemWidth;

    // item 高度
    private int mItemHeight;

    // 左侧溢出范围
    private int mLeftOffSet;

    // 右侧溢出范围
    private int mRightOffSet;
    // 子视图间距
    private int mSpacing;

    // Adapter.getCount() 数量
    private int mItemCount;

    // 回收站
   // private static final RecycleBin mRecycler = new RecycleBin();

    // 子视图上边界
    private int mLayoutTop;

    // adapter 监听
   // private InnerDataSetObserver mDataSetObserver = new InnerDataSetObserver();
/*-----------------------------------------zxy start-----------------------------------------*/
    public HorizontalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyHListView, 0, 0);
        mItemWidth = ta.getDimensionPixelSize(R.styleable.MyHListView_Hitem_width, 200);
        mItemHeight = ta.getDimensionPixelSize(R.styleable.MyHListView_Hitem_height, 200);
        mItemScaleFactor = ta.getFloat(R.styleable.MyHListView_Hitem_scale_factor, 1.1f);
        mItemScaleDuration = ta.getInteger(R.styleable.MyHListView_Hitem_scale_duration, 300);
        mSpacing = ta.getDimensionPixelOffset(R.styleable.MyHListView_Hspacing, 20);
        mLeftOffSet = ta.getDimensionPixelOffset(R.styleable.MyHListView_Hleft_offset, 20);
        mRightOffSet = ta.getDimensionPixelOffset(R.styleable.MyHListView_Hright_offset, 20);
        mGravity = ta.getInt(R.styleable.MyHListView_Hgravity, 1);
    }

    private synchronized void initView() {
        mLeftViewIndex = -1;
        mRightViewIndex = 0;
        mDisplayOffset = 0;
        mCurrentX = 0;
        mNextX = 0;
        mMaxX = Integer.MAX_VALUE;
        mScroller = new Scroller(getContext());
        mGesture = new GestureDetector(getContext(), mOnGesture);
    }

    @Override
    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        mOnItemSelected = listener;
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClicked = listener;
    }

    @Override
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClicked = listener;
    }

    private DataSetObserver mDataObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            synchronized (HorizontalListView.this) {
                mDataChanged = true;
            }
            invalidate();
            requestLayout();
        }

        @Override
        public void onInvalidated() {
            reset();
            invalidate();
            requestLayout();
        }

    };

    @Override
    public ListAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public View getSelectedView() {
        if (mItemCount > 0 && mSelectedPosition >= 0) {
            return getChildAt(mSelectedPosition - mFirstPosition);
        } else {
            return null;
        }
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataObserver);
        }
        mAdapter = adapter;
        mAdapter.registerDataSetObserver(mDataObserver);
//        mFirstPosition = 0;
        reset();
/*
        mRecycler.clear();
        if (mAdapter == null || mAdapter.isEmpty()) {
            setFocusable(false);
            mDataSetObserver.onEmpty();
        } else {
            setFocusable(true);
            mAdapter.registerDataSetObserver(mDataSetObserver);
            mFirstPosition = 0;
            mDataSetObserver.onChanged();
            setSelection(mFirstPosition);
        }*/
    }

    private synchronized void reset() {
        initView();
        removeAllViewsInLayout();
        requestLayout();
    }

   @Override
    public void setSelection(int position) {
        Log.d("zxy", "setSelection: ");
       mDataObserver.onChanged();
       /* // 1.unFocus pre view
        onItemFocusChange(false);

        // 3.reSet selectPosition
        mSelectedPosition = position;

        // 2.scroll to x
        int areaStartX = getScrollXByPosition(position);
        // layoutChildren(areaStartX, areaStartX + getWidth());
        smoothScrollTo(areaStartX, 0);

        // 4.focus cur view
        onItemFocusChange(hasFocus());*/
    }

 /*    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        switch (mGravity) {
            case GRAVITY_TOP:
                mLayoutTop = 0;
                break;
            case GRAVITY_CENTER:
                mLayoutTop = (getMeasuredHeight() - mItemHeight) / 2;
                break;
            case GRAVITY_BOTTOM:
                mLayoutTop = getMeasuredHeight() - mItemHeight;
                break;
        }
    }

    public final void smoothScrollTo(int toX, int toY) {
        Log.d("zxy", "smoothScrollTo: toX = "+toX);
        smoothScrollBy(toX - getScrollX(), 0);
    }

    *//**
     * Scroll smoothly instead of immediately.
     *//*
    public final void smoothScrollBy(int dx, int dy) {
        if (getChildCount() == 0) {
            return;
        }
        final int scrollX = getScrollX();
        int minX = 0;
        int maxX = mFillInWidth - getWidth() + minX;
        Log.d("zxy", "smoothScrollBy: scrollX = "+scrollX+", maxX = "+maxX);
        if (scrollX + dx < minX) {
            dx = minX - scrollX;
        } else if (scrollX + dx > maxX) {
            dx = maxX - scrollX;
        }
        long duration = AnimationUtils.currentAnimationTimeMillis() - mLastScrollTimeStamp;
        if (duration > 250) {
            mScroller.startScroll(scrollX, getScrollY(), dx, 0, 250);
            Log.d("zxy", "smoothScrollBy: scrollX = "+scrollX+", "+getScrollY());
            invalidate();
        } else {
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
            scrollBy(dx, dy);
        }
        mLastScrollTimeStamp = AnimationUtils.currentAnimationTimeMillis();
    }

    // 锚点位置
    private int getScrollXByPosition(int position) {
        int left = mLeftOffSet + position * (mSpacing + mItemWidth);
        int right = left + mItemWidth;
        int dstRight = right + mRightOffSet;
        int dstLeft = left - mLeftOffSet;
        int x = getScrollX();
        if (right > x + getWidth() - mRightOffSet) {
            return dstRight - getWidth();
        } else if (left < x + mLeftOffSet) {
            return dstLeft;
        } else {
            return getScrollX();
        }
    }

    public void onItemFocusChange(boolean focused) {

        // anim focused itemView
        View selected = getSelectedView();
        if (null != selected) {
            selected.setSelected(focused);
            if (focused) {
                zoomIn(selected);
            } else {
                zoomOut(selected);
            }
        }

        // call back
        OnItemSelectedListener onItemSelectedListener = getOnItemSelectedListener();
        if (onItemSelectedListener != null) {
            if (selected != null) {
                onItemSelectedListener.onItemSelected(this, selected, mSelectedPosition, 0);
            } else {
                onItemSelectedListener.onNothingSelected(this);
            }
        }
    }

    // 放大
    private void zoomIn(View view) {
        if (view != null) {
            view.animate().scaleX(mItemScaleFactor).scaleY(mItemScaleFactor).setDuration(mItemScaleDuration).start();
        }
    }

    // 缩小
    private void zoomOut(View view) {
        if (view != null) {
            view.animate().scaleX(1).scaleY(1).setDuration(mItemScaleDuration).start();
        }
    }

    private class InnerDataSetObserver extends DataSetObserver {

        boolean dataChanged;

        @Override
        public void onChanged() {
            mItemCount = getAdapter().getCount();
            mFillInWidth = mLeftOffSet + mRightOffSet + (mItemCount - 1) * mSpacing + mItemCount * mItemWidth;
            dataChanged = true;
            removeAllViewsInLayout();
            scrollTo(0, 0);
            requestLayout();
        }

        @Override
        public void onInvalidated() {
            // nothing
        }

        public boolean isDataChanged() {
            boolean changed = dataChanged;
            dataChanged = false;
            return changed;
        }

        public void onEmpty() {
            mItemCount = 0;
            mFillInWidth = 0;
            dataChanged = true;
            removeAllViewsInLayout();
            scrollTo(0, 0);
        }
    }

    private void layoutChildren(int areaStartX, int areaEndX) {

        // 1. ensure start & end position
        final int startPosition = (areaStartX - mLeftOffSet) / (mItemWidth + mSpacing);
        final int endPosition = (areaEndX - mLeftOffSet) / (mItemWidth + mSpacing) + 1;
        final int count = endPosition - startPosition;

        final int oldCount = getChildCount();
        if (startPosition == mFirstPosition && oldCount == count) {
            return;
        }

        final int start = Math.max(Math.min(mFirstPosition, startPosition), 0);

        final int end = Math.min(Math.max(mFirstPosition + oldCount, startPosition + count), mItemCount);

        // 2.ensure scrap views
        Stack<View> removed = new Stack<View>();

        for (int i = start; i < end && i < mItemCount; i++) {
            boolean isNew = i >= startPosition && i < startPosition + count;
            boolean isOld = i >= mFirstPosition && i < mFirstPosition + oldCount;
            if (isOld && !isNew) {
                View child = getChildAt(i - mFirstPosition);
                if (child != null) {
                    removed.push(child);
                }
            }
        }

        // 3.remove views into scrap recycler
        while (!removed.empty()) {
            View child = removed.pop();
            if (child != null) {
                child.setSelected(false);
                child.clearAnimation();
                removeViewInLayout(child);
                mRecycler.addScrapView(child);
            }
        }

        // 4.layout scrap views
        for (int position = start; position < end && position < mItemCount; position++) {
            boolean isNew = position >= startPosition && position < startPosition + count;
            boolean isOld = position >= mFirstPosition && position < mFirstPosition + oldCount;
            if (!isOld && isNew) {
                View child = mRecycler.getScrapView();
                child = mAdapter.getView(position, child, this);
                boolean rightFlow = position >= mFirstPosition;
                layoutChild(child, position, rightFlow);
            }
        }
        mFirstPosition = startPosition;
    }

    private void layoutChild(View view, int position, boolean rightFlow) {
        // 1. measure
        measureItem(view);

        // 2. add in layout
        int relativeIndex = rightFlow ? -1 : 0;
        addViewInLayout(view, relativeIndex, null, true);

        // 3. layout
        int left = mLeftOffSet + (position) * (mItemWidth + mSpacing);
        int top = mLayoutTop;
        view.layout(left, top, left + view.getMeasuredWidth(), top + view.getMeasuredHeight());
    }

    private void measureItem(View view) {
        LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(mItemWidth, mItemHeight);
        } else {
            params.width = mItemWidth;
            params.height = mItemHeight;
        }
        view.setLayoutParams(params);
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(params.width, MeasureSpec.EXACTLY);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.EXACTLY);
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }


    // 回收站
    private static class RecycleBin {

        private final Stack<View> mScrapViews;

        public RecycleBin() {
            mScrapViews = new Stack<View>();
        }

        public View getScrapView() {
            if (!mScrapViews.empty()) {
                return mScrapViews.pop();
            }
            return null;
        }

        public void addScrapView(View v) {
            mScrapViews.push(v);
        }

        public void clear() {
            mScrapViews.clear();
        }
    }*/

    public void setLastPosition() {
        Log.d("zxy", "setLastPosition: ");
        requestLayout();
    }



    private void addAndMeasureChild(final View child, int viewPos) {
        LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        }

        addViewInLayout(child, viewPos, params, true);
        child.measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.AT_MOST));
    }


    @Override
    protected synchronized void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
       /* if (changed || mDataSetObserver.isDataChanged()) {
            super.onLayout(changed, left, top, right, bottom);
            removeAllViewsInLayout();
            layoutChildren(0, getWidth());
            setSelection(mSelectedPosition);
        }*/

        if (mAdapter == null) {
            return;
        }

        if (mDataChanged) {
            int oldCurrentX = mCurrentX;
            initView();
            removeAllViewsInLayout();
            mNextX = oldCurrentX;
            mDataChanged = false;
        }

        if (mScroller.computeScrollOffset()) {
            int scrollx = mScroller.getCurrX();
            mNextX = scrollx;
        }

        if (mNextX <= 0) {
            mNextX = 0;
            mScroller.forceFinished(true);
        }
        if (mNextX >= mMaxX) {
            mNextX = mMaxX;
            mScroller.forceFinished(true);
        }

        int dx = mCurrentX - mNextX;

        removeNonVisibleItems(dx);
        fillList(dx);
        positionItems(dx);

        mCurrentX = mNextX;

        if (!mScroller.isFinished()) {
            post(new Runnable() {
                @Override
                public void run() {
                    requestLayout();
                }
            });

        }
    }

    private void fillList(final int dx) {
        int edge = 0;
        View child = getChildAt(getChildCount() - 1);
        if (child != null) {
            edge = child.getRight();
        }
        fillListRight(edge, dx);

        edge = 0;
        child = getChildAt(0);
        if (child != null) {
            edge = child.getLeft();
        }
        fillListLeft(edge, dx);


    }

    private void fillListRight(int rightEdge, final int dx) {
        while (rightEdge + dx < getWidth() && mRightViewIndex < mAdapter.getCount()) {

            View child = mAdapter.getView(mRightViewIndex, mRemovedViewQueue.poll(), this);
            addAndMeasureChild(child, -1);
            rightEdge += child.getMeasuredWidth();

            if (mRightViewIndex == mAdapter.getCount() - 1) {
                mMaxX = mCurrentX + rightEdge - getWidth();
            }

            if (mMaxX < 0) {
                mMaxX = 0;
            }
            mRightViewIndex++;
        }

    }

    private void fillListLeft(int leftEdge, final int dx) {
        while (leftEdge + dx > 0 && mLeftViewIndex >= 0) {
            View child = mAdapter.getView(mLeftViewIndex, mRemovedViewQueue.poll(), this);
            addAndMeasureChild(child, 0);
            leftEdge -= child.getMeasuredWidth();
            mLeftViewIndex--;
            mDisplayOffset -= child.getMeasuredWidth();
        }
    }

    private void removeNonVisibleItems(final int dx) {
        View child = getChildAt(0);
        while (child != null && child.getRight() + dx <= 0) {
            mDisplayOffset += child.getMeasuredWidth();
            mRemovedViewQueue.offer(child);
            removeViewInLayout(child);
            mLeftViewIndex++;
            child = getChildAt(0);

        }

        child = getChildAt(getChildCount() - 1);
        while (child != null && child.getLeft() + dx >= getWidth()) {
            mRemovedViewQueue.offer(child);
            removeViewInLayout(child);
            mRightViewIndex--;
            child = getChildAt(getChildCount() - 1);
        }
    }

    private void positionItems(final int dx) {
        if (getChildCount() > 0) {
            mDisplayOffset += dx;
            int left = mDisplayOffset;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                int childWidth = child.getMeasuredWidth();
                child.layout(left, 0, left + childWidth, child.getMeasuredHeight());
                left += childWidth + child.getPaddingRight();
            }
        }
    }

    public synchronized void scrollTo(int x) {
        mScroller.startScroll(mNextX, 0, x - mNextX, 0);
        requestLayout();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean handled = super.dispatchTouchEvent(ev);
        handled |= mGesture.onTouchEvent(ev);
        return handled;
    }

    protected boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                              float velocityY) {
        synchronized (HorizontalListView.this) {
            mScroller.fling(mNextX, 0, (int) -velocityX, 0, 0, mMaxX, 0, 0);
        }
        requestLayout();

        return true;
    }

    protected boolean onDown(MotionEvent e) {
        mScroller.forceFinished(true);
        return true;
    }

    private OnGestureListener mOnGesture = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return HorizontalListView.this.onDown(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return HorizontalListView.this.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {

            synchronized (HorizontalListView.this) {
                mNextX += (int) distanceX;
            }
            requestLayout();

            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (isEventWithinView(e, child)) {
                    if (mOnItemClicked != null) {
                        mOnItemClicked.onItemClick(HorizontalListView.this, child, mLeftViewIndex + 1 + i, mAdapter.getItemId(mLeftViewIndex + 1 + i));
                    }
                    if (mOnItemSelected != null) {
                        mOnItemSelected.onItemSelected(HorizontalListView.this, child, mLeftViewIndex + 1 + i, mAdapter.getItemId(mLeftViewIndex + 1 + i));
                    }
                    break;
                }

            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (isEventWithinView(e, child)) {
                    if (mOnItemLongClicked != null) {
                        mOnItemLongClicked.onItemLongClick(HorizontalListView.this, child, mLeftViewIndex + 1 + i, mAdapter.getItemId(mLeftViewIndex + 1 + i));
                    }
                    break;
                }

            }
        }

        private boolean isEventWithinView(MotionEvent e, View child) {
            Rect viewRect = new Rect();
            int[] childPosition = new int[2];
            child.getLocationOnScreen(childPosition);
            int left = childPosition[0];
            int right = left + child.getWidth();
            int top = childPosition[1];
            int bottom = top + child.getHeight();
            viewRect.set(left, top, right, bottom);
            return viewRect.contains((int) e.getRawX(), (int) e.getRawY());
        }
    };



}
