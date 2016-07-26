package com.dachen.medicine.entity;

public class ResetPassword extends Result {
	public Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public class Data {

		public String smsid;

		@Override
		public String toString() {
			return "ResetPassword [smsid=" + smsid + "]";
		}
	}

	@Override
	public String toString() {
		return "ResetPassword [data=" + data + "]";
	}

}
