package com.dachen.dgroupdoctorcompany.entity;

import android.text.TextUtils;

import com.dachen.dgroupdoctorcompany.db.dbentity.*;
import com.dachen.medicine.entity.Result;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Burt on 2016/2/22.
 */
public class Hospital extends Result{
    //public int total;
/*    {
        "data": {
        "pageCount": 1,
                "pageData": [
        {
            "city": 120100,
                "country": 120101,
                "id": "200108220176",
                "name": "272医院",
                "province": 120000
        },
        {
            "city": 120200,
                "country": 120223,
                "id": "201310110025",
                "name": "静海县静康门诊部",
                "province": 120000
        }
        ],
        "pageIndex": 1,
                "pageSize": 15,
                "start": 15,
                "total": 2
    },
        "resultCode": 1
    }*/
    public Data data;
    public class Data{
        public ArrayList<HospitalDes> pageData;
       /* public class HospitalDes extends  BaseSearch implements Serializable{

            public String city;
            public String country;
            public String id;
            public String name;
            public int province;
            @Override
            public boolean equals(Object o) {
                if ( o instanceof HospitalDes){
                    HospitalDes l = (HospitalDes)o;
                    if(!TextViewUtils.isEmpty(this.id)&&!TextViewUtils.isEmpty(l.id)&&this.id.equals(l.id)){
                        return true;
                    }
                    return false;

                }
                return  false;
            }
           *//* public class Creater{
                public String name;
                public String id;
                public String title;
            }
            public String last_modified_time;
            public String _type;
            public String id;
            public String title;
            public HospitalName hospital;
            public class HospitalName{
                public String name;
                public String _type;
                public String id;
                public String title;
            };*//*
     *//*   public LassModi last_modifier;
        public class LassModi{
            public String name;
            public String id;
            public String title;
        }*//*
        }*/
    }

}
