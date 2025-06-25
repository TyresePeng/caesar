package org.caesar.test.fegin;


import org.caesar.common.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.metadata.MethodType;
import java.lang.reflect.Method;

/**
 * @Description: 微信开发平台请求
 * @Author: peng.guo
 * @Create: 2024-11-08 14:55
 * @Version 1.0
 **/
@FeignClient(name = "caesar", url = "127.0.0.1:8080")
public interface TestFeign {

    @RequestMapping(value = "/test2",method = {RequestMethod.GET})
    ApiResponse<String> jscode2session();
}


