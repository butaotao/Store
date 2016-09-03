package com.dachen.dgroupdoctorcompany.db.dbdao;

import android.content.Context;
import android.text.TextUtils;

import com.dachen.dgroupdoctorcompany.db.SQLiteHelper;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.StringUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by Burt on 2016/3/1.
 */
public class CompanyContactDao {
    private Dao<CompanyContactListEntity, Integer> articleDao;
    private SQLiteHelper helper;
    Context context;
    QueryBuilder<CompanyContactListEntity, Integer> builder;
    public CompanyContactDao(Context context) {
        this.context = context;
        try {
            helper = SQLiteHelper.getHelper(context);
            //  articleDao = helper.getDao(CompanyContactListEntity.class);
            try {
                articleDao = DaoManager.createDao(
                        OpenHelperManager.getHelper(context, SQLiteHelper.class).getConnectionSource(),
                        CompanyContactListEntity.class);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addCompanyContact(CompanyContactListEntity article) {
        try {
            List<CompanyContactListEntity> entitys = queryByUserId(article.userId);
            if (entitys == null || entitys.size() == 0) {
                articleDao.createOrUpdate(article);
            } else {
                if (entitys != null && entitys.size() > 0) {
                    article._id = entitys.get(0)._id;
                }
                articleDao.createOrUpdate(article);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addCompanyContactLis(final ArrayList<CompanyContactListEntity> entities) {
        try {
            articleDao.callBatchTasks(new Callable<Void>() {

                @Override
                public Void call() throws Exception {

                    for (CompanyContactListEntity entity : entities) {
                        articleDao.create(entity);
                    }
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public List<CompanyContactListEntity> queryByUserId(String userId) {
        QueryBuilder<CompanyContactListEntity, Integer> builder = articleDao.queryBuilder();
        try {
            Where<CompanyContactListEntity, Integer> where = builder.where();
            where.eq("userId", userId).and().eq("userloginid", SharedPreferenceUtil.getString(context, "id", ""));
            List<CompanyContactListEntity> entitys = builder.query();
            return entitys;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new ArrayList<>();

    }
    public  CompanyContactListEntity  queryByUserid(String userId) {
        QueryBuilder<CompanyContactListEntity, Integer> builder = articleDao.queryBuilder();
        try {
            Where<CompanyContactListEntity, Integer> where = builder.where();
            where.eq("userId", userId).and().eq("userloginid", SharedPreferenceUtil.getString(context, "id", ""));
            ;
             CompanyContactListEntity  entitys = builder.queryForFirst();

            return entitys;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new CompanyContactListEntity();

    }

    public List<CompanyContactListEntity> queryAndSortByUserIds(List<Integer> userIds) {
        QueryBuilder<CompanyContactListEntity, Integer> builder = articleDao.queryBuilder();
        List<CompanyContactListEntity> list = new ArrayList<>();
        try {
            builder.where().in("userId", userIds).and().eq("userloginid", SharedPreferenceUtil.getString(context, "id", ""));
            List<CompanyContactListEntity> entitys = builder.query();

            //根据传入的userIds的顺序将数据库中查询的数据进行重新排序
            for (int i = 0; i < userIds.size(); i++) {
                for (CompanyContactListEntity entity : entitys) {
                    if (entity.userId.equals(userIds.get(i) + "")) {
                        list.add(entity);
                    }
                }
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();

    }

    public List<CompanyContactListEntity> queryByUserIds(List<Integer> userIds) {
        QueryBuilder<CompanyContactListEntity, Integer> builder = articleDao.queryBuilder();
        try {
            builder.where().in("userId", userIds).and().eq("userloginid", SharedPreferenceUtil.getString(context, "id", ""));
            List<CompanyContactListEntity> list = builder.query();
            Map<String, CompanyContactListEntity> map = new HashMap<String, CompanyContactListEntity>(list.size());
            for (int i = 0; i < list.size(); i++) {
                CompanyContactListEntity entity = list.get(i);
                map.put(entity.userId, entity);
            }

            List<CompanyContactListEntity> sortedList = new ArrayList<CompanyContactListEntity>(userIds.size());

            for (int i = 0; i < userIds.size(); i++) {
                String userId = userIds.get(i) + "";
                if (map.containsKey(userId)) {
                    CompanyContactListEntity entity = map.get(userId);
                    sortedList.add(entity);
                }
            }
            return sortedList;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new ArrayList<CompanyContactListEntity>();
    }


    public List<CompanyContactListEntity> queryByParentId(String pid) {
        QueryBuilder<CompanyContactListEntity, Integer> builder = articleDao.queryBuilder();
        try {
            Where<CompanyContactListEntity, Integer> where = builder.where();
            where.eq("id", pid).and().eq("userloginid", SharedPreferenceUtil.getString(context, "id", ""));
            List<CompanyContactListEntity> entitys = builder.query();
            return entitys;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    public List<CompanyContactListEntity> queryAll(String userloginid) {
        QueryBuilder<CompanyContactListEntity, Integer> builder = articleDao.queryBuilder();
        try {
            Where<CompanyContactListEntity, Integer> where = builder.where();
            where.eq("userloginid", userloginid);
            return builder.query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }//telephone

    public CompanyContactListEntity queryByTelephone(String telephone) {
        QueryBuilder<CompanyContactListEntity, Integer> builder = articleDao.queryBuilder();
        String loginid = SharedPreferenceUtil.getString(context, "id", "");
        try {
            Where<CompanyContactListEntity, Integer> where = builder.where();
            builder.where().eq("userloginid", loginid).and().eq("telephone", telephone);
            return builder.queryForFirst();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    public List<CompanyContactListEntity> queryByTelephones(List<String> telephones) {
        QueryBuilder<CompanyContactListEntity, Integer> builder = articleDao.queryBuilder();
        try {
            builder.where().in("telephone", telephones).and().eq("userloginid", SharedPreferenceUtil.getString(context, "id", ""));
            return builder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    public List<CompanyContactListEntity> queryByDepID(String depid) {
        QueryBuilder<CompanyContactListEntity, Integer> builder = articleDao.queryBuilder();
        String loginid = SharedPreferenceUtil.getString(context, "id", "");
        try {
            Where<CompanyContactListEntity, Integer> where = builder.where();
            builder.where().eq("userloginid", loginid).and().eq("id", depid);
            return builder.query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public List<CompanyContactListEntity> querySearchPage2(String name, int pageNo,boolean limit) {
        QueryBuilder<CompanyContactListEntity, Integer> builder = articleDao.queryBuilder();
        String loginid = SharedPreferenceUtil.getString(context, "id", "");
        boolean ispinyin = false;
        try {
            if (limit){
                builder.limit(50l).offset((pageNo - 1) * 50l);
            }
            List<CompanyContactListEntity> entities = new ArrayList<>();
            Where<CompanyContactListEntity, Integer> where = builder.where();
            if (name.equals("1")){
                builder.orderBy("name", true);
                 where.and(where.eq("userloginid", loginid), where.like("name", "%" + name + "%"));

            }else {
                boolean isNunicodeDigits= StringUtils.isNumeric(name);
                if (isNunicodeDigits){
                    builder.orderBy("phone", true);
                    where.or(where.and(where.eq("userloginid", loginid), where.like("telephone", "%" + name + "%"))
                            ,where.and(where.eq("userloginid", loginid), where.like("name", "%" + name + "%"))
                            );
                }else {
                    builder.orderBy("name", true);
                    where.or(where.and(where.eq("userloginid", loginid), where.like("name", "%" + name + "%")),
                            where.and(where.eq("userloginid", loginid), where.like("telephone", "%" + name + "%")));
                    ispinyin = true;
                }

            }
            if (null != where.query()) {
                entities.addAll(builder.distinct().query());
                CompanyContactListEntity contact = new CompanyContactListEntity();
                contact.userId = loginid;
                entities.remove(contact);
            }
            return entities;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    public QueryBuilder<CompanyContactListEntity, Integer> getbuider(int pageNo){
        if (null==builder){
            builder = articleDao.queryBuilder();
            builder.orderBy("pinYinOrderType", true);
            builder.orderBy("name", true);
        }


        return builder;
    }
    public List<CompanyContactListEntity> querySearchPage(String name, int pageNo,boolean limit) {
        QueryBuilder<CompanyContactListEntity, Integer> builder = getbuider(pageNo);
        String loginid = SharedPreferenceUtil.getString(context, "id", "");
        boolean ispinyin = false;
        try {
           if (limit){
                builder.limit(50l).offset((pageNo - 1) * 50l);
            }
            List<CompanyContactListEntity> entities = new ArrayList<>();
            List<String> phones = new ArrayList<>();
            Where<CompanyContactListEntity, Integer> where = builder.where();
            boolean isNunicodeDigits= StringUtils.isNumeric(name);
            boolean containsChinese = StringUtils.containsChinese(name);
            boolean isEnglish = StringUtils.isEnglish(name);
            if (isNunicodeDigits){
                name = "%" + name + "%";
                if (!name.equals("%" +"1"+ "%")){
                /*    builder.orderBy("pinYinOrderType", true);
                    builder.orderBy("name", true);*/
                    where.and(where.eq("userloginid", loginid), where.like("telephone", name)) ;
                    if (null != where.query()) {
                        entities.addAll(builder.distinct().query());
                    }
                    for (int i = 0; i < entities.size(); i++) {
                        phones.add(entities.get(i).userId);
                    }
                    where.reset();
                   /* builder.orderBy("pinYinOrderType", true);
                    builder.orderBy("name", true);*/

                    where.and(where.and(where.eq("userloginid", loginid), where.like("name", name)),
                            where.notIn("userId", phones));
                    if (null != where.query()) {
                        entities.addAll(builder.distinct().query());
                    }
                    int size = entities.size();
                }else {
                   // builder.reset();
                    builder.orderBy("pinYinOrderType", true);
                    builder.orderBy("name", true);

                    where.and(where.eq("userloginid", loginid), where.like("name", name));
                    if (null != where.query()) {
                        entities.addAll(builder.distinct().query());
                    }
                    int size = entities.size();
                }
            }else {
                if (containsChinese){
                    name = "%" + name + "%";
                    where.reset();
                    builder.orderBy("pinYinOrderType", true);
                    builder.orderBy("name", true);
                    where.and(where.eq("userloginid", loginid), where.like("name", name));
                    if (null != where.query()) {
                        entities.addAll(builder.distinct().query());
                    }
                    for (int i=0;i<entities.size();i++){
                        phones.add(entities.get(i).userId);
                    }
                }else if(isEnglish){
                    where.reset();
                    String s = "";
                    if (!TextUtils.isEmpty(name)){
                        if (name.length()>1){
                            s = "%";
                           for(int i=0;i<name.length();i++){

                                   s+=name.charAt(i) +"%";
                           }

                        }else if(name.length()==1){
                            s = "%" + name + "%";
                        };
                    }
                    builder.orderBy("pinYinOrderType",true);
                    builder.orderBy("simpinyin", true);
                    where.or(
                            where.and(where.eq("userloginid", loginid), where.like("name", "%" + name + "%")),
                            where.and(where.eq("userloginid", loginid), where.like("simpinyin", s)));
                    if (null != where.query()) {
                        entities.addAll(builder.distinct().query());
                    }
                   int size = entities.size();

                }else {
                    where.reset();
                    builder.orderBy("name", true);
                    where.and(where.eq("userloginid", loginid), where.like("name", "%" + name + "%"));
                    if (null != where.query()) {
                        entities.addAll(builder.distinct().query());
                    }
                }

            }
           /* if (entities.size()>0) {
                CompanyContactListEntity contact = new CompanyContactListEntity();
                contact.userId = loginid;
                entities.remove(contact);
            }*/

            return entities;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    public List<CompanyContactListEntity> querySearch(String name) {
        QueryBuilder<CompanyContactListEntity, Integer> builder = articleDao.queryBuilder();
        String loginid = SharedPreferenceUtil.getString(context, "id", "");
        try {
            Where<CompanyContactListEntity, Integer> where = builder.where();
            where.or(where.and(where.eq("userloginid", loginid), where.like("name", "%" + name + "%")),
                    where.and(where.eq("userloginid", loginid), where.like("telephone", "%" + name + "%")));
            List<CompanyContactListEntity> entities = new ArrayList<>();

            if (null != where.query()) {
                entities.addAll(builder.distinct().query());
            }
            if (entities.size() > 1) {
                Collections.sort(entities, new PinyinComparator());
            }

            return entities;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    class PinyinComparator implements Comparator<CompanyContactListEntity> {
        public int compare(CompanyContactListEntity o1, CompanyContactListEntity o2) {
            if (!TextUtils.isEmpty(o1.name)&&!TextUtils.isEmpty(o2.name)){
                if (o1.name.equals("@") || o2.name.equals("#")) {
                    return -1;
                } else if (o1.name.equals("#") || o2.name.equals("@")) {
                    return 1;
                } else {
                    return o1.name.compareTo(o2.name);
                }
            }
            return 1;
        }
    }

    public List<CompanyContactListEntity> queryAll() {
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

            DeleteBuilder<CompanyContactListEntity, Integer> builder = articleDao.deleteBuilder();
            Where<CompanyContactListEntity, Integer> where = builder.where();
            where.eq("userloginid", SharedPreferenceUtil.getString(context, "id", ""));
            return builder.delete();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public int clearAllUser() {
        try {

            DeleteBuilder<CompanyContactListEntity, Integer> builder = articleDao.deleteBuilder();
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

            DeleteBuilder<CompanyContactListEntity, Integer> builder = articleDao.deleteBuilder();
            Where<CompanyContactListEntity, Integer> where = builder.where();
            where.eq("userId", userId).and().eq("userloginid", SharedPreferenceUtil.getString(context, "id", ""));
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
    public int deleteBy(String userId) {
        try {

            DeleteBuilder<CompanyContactListEntity, Integer> builder = articleDao.deleteBuilder();
            Where<CompanyContactListEntity, Integer> where = builder.where();
            where.eq("userId", userId).and().eq("userloginid", SharedPreferenceUtil.getString(context, "id", ""));
            return builder.delete();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }
}
