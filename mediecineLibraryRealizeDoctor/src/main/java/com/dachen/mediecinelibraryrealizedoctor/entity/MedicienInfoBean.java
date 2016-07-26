package com.dachen.mediecinelibraryrealizedoctor.entity;


import java.io.Serializable;
import java.util.List;

import com.dachen.medicine.entity.Result;

public class MedicienInfoBean extends Result implements Serializable{

	private static final long serialVersionUID = 1L;

	public List<MedicienInfoFactory> info_list;

	@Override
	public String toString() {
		return "MedicienInfoBean [info_list=" + info_list + "]";
	}
	

}
