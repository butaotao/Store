package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.dgroupdoctorcompany.db.dbentity.DepAdminsList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/5/31.
 */
public class Company {
    public String companyName;
    public String companyType;
    public String companyId;
    public int duty;
    public int adminType;
    public int isRepresent;
    public ArrayList<DepAdminsList> depAdminsList;
    @Override
    public String toString() {
        return "Company{" +
                "companyName='" + companyName + '\'' +
                ", companyType='" + companyType + '\'' +
                ", companyId='" + companyId + '\'' +
                ", duty=" + duty +
                ", adminType=" + adminType +
                '}';
    }
}
