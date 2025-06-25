package org.caesar.boot.start.configuration;

import org.caesar.boot.start.exception.handler.DefaultExceptionHandler;
import org.caesar.boot.start.exception.handler.GlobalExceptionHandler;
import org.caesar.boot.start.properties.ExceptionPropeties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p> @author GuoPeng</p>
 * <p> @version 1.0.0</p>
 * <p> @description 异常自动装配</p>
 * <p> @createTime 2022-10-31 16:56:00</p>
 */
@Configuration
@EnableConfigurationProperties({ExceptionPropeties.class})
@ConditionalOnProperty(prefix = "caesar.exception", value = "enable", matchIfMissing = true)
public class ExceptionAutoConfiguration {

    @ConditionalOnMissingBean(GlobalExceptionHandler.class)
    @Bean
    public DefaultExceptionHandler baseExceptionHandler() {
        return new DefaultExceptionHandler();
    }
}
