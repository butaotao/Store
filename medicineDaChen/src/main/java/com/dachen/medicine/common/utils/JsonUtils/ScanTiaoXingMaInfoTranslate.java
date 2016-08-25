package com.dachen.medicine.common.utils.JsonUtils;

import android.text.TextUtils;

import com.dachen.medicine.entity.CdrugRecipeitem;
import com.dachen.medicine.entity.ScanTiaoXingMaInfo;
import com.dachen.medicine.entity.ScanTiaoXingma;

/**
 * Created by Burt on 2016/6/6.
 */
public class ScanTiaoXingMaInfoTranslate {
    public static CdrugRecipeitem getTiaoXingMaInfo(ScanTiaoXingma info){//
        CdrugRecipeitem item = new CdrugRecipeitem();
        item.trade_name = info.tradeName;
        item.general_name = info.generalName;
        CdrugRecipeitem.Unit unit = item.new Unit();
        unit.name = info.packUnitText;
        item.unit = unit;
        item.manufacturer = info.manufacturer;
        item.pack_specification = info.packSpecification;
        item.id = info.id;
        if (!TextUtils.isEmpty(info.isExists)&&info.isExists.equals("1")){
            item.is_exists = "true";
        }else {
            item.is_exists = "false";
        }
        return  item;
    }
}
