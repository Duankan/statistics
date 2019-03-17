package com.dankin.utils;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dankin
 * @date 2019-02-26
 * @descr 参数验证(1.参数符合json格式 2.参数至少含有一个经纬度坐标)
 */
public class Verify {
    private static Logger logger = LoggerFactory.getLogger(Verify.class);

    public static Boolean legal(String param) {
        boolean legal = false;
        try {
            JSONObject object = (JSONObject) JSONObject.parse(param);
            String polygon = object.getString("polygon");
            String type = object.getString("type");
            if (StringUtils.isNotBlank(polygon) && StringUtils.isNotBlank(type)) {
                legal = true;
            }
            if (!"point".equals(type) && !"polygon".equals(type)) {
                legal = false;
            }
            //只要抛出异常，就说明参数格式不对
            if ("point".equals(type)) {
                String[] point = polygon.split(" ");
                if (point.length > 2 || point.length < 2) {
                    legal = false;
                    return legal;
                }
                for (int i = 0; i < point.length; i++) {
                    Double.parseDouble(point[i]);
                    legal = true;
                }
            }
            //只要抛出异常就参数不合格
//            if("polygon".equals(type)){
//                String[] points = polygon.split(",");
//                if(points.length<2){
//                    legal=false;
//                    return legal;
//                }
//                for (int i=0;i<points.length;i++){
//                    String[] point=points[i].split(" ");
//                    if(point.length!=2){
//                        legal=false;
//                        return legal;
//                    }
//                    else {
//                        Double.parseDouble(point[0]);
//                        Double.parseDouble(point[1]);
//                    }
//                }
//            }
        } catch (Exception e) {
            legal = false;
            logger.info("参数非法:" + e.getMessage());
        } finally {
            return legal;
        }
    }
}
