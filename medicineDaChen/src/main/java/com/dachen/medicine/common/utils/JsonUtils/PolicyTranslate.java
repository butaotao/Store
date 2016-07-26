package com.dachen.medicine.common.utils.JsonUtils;

import com.dachen.medicine.entity.MedicineEntity;
import com.dachen.medicine.entity.Policy;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/6/13.
 */
public class PolicyTranslate {
    public static ArrayList<Policy> transData(ArrayList<Policy> policies){
        if (null==policies){
            policies = new ArrayList<>();
        }
        return  policies;
    }
}
