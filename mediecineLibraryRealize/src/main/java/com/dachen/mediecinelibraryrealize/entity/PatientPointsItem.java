package com.dachen.mediecinelibraryrealize.entity;

import com.dachen.medicine.entity.Result;

import java.util.List;

/**
 * Created by weiwei on 2016/5/31.
 */
public class PatientPointsItem extends Result {
    public int pointsCount;
    public List<Data> patientList;
    public class Data{
        public int consumePointsNum;
        public int leftPointsNum;
        public String manufacturer;
        public String packSpecification;
        public String packUnit;
        public String packUnitText;
        public String ruleId;
        public String title;
        public String goodsId;
    }
}
