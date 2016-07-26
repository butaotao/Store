package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.dgroupdoctorcompany.db.dbentity.*;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/3/4.
 */
@DatabaseTable(tableName = "tb_hospital")
public class HospitalDes extends BaseSearch {
    @DatabaseField(generatedId=true)
    public int _id;
    @DatabaseField(columnName="city")
    public String city;
    @DatabaseField(columnName="country")
    public String country;
    @DatabaseField(columnName="name")
    public String name;
    @DatabaseField(columnName="province")
    public int province;
    public String level;
    public String id;
    public ArrayList<com.dachen.dgroupdoctorcompany.db.dbentity.Doctor> doctors;
}
