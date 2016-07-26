package com.dachen.medicine.common.utils.JsonUtils;

import android.text.TextUtils;

import com.dachen.medicine.common.utils.Drug;
import com.dachen.medicine.entity.CdrugRecipeitem;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/6/6.
 */
public class DrugListTranslate {
    public static ArrayList<CdrugRecipeitem> getRecipeitems(ArrayList<Drug>  receiptList){
        ArrayList<CdrugRecipeitem> c_drug_recipeitems = new ArrayList<>();
        if (receiptList!=null&&receiptList.size()>0){
            for (int i = 0;i<receiptList.size();i++){
                Drug drug = receiptList.get(i);
                CdrugRecipeitem item = new CdrugRecipeitem();
                item.bought_quantity = drug.boughtQuantity;
                item.unit.name = drug.packUnitText;
                item.unit.title = drug.packUnitText;
                item.general_name = drug.generalName;
                item.trade_name = drug.tradeName;
                if (drug.isOwn==0){
                    item.is_own  =true;
                }else {
                    item.is_own  =false;
                }
               /* if (!TextUtils.isEmpty(drug.state)&&drug.state.equals("2")){
                    item.is_own  =true;
                }else {
                    item.is_own  =false;
                }*/
                if (drug.isJoin==0){
                    item.is_join  =true;
                }else {
                    item.is_join  =false;
                }
                CdrugRecipeitem.State state = item.new State();
                state.title = drug.state;
                item.state = state;

                item.id = drug.goodsId;
                item.requires_quantity = drug.requireQuantity;
                item.specification = drug.specification;
                item.pack_specification = drug.packSpecification;
                CdrugRecipeitem.DataPatient patient = item.new DataPatient();
                CdrugRecipeitem.Data data = item.new Data();
                patient.num_syjf = drug.userPoint.leftPointsNum;
                if (drug.goodsPointRule!=null){
                    data.zsmdwypxhjfs = drug.goodsPointRule.lowPointsNum;
                    data.zyzdsxjfs  = drug.goodsPointRule.consumePointsNum;
                }
                item.manufacturer = drug.manufacturer;
                item.data = data;
                item.data1 = patient;//lastDrugStoreName
                item.lastDrugStoreName = drug.lastDrugStoreName;
                c_drug_recipeitems.add(item);
            }
        }
        return  c_drug_recipeitems;
    }
}
