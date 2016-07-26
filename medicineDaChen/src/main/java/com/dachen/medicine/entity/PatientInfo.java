package com.dachen.medicine.entity;

public class PatientInfo extends Result{
	public String id;
	public String _type;
	public String species_number; 
	public UserId user_id;
	public Group group;
	public Doctor doctor;
	public Patient patient;
	public class UserId{
		public String id;
		public String _type;
		public String title;
		public String name;
		@Override
		public String toString() {
			return "UserId [id=" + id + ", _type=" + _type + ", title=" + title
					+ ", name=" + name + "]";
		} 
		
	}
	public class Group{
		public String id;
		public String _type;
		public String title;
		public String name;
		@Override
		public String toString() {
			return "Group [id=" + id + ", _type=" + _type + ", title=" + title
					+ ", name=" + name + "]";
		} 
		
	}
	public class Doctor{
		public String id;
		public String _type;
		public String title;
		public String name;
		@Override
		public String toString() {
			return "Doctor [id=" + id + ", _type=" + _type + ", title=" + title
					+ ", name=" + name + "]";
		} 
		
	}
	public class Patient{
		public String id;
		public String _type;
		public String title;
		public String user_name;
		@Override
		public String toString() {
			return "Patient [id=" + id + ", _type=" + _type + ", title="
					+ title + ", user_name=" + user_name + "]";
		} 
		
	}
	@Override
	public String toString() {
		return "PatientInfo [id=" + id + ", _type=" + _type
				+ ", species_number=" + species_number + ", user_id=" + user_id
				+ ", group=" + group + ", doctor=" + doctor + ", patient="
				+ patient + "]";
	}
	
}
