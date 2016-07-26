package com.dachen.medicine.bean;

import java.io.Serializable;

/**
 * Created by TianWei on 2016/3/15.
 */
public class IncomeListPageData implements Serializable{
    private static final long serialVersionUID = -5899867854869499565L;
    public String drugName;//药品名称
    public String drugCode;//药检码
    public String companyName;//药厂名称
    public String title;//药品用法
    public float money;//收益金钱
    public String goodsId;//药品ID
    public String packSpecification;
}
