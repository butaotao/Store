package com.dachen.mediecinelibraryrealizedoctor.entity;

import com.dachen.medicine.entity.Result;

/**
 * Created by Burt on 2016/6/2.
 */
public class AddDoctorCollectionInfo extends Result{
    public Data data;
    public class Data{
        public String drugCollectionId;
    }
}
