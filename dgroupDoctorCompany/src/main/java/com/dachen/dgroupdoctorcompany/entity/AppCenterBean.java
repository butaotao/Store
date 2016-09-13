package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

import java.util.List;

/**
 * @项目名 MedicineProject
 * @Author: zxy on 16/9/5上午11:16.
 * @描述 应用中心app
 */
public class AppCenterBean extends Result {

    public DataBean data;

    public static class DataBean {
        public boolean needPage;
        public int pageCount;
        public int pageIndex;
        public int pageSize;
        public int start;
        public int total;
        /**
         * appId : 1001
         * creator : 1613431
         * creatorDate : 1472629598996
         * desc : 及时通知，要事必达
         * id : 57c92080217651ab2253a13b
         * name : 公告
         * pack : local
         * pic : http://default.dev.file.dachentech.com.cn/drugorg/notice_2x.png
         * protocol : local://notice
         * status : 1
         * updator : 1613431
         * updatorDate : 1472629598996
         * weight : 100
         */

        public List<PageDataBean> pageData;

        public static class PageDataBean {
            public String appId;
            public int creator;
            public long creatorDate;
            public String desc;
            public String id;
            public String name;
            public String pack;
            public String pic;
            public String protocol;
            public int status;
            public int updator;
            public long updatorDate;
            public int weight;
        }
    }
}
