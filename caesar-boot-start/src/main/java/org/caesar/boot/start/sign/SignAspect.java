package org.caesar.boot.start.sign;

import com.alibaba.fastjson.JSONObject;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.caesar.boot.start.properties.SignProperties;
import org.caesar.boot.start.sign.handler.SignHandler;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * <p> @author GuoPeng</p>
 * <p> @version 1.0.0</p>
 * <p> @description 签名切面</p>
 * <p> @createTime 2022-10-31 14:09:00</p>
 */
public class SignAspect implements MethodInterceptor {

    @Resource
    private SignHandler signHandler;

    @Resource
    private SignProperties signProperties;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        //获取到请求参数
        SortedMap<String, Object> params = getFieldsName(invocation);
        signHandler.validSign(params, signProperties.getTimeout(), signProperties.getNonceBarrelSize());
        return invocation.proceed();
    }

    private static SortedMap<String, Object> getFieldsName(MethodInvocation invocation) {
        Object[] args = invocation.getArguments();
        ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
        Method method = invocation.getMethod();
        String[] parameterNames = pnd.getParameterNames(method);
        TreeMap<String, Object> argParam = new TreeMap<>();
        for (int i = 0; i < Objects.requireNonNull(parameterNames).length; i++) {
            argParam.put(parameterNames[i], args[i]);
        }
        SortedMap<String, Object> paramMap = new TreeMap<>();
        for (Map.Entry<String, Object> entry : argParam.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            //参数处理
            if (value instanceof Map) {
                paramMap.putAll((Map<? extends String, ?>) value);
            } else if (value.getClass().getClassLoader() != null) {
                paramMap.putAll(JSONObject.parseObject(JSONObject.toJSONString(value)));
            } else {
                paramMap.put(key, value);
            }
        }
        return paramMap;
    }
}