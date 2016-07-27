package com.dachen.dgroupdoctorcompany.adapter;


import com.dachen.dgroupdoctorcompany.entity.MedicineEntity;

import java.util.HashMap;

public interface ChoiceItemNumInterface {
	void getList(HashMap<String, MedicineEntity> position);

	void isShow(boolean isShow);
}
