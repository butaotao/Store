package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

import java.io.Serializable;

/**
 * Created by Burt on 2016/2/23.
 */
public class Area   implements Serializable{

    /** serialVersionUID **/
    private static final long serialVersionUID = 8609419337241629652L;

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
