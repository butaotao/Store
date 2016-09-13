package com.dachen.dgroupdoctorcompany.utils.JsonUtils;

import com.dachen.dgroupdoctorcompany.entity.HospitalDes;
import com.dachen.dgroupdoctorcompany.entity.ShowAddSelfPartHospitals;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/6/8.
 */
public class AddSelfPartHospitalTrans {
    public static ArrayList<HospitalDes> getHospital( ArrayList<ShowAddSelfPartHospitals> pageData){

        ArrayList<HospitalDes> hospitalDes = new ArrayList<>();
        if (null!=pageData){
            for (int i=0;i<pageData.size();i++){
                HospitalDes des = new HospitalDes();
                ShowAddSelfPartHospitals h = pageData.get(i);
                des.id = h.id;
                des.name = h.name;
                hospitalDes.add(des);
            }
        }

        return  hospitalDes;
    }
}
