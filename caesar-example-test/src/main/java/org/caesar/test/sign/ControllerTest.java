package org.caesar.test.sign;

import lombok.Data;
import org.caesar.boot.start.annotation.ApiSign;
import org.caesar.common.response.ApiResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * <p> @author GuoPeng</p>
 * <p> @version 1.0.0</p>
 * <p> @description ApiSign 签名测试</p>
 * <p> @createTime 2022-10-31 14:58:00</p>
 */
@RestController
@RequestMapping("api-sign")
public class ControllerTest {

    @ApiSign
    @RequestMapping("test")
    public ApiResponse<Void> testSign(String name,@RequestBody Map reqDto){
        return ApiResponse.success();
    }
    @ApiSign
    @RequestMapping("test-no-parameter")
    public ApiResponse<Void> testSignNoParameter(){
        return ApiResponse.success();
    }

    @RequestMapping("test-sign-scanAspect")
    public ApiResponse<Void> testSignScanAspect(String name , int age, @RequestBody ReqDto param){
        return ApiResponse.success();
    }

    @RequestMapping("test2")
    public ApiResponse<Void> test(){
        return ApiResponse.success();
    }
}

@Data
class ReqDto {
   private String name;
   private String age;
   private String accessKey;
   private Long timestamp;
   private String nonce;
    private String sign;
}
