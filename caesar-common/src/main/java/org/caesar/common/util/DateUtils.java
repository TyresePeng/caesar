package org.caesar.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p> @author GuoPeng</p>
 * <p> @version 1.0.0</p>
 * <p> @description 日期工具类</p>
 * <p> @createTime 2023-02-03 16:00:00</p>
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private static final String sdf = "yyyy-MM-dd HH:mm:ss";

    /**
     * <p> @description 获取当前时间(yyyy-MM-dd HH:mm:ss)格式化后字符串 </p>
     * <p> @author GuoPeng </p>
     * <p> @time 2023-02-03 16:06</p>
     */
    public static String nowDefaultToString() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(sdf));
    }
}
