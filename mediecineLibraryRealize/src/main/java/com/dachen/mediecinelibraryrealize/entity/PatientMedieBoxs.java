package com.dachen.mediecinelibraryrealize.entity;

import java.util.ArrayList;

import com.dachen.medicine.entity.Result;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientMedieBoxs extends Result{
	//data
	@Expose
	@SerializedName("data")
	public ArrayList<Info> info_list;
	public int page_count; 
/*
{
  "data": [
    {
      "creator": 12666,
      "creatorDate": 1464682772016,
      "goodsTypeNumber": 1,
      "id": "574d49142f142526f0425f5d",
      "patientId": 1371,
      "type": 1,
      "updator": 12666,
      "updatorDate": 1464682772016,
      "userId": 313
    },

 */
	public class Info{
		public String id;
		public String _type;
		public int type;
	public String buyStatus;
	@Expose
	@SerializedName("goodsTypeNumber")
		public int species_number;
		public String last_modified_time;

	@Expose
	@SerializedName("updatorDate")
		public String date;
		public Group group;
		public State state;
		public Doctor doctor;
		public Patient patient;
		public String patientId;
		public String created_time;
	    public String  doctorName;
		public String	 groupName;
		public String patientName;
		public class Patient{
			public String id;
			public String _type;
			public String user_name;
			public String title;

			@Override
			public String toString() {
				return "Patient{" +
						"id='" + id + '\'' +
						", _type='" + _type + '\'' +
						", user_name='" + user_name + '\'' +
						", title='" + title + '\'' +
						'}';
			}
		}
		public class State{
			public String value;
			public String title;

			@Override
			public String toString() {
				return "State{" +
						"value='" + value + '\'' +
						", title='" + title + '\'' +
						'}';
			}
		}
		public class Group{
			public String id = "";
			public String _type = "";
			public String name = "";
			public String title = "";

			@Override
			public String toString() {
				return "Group{" +
						"id='" + id + '\'' +
						", _type='" + _type + '\'' +
						", name='" + name + '\'' +
						", title='" + title + '\'' +
						'}';
			}
		}
		public class Doctor{
			public String id;
			public String _type;
			public String name;
			public String title;

			@Override
			public String toString() {
				return "Doctor{" +
						"id='" + id + '\'' +
						", _type='" + _type + '\'' +
						", name='" + name + '\'' +
						", title='" + title + '\'' +
						'}';
			}
		}

		@Override
		public String toString() {
			return "Info{" +
					"id='" + id + '\'' +
					", _type='" + _type + '\'' +
					", species_number='" + species_number + '\'' +
					", last_modified_time='" + last_modified_time + '\'' +
					", date='" + date + '\'' +
					", group=" + group +
					", state=" + state +
					", doctor=" + doctor +
					", patient=" + patient +
					", created_time='" + created_time + '\'' +
					'}';
		}
	}

	@Override
	public String toString() {
		return "PatientMedieBoxs{" +
				"info_list=" + info_list +
				", page_count=" + page_count +
				'}';
	}
}
