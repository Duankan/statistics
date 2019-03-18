package com.dankin;

import com.dankin.utils.PhantomTools;
import org.junit.Test;

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
}
