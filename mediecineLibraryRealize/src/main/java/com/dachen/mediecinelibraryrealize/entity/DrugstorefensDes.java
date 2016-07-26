package com.dachen.mediecinelibraryrealize.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.dachen.medicine.entity.Result;

public class DrugstorefensDes extends Result  implements Serializable{
	public boolean zcyb;
	public Area area;
	public String sybz;
	public class Area implements Serializable{
		public String    id;
		public String    _type;
		public String    title;
		public String    name;
		@Override
		public String toString() {
			return "Area [id=" + id + ", _type=" + _type + ", title=" + title
					+ ", name=" + name + "]";
		}
		
	}
	public String lxrsj;
	public String qyfzr;
	public boolean zcsy;
	public String name;
	public String lxdz;
	public String yysjd;
	public String title;
	public String manufacturer;
	public String pack_specification;
	public String specification;
	public class DrugList implements Serializable {
		public class CertState  implements Serializable {
			public String value;
			public String title;

			@Override
			public String toString() {
				return "CertState [value=" + value + ", title=" + title + "]";
			}
			
		}
		public class Unit  implements Serializable {
			public String  id;
			public String  _type;
			public String  title;
			public String  name;
			@Override
			public String toString() {
				return "Unit [id=" + id + ", _type=" + _type + ", title="
						+ title + ", name=" + name + "]";
			}
			
		}
		public Unit unit;
		public String title;
		public String manufacturer;
		public String pack_specification;
		public String specification;
		public CertState cert_state;
		public boolean is_charge ;
		@Override
		public String toString() {
			return "DrugList [unit=" + unit + ", title=" + title
					+ ", manufacturer=" + manufacturer
					+ ", pack_specification=" + pack_specification + "]";
		}
		
	}
	public ArrayList<DrugList> c_store_drug_info_list;
	@Override
	public String toString() {
		return "DrugstorefensDes [zcyb=" + zcyb + ", area=" + area + ", lxrsj="
				+ lxrsj + ", qyfzr=" + qyfzr + ", zcsy=" + zcsy + ", name="
				+ name + ", lxdz=" + lxdz + ", yysjd=" + yysjd + ", title="
				+ title + ", manufacturer=" + manufacturer
				+ ", pack_specification=" + pack_specification
				+ ", c_store_drug_info_list=" + c_store_drug_info_list + "]";
	}
	
}
