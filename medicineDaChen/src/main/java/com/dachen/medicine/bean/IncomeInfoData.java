package com.dachen.medicine.bean;

import com.dachen.medicine.entity.Result;

public class IncomeInfoData extends Result {
    private static final long serialVersionUID = 6419296563258039773L;
    public Data data;
    public class Data {
        public long accountBalance;//账户余额
        public long unAuditedAmount;//未审核
        public long totalIncome;//总收入
    }
}
