package com.dachen.medicine.entity;

import java.util.List;

public class MyFeesMonthDetails extends Result{
	public List<MyFees> data;
	public class MyFees {
		public int num;
		public String fee;
		public String created_time;
		public String datetime;
		public String[] calendar;
		public String drug_code;
		public String is_pass;
		public String is_paid;
		public boolean isMon = true;
		public State state;
		public String id;
		public Salesman salesman;
		public User user;

		public boolean isMon() {
			return isMon;
		}

		public void setMon(boolean isMon) {
			this.isMon = isMon;
		}

		public class State {
			public String title;
			public String value;

		}
		public class User{
			public String name;
			public String number;
		}
		public class Salesman {
			public String name;
			public String number;
		}

	}
}
