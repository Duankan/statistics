package com.dankin.base;

import java.io.Serializable;

/**
 * @author dankin
 * @date 2019-02-28
 * @descr 地类和权属实体
 */
public class Entity implements Serializable{
    private String name;
    private String area;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
