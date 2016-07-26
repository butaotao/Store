package com.dachen.medicine.entity;

import java.util.List;

public class FeesRecords extends Result{
	public int total;
	public List<FeesRecord> info_list;

	public class FeesRecord{
		public String fee;
		public String created_time;
		public Lastmodifier last_modifier;
		public String id;
		public class Lastmodifier{
			public String id;
		}
	}
	
	}

