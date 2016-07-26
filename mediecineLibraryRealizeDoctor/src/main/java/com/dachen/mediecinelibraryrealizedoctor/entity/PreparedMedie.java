package com.dachen.mediecinelibraryrealizedoctor.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.dachen.medicine.entity.Result;

public class PreparedMedie extends Result  implements Serializable{
	public ArrayList<Prepared> info_list;
	public class Prepared implements Serializable{
		

		public String image_url;
		public String specification;
		public String title;
		public String manufacturer;
		public String indications_text;
		public String trade_name;
		public boolean is_closed;
		public String abbr;
		public String general_name;
		public String id;
		public String ck_specification;
		public boolean in_process;
		public Company company;
		public class Company{ 
			
		public String name;
		public String _type;
		public String id;
		public String title;
		}
		public class Unit{
		public String name;
		public int id;
		public String title;
		}


	}
	public class Form implements Serializable{
		/**
		 *
		 */
		private static final long serialVersionUID = 7031101361752726791L;
		public String name;
		public String _type;
		public int id;
		public String title;
	}
}
