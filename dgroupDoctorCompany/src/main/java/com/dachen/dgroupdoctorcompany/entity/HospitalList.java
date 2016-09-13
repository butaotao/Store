package com.dachen.dgroupdoctorcompany.entity;

import android.text.TextUtils;

import com.dachen.medicine.entity.Result;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Burt on 2016/2/22.
 */
public class HospitalList extends Result{
    public Data data;
    public class Data {
    public ArrayList<HospitalDes> pageData;
    public class HospitalDes extends BaseSearch implements Serializable {
            public String view;
          public String address;
        public Province province;
        public String level;
        public City city;
        public String name;
        public String id;
        public boolean select;
        @Override
        public boolean equals(Object o) {
            if ( o instanceof HospitalDes){
                HospitalDes l = (HospitalDes)o;
                return !TextUtils.isEmpty(this.id) && !TextUtils.isEmpty(l.id) && this.id.equals(l.id);

            }
            return  false;
        }


        public class Province{
            public String name;
            public String id;
            public String title;
        }
        public class City{
            public String name;
            public String id;
            public String title;
        }

    }
    }
}
