package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.dgroupdoctorcompany.db.dbentity.*;
import com.dachen.medicine.entity.Result;
import com.j256.ormlite.field.DatabaseField;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/3/4.
 */
public class DoctorsList extends Result
{
    public ArrayList<com.dachen.dgroupdoctorcompany.db.dbentity.Doctor> data;

}
