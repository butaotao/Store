package com.dachen.mediecinelibraryrealize.entity;

import com.dachen.medicine.entity.Result;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Burt on 2016/3/14.
 */
public class MedicineInfosList extends Result implements Serializable{
    public ArrayList<MedicineInfo> data;
    public class MedicineInfo implements Serializable{
        public String takeMedicalTime;
        public String generalName;
        public String image;
        public String manufacturer;
        public String goodsId;
        public String packSpecification;
        public String title;
        public String imageUrl;
        public Usage usage;
        public TotalQuantity totalQuantity;
        public int dateSeq;
        public class TotalQuantity{
            public String days;
            public int quantity;
            public String unit;
        }
        public class Reminder{
            public int duration;
            public int gapDay;
        }
        public class Usage{
            public int days;
            public String patients;
            public String quantity;
            public String remarks;
            public int times;
            public String unit;
            public String periodUnit;
            public String periodNum;
            public SomeBox.patientSuggest.Uses.Period period;
        }
    }
}
