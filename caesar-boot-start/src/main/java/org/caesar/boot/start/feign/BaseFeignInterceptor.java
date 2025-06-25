package org.caesar.boot.start.feign;

import com.alibaba.fastjson.JSONObject;
import feign.RequestTemplate;
import lombok.extern.log4j.Log4j2;
import org.caesar.common.util.StringUtils;

import java.net.URL;


/**
 * @Description:
 * @Author: peng.guo
 * @Create: 2024-11-08 15:12
 * @Version 1.0
 **/
@Log4j2
public class BaseFeignInterceptor {


    public static JSONObject getParamsFromURL(String urlString) {
        JSONObject params = null;
        try {
            URL url = new URL("http://url" + urlString);
            String query = url.getQuery();
            if (query == null) {
                return null;
            }
            String[] pairs = query.split("&");
            if (pairs.length > 0) {
                params = new JSONObject();
            }
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                String key = keyValue[0];
                String value = keyValue[1];
                params.put(key, value);
            }
        } catch (Exception e) {
            log.error("Error parsing URL:{}", urlString, e);
            throw new RuntimeException("Invalid URL: " + urlString);
        }
        return params;
    }


    public static <T> T getBodyData(RequestTemplate template, Class<T> clazz) {
        String body = template.requestBody().asString();
        if (StringUtils.isBlank(body)) {
            return JSONObject.parseObject(body, clazz);
        }
        return null;
    }
}
