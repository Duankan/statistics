package com.dankin;

import com.dankin.utils.HttpUtils;
import com.dankin.utils.PhantomTools;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.omg.CORBA.PUBLIC_MEMBER;

public class TestC {
    @Test
    public void tt() {
        PhantomTools phantomTools = new PhantomTools(
                1001,
                "600px*500px",
                "G:/dankin0814/tool/apache-tomcat-8.5.29-windows-x86/apache-tomcat-8.5.29/webapps/ROOT/",
                "G:/phantomjs-1.9.2-windows/phantomjs G:/phantomjs-1.9.2-windows/rasterize.js ");
        try {
            phantomTools.getByteImg("http://192.168.1.86:8079/wms.html");
        } catch (Exception e) {

        }
    }

    public void transferArcgisService() {
        String param = "{\n" +
                "\"where\":\"1=1\",\n" +
                "\"text\":\"\", \n" +
                "\"objectIds\":\"\", \n" +
                "\"time\":\"\", \n" +
                "\"geometry\":\"\", \n" +
                "\"geometryType\":\"esriGeometryEnvelope\",\n" +
                "\"inSR\":\"4490\", \n" +
                "\"spatialRel\":\"esriSpatialRelIntersects\",\n" +
                "\"relationParam\":\"\", \n" +
                "\"outFields\":\"*\", \n" +
                "\"returnGeometry\":true,\n" +
                "\"maxAllowableOffset\":\"\", \n" +
                "\"geometryPrecision\":\"\",\n" +
                "\"outSR\":\"4490\", \n" +
                "\"returnIdsOnly\":false,\n" +
                "\"returnCountOnly\":false,\n" +
                "\"orderByFields\":\"\",\n" +
                "\"groupByFieldsForStatistics\":\"\", \n" +
                "\"outStatistics\":\"\", \n" +
                "\"returnZ\":false,\n" +
                "\"returnM\":false,\n" +
                "\"gdbVersion\":\"\",\n" +
                "\"returnDistinctValues\":false,\n" +
                "\"f\":\"pjson\"\n" +
                "}";
        JSONObject object=JSONObject.fromObject(param);
        HttpUtils httpUtils=new HttpUtils();
        try {
            httpUtils.doPost("http://localhost:8060/convert/toGeojson",object,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
