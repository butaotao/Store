package com.dachen.medicine.common.utils;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.j256.ormlite.dao.Dao;

public interface CallApplicationInterface {
	public HashMap<String, String> getInterfaceMaps();
	public Bitmap getBitmap(String str); 
	public Dao<Alarm, Integer> getInterfacedbAlarm(); 
	public Dao<DrugRemind, Integer> getInterfacedbDrugRemind();
	public Dao<IllEntity,Integer> getInterfaceIllEntity();
	public void resumeIm();
	public void pauseIm();
	public int startLoginActivity(Context context);
	public void showUpdateDilog(Context context,String s);
	public void closeDialog(Context activity);
}
