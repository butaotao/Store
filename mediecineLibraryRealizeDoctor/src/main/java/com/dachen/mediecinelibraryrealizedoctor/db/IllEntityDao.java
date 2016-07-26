package com.dachen.mediecinelibraryrealizedoctor.db;

import android.content.Context;

import com.dachen.medicine.common.utils.Alarm;
import com.dachen.medicine.common.utils.IllEntity;
import com.dachen.medicine.common.utils.MedicineApplication;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Burt on 2016/3/30.
 */
public class IllEntityDao {
    private Dao<IllEntity,Integer> dao;
    private static IllEntityDao mInstance;
    private static Context contexts;
    public IllEntityDao(Context context){
        dao = MedicineApplication.getdb().getInterfaceIllEntity();
    }
    public static IllEntityDao getInstance(Context context) {
        if (mInstance == null) {
            contexts= context;
            mInstance = new IllEntityDao(context);
        }
        return mInstance;
    }

    public int create(IllEntity ills) {
        try {
            return dao.create(ills);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }
    public TreeMap<String, IllEntity> getAllIlls(){
        TreeMap<String, IllEntity> maps = new TreeMap<String, IllEntity>();
        try {
            List<IllEntity> entities = dao.queryForAll();
            for (int i=0;i<entities.size();i++){
                maps.put(entities.get(i).getId(),entities.get(i));
            }
            return maps;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maps;
    }
    public List<IllEntity>  getAllIllsList(){
        try {
            List<IllEntity> entities = dao.queryForAll();

            return entities;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<IllEntity>  getIllByid(String id){
        QueryBuilder<IllEntity,Integer> builder = dao.queryBuilder();
        try {
            Where<IllEntity, Integer> where = builder.where();
            where.eq("id", id);
            return builder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<IllEntity>  getFirstLevelIllList(){
        QueryBuilder<IllEntity,Integer> builder = dao.queryBuilder();
        try {
            Where<IllEntity, Integer> where = builder.where();
            where.eq("id", "0");
            return builder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
