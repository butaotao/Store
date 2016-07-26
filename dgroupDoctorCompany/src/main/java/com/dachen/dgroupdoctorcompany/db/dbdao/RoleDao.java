package com.dachen.dgroupdoctorcompany.db.dbdao;

import android.content.Context;

import com.dachen.dgroupdoctorcompany.db.SQLiteHelper;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.entity.Role;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Burt on 2016/3/1.
 */
public class RoleDao {
    private Dao<Role, Integer> articleDao;
    private SQLiteHelper helper;

    public RoleDao(Context context) {
        try {
          /*  helper = SQLiteHelper.getHelper(context);
            articleDao = helper.getDao(Role.class);*/
            articleDao = DaoManager.createDao(
                    OpenHelperManager.getHelper(context, SQLiteHelper.class).getConnectionSource(),
                    Role.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addRole(Role article) {
        try {
            articleDao.createOrUpdate(article);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteBy(Role article) {
        try {
            articleDao.createOrUpdate(article);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Role> queryAll(String userloginid) {
        QueryBuilder<Role, Integer> builder = articleDao.queryBuilder();
        try {
            Where<Role, Integer> where = builder.where();
            where.eq("userloginid", userloginid);
            return builder.query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    public List<Role> queryByRoleKey(String key) {
        QueryBuilder<Role, Integer> builder = articleDao.queryBuilder();
        try {
            Where<Role, Integer> where = builder.where();
            where.eq("key", key);
            return builder.query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
