package com.dachen.medicine.entity;

import java.io.Serializable;

/**
 * 认证资料
 * 
 * @author lmc
 * 
 */
public class AuthInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4852064653850524978L;

	public String name;

	public int status;

	public String hospital;
	public String departments;
	public String title;

	public String checker;
	public long checkTime;
	public String checkerId;
	public String hospitalId;
	public String licenseExpire;
	public String licenseNum;
	public String remark; // 认证失败原因
}
