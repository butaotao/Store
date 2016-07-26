package com.dachen.mediecinelibraryrealize.entity;

import java.util.List;

import com.dachen.medicine.entity.Result;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineEntity.Goods;
import com.dachen.mediecinelibraryrealizedoctor.entity.SomeBox.patientSuggest.Unit;


public class PatientPoints extends Result{
	public List<Potient> info_list;
	public static class Potient{
		public String num_hqjf;
		public int num_xfjf;
		public Goods goods;
		public String num_dh;
		public Unit goods$unit;
		public String created_time;
	}

}
