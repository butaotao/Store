package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

/**
 * @项目名 MedicineProject
 * @Author: zxy on 16/9/1下午4:45.
 * @描述 TODO
 */
public  class VisitEditEnableBean extends Result{

    /**
     * detailMsg :
     * data : {"editStatus":true}
     */

    public String detailMsg;
    /**
     * editStatus : true
     */

    public DataBean data;

    public static class DataBean {
        public boolean editStatus ;
    }

}
