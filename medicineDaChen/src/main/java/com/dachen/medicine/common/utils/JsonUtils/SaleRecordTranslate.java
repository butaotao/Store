package com.dachen.medicine.common.utils.JsonUtils;

import com.dachen.medicine.entity.Info;
import com.dachen.medicine.entity.SaleRecord;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/6/4.
 */
public class SaleRecordTranslate {
    public static ArrayList<Info> getInfoList(ArrayList<SaleRecord> pageData){
        ArrayList<Info> infos = new ArrayList<>();
        if (null!=pageData&&pageData.size()>0){
            for(int i=0;i<pageData.size();i++){
                Info info = new Info();
                SaleRecord record = pageData.get(i);
                info.datetime = record.salesTime;
                info.saleMonth = record.saleMonth;
                info.drug$general_name = record.generalName;
                info.drug$trade_name = record.tradeName;
                info.id = record.goodsId;
                info.quantity = record.quantity;
                Info.DrugUnit unit = info.new DrugUnit();
                unit.title = record.packUnitText;
                info.drug$unit = unit;
                Info.State state =info.new State();
                state.value = record.state;
                info.state = state;
                infos.add(info);
            }
        }

        return infos;
    }
}
