package com.dachen.medicine.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 实体类 医助
 * 
 * @author WANG
 * 
 */
public class Assistant implements Parcelable {

	/**
	 * 用户Id
	 */
	public String userId;
	/**
	 * 所属公司
	 */
	private String company;
	/**
	 * 区域
	 */
	private String area;
	/**
	 * 部门
	 */
	private String department;
	/**
	 * 岗位
	 */
	private String position;

	private String name;
	private String avatarUrl;
	private String latestMsg;
	private String time;

	private String number;
	private String onduty;

	public Assistant(Parcel in) {
		company = in.readString();
		area = in.readString();
		department = in.readString();
		position = in.readString();
	}

	public Assistant() {

	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getOnduty() {
		return onduty;
	}

	public void setOnduty(String onduty) {
		this.onduty = onduty;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getLatestMsg() {
		return latestMsg;
	}

	public void setLatestMsg(String latestMsg) {
		this.latestMsg = latestMsg;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(company);
		dest.writeString(area);
		dest.writeString(department);
		dest.writeString(position);

	}

	public static final Parcelable.Creator<Assistant> CREATOR = new Creator<Assistant>() {

		@Override
		public Assistant[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Assistant[size];
		}

		@Override
		public Assistant createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Assistant(source);
		}
	};

}
