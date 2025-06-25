package org.caesar.media.factory;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.caesar.media.entity.JobConfig;
import org.caesar.media.job.JobTask;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@RequiredArgsConstructor
@Log4j2
public class JobFactory {

    private final Map<String, JobTask> jobTaskMap;
    public Runnable getJob(JobConfig jobConfig) {
        // 比如 VIDEO_CAPTURE
        String jobType = jobConfig.getJobType();
        JobTask task = jobTaskMap.get(jobType);
        if (task == null) {
            log.error("不支持的任务类型: {}", jobType);
            return null;
        }
        return () -> task.run(jobConfig);
    }
}
