package org.caesar.common.enums;

/**
 * 日志字段枚举
 * 统一管理日志中使用的字段名，便于修改和维护
 * @author peng.guo
 */
public enum LogFields {
    // 通用字段
    START_NS("startNs"),
    CREATE_TIME("createTime"),
    TRACE_ID("traceId"),
    FLOW("flow"),
    FLOW_TYPE("flowType"),

    // 请求相关字段
    METHOD("method"),
    URL("url"),
    PARAM("param"),
    REQUEST_BODY("requestBody"),
    REQUEST_BODY_CONTENT_TYPE("requestBodyContentType"),
    REQUEST_BODY_LENGTH("requestBodyLength"),
    REQUEST_HEADER("requestHeader"),

    // 响应相关字段
    RESPONSE_CODE("responseCode"),
    RESPONSE_MESSAGE("responseMessage"),
    RESPONSE_BODY("responseBody"),
    RESPONSE_CONTENT_LENGTH("responseContentLength"),
    TOOK_MS("tookMs"),
    GZIPPED_LENGTH("gzippedLength");

    private final String field;

    LogFields(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
