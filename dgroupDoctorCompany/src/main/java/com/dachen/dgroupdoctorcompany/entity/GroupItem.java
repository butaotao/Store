package com.dachen.dgroupdoctorcompany.entity;

/**
 * Created by Burt on 2016/3/3.
 */

import java.util.List;

/**
 * [A brief description]
 *
 * @author huxinwu
 * @version 1.0
 * @date 2015-8-18
 *
 **/
public class GroupItem extends BaseModel {

    private String groupIconPath;
    private String companyId;// 55d019de4f130318e4db7a3e,
    private String creator;// 11807,
    private String creatorDate;// 1439701980369,
    private String id;// 55d01bdc4f13030a489463c8,
    private String introduction;// 111111111111,
    private String name;// 张强医生集团,
    private String updator;// 11807,
    private String certStatus;
    private String updatorDate;// 1439701980370
    private List<Department> departmentList;

    public String getGroupIconPath() {
        return groupIconPath;
    }

    public void setGroupIconPath(String groupIconPath) {
        this.groupIconPath = groupIconPath;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    public String getCertStatus() {
        return certStatus;
    }

    public void setCertStatus(String certStatus) {
        this.certStatus = certStatus;
    }

    @Override
    public String toString() {
        return "GroupItem [companyId=" + companyId + ", creator=" + creator
                + ", creatorDate=" + creatorDate + ", id=" + id
                + ", introduction=" + introduction + ", name=" + name
                + ", updator=" + updator + ", certStatus=" + certStatus
                + ", updatorDate=" + updatorDate + ", departmentList="
                + departmentList + "]";
    }


}