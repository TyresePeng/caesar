package org.caesar.media.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.caesar.media.entity.PlatformUser;

/**
 * 平台用户 Mapper 接口
 * 继承 BaseMapper 提供基本 CRUD 操作
 */
@Mapper
public interface PlatformUserMapper extends BaseMapper<PlatformUser> {

}
