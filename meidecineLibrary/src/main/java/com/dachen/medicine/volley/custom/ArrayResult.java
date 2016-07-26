package com.dachen.medicine.volley.custom;

import java.util.List;

import com.dachen.medicine.entity.Result;

public class ArrayResult<T> extends Result {
	private List<T> data;

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

}
