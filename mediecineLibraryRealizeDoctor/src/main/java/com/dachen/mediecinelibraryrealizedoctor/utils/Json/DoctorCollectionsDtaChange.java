package com.dachen.mediecinelibraryrealizedoctor.utils.Json;

import android.text.TextUtils;

import com.dachen.mediecinelibraryrealizedoctor.entity.DoctorCollectionData;
import com.dachen.mediecinelibraryrealizedoctor.entity.DrugData;
import com.dachen.mediecinelibraryrealizedoctor.entity.DrugUsege;
import com.dachen.mediecinelibraryrealizedoctor.entity.GoodsUsagesGoods;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineEntity;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo;
import com.dachen.mediecinelibraryrealizedoctor.entity.PatientCollectionData;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/6/2.
 */
public class DoctorCollectionsDtaChange {
    public static ArrayList<MedicineInfo> getMedicineList(ArrayList<DoctorCollectionData> data) {
        ArrayList<MedicineInfo> infos = new ArrayList<>();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                DoctorCollectionData drugData = data.get(i);
                MedicineInfo info = new MedicineInfo();
                if (null!=drugData.goods){
                    info.goods$specification = drugData.goods.specification;
                    info.id = drugData.goods.groupId;
                    info.goods$image = drugData.goods.imageUrl;
                    info.goods$manufacturer = drugData.goods.manufacturer;
                    info.goods$image_url = drugData.goods.imageUrl;
                    info.trade_name = drugData.goods.tradeName;
                    info.goods$general_name = drugData.goods.generalName;
                    info.title = drugData.goods.title;
                    info.goods$pack_specification = drugData.goods.packSpecification;
                    info.packUnitText = drugData.goods.packUnitText;
                    MedicineEntity entity = new MedicineEntity();
                    info.goodId = drugData.id;
                    MedicineEntity.Goods goods = entity.new Goods();
                    goods.title = drugData.goods.title;
                    goods.id = drugData.goods.id;
                    info.goods = goods;
                    info.goods$number = drugData.goods.number;
                    MedicineInfo.GoodType type = info.new GoodType();
                    type.title = drugData.goods.manageTypeText;
                    info.goods$type = type;
                    MedicineInfo.GoodBizType bizType = info.new GoodBizType();
                    bizType.name = drugData.goods.productTypeText;
                    bizType.title = drugData.goods.productTypeText;
                    info.goods$biz_type = bizType;
                    info.is_group_goods = drugData.goods.ownedGroup;
                    info.is_doctor_cb = drugData.goods.collected;
                    MedicineInfo.GoodForm form = info.new GoodForm();
                    form.name = drugData.goods.formText;
                    info.goods$form = form;

                    ArrayList<GoodsUsagesGoods> goods_usages_goods = new ArrayList<>();
                    if (null != drugData.goods.drugUsegeList) {
                        for (int j = 0; j < drugData.goods.drugUsegeList.size(); j++) {
                            DrugUsege d = drugData.goods.drugUsegeList.get(j);
                            GoodsUsagesGoods goodsUsagesGoods = new GoodsUsagesGoods();
                            goodsUsagesGoods.method = d.method;
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
                            period.unit = d.periodTime;
                            period.text = d.periodNum+""+unit;
                            period.medieUnit = d.unitText;
                            goodsUsagesGoods.period = period;
                            goods_usages_goods.add(goodsUsagesGoods);
                        }
                    }
                    info.goods_usages_goods = goods_usages_goods;
                }

                infos.add(info);
            }
        }
        return  infos;
    }
}
