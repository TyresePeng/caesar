package org.caesar.boot.start.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p> @author GuoPeng</p>
 * <p> @version 1.0.0</p>
 * <p> @description TODO</p>
 * <p> @createTime 2022-11-01 17:14:00</p>
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring")
public class SpringPropeties {

    private Boolean enable=Boolean.TRUE;

    private class main{

    }
}
