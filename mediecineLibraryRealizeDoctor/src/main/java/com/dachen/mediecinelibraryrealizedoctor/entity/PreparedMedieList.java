package com.dachen.mediecinelibraryrealizedoctor.entity;

import java.util.ArrayList;
import java.util.List;

import com.dachen.medicine.entity.Result;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo.GoodBizType;

public class PreparedMedieList extends Result{
	public ArrayList<MedicineEntity> info_list;
	public class Prepared{
		public String number;
		public String created_time;
		public String trade_name;
		public String goods$manufacturer;
		public String goods$specification;
		public String goods$trade_name;
		public String goods$general_name;
		public String goods$pack_specification;
		public List<GoodsUsagesGoods> goods_usages_goods;
		public GoodBizType goods$biz_type; 
		public Goods goods;
		
		public class Goods{
			public String title;
			public String _type;
			public String id;
		}
	}
} 
