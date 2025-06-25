package org.caesar.media.job;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.caesar.media.entity.JobConfig;
import org.caesar.media.entity.PlatformUser;
import org.caesar.media.factory.JobFactory;
import org.caesar.media.service.JobConfigService;
import org.caesar.media.service.PlatformUserService;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
@RequiredArgsConstructor
@Log4j2
public class DynamicScheduler {

    private final TaskScheduler taskScheduler = new ConcurrentTaskScheduler();
    private final JobConfigService jobConfigService;
    private final PlatformUserService platformUserService;
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private final JobFactory jobFactory;

    /**
     * 启动时加载
     */
    @PostConstruct
    public void init() {
        refreshTasks();
    }

    public void refreshTasks() {
        List<JobConfig> jobList = jobConfigService.list();
        for (JobConfig job : jobList) {
            rescheduleJob(job);
        }
    }

    public void rescheduleJob(JobConfig job) {
        // 如果任务未启用，取消已有任务并退出
        if (job.getStatus() != 1) {
            ScheduledFuture<?> future = scheduledTasks.remove(job.getId());
            if (future != null) {
                future.cancel(true);
                log.info("任务 [{}] 被取消（未启用状态）", job.getTitle());
            }
            return;
        }

        // 取消旧任务（如果存在）
        ScheduledFuture<?> future = scheduledTasks.remove(job.getId());
        if (future != null) {
            future.cancel(true);
        }

        // 检查是否有账号
        List<PlatformUser> platformUserList = platformUserService.platformActiveUserList();
        if (CollectionUtils.isEmpty(platformUserList)) {
            log.warn("任务 [{}] 未调度：无可用账号", job.getTitle());
            return;
        }

        // 获取并调度新任务
        Runnable runnable = jobFactory.getJob(job);
        if (runnable != null) {
            ScheduledFuture<?> newFuture = taskScheduler.schedule(runnable, new CronTrigger(job.getCron()));
            scheduledTasks.put(job.getId(), newFuture);
            log.info("调度任务 [{}] 使用 cron: {}", job.getTitle(), job.getCron());
        } else {
            log.warn("任务 [{}] 无法调度：获取 Runnable 为 null", job.getTitle());
        }
    }
}
