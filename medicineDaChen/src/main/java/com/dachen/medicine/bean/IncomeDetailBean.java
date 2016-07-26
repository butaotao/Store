package com.dachen.medicine.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TianWei on 2016/3/15.
 */
public class IncomeDetailBean implements Serializable {
    private static final long serialVersionUID = 5848589458769349013L;
    public String yM;
    public List<Data> list;

    public class Data implements Serializable {
        private static final long serialVersionUID = -5638522698755993878L;
        public String drugCode;
        public String drugTitle;
        public long money;
        public String time;
        public String clerkName;
        public String logType;
        public String formatDate;
        public String bizText;

    }
}
