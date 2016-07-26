package com.dachen.medicine.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class DragList extends Result implements Serializable{
	public ArrayList<CdrugRecipeitem> c_drug_recipeitems;
	public UserId user_id;
	public Doctor doctor;
	public Patient patient;
	public String top_path;
	public Patientinfo patient_info;

	public class Patientinfo{
		public Sex sex;
		public class Sex{
			public String title;
			public String value;
		}
	}
	public class Doctor{
		public String  name ;
		public String  _type;
		public String  id;
		public String  title;
		@Override
		public String toString() {
			return "Doctor [name=" + name + ", _type=" + _type + ", id=" + id
					+ ", title=" + title + "]";
		}
		
	}
	public class UserId{
		public String  name ;
		public String  _type;
		public String  id;
		public String  title;


		@Override
		public String toString() {
			return "UserId{" +
					"name='" + name + '\'' +
					", _type='" + _type + '\'' +
					", id='" + id + '\'' +
					", title='" + title + '\'' +

					'}';
		}
	}
	public class Patient{
		public String  name ;
		public String  _type;
		public String  id;
		public String  title;
		public String user_name;
		@Override
		public String toString() {
			return "UserId [name=" + name + ", _type=" + _type + ", id=" + id
					+ ", title=" + title + "]";
		}
		
	}
	@Override
	public String toString() {
		return "DragList [c_drug_recipeitems=" + c_drug_recipeitems
				+ ", user_id=" + user_id + ", doctor=" + doctor + ", patient="
				+ patient + "]";
	}
	

}
