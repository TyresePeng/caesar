package org.caesar.boot.start.exception.handler;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.caesar.boot.start.exception.BusinessException;
import org.caesar.boot.start.exception.SignException;
import org.caesar.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.naming.ConfigurationException;
import javax.security.sasl.AuthenticationException;
import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * <p> @author GuoPeng</p>
 * <p> @version 1.0.0</p>
 * <p> @description Base异常Handler</p>
 * <p> @createTime 2022-11-01 11:20:00</p>
 */
@Slf4j
@RestControllerAdvice
public class DefaultExceptionHandler implements GlobalExceptionHandler{

    @Override
    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleUnauthorizedException(SecurityException e) {
        log.error(e.getMessage(),e);
        return JSONObject.toJSONString(ApiResponse.fail(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
    }

    @Override
    @ExceptionHandler(value = AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<Void> handleAuthenticationException(AuthenticationException e) {
        log.error(e.getMessage(),e);
        return ApiResponse.fail(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    @Override
    @ExceptionHandler(value = BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleBusinessException(BusinessException e) {
        log.error(e.getMessage(),e);
        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    @Override
    @ExceptionHandler(value = IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleIOException(IOException e) {
        log.error(e.getMessage(),e);
        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    @Override
    @ExceptionHandler(value = ValidationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleValidationException(ValidationException e) {
        log.error(e.getMessage(),e);
        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    @Override
    @ExceptionHandler(value = SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleSQLException(SQLException e) {
        log.error(e.getMessage(),e);
        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Database operation Exception");
    }

    @Override
    @ExceptionHandler(value = ConfigurationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleBusinessException(ConfigurationException e) {
        log.error(e.getMessage(),e);
        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    @Override
    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handlerIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage(),e);
        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    @Override
    @ExceptionHandler({SignException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handlerIllegalArgumentException(SignException e) {
        log.error(e.getMessage(),e);
        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    @Override
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleVaildException(MethodArgumentNotValidException e) {
        log.error("JSR303 Data verification fails. Procedure：{}，Exception types：{}", e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        StringBuffer stringBuffer = new StringBuffer();
        bindingResult.getFieldErrors().forEach(item -> {
            String message = item.getDefaultMessage();
            String field = item.getField();
            stringBuffer.append(field).append(":").append(message).append(" ");
        });
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), stringBuffer.toString());

    }
}
