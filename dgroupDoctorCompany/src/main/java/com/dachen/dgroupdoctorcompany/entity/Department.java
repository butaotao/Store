package com.dachen.dgroupdoctorcompany.entity;

import java.util.List;

/**
 * Created by Burt on 2016/3/3.
 */
public class Department extends BaseModel {

    /** serialVersionUID **/
    private static final long serialVersionUID = -5289058087416134045L;

    private String creator;// 11807,
    private String creatorDate;// 1439712339246,
    private String description;// ,
    private String groupId;// 55d01bdc4f13030a489463c8,
    private String id;// 55d044534f130319ccc90109,
    private String name;// 神经科,
    private String parentId;// 0,
    private String updator;// 11807,
    private String updatorDate;// 1439712339247
    private List<DoctorItem> doctorList;
    private List<CircleDoctor> circleList;
    private List<Department> subList;
    private List<OfficeHead> headList;
    private boolean isGroupFlag;

    public List<CircleDoctor> getCircleList() {
        return circleList;
    }

    public void setCircleList(List<CircleDoctor> circleList) {
        this.circleList = circleList;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public List<DoctorItem> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(List<DoctorItem> doctorList) {
        this.doctorList = doctorList;
    }

    public List<Department> getSubList() {
        return subList;
    }

    public void setSubList(List<Department> subList) {
        this.subList = subList;
    }

    public boolean isGroupFlag() {
        return isGroupFlag;
    }

    public void setGroupFlag(boolean isGroupFlag) {
        this.isGroupFlag = isGroupFlag;
    }

    public List<OfficeHead> getHeadList() {
        return headList;
    }

    public void setHeadList(List<OfficeHead> headList) {
        this.headList = headList;
    }

}