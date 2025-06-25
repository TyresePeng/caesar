package org.caesar.media.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.caesar.media.entity.JobResource;

import java.util.List;

public interface JobResourceService extends IService<JobResource> {
    /**
     * 根据任务配置ID获取资源列表
     */
    List<JobResource> listByJobConfigId(Long jobConfigId);

    /**
     * 根据资源ID保存任务资源
     */
    void saveByResourceId(String resourceId, JobResource jobResource);

}
