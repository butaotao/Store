package com.dachen.dgroupdoctorcompany.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/6/29.
 */
public class VisitMember extends BaseModel{

    private String id;
    private String userId;
    private String name;
    private String companyId;
    private String orgId;
    private String doctorId;
    private String doctorName;
    private long time;
    private String remark;
    private String coordinate;
    private String addressName;
    private String address;
    private String type;
    private String state;
    private String deviceId;
    private String headPic;
    private String userIds;
    private String headPicList;
    private String summaryList;
    private String singedTag;
    private List<GoodsGroupsModel> goodsGroups;
    private List<String> imgUrls;
    private List<PersonModel> groupDetails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }



    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public String getHeadPicList() {
        return headPicList;
    }

    public void setHeadPicList(String headPicList) {
        this.headPicList = headPicList;
    }

    public String getSummaryList() {
        return summaryList;
    }

    public void setSummaryList(String summaryList) {
        this.summaryList = summaryList;
    }

    public String getSingedTag() {
        return singedTag;
    }

    public void setSingedTag(String singedTag) {
        this.singedTag = singedTag;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public List<GoodsGroupsModel> getGoodsGroups() {
        return goodsGroups;
    }

    public void setGoodsGroups(List<GoodsGroupsModel> goodsGroups) {
        this.goodsGroups = goodsGroups;
    }

    public List<PersonModel> getGroupDetails() {
        return groupDetails;
    }

    public void setGroupDetails(List<PersonModel> groupDetails) {
        this.groupDetails = groupDetails;
    }
}
