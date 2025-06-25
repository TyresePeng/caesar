package org.caesar.boot.start.async;

import lombok.Getter;

/**
 * <p> @author GuoPeng</p>
 * <p> @version 1.0.0</p>
 * <p> @description 异步任务抽象方法</p>
 * <p> @createTime 2019-03-14 22:53:00</p>
 */
public abstract class AbstractAsyncTask<T extends AbstractAsyncTaskPool> {

    @Getter
    public T taskPool;

    /**
     * 异步任务信息
     */
    @Getter
    private AsyncTaskInfo asyncTaskInfo;

    /**
     * 异步任务
     */
    @Getter
    private AsyncTaskRunnable<T> asyncTaskRunnable;

    public AbstractAsyncTask(T taskPool) {
        asyncTaskRunnable = new AsyncTaskRunnable<T>(this, taskPool);
        this.taskPool = taskPool;
    }

    /**
     * 异步任务执行
     */
    public AsyncTaskInfo execute() {
        asyncTaskInfo = new AsyncTaskInfo();
        asyncTaskInfo.setStatus(0);
        asyncTaskInfo.setMessage("任务执行中");
        String taskId = taskPool.addTask(this);
        asyncTaskInfo.setId(taskId);
        return this.asyncTaskInfo;
    }

    /**
     * 异步任务内容
     *
     * @throws Exception
     */
    public abstract void run() throws Exception;

    /**
     * 任务执行成功后内容
     */
    public void onSuccess(){};

    /**
     * 任务执行失败后内容
     */
    public void onError(Exception e){};

}
