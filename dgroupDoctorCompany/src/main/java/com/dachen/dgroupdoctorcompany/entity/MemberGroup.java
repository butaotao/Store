package com.dachen.dgroupdoctorcompany.entity;

/**
 * Created by Administrator on 2016/6/29.
 */
public class MemberGroup extends BaseModel{

    private String id;
    private String name;
    private String headPic;
    private String telephone;

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

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
