package com.dachen.dgroupdoctorcompany.entity;

import android.text.TextUtils;

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

    @DatabaseField(columnName="department")
    public String department;
//部门id
    @DatabaseField(columnName="id")
    public String id;

    @DatabaseField(columnName="name")
    public String name;
//职位
    @DatabaseField(columnName="position")
    public String position;

    @DatabaseField(columnName="telephone")
    public String telephone;

    @DatabaseField(columnName="status")
    public String status;
    //用户id
    @DatabaseField(columnName="userId")
    public String userId;
//使用这个软件的用户登录id
    @DatabaseField(columnName="userloginid")
    public String userloginid;
    //用户是否激活
    @DatabaseField(columnName="userStatus")
    public String userStatus;


    @ForeignCollectionField(eager = true)
    public Collection<Role> role;
    @DatabaseField(columnName = "headPicFileName")


    public String headPicFileName;
    public String url;
    public boolean select;
    public boolean haveSelect;
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

}
