package com.dachen.medicine.common.utils;

public class CustomUtils {
	public static int getData(String needBuy,String haveBuy,String nowBuy){
		if (haveBuy==null && null!= nowBuy) {
			return Integer.parseInt(nowBuy) - Integer.parseInt(needBuy);
		}else if(null == nowBuy && null!=haveBuy){
		return	(Integer.parseInt(needBuy)-Integer.parseInt(haveBuy));
		}else if(null == nowBuy && null==haveBuy){
			return Integer.parseInt(needBuy);
		}
		return  Integer.parseInt(nowBuy) - (Integer.parseInt(needBuy)-Integer.parseInt(haveBuy));
	} 
}
