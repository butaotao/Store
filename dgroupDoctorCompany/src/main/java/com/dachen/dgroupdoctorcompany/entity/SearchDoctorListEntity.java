package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.dgroupdoctorcompany.db.dbentity.*;
import com.dachen.medicine.entity.Result;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Burt on 2016/3/5.
 */
public class SearchDoctorListEntity extends Result{
    public ArrayList<SearchDoctorInfo> data;
    public class SearchDoctorInfo implements Serializable{
        public String departments;
        public ArrayList<com.dachen.dgroupdoctorcompany.db.dbentity.Doctor> enterpriseUserList;
    }
}
