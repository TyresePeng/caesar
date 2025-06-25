package org.caesar.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Peng.GUO
 */
//@EnableDynamicTp
@SpringBootApplication
@EnableFeignClients
//@ComponentScan(basePackages = {"org.caesar.agent", "org.caesar.test"})
public class CaesarTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(CaesarTestApplication.class, args);
    }

}
