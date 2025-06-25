package org.caesar.media.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.caesar.common.util.StringUtils;
import org.caesar.media.entity.JobResource;
import org.caesar.media.mapper.JobResourceMapper;
import org.caesar.media.service.JobResourceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobResourceServiceImpl extends ServiceImpl<JobResourceMapper, JobResource> implements JobResourceService {

    @Override
    public List<JobResource> listByJobConfigId(Long jobConfigId) {
        return lambdaQuery()
                .eq(JobResource::getJobConfigId, jobConfigId)
                .list();
    }

    @Override
    public void saveByResourceId(String resourceId, JobResource jobResource) {
        if (StringUtils.isNotBlank(resourceId)) {
            long count = this.count(Wrappers.<JobResource>lambdaQuery().eq(JobResource::getResourceId, resourceId));
            if (count == 0) {
                this.save(jobResource);
            }
        }
    }

}
