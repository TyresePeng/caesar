package org.caesar.agent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p> @author GuoPeng</p>
 * <p> @version 1.0.0</p>
 * <p> @description 日志常量</p>
 * <p> @createTime 2023-02-01 17:30:00</p>
 */
@Getter
@AllArgsConstructor
public enum LogEnums {
    //日志常量
    HTTP_LOGGER("HttpLogger", "HttpLogger"),
    REQUEST_ID("requestId", "requestId"),
    EVENT("event","事件"),
    REQUEST_URL("requestUrl", "uri"),
    REQUEST_TYPE("requestType", "request_type"),
    CLIENT_IP("clientIp", "clientIp"),
    REQUEST_HEADERS("requestHeaders", "requestHeaders"),
    REQUEST_METHOD("requestMethod", "requestMethod"),
    REQUEST_PARAMS("requestParams", "requestParams"),
    REQUEST_BODY("requestBody", "requestBody"),
    STATUS("status", "status"),
    RESPONSE("response", "response"),
    RESPONSE_HEADERS("responseHeaders", "responseHeaders"),
    CREATE_TIME("createTime", "createTime"),
    THROWABLE("throwable", "异常信息"),
    TIME("time", "耗时")

            ;

    private final String code;
    private final String msg;

}
