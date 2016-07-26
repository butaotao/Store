package com.dachen.mediecinelibraryrealizedoctor.entity;

/**
 * Created by Burt on 2016/6/2.
 */
public class DrugUsege {
    /*"method": "口服，不可咀嚼",
            "patients": "成年人",
            "periodNum": 2,
            "periodTime": "month",
            "quantity": "1粒",
            "times": 5,
            "unit": "2"*/
    //使用方法
    public String method;
    public String patients;
    //用药周期 一个周期使用的数量
    public int periodNum;
    //用药周期 是 天 day 周 week 月 month 年 year
    public String periodTime;
    //每次用量
    public String quantity;
    //用药次数 一个周期 多少次
    public String times;
    //服用的单位
    public String unit;

    public String unitText;

  /*  period.number = d.periodNum;
    period.unit = d.unit;
    period.text
*/
}
