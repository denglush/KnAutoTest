package com.kn.httpclient;


import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.io.IOException;

public class HttpDemo {

    @Test(description = "查询接口")
    public void getDemo() throws IOException {
        String result;
        HttpGet httpGet = new HttpGet("http://test3caiwu.api.so/pay/withdraw-query");
        DefaultHttpClient client = new DefaultHttpClient();


        Reporter.log("调用代付查询接口/pay/withdraw-query");
        HttpResponse httpResponse = client.execute(httpGet);
        HttpEntity  entity  = httpResponse.getEntity();

        int code = httpResponse.getStatusLine().getStatusCode();
        System.out.println("响应code"+code);
         Reporter.log("响应code");

        result = EntityUtils.toString(entity);
        System.out.println("响应内容是"+result);
        Reporter.log("响应内容");


    }

    @Test
    public void postDemo() throws IOException {
        String result;
        // 定义client
        DefaultHttpClient client = new DefaultHttpClient();
        //设置post请求方法
        HttpPost httpPost = new HttpPost("http://test3caiwu.api.so/pay/withdraw-query");
        //定义post方法请求参数

        JsonObject json = new JsonObject();

        json.addProperty("trade_report_code","TEST-trade20180730-107");

        StringEntity entity = new StringEntity(json.toString());
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        //添加post请求方法参数

        httpPost.setEntity(entity);
        //获得请求响应信息
        HttpResponse httpResponse = client.execute(httpPost);
        HttpEntity  entity1 = httpResponse.getEntity();
        System.out.println("响应code="+httpResponse.getStatusLine().getStatusCode());
        Reporter.log("响应code"+httpResponse.getStatusLine().getStatusCode());

        result = EntityUtils.toString(httpResponse);
        System.out.println("响应内容+\n"+result);
        Reporter.log("响应内容");

    }
}
