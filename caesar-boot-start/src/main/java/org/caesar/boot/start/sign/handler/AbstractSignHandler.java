package org.caesar.boot.start.sign.handler;

import org.caesar.boot.start.exception.SignException;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;

import java.time.Duration;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * <p> @author GuoPeng</p>
 * <p> @version 1.0.0</p>
 * <p> @description 签名处理器</p>
 * <p> @createTime 2022-11-01 10:51:00</p>
 */

public abstract class AbstractSignHandler implements SignHandler {

    @Override
    public void validTimeStamp(long paramTimeStamp, Duration requestTimeout) {
        long timeout = requestTimeout.toMillis();
        long currentTimeMillis = System.currentTimeMillis();
        if ((currentTimeMillis - paramTimeStamp) > timeout) {
            throw new SignException("时间戳超时");
        }
    }

    @Override
    public String generateSign(SortedMap<String, Object> params) {
        final String parameter = this.getParameter(params).toString();
        return DigestUtils.md5DigestAsHex(parameter.substring(0, parameter.length() - 1).getBytes());
    }

    @Override
    public void validSign(Map<String, Object> params, Duration timeout,int nonceBarrelSize) {
        // 验证必需参数
        String message = "%s不能为空";
        Object paramAccessKey = params.get(ACCESS_KEY_NAME);
        Assert.notNull(paramAccessKey, String.format(message, ACCESS_KEY_NAME));
        Assert.notNull(params.get(TIMESTAMP_NAME), String.format(message, TIMESTAMP_NAME));
        Assert.notNull(params.get(NONCE_NAME), String.format(message, NONCE_NAME));
        Assert.notNull(params.get(ACCESS_KEY_NAME), String.format(message, ACCESS_KEY_NAME));
        Assert.notNull(params.get(SIGN_NAME), String.format(message, SIGN_NAME));
        String accessKey = String.valueOf(params.get(ACCESS_KEY_NAME));
        String secretKey = getSecretKey(accessKey);
        String nonce = String.valueOf(params.get(NONCE_NAME));
        String sign = String.valueOf(params.remove(SIGN_NAME));
        Assert.hasText(secretKey, String.format(message, SECRET_KEY_NAME));
        long timestamp = Long.parseLong(String.valueOf(params.get(TIMESTAMP_NAME)));
        // 验证时间
        validTimeStamp(timestamp, timeout);
        // 验证随机字符串
        validNonce(accessKey,nonce);
        // 验证签名
        params.put(SECRET_KEY_NAME, secretKey);
        String finalSign = generateSign(new TreeMap<>(params));
        if (!sign.equalsIgnoreCase(finalSign)) {
            System.err.printf("签名验证失败, 签名值为(%s), 正确签名值为 %s%n", sign, finalSign);
            throw new SignException("签名验证失败");
        }
        saveNonce(accessKey, nonceBarrelSize,nonce);
    }
}
