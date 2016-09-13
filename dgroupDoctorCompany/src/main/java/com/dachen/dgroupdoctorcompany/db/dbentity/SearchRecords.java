package com.dachen.dgroupdoctorcompany.db.dbentity;

import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Burt on 2016/3/2.
 */
@DatabaseTable(tableName = "tb_searchrecords")
public class SearchRecords extends BaseSearch {
    @DatabaseField(generatedId=true)
    public int _id;

    @DatabaseField(columnName="searchresult")
    public String searchresult;

    @DatabaseField(columnName="userloginid")
    public String userloginid;

    @DatabaseField(columnName="serchtype")
    public String serchtype;
}
