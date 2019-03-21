package com.dankin.biz;

import com.dankin.utils.HttpUtils;
import com.dankin.utils.ResourcesUtil;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

/**
 * @author dankin
 * @date 2019-03-20
 * @desc arcgis转换biz处理类
 */
@Service
public class ConvertBiz {
    @Autowired
    HttpUtils httpUtils;
    @Autowired
    ResourcesUtil resourcesUtil;
    private boolean isLoaded = false;
    private int tatal = 0;
    /**
     * @param param
     * @return map{toatl:xx,objectIds:xxxxx}
     * @desc 获取总条数和所有的objectIds
     */
    public Map<String, Object> getTotalAndObjectIds(String param) throws Exception {
        int objectIds[] = null;
        Map<String, Object> result = new HashMap<>();
        JSONObject object = JSONObject.fromObject(param);
        String arcgisUrl = object.getString("url");
        object.remove("url");
        Set<String> keys = object.keySet();
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> heads = new HashMap<>();
        heads.put("Content-Type", "application/x-www-form-urlencoded");
        for (String key : keys) {
            params.put(key, object.getString(key));
        }
        //2.返回所有的objectids
        params.put("returnIdsOnly", true);
        String res_objectIds = httpUtils.doPost(arcgisUrl, params, heads);
        if (StringUtils.isNotBlank(res_objectIds)) {
            JSONObject jb2 = JSONObject.fromObject(res_objectIds);
            JSONArray array = jb2.getJSONArray("objectIds");
            objectIds = new int[array.size()];
            for (int i = 0; i < array.size(); i++) {
                objectIds[i] = (int) array.get(i);
            }
            result.put("count", array.size());
        }
        result.put("objectIds", objectIds);
        return result;
    }

    /**
     * @param map
     * @param param
     * @desc 获取geojson
     */
    public boolean getGeoJson(Map<String, Object> map, String param) throws Exception {
        //初始化
        isLoaded = false;
        int index = 0;//每次起始索引
        int pageSize = Integer.parseInt(resourcesUtil.getArcgis_pageSize());
        tatal = (int) map.get("count");//总条数
        int endIndex = index + pageSize > tatal ? tatal : index + pageSize;
        int objectIds[] = (int[]) map.get("objectIds");
        Map<String,Object> streams=openStream(param);
        if(streams!=null&&streams.get("os")!=null){
            while (!isLoaded) {
                String ids = "";
                for (int i = index; i < endIndex; i++) {
                    if (i != endIndex - 1) {
                        ids += String.valueOf(objectIds[i]) + ",";
                    } else {
                        ids += String.valueOf(objectIds[i]);
                    }
                }
                polling(ids, index, endIndex, param, (BufferedOutputStream) streams.get("bos"));
                index += pageSize;
                endIndex = endIndex + pageSize > tatal ? tatal : endIndex + pageSize;
            }
            closeStream(streams);
        }
        return true;
    }

    /**
     * @param cuurenObjectIds
     * @param cuurentEndIndex
     * @param index
     * @param param
     * @desc 轮询请求geojson
     */
    public void polling(String cuurenObjectIds, int index, int cuurentEndIndex, String param,BufferedOutputStream os) throws Exception {
        JSONObject object = JSONObject.fromObject(param);
        String arcgisUrl = object.getString("url");
        object.remove("url");
        Set<String> keys = object.keySet();
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> heads = new HashMap<>();
        heads.put("Content-Type", "application/x-www-form-urlencoded");
        for (String key : keys) {
            params.put(key, object.getString(key));
            params.put("objectIds", cuurenObjectIds);
        }
        String res = httpUtils.doPost(arcgisUrl, params, heads);
        String a = StringUtils.substringBetween(res, "features\": [\r\n", " ]\r\n}");//featrues 数组
        String all=null;
        if (index == 0) {
            String pre = StringUtils.substringBefore(res, "features");
            //只有一页记录
            if(cuurentEndIndex==tatal){
                all = pre + "features\": [\n" + a+" ]\r\n}";
            }
            else {
                all = pre + "features\": [\n" + a+",";
            }
        }
        if(index!=0&&cuurentEndIndex>=tatal){
            all=a+" ]\r\n}";
        }
        if(index!=0&&cuurentEndIndex<tatal){
            all=a+",";
        }
        saveGeojsonToLocal(all,os);
        if (cuurentEndIndex >= tatal) {
            isLoaded = true;
        }
    }

    /**
     * @param txt
     * @return void
     * @param  os
     * @desc 将geojson保存到本地文件
     */
    public void saveGeojsonToLocal(String txt,BufferedOutputStream os) throws Exception {
        //将数据写到本地
        byte[] data = txt.getBytes();
        os.write(data, 0, data.length);
        os.flush();
    }

    /**
     * @param param
     * @desc 开启流 文件存在先删除文件，以确保第一次追加文件没有内容
     * @return
     */
    public Map<String, Object> openStream(String param) {
        JSONObject object=JSONObject.fromObject(param);
        String suffi=StringUtils.substringBetween(object.getString("url"),"services/","/MapServer")+".json";
        Map<String,Object> streams=new HashMap<>();
        BufferedOutputStream bos=null;
        OutputStream os=null;
        try {
            File file = new File(resourcesUtil.getArcgis_JsonDir()+suffi);
            os= new FileOutputStream(file,true);
            if(file.exists()){
                file.delete();
            }
            bos = new BufferedOutputStream(os);
        } catch (Exception e) {
            e.printStackTrace();
        }
        streams.put("os",os);
        streams.put("bos",bos);
        return streams;
    }

    /**
     * @param streams
     * @desc 关闭流
     */
    public void closeStream(Map<String,Object> streams){
        try {
            if(streams!=null){
                if(streams.get("os")!=null){
                    OutputStream os= (OutputStream) streams.get("os");
                    os.close();
                }
                if(streams.get("bos")!=null){
                    BufferedOutputStream bos= (BufferedOutputStream) streams.get("bos");
                    bos.close();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("关闭流失败!");
        }
    }
}
