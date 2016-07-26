package com.dachen.mediecinelibraryrealize.entity;

import com.dachen.medicine.entity.Result;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Burt on 2016/1/20.
 */
public class PointCanExchanges extends Result implements Serializable{
    private static final long serialVersionUID = -4785890341232245314L;
    public int num_hqjf;
    public List<PointCanExchange> list_datas;
    public static class PointCanExchange implements  Serializable{
        private static final long serialVersionUID = -603396526804679744L;
        public Goods goods;
        public String goods$image;
        public String goods$manufacturer;
        public static class Goods implements Serializable{
            private static final long serialVersionUID = -2063868983051537265L;
            public String id;
            public String title;
        }
        public  Unit goods$unit;
        public String goods$specification;
        public String goods$pack_specification;

        //赠药最低所需积分
        public int zyzdsxjfs;
        //赠药每单位所需积分
        public int zsmdwypxhjfs;
        //患者积分数
        public int num_syjf;

        public static class Unit implements  Serializable{
            private static final long serialVersionUID = 64181837693699288L;
            public String name;
        }

    }
}
