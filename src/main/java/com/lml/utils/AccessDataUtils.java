package com.lml.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.lml.config.DataUrlProperties;
import com.lml.dto.JsonResult;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Author: leemonlin
 * @Date: 2023/10/29/13:26
 */

@Component
public class AccessDataUtils {

    public AccessDataUtils(DataUrlProperties dataUrlProperties){
        this.dataUrlProperties = dataUrlProperties;
    }

    private static final String ROOT_PATH = "src/main/resources/static/";

    private final DataUrlProperties dataUrlProperties;

    private static final Map<String, String> typeMap = new HashMap<>();

    static {
        typeMap.put("getBodyNodeAndRelation","body");
        typeMap.put("getInstanceNodeAndRelation","instance");
    }


    public JsonResult getData(String type, String siteID) {
        //数据获取接口地址
        String url = dataUrlProperties.getItemUrl()+type;
        //使用Restemplate来发送HTTP请求
        RestTemplate restTemplate = new RestTemplate();
        //请求方法
        HttpMethod method = HttpMethod.POST;

        HttpEntity httpEntity = getHttpEntity(siteID);
        try {
            //使用 exchange 发送请求(指定headers)，以String的类型接收返回的数据
            ResponseEntity<String> response = restTemplate.exchange(url, method, httpEntity, String.class);
            //解析返回的数据
            JSONObject data = JSONObject.parseObject(response.getBody());
            return data.getObject("result", JsonResult.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public JsonResult getDataOffline(String type, String siteID) {
        //数据获取接口地址
        JsonResult result;
        String fileString = ROOT_PATH + siteID + "/" + type + ".json";
        try(FileReader fileReader = new FileReader(fileString)){
            JSONReader jsonReader = new JSONReader(fileReader);
            JSONObject data = jsonReader.readObject(JSONObject.class);
            result = data.getObject("result", JsonResult.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static HttpEntity getHttpEntity(String siteID) {
//        LinkedMultiValueMap body = new LinkedMultiValueMap();
        //要求请求体为json格式
        JSONObject body = new JSONObject();
        body.put("siteId", siteID);
        //设置请求header 为 APPLICATION_FORM_URLENCODED
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "6");

        headers.add("Request-Origion","SwaggerBootstrapUi");

        headers.add("accept","*/*");

        headers.add("Content-Type","application/json");

        // 请求体，包括请求数据 body 和 请求头 headers
        return new HttpEntity(body,headers);
    }
}
