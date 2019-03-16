package com.dankin.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dankin.base.Entity;
import com.dankin.utils.ResourcesUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dankin
 * @date 2019-02-27
 * @descr 统计分析的业务处理类
 */
@Service
public class StatiscBiz {
    @Autowired
    private ResourcesUtil resourcesUtil;

    /**
     * @param polygon
     * @param type    point,polygon
     * @return bbox
     * @descr 根据点或面计算bbox
     */
    public String calcBbox(String polygon, String type) {
        String bbox = null;
        String maxX,minX,maxY,minY;
        //点缓冲200m
        if ("point".equals(type)) {
            double unitY = 0.001801801802;//200m缓冲的偏移纬度
            double unitX = 0.002342194636;//200m缓冲的偏移经度
            maxY = String.valueOf(Double.parseDouble(polygon.split(" ")[0]) + unitY);
            minY = String.valueOf(Double.parseDouble(polygon.split(" ")[0]) - unitY);
            maxX = String.valueOf(Double.parseDouble(polygon.split(" ")[1]) + unitX);
            minX = String.valueOf(Double.parseDouble(polygon.split(" ")[1]) - unitX);
        } else {
            String[] poits = polygon.split(",");
            double[] xArray = new double[poits.length];
            double[] yArray = new double[poits.length];
            for (int i = 0; i < poits.length; i++) {
                String[] xys = poits[i].split(" ");
                xArray[i] = Double.parseDouble(xys[1]);
                yArray[i] = Double.parseDouble(xys[0]);
            }
            //冒泡排序计算出x,y的最大最小值
            for (int i = 0; i < xArray.length; i++) {
                for (int j = 0; j < xArray.length - 1 - i; j++) {
                    if (xArray[j] > xArray[j + 1]) {
                        double temp = xArray[j];
                        xArray[j] = xArray[j + 1];
                        xArray[j + 1] = temp;
                    }
                }
            }
            for (int i = 0; i < yArray.length; i++) {
                for (int j = 0; j < yArray.length - 1 - i; j++) {
                    if (yArray[j] > yArray[j + 1]) {
                        double temp = yArray[j];
                        yArray[j] = yArray[j + 1];
                        ;
                        yArray[j + 1] = temp;
                    }
                }
            }
            maxX = String.valueOf(xArray[xArray.length - 1]);
            minX = String.valueOf(xArray[0]);
            maxY = String.valueOf(yArray[yArray.length - 1]);
            minY = String.valueOf(yArray[0]);
        }
        bbox = minY + "," + minX + "," + maxY + "," + maxX;
        return bbox;
    }

    /**
     * @param param
     * @return wmsurl
     * @descr 拼接wms的url地址
     */
    public String appendWmsUrl(String param) {
        JSONObject object = (JSONObject) JSONObject.parse(param);
        //1.计算bbox
        String polygon = object.getString("polygon");
        String type = object.getString("type");
        String bbox = calcBbox(polygon, type);
//        bbox = "104.073486328125,27.388916015625,119.893798828125,35.70556640625";
        String gisurl = resourcesUtil.getGisurl();
        String typename = resourcesUtil.getTypename();
        String newGisUrl=resourcesUtil.getWmsUrl();
        String wmsurl = newGisUrl + "?service=WMS&request=GetMap&version=1.1.1&layers=" + typename + "&styles=&format=image/png&transparent=true&id=tipicLayercity&pane=[object%20HTMLDivElement]&srs=EPSG:4326&width=800&height=600&bbox=" + bbox;
        return wmsurl;
    }


    /**
     * @param param
     * @param type
     * @return map["url":"","param",""]
     * @descr 拼接wfs的Url和请求参
     */
    public Map appnedWfsUrlAndParam(String param, String type) {
        Map<String, String> map = new HashMap<>();
        JSONObject object = (JSONObject) JSONObject.parse(param);
        String ponitOrPolygon = object.getString("type");
        String polygon = object.getString("polygon");
        if ("point".equals(ponitOrPolygon)) {
            polygon = "POINT(" + polygon + ")";
        } else {
            polygon = "POLYGON((" + polygon + "))";
        }
        String gisurl = resourcesUtil.getGisurl();
        String typename = resourcesUtil.getTypename();
        String field = resourcesUtil.getCalcfield();
        String groupFields = null;
        String wfsurl = gisurl + "?request=aggregate&service=wps";
        switch (type) {
            case "dl":
                groupFields = resourcesUtil.getGroupField_dl();
                break;
            case "qs":
                groupFields = resourcesUtil.getGroupField_qs();
                break;
        }
        String postParam = "{" +
                "\"url\":" + "\"" + gisurl + "\"," +
                "\"typename\":" + "\"" + typename + "\"," +
                "\"groupFields\":" + "\"" + groupFields + "\"," +
                "\"statisticsFields\":[{\"operate\":\"sum\",\"field\":" + "\"" + field + "\"}],\n" +
                "\"clip\":\"0\",\n" +
                "\"wkt\":" + "\"" + polygon + "\"}";
        map.put("wfsurl", wfsurl);
        map.put("postParam", postParam);
        map.put("groupFields", groupFields);
        map.put("field", field);
        return map;
    }

    /**
     * @param res
     * @param urlAndParams
     * @return list<Entity>
     * @descr 处理数据格式
     */
    public List dealData(String res, Map<String, String> urlAndParams) {
        List<Entity> ls = new ArrayList<>();
        if (!StringUtils.isEmpty(res)) {
            JSONArray array = JSONArray.parseArray(res);
            for (int i = 0; i < array.size(); i++) {
                JSONObject jb = array.getJSONObject(i);
                Entity entity = new Entity();
                entity.setName(jb.getString(urlAndParams.get("groupFields")));
                entity.setArea(jb.getString("SUM_" + urlAndParams.get("field")));
                ls.add(entity);
            }
        }
        return ls;
    }
}

