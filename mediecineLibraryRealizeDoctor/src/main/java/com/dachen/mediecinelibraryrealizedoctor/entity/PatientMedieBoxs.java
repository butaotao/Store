package com.dachen.mediecinelibraryrealizedoctor.entity;

import java.util.ArrayList;

import com.dachen.medicine.entity.Result;

public class PatientMedieBoxs extends Result{
	public ArrayList<Info> info_list;
	public int page_count; 

	public class Info{
		public String id;
		public String _type;
		public String species_number;
		public String last_modified_time;
		public String date;
		public Group group;
		public State state;
		public Doctor doctor;
		public Patient patient;
		public String created_time; 
		public class Patient{
			public String id;
			public String _type;
			public String user_name;
			public String title; 
		}
		public class State{
			public String value;
			public String title;
		}
		public class Group{
			public String id = "";
			public String _type = "";
			public String name = "";
			public String title = ""; 
		}
		public class Doctor{
			public String id;
			public String _type;
			public String name;
			public String title; 
		}
	}
}
