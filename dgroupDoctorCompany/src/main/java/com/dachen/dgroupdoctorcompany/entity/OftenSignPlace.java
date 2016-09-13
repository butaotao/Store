package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.dgroupdoctorcompany.db.dbentity.OftenSinPlace;
import com.dachen.medicine.entity.Result;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/8/5.
 */
public class OftenSignPlace extends Result{
    public Data data;
    public class Data{
        public ArrayList<OftenSinPlace> pageData;

    }
}
