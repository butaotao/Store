package com.dachen.dgroupdoctorcompany.db.dbdao;

import android.content.Context;

import com.dachen.dgroupdoctorcompany.db.SQLiteHelper;
import com.dachen.dgroupdoctorcompany.db.dbentity.OftenSinPlace;
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
import java.util.concurrent.Callable;

/**
 * Created by Burt on 2016/3/1.
 */
public class OftenSignPlaceDao {
    private Dao<OftenSinPlace, Integer> articleDao;
    private SQLiteHelper helper;
    Context context;
    public OftenSignPlaceDao(Context context) {
        this.context = context;
        try {
            helper = SQLiteHelper.getHelper(context);
          //  articleDao = helper.getDao(CompanyContactListEntity.class);
            try {
                articleDao = DaoManager.createDao(
                        OpenHelperManager.getHelper(context, SQLiteHelper.class).getConnectionSource(),
                        OftenSinPlace.class);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addRemind(OftenSinPlace article) {
             try {

                /*if (null==queryByUserId(article.userId)||queryByUserId(article.userId).size()==0){*/
                    articleDao.createOrUpdate(article);
               // }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    public List<OftenSinPlace> queryByUserCreateTime(long userId){
        QueryBuilder<OftenSinPlace, Integer> builder = articleDao.queryBuilder();
        try {
            String  loginid=SharedPreferenceUtil.getString(context, "id", "");
            Where<OftenSinPlace, Integer> where = builder.where();
            where.eq("userloginid", loginid);
            return builder.query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }
    public void addCompanyContactLis(final ArrayList<OftenSinPlace> entities) {
        try {
            articleDao.callBatchTasks(new Callable<Void>() {

                @Override
                public Void call() throws Exception {

                    for (OftenSinPlace entity : entities) {
                        articleDao.create(entity);
                    }
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public List<OftenSinPlace> queryAllByUserid() {
        QueryBuilder<OftenSinPlace, Integer> builder = articleDao.queryBuilder();
        try {
            String  loginid=SharedPreferenceUtil.getString(context, "id", "");
            Where<OftenSinPlace, Integer> where = builder.where();
            where.eq("userloginid", loginid);
            return builder.query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new ArrayList<>();
    }



    public List<OftenSinPlace> queryAll() {
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

            DeleteBuilder<OftenSinPlace, Integer> builder = articleDao.deleteBuilder();
            Where<OftenSinPlace, Integer> where = builder.where();
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
            DeleteBuilder<OftenSinPlace, Integer> builder = articleDao.deleteBuilder();
            Where<OftenSinPlace, Integer> where = builder.where();
            where.eq("userloginid", loginid);
            return builder.delete();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

}
