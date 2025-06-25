package org.caesar.boot.start.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

/**
 * <p> @author GuoPeng</p>
 * <p> @version 1.0.0</p>
 * <p> @description sign签名配置 </p>
 * <p> @createTime 2022-10-31 15:49:00</p>
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "caesar.sign")
public class  SignProperties {
    private final String defaultScanAspect="@annotation(org.caesar.boot.start.annotation.ApiSign)";

    private Boolean enable=Boolean.FALSE;

    /**
     * 配置自定义切点
     */
    private String scanAspect=defaultScanAspect;

    /**
     * 随机字符串桶大小
     */
    private int nonceBarrelSize = 10000;

    /**
     * 请求超时时间
     */
    private Duration timeout = Duration.ofMinutes(1L);

    /**
     * 授权容器
     */
    private Map<String, String> authApps;
}
