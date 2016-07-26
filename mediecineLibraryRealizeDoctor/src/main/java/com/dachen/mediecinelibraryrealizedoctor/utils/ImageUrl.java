package com.dachen.mediecinelibraryrealizedoctor.utils;

import com.dachen.medicine.common.utils.ImageUtil;
import com.dachen.medicine.common.utils.MedicineApplication;

import java.util.HashMap;

/**
 * Created by Burt on 2016/2/25.
 */
public class ImageUrl {
    public static String getUrl(String url){
        HashMap<String, String> maps = MedicineApplication.getMapConfig();
        String urls = url;
        if (!url.contains("/web/files/")) {
            urls = maps.get("ip")+"/web/files/"+url+ "?a="+maps.get("session");
        }else {
            urls = maps.get("ip")+"/"+url+ "?a="+maps.get("session");
        }
        return ImageUtil.getEncodeUrl(urls);
        //return Constants.MEDIC_RUL_CODE+url + "?a="+UserSp.getInstance(DApplication.getInstance()).getAccessToken("");
    }
}
