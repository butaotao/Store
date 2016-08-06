package com.dachen.dgroupdoctorcompany.db.dbdao;

import android.content.Context;
import android.text.TextUtils;

import com.dachen.dgroupdoctorcompany.db.SQLiteHelper;
import com.dachen.dgroupdoctorcompany.db.dbentity.Reminder;
import com.dachen.dgroupdoctorcompany.db.dbentity.WeekSinger;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Burt on 2016/3/1.
 */
public class WeekDao {
    private Dao<WeekSinger, Integer> articleDao;
    private SQLiteHelper helper;
    Context context;
    public WeekDao(Context context) {
        this.context = context;
        try {
            helper = SQLiteHelper.getHelper(context);
          //  articleDao = helper.getDao(CompanyContactListEntity.class);
            try {
                articleDao = DaoManager.createDao(
                        OpenHelperManager.getHelper(context, SQLiteHelper.class).getConnectionSource(),
                        WeekSinger.class);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addRemind(WeekSinger article) {
             try {

                /*if (null==queryByUserId(article.userId)||queryByUserId(article.userId).size()==0){*/
                    articleDao.createOrUpdate(article);
               // }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    public List<WeekSinger> queryByUserCreateTime(long userId){
        QueryBuilder<WeekSinger, Integer> builder = articleDao.queryBuilder();
        try {
            String  loginid=SharedPreferenceUtil.getString(context, "id", "");
            Where<WeekSinger, Integer> where = builder.where();
            where.eq("userloginid", loginid);
            return builder.query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    public List<WeekSinger> queryAllByUserid() {
        QueryBuilder<WeekSinger, Integer> builder = articleDao.queryBuilder();
        try {
            String  loginid=SharedPreferenceUtil.getString(context, "id", "");
            Where<WeekSinger, Integer> where = builder.where();
            where.eq("userloginid", loginid).and().eq("userloginid", loginid);
            return builder.query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }



    public List<WeekSinger> queryAll() {
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

            DeleteBuilder<WeekSinger, Integer> builder = articleDao.deleteBuilder();
            Where<WeekSinger, Integer> where = builder.where();
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
            DeleteBuilder<WeekSinger, Integer> builder = articleDao.deleteBuilder();
            Where<WeekSinger, Integer> where = builder.where();
            where.eq("userloginid", loginid);
            return builder.delete();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

}
