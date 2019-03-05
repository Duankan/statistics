package com.dankin.base;

import java.io.Serializable;

/**
 * @author dankin
 * @date 2019-02-26
 * @descr 返回给客户端的数据
 */
public class Response<T> implements Serializable {
    private String code;
    private String msg;
    private String imageUrl;//图片地址
    private T dl;//土地分类集合
    private T qs;//权属集合

    public Response() {
    }

    /**
     * @param code
     * @param msg
     * @descr 查询失败返回实体
     */
    public Response(String code,String msg){
        this.code=code;
        this.msg=msg;
    }

    /**
     * @param code
     * @param msg
     * @param dl
     * @param qs
     * @descr 查询成功返回实体
     */
    public Response(String code,String msg,String imageUrl,T dl,T qs){
        this.code=code;
        this.msg=msg;
        this.imageUrl=imageUrl;
        this.dl=dl;
        this.qs=qs;
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
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public T getDl() {
        return dl;
    }
    public void setDl(T dl) {
        this.dl = dl;
    }
    public T getQs() {
        return qs;
    }
    public void setQs(T qs) {
        this.qs = qs;
    }
}
