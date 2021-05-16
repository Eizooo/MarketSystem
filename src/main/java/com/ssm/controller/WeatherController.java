package com.ssm.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ssm.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.HashMap;
import java.util.Map;

/**
 * @author Eizooo
 * @date 2021/5/13 17:54
 */
@Controller
public class WeatherController {
    private static final String appcode = "d5737f2af83b4b6283e3f52220262a71";

    @RequestMapping(value = "/weather")
    public String queryWeather(@RequestParam("city") String city, Model model){
        String cityWeather = getWeather(city);
        String result = JSONObject.parseObject(cityWeather).getJSONObject("result").toString();
        System.out.println(result);
        model.addAttribute("weather",result);
        return "weather";
    }
    public String getWeather(String cityName) {
        String cityWeather = null;
        String host = "https://jisutqybmf.market.alicloudapi.com";
        String path = "/weather/query";
        String method = "GET";//GET/POST 任意
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/json; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("city", cityName);
        querys.put("citycode", "citycode");
        querys.put("cityid", "cityid");
        querys.put("ip", "ip");
        querys.put("location", "location");

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity(),"utf-8"));
            cityWeather = EntityUtils.toString(response.getEntity(),"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cityWeather;
    }
}
