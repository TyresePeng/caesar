package org.caesar.boot.start.exception;

import java.io.Serializable;
import java.util.Formatter;

/**
 * <p> @author GuoPeng</p>
 * <p> @version 1.0.0</p>
 * <p> @description TODO</p>
 * <p> @createTime 2022-11-01 11:49:00</p>
 */
public class SignException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 5640534814491720789L;

    private Object data;

    public Object getData() {
        return data;
    }

    public SignException(String message) {
        super(message);
    }

    public SignException(String message, Object data) {
        super(message);
        this.data = data;
    }

    public SignException(String message, Object... args) {
        super(message);
        this.data = new Formatter().format(message, args).toString();
    }
}
