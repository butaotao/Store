package com.dachen.dgroupdoctorcompany.base;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.dachen.dgroupdoctorcompany.activity.MainActivity;
import com.dachen.dgroupdoctorcompany.app.CompanyApplication;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.DepAdminsListDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.DoctorDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.RoleDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
import com.dachen.dgroupdoctorcompany.entity.Company;
import com.dachen.dgroupdoctorcompany.db.dbentity.DepAdminsList;
import com.dachen.dgroupdoctorcompany.entity.DoctorsList;
import com.dachen.dgroupdoctorcompany.entity.LoginGetUserInfo;
import com.dachen.dgroupdoctorcompany.entity.LoginRegisterResult;
import com.dachen.dgroupdoctorcompany.utils.GetAllDoctor;
import com.dachen.imsdk.ImSdk;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.entity.User;
import com.dachen.medicine.net.HttpManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Burt on 2016/4/12.
 */
public class UserLoginc {
    public static boolean isFirstLogin = true;
    public static DoctorDao dao;
    public static CompanyContactDao companyContactDao;
    public static RoleDao roleDao;
    public static  void setUserInfo(LoginRegisterResult logins,Activity mThis){
        if (null == logins.data || null == logins.data.majorUser){
            return;
        }
        LoginRegisterResult.LoginData.MajorUser user = logins.data.majorUser;
        if (user==null){
            return;
        }
        String access_token = logins.data.access_token;
        String userId = logins.data.userId;
       if(!TextUtils.isEmpty(access_token)){
           SharedPreferenceUtil.putString(mThis, "session",
                   access_token);
           UserInfo.getInstance(mThis).setSession(access_token);
       }
        UserInfo.getInstance(mThis).setContextSession(logins.data.access_context);
        SharedPreferenceUtil.putString(mThis, "context_token",
                logins.data.access_context);
        UserInfo.getInstance(mThis).setPackageName(mThis, "com.dachen.dgroupdoctorcompany");
        UserInfo.getInstance(mThis).setUserType(Constants.USER_TYPE);
        UserInfo.getInstance(mThis).setId(userId);
        if (!userId.equals(SharedPreferenceUtil.getString(mThis, "id", ""))){
            companyContactDao = new CompanyContactDao(mThis);
            companyContactDao.clearAllUser();
            SharedPreferenceUtil.putString(CompanyApplication.context,
                    SharedPreferenceUtil.getString(mThis,"id","") + "_time", "0");

        }
        SharedPreferenceUtil.putString(mThis, "id", userId);

        boolean isrole = false;
                    if (user.bizRoleConfig.drugSales==1){
                        SharedPreferenceUtil.putString(mThis,"role","医药代表");
                        isrole = true;
                    }

        if (!isrole){
            SharedPreferenceUtil.putString(mThis,"role","");
        }

        long expires_in = logins.data.expires_in * 1000L + System.currentTimeMillis();
        SharedPreferenceUtil.putLong(mThis, "expires_in", expires_in);

        SharedPreferenceUtil.getString(mThis, userId + "firstlogin", "");
        if (!TextUtils.isEmpty(SharedPreferenceUtil.getString(mThis, userId + "firstlogin", ""))){
            isFirstLogin = false;
        }

        if(!TextUtils.isEmpty(access_token)){
            ImSdk.getInstance().initUser(access_token, userId, user.name, user.name, user.headPic);
        }
        setUserInfos(logins,mThis);

    }
    public static  void setUserInfos(LoginRegisterResult logins,Activity mThis,int type){
        if ( type ==0 ){
            setUserInfos(logins,mThis);
        }
    }
    public static  void setUserInfos(LoginRegisterResult logins,Activity mThis){
        if (logins.data==null){
            return;
        }
        LoginRegisterResult.LoginData.MajorUser user = logins.data.majorUser;
        if (user==null){
            return;
        }
        UserInfo.getInstance(CompanyApplication.getInstance()).setPackageName(mThis, "com.dachen.dgroupdoctorcompany");
        UserInfo.getInstance(CompanyApplication.getInstance()).setUserType(Constants.USER_TYPE);
        UserInfo.getInstance(CompanyApplication.getInstance()).setId(logins.data.userId);
        SharedPreferenceUtil.putString(CompanyApplication.getInstance(), "id", logins.data.userId);
        SharedPreferenceUtil.putString(CompanyApplication.getInstance(),"telephone", user.telephone);
        SharedPreferenceUtil.putString(CompanyApplication.getInstance(), "username", user.name);
        SharedPreferenceUtil.putString(CompanyApplication.getInstance(),"employeeId",user.employeeId);
        ArrayList<DepAdminsList> companys = logins.data.depAdminsList;
        getCompanyInfo(logins.data,mThis);
        String url = logins.data.majorUser.headPic;
        SharedPreferenceUtil.putString(CompanyApplication.getInstance(),   "head_url", url);
        SharedPreferenceUtil.putString(CompanyApplication.getInstance(), user.headPic + "head_url", url);
        if (!TextUtils.isEmpty(SharedPreferenceUtil.getString(mThis,"session",""))){
            addDoctor(mThis,true,false);
        }
    }
    public static void getCompanyInfo(LoginRegisterResult.LoginData data,Activity mThis){
        if (null == data || null == data.majorUser){
            return;
        }
        LoginRegisterResult.LoginData.MajorUser user = data.majorUser;
        if (user==null){
            return;
        }
            DepAdminsListDao dao = new DepAdminsListDao(mThis);
            SharedPreferenceUtil.putString(CompanyApplication.getInstance(),"enterpriseId",user.drugCompanyId);
            SharedPreferenceUtil.putString(CompanyApplication.getInstance(),"enterpriseIds",user.drugCompanyId);
        if (null!=data.drugCompany){
            SharedPreferenceUtil.putString(CompanyApplication.getInstance(),"enterpriseName",data.drugCompany.name);
        }


            if(null!=user.bizRoleConfig&&user.bizRoleConfig.drugSales==1){
                SharedPreferenceUtil.putString(CompanyApplication.getInstance(), com.dachen.dgroupdoctorcompany.utils.UserInfo.ROLE,"医药代表");
            }else {
                SharedPreferenceUtil.putString(CompanyApplication.getInstance(), com.dachen.dgroupdoctorcompany.utils.UserInfo.ROLE,"");
            }
            String enterpriseId = SharedPreferenceUtil.getString(CompanyApplication.getInstance(),"enterpriseId","");
            String enterpriseName = SharedPreferenceUtil.getString(CompanyApplication.getInstance(),"enterpriseName","");
            ArrayList<DepAdminsList> depAdminsList = data.depAdminsList;
            dao.clearAll();
            if (null!=depAdminsList&&depAdminsList.size()>0){

                for (int i=0;i<depAdminsList.size();i++){
                    DepAdminsList admin = depAdminsList.get(i);
                        admin.loginUserId = SharedPreferenceUtil.getString(CompanyApplication.getInstance(),"id","");
                        dao.addCompanyContact(admin);
                     if(!TextUtils.isEmpty(admin.orgId)){
                        SharedPreferenceUtil.putString(CompanyApplication.getInstance(),
                                "departId", admin.orgId + "");
                    }
                }

            }
    }
    public static  void addDoctor(final Activity context, final boolean add,boolean showLoading){
        //enterprise/doctor/search
        if (showLoading){
            if (context instanceof BaseActivity){
                ((BaseActivity)context).closeLoadingDialog();
            }
        }

        HashMap<String ,String > maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(context).getSesstion());
        dao = new DoctorDao(context);
        companyContactDao = new CompanyContactDao(context);
        roleDao = new RoleDao(context);
        if (add){
            GetAllDoctor.getInstance().getPeople(context);
        }
        //"org/saleFriend/search"
        new HttpManager().get(context, Constants.FRIENDLIST, DoctorsList.class,
                maps, new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result entity) {
                        if (context instanceof BaseActivity){
                            ((BaseActivity)context).closeLoadingDialog();
                        }

                        if (entity instanceof DoctorsList){
                            DoctorsList doctorsList = (DoctorsList) entity;
                            if (null!=doctorsList&&null!=doctorsList.data&&doctorsList.data.size()>0){
                                for (int i=0;i<doctorsList.data.size();i++){
                                    Doctor doctor = doctorsList.data.get(i);
                                    doctor.userloginid = SharedPreferenceUtil.getString(context,"id","");
                                    dao.addCompanyContact(doctor);
                                }
                            }
                        }
                    }
                    @Override
                    public void onSuccess(ArrayList<Result> response) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {
                        if (context instanceof BaseActivity){
                            ((BaseActivity)context).closeLoadingDialog();
                        }
                    }
                },
                false, 1);
    }
}
