package org.caesar.boot.start.feign;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import okio.Buffer;
import okio.BufferedSource;
import org.caesar.boot.start.transmittable.ContextThreadLocal;
import org.caesar.common.enums.LogFields;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Feign 日志拦截器，用于记录请求和响应的详细信息
 */
@Component
@Log4j2
public class FeignLogInterceptor implements Interceptor {

    private static final Charset UTF8 = StandardCharsets.UTF_8;

    private final ObjectMapper MAPPER;

    // 构造函数
    public FeignLogInterceptor() {
        this(new ObjectMapper());
    }

    public FeignLogInterceptor(ObjectMapper objectMapper) {
        this.MAPPER = objectMapper;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long startNs = System.nanoTime(); // 记录请求开始时间
        JSONObject logNode = new JSONObject();
        addCommonLogInfo(logNode, request, startNs);

        // 处理请求头和请求体
        processRequestBody(logNode, request);

        // 发起请求并记录响应
        Response response;
        long tookMs = 0L;
        try {
            response = chain.proceed(request);
            tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
            processResponseBody(logNode, response, tookMs);
        } catch (Exception e) {
            logNode.put(LogFields.RESPONSE_MESSAGE.getField(), e.getMessage());
            logNode.put(LogFields.TOOK_MS.getField(), tookMs);
            log.trace(logNode);
            throw e;
        }

        log.trace(logNode);
        return response;
    }

    /**
     * 添加通用日志信息
     *
     * @param logNode  日志对象
     * @param request  请求对象
     * @param startNs  请求开始时间
     */
    private void addCommonLogInfo(JSONObject logNode, Request request, long startNs) {
        logNode.put(LogFields.START_NS.getField(), startNs);
        logNode.put(LogFields.CREATE_TIME.getField(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date()));
        logNode.put(LogFields.TRACE_ID.getField(), ContextThreadLocal.getTraceId());
        logNode.put(LogFields.FLOW.getField(), "OUTBOUND");
        logNode.put(LogFields.FLOW_TYPE.getField(), "feign");
        logNode.put(LogFields.METHOD.getField(), request.method());
        HttpUrl url = request.url();
        logNode.put(LogFields.URL.getField(), (request.isHttps() ? "https://" : "http://") + url.host() + url.encodedPath());

        if (url.encodedQuery() != null) {
            logNode.put(LogFields.PARAM.getField(), getRequestGetParams(request));
        }
    }

    /**
     * 处理请求体
     *
     * @param logNode 日志对象
     * @param request 请求对象
     */
    private void processRequestBody(JSONObject logNode, Request request) throws IOException {
        RequestBody requestBody = request.body();
        if (requestBody != null) {
            Buffer requestBuffer = new Buffer();
            requestBody.writeTo(requestBuffer);

            MediaType contentType = requestBody.contentType();
            Charset charset = contentType != null ? contentType.charset(UTF8) : UTF8;
            logNode.put(LogFields.REQUEST_BODY_CONTENT_TYPE.getField(), contentType);
            logNode.put(LogFields.REQUEST_BODY_LENGTH.getField(), requestBody.contentLength());

            if (isPlaintext(requestBuffer)) {
                logNode.put(LogFields.REQUEST_BODY.getField(), requestBuffer.readString(charset));
            }
        }

        // 添加请求头
        logNode.put(LogFields.REQUEST_HEADER.getField(), getRequestHeaderArrayNode(request));
    }

    /**
     * 处理响应体
     *
     * @param logNode 日志对象
     * @param response 响应对象
     * @param tookMs 请求耗时
     */
    private void processResponseBody(JSONObject logNode, Response response, long tookMs) throws IOException {
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            long contentLength = responseBody.contentLength();
            logNode.put(LogFields.RESPONSE_CODE.getField(), response.code());
            logNode.put(LogFields.RESPONSE_MESSAGE.getField(), response.message());
            logNode.put(LogFields.TOOK_MS.getField(), tookMs);
            logNode.put(LogFields.RESPONSE_CONTENT_LENGTH.getField(), contentLength);

            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // 缓存整个响应体
            Buffer buffer = source.getBuffer();

            if (isPlaintext(buffer)) {
                logNode.put(LogFields.RESPONSE_BODY.getField(), buffer.clone().readString(UTF8));
            }
        }
    }

    /**
     * 检查是否是纯文本
     */
    public static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = buffer.clone();
            long byteCount = Math.min(prefix.size(), 64);
            for (int i = 0; i < byteCount; i++) {
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false;
        }
    }

    /**
     * 获取 GET 请求参数
     */
    private ArrayNode getRequestGetParams(Request request) {
        HttpUrl url = request.url();
        ArrayNode paramsNode = MAPPER.createArrayNode();
        Set<String> paramNames = url.queryParameterNames();
        paramNames.forEach(name -> {
            List<String> values = url.queryParameterValues(name);
            if (!CollectionUtils.isEmpty(values)) {
                paramsNode.add(name + ": " + values.get(0));
            }
        });
        return paramsNode;
    }

    /**
     * 获取请求头 JSON 节点
     */
    private ArrayNode getRequestHeaderArrayNode(Request request) {
        ArrayNode headerNode = MAPPER.createArrayNode();
        Headers headers = request.headers();
        Set<String> headersToRedact = Collections.emptySet();
        for (int i = 0, count = headers.size(); i < count; i++) {
            String name = headers.name(i);
            if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                String value = headersToRedact.contains(headers.name(i)) ? "██" : headers.value(i);
                headerNode.add(headers.name(i) + ": " + value);
            }
        }
        return headerNode;
    }
}
