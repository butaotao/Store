package com.dachen.medicine.adapter;

import java.util.HashMap;

import com.dachen.medicine.entity.MedicineEntity;

public interface ChoiceItemNumInterface {
	public void getList(HashMap<String, MedicineEntity> position);

	public void isShow(boolean isShow);
}
