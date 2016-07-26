package com.dachen.dgroupdoctorcompany.adapter;


import com.dachen.dgroupdoctorcompany.entity.MedicineEntity;

import java.util.HashMap;

public interface ChoiceItemNumInterface {
	public void getList(HashMap<String, MedicineEntity> position);

	public void isShow(boolean isShow);
}
