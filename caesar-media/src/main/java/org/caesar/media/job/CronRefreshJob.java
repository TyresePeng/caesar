package org.caesar.media.job;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CronRefreshJob {

    private final DynamicScheduler dynamicScheduler;

    /**每 30 秒刷新一次*/
    @Scheduled(cron = "${cron-refresh-job.cron}")
    public void refreshCron() {
        dynamicScheduler.refreshTasks();
    }
}
