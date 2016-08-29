package com.dachen.dgroupdoctorcompany.js;

/**
 * @author gzhuo
 * @date 2016/8/5
 */
public class JObjectResult<T> extends JResult {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
