package org.caesar.boot.start.feign;

import feign.Logger;
import feign.codec.Decoder;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


/**
 * @Description:
 * @Author: peng.guo
 * @Create: 2024-11-08 15:12
 * @Version 1.0
 **/
@Log4j2
@Component
public class DefaultFeignInterceptor {


    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }


    @Bean
    public Decoder feignDecoder() {
        return new CustomFeignDecoder();
    }

}
