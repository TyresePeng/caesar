package org.caesar.test.orm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.caesar.test.orm.entity.CaesarModel;

/**
 * <p> @author GuoPeng</p>
 * <p> @version 1.0.0</p>
 * <p> @description orm 测试</p>
 * <p> @createTime 2022-11-13 21:32:00</p>
 */
@Mapper
public interface CaesarMapper extends BaseMapper<CaesarModel> {
}
