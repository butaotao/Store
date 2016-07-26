package com.dachen.medicine.logic;

/**
 * Created by Burt on 2016/1/26.
 */
public class SubString {

    public static String getMediedata(String ids){
        String id = ids;
        if (ids.length()>10) {
            id = ids.substring(0, 7) + "***" + ids.substring(ids.length() - 3);
            return id;
        }else {
            return  ids;
        }

    }

    public static String getTitle(String ids){
        String id = ids;
        if (ids.length()>10) {
            id = ids.substring(0, 8)+"..." ;
            return id;
        }
        return ids;
    }
}
