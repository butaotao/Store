package com.dachen.dgroupdoctorcompany.adapter;

/**
 * Created by Burt on 2016/5/16.
 */
/*
    ShengDao Android Client, sss
    Copyright (c) 2015 ShengDao Tech Company Limited
 */

import android.text.TextUtils;

import com.dachen.common.utils.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



import android.text.TextUtils;

import com.dachen.common.utils.Logger;
import com.dachen.dgroupdoctorcompany.common.PinyinHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * [A brief description]
 *
 * @author huxinwu
 * @version 1.0
 * @date 2015-8-14
 **/
public class GB2Alpha {
    //根据一个包含汉字的字符串返回一个汉字拼音首字母的字符串
    public String String2Alpha(String SourceStr) {
        String letter = "#";
        try {
            if (TextUtils.isEmpty(SourceStr)) {
                return "#";
            }

            if (!TextUtils.isEmpty(SourceStr) && SourceStr.length() > 0) {
                SourceStr = SourceStr.substring(0, 1);
                if (isNumeric(SourceStr)) {
                    return "#";
                }

            }

            letter = PinyinHelper.getShortPinyin(SourceStr);
            if (!isLetter(letter)) {
                return "#";
            }
            Logger.e("TAG", "----------letter-----------" + letter);

        } catch (Exception e) {

        }
        return letter.toLowerCase();
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public boolean isLetter(String letter) {
        Pattern pattern = Pattern.compile("[a-zA-Z]");
        Matcher isLetter = pattern.matcher(letter);
        if (isLetter.matches()) {
            return true;
        }
        return false;
    }

}
