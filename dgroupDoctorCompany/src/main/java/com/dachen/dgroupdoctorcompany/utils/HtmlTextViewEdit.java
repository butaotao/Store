package com.dachen.dgroupdoctorcompany.utils;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by Burt on 2016/3/8.
 */
public class HtmlTextViewEdit {
    public static Spanned getTimeSpan( ) {
        Spanned dTimeStr;
        dTimeStr = Html.fromHtml(
                          "<font color=\"#aaaaaa\">" + "你的通讯录里还没有医生" + "</font>"+"<br/>"
                          + "<font color=\"#aaaaaa\">你可以点击右上角的"  +  "</font>"
                        + "<font color=\"#3cbaff\">" + "  +  " + "</font>"
                        + "<font color=\"#aaaaaa\">" + "添加" + "</font>" );
        return dTimeStr;
    }

    public static Spanned getMettingVistPassword(String  des1,String des2,String des3,String des4,String des5) {
        Spanned dTimeStr;
        dTimeStr = Html.fromHtml(
                "<font color=\"#333333\">" + des1 + "</font>"+"<br/>"
                         +"<font color=\"#333333\">"+des2  +  "</font>"+"<br/>"
                        + "<font color=\"#333333\">" + des3 + "</font>"+"<br/>"
                        + "<font color=\"#3cbaff\">" + des4 + "</font>"+"<br/>"
                        + "<font color=\"#333333\">" + des5 + "</font>");
        return dTimeStr;
    }
    public static Spanned getMettingVistLink(String  des1 ) {
        Spanned dTimeStr;
        dTimeStr = Html.fromHtml(
                         "<font color=\"#3cbaff\">" + des1 + "</font>"
                 );
        return dTimeStr;
    }
}
