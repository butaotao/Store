package com.dachen.dgroupdoctorcompany.entity;

/**
 * Created by Burt on 2016/3/3.
 */
public class CircleDoctor {

    /** Not-null value. */
    private String id;
    private String contactWay;
    private String departmentId;
    private String groupId;
    private String main;
    private String referenceId;
    private String status;
    private String departments;
    private String doctorId;
    private String doctorNum;
    private String headPicFilePath;
    private String hospital;
    private String introduction;
    private String name;
    private String position;
    private String skill;
    private String telephone;
    private String userType;

    public CircleDoctor() {
    }

    public CircleDoctor(String id) {
        this.id = id;
    }

    public CircleDoctor(String id, String contactWay, String departmentId, String groupId, String main, String referenceId, String status, String departments, String doctorId, String doctorNum, String headPicFilePath, String hospital, String introduction, String name, String position, String skill, String telephone, String userType) {
        this.id = id;
        this.contactWay = contactWay;
        this.departmentId = departmentId;
        this.groupId = groupId;
        this.main = main;
        this.referenceId = referenceId;
        this.status = status;
        this.departments = departments;
        this.doctorId = doctorId;
        this.doctorNum = doctorNum;
        this.headPicFilePath = headPicFilePath;
        this.hospital = hospital;
        this.introduction = introduction;
        this.name = name;
        this.position = position;
        this.skill = skill;
        this.telephone = telephone;
        this.userType = userType;
    }

    /** Not-null value. */
    public String getId() {
        return id;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setId(String id) {
        this.id = id;
    }

    public String getContactWay() {
        return contactWay;
    }

    public void setContactWay(String contactWay) {
        this.contactWay = contactWay;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    private boolean exits;

    public boolean isExits() {
        return exits;
    }

    public void setExits(boolean exits) {
        this.exits = exits;
    }


}
