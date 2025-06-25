package org.caesar.test.orm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.caesar.test.orm.entity.CaesarModel;
import org.caesar.test.orm.mapper.CaesarMapper;
import org.caesar.test.orm.service.CaesarService;
import org.springframework.stereotype.Service;

/**
 * <p> @author GuoPeng</p>
 * <p> @version 1.0.0</p>
 * <p> @description orm 测试</p>
 * <p> @createTime 2022-11-13 21:38:00</p>
 */
@Service
public class CaesarServiceImpl extends ServiceImpl<CaesarMapper, CaesarModel> implements CaesarService {
}
