package com.dachen.mediecinelibraryrealize.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.dachen.medicine.entity.Result;
 

public class AlarmsTime extends Result implements Serializable{
	public List<AlarmsTimeInfo> list_datas; 

	public class AlarmsTimeInfo  implements Serializable{
		public String id;
		public String goods;
		public String goods_name;
		public String time;
		public String general_name;
		public String title; 
		public int time_type;
		public int size;
		public int position;
		public String intergTime;
		public boolean is_done;
		public int times;
		public long eat;
		public ArrayList<AlarmsTimeInfo> infos;
		@Override
		public String toString() {
			return "AlarmsTimeInfo [id=" + id + ", goods=" + goods
					+ ", goods_name=" + goods_name + ", time=" + time
					+ ", time_type=" + time_type + "]";
		}
		
	}

	@Override
	public String toString() {
		return "AlarmsTime [list_datas=" + list_datas + "]";
	}
	
}
