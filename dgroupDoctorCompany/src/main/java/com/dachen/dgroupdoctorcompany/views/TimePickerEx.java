package com.dachen.dgroupdoctorcompany.views;

import android.app.Activity;

import com.dachen.common.utils.Logger;

import java.lang.reflect.Field;
import java.util.Calendar;

import cn.qqtheme.framework.picker.TimePicker;
import cn.qqtheme.framework.util.DateUtils;

/**
 * 修复TimePicker类setSelectedItem(int hour, int minute)方法得bug
 *
 * @author gaozhuo
 * @date 2016/3/10
 */
public class TimePickerEx extends TimePicker {

    public TimePickerEx(Activity activity) {
        super(activity);
    }

    public TimePickerEx(Activity activity, int mode) {
        super(activity, mode);
    }

    @Override
    public void setSelectedItem(int hour, int minute) {
        try {
            Field selectedHourField = getField(getClass().getSuperclass(), "selectedHour");
            setValue(selectedHourField, hour);
            Field selectedMinuteField = getField(getClass().getSuperclass(), "selectedMinute");
            setValue(selectedMinuteField, minute);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setValue(Field field, int value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(this, DateUtils.fillZero(value));
    }

    private Field getField(Class clazz, String fieldName) throws NoSuchFieldException {
        return clazz.getDeclaredField(fieldName);
    }

}
