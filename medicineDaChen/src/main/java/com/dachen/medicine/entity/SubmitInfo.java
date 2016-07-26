package com.dachen.medicine.entity;

public class SubmitInfo extends Result{
	public String is_success;

	@Override
	public String toString() {
		return "SubmitInfo [is_success=" + is_success + ", resultCode="
				+ resultCode + ", resultMsg=" + resultMsg + ", errormessage="
				+ errormessage + "]";
	}


}
