package com.dachen.dgroupdoctorcompany.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.view.View;

import com.dachen.dgroupdoctorcompany.R;

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
    public static Spanned showkeywordContent(String content,String searchkeyword,Context context) {
        SpannableString ss = new SpannableString(content);
        //  String s = "您当前未能找到任何分管医院，请先“添加分管医院”，再添加医生";
        // String s = "当前没有可添加的医生，原因可能是您分管的医院目前暂无医生，或者暂未设置分管的医院，请“检查分管医院”是否已经正确设置";
        ForegroundColorSpan coloSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.blue));
        ForegroundColorSpan coloSpannormal = new ForegroundColorSpan(context.getResources().getColor(R.color.color_333333));
        if (content.contains(searchkeyword)){

            int beginlocation = content.indexOf(searchkeyword);
            int endlocation = beginlocation+searchkeyword.length();
             ss = new SpannableString(content);
            ss.setSpan(coloSpannormal, 0,beginlocation, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(coloSpan, beginlocation, endlocation, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(coloSpannormal, endlocation, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else {
            ss.setSpan(coloSpannormal, 0,content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }


        return ss;
    }

}
