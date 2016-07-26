package com.dachen.medicine.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class MedicineEntity extends Result implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	public String url;
	public String height;
	public String company;
	public int num;
	public String unit;
	public String total;
	public String sub_title;
	public int page_count;
	public Params params;
	public MedicineInfo[] info_list;
	public ArrayList<MedicineInfo> list_datas;
	public Long time;

	public void setTime(Long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "MedicineEntity [name=" + name + ", url=" + url + ", height="
				+ height + ", company=" + company + ", num=" + num + ", unit="
				+ unit + ", total=" + total + ", sub_title=" + sub_title
				+ ", page_count=" + page_count + ", params=" + params
				+ ", lifoList=" + Arrays.toString(info_list) + ", time=" + time
				+ "]";
	}

	public class MedicineInfo {

		public String id;
		public String _type;
		public String goods$image;
		public String goods$manufacturer;
		public String goods$specification;
		public GoodType goods$type;
		public Goods goods;
		public String goods$trade_name;
		public String goods$image_url;
		public String goods$number;
		public String goods$general_name;
		public String start_date;
		public String end_date;
		public String dptgf;
		public String goods$pack_specification;
		public class GoodType {
			public String value;
			public String title;

			@Override
			public String toString() {
				return "GoodType [value=" + value + ", title=" + title + "]";
			}
		}

		public class Goods {
			public String id;
			public String _type;
			public String title;

			@Override
			public String toString() {
				return "Goods [id=" + id + ", _type=" + _type + ", title="
						+ title + "]";
			}

		}

		@Override
		public String toString() {
			return "MedicineInfo [id=" + id + ", _type=" + _type
					+ ", goods$image=" + goods$image + ", goods$manufacturer="
					+ goods$manufacturer + ", goods$specification="
					+ goods$specification + ", goods$type=" + goods$type
					+ ", goods=" + goods + ", goods$trade_name="
					+ goods$trade_name + ", goods$image_url=" + goods$image_url
					+ ", goods$number=" + goods$number
					+ ", goods$general_name=" + goods$general_name + "]";
		}

	}

	public class Params {
		public String category;

		@Override
		public String toString() {
			return "Params [category=" + category + "]";
		}

	}

	public Long getTime() {
		// TODO Auto-generated method stub
		return time;

	}
}
