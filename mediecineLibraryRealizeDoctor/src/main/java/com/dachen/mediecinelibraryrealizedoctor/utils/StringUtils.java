package com.dachen.mediecinelibraryrealizedoctor.utils;

import android.content.Context;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.dachen.mediecinelibraryrealizedoctor.R;


/**
 * Created by Burt on 2016/3/11.
 */
public class StringUtils {
    public static int getNum(String str1){
        String str2="";
        if (TextUtils.isEmpty(str1)){
            return 0;
        }
        for (int i=0; i< str1.length();i++){
            if(Character.isDigit(str1.charAt(i))){
                return Integer.parseInt(str1.charAt(i)+"");
            }
        }
        return 0;

    }
    public static SpannableString getStringDifSize(String name,String unit,Context context){
        if (TextUtils.isEmpty(name)){
            name = " ";
        }
        if (TextUtils.isEmpty(unit)){
            unit = " ";
        }
        String text = name+" "+unit;
        SpannableString styledText = new SpannableString(text);
        styledText.setSpan(new TextAppearanceSpan(context, R.style.styletextsize3), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new TextAppearanceSpan(context, R.style.styletextsize4), name.length(), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      //  styledText = setForegroundColorSpan(styledText,context,0,name.length()-1,text.length()-1,R.color.black);
            return styledText;
    }
    public static SpannableString setForegroundColorSpan(SpannableString spanStr,Context context,
                                              int start,int end,int last,int colorResId){
        ForegroundColorSpan coloSpan = new ForegroundColorSpan(context.getResources().getColor(colorResId));
        spanStr.setSpan(coloSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(coloSpan, end+1, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
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
                StringUtils.getStringDifSize(name, unit, context);
        return s;
    }
    public static void get(final TextView view,final  String name, final String unit, final Context context){

        if (TextUtils.isEmpty(view.getText())||view.getText().equals("--")){
            view.setText(name);
        }
        Layout l = view.getLayout();
        if (l != null) {
            int lines = l.getLineCount();

            if (lines > 0) {
                int t = l.getEllipsisCount(lines - 1);
                String names = "";
                if (l.getEllipsisCount(lines - 1) >= 0) {

                    if ((t + 5) < (name.length() + 1)) {
                        names = name.substring(0, name.length() - (t + 3)) + "...  ";
                    } else {
                        names = name;
                    }
                } else {
                    names = name;
                }
                SpannableString s =
                        getStringDifSize(names, unit, context);
                view.setText(s);
            }

        }
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                String text = view.getText().toString();
                    //view.setText(name);

                    Layout l = view.getLayout();
                    if (l != null) {
                        int lines = l.getLineCount();

                        if (lines > 0) {
                            int t = l.getEllipsisCount(lines - 1);
                            String names = "";
                            if (l.getEllipsisCount(lines - 1) >= 0) {

                                if ((t + 5) < (name.length() + 1)) {
                                    names = name.substring(0, name.length() - (t + 3)) + "...  ";
                                } else {
                                    names = name;
                                }
                            } else {
                                names = name;
                            }
                            SpannableString s =
                                    getStringDifSize(names, unit, context);
                            view.setText(s);
                            // view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }

                }


            }
        });
        if (TextUtils.isEmpty(view.getText())||view.getText().equals("--")){
            view.setText(name);
        }
    }
}
