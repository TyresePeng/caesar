package org.caesar.test;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.caesar.test.orm.entity.CaesarModel;
import org.caesar.test.orm.service.CaesarService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class CaesarTestApplicationTests {

    @Resource
    private CaesarService caesarService;

    @Test
    void contextLoads() {
        List<CaesarModel> list = caesarService.list(Wrappers.<CaesarModel>lambdaQuery().eq(CaesarModel::getOrderId, 2321232));
        System.err.println(list);

    }

}
