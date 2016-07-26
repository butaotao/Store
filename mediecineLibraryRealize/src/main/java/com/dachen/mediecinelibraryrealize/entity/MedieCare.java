package com.dachen.mediecinelibraryrealize.entity;

import com.dachen.medicine.entity.Result;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/5/18.
 */
public class MedieCare extends Result{
    public ArrayList<Care> data;
    public class Care{
        public int dateSeq;
        public String general_name;
        public String image;
        public String manufacturer;
        public String medicalId;
        public String pack_specification;
        public String title;
        public TotalQuantity totalQuantity;
        public class TotalQuantity{
            public int days;
            public int quantity;
            public String unit;
        }
        public MedicineInfosList.MedicineInfo.Usage usage;
   /*     public class usage{
            public String patients;
            public Period period;
                public class Period{
                public int number;
                public String text;
                public String unit;
                }
            public String quantity;
            public String remarks;
            public String times;
        }*/
    }
}
