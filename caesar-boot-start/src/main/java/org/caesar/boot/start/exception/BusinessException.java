package org.caesar.boot.start.exception;

import java.io.Serializable;
import java.util.Formatter;

/**
 * <p> @author GuoPeng</p>
 * <p> @version 1.0.0</p>
 * <p> @description 业务异常</p>
 * <p> @createTime 2022-11-01 11:20:00</p>
 */
public class BusinessException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 7125213580640505574L;

    private Object data;

    public Object getData() {
        return data;
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Object data) {
        super(message);
        this.data = data;
    }

    public BusinessException(String message, Object... args) {
        super(message);
        this.data = new Formatter().format(message, args).toString();
    }

}
