package org.caesar.boot.start.exception.handler;

import org.caesar.boot.start.exception.BusinessException;
import org.caesar.boot.start.exception.SignException;
import org.caesar.common.response.ApiResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.naming.ConfigurationException;
import javax.security.sasl.AuthenticationException;
import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * <p> @author GuoPeng</p>
 * <p> @version 1.0.0</p>
 * <p> @description 异常处理</p>
 * <p> @createTime 2022-11-01 17:31:00</p>
 */
public interface GlobalExceptionHandler {

    String handleUnauthorizedException(SecurityException e);

    ApiResponse<Void> handleAuthenticationException(AuthenticationException e);

    ApiResponse<Void> handleBusinessException(BusinessException e);

    ApiResponse<Void> handleIOException(IOException e);

    ApiResponse<Void> handleValidationException(ValidationException e);

    ApiResponse<Void> handleSQLException(SQLException e);

    ApiResponse<Void> handleBusinessException(ConfigurationException e);

    ApiResponse<Void> handlerIllegalArgumentException(IllegalArgumentException e);

    ApiResponse<Void> handlerIllegalArgumentException(SignException e);

    ApiResponse<Void> handleVaildException(MethodArgumentNotValidException e);
}
