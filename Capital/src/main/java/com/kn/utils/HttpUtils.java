package com.kn.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.sf.json.JSONObject;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.testng.Reporter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpUtils {

    /**
     * get请求（无参数）
     * @param url
     * @return
     */
    public static String doGet(String url) {
        try {
            HttpClient client = new DefaultHttpClient();
            //发送get请求
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);

            // 请求发送成功，并得到响应
            if (response.getStatusLine().getStatusCode() == 0) {
                // 读取服务器返回过来的json字符串数据
                String strResult = EntityUtils.toString(response.getEntity());

                return strResult;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * get请求(用于key-value格式的参数)
     * @param url
     * @return
     */
    public static String doGet(String url,List<NameValuePair> list) {
        try {
            HttpClient client = new DefaultHttpClient();


            String s ;
            s = EntityUtils.toString(new UrlEncodedFormEntity(list,"utf-8"));

            System.out.println("键值对参数"+s);
            HttpGet request = new HttpGet(url+'?'+s);
            //发送get请求
            HttpResponse response = client.execute(request);

            // 请求发送成功，并得到响应
            if (response.getStatusLine().getStatusCode() == 200) {
                // 读取服务器返回过来的json字符串数据
                String strResult = EntityUtils.toString(response.getEntity());
                System.out.println("结果是"+strResult);
                return strResult;
            }else
                System.out.println("请求状态"+response.getStatusLine().getStatusCode()) ;

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }




    /**
     * post请求(用于key-value格式的参数)
     * @param url
     * @param list
     * @return
     */
    public static String doPost(String url, List<NameValuePair> list){

        BufferedReader in ;
        try {
            // 定义HttpClient
            HttpClient client = new DefaultHttpClient();
            // 实例化HTTP方法
            HttpPost request = new HttpPost();
            request.setURI(new URI(url));

            //设置参数
 //           List<NameValuePair> nvps = new ArrayList<>();
//            for (Object o : params.keySet()) {
//                String name = (String) o;
//                String value = String.valueOf(params.get(name));
//                nvps.add(new BasicNameValuePair(name, value));
//
//                //System.out.println(name +"-"+value);
//            }
            request.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));

            HttpResponse response = client.execute(request);
            int code = response.getStatusLine().getStatusCode();
            if(code == 200){	//请求成功
                in = new BufferedReader(new InputStreamReader(response.getEntity()
                        .getContent(),"utf-8"));
                StringBuilder sb = new StringBuilder();
                String line ;
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }

                in.close();

                return sb.toString();
            }
            else{	//
                System.out.println("状态码：" + code);
                return null;
            }
        }
        catch(Exception e){
            e.printStackTrace();

            return null;
        }
    }

    /**
     * post请求（用于请求json格式的参数）
     * @param url
     * @param json
     * @return
     */
    public static String doPost(String url, String json) throws Exception {

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
        //httpPost.setHeader("Accept", "application/json");
        //httpPost.setHeader("Content-Type", "application/json");

        System.out.println("请求url："+url);
        Reporter.log("请求url："+url);
        System.out.println("请求u参数："+json);
        Reporter.log("请求参数："+json);
         StringEntity entity = new StringEntity(json, "UTF-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
//        httpPostst.setEntity(json);
        HttpResponse response;
        String jsonString ="";


        try {
            Reporter.log("开始执行");
            response = httpclient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            System.out.println("请求状态："+state);
            Reporter.log("请求状态："+state);
            if (state == HttpStatus.SC_OK) {
            HttpEntity responseEntity = response.getEntity();
             jsonString = EntityUtils.toString(responseEntity);
            return jsonString;
             }
            else{
               // logger.error("请求返回:"+state+"("+url+")");
            }
        } catch (Exception e) {

        }
        return jsonString;

    }


    /**
     * 更新原始jsonObject请求数据
     * @param jsonObject
     * @param map
     */
    public static  void getJSONData (JSONObject jsonObject,Map map){

        Iterator<String> sIterator = jsonObject.keys();
        while(sIterator.hasNext()){


            // 获得key
            String key = sIterator.next();
            System.out.println("key值"+key);
            System.out.println(map.get(key));
            if(jsonObject.get(key) instanceof  JSONObject){
                getJSONData((JSONObject) jsonObject.get(key),map);
            }else{

                if(map.get(key) != null && map.get(key) !="" ){
                    jsonObject.put(key,map.get(key));
                    // 根据key获得value, value也可以是JSONObject,JSONArray,使用对应的参数接收即可
                    String value = jsonObject.getString(key);
                }
            }

        }
        System.out.println(jsonObject.toString());


    }


    public static String getDatafromFile(String Path) {

        BufferedReader reader = null;
        String laststr = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(Path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                laststr += tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return laststr;

    }



    /**
     * 解析json
     */
    public void handleResult(String result){

        JsonObject jsonObject = new JsonObject();
        JsonObject json =     jsonObject.getAsJsonObject(result);

        //        System.out.println(jsonObject.has("message"));
        //        System.out.println(jsonObject.getString("message"));
        String code = jsonObject.getAsString();
        String message = jsonObject.getAsString();
        /**
         * 进行结果验证
         * 注意asset对象字符类型,此处全为String类型
         */
        if (jsonObject.get("data") !=null && code.equals("0")){
            JsonArray array = jsonObject.getAsJsonArray("data");
            if (array.size() > 0){
                JsonObject data = array.getAsJsonObject();
                String su_id = data.getAsString();
                System.out.println("subjectId:" + su_id);
                //   Assert.assertEquals(subject_id,su_id);

            }else {
                //  Assert.assertEquals(hcode,code);
                System.out.println(code);
            }
        }
        else {
            // Assert.assertEquals(hcode,code);
        }
        System.out.println("message:" + message);

    }

}
