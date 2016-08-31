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
    public static String companyId = "";
    public static CompanyContactDao companyContactDao;
    public static RoleDao roleDao;
    public static  void setUserInfo(LoginRegisterResult logins,Activity mThis){
        if (null == logins.data || null == logins.data.user){
            return;
        }
       if(!TextUtils.isEmpty(logins.data.getAccess_token())){
           SharedPreferenceUtil.putString(mThis, "session",
                   logins.data.getAccess_token());
           UserInfo.getInstance(mThis).setSession(logins.data.getAccess_token());
       }
        UserInfo.getInstance(mThis).setContextSession(logins.data.access_context);
        SharedPreferenceUtil.putString(mThis, "context_token",
                logins.data.access_context);
        UserInfo.getInstance(mThis).setPackageName(mThis, "com.dachen.dgroupdoctorcompany");
        UserInfo.getInstance(mThis).setUserType(Constants.USER_TYPE);
        UserInfo.getInstance(mThis).setId(logins.data.getUser().getUserId());
        if (!logins.data.getUser().getUserId().equals(SharedPreferenceUtil.getString(mThis,"id",""))){
            companyContactDao = new CompanyContactDao(mThis);
            companyContactDao.clearAllUser();
            SharedPreferenceUtil.putString(CompanyApplication.context,
                    SharedPreferenceUtil.getString(mThis,"id","") + "_time", "0");

        }
        SharedPreferenceUtil.putString(mThis, "id", logins.data.getUser().getUserId());

        boolean isrole = false;
        if (null!=logins.data.getUser()&&null!=logins.data.getUser().enterpriseUser
                &&null!=logins.data.getUser().enterpriseUser.role){
            for (int i=0;i<logins.data.getUser().enterpriseUser.role.size();i++){
                if (!TextUtils.isEmpty(logins.data.getUser().enterpriseUser.role.get(i).name)){
                    if (logins.data.getUser().enterpriseUser.role.get(i).name.equals("医药代表")){
                        SharedPreferenceUtil.putString(mThis,"role","医药代表");
                        isrole = true;
                        break;
                    }
                }
            }
        }
        if (!isrole){
            SharedPreferenceUtil.putString(mThis,"role","");
        }

        long expires_in = logins.data.getExpires_in() * 1000L + System.currentTimeMillis();
        SharedPreferenceUtil.putLong(mThis, "expires_in", expires_in);

        String url = logins.data.getUser().headPicFileName;
        SharedPreferenceUtil.getString(mThis, logins.data.getUser().getUserId() + "firstlogin", "");
        if (!TextUtils.isEmpty(SharedPreferenceUtil.getString(mThis, logins.data.getUser().getUserId() + "firstlogin", ""))){
            isFirstLogin = false;
        }
        LoginRegisterResult.LoginData d = logins.data;
        User u = d.getUser();
        if(!TextUtils.isEmpty(d.getAccess_token())){
            ImSdk.getInstance().initUser(d.getAccess_token(), u.userId, u.name, u.nickname, u.headPicFileName);
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

        UserInfo.getInstance(CompanyApplication.getInstance()).setPackageName(mThis, "com.dachen.dgroupdoctorcompany");
        UserInfo.getInstance(CompanyApplication.getInstance()).setUserType(Constants.USER_TYPE);
        UserInfo.getInstance(CompanyApplication.getInstance()).setId(logins.data.userId);
        SharedPreferenceUtil.putString(CompanyApplication.getInstance(), "id", logins.data.userId);
        SharedPreferenceUtil.putString(CompanyApplication.getInstance(),"telephone", logins.data.telephone);
        SharedPreferenceUtil.putString(CompanyApplication.getInstance(), "username", logins.data.userName);
        SharedPreferenceUtil.putString(CompanyApplication.getInstance(),"employeeId",logins.data.employeeId);
        ArrayList<Company> companys = logins.data.companys;
        getCompanyInfo(companys,mThis);
        String url = logins.data.logo;
        SharedPreferenceUtil.putString(CompanyApplication.getInstance(),   "head_url", url);
        SharedPreferenceUtil.putString(CompanyApplication.getInstance(), logins.data.headPic + "head_url", url);
        if (!TextUtils.isEmpty(SharedPreferenceUtil.getString(mThis,"session",""))){
            addDoctor(mThis,true,false);
        }
    }
    public static void getCompanyInfo(ArrayList<Company> companys,Activity mThis){
        if (null!=companys&&companys.size()>0){
            DepAdminsListDao dao = new DepAdminsListDao(mThis);
            Company c = companys.get(0);
            SharedPreferenceUtil.putString(CompanyApplication.getInstance(),"enterpriseId",c.companyId);
            SharedPreferenceUtil.putString(CompanyApplication.getInstance(),"enterpriseIds",c.companyId);
            SharedPreferenceUtil.putString(CompanyApplication.getInstance(),"enterpriseName",c.companyName);
            SharedPreferenceUtil.putString(CompanyApplication.getInstance(), "adminType", c.adminType + "");
            companyId = c.companyId;
            if(c.isRepresent==1){
                SharedPreferenceUtil.putString(CompanyApplication.getInstance(), com.dachen.dgroupdoctorcompany.utils.UserInfo.ROLE,"医药代表");
            }else {
                SharedPreferenceUtil.putString(CompanyApplication.getInstance(), com.dachen.dgroupdoctorcompany.utils.UserInfo.ROLE,"");
            }
            String enterpriseId = SharedPreferenceUtil.getString(CompanyApplication.getInstance(),"enterpriseId","");
            String enterpriseName = SharedPreferenceUtil.getString(CompanyApplication.getInstance(),"enterpriseName","");
            String adminType = SharedPreferenceUtil.getString(CompanyApplication.getInstance(),"adminType","");
            ArrayList<DepAdminsList> depAdminsList = c.depAdminsList;
            dao.clearAll();
            if (null!=depAdminsList&&depAdminsList.size()>0){

                for (int i=0;i<depAdminsList.size();i++){
                    DepAdminsList admin = depAdminsList.get(i);
                    if (!TextUtils.isEmpty(admin.orgDuty)&&admin.orgDuty.equals("1")){
                        admin.loginUserId = SharedPreferenceUtil.getString(CompanyApplication.getInstance(),"id","");
                        dao.addCompanyContact(admin);
                    } if(!TextUtils.isEmpty(admin.orgId)){
                        SharedPreferenceUtil.putString(CompanyApplication.getInstance(),
                                "departId", admin.orgId + "");
                    }
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
