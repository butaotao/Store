package com.dachen.mediecinelibraryrealize.utils.JsonUtils;

import android.text.TextUtils;

import com.dachen.mediecinelibraryrealize.entity.Drugstorefens;
import com.dachen.mediecinelibraryrealize.entity.DrugstorefensDes;
import com.dachen.mediecinelibraryrealize.entity.RecomendData;
import com.dachen.mediecinelibraryrealize.entity.RecomendsGoodsList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Burt on 2016/6/6.
 */
public class BaiduMapDataTrans {
    public static ArrayList<Drugstorefens> drugstorefens(ArrayList<RecomendData> datas){
        ArrayList<Drugstorefens> fens = new ArrayList<>();
        if (null!=datas&&datas.size()>0) {
            for (int i = 0; i < datas.size(); i++) {
                Drugstorefens drugstorefens = new Drugstorefens();
                RecomendData data = datas.get(i);
                ArrayList<DrugstorefensDes.DrugList> list = new ArrayList<>();
                for (int j = 0; j < data.goodsList.size(); j++) {
                    RecomendsGoodsList goods = data.goodsList.get(j);
                    DrugstorefensDes des = new DrugstorefensDes();
                    DrugstorefensDes.Area area = des.new Area();
                    des.name = data.companyName;
                    des.sybz = data.deliveryNote;
                    area.name = data.area;
                    des.area = area;
                    DrugstorefensDes.DrugList drug = des.new DrugList();
                    drug.title = goods.title;
                    drug.pack_specification = goods.packSpecification;
                    drug.manufacturer = goods.manufacturer;
                    drug.specification = goods.specification;
                    if (!TextUtils.isEmpty(goods.isExchange) && goods.isExchange.equals("0")) {
                        drug.is_charge = true;
                    } else {
                        drug.is_charge = false;
                    }

                    DrugstorefensDes.DrugList.CertState state = drug.new CertState();
                    if (!TextUtils.isEmpty(goods.isCert) && goods.isCert.equals("0")) {
                        state.value = "9";
                    }
                    drug.cert_state = state;

                    list.add(drug);
                }
                drugstorefens.list = list;
                drugstorefens.sybz = data.deliveryNote;
                if (!TextUtils.isEmpty(data.doorService) && data.doorService.equals("1")) {
                    drugstorefens.zcsy = true;
                } else {
                    drugstorefens.zcsy = false;
                }
                drugstorefens.num_cert = data.ownGoodsCertNum;
                if (!TextUtils.isEmpty(data.medicalInsurance) && data.medicalInsurance.equals("1")) {
                    drugstorefens.zcyb = true;
                } else {
                    drugstorefens.zcyb = false;
                }
                if (!TextUtils.isEmpty(data.isExchange) && data.isExchange.equals("0")) {
                    drugstorefens.is_charge = true;
                } else {
                    drugstorefens.is_charge = false;
                }
                drugstorefens.num_on_stack = data.ownGoodsNum;
                drugstorefens.fen = data.pointsNum;
                drugstorefens.mm_str = data.distance;
                drugstorefens.name = data.companyName;
                drugstorefens.lxrsj = data.contactPhone;
                drugstorefens.longitude = data.longtitude;
                drugstorefens.latitude = data.latitude;
                drugstorefens.lxdz = data.address;
                drugstorefens.yysjd = data.businessHours;
                drugstorefens.area = data.area;
                fens.add(drugstorefens);
            }
        }
        return fens;
    }
}
