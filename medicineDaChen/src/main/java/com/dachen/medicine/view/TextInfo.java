package com.dachen.medicine.view;

import android.graphics.Color;

public class TextInfo {
	public TextInfo(int index, String text, boolean isSelected) {
		mIndex = index;
		mText = text;
		mIsSelected = isSelected;
		if (isSelected) {
			mColor = Color.BLUE;
		}
	}

	public int mIndex;
	public String mText;
	public boolean mIsSelected = false;
	public int mColor = Color.BLACK;
}