package com.dachen.medicine.common.utils;

import android.text.TextUtils;
import android.view.TextureView;

public class CustomUtils {
	public static int getData(int needBuy,int haveBuy,int nowBuy){
		 
		if (haveBuy==0 && 0!= nowBuy) {
			return  nowBuy -  needBuy;
		}else if(0 == nowBuy && 0!=haveBuy){
		return	needBuy-haveBuy;
		}else if(0 == nowBuy && 0==haveBuy){
			return  needBuy;
		}
		return  nowBuy - (needBuy-haveBuy);
	} 
	public static int getTotalData(int needBuy,int haveBuy,int nowBuy){
		if (needBuy==0) {
			return 0;
		}
		if (haveBuy==0 && 0!= nowBuy) {
			if (nowBuy -  needBuy<0) {
				return 0;
			}
			return  nowBuy -  needBuy;
		}else if(0 == nowBuy && 0!=haveBuy){
			if (needBuy-haveBuy<0) {
				return 0;
			}
		return	needBuy-haveBuy;
		}else if(0 == nowBuy && 0==haveBuy){
			 
			return  needBuy;
		}
		if (nowBuy - (needBuy-haveBuy)<0) {
			return 0;
		}
		return  nowBuy - (needBuy-haveBuy);
	} 
}
