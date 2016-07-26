package com.dachen.dgroupdoctorcompany.entity;

/**
 * Created by Burt on 2016/5/16.
 */

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * @author WANG 2015-7-7
 */

@DatabaseTable(tableName = "user")
public class User implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 5026102817771405512L;

    /**
     * 用户Id
     */
    @DatabaseField(id = true)
    public String userId;
    /**
     * 用户类型
     */
    @DatabaseField
    public int userType;
    /**
     * 手机号
     */
    @DatabaseField
    public String telephone;
    /**
     * 用户姓名
     */
    @DatabaseField
    public String name;
    /**
     * 性别
     */
    @DatabaseField
    public int sex;
    /**
     * 用户签名信息
     */
    @DatabaseField
    public String description;
    @DatabaseField
    public String password;
    @DatabaseField
    public String birthday;
    @DatabaseField
    public String companyId;
    @DatabaseField
    public String email;
    @DatabaseField
    public int status;
    @DatabaseField
    public String nickname;
    @DatabaseField
    public int isAuth;
    @DatabaseField
    public int age;
    @DatabaseField
    public String headPicFileName;
    @DatabaseField
    public String area; // 所在地

    //TODO 把信息全放在User表
    public Doctor doctor;
    private HospitalList[] hospitalList;
    public int hospitalStatus; //1已加入医院，2未加入医院，3已离职

    /**
     * 认证信息
     */
    @DatabaseField
    public String hospitalId;
    /**
     * 科室id
     */
    @DatabaseField
    public String deptId;

    /**
     * 医生信息
     */
    //职称
    @DatabaseField
    public String title;
    /**
     * 医院名称
     */
    @DatabaseField
    public String hospital;
    /**
     * 科室
     */
    @DatabaseField
    public String departments;
    /**
     * 入职时间
     */
    @DatabaseField
    public String entryTime;
    /**
     * 医生号
     */
    @DatabaseField
    public String doctorNum;
    @DatabaseField
    public String introduction;
    @DatabaseField
    public String skill; // 擅长

    /**
     * 医生集团信息
     */
    @DatabaseField
    public String groupListName; //集团名
    @DatabaseField
    public String departmentListName; //部门名
    @DatabaseField
    public String hospitalName; //认证的医院名

    public int patientNum; //患者数

    public UserConfig userConfig; //提示音

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public HospitalList[] getHospitalList() {
        return hospitalList;
    }

    public void setHospitalList(HospitalList[] hospitalList) {
        this.hospitalList = hospitalList;
    }

    public int getHospitalStatus() {
        return hospitalStatus;
    }

    public void setHospitalStatus(int hospitalStatus) {
        this.hospitalStatus = hospitalStatus;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public UserConfig getUserConfig() {
        return userConfig;
    }

    public void setUserConfig(UserConfig userConfig) {
        this.userConfig = userConfig;
    }


    public int getPatientNum() {
        return patientNum;
    }

    public void setPatientNum(int patientNum) {
        this.patientNum = patientNum;
    }

    public String getGroupListName() {
        return groupListName;
    }

    public void setGroupListName(String groupListName) {
        this.groupListName = groupListName;
    }

    public String getDepartmentListName() {
        return departmentListName;
    }

    public void setDepartmentListName(String departmentListName) {
        this.departmentListName = departmentListName;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
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

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
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



    public class UserConfig implements Serializable {
        private static final long serialVersionUID = 566664487545L;
        private String remindVoice;

        public String getRemindVoice() {
            return remindVoice;
        }

        public void setRemindVoice(String remindVoice) {
            this.remindVoice = remindVoice;
        }
    }
}
