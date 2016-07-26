package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by weiwei on 2016/3/4.
 */
public class MedieEntity extends Result implements Serializable{
    public ArrayList<Medie> data;
    public class Medie extends BaseCompare{
        public String drugName;
        public long creatorDate;
        @Expose
        @SerializedName("goodsGroupId")
        public String id;
        public String userId;
        public String goodsId;
        public int selectedNum;
        public boolean select;
    }
}
