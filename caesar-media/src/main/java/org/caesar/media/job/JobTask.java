package org.caesar.media.job;

import org.caesar.media.entity.JobConfig;

public interface JobTask {
    void run(JobConfig jobConfig);
}
