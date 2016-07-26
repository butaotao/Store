package com.dachen.mediecinelibraryrealize.entity;

import java.util.ArrayList;

import com.dachen.medicine.entity.Result;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SomeBox extends Result{
	public ArrayList<patientSuggest> c_patient_drug_suggest_list;
	public class patientSuggest{
		public Unit unit;
		public String days;
		public String takeMedicalTime;
		public String unitname;
		public class Unit{ 
			public String  id  ;
			public String  _type;
			public String  title;
			public String  name; 
		}
		//
		/*@Expose
		@SerializedName("id")*/
		public String drugid;
		public String patientName;
		public String type;

		public String requires_quantity;
		public String manufacturer;
		public int dateSeq;
		public String image;
		public String pack_specification;
		public String general_name;
		public String trade_name;
		public boolean flagOpen = false;
		public Drug drug;
		public String title;
		public Data1 data1;
		public String units;
		public String specification;
		public class Data{
	    	  public int bptydxsdphdjfs;
	    	  public int qtqdxsdphdjfs;
			//购药所需最低积分
	    	  public int zsmdwypxhjfs;
			//每单位所需积分
	    	  public int zyzdsxjfs;
	      }
	    public Data data;
	    public class Data1 {
	    	public int num_zjf;
	    	public int ybxfjf;
			////剩余积分
	    	public int num_syjf;
	    }

		@Expose
		@SerializedName("detailJson")
		public ArrayList<Uses> c_drug_usage_list;
		public class Drug{
			public String id;
			public String _type;
			public String title;
		}
		public class Uses{
			@Expose
			@SerializedName("doseMothod")
			public String method;
			@Expose
			@SerializedName("doseQuantity")
			public String quantity;
			public String target_patient_type;
			public String times;
			public String patients;
			public String unit;
			public String periodNum;
			//periodNum
			public Period period;
			public String goodsId;
			public String goodsNumber;
			public String periodUnit;
			public String doseDays;
			public String doseUnit;
			public String doseUnitName;
			public String periodTimes;
			 public class Period{
				 public String unit;
	             public String text;
	             public String number;
			}
		}
	}
	
}
