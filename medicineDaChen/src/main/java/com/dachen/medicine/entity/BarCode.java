package com.dachen.medicine.entity;

public class BarCode extends Result{
	public String id = "";
	public String isused;
	public String is_exists;
	public String manufacturer;
	public String general_name;
	public String trade_name;
	public String pack_specification;
	public Unit unit;
	public class Unit{
		public String id;
		public String _type;
		public String title;
		public String name;
		@Override
		public String toString() {
			return "Unit [id=" + id + ", _type=" + _type + ", title=" + title
					+ ", name=" + name + "]";
		}
		
	}
	@Override
	public String toString() {
		return "BarCode [id=" + id + ", isused=" + isused + ", is_exists="
				+ is_exists + ", manufacturer=" + manufacturer
				+ ", general_name=" + general_name + ", trade_name="
				+ trade_name + ", pack_specification=" + pack_specification
				+ ", unit=" + unit + "]";
	}
	
}
