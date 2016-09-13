package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

import java.util.List;

/**
 * @项目名 MedicineProject
 * @Author: zxy on 16/9/5下午5:30.
 * @描述 TODO
 */
public class MyAppBean extends Result {

    public DataBean data;

    public static class DataBean {
        public int deptManager;
        public String drugCompanyId;
        public int drugSales;
        public int marketing;
        public boolean needPage;
        public int pageCount;
        public int pageIndex;
        public int pageSize;
        public int start;
        public int status;
        public int total;
        /**
         * appId : 1004
         * creator : 1613431
         * creatorDate : 1472895550153
         * desc : 了解团队，掌握变化
         * drugCompanyId : 579700c94203f30f3440750f
         * id : 57cd3943d1baf86fb72f1038
         * name : 业务统计
         * pack : local
         * pic : http://default.dev.file.dachentech.com.cn/drugorg/statistics_2x.png
         * protocol : local://statistics
         * roles : {"deptManager":1,"drugSales":1,"marketing":1}
         * secret : ba0ef2d972324b6bb0d0b5ea99f95f48
         * status : 1
         * updator : 1613431
         * updatorDate : 1472895550153
         * weight : 400
         */

        public List<PageDataBean> pageData;

        public static class PageDataBean {
            public String appId;
            public int creator;
            public long creatorDate;
            public String desc;
            public String drugCompanyId;
            public String id;
            public String name;
            public String pack;
            public String pic;
            public String protocol;
            /**
             * deptManager : 1
             * drugSales : 1
             * marketing : 1
             */

            public RolesBean roles;
            public String secret;
            public int status;
            public int updator;
            public long updatorDate;
            public int weight;

            public static class RolesBean {
                public int deptManager;
                public int drugSales;
                public int marketing;
            }
        }
    }
}
