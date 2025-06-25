package org.caesar.media.utils;

import com.microsoft.playwright.options.Cookie;
import org.caesar.common.util.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 请求参数和 Cookie 处理工具类
 * 提供 Cookie 转换、查询参数构建等常用方法
 *
 * @author peng
 */
public class RequestParametersUtils {

    /**
     * 将 Cookie 列表转换为 Map 格式，方便查找和操作
     *
     * @param cookies Playwright Cookie 列表
     * @return Map 格式的 Cookie（键为 name，值为 value）
     */
    public static Map<String, String> convertCookiesToMap(List<Cookie> cookies) {
        Map<String, String> cookieMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.name, cookie.value);
            }
        }
        return cookieMap;
    }

    /**
     * 将 Cookie 列表转换为标准的 Cookie 字符串（用于请求头）
     * 格式示例：name1=value1; name2=value2
     *
     * @param cookies Playwright Cookie 列表
     * @return 拼接好的 Cookie 字符串
     */
    public static String convertCookiesToString(List<Cookie> cookies) {
        StringJoiner joiner = new StringJoiner("; ");
        if (!CollectionUtils.isEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                joiner.add(cookie.name + "=" + cookie.value);
            }
        }
        return joiner.toString();
    }

    /**
     * 将查询参数 Map 构建为 URL 查询字符串，进行 URL 编码
     * 格式示例：key1=value1&key2=value2
     *
     * @param params 查询参数 Map
     * @return URL 编码后的查询字符串
     */
    public static String buildQueryString(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }

        StringJoiner joiner = new StringJoiner("&");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            try {
                if (StringUtils.isNoneBlank(entry.getKey()) && StringUtils.isNoneBlank(entry.getValue())) {
                    String encodedKey = URLEncoder.encode(entry.getKey(), "UTF-8");
                    String encodedValue = URLEncoder.encode(entry.getValue(), "UTF-8");
                    joiner.add(encodedKey + "=" + encodedValue);
                }
            } catch (UnsupportedEncodingException e) {
                // UTF-8 是标准编码，正常不会抛异常
                throw new RuntimeException("URL encoding failed", e);
            }
        }

        return joiner.toString();
    }

}
