package org.caesar.media.utils;

import lombok.extern.log4j.Log4j2;
import org.caesar.common.util.StringUtils;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 抖音签名生成工具类，基于 GraalVM 执行 JS 文件，提供 a_bogus 签名与辅助参数计算方法。
 *
 * 依赖资源文件：resources/libs/douyin.js（包含 sign_reply, sign_datail 等函数）
 *
 * 使用场景：WebSocket 鉴权参数、直播拉流请求加密等
 *
 * @author peng
 */
@Log4j2
public class DouyinSignUtil {

    private static final String JS_FILE_PATH = "libs/douyin.js";
    private static final String AID = "6383";
    private static final Random RANDOM = new Random();

    private static final String douyinJsCode;

    public static final String FRONTIER_SIGN_STR = "stub => {" +
            "  if (window.byted_acrawler && typeof window.byted_acrawler.frontierSign === 'function') {" +
            "    return window.byted_acrawler.frontierSign({'X-MS-STUB': stub});" +
            "  } else {" +
            "    return null;" +
            "  }" +
            "}";

    // 加载 douyin.js 脚本内容
    static {
        douyinJsCode = readResourceFile(JS_FILE_PATH);
    }

    /**
     * 从资源文件中读取 JavaScript 内容
     */
    public static String readResourceFile(String fileName) {
        try (InputStream inputStream = DouyinSignUtil.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + fileName);
            }
            return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load JS resource: " + fileName, e);
        }
    }

    /**
     * 构造 webid 参数，模拟客户端生成规则
     */
    public static String getWebId() {
        String base = generateUUIDPattern(null);
        StringBuilder webId = new StringBuilder();

        for (char c : base.toCharArray()) {
            if (c == '0' || c == '1' || c == '8') {
                webId.append(generateUUIDPattern(Character.getNumericValue(c)));
            } else {
                webId.append(c);
            }
        }
        return webId.toString().replace("-", "").substring(0, 19);
    }

    /**
     * UUID 模拟生成（抖音前端实现方式）
     */
    private static String generateUUIDPattern(Integer t) {
        if (t != null) {
            int randInt = (int) (16 * RANDOM.nextDouble());
            int shifted = randInt >> (t / 4);
            return String.valueOf(t ^ shifted);
        } else {
            return String.join("-",
                    String.valueOf((int) 1e7),
                    String.valueOf((int) 1e3),
                    String.valueOf((int) 4e3),
                    String.valueOf((int) 8e3),
                    String.valueOf((int) 1e11)
            );
        }
    }

    /**
     * 调用 JavaScript 方法执行签名生成逻辑
     */
    private static String executeJsFunction(String functionName, Object... args) {
        try (Context context = Context.create("js")) {
            context.eval("js", douyinJsCode);
            Value func = context.getBindings("js").getMember(functionName);
            if (func == null || !func.canExecute()) {
                throw new RuntimeException("JS function not found or not executable: " + functionName);
            }
            Value result = func.execute(args);
            return result.asString();
        } catch (Exception e) {
            log.error("Error executing JavaScript function: {}", functionName, e);
            return null;
        }
    }

    /**
     * 根据 URL 自动选择签名函数并获取 a_bogus 签名
     */
    public static String getABogusFromJS(String url, String params, String userAgent) {
        String function = url.contains("/reply") ? "sign_reply" : "sign_datail";
        return executeJsFunction(function, params, userAgent);
    }

    /**
     * 执行 JS 中 wordsToBytes 函数（可用于其他编码转换）
     */
    public static String wordsToBytes(String params) {
        return executeJsFunction("wordsToBytes", params);
    }

    /**
     * 构建抖音请求的通用基础参数（含浏览器环境信息）
     */
    private static Map<String, String> buildBaseParams() {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("aid", AID);
        params.put("app_name", "douyin_web");
        params.put("device_platform", "web");
        params.put("cookie_enabled", "true");
        params.put("browser_language", "zh-CN");
        params.put("browser_platform", "MacIntel");
        params.put("browser_name", "Chrome");
        params.put("browser_version", "136.0.0.0");
        params.put("screen_width", "1440");
        params.put("screen_height", "900");
        return params;
    }

    /**
     * 构造 WebSocket 请求的参数 Map
     */
    public static Map<String, String> getWssParams(String roomId, String userId, String signature) {
        Map<String, String> params = buildBaseParams();

        params.put("version_code", "180800");
        params.put("webcast_sdk_version", "1.0.14-beta.0");
        params.put("update_version_code", "1.0.14-beta.0");
        params.put("compress", "gzip");
        params.put("browser_online", "true");
        params.put("tz_name", "Asia/Shanghai");
        params.put("internal_ext", "internal_src:dim|wss_push_room_id:" + roomId +
                "|wss_push_did:7492367746048460315|first_req_ms:1750262359394" +
                "|fetch_time:1750262359479|seq:1|wss_info:0-1750262359479-0-0" +
                "|wrds_v:7517319578439913551");
        params.put("host", "https://live.douyin.com");
        params.put("live_id", "1");
        params.put("did_rule", "3");
        params.put("endpoint", "live_pc");
        params.put("support_wrds", "1");
        params.put("user_unique_id", userId);
        params.put("im_path", "/webcast/im/fetch/");
        params.put("identity", "audience");
        params.put("need_persist_msg_count", "15");
        params.put("insert_task_id", "");
        params.put("live_reason", "");
        params.put("room_id", roomId);
        params.put("heartbeatDuration", "0");
        params.put("signature", signature);
        return params;
    }

    /**
     * 构造通用请求参数，并计算 a_bogus 签名
     *
     * @param param     额外参数（如 room_id 等）
     * @param xmst      msToken
     * @param userAgent User-Agent 字符串
     * @return 包含签名的完整参数 Map
     */
    public static Map<String, String> getCommonParams(Map<String, String> param,String uri, String xmst, String userAgent) {
        Map<String, String> params = buildBaseParams();
        params.put("language", "zh-CN");
        params.put("enter_from", "link_share");
        params.put("is_need_double_stream", "false");
        params.put("webid", getWebId());
        params.put("msToken", xmst);

        if (param != null && !param.isEmpty()) {
            params.putAll(param);
        }
        String queryString = RequestParametersUtils.buildQueryString(params);
        String a_bogus = getABogusFromJS(uri, queryString, userAgent);
        params.put("a_bogus", a_bogus);

        return params;
    }



