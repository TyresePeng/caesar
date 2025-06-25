package org.caesar.media.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.caesar.media.entity.JobConfig;
import org.caesar.media.mapper.JobConfigMapper;
import org.caesar.media.service.JobConfigService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 任务配置服务实现类
 */
@Service
public class JobConfigServiceImpl extends ServiceImpl<JobConfigMapper, JobConfig> implements JobConfigService {

    /**
     * 分页查询任务配置，支持根据标题模糊查询，平台编码和任务类型精确查询
     *
     * @param page 分页参数
     * @param jobConfig 查询条件实体
     * @return 分页结果
     */
    @Override
    public IPage<JobConfig> pageJobConfig(Page<JobConfig> page, JobConfig jobConfig) {
        QueryWrapper<JobConfig> queryWrapper = new QueryWrapper<>();

        if (jobConfig != null) {
            if (StringUtils.hasText(jobConfig.getTitle())) {
                queryWrapper.like("title", jobConfig.getTitle());
            }
            if (StringUtils.hasText(jobConfig.getPlatformCode())) {
                queryWrapper.eq("platform_code", jobConfig.getPlatformCode());
            }
            if (StringUtils.hasText(jobConfig.getJobType())) {
                queryWrapper.eq("job_type", jobConfig.getJobType());
            }
        }

        return this.page(page, queryWrapper);
    }

    @Override
    public List<JobConfig> ActiveJobConfigList() {
        return this.list(Wrappers.<JobConfig>lambdaQuery().eq(JobConfig::getStatus, 1));

    }

    @Override
    public void updateStatus(Long id, Integer status) {
        JobConfig jobConfig = new JobConfig();
        jobConfig.setId(id);
        jobConfig.setStatus(status);
        this.updateById(jobConfig);

    }
}
