package org.caesar.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 公共业务返回常量
 *
 * @author GuoPeng
 */
@Getter
@AllArgsConstructor
public enum ReturnCode {

    /**
     * 请求成功
     */
    SUCCESS(200, "SUCCESS"),
    /**
     * 请求失败
     */
    FAIL(500, "SYSTEM ERROR"),

    /**
     * 请进行登录验证
     */
    NOT_LOG_IN(403, "请求被拒绝访问，请先进行登录认证"),

    /**
     * 参数不合法
     */
    PARAMETER_INVALID(400, "参数不合法"),

    /**
     * 验证失败
     */
    SIGN_ERROR(401, "验签异常");

    private final Integer code;
    private final String msg;
}
