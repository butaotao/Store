package com.dachen.dgroupdoctorcompany.db.dbdao;

import android.content.Context;
import android.text.TextUtils;

import com.dachen.dgroupdoctorcompany.db.SQLiteHelper;
import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
import com.dachen.dgroupdoctorcompany.db.dbentity.Reminder;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Burt on 2016/3/1.
 */
public class RemindDao {
    private Dao<Reminder, Integer> articleDao;
    private SQLiteHelper helper;
    Context context;
    public RemindDao(Context context) {
        this.context = context;
        try {
            helper = SQLiteHelper.getHelper(context);
          //  articleDao = helper.getDao(CompanyContactListEntity.class);
            try {
                articleDao = DaoManager.createDao(
                        OpenHelperManager.getHelper(context, SQLiteHelper.class).getConnectionSource(),
                        Reminder.class);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addRemind(Reminder article) {
        if (!TextUtils.isEmpty(article.userloginid)/*&&( null==queryByUserId(article.userId)||queryByUserId(article.userId).size()==0)*/){
            try {
                if (null!=queryByUserCreateTime(article.createTime)&&queryByUserCreateTime(article.createTime).size()>0){
                    Reminder doctor = queryByUserCreateTime(article.createTime).get(0);
                    article._id = doctor._id;
                }
                /*if (null==queryByUserId(article.userId)||queryByUserId(article.userId).size()==0){*/
                    articleDao.createOrUpdate(article);
               // }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public List<Reminder> queryByUserCreateTime(long userId){
        QueryBuilder<Reminder, Integer> builder = articleDao.queryBuilder();
        try {
            String  loginid=SharedPreferenceUtil.getString(context, "id", "");
            Where<Reminder, Integer> where = builder.where();
            where.eq("userloginid", loginid);
            return builder.query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    public List<Reminder> queryAllByUserid() {
        QueryBuilder<Reminder, Integer> builder = articleDao.queryBuilder();
        try {
            String  loginid=SharedPreferenceUtil.getString(context, "id", "");
            Where<Reminder, Integer> where = builder.where();
            where.eq("userloginid", loginid).and().eq("userloginid", loginid);
            return builder.query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }



    public List<Reminder> queryAll() {
        try {

            return articleDao.queryForAll();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 清空所有记录
     *
     * @param
     * @return
     */
    public int clearAll() {
        try {

            DeleteBuilder<Reminder, Integer> builder = articleDao.deleteBuilder();
            Where<Reminder, Integer> where = builder.where();
            where.eq("userloginid", SharedPreferenceUtil.getString(context,"id",""));
            return builder.delete();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 清空所有记录
     *
     * @param
     * @return
     */
    public int deleteByid(String userId) {
        try {
            String  loginid=SharedPreferenceUtil.getString(context,"id","");
            DeleteBuilder<Reminder, Integer> builder = articleDao.deleteBuilder();
            Where<Reminder, Integer> where = builder.where();
            where.eq("userloginid", loginid);
            return builder.delete();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

}
