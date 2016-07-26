package com.dachen.mediecinelibraryrealizedoctor.utils.Json;

import com.dachen.mediecinelibraryrealizedoctor.entity.PatientCollectionData;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineEntity;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/6/2.
 */
public class CollectionsDtaChange {
    public static ArrayList<MedicineInfo> getCollections(ArrayList<PatientCollectionData> data){
        ArrayList<MedicineInfo> infos = new ArrayList<>();
        if (data!=null&&data.size()>0){
            for (int i=0;i<data.size();i++){
                MedicineInfo info = new MedicineInfo();
                PatientCollectionData data1 = data.get(i);
                info.goods$general_name = data1.goodsGroupGeneralName;
                info.trade_name = data1.goodsGroupGeneralName;
                info.goodId = data1.id;
                info.id = data1.goodsGroupId;
                MedicineEntity entity = new MedicineEntity();
                MedicineEntity.Goods goods = entity.new Goods();
                goods.id = data1.id;
                info.goods = goods;
                infos.add(info);
            }
        }
        return infos;
    }
}
