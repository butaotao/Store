package com.dachen.dgroupdoctorcompany.db.dbdao;

import android.content.Context;
import android.text.TextUtils;

import com.dachen.dgroupdoctorcompany.db.SQLiteHelper;
import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
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
public class DoctorDao {
    private Dao<Doctor, Integer> articleDao;
    private SQLiteHelper helper;
    Context context;
    public DoctorDao(Context context) {
        this.context = context;
        try {
            helper = SQLiteHelper.getHelper(context);
          //  articleDao = helper.getDao(CompanyContactListEntity.class);
            try {
                articleDao = DaoManager.createDao(
                        OpenHelperManager.getHelper(context, SQLiteHelper.class).getConnectionSource(),
                        Doctor.class);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addCompanyContact(Doctor article) {
        if (!TextUtils.isEmpty(article.userId)/*&&( null==queryByUserId(article.userId)||queryByUserId(article.userId).size()==0)*/){
            try {
                if (null!=queryByUserId(article.userId)&&queryByUserId(article.userId).size()>0){
                    Doctor doctor = queryByUserId(article.userId).get(0);
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
    public List<Doctor> queryByUserId(String userId){
        QueryBuilder<Doctor, Integer> builder = articleDao.queryBuilder();
        try {
            String  loginid=SharedPreferenceUtil.getString(context, "id", "");
            Where<Doctor, Integer> where = builder.where();
            where.eq("userloginid", loginid).and().eq("userId", userId);
            return builder.query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }
    public List<Doctor> queryByDepId(String hospitalId){
        QueryBuilder<Doctor, Integer> builder = articleDao.queryBuilder();
        try {
            String  loginid=SharedPreferenceUtil.getString(context, "id", "");
            Where<Doctor, Integer> where = builder.where();
            where.eq("userloginid", loginid).and().eq("hospitalId", hospitalId);
            List<Doctor> lists = builder.query();
            if (null ==lists){
                lists = new ArrayList<>();
            }
            return lists;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }
    public List<Doctor> queryAllByUserid() {
        QueryBuilder<Doctor, Integer> builder = articleDao.queryBuilder();
        try {
            String  loginid=SharedPreferenceUtil.getString(context, "id", "");
            Where<Doctor, Integer> where = builder.where();
            where.eq("userloginid", loginid).and().eq("userloginid", loginid);
            return builder.query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    public List<Doctor> queryByDepID(String depid) {
        QueryBuilder<Doctor, Integer> builder = articleDao.queryBuilder();
        String  loginid=SharedPreferenceUtil.getString(context, "id", "");
        try {
            Where<Doctor, Integer> where = builder.where();
            where.eq("userloginid", loginid).and().eq("hospitalId", depid);
            return builder.query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public List<Doctor> querySearchPage(String name, int pageNo) {
        QueryBuilder<Doctor, Integer> builder = articleDao.queryBuilder();
        String loginid=SharedPreferenceUtil.getString(context, "id", "");
        try {
            builder.limit(50l).offset((pageNo-1)*50l);
            Where<Doctor, Integer> where = builder.where();
            if (name.equals("1")){
                where.and (where.like("name", "%" + name + "%"), where.eq("userloginid", loginid));
            }else {
                where.or(where.and(where.like("telephone", "%" + name + "%"), where.eq("userloginid", loginid)),
                        where.and (where.like("name", "%" + name + "%"), where.eq("userloginid", loginid))
                        );
            }/*else {
                where.or(where.and(where.like("telephone", "%" + name + "%"), where.eq("userloginid", loginid)),
                        where.and (where.like("name", "%" + name + "%"), where.eq("userloginid", loginid))
                );
            }*/


            List<Doctor> doctors= builder.distinct().query();
            if (doctors==null){
                doctors = new ArrayList<>();
            }
            if (doctors.size()>1) {
                Collections.sort(doctors, new PinyinComparator());
            }
            return doctors;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Doctor> querySearch(String name) {
        QueryBuilder<Doctor, Integer> builder = articleDao.queryBuilder();

        String  loginid = SharedPreferenceUtil.getString(context, "id", "");
        List<Doctor> doctorss = new ArrayList<>();
        try {
            Where<Doctor, Integer> where = builder.where();
            where.or(where.and (where.like("name", "%" + name + "%"), where.eq("userloginid", loginid)),
                     where.and(where.like("telephone", "%" + name + "%"), where.eq("userloginid", loginid)));

            List<Doctor> doctors= builder.distinct().query();
            if (doctors==null){
                doctors = new ArrayList<>();
            }
            if (doctors.size()>1) {
                Collections.sort(doctors, new PinyinComparator());
            }
            return doctors;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    class PinyinComparator implements Comparator<Doctor> {
        public int compare(Doctor o1, Doctor o2) {
            if (o1.name.equals("@") || o2.name.equals("#")) {
                return -1;
            } else if (o1.name.equals("#") || o2.name.equals("@")) {
                return 1;
            } else {
                return o1.name.compareTo(o2.name);
            }
        }
    }
    public List<Doctor> queryAll() {
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

            DeleteBuilder<Doctor, Integer> builder = articleDao.deleteBuilder();
            Where<Doctor, Integer> where = builder.where();
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
            DeleteBuilder<Doctor, Integer> builder = articleDao.deleteBuilder();
            Where<Doctor, Integer> where = builder.where();
            where.eq("userloginid", loginid).and().eq("userId", userId);
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
    public int deleteByDepID(String hospitalId) {
        try {
            String  loginid=SharedPreferenceUtil.getString(context,"id","");
            DeleteBuilder<Doctor, Integer> builder = articleDao.deleteBuilder();
            Where<Doctor, Integer> where = builder.where();
            where.eq("userloginid", loginid).and().eq("hospitalId", hospitalId);
            return builder.delete();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }
}
