package com.dachen.medicine.entity;

import java.io.Serializable;

/**
 * 
 * @author WANG 即User类，待User类完成，合并
 * 
 */
public class Doctor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5298824426129302643L;
	/**
	 * 职称
	 */
	public String title;
	/**
	 * 医院名称
	 */
	public String hospital;
	/**
	 * 科室
	 */
	public String departments;
	/**
	 * 入职时间
	 */
	public String entryTime;
	/**
	 * 医生号
	 */
	public String doctorNum;

	public String introduction;

	public String skill; // 擅长

	public AuthInfo check;

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public AuthInfo getCheck() {
		return check;
	}

	public void setCheck(AuthInfo check) {
		this.check = check;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getDepartments() {
		return departments;
	}

	public void setDepartments(String departments) {
		this.departments = departments;
	}

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public String getDoctorNum() {
		return doctorNum;
	}

	public void setDoctorNum(String doctorNum) {
		this.doctorNum = doctorNum;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

}
