package com.dachen.medicine.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 用户
 * 
 * @author WANG
 * 
 */
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 111L;
	public String headUrl;

	public String getEnterpriseId() {
		return enterpriseId;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String enterpriseId;
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String token;
	public EnterpriseUser enterpriseUser;
	public class EnterpriseUser{
		public ArrayList<Role> role;
		public class Role{
			public String name;
			public String id;
		}
	}
	public String getBooleanOpen() {
		return booleanOpen;
	}

	public void setBooleanOpen(String booleanOpen) {
		this.booleanOpen = booleanOpen;
	}

	public String booleanOpen = "0";

	public String enterpriseName;

	/**
	 * 用户Id
	 */
	public String userId;
	/**
	 * 用户类型
	 */
	public int userType;
	/**
	 * 手机号
	 */

	public String telephone;
	/**
	 * 用户姓名
	 */

	public String name;
	/**
	 * 性别
	 */

	public int sex;
	/**
	 * 用户签名信息
	 */

	public String description;

	public String password;

	public String birthday;

	public String companyId;

	public String email;

	public int status;

	public String nickname;

	public int isAuth;

	public int age;

	public String headPicFileName;

	public String area; // 所在地

	// TODO 把信息全放在User表
	public LoginLog loginLog;
	public Settings settings;
	public Doctor doctor;
	public Assistant assistant;
	public Loc loc;

	/**
	 * 医生信息
	 */
	// 职称

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

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getHeadPicFileName() {
		return headPicFileName;
	}

	public void setHeadPicFileName(String headPicFileName) {
		this.headPicFileName = headPicFileName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserId() {
		return userId;
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

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getIsAuth() {
		return isAuth;
	}

	public void setIsAuth(int isAuth) {
		this.isAuth = isAuth;
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

	public Loc getLoc() {
		return loc;
	}

	public void setLoc(Loc loc) {
		this.loc = loc;
	}

	@Override
	public String toString() {
		return "User [age=" + age + ", doctor=" + doctor + ", isAuth=" + isAuth
				+ ", loc=" + loc + ", loginLog=" + loginLog + ", name=" + name
				+ ", nickname=" + nickname + ", settings=" + settings
				+ ", status=" + status + ", telephone=" + telephone
				+ ", userId=" + userId + ", userType=" + userType + "]";
	}

}
