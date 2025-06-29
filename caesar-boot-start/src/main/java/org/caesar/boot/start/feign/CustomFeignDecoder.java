package org.caesar.boot.start.feign;

import com.alibaba.fastjson.JSONObject;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;

@Log4j2
public class CustomFeignDecoder implements Decoder {
    @Override
    public Object decode(Response response, Type type) throws IOException {
        if (response.status() == HttpStatus.NO_CONTENT.value() || response.body() == null) {
            return null;
        }
        Collection<String> contentTypeHeader = response.headers().getOrDefault("Content-Type", Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM_VALUE));
        String contentType;
        if (contentTypeHeader != null && !contentTypeHeader.isEmpty()) {
            contentType = contentTypeHeader.iterator().next();
        } else {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        MediaType mediaType = MediaType.parseMediaType(contentType);

        if (MediaType.APPLICATION_JSON_VALUE.equals(mediaType.toString())) {
            String body = StreamUtils.copyToString(response.body().asInputStream(), StandardCharsets.UTF_8);
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                type = parameterizedType.getRawType();
            }
            return JSONObject.parseObject(body, type);
        } else {
            if (response.status() != 404 && response.status() != 204) {
                if (response.body() == null) {
                    return null;
                } else {
                    if (byte[].class.equals(type)) {
                        return Util.toByteArray(response.body().asInputStream());
                    } else {
                        Response.Body body = response.body();
                        if (body == null) {
                            return null;
                        } else if (String.class.equals(type)) {
                            return Util.toString(body.asReader());
                        } else {
                            throw new IllegalArgumentException(String.format("%s is not a type supported by this decoder.", type));
                        }
                    }
                }
            } else {
                return Util.emptyValueOf(type);
            }
        }
    }
}

