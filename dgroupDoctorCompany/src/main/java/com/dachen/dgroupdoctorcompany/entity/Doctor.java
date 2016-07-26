package com.dachen.dgroupdoctorcompany.entity;

import java.util.List;

/**
 * Created by Burt on 2016/3/3.
 */
public class Doctor extends BaseModel {

    private String name;
    private String departments;// ,
    private String doctorId;// 539,
    private String doctorNum;// 380,
    private String headPicFileName;// ,
    private String headPicFilePath;
    private String hospital;// ,
    private String introduction;// ,
    private String position;// ,
    private String skill;// ,
    private String telephone;// 10039,
    private String userType;// 3
    private List<DiseaseType> expertise;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartments() {
        return departments;
    }

    public void setDepartments(String departments) {
        this.departments = departments;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorNum() {
        return doctorNum;
    }

    public void setDoctorNum(String doctorNum) {
        this.doctorNum = doctorNum;
    }

    public String getHeadPicFileName() {
        return headPicFileName;
    }

    public void setHeadPicFileName(String headPicFileName) {
        this.headPicFileName = headPicFileName;
    }

    public String getHeadPicFilePath() {
        return headPicFilePath;
    }

    public void setHeadPicFilePath(String headPicFilePath) {
        this.headPicFilePath = headPicFilePath;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<DiseaseType> getExpertise() {
        return expertise;
    }

    public void setExpertise(List<DiseaseType> expertise) {
        this.expertise = expertise;
    }

}
