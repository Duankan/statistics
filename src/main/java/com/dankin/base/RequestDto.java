package com.dankin.base;


/**
 * @author dankin
 * @date 2019-02-27
 * @descr 请求体
 */
public class RequestDto {
    private String type;//传到后台的类型 {点，面}
    private String polygon;

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getPolygon() {
        return polygon;
    }
    public void setPolygon(String polygon) {
        this.polygon = polygon;
    }
}
