package com.dachen.mediecinelibraryrealizedoctor.utils.Json;

import android.text.TextUtils;

import com.dachen.mediecinelibraryrealizedoctor.entity.DrugData;
import com.dachen.mediecinelibraryrealizedoctor.entity.DrugDtaList;
import com.dachen.mediecinelibraryrealizedoctor.entity.DrugUsege;
import com.dachen.mediecinelibraryrealizedoctor.entity.GoodsUsagesGoods;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineEntity;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/6/2.
 */
public class DrugChange {
    public static ArrayList<MedicineInfo> getMedicineList(ArrayList<DrugData> data){
        ArrayList<MedicineInfo> infos = new ArrayList<>();
        if (data!=null&&data.size()>0){
            for (int i=0;i<data.size();i++){
                DrugData drugData = data.get(i);
                MedicineInfo info = new MedicineInfo();
                info.goods$specification = drugData.specification;
                info.id = drugData.groupId;
                info.goods$image = drugData.imageUrl;
                info.goods$manufacturer = drugData.manufacturer;
                info.goods$image_url = drugData.imageUrl;
                info.trade_name = drugData.tradeName;
                info.goods$general_name = drugData.generalName;
                info.title = drugData.title;
                info.packUnitText = drugData.packUnitText;
                info.goods$pack_specification = drugData.packSpecification;
                MedicineEntity entity = new MedicineEntity();
                MedicineEntity.Goods goods = entity.new Goods();
                goods.title = drugData.title;
                goods.id = drugData.id;
                info.goods = goods;
                info.goodId = drugData.id;
                info.goods$number = drugData.number;
                MedicineInfo.GoodType type = info.new GoodType();
                type.title = drugData.manageTypeText;
                info.goods$type = type;
                MedicineInfo.GoodBizType bizType = info.new GoodBizType();
                bizType.name = drugData.productTypeText;
                bizType.title = drugData.productTypeText;
                info.goods$biz_type  = bizType;
                info.is_group_goods = drugData.ownedGroup;
                info.is_doctor_cb = drugData.collected;
                MedicineInfo.GoodForm form = info.new GoodForm();
                form.name = drugData.formText;
                info.goods$form = form;

                ArrayList<GoodsUsagesGoods> goods_usages_goods = new ArrayList<>();
                if (null!=drugData.drugUsegeList){
                    for (int j=0;j<drugData.drugUsegeList.size();j++){
                        DrugUsege d = drugData.drugUsegeList.get(j);
                        GoodsUsagesGoods goodsUsagesGoods = new GoodsUsagesGoods();
                        goodsUsagesGoods.method  = d.method;
                        goodsUsagesGoods.patients = d.patients;
                        goodsUsagesGoods.times = d.times;
                        goodsUsagesGoods.quantity =d.quantity;
                        GoodsUsagesGoods.Period period = goodsUsagesGoods.new Period();
                        period.number = d.periodNum;
                        String unit = "-1";
                        if (!TextUtils.isEmpty(d.periodTime)){
                            if (d.periodTime.contains("mon")){
                                unit = "月";
                            }else if (d.periodTime.contains("week")){
                                unit = "周";
                            }else if (d.periodTime.contains("day")){
                                unit = "天";
                            }else if (d.periodTime.contains("year")){
                                unit = "年";
                            }
                        }
                        if (unit.equals("-1")){
                            unit = d.periodTime;
                        }
                        period.dataUnit = unit;
                        period.unit = d.periodTime;
                        period.text = d.periodNum+"";
                        period.medieUnit = d.unitText;
                        goodsUsagesGoods.period = period;
                        goods_usages_goods.add(goodsUsagesGoods);
                    }
                }
                info.goods_usages_goods = goods_usages_goods;
                infos.add(info);
            }
        }
        return infos;
    }
}
