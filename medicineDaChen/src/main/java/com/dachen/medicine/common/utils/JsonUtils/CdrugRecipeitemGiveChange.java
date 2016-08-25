package com.dachen.medicine.common.utils.JsonUtils;

import android.text.TextUtils;

import com.dachen.medicine.entity.CdrugGive;
import com.dachen.medicine.entity.CdrugRecipeitem;
import com.dachen.medicine.entity.CdrugRecipeitemGive;
import com.dachen.medicine.entity.MedicineEntity;

/**
 * Created by Burt on 2016/6/8.
 */
public class CdrugRecipeitemGiveChange {
    public static CdrugRecipeitemGive.CdrugRecipeitemGiveInfo getData(CdrugGive gives){
        CdrugRecipeitemGive give = new CdrugRecipeitemGive();
        CdrugRecipeitemGive.CdrugRecipeitemGiveInfo info = give.new CdrugRecipeitemGiveInfo();
        CdrugRecipeitemGive.CdrugRecipeitemGiveInfo.GoodsUnit unit = info.new GoodsUnit();
        MedicineEntity entity = new MedicineEntity();
        MedicineEntity.MedicineInfo minfo = entity.new MedicineInfo();
        MedicineEntity.MedicineInfo.Goods goods = minfo.new Goods();

if (null!=gives){
    goods.id = gives.data.goodsId;
    goods.title = gives.data.title;
} if (null!=gives&&null!=gives.data){
            info.goods$general_name = gives.data.generalName;
            info.goods$pack_specification = gives.data.packSpecification;
            info.goods$specification = gives.data.specification;
            info.patient = gives.data.patientId;
            unit.name = gives.data.packUnitText;
            info.goods$unit = unit;
            info.goods$manufacturer = gives.data.manufacturer+"";
            info.patient$user_name = gives.data.patientName;
            if (!TextUtils.isEmpty(gives.data.isJoin)&&gives.data.isJoin.equals("0")){
                info.is_join = true;
            }else {
                info.is_join = true;
            }
            if (!TextUtils.isEmpty(gives.data.isOwn)&&gives.data.isOwn.equals("0")){
                info.is_own = true;
            }else {
                info.is_own = true;
            }
           /* info.num_syjf =gives.data.leftPointsNum;
            info.zyzdsxjfs = gives.data.lowPointsNum;
            info.zsmdwypxhjfs = gives.data.consumePointsNum;*/
           info.num_syjf =gives.data.leftPointsNum;
            info.zyzdsxjfs = gives.data.lowPointsNum;
            info.zsmdwypxhjfs = gives.data.consumePointsNum;
        }
        info.goods = goods;
        return info;

    }
}
