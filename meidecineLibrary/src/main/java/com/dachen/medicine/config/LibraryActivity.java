package com.dachen.medicine.config;

import android.app.Activity;
import android.os.Bundle;

public abstract class LibraryActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	public abstract void setTitleColor();
	
}
