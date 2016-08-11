package com.dachen.dgroupdoctorcompany.db.dbentity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Burt on 2016/8/10.
 */
@DatabaseTable(tableName = "table_oftensinplace")
public class OftenSinPlace {
    @DatabaseField(generatedId=true)
    public int _id;
    @DatabaseField(columnName = "coordinate")
    public String coordinate;
    @DatabaseField(columnName = "drugCompanyId")
    public String drugCompanyId;
    @DatabaseField(columnName = "id")
    public String id;
    @DatabaseField(columnName = "simpleAddress")
    public String simpleAddress;
    @DatabaseField(columnName = "type")
    public String type;
    @DatabaseField(columnName = "status")
    public int status;
    @DatabaseField(columnName = "userloginid")
    public String userloginid;
}
