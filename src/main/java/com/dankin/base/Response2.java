package com.dankin.base;

import java.io.Serializable;

/**
 * @author dankin
 * @date 2019-03-20
 * @desc arcgis控制器的响应类
 */
public class Response2<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 1L;
    private String code;
    private String msg;
    private boolean success;
    private T data;

    public Response2() {}
    public Response2(String code,String msg,boolean success,T data) {
        this.code=code;
        this.msg=msg;
        this.success=success;
        this.data=data;
    }
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
