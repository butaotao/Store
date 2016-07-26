package com.dachen.mediecinelibraryrealize.entity;

import com.dachen.medicine.entity.Result;

import java.util.ArrayList;
import java.util.List;

public class MedicienBreedBean extends Result {

	//	public List<String> set_datas;
	public Data data;
	public class Data{
		public ArrayList<PageData> pageData;
		public class PageData{
		/*	public String abbr;
			public String companyName;*/
			public String generalName;
			public String id;
			public String manufacturer;/*
			public String manufacturer2;
			public String pharmacoTypes;*/
			public String title;
		//	public String tradeName;
		}
	}


}
