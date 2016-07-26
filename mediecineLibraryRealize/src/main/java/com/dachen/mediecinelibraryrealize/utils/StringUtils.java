package com.dachen.mediecinelibraryrealize.utils;

import android.content.Context;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.dachen.mediecinelibraryrealize.R;

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
    public static SpannableString getStringDifSize(String s,Context context){
        SpannableString styledText = new SpannableString(s);
        styledText.setSpan(new TextAppearanceSpan(context, R.style.styletextsize1), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new TextAppearanceSpan(context, R.style.styletextsize2), 5, s.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return styledText;
    }
    public static SpannableString getStringDifSizeCare(String s,int lenth,Context context){
        SpannableString styledText = new SpannableString(s);
        styledText.setSpan(new TextAppearanceSpan(context, R.style.styletextsize7), 0, lenth, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new TextAppearanceSpan(context, R.style.styletextsize6), lenth, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return styledText;
    }
    public static SpannableString getStringDifSize(String name,String unit,Context context){
        if (TextUtils.isEmpty(name)){
            name = " ";
        }
        if (TextUtils.isEmpty(unit)){
            unit = " ";
        }
        String text = name+unit;
        SpannableString styledText = new SpannableString(text);
        styledText.setSpan(new TextAppearanceSpan(context, R.style.styletextsize3), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new TextAppearanceSpan(context, R.style.styletextsize4), name.length(), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //  styledText = setForegroundColorSpan(styledText,context,0,name.length()-1,text.length()-1,R.color.black);
        return styledText;
    }
    public static void get(final TextView view,final  String name, final String unit, final Context context){


        view.setText(name);
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Layout l = view.getLayout();
                if (l != null) {
                    int lines = l.getLineCount();

                    if (lines > 0) {
                        int t =	l.getEllipsisCount(lines - 1);
                        String names = "";
                        if (l.getEllipsisCount(lines - 1) > 0) {
                            String viewText = view.getText().toString();

                            if ((t+7)<(name.length()+1)){
                                names = name.substring(0, name.length() - (t+10)) + "...    ";
                            }else {
                                names = name;
                            }


                        } else {
                            names = name;
                        }
                        SpannableString s =
                                getStringDifSize(names,unit,context);
                        view.setText(s);
                    }

                }
            }
        });



    }
}
