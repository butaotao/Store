package com.dachen.medicine.entity;

import java.io.Serializable;

public class Settings implements Serializable {

	private static final long serialVersionUID = 11113L;
	private int allowAtt;
	private int allowGreet;
	private int doctorVerify;
	private int friendsVerify;
	private int ispushflag;
	private int needAssistant;
	private int patientVerify;

	public int getAllowAtt() {
		return allowAtt;
	}

	public void setAllowAtt(int allowAtt) {
		this.allowAtt = allowAtt;
	}

	public int getAllowGreet() {
		return allowGreet;
	}

	public void setAllowGreet(int allowGreet) {
		this.allowGreet = allowGreet;
	}

	public int getDoctorVerify() {
		return doctorVerify;
	}

	public void setDoctorVerify(int doctorVerify) {
		this.doctorVerify = doctorVerify;
	}

	public int getFriendsVerify() {
		return friendsVerify;
	}

	public void setFriendsVerify(int friendsVerify) {
		this.friendsVerify = friendsVerify;
	}

	public int getIspushflag() {
		return ispushflag;
	}

	public void setIspushflag(int ispushflag) {
		this.ispushflag = ispushflag;
	}

	public int getNeedAssistant() {
		return needAssistant;
	}

	public void setNeedAssistant(int needAssistant) {
		this.needAssistant = needAssistant;
	}

	public int getPatientVerify() {
		return patientVerify;
	}

	public void setPatientVerify(int patientVerify) {
		this.patientVerify = patientVerify;
	}

	@Override
	public String toString() {
		return "Settings [allowAtt=" + allowAtt + ", allowGreet=" + allowGreet
				+ ", doctorVerify=" + doctorVerify + ", friendsVerify="
				+ friendsVerify + ", ispushflag=" + ispushflag
				+ ", needAssistant=" + needAssistant + ", patientVerify="
				+ patientVerify + "]";
	}

}
