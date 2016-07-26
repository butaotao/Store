package com.dachen.medicine.volley.custom;

import com.dachen.medicine.entity.Result;

public class ObjectResult<T> extends Result {
	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ObjectResult [data=" + data + "]";
	}

}
