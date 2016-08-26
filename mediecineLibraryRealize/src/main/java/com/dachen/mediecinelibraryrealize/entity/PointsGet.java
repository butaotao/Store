package com.dachen.mediecinelibraryrealize.entity;

import com.dachen.medicine.entity.Result;

import java.util.List;

public class PointsGet extends Result{
	public List<Point> info_list;
	public int num_dh;
	public static class Point{
		//有赠送药当为false的时候
		public boolean is_receive;
		public String num_dh;
		public String num_hqjf;
		public Goods$unit goods$unit;
		public String created_time;
		public String number;
		public Goods goods;
		public String id;
		public long createtime;
		public static class Goods{
			public String title;
		}
		public static class Goods$unit{
			public String title;
			public String id;
		}

	}



}
