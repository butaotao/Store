package com.dachen.dgroupdoctorcompany.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.dachen.common.utils.Logger;
import com.dachen.common.utils.ToastUtil;
import com.dachen.dgroupdoctorcompany.activity.AddFriendByPhone;
import com.dachen.dgroupdoctorcompany.activity.LoginActivity;
import com.dachen.dgroupdoctorcompany.app.CompanyApplication;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.dgroupdoctorcompany.base.BaseActivity;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.DoctorDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.RoleDao;
import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
import com.dachen.dgroupdoctorcompany.entity.ChatAgreeEntity;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.entity.DoctorsList;
import com.dachen.dgroupdoctorcompany.entity.Role;
import com.dachen.medicine.common.utils.MActivityManager;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.TimeUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.HttpManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Burt on 2016/3/5.
 */
public class GetAllDoctor {
    public static final int DATALENGTH = 3;
    public static GetAllDoctor doctors;

    public static GetAllDoctor getInstance() {
        if (null == doctors) {
            doctors = new GetAllDoctor();
        }
        return doctors;
    }

    DoctorDao dao;
    CompanyContactDao companyContactDao;
    RoleDao roleDao;

    public void getPeople(Handler handler) {
        this.getPeople(null, handler);
    }

    public void getPeople() {
        this.getPeople(null, null);
    }

    public void getPeople(final Activity activity) {
        this.getPeople(activity, null);
    }

