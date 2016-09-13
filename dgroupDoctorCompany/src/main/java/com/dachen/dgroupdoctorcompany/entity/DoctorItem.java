package com.dachen.dgroupdoctorcompany.entity;

public class DoctorItem extends BaseModel {

    private String contactWay;// ,
    private String creator;// 11807,
    private String creatorDate;// 1439709090522,
    private Doctor doctor;//
    private String doctorId;// 11857,
    private String groupId;// 55d01bdc4f13030a489463c8,
    private String id;// 55d037a24f130310784db65b,
    private String departmentId;
    private String recordMsg;// ,
    private String referenceId;// 11807,
    private String remarks;// ,
    private String status;// C,
    private String updator;// 11807,
    private String position;
    private String updatorDate;// 1439709090522
    private String main;
    private boolean isExits;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getContactWay() {
        return contactWay;
    }

    public void setContactWay(String contactWay) {
        this.contactWay = contactWay;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreatorDate() {
        return creatorDate;
    }

    public void setCreatorDate(String creatorDate) {
        this.creatorDate = creatorDate;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecordMsg() {
        return recordMsg;
    }

    public void setRecordMsg(String recordMsg) {
        this.recordMsg = recordMsg;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public String getUpdatorDate() {
        return updatorDate;
    }

    public void setUpdatorDate(String updatorDate) {
        this.updatorDate = updatorDate;
    }

    public boolean isExits() {
        return isExits;
    }

    public void setExits(boolean isExits) {
        this.isExits = isExits;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }


}