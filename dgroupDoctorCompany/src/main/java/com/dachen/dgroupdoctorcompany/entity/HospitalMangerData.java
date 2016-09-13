package com.dachen.dgroupdoctorcompany.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Burt on 2016/6/13.
 */
public class HospitalMangerData  implements Serializable{
    public String goodsGroupId;
    public String goodsGroupName;
    public ArrayList<HospitalDes> hospitalList;
}
