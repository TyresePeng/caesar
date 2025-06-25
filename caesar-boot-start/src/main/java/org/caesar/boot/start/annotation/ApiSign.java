package org.caesar.boot.start.annotation;

import java.lang.annotation.*;

/**
 * <p> @author GuoPeng</p>
 * <p> @version 1.0.0</p>
 * <p> @description 需要验签接口 </p>
 * <p> @createTime 2022-10-31 11:42:00</p>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiSign {

}
