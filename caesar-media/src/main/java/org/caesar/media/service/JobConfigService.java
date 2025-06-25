package org.caesar.media.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.caesar.media.entity.JobConfig;

import java.util.List;

/**
 * 任务配置服务接口
 */
public interface JobConfigService extends IService<JobConfig> {

    /**
     * 分页查询任务配置
     * @param page 分页参数
     * @param jobConfig 查询条件实体（可选，支持根据标题、平台编码、任务类型筛选）
     * @return 分页结果
     */
    IPage<JobConfig> pageJobConfig(Page<JobConfig> page, JobConfig jobConfig);

    /**
     * 获取所有激活的任务配置列表
     * @return 任务配置列表
     */
   List<JobConfig> ActiveJobConfigList();

    /**
     * 更新任务配置状态
     * @param id 任务配置ID
     * @param status 状态
     */
    void updateStatus(Long id, Integer status);

}
