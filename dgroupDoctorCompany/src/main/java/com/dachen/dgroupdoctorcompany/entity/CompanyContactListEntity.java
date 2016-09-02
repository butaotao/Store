package com.dachen.dgroupdoctorcompany.entity;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by Burt on 2016/2/26.
 */
@DatabaseTable(tableName = "tb_companycontact")
public class CompanyContactListEntity extends BaseSearch implements Serializable{

    @DatabaseField(generatedId=true)
    public int _id;
    @Expose
    @SerializedName("orgName")
    @DatabaseField(columnName="department")
    public String department;
//部门id orgId
    @Expose
    @SerializedName("orgId")
    @DatabaseField(columnName="id")
    public String id;

    @DatabaseField(columnName="name")
    public String name;
    //职位
    @Expose
    @SerializedName("title")
    @DatabaseField(columnName="position")
    public String position;

    @DatabaseField(columnName="telephone")
    public String telephone;

    @DatabaseField(columnName="status")
    public String status;
    @DatabaseField(columnName="deptManager")
    public int deptManager;
    @DatabaseField(columnName="hidePhone")
    public int hidePhone;
    @DatabaseField(columnName="jobType")
    public int jobType;
    @DatabaseField(columnName="rootManager")
    public int rootManager;
    @DatabaseField(columnName="treePath")
    public String treePath;
    @Expose
    @SerializedName("drugCompanyId")
    @DatabaseField(columnName="drugCompanyId")
    public String drugCompanyId;
    //用户id
    @DatabaseField(columnName="employeeId")
    public String employeeId;
    @DatabaseField(columnName="userId")
    public String userId;
    @DatabaseField(columnName="openId")
    public String openId;
//使用这个软件的用户登录id
    @DatabaseField(columnName="userloginid")
    public String userloginid;
    @DatabaseField(columnName="sysManager")
    public String sysManager;
    //用户是否激活
    @DatabaseField(columnName="ismanager")
    public String ismanager;

    @DatabaseField(columnName="ispresent")
    public int ispresent;
    @Expose
    @SerializedName("pinYin")
    @DatabaseField(columnName="simpinyin")
    public String simpinyin;
    @DatabaseField(columnName="simpinyinnotnum")
    public String simpinyinnotnum;
    @Expose
    @SerializedName("fullPinYin")
    @DatabaseField(columnName="allpinyin")
    public String allpinyin;
    @Expose
    @SerializedName("active")
    @DatabaseField(columnName="userStatus")
    public String userStatus;
    @ForeignCollectionField(eager = true)
    public Collection<Role> role;
    @DatabaseField(columnName = "headPicFileName")


    @Expose
    @SerializedName("headPicUrl")
    public String headPicFileName;
    public BizRoleConfig bizRoleConfig;
    public boolean select;
    public boolean haveSelect;
    public class BizRoleConfig implements Serializable{
        public int drugSales;
        public int marketing;
    }
    @Override
    public boolean equals(Object o) {
        boolean flag = false;
        if (o instanceof  CompanyContactListEntity){
            CompanyContactListEntity  other = (CompanyContactListEntity)o;
            if (!TextUtils.isEmpty(this.userId)&&!TextUtils.isEmpty(other.userId)){
                flag = other.userId.equals(this.userId);
            }else {
                flag=  false;
            }
        }

        return flag;
    }

    @Override
    public int hashCode() {
        return this.userId.hashCode();
    }
    /* @Override
    public int compareTo(Object o) {
        int flag = 0;
        if (o instanceof  CompanyContactListEntity){
            CompanyContactListEntity  other = (CompanyContactListEntity)o;
            if (!TextUtils.isEmpty(this.userId)&&!TextUtils.isEmpty(other.userId)){
                if (other.userId.equals(this.userId)){
                    flag = 0;
                }
            }else {
                flag=  1;
            }
        }

        return flag;
    }*/
}
