package com.dachen.dgroupdoctorcompany.entity;

import com.dachen.medicine.entity.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiwei on 2016/4/8.
 */
public class SignInList extends Result {
/*    public String  detailMsg;


    public DataBean data;

    public static class DataBean {
        public int pageCount;
        public int pageIndex;
        public int pageSize;
        public int start;
        public int total;
        public Object sorter;
        *//*
        public List<PageDataBean> pageData;

        public static class PageDataBean {
            public String  times;
            public long  time;
            public String address;
            public String  deviceNum;
            public String remark;
            public String  department;
            public String  userName;
            public String  day;
            public String  longTime;
            public String  headPic;
            public String signedId;
            public String coordinate;
            public List<String> tag;
        }
    }*/
    public String detailMsg;
    public Data   data;
    public class Data{
        public String   msg;
        public ArrayList<DataList> dataList;
        public class DataList{
            public String time;
            public String imgPath;
            public ArrayList<ListVisitVo> listVisitVo;
            public class ListVisitVo{
                public String  id;
                public String  userId;
                public String  doctorId;
                public String  doctorName;
                public long  time;
                public String  remark;
                public String  coordinate;
                public String  address;
                public String  type;
                public String  headPic;
                public String  addressName;
                public String  state;
                public String  deviceId;
                public List<String> singedTag;

                @Override
                public String toString() {
                    return "ListVisitVo{" +
                            "id='" + id + '\'' +
                            ", userId='" + userId + '\'' +
                            ", doctorId='" + doctorId + '\'' +
                            ", doctorName='" + doctorName + '\'' +
                            ", time=" + time +
                            ", remark='" + remark + '\'' +
                            ", coordinate='" + coordinate + '\'' +
                            ", address='" + address + '\'' +
                            ", type='" + type + '\'' +
                            ", headPic='" + headPic + '\'' +
                            '}';
                }
            }

            @Override
            public String toString() {
                return "DataList{" +
                        "time='" + time + '\'' +
                        ", imgPath='" + imgPath + '\'' +
                        ", listVisitVo=" + listVisitVo +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "SignInList{" +
                "detailMsg='" + detailMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
