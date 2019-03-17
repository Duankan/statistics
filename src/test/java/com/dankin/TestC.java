package com.dankin;

import com.dankin.utils.PhantomTools;
import org.junit.Test;

public class TestC {
    @Test
    public void tt() {
        PhantomTools phantomTools = new PhantomTools(111,"600px*500px","C:/APP/apache-tomcat-8.5.31-windows-x64/apache-tomcat-8.5.31/webapps/ROOT/");
        try {
            phantomTools.getByteImg("http://192.168.1.6:8060/html");
        } catch (Exception e) {

        }
    }
}
