//package org.caesartest.pool;
//
//import org.caesar.boot.start.annotation.ApiSign;
//import org.caesar.common.response.ApiResponse;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//import java.util.Map;
//import java.util.concurrent.ThreadPoolExecutor;
//
///**
// * <p> @author GuoPeng</p>
// * <p> @version 1.0.0</p>
// * <p> @description TODO</p>
// * <p> @createTime 2024-02-22 18:04:00</p>
// */
//@RestController
//@RequestMapping("pool")
//public class PoolController {
//
//
//    @Resource
//    private ThreadPoolExecutor taskExecutor;
//    @ApiSign
//    @RequestMapping("test")
//    public ApiResponse test(){
//        for (int i = 0; i < 1000; i++) {
//            taskExecutor.submit(() -> {
//                try {
//                    Thread.sleep(80);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//        }
//        return ApiResponse.success();
//    }
//}
