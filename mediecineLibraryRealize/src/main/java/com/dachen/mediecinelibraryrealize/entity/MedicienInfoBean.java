package com.dachen.mediecinelibraryrealize.entity;

import java.util.List;

import com.dachen.medicine.entity.Result;

public class MedicienInfoBean extends Result {

	private static final long serialVersionUID = 1L;

	public List<MedicienInfo> info_list;

	@Override
	public String toString() {
		return "MedicienInfoBean [info_list=" + info_list + "]";
	}
	

}
