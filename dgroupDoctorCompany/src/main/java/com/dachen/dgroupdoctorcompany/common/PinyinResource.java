package com.dachen.dgroupdoctorcompany.common;

/**
 * Created by Burt on 2016/5/16.
 */

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 资源文件加载类
 *
 * @author stuxuhai (dczxxuhai@gmail.com)
 */
public final class PinyinResource {

    private PinyinResource() {}

    private static Map<String, String> getResource(String resourceName, Context context) {
        Map<String, String> map = new HashMap<String, String>();
        try {

            InputStream is = context.getAssets().open(resourceName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.trim().split("=");
                map.put(tokens[0], tokens[1]);
            }
            br.close();
        } catch (IOException e) {
            throw new PinyinException(e);
        }

        return map;
    }

    public static Map<String, String> getPinyinResource(Context context) {
        return getResource("pinyin.db", context);
    }

    public static Map<String, String> getMutilPinyinResource(Context context) {
        return getResource("mutil_pinyin.db", context);
    }

    public static Map<String, String> getChineseResource(Context context) {
        return getResource("chinese.db", context);
    }
}
