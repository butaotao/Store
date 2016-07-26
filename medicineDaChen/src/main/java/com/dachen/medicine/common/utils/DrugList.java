package com.dachen.medicine.common.utils;

import com.dachen.incomelibrary.utils.UserInfo;
import com.dachen.medicine.entity.*;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/6/6.
 */
public class DrugList extends com.dachen.medicine.entity.Result{
    public Data data;
    public class Data{
        public ArrayList<Drug>  receiptList;
        public  PatientInfo patientInfo;
        //public UserInfo userInfo;
    }
}
