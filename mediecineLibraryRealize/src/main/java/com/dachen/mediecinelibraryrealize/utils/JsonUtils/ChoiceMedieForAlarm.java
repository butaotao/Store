package com.dachen.mediecinelibraryrealize.utils.JsonUtils;

import com.dachen.mediecinelibraryrealize.entity.ChoiceMedieEntity;
import com.dachen.mediecinelibraryrealizedoctor.entity.DrugData;
import com.dachen.mediecinelibraryrealizedoctor.entity.DrugDtaList;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Burt on 2016/6/4.
 */
public class ChoiceMedieForAlarm {
   /* public String id;
    public String name;
    public String quantity;
    public String general_name;
    public String pack_specification;
    public int bought_quantity;
    public String title;*/
    public static List<ChoiceMedieEntity.MedieEntity> getMedicineList( ArrayList<DrugData> data){
        List<ChoiceMedieEntity.MedieEntity> entities =new ArrayList<>();
        if (null!=data&&data.size()>0){
            for (int i=0;i<data.size();i++){
                ChoiceMedieEntity entity = new ChoiceMedieEntity();
                ChoiceMedieEntity.MedieEntity entity1 = entity. new MedieEntity();
                DrugData drugData = data.get(i);
                entity1.id = drugData.id;
                entity1.title = drugData.title;
                entity1.general_name = drugData.generalName;
                entity1.pack_specification = drugData.packSpecification;
                entities.add(entity1);
            }
        }

        return  entities;
    }
}
