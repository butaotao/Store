package com.dachen.dgroupdoctorcompany.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Burt on 2016/3/1.
 */
@DatabaseTable(tableName = "tb_role")
public class Role  extends CompanyContactBase implements Serializable{
        @DatabaseField(generatedId = true)
        public int id;
        @DatabaseField(columnName="key")
        public String key;
        @DatabaseField(columnName="name")
        public String name;
        @DatabaseField(columnName="userloginid")
        public String userloginid;
        @DatabaseField(canBeNull = true, foreign = true, columnName = "companycontact_id")
        public CompanyContactListEntity companycontact;
}
