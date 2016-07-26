package com.dachen.medicine.logic;

import android.text.TextUtils;
import android.view.View;

import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.entity.CdrugRecipeitem;

public class CompareData {
	public static String getName(String general_name,String tradename,String isopen){
		if (TextUtils.isEmpty(isopen)) {
			isopen = SharedPreferenceUtil.getString("isopen_" + SharedPreferenceUtil.getString("id", ""), "1");
		}
		String name = general_name;
		if (isopen.equals("1")) {
			name = general_name;
		}else {
		  name = tradename;
		  if (name==null||name.equals("")) {
			  name = general_name;
		}
		}
		if (null != name && name.length() > 9) {
			name = name.substring(0, 6) + "..."
					+ name.substring(name.length() - 2); 
		}  
		return name;
	}
	public static boolean isShow(CdrugRecipeitem media){
		if(media.is_join){

		if (null==media.data1||0==media.data1.num_syjf||null==media.data||0==media.data.zsmdwypxhjfs/*||0==media.data.zyzdsxjfs*/) {
			return false;
		} else if (null!=media.data1&&media.data!=null&&
				media.data1.num_syjf>=media.data.zyzdsxjfs
				&&media.data1.num_syjf>=media.data.zsmdwypxhjfs) {
			return true;
		}

		}
		return false; 
	}
	public static int showNum(CdrugRecipeitem media){
		if (null==media.data1||0==media.data1.num_syjf||null==media.data||0==media.data.zsmdwypxhjfs/*||0==media.data.zyzdsxjfs*/) {
			return 0;
		} else if (null!=media.data1&&media.data!=null&&
				media.data1.num_syjf>=media.data.zyzdsxjfs
				&&media.data1.num_syjf>=media.data.zsmdwypxhjfs) {
			int n = media.data1.num_syjf/ media.data.zsmdwypxhjfs;
			return n;
		}
		return 0;
	}
}
