package com.dankin;

import com.dankin.utils.PhantomTools;
import com.dankin.utils.ResourcesUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DankinApplicationTests {
    @Autowired
    ResourcesUtil resourcesUtil;

    @Test
    public void contextLoads() {
    }

    @Test
    public void GetImageFromHtml() {
        PhantomTools phantomTools = new PhantomTools(1001,"600px*500px",resourcesUtil.getImageDir());
        try {
            phantomTools.getByteImg("http://localhost:8060/html");
        }
        catch (Exception e){

        }
    }
}
