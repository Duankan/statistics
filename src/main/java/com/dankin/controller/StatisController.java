package com.dankin.controller;

import com.alibaba.fastjson.JSONObject;
import com.dankin.base.Entity;
import com.dankin.base.Response;
import com.dankin.base.ResponseEnum;
import com.dankin.biz.StatiscBiz;
import com.dankin.utils.HttpUtils;
import com.dankin.utils.Verify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author dankin
 * @date 2019-02-26
 * @descr 站立分析(点和面)
 */
@RestController
@RequestMapping("/")
public class StatisController {
    @Autowired
    public StatiscBiz statiscBiz;
    @Autowired
    public HttpUtils httpUtils;

    @CrossOrigin
    @RequestMapping("/statis")
    public Response analyze(@RequestBody String param,HttpServletRequest request){
        //1.简单验证
        boolean legal = Verify.legal(param);
        if (!legal) {
            return new Response(ResponseEnum.NOLEGAL.getCode(), ResponseEnum.NOLEGAL.getDisplayName());
        }
        String ip="http://"+request.getLocalAddr()+":"+String.valueOf(request.getLocalPort());
        //2.拼接wms的url与统计土地分类和权属的wfs的url
        String wmsurl = statiscBiz.appendWmsUrl(param);
        Map<String, String> urlAndParams_dl = statiscBiz.appnedWfsUrlAndParam(param, "dl");
        Map<String, String> urlAndParams_qs = statiscBiz.appnedWfsUrlAndParam(param, "qs");
        try {
            //3.请求wms
            String Imageres = httpUtils.doGetImage(wmsurl,ip);
            //4.请求wfs
            List<Map<String, Object>> l = HttpUtils.addHeadAndParam("application/x-www-form-urlencoded", JSONObject.parse(urlAndParams_dl.get("postParam")).toString());
            String dlres = httpUtils.doPost(urlAndParams_dl.get("wfsurl"), l.get(1), l.get(0));
            List<Entity> Dl_ls = statiscBiz.dealData(dlres, urlAndParams_dl);

            List<Map<String, Object>> l2 = HttpUtils.addHeadAndParam("application/x-www-form-urlencoded", JSONObject.parse(urlAndParams_qs.get("postParam")).toString());
            String qsres = httpUtils.doPost(urlAndParams_qs.get("wfsurl"), l2.get(1), l2.get(0));
            List<Entity> QS_ls = statiscBiz.dealData(qsres, urlAndParams_qs);
            return new Response(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getDisplayName(), Imageres, Dl_ls, QS_ls);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(ResponseEnum.FAIL.getCode(), ResponseEnum.FAIL.getDisplayName());
        }
    }
}
