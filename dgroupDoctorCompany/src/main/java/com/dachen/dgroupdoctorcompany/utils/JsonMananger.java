package com.dachen.dgroupdoctorcompany.utils;

/**
 * Created by Burt on 2016/2/23.
 */
import java.util.List;

import org.apache.http.HttpException;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * [JSON解析管理类]
 *
 * @author huxinwu
 * @version 1.0
 * @date 2014-3-5
 *
 **/
public class JsonMananger {

    static{
        TypeUtils.compatibleWithJavaBean = true;
    }
    private static final String tag = JsonMananger.class.getSimpleName();

    /**
     * 将json字符串转换成java对象
     * @param json
     * @param cls
     * @return
     * @throws HttpException
     */
    public static <T> T jsonToBean(String json, Class<T> cls) {
        return JSON.parseObject(json, cls);
    }

    /**
     * 将json字符串转换成java List对象
     * @param json
     * @param cls
     * @return
     * @throws HttpException
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        return JSON.parseArray(json, cls);
    }

    /**
     * 将bean对象转化成json字符串
     * @param obj
     * @return
     * @throws HttpException
     */
    public static String beanToJson(Object obj) {
        String result = JSON.toJSONString(obj);
        Log.e(tag, "beanToJson: " + result);
        return result;
    }

}