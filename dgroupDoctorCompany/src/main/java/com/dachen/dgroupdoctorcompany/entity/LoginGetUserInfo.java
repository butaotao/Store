package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Assistant;
import com.dachen.medicine.entity.Doctor;
import com.dachen.medicine.entity.Loc;
import com.dachen.medicine.entity.LoginLog;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.entity.Settings;
import com.dachen.medicine.entity.User;

import java.util.ArrayList;

public class LoginGetUserInfo extends Result {
	public LoginData data;
	//{"data":{"companys":[{"companyId":"5745675f4f19d15769f3dc3d","adminType":2,"companyType":1,
	// "companyName":"测试药企0012","duty":1}],"logo":"http://default.dev.file.dachentech.com.cn/user/default.jpg",
	// "telephone":"12100000328","userType":10,"userName":"测试","userId":144703},"resultCode":1}


//{"data":{"companys":[{"depAdminsList":[],"companyId":"575f9e4da3758d2274ca45cf","companyType":1,"companyName":
// "生产药企产品测试","duty":1}],
// "logo":"http://default.dev.file.dachent
// ech.com.cn/user/default.jpg","telephone":"18532345645","userType":10,
// "userName":"GCTEST","userId":145162},"resultCode":1}
	public class LoginData {

		public String userId;
		public int userType;
		public String telephone;
		public int age;
		public int sex;
		public int status;
		public String logo;
		public String userName;
		public String createTime;

		private int companyId;// 公司Id
		private Cv cv;

		private Login login;
		public User user;
		public Settings settings;
		public Doctor doctor;
		public Assistant assistant;
		public Loc loc;
		public LoginLog loginLog;
		public String headPic;
		public ArrayList<Company> companys;
		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public int getUserType() {
			return userType;
		}

		public void setUserType(int userType) {
			this.userType = userType;
		}

		public String getTelephone() {
			return telephone;
		}

		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public int getSex() {
			return sex;
		}

		public void setSex(int sex) {
			this.sex = sex;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public LoginLog getLoginLog() {
			return loginLog;
		}

		public void setLoginLog(LoginLog loginLog) {
			this.loginLog = loginLog;
		}



		public Settings getSettings() {
			return settings;
		}

		public void setSettings(Settings settings) {
			this.settings = settings;
		}

		public Doctor getDoctor() {
			return doctor;
		}

		public void setDoctor(Doctor doctor) {
			this.doctor = doctor;
		}

		public Assistant getAssistant() {
			return assistant;
		}

		public void setAssistant(Assistant assistant) {
			this.assistant = assistant;
		}

		public Loc getLoc() {
			return loc;
		}

		public void setLoc(Loc loc) {
			this.loc = loc;
		}



		public Cv getCv() {
			return cv;
		}

		public void setCv(Cv cv) {
			this.cv = cv;
		}



		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}



		public Login getLogin() {
			return login;
		}

		public void setLogin(Login login) {
			this.login = login;
		}

		public int getCompanyId() {
			return companyId;
		}

		public void setCompanyId(int companyId) {
			this.companyId = companyId;
		}

		public class Cv {
			private String resumeId;
			private String resumeName;

			public String getResumeId() {
				return resumeId;
			}

			public void setResumeId(String resumeId) {
				this.resumeId = resumeId;
			}

			public String getResumeName() {
				return resumeName;
			}

			public void setResumeName(String resumeName) {
				this.resumeName = resumeName;
			}
		}

		public class Login {
			private String userId;
			private int isFirstLogin;
			private long loginTime;
			private long offlineTime;
			private String apiVersion;
			private String model;
			private String osVersion;
			private String serial;
			private String latitude;
			private String longitude;
			private String location;

			public String getUserId() {
				return userId;
			}

			public void setUserId(String userId) {
				this.userId = userId;
			}

			public int getIsFirstLogin() {
				return isFirstLogin;
			}

			public void setIsFirstLogin(int isFirstLogin) {
				this.isFirstLogin = isFirstLogin;
			}

			public long getLoginTime() {
				return loginTime;
			}

			public void setLoginTime(long loginTime) {
				this.loginTime = loginTime;
			}

			public String getApiVersion() {
				return apiVersion;
			}

			public void setApiVersion(String apiVersion) {
				this.apiVersion = apiVersion;
			}

			public String getModel() {
				return model;
			}

			public void setModel(String model) {
				this.model = model;
			}

			public String getOsVersion() {
				return osVersion;
			}

			public void setOsVersion(String osVersion) {
				this.osVersion = osVersion;
			}

			public String getSerial() {
				return serial;
			}

			public void setSerial(String serial) {
				this.serial = serial;
			}

			public String getLatitude() {
				return latitude;
			}

			public void setLatitude(String latitude) {
				this.latitude = latitude;
			}

			public String getLongitude() {
				return longitude;
			}

			public void setLongitude(String longitude) {
				this.longitude = longitude;
			}

			public String getLocation() {
				return location;
			}

			public void setLocation(String location) {
				this.location = location;
			}

			public long getOfflineTime() {
				return offlineTime;
			}

			public void setOfflineTime(long offlineTime) {
				this.offlineTime = offlineTime;
			}

		}


	}

	@Override
	public String toString() {
		return "LoginGetUserInfo{" +
				"data=" + data +
				'}';
	}
}
