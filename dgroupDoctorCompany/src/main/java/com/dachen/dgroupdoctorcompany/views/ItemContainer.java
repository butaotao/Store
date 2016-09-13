package com.dachen.dgroupdoctorcompany.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.dachen.dgroupdoctorcompany.utils.CommonUitls;

public class ItemContainer extends LinearLayout {
    private int width;//组件宽
    private int height;//组件高
    private int childCount;
    private int childMarginLeft = (int)CommonUitls.dpToPixel(8f,getContext());//子控件相对左边控件的距离
    private int childMarginHorizonTal = (int)CommonUitls.dpToPixel(10f,getContext());//子控件相对最左、最右的距离
    private int childMarginTop = (int)CommonUitls.dpToPixel(8f,getContext());//子控件相对顶部控件的距离
    private int childWidth;//子控件宽
    private int childHeight;//子控件高
    private int itemNum = 4;
    public ItemContainer(Context context) {
        super(context);
    }

    public ItemContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void setItemNum(int itemNum){
        this.itemNum = itemNum;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        childCount = getChildCount();//得到子控件数量
        if(childCount>0) {
            childWidth = (width - childMarginLeft * itemNum+1) / itemNum;
            childHeight = (int)CommonUitls.dpToPixel(30f,getContext());//给子控件的高度一个定值
            //根据子控件的高和子控件数目得到自身的高
            height = childHeight * ((childCount-1)/ itemNum+1) + childMarginHorizonTal * 2 + childMarginTop*((childCount-1)/itemNum);
//            Log.d(childHeight,childHeight+);
        }else {
            //如果木有子控件，自身高度为0，即不显示
            height = 0;
        }
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
//        Log.d(height,height+);
        //根据自身的宽度约束子控件宽度
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //设置自身宽度
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        /**
         * 遍历所有子控件，并设置它们的位置和大小
         * 每行只能有四个子控件，且高度固定，宽度相同，且每行正好布满
         */
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);//得到当前子控件
            childView.layout((i%itemNum) * childWidth + (i%itemNum+1)*childMarginLeft
                    , (i / itemNum)*childHeight + childMarginHorizonTal + (i / itemNum)*childMarginTop
                    , (i%itemNum+1) * childWidth + (i%itemNum+1)*childMarginLeft
                    , (i / itemNum+1)*childHeight + childMarginHorizonTal + (i / itemNum)*childMarginTop);
        }
    }

}
