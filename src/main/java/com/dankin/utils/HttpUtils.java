package com.dankin.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * @author dankin
 * @date 2019-02-26
 * @descr http请求工具
 */
@Component
public class HttpUtils {
    private Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * @param url
     * @return body
     * @descr get请求 抛出异常让contoller获取
     */
    public String doGet(String url) throws Exception {
//        try {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);

        //将成功返回的信息转换为普通字符文本
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String strResult = EntityUtils.toString(response.getEntity(), "utf-8");
            return strResult;
        }
//        } catch (Exception e) {
//            logger.info(e.getMessage());
//        }
        return null;
    }

    /**
     * @param url
     * @param params
     * @param headers
     * @return body
     * @descr post请求 抛出异常让contoller获取
     */
    public String doPost(String url, Map params, Map headers) throws Exception {
        BufferedReader in = null;
//        try {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost();

        //添加头信息
        if (headers != null) {
            for (Iterator iter = headers.keySet().iterator(); iter.hasNext(); ) {
                String key = (String) iter.next();
                String value = String.valueOf(headers.get(key));
                httpPost.addHeader(key, value);
            }
        }
        httpPost.setURI(new URI(url));

        //设置参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (params != null) {
            for (Iterator iter = params.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String value = String.valueOf(params.get(name));
                nvps.add(new BasicNameValuePair(name, value));
            }
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        HttpResponse response = client.execute(httpPost);
        int code = response.getStatusLine().getStatusCode();
        if (code == HttpStatus.SC_OK) { //200：请求成功
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent(), "utf-8"));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            return sb.toString();
        }
        httpPost.releaseConnection();//手动释放连接
//        } catch (Exception e) {
//        e.printStackTrace();
//    }
        return null;
    }

    /**
     * @param url
     * @return 图片存储路径
     * @descr get请求返回图片
     */
    public String doGetImage(String url,String ip) throws Exception {
        InputStream in;
        String imageurl = null;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//        try {
        URL realUrl = new URL(url);
        // 打开和URL之间的连接
        URLConnection conn = getURLConnection(realUrl, "");
        // 建立实际的连接
        conn.connect();
        // 获取所有响应头字段
        Map<String, List<String>> map = conn.getHeaderFields();
        in = conn.getInputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = in.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        String basePath = ResourceUtils.getURL("classpath:").getPath();
//        G:\\dankin0814\\tool\\apache-tomcat-8.5.29-windows-x86\\apache-tomcat-8.5.29\\webapps
        if (basePath.contains("webapps") || basePath.contains("ROOT")) {
            basePath= StringUtils.substringBefore(basePath,"webapps")+"webapps\\ROOT\\";
        }
        String filename = String.valueOf(System.currentTimeMillis()) + ".png";
        File desc = new File(basePath + filename);
        FileOutputStream out = new FileOutputStream(desc);
        imageurl = ip+"/"+filename;
        out.write(outStream.toByteArray());
        out.close();
//        } catch (Exception e) {
//            System.out.println("发送GET请求出现异常！" + e);
//            e.printStackTrace();
//        } finally {
//        }
        return imageurl;
    }

    public static URLConnection getURLConnection(URL realUrl, String contentType) throws IOException {
        URLConnection conn = realUrl.openConnection();
        // 设置通用的请求属性
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        if (contentType.isEmpty())
            conn.setRequestProperty("Content-type", "application/json;charset=UTF-8");
        else
            conn.setRequestProperty("Content-type", contentType + ";charset=UTF-8");
        conn.setRequestProperty("User-Operation-Info", "a3UjjlaLC9He");
        conn.setRequestProperty("Authorization", "Basic YWRtaW46Z2Vvc2VydmVy");
        return conn;
    }

    /**
     * @param heads
     * @param params
     * @return 请求头与请求参的List
     */
    public static List<Map<String, Object>> addHeadAndParam(String heads, String params) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> head = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        head.put("Content-Type", heads);
        param.put("statistics", params);
        list.add(head);
        list.add(param);
        return list;
    }
}
