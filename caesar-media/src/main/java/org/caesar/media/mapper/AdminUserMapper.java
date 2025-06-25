package org.caesar.media.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.caesar.media.entity.AdminUser;

/**
 * @Description: 后台用户Mapper接口
 * @Author: peng.guo
 * @Create: 2025-05-27 15:50
 * @Version 1.0
 **/
@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUser> {
}
