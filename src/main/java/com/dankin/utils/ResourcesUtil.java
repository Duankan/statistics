package com.dankin.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "resource")  //配置文件中的前缀
@PropertySource(value = "classpath:app.properties")
public class ResourcesUtil {
    @Value("${gisurl}")
    private String gisurl;
    @Value("${typename}")
    private String typename;
    @Value("${groupField_dl}")
    private String groupField_dl;
    @Value("${groupField_qs}")
    private String groupField_qs;
    @Value("${calcfield}")
    private String calcfield;
    @Value("${wmsUrl}")
    private String wmsUrl;
    @Value("${viewUrl}")
    private String viewUrl;
    @Value("${dev}")
    private String dev;
    @Value("${polygonSaveDir}")
    private String polygonSaveDir;
    @Value("${imageDir}")
    private String imageDir;

    public String getImageDir() {
        return imageDir;
    }

    public void setImageDir(String imageDir) {
        this.imageDir = imageDir;
    }

    public String getPolygonSaveDir() {
        return polygonSaveDir;
    }

    public void setPolygonSaveDir(String polygonSaveDir) {
        this.polygonSaveDir = polygonSaveDir;
    }

    public String getDev() {
        return dev;
    }

    public void setDev(String dev) {
        this.dev = dev;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
    }

    public String getWmsUrl() {
        return wmsUrl;
    }
    public void setWmsUrl(String wmsUrl) {
        this.wmsUrl = wmsUrl;
    }

    public String getCalcfield() {
        return calcfield;
    }
    public void setCalcfield(String calcfield) {
        this.calcfield = calcfield;
    }
    public String getGroupField_dl() {
        return groupField_dl;
    }
    public void setGroupField_dl(String groupField_dl) {
        this.groupField_dl = groupField_dl;
    }
    public String getGroupField_qs() {
        return groupField_qs;
    }
    public void setGroupField_qs(String groupField_qs) {
        this.groupField_qs = groupField_qs;
    }
    public String getTypename() {
        return typename;
    }
    public void setTypename(String typename) {
        this.typename = typename;
    }
    public String getGisurl() {
        return gisurl;
    }
    public void setGisurl(String gisurl) {
        this.gisurl = gisurl;
    }
}