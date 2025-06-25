package org.caesar.test.exception;

import lombok.extern.slf4j.Slf4j;
import org.caesar.boot.start.exception.handler.DefaultExceptionHandler;
import org.caesar.boot.start.exception.handler.GlobalExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p> @author GuoPeng</p>
 * <p> @version 1.0.0</p>
 * <p> @description 全局异常处理测试</p>
 * <p> @createTime 2022-11-01 18:13:00</p>
 */
@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler extends DefaultExceptionHandler implements GlobalExceptionHandler {

}
