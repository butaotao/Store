package com.dachen.mediecinelibraryrealizedoctor.entity;

import java.util.List;

import com.dachen.medicine.entity.Result;

public class Patients extends Result{
	public List<patient> c_user_patient_list;
	
	@Override
	public String toString() {
		return "Patients [c_user_patient_list=" + c_user_patient_list + "]";
	}

	public class patient{
		public String user_name;
		public String id;
		@Override
		public String toString() {
			return "patient [user_name=" + user_name + ", id=" + id + "]";
		}
		
	};
}
