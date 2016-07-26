package com.dachen.medicine.common.utils;

import java.util.ArrayList;

public class ListListener<MedicineEntity> extends ArrayList<MedicineEntity> {
	public static ArrayList list;

	public ArrayList getList() {
		if (null == list) {
			list = new ArrayList<MedicineEntity>();
		}
		return list;
	}

	public ListListener() {
		list = new ArrayList<MedicineEntity>();
	}

	public boolean add(MedicineEntity object) {
		return list.add(object);
	}

	public boolean removes(MedicineEntity object) {
		return list.remove(object);
	}

}
