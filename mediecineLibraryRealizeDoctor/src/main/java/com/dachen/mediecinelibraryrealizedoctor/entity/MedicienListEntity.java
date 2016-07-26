package com.dachen.mediecinelibraryrealizedoctor.entity;

import com.dachen.medicine.entity.Result;

import java.util.List;

/**
 * Created by weiwei on 2016/6/1.
 */
public class MedicienListEntity extends Result{
    public List<Data> data;
    public class Data{
        public long creatorDate;
        public String goodsGroupGeneralName;
        public String goodsGroupId;
        public String id;
        public String userId;
        public String userType;
        public String weight;
    }
}
