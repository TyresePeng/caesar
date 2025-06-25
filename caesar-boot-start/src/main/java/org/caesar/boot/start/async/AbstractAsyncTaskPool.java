package org.caesar.boot.start.async;


import java.util.Map;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p> @author GuoPeng</p>
 * <p> @version 1.0.0</p>
 * <p> @description 异步任务线程池</p>
 * <p> @createTime 2019-03-14 22:53:00</p>
 */
public abstract class AbstractAsyncTaskPool {

    public static AbstractAsyncTaskPool instance = null;
    public Map<String, AsyncTaskInfo> taskMap;
    public ThreadPoolExecutor pool;
    public LinkedBlockingQueue<Runnable> workQueue = null;

    /**
     * 添加任务
     * @param asyncTask 任务
     * @return 任务id
     */
    public String addTask(AbstractAsyncTask asyncTask) {
        pool.execute(asyncTask.getAsyncTaskRunnable());
        String id = UUID.randomUUID().toString();
        taskMap.put(id, asyncTask.getAsyncTaskInfo());
        return id;
    }

    /**
     * 更新任务
     * @param asyncTaskInfo 任务信息
     * @return 任务id
     */
    public String updateTask(AsyncTaskInfo asyncTaskInfo) {
        String id = asyncTaskInfo.getId();
        taskMap.put(id, asyncTaskInfo);
        return id;
    }


    /**
     * 获取任务信息
     * @param taskId 任务id
     * @return 任务信息
     */
    public AsyncTaskInfo getAsyncTaskInfo(String taskId) {
        if (taskMap.get(taskId) != null) {
            return taskMap.get(taskId);
        }
        return null;
    }

    /**
     * 获取任务列表
     * @return 任务列表
     */
    public Map<String, AsyncTaskInfo> getAsyncTaskMap() {
        return taskMap;
    }

    /**
     * 任务存储释放
     * @param asyncTaskInfo 任务
     */
    public void release(AsyncTaskInfo asyncTaskInfo) {
        pool.execute(() -> taskMap.put(asyncTaskInfo.getId(), asyncTaskInfo));
    }

}
