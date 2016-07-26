package com.dachen.dgroupdoctorcompany.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by weiwei on 2016/4/14.
 */
public class NoScrollerPullToRefreshListView extends PullToRefreshListView {

    public NoScrollerPullToRefreshListView(Context context) {
        super(context);

    }

    public NoScrollerPullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
