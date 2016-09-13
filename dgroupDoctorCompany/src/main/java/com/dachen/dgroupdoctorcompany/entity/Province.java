package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

/**
 * Created by Burt on 2016/2/23.
 */
public class Province extends Result{
    private String ProID;// 1,
    private String name;// 北京市,
    private String ProSort;// 1,
    private String ProRemark;// 直辖市

    public String getProID() {
        return ProID;
    }

    public void setProID(String proID) {
        ProID = proID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProSort() {
        return ProSort;
    }

    public void setProSort(String proSort) {
        ProSort = proSort;
    }

    public String getProRemark() {
        return ProRemark;
    }

    public void setProRemark(String proRemark) {
        ProRemark = proRemark;
    }
}
