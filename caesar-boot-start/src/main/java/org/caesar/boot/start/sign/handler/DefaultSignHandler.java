package org.caesar.boot.start.sign.handler;

import org.caesar.boot.start.exception.SignException;
import org.caesar.boot.start.properties.SignProperties;
import org.caesar.common.util.StringUtils;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> @author GuoPeng</p>
 * <p> @version 1.0.0</p>
 * <p> @description 签名处理默认实现</p>
 * <p> @createTime 2022-11-01 11:54:00</p>
 */

public class DefaultSignHandler extends AbstractSignHandler {

    @Resource
    private SignProperties signProperties;

    /**
     * 随机数桶，可实现放入redis,当前实现放入内存，大于10000请求清空随机数桶
     */
    private static final Map<String, String> NONCE_BARRELS = new ConcurrentHashMap<>();


    @Override
    public String getSecretKey(String accessKey) {
        Map<String, String> authApps = signProperties.getAuthApps();
        if (Objects.isNull(authApps)) {
            throw new SignException("未配置签名授权");
        }
        String secretKey = authApps.get(accessKey);
        if (StringUtils.isBlank(secretKey)) {
            throw new SignException("accessKey异常");
        }
        return secretKey;
    }

    @Override
    public void saveNonce(String accessKey, int nonceBarrelSize, String nonce) {
        if (NONCE_BARRELS.size() > nonceBarrelSize) {
            NONCE_BARRELS.clear();
        }
        NONCE_BARRELS.put(accessKey + nonce, nonce);
    }


    @Override
    public void validNonce(String accessKey, String nonce) {
        if (Objects.nonNull(NONCE_BARRELS.get(accessKey + nonce))) {
            throw new SignException("随机数重复");
        }
    }

}
