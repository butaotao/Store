package com.dachen.medicine.bean;

import java.io.Serializable;

public class QRCodeScanner2Bean implements Serializable{
	@Override
	public String toString() {
		return "QRCodeScanner2Bean [detailMsg=" + detailMsg + ", resultCode="
				+ resultCode + ", resultMsg=" + resultMsg + ", num=" + num
				+ ", name=" + name + ", factory=" + factory + ", scanCode="
				+ scanCode + ", isAdd=" + isAdd + ", id=" + id + ", unit="
				+ unit + ", pack_specification=" + pack_specification
				+ ", trade_name=" + trade_name + "]";
	}
	public String detailMsg;
	public int resultCode;
	public String resultMsg;
	public int num;
	public String name;
	public String factory;
	public String scanCode;
	public int isAdd;
	public String id;
	public String unit;
	public String pack_specification;
	public String trade_name;
}
