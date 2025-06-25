package org.caesar.media.exception;

/**
 * @Description: 媒体查询异常
 * @Author: peng.guo
 * @Create: 2025-06-20 16:56
 * @Version 1.0
 **/
public class MediaException extends RuntimeException {

    public MediaException(String message) {
        super(message);
    }

    public MediaException(String message,Throwable cause) {
        super(message,cause);

    }
}
