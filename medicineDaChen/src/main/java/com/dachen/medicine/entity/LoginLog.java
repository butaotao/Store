package com.dachen.medicine.entity;

public class LoginLog {
	private int isFirstLogin;
	private double latitude;
	private double longitude;
	private String loginTime;
	private String offlineTime;

	public int getIsFirstLogin() {
		return isFirstLogin;
	}

	public void setIsFirstLogin(int isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public String getOfflineTime() {
		return offlineTime;
	}

	public void setOfflineTime(String offlineTime) {
		this.offlineTime = offlineTime;
	}

	@Override
	public String toString() {
		return "LoginLog [isFirstLogin=" + isFirstLogin + ", latitude="
				+ latitude + ", longitude=" + longitude + ", loginTime="
				+ loginTime + ", offlineTime=" + offlineTime + "]";
	}

}
