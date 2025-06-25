package org.caesar.media.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.caesar.media.entity.JobConfig;

/**
 * 任务配置 Mapper 接口，继承 MyBatis-Plus BaseMapper，提供CRUD操作
 */
@Mapper
public interface JobConfigMapper extends BaseMapper<JobConfig> {
}
