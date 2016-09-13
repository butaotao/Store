package com.dachen.dgroupdoctorcompany.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Burt on 2016/3/1.
 */
public class CompanyContactEntity extends BaseSearch{
    public String id;
    public String department;
    public String companyid;
    public String name;
    public String position;
    public String telephone;
    public String status;
    public String userId;
    public String userloginid;
    public ArrayList<Role> role ;
}
