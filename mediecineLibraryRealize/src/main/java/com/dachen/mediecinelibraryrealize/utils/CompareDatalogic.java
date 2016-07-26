package com.dachen.mediecinelibraryrealize.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.widget.TextView;

import com.dachen.mediecinelibraryrealize.entity.PointCanExchanges;

/**
 * Created by Burt on 2016/1/21.
 */
public class CompareDatalogic {
    public static boolean isShow(PointCanExchanges.PointCanExchange media){
        if ( 0==media.num_syjf||0==media.zsmdwypxhjfs/*||0==media.zyzdsxjfs*/) {
            return false;
        } else if (
                media.num_syjf>=media.zyzdsxjfs
                &&media.num_syjf>=media.zsmdwypxhjfs) {
            return true;
        }
        return false;
    }
    public static boolean isShow(int num_syjf,int zyzdsxjfs,int zsmdwypxhjfs){
        if (0==num_syjf||0==zsmdwypxhjfs/*||0==zyzdsxjfs*/) {
            return false;
        } else if (
                num_syjf>=zyzdsxjfs
                &&num_syjf>=zsmdwypxhjfs) {
            return true;
        }
        return false;
    }
    public static int getShowNum(int num_syjf,int zyzdsxjfs,int zsmdwypxhjfs){
        if (0==num_syjf||0==zsmdwypxhjfs/*||0==zyzdsxjfs*/) {
            return 0;
        } else if (
                num_syjf>=zyzdsxjfs
                        &&num_syjf>=zsmdwypxhjfs) {
           int num = num_syjf / zyzdsxjfs;
            return num;
        }
        return 0;
    }
    public static String getShowName(String generanName,String tradeName,String goodsName){
        String name = "";


        if (!TextUtils.isEmpty(goodsName)) {
            name = goodsName;
        }else if(!TextUtils.isEmpty(generanName)){
            name = generanName;
        }else if(!TextUtils.isEmpty(tradeName)){
            name= tradeName;
        }
      /*  if (null != name && name.length() > 9) {
            name = name.substring(0, 6) + "..."
                    + name.substring(name.length() - 2);
        }*/
        return name;
    }
    public static String getShowName2(String generanName,String tradeName,String goodsName){
        String name = "";


        if (!TextUtils.isEmpty(goodsName)) {
            name = goodsName;
        }else if(!TextUtils.isEmpty(generanName)){
            name = generanName;
        }else if(!TextUtils.isEmpty(tradeName)){
            name= tradeName;
        }

        return name;
    }
    public static SpannableString getShowName3(String generanName,String tradeName,String goodsName,TextView view,String unit,Context context) {

        String name = "";
        if (!TextUtils.isEmpty(goodsName)) {
            name = goodsName;
        } else if (!TextUtils.isEmpty(generanName)) {
            name = generanName;
        } else if (!TextUtils.isEmpty(tradeName)) {
            name = tradeName;
        }
        view.setText(name);
        boolean flag = view.getText().toString().endsWith("...");
        if (flag||view.getText().length()>13) {
            name = name.substring(0, 10) + "... ";

        }
        SpannableString s =
                StringUtils.getStringDifSize(name,unit,context);
        return s;
    }
    public static SpannableString getShowName4(String generanName,String tradeName,String goodsName,TextView view,String unit,Context context) {

        String name = "";
        if (!TextUtils.isEmpty(goodsName)) {
            name = goodsName;
        } else if (!TextUtils.isEmpty(generanName)) {
            name = generanName;
        } else if (!TextUtils.isEmpty(tradeName)) {
            name = tradeName;
        }
        view.setText(name);
        if (view.getText().toString().endsWith("...")) {
            name = name.substring(0, name.length() - 15) + "...    ";

        }
        SpannableString s =
                StringUtils.getStringDifSize(name,unit,context);
        return s;
    }
}
