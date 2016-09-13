package com.dachen.dgroupdoctorcompany.views;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.views.wheel.DateObject;
import com.forexpand.datepicker.OnWheelChangedListener;


import java.util.ArrayList;
import java.util.Calendar;

import cn.qqtheme.framework.picker.WheelPicker;
import cn.qqtheme.framework.widget.WheelView;

/**
 * 自定义的时间选择器
 * @author sxzhang
 *
 */
public class TimePickerCustomer extends LinearLayout {
	protected int textSize = WheelView.TEXT_SIZE;
	Context context;
	/**
	 * The Text color normal.
	 */
	protected int textColorNormal = WheelView.TEXT_COLOR_NORMAL;
	/**
	 * The Text color focus.
	 */
	protected int textColorFocus = WheelView.TEXT_COLOR_FOCUS;
	/**
	 * The Line color.
	 */
	protected int lineColor = WheelView.LINE_COLOR;
	/**
	 * The Line visible.
	 */
	protected boolean lineVisible = true;
	public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
	/**
	 * The Offset.
	 */
	protected int offset = WheelView.OFF_SET;
	private Calendar calendar = Calendar.getInstance();
	public WheelView hours, mins; //Wheel picker
	private OnChangeListener onChangeListener; //onChangeListener
	private final int MARGIN_RIGHT = 0;		//调整文字右端距离
	public ArrayList<String> hourList,minuteList;
	private DateObject dateObject;		//时间数据对象

	/**
	 * Instantiates a new Wheel picker.
	 *
	 * @param activity the activity
	 */
	public TimePickerCustomer(Activity activity) {
		super(activity);
	}
	//Constructors


	private void init(Context context){

		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		hourList = new ArrayList<String>();
		minuteList = new ArrayList<String>();

		for (int i = 0; i < 24; i++) {
			String h = String.format("%02d",i);
			hourList.add(h );
		}

		for (int j = 0; j < 60; j++) {
			String m = String.format("%02d",j);
			minuteList.add(m);
		}

		//小时选择器
		hours = new WheelView(context);
		/*LayoutParams lparams_hours = new LayoutParams(180,LayoutParams.WRAP_CONTENT);
		lparams_hours.setMargins(0, 0, MARGIN_RIGHT, 0);*/
	/*	LinearLayout lparams_hours = new LinearLayout(context);
		lparams_hours.setOrientation(LinearLayout.HORIZONTAL);
		lparams_hours.setGravity(Gravity.CENTER);*/
		LayoutParams lparams_hours = new LayoutParams(180,LayoutParams.WRAP_CONTENT);
		lparams_hours.setMargins(0, 0, MARGIN_RIGHT, 0);
		hours.setLayoutParams(lparams_hours);
		//hours.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
		hours.setTextSize(textSize);
		hours.setTextColor(textColorNormal, textColorFocus);
		hours.setLineVisible(lineVisible);
		hours.setLineColor(lineColor);
		hours.setOffset(2);

		addView(hours);
		//分钟选择器
		mins = new WheelView(context);
		mins.setTextSize(textSize);
		mins.setLayoutParams(new LayoutParams(180, LayoutParams.WRAP_CONTENT));
		//mins.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
		mins.setTextColor(textColorNormal, textColorFocus);
		mins.setLineVisible(lineVisible);
		mins.setLineColor(lineColor);
		mins.setOffset(2);
		addView(mins);
		hours.setItems(hourList);
		hours.setItems(minuteList);

	}



	//listeners
	private OnWheelChangedListener onHoursChangedListener = new OnWheelChangedListener(){
		@Override
		public void onChanged(com.forexpand.datepicker.WheelView wheel, int oldValue, int newValue) {
			calendar.set(Calendar.HOUR_OF_DAY, newValue);
			change();
		}

	};
	private OnWheelChangedListener onMinsChangedListener = new OnWheelChangedListener(){
		@Override
		public void onChanged(com.forexpand.datepicker.WheelView wheel, int oldValue, int newValue) {
			calendar.set(Calendar.MINUTE, newValue);
			change();
		}

	};


	/**
	 * 滑动改变监听器回调的接口
	 */
	public interface OnChangeListener {
		void onChange(int hour, int munite);
	}

	/**
	 * 设置滑动改变监听器
	 * @param onChangeListener
	 */
	public void setOnChangeListener(OnChangeListener onChangeListener){
		this.onChangeListener = onChangeListener;
	}

	/**
	 * 滑动最终调用的方法
	 */
	private void change(){
		if(onChangeListener!=null){
			onChangeListener.onChange(getHourOfDay(), getMinute());
		}
	}


	/**
	 * 获取小时
	 * @return
	 */
	public int getHourOfDay(){
		return Integer.parseInt(hourList.get(hours.getSelectedIndex()));
	}

	/**
	 * 获取分钟
	 * @return
	 */
	public int getMinute(){
		return Integer.parseInt((minuteList.get(mins.getSelectedIndex())));
	}
	/**
	 * Instantiates a new Wheel view.
	 *
	 * @param context the context
	 * @param attrs   the attrs
	 */
	public TimePickerCustomer(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init(context);
	}

	/**
	 * Instantiates a new Wheel view.
	 *
	 * @param context  the context
	 * @param attrs    the attrs
	 * @param defStyle the def style
	 */
	public TimePickerCustomer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init(context);
	}
}
