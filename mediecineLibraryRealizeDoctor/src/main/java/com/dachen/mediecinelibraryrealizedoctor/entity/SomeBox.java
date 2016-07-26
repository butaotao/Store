package com.dachen.mediecinelibraryrealizedoctor.entity;

import com.dachen.medicine.entity.Result;

import java.util.ArrayList;

public class SomeBox extends Result{
	public ArrayList<patientSuggest> c_patient_drug_suggest_list;
	public static class patientSuggest{
		public Unit unit;
		public static class Unit{
			public String  id  ;
			public String  _type;
			public String  title;
			public String  name;
		}
		public String requires_quantity;
		public String manufacturer;
		public String pack_specification;
		public String general_name;
		public String trade_name;
		public boolean flagOpen = false;
		public Drug drug;
		public String title;
		public ArrayList<Uses> c_drug_usage_list;
		public class Drug{
			public String id;
			public String _type;
			public String title;
		}
		public class Uses{
			public String method;
			public String quantity;
			public String target_patient_type;
			public String times;
			public Period period;
			public class Period{
				public String unit;
				public String text;
				public String number;
			}
		}
	}

}
