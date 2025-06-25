package org.caesar.boot.start.sign.handler;

import java.time.Duration;
import java.util.Map;
import java.util.SortedMap;

/**
 * <p> @author GuoPeng</p>
 * <p> @version 1.0.0</p>
 * <p> @description 签名处理接口</p>
 * <p> @createTime 2022-11-01 10:53:00</p>
 */
public interface SignHandler {

    /**
     * 调用方唯一key参数名
     */
    String ACCESS_KEY_NAME = "accessKey";
    /**
     * 调用方密钥参数名
     */
    String SECRET_KEY_NAME = "secretKey";
    /**
     * 调用方时间戳参数名
     */
    String TIMESTAMP_NAME = "timestamp";
    /**
     * 调用方随机数参数名
     */
    String NONCE_NAME = "nonce";
    /**
     * 调用方签名数参数名
     */
    String SIGN_NAME = "sign";


    /**
     * @param params 入参
     * @return StringBuilder
     */
    default StringBuilder getParameter(SortedMap<String, Object> params) {
        StringBuilder stringBuilder = new StringBuilder();
        // 拼接字符串
        for (String key : params.keySet()) {
            stringBuilder.append(key).append("=").append(params.get(key)).append("&");
        }
        return stringBuilder;
    }

    /**
     * 通过accessKey获取配置的SecretKey
     *
     * @param accessKey
     * @return secretKey
     */
    String getSecretKey(String accessKey);

    /**
     * 根据参数生成签名
     *
     * @param params
     * @return sign
     */
    String generateSign(SortedMap<String, Object> params);

    /**
     * 验证签名
     *
     * @param params  请求参数中时间戳
     * @param timeout 请求超时时间 配置文件中获取
     * @param nonceBarrelSize 随机字符串桶大小 配置文件中获取
     * @return
     */
    void validSign(Map<String, Object> params, Duration timeout,int nonceBarrelSize);

    /**
     * 校验请求时间是否过期
     *
     * @param paramTimeStamp 请求参数中时间戳
     * @param requestTimeout 请求时间戳超时时间
     * @return void 请求超时时间 配置文件中获取
     */
    void validTimeStamp(long paramTimeStamp, Duration requestTimeout);


    /**
     * 保存随机字符串
     *
     * @param accessKey accessKey
     * @param nonceBarrelSize 随机数桶大小
     * @param nonce 随机数
     */
    void saveNonce(String accessKey, int nonceBarrelSize, String nonce);

    /**
     * 校验随机字符串
     * @param accessKey accessKey
     * @param nonce 随机字符串
     * @return
     */
    void validNonce(String accessKey,String nonce);

}
