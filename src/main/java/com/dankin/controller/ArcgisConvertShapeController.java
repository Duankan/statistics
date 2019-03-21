package com.dankin.controller;

import com.dankin.base.Response2;
import com.dankin.base.ResponseEnum;
import com.dankin.biz.ConvertBiz;
import com.dankin.utils.HttpUtils;
import com.dankin.utils.ResourcesUtil;
import com.dankin.utils.Verify;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * @author dankin
 * @date 2019-03-20
 * @desc 将arcgis服务的json地址保存到本地文件，以便转化为shape文件
 *       1.首先获取所有的objectIds,保存下来
 *       2.轮询请求每页的信息，然后将信息拼接到文件
 */
@RestController
@RequestMapping("/convert")
public class ArcgisConvertShapeController {
    @Autowired
    HttpUtils httpUtils;
    @Autowired
    ResourcesUtil resourcesUtil;
    @Autowired
    ConvertBiz convertBiz;
    @RequestMapping("/toGeojson")
    public Response2 convertToGeoJson(@RequestBody String param){
        String path=null;
        if(!Verify.validateArcgis(param)){
            return new Response2(ResponseEnum.NOLEGAL.getCode(),ResponseEnum.NOLEGAL.getDisplayName(),false,null);
        }
        try {
            net.sf.json.JSONObject object= net.sf.json.JSONObject.fromObject(param);
            path= resourcesUtil.getArcgis_JsonDir()+StringUtils.substringBetween(object.getString("url"),"services/","/MapServer")+".json";
            Map<String,Object> maps=convertBiz.getTotalAndObjectIds(param);
            if(maps!=null&&maps.get("count")!=null&&maps.get("objectIds")!=null){
                convertBiz.getGeoJson(maps,param);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new Response2(ResponseEnum.FAILCONVERT.getCode(),ResponseEnum.FAILCONVERT.getDisplayName(),false,null);
        }
        return new Response2(ResponseEnum.SUCCESS.getCode(),ResponseEnum.SUCCESS.getDisplayName(),true,path);
    }
}
