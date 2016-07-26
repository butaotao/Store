package com.dachen.medicine.common.utils;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.Log;

public class CommonUtils {
	 public static int getId(Context context,String paramString1, String paramString2)
	    {
	        try
	        {
	            Class localClass = Class.forName(context.getPackageName() + ".R$" + paramString1);
	            Field localField = localClass.getField(paramString2);
	            int i = Integer.parseInt(localField.get(localField.getName()).toString());
	            return i;
	        } catch (Exception localException)
	        {
	            Log.e("getIdByReflection error", localException.getMessage());
	        }
	 
	        return 0;
	    }
}