    public void getPeople(final Activity activity, final Handler handler) {
        dao = new DoctorDao(CompanyApplication.context);
        companyContactDao = new CompanyContactDao(CompanyApplication.context);
        roleDao = new RoleDao(CompanyApplication.context);
        HashMap<String, String> maps = new HashMap<>();
        maps.put("access_token", com.dachen.medicine.config.UserInfo.getInstance(CompanyApplication.context).getSesstion());
        String companyId = "" + SharedPreferenceUtil.getString(CompanyApplication.getInstance(), "enterpriseIds", "");

        final String userId = com.dachen.dgroupdoctorcompany.utils.UserInfo.getInstance(CompanyApplication.context).getId();
        final String update = SharedPreferenceUtil.getString(CompanyApplication.context,
                userId + "_update", "0");
        if (update.equals("0")) {
            SharedPreferenceUtil.putString(CompanyApplication.context,
                    userId + "_time", "0");
            SharedPreferenceUtil.putString(CompanyApplication.context,
                    userId + "_update", "1");
        }
        final String time = SharedPreferenceUtil.getString(CompanyApplication.context,
                userId + "_time", "0");

        if (TextUtils.isEmpty(companyId)) {
            SharedPreferenceUtil.putString(CompanyApplication.context,
                    userId + "_time", "0");
            return;
        }
        maps.put("drugCompanyId", companyId);
        maps.put("updateTime", time);
        HashMap<String, String> interfaces = new HashMap<>();
        interfaces.put("interface1", Constants.COMPANYCONTACTLIST + "");

        new HttpManager().post(CompanyApplication.context, interfaces, CompanyContactListEntity.class,
                maps, new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(final Result response) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Logger.d("yehj","begin---add---"+ TimeUtils.getTime());
                                String time = SharedPreferenceUtil.getString(CompanyApplication.context,
                                        userId + "_time", "0");
                                if (time.equals("0")) {
                                    companyContactDao.clearAll();
                                }
                                Result s = response;
                                String str = s.resultMsg;
                                String[] userinfo = str.split("\r\n");

                                final Gson mGson = new Gson();
                                String userID = SharedPreferenceUtil.getString(CompanyApplication.context, "id", "");

                                //add by yehj
                                ArrayList<CompanyContactListEntity>  addCompanyContactListEntity=new ArrayList<CompanyContactListEntity>();
                                ArrayList<CompanyContactListEntity>  deleteCompanyContactListEntity=new ArrayList<CompanyContactListEntity>();
                                if (userinfo.length >= DATALENGTH) {
                                    for (int i = DATALENGTH; i < userinfo.length; i++) {
                                        CompanyContactListEntity entity1 = new CompanyContactListEntity();
                                        entity1 = mGson.fromJson(userinfo[i], CompanyContactListEntity.class);
                                        if (entity1.userId.equals(userID)) {
                                            String url = entity1.headPicFileName;
                                            SharedPreferenceUtil.putString(CompanyApplication.context, "head_url", url);
                                            SharedPreferenceUtil.putString(CompanyApplication.context, userID + "head_url", url);
                                        }
                                        entity1.userloginid = userID;
                                        if (!TextUtils.isEmpty(entity1.status) && entity1.status.equals("1")) {
                                            addCompanyContactListEntity.add(entity1);
                                        }else{
                                            deleteCompanyContactListEntity.add(entity1);
                                        }
                                    }
                                }
                                if(time.equals("0")){
                                    if(addCompanyContactListEntity.size()>0){
                                        companyContactDao.addCompanyContactLis(addCompanyContactListEntity);
                                    }
                                }else{
                                    if(addCompanyContactListEntity.size()>0){
                                        for (CompanyContactListEntity entity: addCompanyContactListEntity){
                                            companyContactDao.addCompanyContact(entity);
                                        }
                                    }
                                    if(deleteCompanyContactListEntity.size()>0){
                                        for (CompanyContactListEntity entity: deleteCompanyContactListEntity){
                                            companyContactDao.deleteByid(entity.userId);
                                        }
                                    }
                                }
                                if (null != activity && activity instanceof LoginActivity) {
                                    //  activity.finish();
                                }
                                if (handler != null) {
                                    handler.sendEmptyMessage(1100);
                                }
                               if (null != activity && activity instanceof AddFriendByPhone) {
                                    Intent intent = new Intent();
                                    activity.setResult(200, intent);
                                   MActivityManager.getInstance().popTos(activity);
                                    ((BaseActivity) activity).closeLoadingDialog();
                                }

                                if (userinfo.length > 2) {
                                    String updatetime = userinfo[2];
                                    updatetime = updatetime.replace(" ", "").replace("ts=", "");
                                    SharedPreferenceUtil.putString(CompanyApplication.context,
                                            com.dachen.dgroupdoctorcompany.utils.UserInfo.getInstance(CompanyApplication.context).getId() + "_time", updatetime);
                                }
                                CompanyApplication.setInitContactList(3);
                            }
                        }).start();

                    }

                    @Override
                    public void onSuccess(ArrayList<Result> response) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {
                        if (null != activity && activity instanceof LoginActivity) {
                            //  activity.finish();
                        }
                    }
                },
                false, 1, false);
    }

    public void addDoctor() {
        //enterprise/doctor/search
        dao = new DoctorDao(CompanyApplication.context);
        companyContactDao = new CompanyContactDao(CompanyApplication.context);
        roleDao = new RoleDao(CompanyApplication.context);
        HashMap<String, String> maps = new HashMap<>();
        maps.put("access_token", UserInfo.getInstance(CompanyApplication.context).getSesstion());

        new HttpManager().get(CompanyApplication.context, Constants.DRUG+"saleFriend/search", DoctorsList.class,
                maps, new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result entity) {
                        DoctorsList doctorsList = (DoctorsList) entity;
                        if (null != doctorsList && null != doctorsList.data && doctorsList.data.size() > 0) {
                            for (int i = 0; i < doctorsList.data.size(); i++) {
                                Doctor doctor = doctorsList.data.get(i);
                                doctor.userloginid = SharedPreferenceUtil.getString(CompanyApplication.context, "id", "");
                                dao.addCompanyContact(doctor);
                            }
                        }
                    }

                    @Override
                    public void onSuccess(ArrayList<Result> response) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {

                    }
                },
                false, 1);
    }

    public void addSingDoctor(String userid) {
        dao = new DoctorDao(CompanyApplication.context);

        HashMap<String, String> maps = new HashMap<>();
        maps.put("access_token", com.dachen.medicine.config.UserInfo.getInstance(CompanyApplication.context).getSesstion());
        maps.put("userId", userid);
        HashMap<String, String> interfaces = new HashMap<>();
        interfaces.put("interface1", Constants.DRUG+"user/get" + "");
        new HttpManager().get(CompanyApplication.context, interfaces, ChatAgreeEntity.class,
                maps, new HttpManager.OnHttpListener<Result>() {
                    @Override
                    public void onSuccess(Result response) {
                        if (response instanceof ChatAgreeEntity) {

                            ChatAgreeEntity res = (ChatAgreeEntity) response;
                            if (null != res.data && null != res.data.doctor && null != res.data.doctor.check) {
                                if (res.data.userType == 3) {
                                    //if (res.data.status.equals("3")){
                                    Doctor doctor = new Doctor();
                                    doctor.userId = res.data.userId;
                                    doctor.name = res.data.name;
                                    doctor.departments = res.data.doctor.check.departments;
                                    doctor.headPicFileName = res.data.headPicFileName;
                                    doctor.hospital = res.data.doctor.check.hospital;
                                    doctor.hospitalId = res.data.doctor.check.hospitalId;
                                    doctor.sex = res.data.sex;
                                    doctor.skill = res.data.doctor.skill;
                                    doctor.telephone = res.data.telephone;
                                    doctor.userloginid = SharedPreferenceUtil.getString(CompanyApplication.context, "id", "");
                                    doctor.title = res.data.doctor.check.title;
                                    dao.addCompanyContact(doctor);
                                    // }

                                }
                            }

                        }
                    }

                    @Override
                    public void onSuccess(ArrayList<Result> response) {

                    }

                    @Override
                    public void onFailure(Exception e, String errorMsg, int s) {

                    }
                },
                false, 1);
    }
}
