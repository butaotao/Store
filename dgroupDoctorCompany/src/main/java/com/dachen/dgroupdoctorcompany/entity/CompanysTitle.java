package com.dachen.dgroupdoctorcompany.entity;

import android.text.TextUtils;

/**
 * Created by Burt on 2016/6/28.
 */
public class CompanysTitle {
    public String parentDept;
    public String id;
    public String name;

    @Override
    public boolean equals(Object o) {
        CompanysTitle title = (CompanysTitle)o;
        if (!TextUtils.isEmpty(id)&&!TextUtils.isEmpty(title.id)&&title.id.equals(this.id)){
                return true;
        }
        return false;
    }
}
