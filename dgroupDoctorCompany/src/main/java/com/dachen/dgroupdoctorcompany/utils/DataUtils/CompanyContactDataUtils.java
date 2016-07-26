package com.dachen.dgroupdoctorcompany.utils.DataUtils;

import android.content.Context;

import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.DepAdminsListDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.DepAdminsList;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Burt on 2016/6/15.
 */
public class CompanyContactDataUtils {
    public static  List<CompanyContactListEntity> getManagerPeople(Context context,String idDep){
        CompanyContactDao  companyContactDao = new CompanyContactDao(context);

        List<CompanyContactListEntity> entities =companyContactDao.queryByParentId(idDep);
        if (null==entities){
            entities = new ArrayList<>();
        }
        return  entities;
    }
    public static  List<DepAdminsList> getManagerDep(Context context){
        DepAdminsListDao depDao = new DepAdminsListDao(context);
        List<DepAdminsList> entities  = depDao.queryManager();
        if (null==entities){
            entities = new ArrayList<>();
        }
        return  entities;
    }
}
