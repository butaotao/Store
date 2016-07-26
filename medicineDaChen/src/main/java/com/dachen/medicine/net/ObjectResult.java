package com.dachen.medicine.net;

import com.dachen.medicine.entity.Result;

public class ObjectResult<T> extends Result {
	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