//    /**
//     * 获取通用参数
//     *
//     * @param param     查询参数
//     * @param xmst      xmst
//     * @param userAgent userAgent
//     * @return 通用参数计算结果
//     */
//    private Map<String, String> getCommonParams(Map<String, String> param, String xmst, String userAgent) {
//        Map<String, String> params = new HashMap<>();
//        params.put("aid", "6383");
//        params.put("app_name", "douyin_web");
//        params.put("device_platform", "web");
//        params.put("language", "zh-CN");
//        params.put("enter_from", "link_share");
//        params.put("cookie_enabled", "true");
//        params.put("screen_width", "1280");
//        params.put("screen_height", "720");
//        params.put("browser_language", "zh-CN");
//        params.put("browser_platform", "MacIntel");
//        params.put("browser_name", "Chrome");
//        params.put("browser_version", "136.0.0.0");
//        params.put("web_rid", "525960662143");
//        params.put("is_need_double_stream", "false");
//
//        params.put("webid", DouyinSignUtil.getWebId());
//        params.put("msToken", xmst);
//        params.putAll(param);
//
//        String queryString = buildQueryString(params);
//        String a_bogus = DouyinSignUtil.getABogusFromJS("/webcast/room/web/enter/", queryString, userAgent);
//        params.put("a_bogus", a_bogus);
//        return params;
//    }
//
//    /**
//     * 构建查询字符串
//     *
//     * @param params 查询字段
//     * @return 查询字符串
//     */
//    public static String buildQueryString(Map<String, String> params) {
//        StringBuilder result = new StringBuilder();
//        try {
//            for (Map.Entry<String, String> entry : params.entrySet()) {
//                if (result.length() > 0) {
//                    result.append("&");
//                }
//                if (StringUtils.isNoneBlank(entry.getKey()) && StringUtils.isNoneBlank(entry.getValue())) {
//                    result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
//                    result.append("=");
//                    result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
//                }
//            }
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException("UTF-8 encoding not supported", e);
//        }
//        return result.toString();
//    }
//
//    public Map<String, String> getWssParams(String roomId, String userId, String signature) {
//        Map<String, String> params = new LinkedHashMap<>();
//        params.put("app_name", "douyin_web");
//        params.put("version_code", "180800");
//        params.put("webcast_sdk_version", "1.0.14-beta.0");
//        params.put("update_version_code", "1.0.14-beta.0");
//        params.put("compress", "gzip");
//        params.put("device_platform", "web");
//        params.put("cookie_enabled", "true");
//        params.put("screen_width", "1440");
//        params.put("screen_height", "900");
//        params.put("browser_language", "zh-CN");
//        params.put("browser_platform", "MacIntel");
//        params.put("browser_name", "Mozilla");
//        params.put("browser_version", "5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36");
//        params.put("browser_online", "true");
//        params.put("tz_name", "Asia/Shanghai");
//        params.put("internal_ext", "internal_src:dim|wss_push_room_id:7517309070055033634|wss_push_did:7492367746048460315|first_req_ms:1750262359394|fetch_time:1750262359479|seq:1|wss_info:0-1750262359479-0-0|wrds_v:7517319578439913551");
//        params.put("host", "https://live.douyin.com");
//        params.put("aid", "6383");
//        params.put("live_id", "1");
//        params.put("did_rule", "3");
//        params.put("endpoint", "live_pc");
//        params.put("support_wrds", "1");
//        params.put("user_unique_id", userId);
//        params.put("im_path", "/webcast/im/fetch/");
//        params.put("identity", "audience");
//        params.put("need_persist_msg_count", "15");
//        params.put("insert_task_id", "");
//        params.put("live_reason", "");
//        params.put("room_id", roomId);
//        params.put("heartbeatDuration", "0");
//        params.put("signature", signature);
//        return params;
//    }
}
