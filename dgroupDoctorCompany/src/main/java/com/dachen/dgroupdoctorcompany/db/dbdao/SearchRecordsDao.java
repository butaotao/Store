package com.dachen.dgroupdoctorcompany.db.dbdao;

import android.content.Context;

import com.dachen.dgroupdoctorcompany.db.SQLiteHelper;
import com.dachen.dgroupdoctorcompany.db.dbentity.SearchRecords;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.entity.Role;
import com.dachen.medicine.common.utils.DrugRemind;
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
 * Created by Burt on 2016/3/2.
 */
public class SearchRecordsDao {
    private Dao<SearchRecords, Integer> articleDao;
    private SQLiteHelper helper;
    Context context;
    public SearchRecordsDao(Context context) {
        this.context = context;
        try {
          /*  helper = SQLiteHelper.getHelper(context);
            articleDao = helper.getDao(Role.class);*/
            articleDao = DaoManager.createDao(
                    OpenHelperManager.getHelper(context, SQLiteHelper.class).getConnectionSource(),
                    SearchRecords.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addRole(SearchRecords article,String searchtype) {
      if (null==this.queryBySearchContent(article.searchresult,searchtype)||this.queryBySearchContent(article.searchresult,searchtype).size()==0) {
          try {
              articleDao.createOrUpdate(article);
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
    }
    public List<SearchRecords> queryAll(String serchtype) {
        QueryBuilder<SearchRecords, Integer> builder = articleDao.queryBuilder();
        try {
            Where<SearchRecords, Integer> where = builder.where();
            builder.orderBy("_id", false);
            where.eq("userloginid", SharedPreferenceUtil.getString(context, "id", "")).and().eq("serchtype", serchtype);
            return builder.query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    public List<SearchRecords> queryBySearchContent(String content,String serchtype) {
        QueryBuilder<SearchRecords, Integer> builder = articleDao.queryBuilder();
        try {
            Where<SearchRecords, Integer> where = builder.where();
            builder.orderBy("_id", true);

            where .eq("searchresult", content).and().eq("userloginid",SharedPreferenceUtil.getString(context, "id", ""))
            .and().eq("serchtype",serchtype);
            return builder.query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    public List<SearchRecords> queryAllUserInfo() {
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
    public int clearAll(String serchtype) {
        try {

            DeleteBuilder<SearchRecords, Integer> builder = articleDao.deleteBuilder();
            Where<SearchRecords, Integer> where = builder.where();
            where.eq("userloginid", SharedPreferenceUtil.getString(context,"id","")).and().eq("serchtype", serchtype);
            return builder.delete();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }
    public List<SearchRecords> querySearch(String name,String serchtype) {
        QueryBuilder<SearchRecords, Integer> builder = articleDao.queryBuilder();
        String  loginid=SharedPreferenceUtil.getString(context,"id","");
        try {
            Where<SearchRecords, Integer> where = builder.where();
            where.like("searchresult", "%" + name + "%").and().eq("serchtype", serchtype);
            return builder.query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
