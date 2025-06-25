package org.caesar.boot.start.async;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * <p> @author GuoPeng</p>
 * <p> @version 1.0.0</p>
 * <p> @description 异步任务执行</p>
 * <p> @createTime 2019-03-14 22:53:00</p>
 */
@Log4j2
public class AsyncTaskRunnable<T extends AbstractAsyncTaskPool> implements Runnable {

    private final AbstractAsyncTask<T> asyncTask;

    @Getter
    public T taskPool;

    public AsyncTaskRunnable(AbstractAsyncTask<T> asyncTask, T taskPool) {
        this.asyncTask = asyncTask;
        this.taskPool = taskPool;
    }

    @Override
    public void run() {
        try {
            asyncTask.run();
            asyncTask.getAsyncTaskInfo().setStatus(1);
            asyncTask.getAsyncTaskInfo().setMessage("任务执行完成");
            taskPool.updateTask(asyncTask.getAsyncTaskInfo());
            log.info("async task run success,taskId:{}",asyncTask.getAsyncTaskInfo().getId());
            asyncTask.onSuccess();
        } catch (Exception e) {
            asyncTask.getAsyncTaskInfo().setStatus(2);
            asyncTask.getAsyncTaskInfo().setMessage("任务执行失败:" + e.getMessage());
            taskPool.updateTask(asyncTask.getAsyncTaskInfo());
            log.info("async task run error,taskId:{}",asyncTask.getAsyncTaskInfo().getId());
            asyncTask.onError(e);
        } finally {
            taskPool.release(asyncTask.getAsyncTaskInfo());
        }
    }
}
