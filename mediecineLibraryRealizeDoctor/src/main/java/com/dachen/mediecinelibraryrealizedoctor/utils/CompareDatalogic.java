package com.dachen.mediecinelibraryrealizedoctor.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.widget.TextView;


/**
 * Created by Burt on 2016/1/21.
 */
public class CompareDatalogic {

    public static String getShowName(String generanName,String tradeName,String goodsName/*,TextView view*/) {

        String name = "";
        if (!TextUtils.isEmpty(goodsName)) {
            name = goodsName;
        } else if (!TextUtils.isEmpty(generanName)) {
            name = generanName;
        } else if (!TextUtils.isEmpty(tradeName)) {
            name = tradeName;
        }
      /*  view.setText(name);
        if (view.getText().toString().endsWith("...")) {
            name = name.substring(0, name.length() - 7) + "...    ";

        }*/
      /*  if (null != name && name.length() > 9) {
            name = name.substring(0, 6) + "..."
                    + name.substring(name.length() - 2);
        }*/
            return name;
    }
    public static String/*SpannableString*/ getShowName2(String generanName,String tradeName,String goodsName,TextView view,String unit,Context context) {

        String name = "";
        if (!TextUtils.isEmpty(goodsName)) {
            name = goodsName;
        } else if (!TextUtils.isEmpty(generanName)) {
            name = generanName;
        } else if (!TextUtils.isEmpty(tradeName)) {
            name = tradeName;
        }else {
            return "";
        }
     /*  view.setText(name);
        if (view.getText().toString().endsWith("...")) {
            name = name.substring(0, name.length() - 7) + "...    ";

        }
        SpannableString s =com.dachen.mediecinelibraryrealizedoctor.utils.
                StringUtils.getStringDifSize(name,unit,context);*/
       // return s;
        return  name;
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
        if (view.getText().toString().endsWith("...")) {
            name = name.substring(0, name.length() - 15) + "...    ";

        }
        SpannableString s =com.dachen.mediecinelibraryrealizedoctor.utils.
                StringUtils.getStringDifSize(name,unit,context);
        return s;
    }
}
