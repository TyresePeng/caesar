package org.caesar.test.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.caesar.boot.start.exception.BusinessException;
import org.caesar.common.response.ApiResponse;
import org.caesar.test.fegin.TestFeign;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * @Description: test
 * @Author: peng.guo
 * @Create: 2024-11-15 16:52
 * @Version 1.0
 **/
@RestController
@RequestMapping("api-test")
@Log4j2
public class TestController {

    @Resource
    private TestFeign testFeign;
    private static final ArrayList<String> list = new ArrayList<>();

    @GetMapping("health-check")
    public ApiResponse<String> healthCheck()
    {
        //添加 2MB 内存数据
        for (int i = 0; i < 1024 * 1024 * 2; i++) {
            list.add("xxxxxxxxxxxxxxxxxxxxxxxx");
        }
        System.err.println("x");
        return ApiResponse.success("test----1");
    }

    @PostMapping("health-check")
    public ApiResponse<String> healthCheck(@RequestBody JSONObject parma)
    {
        System.err.println("");
        return ApiResponse.success(parma.toJSONString());
    }

    @RequestMapping("test")
    public ApiResponse<String> test()
    {
        if (true){
            throw new BusinessException("x");
        }
        log.info("test----1 xxxx");
        return ApiResponse.success("test----1");
    }

    @RequestMapping("test2")
    public ApiResponse test2()
    {
        log.info("test2----2xxxx");
        return ApiResponse.success(testFeign.jscode2session());

    }

}
