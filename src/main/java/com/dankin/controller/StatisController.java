package com.dankin.controller;

import com.alibaba.fastjson.JSONObject;
import com.dankin.base.Entity;
import com.dankin.base.Response;
import com.dankin.base.ResponseEnum;
import com.dankin.biz.StatiscBiz;
import com.dankin.utils.HttpUtils;
import com.dankin.utils.PhantomTools;
import com.dankin.utils.ResourcesUtil;
import com.dankin.utils.Verify;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author dankin
 * @date 2019-02-26
 * @desc 站立分析(点和面)
 */
@Controller
@RequestMapping("/")
public class StatisController {
    @Autowired
    public StatiscBiz statiscBiz;
    @Autowired
    public HttpUtils httpUtils;
    @Autowired
    public ResourcesUtil resourcesUtil;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @CrossOrigin
    @RequestMapping("/statis")
    @ResponseBody
    public Response analyze(@RequestBody String param, HttpServletRequest request) {
        //1.简单验证
        boolean legal = Verify.legal(param);
        if (!legal) {
            return new Response(ResponseEnum.NOLEGAL.getCode(), ResponseEnum.NOLEGAL.getDisplayName());
        }
        String ip = "http://" + request.getLocalAddr() + ":" + String.valueOf(request.getLocalPort());
        //2.拼接wms的url与统计土地分类和权属的wfs的url
        Map<String, String> urlAndParams_dl = statiscBiz.appnedWfsUrlAndParam(param, "dl");
        Map<String, String> urlAndParams_qs = statiscBiz.appnedWfsUrlAndParam(param, "qs");
        //保存polygon.json到tomcat服务器
        boolean isWrite = statiscBiz.SavePolygonToLocalTXT(param);
        if (!isWrite) {
            return new Response(ResponseEnum.SAVEPOLYFAIL.getCode(), ResponseEnum.SAVEPNGFAIL.getDisplayName());
        }
        try {
            //phantomjs把绘制的图截屏保存到tomcat服务器
            PhantomTools phantomTools = new PhantomTools(1001, resourcesUtil.getImageSize(), resourcesUtil.getImageDir(), resourcesUtil.getPhantomjsDir());
            logger.info("请求页面地址>>>"+ resourcesUtil.getViewUrl());
            Map<String, Object> result = phantomTools.getByteImg(resourcesUtil.getViewUrl());
            if (result.get("ret") == null) {
                return new Response(ResponseEnum.SAVEPNGFAIL.getCode(), ResponseEnum.SAVEPNGFAIL.getDisplayName());
            }
            String path = ip + StringUtils.substringAfter((String) result.get("_file"), "ROOT");
            logger.info("图片保存地址>>>"+path);

            //4.请求wfs
            List<Map<String, Object>> l = HttpUtils.addHeadAndParam("application/x-www-form-urlencoded", JSONObject.parse(urlAndParams_dl.get("postParam")).toString());
            String dlres = httpUtils.doPost(urlAndParams_dl.get("wfsurl"), l.get(1), l.get(0));
            List<Entity> Dl_ls = statiscBiz.dealData(dlres, urlAndParams_dl);

            List<Map<String, Object>> l2 = HttpUtils.addHeadAndParam("application/x-www-form-urlencoded", JSONObject.parse(urlAndParams_qs.get("postParam")).toString());
            String qsres = httpUtils.doPost(urlAndParams_qs.get("wfsurl"), l2.get(1), l2.get(0));
            List<Entity> QS_ls = statiscBiz.dealData(qsres, urlAndParams_qs);
            return new Response(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getDisplayName(), path, Dl_ls, QS_ls);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(ResponseEnum.FAIL.getCode(), ResponseEnum.FAIL.getDisplayName());
        }
    }

    @RequestMapping("/html")
    public String html() {
        return "wms";
    }
}
