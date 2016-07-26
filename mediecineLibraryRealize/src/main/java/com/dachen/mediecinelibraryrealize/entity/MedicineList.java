package com.dachen.mediecinelibraryrealize.entity;

import java.util.ArrayList;
import java.util.List;

import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo;

public class MedicineList {
	public static List<MedicineInfo> lists_children;
public static List<MedicineInfo> getList(){
	if (null==lists_children) {
		lists_children = new ArrayList<MedicineInfo>();
	}
	return lists_children;
}
}
