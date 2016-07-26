package com.dachen.medicine.net;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 接口链接管理类 作者 yuankangle 时间 2015/7/14
 */
public class UrlManager {

	/**
	 * map参数转string
	 * 
	 * @param params
	 * @return
	 */
	private static String paramsToString(HashMap<String, String> params) {
		StringBuffer paramsString = new StringBuffer();
		if (params != null) {
			Iterator it = params.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next().toString();
				paramsString.append("&" + key);
				paramsString.append("=" + params.get(key));
			}
		}
		return paramsString.toString();
	}

}
