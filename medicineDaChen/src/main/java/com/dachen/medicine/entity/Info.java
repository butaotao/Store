package com.dachen.medicine.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Info {
	public boolean isMon = true;

	public boolean isMon() {
		return isMon;
	}

	public void setMon(boolean isMon) {
		this.isMon = isMon;
	}

	public String id;
	public String _type;
	public int quantity;
	public long datetime;
	public String saleMonth;
	public String drug$pack_specification;
	public SaleSman salesman;
	public DrugFactory drug_factory;
	public DrugUnit drug$unit;
	public DrugStore drug_store;
	public Drug drug;
	public String drug$trade_name;
	public String drug$general_name;
	public String[] calendar;
	public State state;
	@Expose
	@SerializedName("buyType")
	public int type;
    public class State{
    	public String value;
    	public String title;
    }
	public class SaleSman {
		public String id;
		public String _type;
		public String title;
		public String name;

		@Override
		public String toString() {
			return "SaleSman [id=" + id + ", _type=" + _type + ", title="
					+ title + ", name=" + name + "]";
		}

	}

	public class DrugFactory {
		public String id;
		public String _type;
		public String title;
		public String name;

		@Override
		public String toString() {
			return "DrugFactory [id=" + id + ", _type=" + _type + ", title="
					+ title + ", name=" + name + "]";
		}

	}

	public class DrugUnit {
		public String id;
		public String _type;
		public String title;
		public String name;

		@Override
		public String toString() {
			return "DrugUnit [id=" + id + ", _type=" + _type + ", title="
					+ title + ", name=" + name + "]";
		}

	}

	public class Drug {
		public String id;
		public String _type;
		public String title;

		@Override
		public String toString() {
			return "Drug [id=" + id + ", _type=" + _type + ", title=" + title
					+ "]";
		}

	}

	public class DrugStore {
		public String id;
		public String _type;
		public String title;
		public String name;

		@Override
		public String toString() {
			return "DrugStore [id=" + id + ", _type=" + _type + ", title="
					+ title + ", name=" + name + "]";
		}

	}

	@Override
	public String toString() {
		return "Info [isMon=" + isMon + ", id=" + id + ", _type=" + _type
				+ ", quantity=" + quantity + ", datetime=" + datetime
				+ ", drug$pack_specification=" + drug$pack_specification
				+ ", salesman=" + salesman + ", drug_factory=" + drug_factory
				+ ", drug$unit=" + drug$unit + ", drug_store=" + drug_store
				+ ", drug=" + drug + ", calendar=" + Arrays.toString(calendar)
				+ "]";
	}

}