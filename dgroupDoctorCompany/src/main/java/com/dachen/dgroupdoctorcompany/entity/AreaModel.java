package com.dachen.dgroupdoctorcompany.entity;

import java.util.List;

/**
 * Created by Burt on 2016/2/23.
 */
public class AreaModel extends Area {
    private static final long serialVersionUID = 6261654123262575663L;
    private List<Area> children;
    public List<Area> getChildren() {
        return children;
    }
    public void setChildren(List<Area> children) {
        this.children = children;
    }

}
