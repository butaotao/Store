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
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public static Spanned getNotSignAlert( ) {
        Spanned dTimeStr;
        dTimeStr = Html.fromHtml(
                "<font color=\"#444444\">" + "亲,昨天没有下班记录," + "</font>"
                        + "<font color=\"#3db4ff\">是忘记下班签到了么?"  +  "</font>"
                         );
        return dTimeStr;
    }
    public static Spanned getMettingVistPassword(String  des1,String des2,String des3,String des4,String des5) {
        Spanned dTimeStr;
        dTimeStr = Html.fromHtml(
                          "<font color=\"#333333\">" + des1 + "</font>"+"<br/>"
                         +"<font color=\"#333333\">" + des2  +  "</font>"+"<br/>"
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
        if (content.toLowerCase().contains(searchkeyword.toLowerCase())){

            int beginlocation = content.toLowerCase().indexOf(searchkeyword.toLowerCase());
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
    public static Spanned showkeywordContent(String content,String searchkeyword,Context context,CompanyContactListEntity people) {
        content = people.allpinyin.trim();
        SpannableString ss = new SpannableString(content);
        searchkeyword = searchkeyword.toLowerCase();
        ForegroundColorSpan coloSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.blue));
        ForegroundColorSpan coloSpannormal = new ForegroundColorSpan(context.getResources().getColor(R.color.color_333333));
        String[] simplepinyin = content.split(" ");
        if (simplepinyin.length==0){
            simplepinyin = new String[]{content};
        }
        ArrayList<Data> s =new ArrayList<>();
        if (content.contains(searchkeyword)){
            for(int i=0;i<searchkeyword.length();i++){
                String s3 = searchkeyword.charAt(i)+"";
                HtmlTextViewEdit edit = new HtmlTextViewEdit();
                Data data = edit.new Data();
                boolean find = false;
                for (int j =i;j<simplepinyin.length;j++){
                    String sp = simplepinyin[j].charAt(0)+"";
                    if ((sp).equals(s3)){
                        find = true;

                        int length = 0;
                            for(int k=1;k<=j;k++){
                               String s1 = simplepinyin[k-1];
                               length+=s1.length()+1;
                            }
                        if (j ==0){
                            data.begindata = 0;
                            data.enddata = 1;
                        }else {
                            data.begindata = length;
                            if (length+1<=content.length()){
                                data.enddata = length+1;
                            }else {
                                data.enddata = length;
                            }
                        }
                        break;
                    }
                }
                if (find){
                    s.add(data);
                }
            }
            StringBuffer buffer = new StringBuffer();
                for (int m = 0;m<simplepinyin.length;m++){
                    if (m==0){
                        buffer.append(simplepinyin[m].replace(simplepinyin[m].charAt(0)+"",
                                (simplepinyin[m].charAt(0)+"").toUpperCase()));
                    }else {
                        buffer.append(" "+simplepinyin[m].replace(simplepinyin[m].charAt(0)+"",
                                (simplepinyin[m].charAt(0)+"").toUpperCase()));
                    }

                }
            content = buffer.toString();

            content = "("+content+")";
            ss = new SpannableString(content);
            ss.setSpan(coloSpannormal,  0,  content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            for (int m = 0;m<s.size();m++){
                ss.setSpan(coloSpan, s.get(m).begindata+1, s.get(m).enddata+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }else {
            content = "("+content+")";
            ss = new SpannableString(content);
            ss.setSpan(coloSpannormal, 0,content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }
    public class Data{
        public int begindata;
        public int enddata;
    }

}
