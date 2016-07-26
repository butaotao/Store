package com.dachen.medicine.common.utils;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by Burt on 2016/1/12.
 */
public class TextUtils {
    private Spanned getTimeSpan(String num,String money) {
        Spanned dTimeStr;
        dTimeStr = Html.fromHtml(
                "<font color=\"#aaaaaa\">" + "售出" + "</font>"
                        + "<font color=\"#333333\">" + num + "</font>"
                        + "<font color=\"#aaaaaa\">" + "盒    " + "</font>"
                        + "<font color=\"#ff9d6a\">" + money + "</font>"
                        + "<font color=\"#aaaaaa\">" + " 元" + "</font>");
        return dTimeStr;

    }
}
