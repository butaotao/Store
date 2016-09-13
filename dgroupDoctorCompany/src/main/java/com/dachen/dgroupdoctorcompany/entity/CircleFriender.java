package com.dachen.dgroupdoctorcompany.entity;

/**
 * Created by Burt on 2016/3/2.
 */
public class CircleFriender {

    /** Not-null value. */
    private String userId;
    private String area;
    private String departments;
    private String doctorNum;
    private String creator;
    private String headPicFileName;
    private String hospital;
    private String name;
    private String sex;
    private String status;
    private String telephone;
    private String title;
    private String letter;
    private boolean exits;
    private String position;

    public CircleFriender() {
    }

    public CircleFriender(String userId) {
        this.userId = userId;
    }

    public CircleFriender(String userId, String area, String departments,
                          String doctorNum, String creator, String headPicFileName,
                          String hospital, String name, String sex, String status,
                          String telephone, String title, String letter) {
        this.userId = userId;
        this.area = area;
        this.departments = departments;
        this.doctorNum = doctorNum;
        this.creator = creator;
        this.headPicFileName = headPicFileName;
        this.hospital = hospital;
        this.name = name;
        this.sex = sex;
        this.status = status;
        this.telephone = telephone;
        this.title = title;
        this.letter = letter;
    }

    /** Not-null value. */
    public String getUserId() {
        return userId;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the
     * database.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDepartments() {
        return departments;
    }

    public void setDepartments(String departments) {
        this.departments = departments;
    }

    public String getDoctorNum() {
        return doctorNum;
    }

    public void setDoctorNum(String doctorNum) {
        this.doctorNum = doctorNum;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getHeadPicFileName() {
        return headPicFileName;
    }

    public void setHeadPicFileName(String headPicFileName) {
        this.headPicFileName = headPicFileName;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public boolean isExits() {
        return exits;
    }

    public void setExits(boolean exits) {
        this.exits = exits;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


}