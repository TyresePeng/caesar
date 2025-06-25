package org.caesar.boot.start.transmittable;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;

/**
 * @Description: 可传递的线程上下文
 * @Author: peng.guo
 * @Create: 2024-11-22 14:34
 * @Version 1.0
 **/
@Log4j2
public class ContextThreadLocal {

    // 线程局部变量，用于存储上下文信息
    private static final TransmittableThreadLocal<JSONObject> CONTEXT_THREAD_LOCAL = new TransmittableThreadLocal<>();

    /**
     * 初始化上下文信息，生成唯一的 traceId 并设置到上下文中
     */
    public static JSONObject init() {
        try {
            String traceId = UUID.randomUUID().toString().replace("-", "");
            CONTEXT_THREAD_LOCAL.set(new JSONObject());
            CONTEXT_THREAD_LOCAL.get().put("traceId", traceId);
        } catch (OutOfMemoryError e) {
            log.error("Failed to generate UUID:{}", e.getMessage(), e);
        }
        return CONTEXT_THREAD_LOCAL.get();
    }

    /**
     * 设置上下文中的键值对
     *
     * @param key   键
     * @param value 值
     */
    public static void set(String key, Object value) {
        try {
            JSONObject context = CONTEXT_THREAD_LOCAL.get();
            if (context == null) {
                context = init();
            }
            context.put(key, value);
        } catch (NullPointerException e) {
            log.error("Failed to set context: {}", e.getMessage(), e);
        }
    }

    /**
     * 获取上下文信息
     *
     * @return 上下文信息
     */
    public static JSONObject get() {
        try {
            JSONObject context = CONTEXT_THREAD_LOCAL.get();
            if (context == null) {
                context = init();
                CONTEXT_THREAD_LOCAL.set(context);
            }
            return context;
        } catch (NullPointerException e) {
            log.error("Failed to get context:{}", e.getMessage(), e);
            return new JSONObject();
        }
    }

    /**
     * 获取 traceId
     *
     * @return traceId
     */
    public static String getTraceId() {
        try {
            JSONObject context = CONTEXT_THREAD_LOCAL.get();
            if (context == null) {
                context = init();
            }
            return context.getString("traceId");
        } catch (NullPointerException e) {
            log.error("Failed to get traceId:{}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 清除上下文信息
     */
    public static void clear() {
        try {
            CONTEXT_THREAD_LOCAL.remove();
        } catch (Exception e) {
            log.error("Failed to clear context:{}", e.getMessage(), e);
        }
    }
}
