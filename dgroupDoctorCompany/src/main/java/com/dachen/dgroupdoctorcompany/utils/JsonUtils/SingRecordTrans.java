package com.dachen.dgroupdoctorcompany.utils.JsonUtils;

import android.content.Context;

import com.dachen.dgroupdoctorcompany.db.dbdao.DepAdminsListDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.DepAdminsList;
import com.dachen.dgroupdoctorcompany.entity.ChoiceDep;

/**
 * Created by Burt on 2016/6/24.
 */
public class SingRecordTrans {
    public static DepAdminsListDao managerDao;
    Context context;
    public static void processData(Context context,ChoiceDep dep){
        managerDao = new DepAdminsListDao(context);
        managerDao.clearSingDep();
        if (null!=dep){
            DepAdminsList dep5 = new DepAdminsList();
            dep5.cid = dep.orgId;
            dep5.pid = dep.parentId;
            dep5.orgName = dep.orgName;
            managerDao.addCompanyRecord(dep5);
        }else {
            return;
        }


        if (dep.childs!=null&&dep.childs.size()>0){
            for (int i=0;i<dep.childs.size();i++){
                ChoiceDep dep1 = dep.childs.get(i);
                DepAdminsList dep4 = new DepAdminsList();
                dep4.cid = dep1.orgId;
                dep4.pid = dep1.parentId;
                dep4.orgName = dep1.orgName;
                managerDao.addCompanyRecord(dep4);
            }
        }
        if (dep.parent!=null&&dep.parent.size()>0){
            for (int i=0;i<dep.parent.size();i++){
                    ChoiceDep dep1 = dep.parent.get(i);
                    DepAdminsList dep4 = new DepAdminsList();
                    dep4.cid = dep1.orgId;
                    dep4.pid = dep1.parentId;
                    dep4.orgName = dep1.orgName;
                    managerDao.addCompanyRecord(dep4);
            }
        }

    }
}
