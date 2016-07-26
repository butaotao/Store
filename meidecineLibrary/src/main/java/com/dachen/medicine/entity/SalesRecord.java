package com.dachen.medicine.entity;

import java.util.List;

public class SalesRecord extends Result {
	public int page_count;
	public int total;
	public String sub_title;
	public List<Info> info_list;

	@Override
	public String toString() {
		return "SalesRecord [page_count=" + page_count + ", total=" + total
				+ ", info_list=" + info_list + "]";
	}

}
