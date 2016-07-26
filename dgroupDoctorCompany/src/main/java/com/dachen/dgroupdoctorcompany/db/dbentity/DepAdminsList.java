package com.dachen.dgroupdoctorcompany.db.dbentity;

import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Burt on 2016/6/14.
 */
@DatabaseTable(tableName = "tb_depadminslist")
public class DepAdminsList  extends BaseSearch {
    @DatabaseField(generatedId=true)
    public int _id;

    @DatabaseField(columnName = "orgName")
    public String orgName;
    @DatabaseField(columnName = "orgId")
    public String orgId;
    @DatabaseField(columnName = "loginUserId")
    public String loginUserId;
    @DatabaseField (columnName = "orgDuty")
    public String orgDuty;
    @DatabaseField (columnName = "pid")
    public String pid;
    @DatabaseField (columnName = "cid")
    public String cid;
    @DatabaseField (columnName = "recorder")
    public String recorder;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DepAdminsList that = (DepAdminsList) o;
            if (orgId==null||that.orgId == null){
                return  false;
            }
        if (orgId.equals(that.orgId)){
            return true;
        }
        return false;

    }

}
