package org.caesar.boot.start.async;


import java.util.HashMap;
import java.util.concurrent.*;

/**
 * <p> @author GuoPeng</p>
 * <p> @version 1.0.0</p>
 * <p> @description 异步任务线程池</p>
 * <p> @createTime 2019-03-14 22:53:00</p>
 */
public class DefaultAsyncTaskPool extends AbstractAsyncTaskPool {

    private DefaultAsyncTaskPool() {
        taskMap = new HashMap<>();
        workQueue = new LinkedBlockingQueue<Runnable>();
        pool = new ThreadPoolExecutor(10, 10, 60, TimeUnit.MINUTES, workQueue, new ThreadPoolExecutor.CallerRunsPolicy());
        pool.allowCoreThreadTimeOut(true);
    }

    synchronized public static AbstractAsyncTaskPool getInstance() {
        if (instance == null) {
            instance = new DefaultAsyncTaskPool();
        }
        return instance;
    }

}
