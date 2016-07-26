package com.dachen.medicine.entity;

public class LoginRegisterResult extends Result {
	public LoginData data;

	public class LoginData {
		private String _tk;// 登录xmpp的凭证
		public String access_token;
		private int expires_in;// 多少秒后过时
		private String name;// 姓名
		private String nickname;// 昵称
		private String userId;
		public int userType;
		public String telephone;
		public int age;
		public int sex;
		public int status;
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
		public EnterpriseUser enterpriseUser;
		public class EnterpriseUser{
		public String companyName;
		}
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

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
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

		public String get_tk() {
			return _tk;
		}

		public void set_tk(String _tk) {
			this._tk = _tk;
		}

		public Cv getCv() {
			return cv;
		}

		public void setCv(Cv cv) {
			this.cv = cv;
		}

		public String getAccess_token() {
			return access_token;
		}

		public void setAccess_token(String access_token) {
			this.access_token = access_token;
		}

		public int getExpires_in() {
			return expires_in;
		}

		public void setExpires_in(int expires_in) {
			this.expires_in = expires_in;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getNickName() {
			return nickname;
		}

		public void setNickName(String nickname) {
			this.nickname = nickname;
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

		@Override
		public String toString() {
			return "LoginRegisterResult [_tk=" + _tk + ", access_token="
					+ access_token + ", expires_in=" + expires_in + ", name="
					+ name + ", nickname=" + nickname + ", userId=" + userId
					+ ", userType=" + userType + ", telephone=" + telephone
					+ ", age=" + age + ", sex=" + sex + ", status=" + status
					+ ", createTime=" + createTime + ", companyId=" + companyId
					+ ", cv=" + cv + ", login=" + login + ", user=" + user
					+ ", settings=" + settings + ", doctor=" + doctor
					+ ", assistant=" + assistant + ", loc=" + loc
					+ ", loginLog=" + loginLog + "]";
		}
	}

	@Override
	public String toString() {
		return "LoginRegisterResult [data=" + data + "]";
	}

}
