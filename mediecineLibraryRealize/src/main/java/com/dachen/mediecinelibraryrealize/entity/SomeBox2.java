package com.dachen.mediecinelibraryrealize.entity;

import com.dachen.medicine.entity.Result;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SomeBox2 extends Result{
	//{"data":{"creator":12666,"creatorDate":1464682772016,"detailJson":"","goodsTypeNumber":1,
	// "id":"574d49142f142526f0425f5d","patientId":1371,"patientName":"
	// 段段","type":1,"updator":12666,"updatorDate":1464682772016,"userId":313},
	// "resultCode":1,"resultMsg":"success"}

	public patientSuggest data;
	public class patientSuggest{
		public String patientName;
		public String type;
		public ArrayList<Uses> recipeDetailList;

		public class Uses{
			public String doseMothod;
			public String goodsPackSpecification;
			public String goodsManufacturer;
			public String goodsGenralName;
			public String goodsSpecification;
			public String goodsPackUnit;
			public String doseQuantity;
			public String title;
			public String target_patient_type;
			public String times;
			public String patients;
			public String periodNum;
			public String type;
			//periodNum
			public Period period;
			public String goodsId;
			public String goodsNumber;
			public String periodUnit;
			public String doseDays;
			public String doseUnit;
			public String doseUnitName;
			public String periodTimes;
			public String goodsTitle;
			public int leftPointsNum;
			public int lowPointsNum;
			public int consumePointsNum;
			 public class Period{
				 public String unit;
	             public String text;
	             public String number;
			}
		}
	}
	
}
