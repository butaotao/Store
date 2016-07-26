package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/6/8.
 */
public class ShowAddSelfPartHospitalsData extends Result{
    public Data data;
    public class Data {
        public ArrayList<ShowAddSelfPartHospitals> pageData;
    }
}
