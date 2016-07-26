package com.dachen.medicine.entity;

import java.util.List;

public class PopularizeData extends Result{
	public String money_min;
	public String fee;
	public String fee_audit;
	public String fee_unaudit;
	public List<Datas> list_datas;
	public List<Datas> data;
	public class Datas{
		public String goods$company;
		public String goods$specification;
		public String goods$pack_specification;
		public Goods goods; 	
		public String fee;
		public String goods$manufacturer;
		public String goods$image;
	}
	public class Goods{
		public String title;
		public String id;
	}
}
