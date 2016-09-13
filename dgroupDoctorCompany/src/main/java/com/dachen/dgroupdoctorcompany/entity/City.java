package com.dachen.dgroupdoctorcompany.entity;

/**
 * Created by Burt on 2016/2/23.
 */
public class City {
    private String CityID;// 2,
    private String name;// 天津市,
    private String ProID;// 2,
    private String CitySort;// 2

    public String getCityID() {
        return CityID;
    }

    public void setCityID(String cityID) {
        CityID = cityID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProID() {
        return ProID;
    }

    public void setProID(String proID) {
        ProID = proID;
    }

    public String getCitySort() {
        return CitySort;
    }

    public void setCitySort(String citySort) {
        CitySort = citySort;
    }

    @Override
    public String toString() {
        return "City{" +
                "CityID='" + CityID + '\'' +
                ", name='" + name + '\'' +
                ", ProID='" + ProID + '\'' +
                ", CitySort='" + CitySort + '\'' +
                '}';
    }
}
