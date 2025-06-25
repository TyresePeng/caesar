package org.caesar.common.context;

import org.caesar.common.util.SpringContextUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * <p> @author GuoPeng</p>
 * <p> @version 1.0.0</p>
 * <p> @description 初始化spring容器</p>
 * <p> @createTime 2022-10-28 16:30:00</p>
 */
public class CaesarApplicationContextInitializer implements ApplicationContextInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        SpringContextUtils.setApplicationContext(applicationContext);
    }
}

