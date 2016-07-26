package com.dachen.medicine.entity;

import java.util.List;

public class MyFeesDetails extends Result{
	public List<MyFees> info_list;
	public class MyFees{
		public int num;
		public String fee;
		public String created_time;
		public String datetime;
		public String[] calendar;
		public boolean isMon = true;
		public State state;
		public String id;
		public boolean isMon() {
			return isMon;
		}

		public void setMon(boolean isMon) {
			this.isMon = isMon;
		}
		public class State{ 
	        public String title;
	        public String value; 

		}

	}
	
}
