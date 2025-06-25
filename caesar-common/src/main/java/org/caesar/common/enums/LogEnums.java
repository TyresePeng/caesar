package org.caesar.common.enums;

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
    HTTP_LOGGER("HttpLogger", "HttpLogger"),
    URI("uri", "uri"),
    CLIENT_IP("clientIp", "clientIp"),
    REQUEST_HEADERS("requestHeaders", "requestHeaders"),
    METHOD("method", "method"),
    PARAMS("params", "params"),
    REQUEST_BODY("requestBody", "requestBody"),
    STATUS("status", "status"),
    RESPONSE("response", "response"),
    RESPONSE_HEADERS("responseHeaders", "responseHeaders"),
    CREATE_TIME("create_time", "create_time")
            ;

    private final String code;
    private final String msg;

}
