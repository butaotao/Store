package com.dachen.dgroupdoctorcompany.db.dbdao;

import android.content.Context;

import com.dachen.dgroupdoctorcompany.db.SQLiteHelper;
import com.dachen.dgroupdoctorcompany.db.dbentity.DepAdminsList;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Burt on 2016/6/14.
 */
public class DepAdminsListDao {
    private Dao<DepAdminsList, Integer> depAdminDao;
    private SQLiteHelper helper;
    Context context;
    public DepAdminsListDao(Context context) {
        this.context = context;
        try {
            helper = SQLiteHelper.getHelper(context);
            //  articleDao = helper.getDao(CompanyContactListEntity.class);
            try {
                depAdminDao = DaoManager.createDao(
                        OpenHelperManager.getHelper(context, SQLiteHelper.class).getConnectionSource(),
                        DepAdminsList.class);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addCompanyContact(DepAdminsList article) {
        try {
                depAdminDao.createOrUpdate(article);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addCompanyRecord(DepAdminsList article) {
        try {
                article.recorder = "1";
            article.loginUserId = SharedPreferenceUtil.getString(context, "id", "");
            depAdminDao.createOrUpdate(article);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<DepAdminsList> queryByUserId(){
        QueryBuilder<DepAdminsList, Integer> builder = depAdminDao.queryBuilder();
        try {
            Where<DepAdminsList, Integer> where = builder.where();
            where.eq("loginUserId", SharedPreferenceUtil.getString(context, "id", ""));
            List<DepAdminsList> entitys = builder.query();
            return entitys;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new ArrayList<>();

    }
    public List<DepAdminsList> queryManager(){
        QueryBuilder<DepAdminsList, Integer> builder = depAdminDao.queryBuilder();
        try {
            Where<DepAdminsList, Integer> where = builder.where();
            where.eq("loginUserId", SharedPreferenceUtil.getString(context, "id", ""));
            List<DepAdminsList> entitys = builder.query();
            return entitys;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new ArrayList<>();

    }
    public List<DepAdminsList> queryManagerSiner(String orgId){
        QueryBuilder<DepAdminsList, Integer> builder = depAdminDao.queryBuilder();
        try {
            Where<DepAdminsList, Integer> where = builder.where();
            where.eq("loginUserId", SharedPreferenceUtil.getString(context, "id", "")).and().eq("recorder", "1")
                    .and().eq("pid", orgId);
            List<DepAdminsList> entitys = builder.query();
            return entitys;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new ArrayList<>();

    }
    public List<DepAdminsList> queryManagerid(String orgId){
        QueryBuilder<DepAdminsList, Integer> builder = depAdminDao.queryBuilder();
        try {
            Where<DepAdminsList, Integer> where = builder.where();
            where.eq("loginUserId", SharedPreferenceUtil.getString(context, "id", "")).and().eq("recorder", "1")
                    .and().eq("cid", orgId);
            List<DepAdminsList> entitys = builder.query();
            return entitys;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new ArrayList<>();

    }
    /**
     * 清空所有记录
     *
     * @param
     * @return
     */
    public int clearAll() {
        try {

            DeleteBuilder<DepAdminsList, Integer> builder = depAdminDao.deleteBuilder();
            Where<DepAdminsList, Integer> where = builder.where();
            where.eq("loginUserId", SharedPreferenceUtil.getString(context,"id",""));
            return builder.delete();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }
    public int clearSingDep() {
        try {

            DeleteBuilder<DepAdminsList, Integer> builder = depAdminDao.deleteBuilder();
            Where<DepAdminsList, Integer> where = builder.where();
            where.eq("loginUserId", SharedPreferenceUtil.getString(context,"id","")).and().eq("recorder", "1");
            return builder.delete();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }
}
