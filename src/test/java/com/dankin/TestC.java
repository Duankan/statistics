package com.dankin;

import com.dankin.utils.PhantomTools;
import org.junit.Test;

public class TestC {
    @Test
    public void tt() {
        PhantomTools phantomTools = new PhantomTools(111,"600px*500px");
        try {
            phantomTools.getByteImg("http://192.168.1.6:8080/wms.html");
        } catch (Exception e) {

        }
    }
}
