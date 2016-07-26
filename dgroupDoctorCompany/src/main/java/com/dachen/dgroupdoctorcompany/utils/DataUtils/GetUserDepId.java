package com.dachen.dgroupdoctorcompany.utils.DataUtils;

import android.content.Context;

import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;

import java.util.List;

/**
 * Created by Burt on 2016/6/27.
 */
public class GetUserDepId {
    public static String getUserDepId(Context context){
        String depid = "";
        CompanyContactDao contactDao = new CompanyContactDao(context);
        List<CompanyContactListEntity> entities =  contactDao.queryByTelephone
                (SharedPreferenceUtil.getString(context, "telephone", ""));
        if (entities.size()>0){
            depid = entities.get(0).id;
        }else {
//            depid = SharedPreferenceUtil.getString(context,"orgId","");
            depid = SharedPreferenceUtil.getString(context,"departId","");
        }
        return depid;
    }
}
