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
    @Value("${viewUrl}")
    private String viewUrl;
    @Value("${polygonSaveDir}")
    private String polygonSaveDir;
    @Value("${imageDir}")
    private String imageDir;
    @Value("${phantomjsDir}")
    private String phantomjsDir;
    @Value("${imageSize}")
    private String imageSize;
    @Value("${arcgis_JsonDir}")
    private String arcgis_JsonDir;
    @Value("${Arcgis_pageSize}")
    private String Arcgis_pageSize;

    public String getArcgis_pageSize() {
        return Arcgis_pageSize;
    }
    public void setArcgis_pageSize(String arcgis_pageSize) {
        Arcgis_pageSize = arcgis_pageSize;
    }
    public String getArcgis_JsonDir() {
        return arcgis_JsonDir;
    }
    public void setArcgis_JsonDir(String arcgis_JsonDir) {
        this.arcgis_JsonDir = arcgis_JsonDir;
    }
    public String getImageSize() {
        return imageSize;
    }
    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }
    public String getPhantomjsDir() {
        return phantomjsDir;
    }
    public void setPhantomjsDir(String phantomjsDir) {
        this.phantomjsDir = phantomjsDir;
    }
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
    public String getViewUrl() {
        return viewUrl;
    }
    public void setViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
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